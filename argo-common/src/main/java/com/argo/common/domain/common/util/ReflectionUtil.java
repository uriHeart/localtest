package com.argo.common.domain.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ReflectionUtil {
    public static Method getConversionMethod(String className, String methodName) {
        Method method = null;
        try {
            Class clazz = Class.forName(className);
            method = clazz.getMethod(methodName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    public static List<Method> getPublicMethods(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.warn(MessageFormat.format("Cannot find class with : {1}", className));
        }
        return Arrays.asList(clazz.getMethods());
    }

    public static Object createObjectWithNoArgConstructor(String className) {
        Object result = null;
        try {
            result = Class.forName(className).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e ) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean setFieldValue(Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        if(clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    public static List<String> getAllFieldsForClass(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.warn(MessageFormat.format("Cannot find class with : {1}", className));
        }
        return FieldUtils.getAllFieldsList(clazz).stream().map(Field::getName).collect(Collectors.toList());
    }
}
