package com.argo.common.domain.common.data.conversion.template;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private Map<String, String> operatorParams;

    private Map<String, String> jsonMap;

    private String conversionTemplateSourceId;
    private String conversionTemplateTargetId;



    private String sqlString;

    public Class[] getOperatorParamsAsClass() {
        List<Class> classes = ClassUtils.convertClassNamesToClasses(new ArrayList(this.operatorParams.keySet()));
        return classes.toArray(new Class[0]);
    }
}
