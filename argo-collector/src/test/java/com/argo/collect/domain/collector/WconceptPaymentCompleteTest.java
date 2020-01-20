package com.argo.collect.domain.collector;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.domain.event.EventConverter;
import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.vendor.VendorChannel;
import com.argo.common.domain.vendor.VendorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "value=test"
        },
        classes = {ArgoCollectorApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class WconceptPaymentCompleteTest extends AbstractOrderCollector{
    @Autowired
    private VendorService vendorService;

    @Autowired
    private List<EventConverter> eventConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean isSupport(SalesChannel channel) {
        return false;
    }

    @Override
    public void collect(VendorChannel vendorChannel) {

        File TargetHtml = new File("C:\\project\\argo_server_dev2\\argo-collector\\src\\test\\java\\com\\argo\\collect\\domain\\data\\wconceptPaymentComplete.html");
        Document doc = null;
        try {
            doc = Jsoup.parse(TargetHtml,"euc-kr");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element body = doc.body();
        List<Element> tbody = body.getElementsByTag("tbody");
    }

    @Test
    public void run(){
        for (VendorChannel channel : vendorService.autoCollectingTargets()) {
            if(channel.getSalesChannel().getSalesChannelId()==6){
                collect(channel);
            }
        }
    }
}
