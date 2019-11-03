package com.argo.api.controller.convert;

import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@AllArgsConstructor
public class ConversionTemplateController {

    private ConversionTemplateService conversionTemplateService;

    @PostMapping("/conversionTemplate")
    public void addConversionTemplate(@RequestParam String targetid, @RequestParam String sourceId,
                               @RequestParam Long expiredAt, @RequestParam String vendorItemId) {

        ConversionTemplate conversionTemplate = ConversionTemplate.builder()
                .targetId(targetid)
                .sourceId(sourceId)
                .expiredAt(new Date(expiredAt))
                .createdAt(new Date())
                .build();
        conversionTemplateService.save(conversionTemplate);
    }
}
