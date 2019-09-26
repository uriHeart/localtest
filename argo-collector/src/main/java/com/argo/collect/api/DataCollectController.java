package com.argo.collect.api;

import com.argo.collect.domain.collector.OrderCollectExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataCollectController {

    @Autowired
    private OrderCollectExecutor executor;

    @GetMapping("/collect")
    public void collect() {
        executor.run();
    }
}
