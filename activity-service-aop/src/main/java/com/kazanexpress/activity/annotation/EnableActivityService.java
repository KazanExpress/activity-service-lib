package com.kazanexpress.activity.annotation;


import com.kazanexpress.activity.config.ActivityConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(ActivityConfiguration.class)
public @interface EnableActivityService {
}
