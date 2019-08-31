package com.argo.common.domain.vendor;

import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.common.enums.YesOrNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<VendorChannel> listAllByEnabled() {
        return vendorChannelRepository.findByEnabled(YesOrNo.Y);
    }

    public ChannelVendorAccount getChannelVendorAccount(SalesChannel salesChannel, Vendor vendor) {
        return channelVendorAccountRepository.findBySalesChannelAndVendor(salesChannel, vendor);
    }
}
