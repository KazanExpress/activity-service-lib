package com.kazanexpress.activity.aspect;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Parameter;

@Data
@AllArgsConstructor(staticName = "of")
public class ParameterObject {

    private final Parameter parameter;
    private final Object value;

}
