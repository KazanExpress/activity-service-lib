package com.kazanexpress.activity.aspect;

import com.kazanexpress.activity.annotation.ActivityRecord;
import com.kazanexpress.activity.annotation.Identifier;
import com.kazanexpress.activity.annotation.NestedIdentifier;
import com.kazanexpress.activity.model.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * Class to process methods which are marked by {@link ActivityRecord} annotation
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class AspectProcessor {

    private final ApplicationEventPublisher eventPublisher;

    @Pointcut("execution(@(@com.kazanexpress.activity.annotation.ActivityRecord *) * *(..)) && execution(* *(..))")
    public void nestedPointCut() {
        // Pointcut expression
    }

    @Pointcut("@annotation(com.kazanexpress.activity.annotation.ActivityRecord)")
    private void directPointcut() {
        // Pointcut expression
    }

    /**
     * Aspect processor fom the methods marked {@link ActivityRecord}
     *
     * @param joinPoint      join point for the {@link Around} annotation
     */
    @Around("nestedPointCut() || directPointcut()")
    public Object afterAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ActivityRecord activityRecord = requireNonNull(AnnotationUtils.getAnnotation(method, ActivityRecord.class));

        log.debug("Start processing annotation: {}", activityRecord);
        Object retVal = joinPoint.proceed();

        List<Object> nestedIdentifiers = new ArrayList<>();
        List<ParameterObject> parameterObjects = new ArrayList<>();
        addAnnotatedParams(joinPoint, nestedIdentifiers, parameterObjects);

        processInvocation(activityRecord, retVal, nestedIdentifiers, parameterObjects);

        return retVal;
    }

    private void addAnnotatedParams(ProceedingJoinPoint joinPoint,
                                    List<Object> nestedIdentifiers,
                                    List<ParameterObject> parameterObjects) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            for (Annotation argAnnotation : parameterAnnotations[i]) {
                if (argAnnotation instanceof NestedIdentifier) {
                    nestedIdentifiers.add(arg);
                } else if (argAnnotation instanceof Identifier) {
                    parameterObjects.add(ParameterObject.of(method.getParameters()[i], arg));
                }
            }
        }
    }

    private void processInvocation(@NonNull ActivityRecord activityRecord,
                                   @Nullable Object retVal,
                                   List<Object> nestedIdentifiers,
                                   List<ParameterObject> parameterObjects) {
        Map<String, List<Object>> idMap = new HashMap<>();
        Map<String, Object> subsMap = new HashMap<>();
        for (Object argumentIdentifier : nestedIdentifiers) {
            addIdentifiersForObject(argumentIdentifier, idMap, subsMap);
        }
        for (ParameterObject parameterObject : parameterObjects) {
            if (parameterObject == null) {
                continue;
            }
            idMap.putAll(getIdentifiersMap(parameterObject, true));
            subsMap.putAll(flattenMap(getIdentifiersMap(parameterObject, false)));
        }

        for (String nestedIdName : activityRecord.nestedRequestParams()) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                break;
            }
            Object nestedIdentifier = requestAttributes.getAttribute(nestedIdName, SCOPE_REQUEST);
            addIdentifiersForObject(nestedIdentifier, idMap, subsMap);
        }
        for (String key : activityRecord.requestParams()) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                break;
            }
            Object value = requestAttributes.getAttribute(key, SCOPE_REQUEST);
            addValue(idMap, key, value);
            subsMap.put(key, value);
        }
        addIdentifiersForObject(retVal, idMap, subsMap);

        Record event = Record.builder()
                .id(UUID.randomUUID().toString())
                .timestamp(new Date())
                .message(StringSubstitutor.replace(activityRecord.message(), subsMap))
                .type(activityRecord.type())
                .identifiers(idMap)
                .build();

        log.debug("Produced record: {}", event);
        eventPublisher.publishEvent(event);
    }

    private void addIdentifiersForObject(@Nullable Object object,
                                         Map<String, List<Object>> idMap,
                                         Map<String, Object> subsMap) {
        if (object == null) {
            return;
        }
        idMap.putAll(getIdentifiersMap(object, true));
        subsMap.putAll(getSubstitutionMap(object));
    }

    private Map<String, List<Object>> getIdentifiersMap(ParameterObject parameterObject,
                                                        boolean onlyExported) {
        Map<String, List<Object>> result = new HashMap<>();
        Parameter parameter = parameterObject.getParameter();
        Identifier annotation = AnnotationUtils.findAnnotation(parameter, Identifier.class);
        if (annotation == null) {
            return result;
        }
        if (annotation.exported() || !onlyExported) {
            String identifier = extractAnnotationName(parameter, annotation.name());
            Object value = parameterObject.getValue();
            addValue(result, identifier, value);
        }
        return result;
    }

    private Map<String, Object> getSubstitutionMap(@NonNull Object o) {
        Map<String, List<Object>> result = getIdentifiersMap(o, false);
        return flattenMap(result);
    }

    private Map<String, Object> flattenMap(Map<String, List<Object>> result) {
        Map<String, Object> refinedResult = new HashMap<>();
        for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
            List<Object> values = entry.getValue();
            if (values.size() > 1) {
                refinedResult.put(entry.getKey(), values);
            } else {
                refinedResult.put(entry.getKey(), values.get(0));
            }
        }
        return refinedResult;
    }

    private Map<String, List<Object>> getIdentifiersMap(@NonNull Object o,
                                                        boolean onlyExported) {
        Map<String, List<Object>> result = new HashMap<>();
        List<FieldObject> fields = getFieldsRecursively(o.getClass(), o);
        for (FieldObject fieldObject : fields) {
            Field field = fieldObject.getField();
            Identifier annotation = AnnotationUtils.findAnnotation(field, Identifier.class);
            if (annotation == null) {
                continue;
            }
            if (annotation.exported() || !onlyExported) {
                String identifier = extractAnnotationName(field, annotation.name());
                Object value = getFieldValue(fieldObject.getTarget(), field);
                addValue(result, identifier, value);
            }
        }
        return result;
    }

    private void addValue(Map<String, List<Object>> result,
                          String identifier,
                          Object value) {
        result.putIfAbsent(identifier, new ArrayList<>());
        List<Object> list = result.get(identifier);
        list.add(value);
    }

    private List<FieldObject> getFieldsRecursively(Class<?> aClass,
                                                   Object o) {
        return getFieldsRecursively(aClass, 0, o);
    }

    private List<FieldObject> getFieldsRecursively(Class<?> aClass,
                                                   int currentDepth,
                                                   Object o) {
        ArrayList<FieldObject> allFields = new ArrayList<>();
        if (o == null) {
            return allFields;
        }
        ArrayList<Field> fields = new ArrayList<>();
        ReflectionUtils.doWithFields(aClass, fields::add);

        for (Field field : fields) {
            NestedIdentifier annotation = AnnotationUtils.findAnnotation(field, NestedIdentifier.class);
            if (annotation != null) {
                Object fieldValue = getFieldValue(o, field);
                if (fieldValue instanceof Iterable<?>) {
                    for (Object item : (Iterable<?>) fieldValue) {
                        allFields.addAll(getFieldsRecursively(item.getClass(), currentDepth + 1, item));
                    }
                } else {
                    allFields.addAll(getFieldsRecursively(field.getType(), currentDepth + 1, fieldValue));
                }
            } else {
                allFields.add(FieldObject.of(field, o));
            }
        }
        return allFields;
    }

    /**
     * Extracts annotation name based on defaultValue.
     *
     * @param field        the field marked with annotation
     * @param defaultValue default value for annotation
     * @return the field name If defaultValue is empty
     */
    private String extractAnnotationName(Field field,
                                         String defaultValue) {
        if (defaultValue.isEmpty()) {
            return field.getName();
        }
        return defaultValue;
    }

    /**
     * Extracts annotation name based on defaultValue.
     *
     * @param parameter    the parameter marked with annotation
     * @param defaultValue default value for annotation
     * @return the field name If defaultValue is empty
     */
    private String extractAnnotationName(Parameter parameter,
                                         String defaultValue) {
        if (defaultValue.isEmpty()) {
            return parameter.getName();
        }
        return defaultValue;
    }

    /**
     * Returns field value for certain object
     */
    private Object getFieldValue(Object o,
                                 Field field) {
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, o);
    }

}
