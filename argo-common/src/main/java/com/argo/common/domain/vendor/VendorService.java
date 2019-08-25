package com.argo.common.domain.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ChannelVendorAccountRepository channelVendorAccountRepository;

    public List<Vendor> listAll() {
        return vendorRepository.findAll();
    }
}
