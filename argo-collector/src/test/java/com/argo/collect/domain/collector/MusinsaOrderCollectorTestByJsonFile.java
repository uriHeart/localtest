package com.argo.collect.domain.collector;

import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.vendor.VendorChannel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class MusinsaOrderCollectorTestByJsonFile {

    public void collect(VendorChannel channel)  {
        ObjectMapper objectMapper = new ObjectMapper();

        FileReader dataResult = null;
        try {
            dataResult = new FileReader("C:\\project\\argo_localtest\\argo-collector\\src\\test\\java\\com\\argo\\collect\\domain\\collector\\musinsaJsonData");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Map data = objectMapper.readValue(dataResult, Map.class);
            List<Map> rawEvents = (List<Map>) data.get("data");


            RawEvent rawEvent = RawEvent.builder()
                    .vendorId(channel.getVendor().getVendorId())
                    .channelId(channel.getSalesChannel().getSalesChannelId())
                    .format("JSON")
                    .auto(true)
                    .createdAt(new Date())
                    .build();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void run() {

    }
}
