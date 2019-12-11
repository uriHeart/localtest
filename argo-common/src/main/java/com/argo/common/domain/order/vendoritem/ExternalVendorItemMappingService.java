package com.argo.common.domain.order.vendoritem;

import com.argo.common.domain.common.OptionService;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

@Service
@ConversionOperationService
public class ExternalVendorItemMappingService {
    @Autowired
    private ExternalVendorItemMappingRepository externalVendorItemMappingRepository;

    @Autowired
    private OptionService optionService;

    @ConversionOperationMethod
    public String getVendorItem(Long vendorId, String sourceItemId, String sourceItemName) {
        String sourceItemOption = sourceItemOption = optionService.extractOptions(sourceItemName);
        return getVendorItem(vendorId, sourceItemId, sourceItemName, sourceItemOption);
    }

    @ConversionOperationMethod
    public String getVendorItem(Long vendorId, String sourceItemId, String sourceItemName, String sourceItemOption) {


        ExternalVendorItemMapping mapping = externalVendorItemMappingRepository
                .findFirstByVendorIdAndSourceItemIdAndSourceItemNameAndSourceItemOptionOrderByVendorItemIdDesc(vendorId, sourceItemId, sourceItemName, sourceItemOption);
        if (mapping != null) {
            return mapping.getVendorItemId();
        }

        StringJoiner strJoiner = new StringJoiner("_");
        strJoiner.add(String.valueOf(vendorId)).add(sourceItemId).add(sourceItemName).add(sourceItemOption);
        String vendorItemId = Hashing.sha256().hashString(strJoiner.toString(), StandardCharsets.UTF_8).toString();

        externalVendorItemMappingRepository.save(ExternalVendorItemMapping.builder()
                .vendorId(vendorId)
                .sourceItemId(sourceItemId)
                .sourceItemName(sourceItemName)
                .sourceItemOption(sourceItemOption)
                .vendorItemId(vendorItemId)
                .build());
        return vendorItemId;
    }
}
