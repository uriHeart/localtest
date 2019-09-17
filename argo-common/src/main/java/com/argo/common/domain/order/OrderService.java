package com.argo.common.domain.order;

import com.argo.common.domain.channel.SalesChannelDto;
import com.argo.common.domain.vendor.VendorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
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

    public Mono<List<OrderResultDto>> getOrders(OrderSearchParam param) {
        MultiSearchRequest request = new MultiSearchRequest();
        this.setSearchRequest("order_doc", request, "vendorId", param.getVendorId());
        this.setSearchRequest("order_doc", request,"salesChannelCode", param.getSalesChannelCode());
        this.setSearchRequest("order_doc", request,"orderId", param.getOrderId());

        return Mono.create(sink -> {
            client.msearchAsync(request, RequestOptions.DEFAULT, new ActionListener<MultiSearchResponse>() {
                @Override
                public void onResponse(MultiSearchResponse response) {
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

    private void setSearchRequest(String index, MultiSearchRequest request, String targetColumn, Object value) {
        if (value == null) {
            return;
        }
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(targetColumn, value));
        searchRequest.source(searchSourceBuilder);
        request.add(searchRequest);
    }

}
