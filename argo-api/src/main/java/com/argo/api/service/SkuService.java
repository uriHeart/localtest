package com.argo.api.service;

import com.argo.common.domain.sku.Sku;
import com.argo.common.domain.sku.SkuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SkuService {

    @Autowired
    SkuRepository skuRepository;

    public List<Sku> getSkuList(){
        return skuRepository.findAll();
    }

}
