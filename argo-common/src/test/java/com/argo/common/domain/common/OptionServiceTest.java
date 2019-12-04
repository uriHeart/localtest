package com.argo.common.domain.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.testng.Assert;

@RunWith(MockitoJUnitRunner.class)
public class OptionServiceTest {

    @InjectMocks
    private OptionService optionService;

    @Test
    public void extractOptions_oneOption() {
        String option = optionService.extractOptions("SMALL LOGO SHORT PANTS - FREE");
        Assert.assertEquals("FREE", option);
    }

    @Test
    public void extractOptions_twoOptions() {
        String option = optionService.extractOptions("SMALL LOGO SHORT PANTS - BLACK - L");
        Assert.assertEquals("BLACK L", option);
    }

    @Test
    public void extractOptions_noOption() {
        String option = optionService.extractOptions("MONDAY TEE LYE L");
        Assert.assertEquals("", option);

        option = optionService.extractOptions("");
        Assert.assertEquals("", option);

        option = optionService.extractOptions(null);
        Assert.assertEquals("", option);
    }
}
