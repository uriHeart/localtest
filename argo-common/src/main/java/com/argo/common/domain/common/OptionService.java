package com.argo.common.domain.common;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@ConversionOperationService
public class OptionService {

    @ConversionOperationMethod
    public String extractOptions(String menu) {
        if (StringUtils.isEmpty(menu) || !menu.contains("-")) {
            return "";
        }

        return Arrays.stream(menu.split("-"))
            .skip(1)
            .map(String::trim)
            .collect(Collectors.joining(" "));
    }
}
