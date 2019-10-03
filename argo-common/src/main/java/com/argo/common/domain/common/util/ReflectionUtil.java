package com.argo.common.domain.common.util;

import java.lang.reflect.Method;

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


    public static Object createObjectWithNoArgConstructor(String className) {
        Object result = null;
        try {
            result = Class.forName(className).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e ) {
            e.printStackTrace();
        }
        return result;
    }
}
