package com.argo.collect.web;

import com.argo.collect.service.CommonCodeService;
import com.argo.common.domain.channel.SalesChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
public class CommonCodeController {

    @Autowired
    CommonCodeService commonCodeService;

    /**
     * TODO session에서 사용자Id 에 해당하는 vendorId 를 취득후 vendor에 연결된
     * 채널만 조회하도록 수정
     * @return
     */
    @RequestMapping("/channels")
    public  @ResponseBody
    List<SalesChannel> getChannel(){
        return commonCodeService.getChannel();
    }
}
