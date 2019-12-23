package com.argo.api.controller.sku;

import com.argo.api.dto.SkuDto;
import com.argo.common.domain.sku.SkuService;
import com.argo.common.domain.sku.SkuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/sku")
@RestController
public class SkuController {
    private SkuService skuService;

    @Autowired
    public SkuController(SkuService skuService) {
        this.skuService = skuService;
    }

    @GetMapping(path = "/list")
    @ResponseBody
    public List<SkuDto> getSkuList(
            @RequestParam Long vendorId,
            @RequestParam Long fromSkuId,
            @RequestParam Long limit
    ) {
        List<SkuVo> skuList = skuService.findByVendorId(vendorId, fromSkuId, limit);

        return skuList.stream()
                .map(SkuDto::new)
                .collect(Collectors.toList());
    }

//    @PostMapping("/save")
//    @ResponseBody
//    public Optional<SkuDto> saveSku() {
//        return Optional.empty();
//    }
}
