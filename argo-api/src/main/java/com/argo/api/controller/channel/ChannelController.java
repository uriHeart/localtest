package com.argo.api.controller.channel;

import com.argo.common.domain.channel.SalesChannelDto;
import com.argo.common.domain.channel.SalesChannelService;
import com.argo.common.domain.vendor.VendorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class ChannelController {
    private VendorService vendorService;

    private SalesChannelService salesChannelService;

    @GetMapping("/channels/{vendorId}")
    public List<SalesChannelDto> listSalesChannel(@PathVariable Long vendorId) {
        return vendorService.listActiveVendorChannel(vendorId)
                .stream().map(SalesChannelDto::from).collect(Collectors.toList());
    }
    @GetMapping("/channels/all")
    public List<SalesChannelDto> listAllChannel() {
        return salesChannelService.listAll()
                .stream().map(SalesChannelDto::from).collect(Collectors.toList());
    }
}
