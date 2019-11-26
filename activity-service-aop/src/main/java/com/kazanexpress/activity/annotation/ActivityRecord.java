package com.kazanexpress.activity.annotation;

import org.apache.commons.text.StringSubstitutor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark method as an activity record producer.
 *
 * The method has to return value which should be non null.
 *
 * Returned value type may contain {@link Identifier}
 * to enhance the activity record.
 *
 * @see Identifier
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface ActivityRecord {

    /**
     * Message with which the activity record will be created.
     *
     * Message may contain placeholders which will be resolved through
     * {@link Identifier} annotations.
     *
     * @see StringSubstitutor
     * @see Identifier
     */
    String message();

    /**
     * The record type
     */
    String type();

    /**
     * Identifiers from the {@link RequestContextHolder}
     * with the scope {@link RequestAttributes#REFERENCE_REQUEST}
     *
     * The name of the parameter will be treated as the identifier for the value
     */
    String[] requestParams() default {};

    /**
     * Nested Identifiers from the {@link RequestContextHolder}
     * with the scope {@link RequestAttributes#REFERENCE_REQUEST}
     */
    String[] nestedRequestParams() default {};
}
