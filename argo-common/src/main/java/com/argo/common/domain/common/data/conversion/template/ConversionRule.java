package com.argo.common.domain.common.data.conversion.template;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@UserDefinedType("conversion_rule")
public class ConversionRule {
    private String sourceField;

    private String targetField;

    @Enumerated(EnumType.STRING)
    private ConversionType conversionType;

    private String operatorClass;

    private String operatorMethod;

    private Map<String, String> jsonMap;

    private Map<String, String> operatorParams;

    private String sqlString;
}
