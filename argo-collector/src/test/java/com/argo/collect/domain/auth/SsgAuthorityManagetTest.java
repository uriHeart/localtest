package com.argo.collect.domain.auth;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.common.domain.vendor.VendorChannel;
import com.argo.common.domain.vendor.VendorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "value=test"
        },
        classes = {ArgoCollectorApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SsgAuthorityManagetTest extends AbstractAuthorityManager {

    @Autowired
    private VendorService vendorService;

    @Override
    public boolean isTargetChannel(String channel) {
        return false;
    }

    @Override
    public String requestAuth(VendorChannel channel) {

        AuthorityParam authorityParam = this.getParam(channel);

        return authorityParam.getTokenUrl();
    }


    @Test
    public void run(){
        for (VendorChannel channel : vendorService.autoCollectingTargets()) {
            if(channel.getSalesChannel().getSalesChannelId()==9){
                requestAuth(channel);
            }
        }
    }
}
