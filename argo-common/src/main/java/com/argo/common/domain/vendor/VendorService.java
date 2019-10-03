package com.argo.common.domain.vendor;

import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.common.enums.YesOrNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ChannelVendorAccountRepository channelVendorAccountRepository;

    @Autowired
    private VendorChannelRepository vendorChannelRepository;

    public List<Vendor> listAll() {
        return vendorRepository.findAll();
    }

    public List<VendorChannel> autoCollectingTargets() {
        return vendorChannelRepository.findAllByAutoCollecting(true);
    }

    public ChannelVendorAccount getChannelVendorAccount(SalesChannel salesChannel, Vendor vendor) {
        return channelVendorAccountRepository.findBySalesChannelAndVendor(salesChannel, vendor);
    }

    public List<SalesChannel> listActiveVendorChannel(Long vendorId) {
        return vendorChannelRepository.findByVendorAndEnabledAndAutoCollecting(vendorRepository.findByVendorId(vendorId), true, false)
                .stream().map(VendorChannel::getSalesChannel).collect(Collectors.toList());
    }
}
