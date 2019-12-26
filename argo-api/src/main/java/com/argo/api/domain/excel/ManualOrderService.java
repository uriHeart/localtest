package com.argo.api.domain.excel;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ManualOrderService {

    private final ManualOrderRepository repository;

    public void saveExcelStatus(String name, Long vendorId, Long channelId, String channelName) {
        repository.save(ManualOrder.builder()
                .vendorId(vendorId)
                .channelId(channelId)
                .channelName(channelName)
                .fileName(name)
                .status("UPLOAD")
                .uploadDate(new Date())
                .updatedAt(new Date())
                .createdAt(new Date())
                .build());
    }

    public void save(ManualOrder menualOrder){
        repository.save(menualOrder);
    }

    public void deleteExcel(String name, Long vendorId){
        ManualOrderPk manualOrderPk = new ManualOrderPk();
        manualOrderPk.setFileName(name);
        manualOrderPk.setVendorId(vendorId);
        repository.deleteById(manualOrderPk);
    }

    public List<ManualOrder> getExcelList(Long vendorId){
        return repository.findAllByVendorId(vendorId);
    }

    public ManualOrder getManualOrder(Long vendorId, String fileName){
        ManualOrderPk manualOrderPk = new ManualOrderPk();
        manualOrderPk.setFileName(fileName);
        manualOrderPk.setVendorId(vendorId);
        return repository.findById(manualOrderPk).get();
    }



}
