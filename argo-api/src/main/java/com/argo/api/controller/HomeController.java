package com.argo.api.controller;

import com.argo.common.domain.common.email.EmailService;
import com.argo.common.domain.order.ArgoOrder;
import com.argo.common.domain.order.OrderAddress;
import com.argo.common.domain.order.OrderService;
import com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.raw.RawEventRepository;
import com.argo.common.domain.common.data.DataConversionService;
import com.argo.common.domain.user.ArgoUser;
import com.argo.common.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    RawEventRepository rawEventRepository;

    @Autowired
    DataConversionService dataConversionService;

    @Autowired
    EmailService emailService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/order/rawEventConversionTest")
    @ResponseStatus(value = HttpStatus.OK)
    public String test() {
        RawEvent rawEvent = rawEventRepository.findFirstByVendorIdAndChannelIdAndOrderId(1L, 2L, "[1942_0]");
        Map<String, Object> map = dataConversionService.convert(rawEvent);
        orderService.saveOrder((ArgoOrder) map.get("com.argo.common.domain.order.ArgoOrder"), (OrderAddress) map.get("com.argo.common.domain.order.OrderAddress"), (List<OrderVendorItemLifecycle>) map.get("com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle"));
        return map.toString();
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/order/testMail")
    public void testMail() {
        emailService.sendSimpleMessage("syy0320@gmail.com", "Test Email", "Argo Test");
    }
}
