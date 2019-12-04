package com.argo.api.controller.convert;

import com.argo.common.domain.common.data.conversion.template.ConversionType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Map;

public class ConversionRuleForm {
    private String sourceField;
    private String targetField;

    @Enumerated(EnumType.STRING)
    private ConversionType conversionType;

    private String operatorClass;
    private String operatorMethod;
    private Map<String, String> operatorParams;

    private Map<String, String> jsonMap;

    private String conversionTemplateSourceId;
    private String conversionTemplateTargetId;
    private String sqlString;
}
