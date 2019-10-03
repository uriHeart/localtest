package com.argo.api.controller.channel;

import com.argo.common.domain.channel.SalesChannelDto;
import com.argo.common.domain.vendor.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChannelController {
    private VendorService vendorService;

    @Autowired
    public ChannelController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/channels/{vendorId}")
    public List<SalesChannelDto> listSalesChannel(@PathVariable Long vendorId) {
        return vendorService.listActiveVendorChannel(vendorId)
                .stream().map(SalesChannelDto::from).collect(Collectors.toList());
    }
}
