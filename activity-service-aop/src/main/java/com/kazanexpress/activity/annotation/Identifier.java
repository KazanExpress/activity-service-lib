package com.kazanexpress.activity.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark the field as an identifier for an {@link ActivityRecord}
 * Identifiers are also used for string substitutions for {@link ActivityRecord#message()}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Identifier {

    /**
     * Alias for {@link #name()}
     */
    @AliasFor("name")
    String value() default "";

    /**
     * Set a name for the identifier which will be exported into {@link ActivityRecord}
     */
    @AliasFor("value")
    String name() default "";

    boolean exported() default true;
}
