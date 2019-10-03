package com.argo.common.domain.common.data.conversion.template;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConversionTemplateService {
    @Autowired
    private ConversionTemplateRepository conversionTemplateRepository;

    public Map<String, ConversionTemplate> getConversionTemplateMaps(String sourceId) {
        List<ConversionTemplate> templates = conversionTemplateRepository.findAllBySourceId(sourceId);
        Map<String, ConversionTemplate> map = templates
                .stream()
                .collect(Collectors.toMap(ConversionTemplate::getTargetId, Function.identity()));
        return map;
    }

    public void save(ConversionTemplate conversionTemplate) {
        conversionTemplateRepository.save(conversionTemplate);
    }

}
