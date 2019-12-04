package com.argo.common.domain.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class ConversionUtil {
    public static Object convertStringToPrimitiveObject(Class clazz, String value) {
        if(clazz.equals(String.class)) {
            return value.toString();
        } else if(clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return Boolean.valueOf(value);
        } else if(clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return Integer.valueOf(StringUtils.getDigits(value));
        } else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
            return Long.valueOf(StringUtils.getDigits(value));
        } else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
            return Double.valueOf(StringUtils.getDigits(value));
        } else if(clazz.equals(Float.class) || clazz.equals(float.class)) {
            return Float.valueOf(StringUtils.getDigits(value));
        } else if(clazz.equals(Short.class) || clazz.equals(short.class)) {
            return Short.valueOf(StringUtils.getDigits(value));
        } else if(clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            return Byte.valueOf(value);
        } else if(clazz.equals(Character.class) || clazz.equals(char.class)) {
            return value.indexOf(0);
        } else if(clazz.equals(Date.class)) {
            return ArgoDateUtil.parseDateString(value);
        }
        return null;
    }

    public static Object getJsonValueWithType(JsonNode jsonNode, Class clazz) {
        if(jsonNode == null) {
            return null;
        }
        if(clazz.equals(String.class)) {
            if(jsonNode.isTextual()) {
                return jsonNode.asText();
            }
        } else if(clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            if(jsonNode.isBoolean()) {
                return jsonNode.asBoolean();
            }
        } else if(clazz.equals(Integer.class) || clazz.equals(int.class)) {
            if(jsonNode.isInt()) {
                return jsonNode.asInt();
            } else {
                return Integer.valueOf(jsonNode.asText());
            }
        } else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
            if(jsonNode.isNumber()) {
                return jsonNode.asLong();
            } else {
                try {
                    return Long.valueOf(jsonNode.asText().replaceAll("[^\\d.]", ""));
                } catch (Exception e) {
                    return 0L;
                }
            }
        } else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
            if(jsonNode.isDouble()) {
                return jsonNode.asDouble();
            } else {
                return Double.valueOf(jsonNode.asText());
            }
        } else if(clazz.equals(Date.class)) {
            String dateString = jsonNode.asText();
            return ArgoDateUtil.parseDateString(dateString);
        }

        return jsonNode.asText();
    }

    public static Object[] getOperatorParamsValues(Class[] classes, String[] values) {
        if(classes.length != values.length) {
            throw new IndexOutOfBoundsException("classes array length : " + classes.length + " and values array lenght : " + values.length + " are different");
        }
        Object[] result = new Object[values.length];
        for(int i = 0; i < classes.length; i++) {
            Class clazz = classes[i];
            String value = values[i];
            result[i] = ConversionUtil.convertStringToPrimitiveObject(clazz, value);
        }
        return result;
    }
}
