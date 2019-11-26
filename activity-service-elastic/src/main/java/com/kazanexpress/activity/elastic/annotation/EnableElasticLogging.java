package com.kazanexpress.activity.elastic.annotation;


import com.kazanexpress.activity.elastic.ElasticActivityConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(ElasticActivityConfiguration.class)
public @interface EnableElasticLogging {
}
