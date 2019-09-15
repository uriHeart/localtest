package com.argo.common.domain.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
public class OrderService {
    private RestHighLevelClient client;
    private ObjectMapper objectMapper;

    @Autowired
    public OrderService(RestHighLevelClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public Mono<Void> addOrder(OrderDoc orderDoc){
        String json;
        try {
            json = objectMapper.writeValueAsString(orderDoc);
        } catch (JsonProcessingException e) {
            json = "";
        }

        IndexRequest indexRequest = new IndexRequest("order_doc").id(orderDoc.getId());
        indexRequest.source(json, XContentType.JSON);

        return Mono.create(sink -> {  //1
            client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {  //2
                @Override
                public void onResponse(IndexResponse indexResponse) {
                    log.info("index success : " + indexResponse.toString());
                    sink.success();
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("index error ", e);
                    sink.error(e);
                }
            });
        });
    }

}
