package com.argo.common.domain.common.data;

import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplateService;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.argo.common.domain.common.util.ReflectionUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.objenesis.instantiator.util.ClassUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Service
public class DataConversionService {

    @Autowired
    private WebApplicationContext appContext;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private static JsonParser jsonParser;

    @Autowired
    ConversionTemplateService conversionTemplateService;

    @Autowired
    DataConversionHandler dataConversionHandler;

    public void convert(ConvertibleData sourceData) {
        Map<String, ConversionTemplate> conversionTemplateMap = conversionTemplateService.getTestTemplate(sourceData);
        conversionTemplateMap.forEach((targetId, template) -> {
            convertWithTemplate(sourceData, targetId, template);
        });
    }

    private void saveToRepository(Object entity) {
        Repositories repositories = new Repositories(appContext);
        repositories.getRepositoryFor(entity.getClass())
                .ifPresent(repository -> ((CrudRepository) repository).save(entity));
    }
    public Object convertWithTemplate(ConvertibleData sourceData, String targetId, ConversionTemplate template) {
        Class targetClass = getClassWithName(targetId);
        return convert(sourceData, targetClass, template);
    }

    public Object convert(ConvertibleData sourceData, Class targetClass, ConversionTemplate template) {
        Object targetInstance = ClassUtils.newInstance(targetClass);
        String json = convertibleDataToJsonString(sourceData);
        JsonNode jsonNode = jsonStringToJsonObject(json, sourceData);
        List<ConversionRule> conversionRules = template.getRules();
        conversionRules.stream().forEach(rule -> applyConversionRule(rule, jsonNode, targetInstance));
        return targetInstance;
    }

    private Object getJsonValueWithType(JsonNode jsonNode, Class clazz) {
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
            }
        } else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
            if(jsonNode.isLong()) {
                return jsonNode.asLong();
            }
        } else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
            if(jsonNode.isDouble()) {
                return jsonNode.asDouble();
            }
        }

        return null;
    }

    private void applyConversionRule(ConversionRule conversionRule, JsonNode jsonNode, Object target) {
        ConversionType conversionType = conversionRule.getConversionType();
        Object fieldValue = null;
        Field targetField = null;
        Class fieldType = null;
        Object targetValue = null;

        try {
            targetField = target.getClass().getDeclaredField(conversionRule.getTargetField());
            fieldType = targetField.getType();
            fieldValue = fieldType.cast(getJsonValueWithType(jsonNode.findValue(conversionRule.getSourceField()), fieldType));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        switch (conversionType) {
            case DIRECT:
                // default so do nothing for now
                break;
            case SQL:
                targetValue = getTargetValueBySql((String) fieldValue, conversionRule.getSqlString());
                break;
            case OPERATION:
                targetValue = getTargetValueByInvocation(fieldValue, fieldType, conversionRule.getOperatorClass(), conversionRule.getOperatorMethod());
                break;
            default:
                throw new UnsupportedOperationException("Not Supported Conversion Type");
        }

        ReflectionUtil.setFieldValue(target, targetField.getName(), targetValue);
    }


    private Object getTargetValueBySql(String sourceValue, String sql) {
        return null;
    }

    private Object getTargetValueByInvocation(Object sourceValue, Class sourceType, String invokingClass, String invokingMethod) {
        Object result = null;
        try {
            Method method = appContext.getBean(invokingClass).getClass().getMethod(invokingMethod, sourceType);
            result = method.invoke(sourceValue);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JsonNode jsonStringToJsonObject(String json, ConvertibleData sourceData) {
        JsonNode jsonNode = null;

        try {
            JsonObject node = jsonParser.parse(json).getAsJsonObject();
            Class clazz = Class.forName(sourceData.className());
            List<Field> jsonFields = findJsonProperties(clazz);

            // check for fields with json string and merge them to the json tree root
            jsonFields.forEach(field -> node.add(field.getName(), jsonParser.parse(node.get(field.getName()).getAsString())));

            jsonNode = mapper.readTree(node.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

    private String convertibleDataToJsonString(ConvertibleData convertibleData) {
        String json = "";
        try {
            Class clazz = Class.forName(convertibleData.className());
            json = mapper.writeValueAsString(clazz.cast(convertibleData));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    private Class getClassWithName(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    private List<Field> findJsonProperties(Class clazz) {
        return FieldUtils.getFieldsListWithAnnotation(clazz, JsonProperty.class);
    }
}
