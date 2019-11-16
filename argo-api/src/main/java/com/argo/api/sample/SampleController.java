package com.argo.api.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(value = "/board")
@RestController
public class SampleController {

    @PostMapping(value = "/message")
    public void post(@RequestBody SampleParam param) {
        log.info(" sample param : {}", param.toString());
    }

    @GetMapping(value = "/{id}")
    public String get(@PathVariable String id) {
        log.info(" id : {}", id );
        return id;
    }

    @PutMapping(value = "/message")
    public void put(@RequestBody SampleParam param) {
        log.info(" sample param : {}", param.toString());
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable String id) {
        log.info(" id : {}", id );
    }
}
