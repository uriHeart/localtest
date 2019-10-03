package com.argo.data.service;

import com.argo.common.domain.common.data.ConvertibleData;
import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplateService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.util.ClassUtils;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Entity
public class DataConversionService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private static JsonParser jsonParser;

    @Autowired
    ConversionTemplateService conversionTemplateService;

    @Autowired
    DataConversionHandler dataConversionHandler;

    public void convert(ConvertibleData sourceData) {
        Map<String, ConversionTemplate> conversionTemplateMap = conversionTemplateService.getConversionTemplateMaps(sourceData.sourceKey());
        conversionTemplateMap.forEach((targetId, template) -> convert(sourceData, targetId, template));
    }

    public void convert (ConvertibleData sourceData, String targetId, ConversionTemplate template) {
        Class targetClass = getClassWithName(targetId);
        convert(sourceData, targetClass, template);
    }

    public void convert(ConvertibleData sourceData, Class targetClass, ConversionTemplate template) {
        Object targetInstance = ClassUtils.newInstance(targetClass);
        String json = convertibleDataToJsonString(sourceData);
        JsonNode jsonNode = jsonStringToJsonObject(json, sourceData);
        List<ConversionRule> conversionRules = template.getRules();
        conversionRules.stream().forEach(rule -> applyConversionRule(rule, jsonNode, targetInstance));

    }


    private void applyConversionRule(ConversionRule conversionRule, JsonNode jsonNode, Object target) {
        conversionRule.getTargetField();
    }
    private JsonNode jsonStringToJsonObject(String json, ConvertibleData sourceData) {
        JsonNode jsonNode = null;
        // either parse json or create schema using Jsonschema2Pojo
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
