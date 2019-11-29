package com.argo.api.controller.sku;

import com.argo.api.service.SkuService;
import com.argo.common.domain.sku.Sku;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class SkuController {

    @Autowired
    SkuService skuService;

    @RequestMapping(value="/sku",
            method = RequestMethod.GET,
            produces = "application/json; charset=utf8")
    @ResponseBody
    public List<Sku> existsExcel() throws IOException {
        List<Sku> skuList = skuService.getSkuList();
        return skuList;
    }
}
