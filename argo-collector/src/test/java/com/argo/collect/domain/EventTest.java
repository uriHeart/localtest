package com.argo.collect.domain;

import com.argo.collect.ArgoCollectorApplication;
import com.argo.collect.domain.event.EventConverter;
import com.argo.common.domain.common.jpa.EventType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "value=test"
        },
        classes = {ArgoCollectorApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class EventTest {

    @Autowired
    private List<EventConverter> eventConverter;

    @Test
    public void run(){
        Map sevnt = new HashMap();
        sevnt.put("ord_state","구매확정");//W_CONCEPT
        EventType e = eventConverter.stream().filter(s -> s.isSupport("MUSINSA"))
                .map(s -> s.getEventType(sevnt))
                .findFirst()
        .orElse(EventType.OTHER);
        System.out.println(e);
    }
}
