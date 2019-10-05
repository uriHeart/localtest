package com.argo.api.controller;

import com.argo.common.domain.order.OrderService;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.raw.RawEventRepository;
import com.argo.common.domain.common.data.DataConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

  @Autowired
  RawEventRepository rawEventRepository;

  @Autowired
  DataConversionService dataConversionService;

  @Autowired
  OrderService orderService;

  @GetMapping(value = "/order/rawEventConversionTest")
  @ResponseStatus(value = HttpStatus.OK)
  public void test() {
    RawEvent rawEvent = rawEventRepository.findByVendorIdAndChannelIdAndOrderId(1L, 2L, "[20190905-0000016]");
    Map<String, Object> map = dataConversionService.convert(rawEvent);
    map.toString();
  }

  @GetMapping("/home")
  public String home() {
    return "home";
  }

}
