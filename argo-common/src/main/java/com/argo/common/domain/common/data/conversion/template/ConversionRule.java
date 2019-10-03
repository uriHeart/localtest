package com.argo.common.domain.common.data.conversion.template;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@UserDefinedType("conversion_rule")
public class ConversionRule {

    private String targetField;

    private String sourceField;

    @Enumerated(EnumType.STRING)
    private ConversionType conversionType;

    private String operatorClass;

    private String operatorMethod;

    private String sql;
}
