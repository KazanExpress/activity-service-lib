package com.kazanexpress.activity.aspect;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

@AllArgsConstructor(staticName = "of")
@Data
class FieldObject {

    private final Field field;
    private final Object target;
}
