package com.kazanexpress.activity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
@Inherited
public @interface NestedIdentifier {

    NameOverride[] nameOverrides() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
    @Inherited
    @interface NameOverride {

        String from();

        String to();
    }
}
