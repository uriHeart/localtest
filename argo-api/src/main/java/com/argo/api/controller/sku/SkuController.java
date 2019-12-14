package com.argo.api.controller.sku;

import com.argo.api.dto.SkuDto;
import com.argo.common.domain.sku.SkuService;
import com.argo.common.domain.sku.SkuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
public class SkuController {
    private SkuService skuService;

    @Autowired
    public SkuController(SkuService skuService) {
        this.skuService = skuService;
    }

    @GetMapping("/skuList/{vendorId}")
    public List<SkuDto> getSkuList(@PathVariable("vendorId") Long vendorId) {
        List<SkuVo> skuList = skuService.findByVendorId(vendorId);

        return skuList.stream()
                .map(SkuDto::new)
                .collect(Collectors.toList());
    }
}
