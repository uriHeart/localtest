package com.argo.common.domain.common;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        if (CollectionUtils.isEmpty(stringList)) {
            return null;
        }
        return String.join(SPLIT_CHAR, stringList);
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        if (StringUtils.isEmpty(string)) {
            return Collections.emptyList();
        }
        return Arrays.asList(string.split(SPLIT_CHAR));
    }
}
