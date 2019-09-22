package com.argo.common.domain.order;

import com.argo.common.domain.channel.SalesChannelDto;
import com.argo.common.domain.order.doc.OrderDoc;
import com.argo.common.domain.order.dto.*;
import com.argo.common.domain.vendor.VendorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

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

    public Mono<List<OrderResultDto>> getOrders(OrderSearchParam param) {
        BoolQueryBuilder filter = QueryBuilders.boolQuery();

        if (param.getOrderId() != null) {
            filter.filter(QueryBuilders.matchQuery("orderId", param.getOrderId()));
        } else {
            if (param.getFrom() != null) {
                filter.filter(QueryBuilders.rangeQuery("orderedAt").gte(param.getFrom()).lte(param.getTo()));
            }
            filter.filter(QueryBuilders.matchQuery("vendorId", param.getVendorId()))
                    .filter(QueryBuilders.matchQuery("salesChannelCode", param.getSalesChannelCode()));
        }

        SearchRequest request = new SearchRequest("order_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(filter);
        request.source(searchSourceBuilder);

        return Mono.create(sink -> {
            client.searchAsync(request, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
                @Override
                public void onResponse(SearchResponse response) {
                    log.info("index success : " + response.toString());

                    sink.success(Lists.newArrayList(OrderResultDto.builder()
                            .vendor(VendorDto.builder().build())
                            .salesChannel(SalesChannelDto.builder().build())
                            .orderProduct(OrderProductDto.builder().productPrice(ProductPriceDto.builder().build()).build())
                            .orderAddress(OrderAddressDto.builder()
                                    .originalAddress(OriginalAddressDto.builder().build())
                                    .refinedAddress(RefinedAddressDto.builder().build())
                                    .recipient(RecipientDto.builder().build())
                                    .orderer(OrdererDto.builder().build())
                                    .deliveryRequest(DeliveryRequestDto.builder().build())
                                    .build())
                            .build()));
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
