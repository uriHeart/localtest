package com.argo.api.service;

import com.argo.api.configuration.ArgoApiConfig;
import com.argo.api.service.gopotal.BusinessNumberCheckService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles(value = "local")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BusinessNumberCheckService.class, ArgoApiConfig.class})
public class BusinessNumberCheckServiceTest {

    @Autowired
    private BusinessNumberCheckService businessNumberChecker;

    @Test
    public void checkNumber() {
        Assert.assertTrue(businessNumberChecker.checkNumber("124815", "삼성전자로지텍주식회사", 1));
        Assert.assertFalse(businessNumberChecker.checkNumber("124815", "삼성전자로지텍주식회", 1));
    }
}
