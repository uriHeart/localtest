package com.argo.common.domain.common.data;

import ch.qos.logback.core.boolex.EvaluationException;
import com.argo.common.domain.common.data.conversion.template.ConversionRule;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplateService;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.argo.common.domain.common.util.ConversionUtil;
import com.argo.common.domain.common.util.ReflectionUtil;
import com.argo.common.domain.order.PaymentType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.objenesis.instantiator.util.ClassUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataConversionService implements ApplicationContextAware {

    private ApplicationContext appContext;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JsonParser jsonParser;

    @Autowired
    private ConversionTemplateService conversionTemplateService;

    public Map<String, Object> convert(SourceData sourceData) {
        Map<String, ConversionTemplate> conversionTemplateMap = conversionTemplateService.getRawEventConversionTemplateMap(sourceData);
        Map<String, Object> result = conversionTemplateMap.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> convertWithTemplate(sourceData, e.getKey(), e.getValue())
                ));
        return result;
    }

    public Object convertWithTemplate(SourceData sourceData, String targetId, ConversionTemplate template) {
        Class targetClass = getClassWithName(targetId);
        Object object = convert(sourceData, targetClass, template);
        return object;
    }

    public Object convert(SourceData sourceData, Class targetClass, ConversionTemplate template) {
        String json = convertibleDataToJsonString(sourceData);
        JsonNode jsonNode = jsonStringToJsonObject(json, sourceData);
        List<ConversionRule> conversionRules = template.getRules();
        if(template.getListReference() == null || template.getListReference().isEmpty()) {
            Object targetInstance = ClassUtils.newInstance(targetClass);
            conversionRules.stream().forEach(rule -> applyConversionRule(rule, jsonNode, targetInstance));
            return targetInstance;
        } else {
            List list = Lists.newArrayList();
            String listRef = template.getListReference();
            ObjectNode originalObjNode = (ObjectNode) jsonNode;
            JsonNode arrNode = jsonNode.findValue(template.getListReference());
            originalObjNode.findParent(listRef).remove(listRef);
            if(arrNode.isArray()) {
                for (JsonNode node : arrNode) {
                    ObjectNode objNode = (ObjectNode) node;
                    objNode.set("originalData", originalObjNode);
                    Object targetInstance = ClassUtils.newInstance(targetClass);
                    conversionRules.stream().forEach(rule -> applyConversionRule(rule, objNode, targetInstance));
                    list.add(targetInstance);
                }
            } else {
                throw new IllegalStateException("List Reference : " + template.getListReference() + " is not an array format");
            }
            return list;
        }
    }

    public List<String> getMethods(String invokingClass) {
        Object invokingClassBean = appContext.getBean(invokingClass);
        return Arrays.stream(invokingClassBean.getClass().getDeclaredMethods()).map(Method::getName).collect(Collectors.toList());
    }

    public Object convert(JsonNode jsonNode, Class targetClass, ConversionTemplate template) {
        Object targetInstance = ClassUtils.newInstance(targetClass);
        List<ConversionRule> conversionRules = template.getRules();
        conversionRules.stream().forEach(rule -> applyConversionRule(rule, jsonNode, targetInstance));
        return targetInstance;
    }

    private Object getFieldValue(ConversionRule conversionRule, JsonNode jsonNode, Object target) {
        Object fieldValue = null;
        Field targetField = null;
        Class fieldType = null;


        try {
            targetField = target.getClass().getDeclaredField(conversionRule.getTargetField());
            fieldType = targetField.getType();
            fieldValue = ConversionUtil.getJsonValueWithType(jsonNode, fieldType);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }
    private void applyConversionRule(ConversionRule conversionRule, JsonNode jsonNode, Object target) {
        ConversionType conversionType = conversionRule.getConversionType();
        Object targetValue = null;
        Object fieldValue = null;
        Class fieldType = null;
        try {
            fieldType = target.getClass().getDeclaredField(conversionRule.getTargetField()).getType();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        switch (conversionType) {
            case DIRECT:
                // default so do nothing for now
                fieldValue = getFieldValue(conversionRule, jsonNode.findValue(conversionRule.getSourceField()), target);
                targetValue = fieldValue;
                break;
            case SQL:
                fieldValue = getFieldValue(conversionRule, jsonNode.findValue(conversionRule.getSourceField()), target);
                targetValue = getTargetValueBySql((String) fieldValue, conversionRule.getSqlString());
                break;
            case OPERATION:
                List<String> values = Lists.newArrayList(conversionRule.getOperatorParams().keySet())
                        .stream().map(value -> {
                            if(jsonNode.findValue(value)==null){
                                log.error("node data not found. value:"+ value);
                            }
                           return jsonNode.findValue(value).asText();
                        })
                        .collect(Collectors.toList());
                Class[] paramClasses = conversionRule.getOperatorParamsAsClasses();
                Object[] paramValues = ConversionUtil.getOperatorParamsValues(paramClasses, values.toArray(new String[0]));
                targetValue = getTargetValueByInvocation(conversionRule.getOperatorClass(), conversionRule.getOperatorMethod(), paramClasses, paramValues);
                break;
            case AGGREGATE:
                Double doubleSum = jsonNode.findValues(conversionRule.getSourceField())
                        .stream()
                        .mapToDouble(node -> Double.valueOf(StringUtils.isEmpty(node.asText())?"0":node.asText()))
                        .sum();

                if(fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                    targetValue = doubleSum.longValue();
                } else if(fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                    targetValue = doubleSum.intValue();
                }
                break;
            case JSON:
                break;
            case CUSTOM_INPUT:
                String className = conversionRule.getCustomType();
                Class customClass = null;
                try {
                    customClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    log.error(MessageFormat.format("Class not found during CUSTOM_INPUT Conversion for : {1}", className));
                }
                if(customClass.isEnum()) {
                    try {
                        targetValue = customClass.getMethod("valueOf", String.class).invoke(null, conversionRule.getCustomValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    targetValue = ConversionUtil.convertStringToPrimitiveObject(customClass, conversionRule.getCustomValue());
                }
                break;
            case CONVERSION_TEMPLATE:
                ConversionTemplate conversionTemplate = conversionTemplateService.conversionTemplateBySourceIdAndTargetId(conversionRule.getConversionTemplateSourceId(), conversionRule.getConversionTemplateTargetId());//getConversionTemplate(conversionRule.getConversionTemplateSourceId(), conversionRule.getConversionTemplateTargetId());
                targetValue = convert(jsonNode, getClassWithName(conversionTemplate.getTargetId()), conversionTemplate);
                if(fieldType.equals(String.class)) {
                    try {
                        targetValue = mapper.writeValueAsString(targetValue);
                    } catch (JsonProcessingException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("Not Supported Conversion Type");
        }
        ReflectionUtil.setFieldValue(target, conversionRule.getTargetField(), targetValue);
    }


    private Object getTargetValueBySql(String sourceValue, String sql) {
        return null;
    }

    private Object getTargetValueByInvocation(String invokingClass, String invokingMethod, Class[] sourceTypes, Object[] sourceValues) {
        Object result = null;
        try {
            Object invokingClassBean = appContext.getBean(invokingClass);
            Method method = invokingClassBean.getClass().getMethod(invokingMethod, sourceTypes);
            result = method.invoke(invokingClassBean, sourceValues);
        } catch (NoSuchMethodException e) {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < sourceTypes.length; i++) {
                sb.append(sourceTypes[i].getName() + " " + sourceValues[i].toString() + " ");
            }
            log.error("Method not found for class and values : " + sb.toString());
            e.printStackTrace();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JsonNode jsonStringToJsonObject(String json, SourceData sourceData) {
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

    private String convertibleDataToJsonString(SourceData sourceData) {
        String json = "";
        try {
            Class clazz = Class.forName(sourceData.className());
            json = mapper.writeValueAsString(clazz.cast(sourceData));
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

    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }
}
