package com.argo.collect.api;

import com.argo.collect.crawler.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CrawlingController {

    @Autowired
    private WebCrawler webCrawler;

    @GetMapping("/test")
    public void test(
            @RequestParam(value = "vendor") String vendorCode,
            @RequestParam(value = "channel") String saleChannelCode) throws IOException, InterruptedException {
        webCrawler.vendorCrawling(vendorCode, saleChannelCode);
    }


}
