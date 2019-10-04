package com.argo.collect.domain.service;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.service.ConvertService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(
    properties = {
            "value=test"
    },
    classes = {ArgoCollectorApplication.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ConvertServiceTest {

    @Autowired
    ConvertService convertService;

    @Test
    public void existsExcel() throws IOException {
        boolean result =  convertService.existsExcel("플레이어3월.xlsx");
        System.out.println(result);
    }

}
