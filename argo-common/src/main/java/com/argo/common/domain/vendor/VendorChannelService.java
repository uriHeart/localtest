package com.argo.common.domain.vendor;

import com.argo.common.domain.common.enums.YesOrNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorChannelService {
    @Autowired
    private VendorChannelRepository vendorChannelRepository;

    public List<VendorChannel> listAllByEnabled() {
        return vendorChannelRepository.findByEnabled(true);
    }
}