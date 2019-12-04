package com.argo.common.domain.vendor;

import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ConversionOperationService
@Service
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ChannelVendorAccountRepository channelVendorAccountRepository;

    @Autowired
    private VendorChannelRepository vendorChannelRepository;

    private Map<Long, Vendor> vendorMap;

    @PostConstruct
    public void init() {
        vendorMap = Maps.newHashMap();
        listAll().forEach(
            v -> {
                vendorMap.put(v.getVendorId(), v);
            }
        );
    }

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
        return vendorChannelRepository.findByVendorAndEnabledAndAutoCollecting(getVendor(vendorId), true, false)
                .stream().map(VendorChannel::getSalesChannel).collect(Collectors.toList());
    }

    public Vendor getVendor(Long vendorId) {
        return vendorMap.get(vendorId);
    }

    @ConversionOperationMethod
    public Long getChannelId(Long vendorId, String sourceChannelId) {
        VendorChannel result = vendorChannelRepository.findByVendorAndChannelMapping(getVendor(vendorId), sourceChannelId);
        if (result == null) {
            return null;
        }
        return result.getSalesChannel().getSalesChannelId();
    }
}
