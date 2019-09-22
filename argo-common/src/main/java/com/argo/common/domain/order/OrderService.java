package com.argo.common.domain.order;

import com.argo.common.domain.common.util.JsonUtil;
import com.argo.common.domain.order.doc.OrderDoc;
import com.argo.common.domain.order.dto.OrderResultDto;
import com.argo.common.domain.order.reactive.ReactiveOrderAddressRepository;
import com.argo.common.domain.order.reactive.ReactiveOrderRepository;
import com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle;
import com.argo.common.domain.order.reactive.ReactiveOrderVendorItemLifecycleRepository;
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
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {
    private RestHighLevelClient client;
    private ObjectMapper objectMapper;
    @Autowired
    private ReactiveOrderRepository reactiveOrderRepository;
    @Autowired
    private ReactiveOrderAddressRepository reactiveOrderAddressRepository;
    @Autowired
    private ReactiveOrderVendorItemLifecycleRepository reactiveOrderVendorItemLifecycleRepository;

    @Autowired
    public OrderService(RestHighLevelClient client, ObjectMapper objectMapper,
                        ReactiveOrderRepository reactiveOrderRepository, ReactiveOrderAddressRepository reactiveOrderAddressRepository,
                        ReactiveOrderVendorItemLifecycleRepository reactiveOrderVendorItemLifecycleRepository) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.reactiveOrderRepository = reactiveOrderRepository;
        this.reactiveOrderAddressRepository = reactiveOrderAddressRepository;
        this.reactiveOrderVendorItemLifecycleRepository = reactiveOrderVendorItemLifecycleRepository;
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

    public Mono<List<OrderDoc>> searchOrders(OrderSearchParam param) {
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
                    List<OrderDoc> orderDocs = Lists.newArrayList();
                    SearchHits hits = response.getHits();
                    hits.forEach(
                            hit -> {
                                OrderDoc doc = JsonUtil.read(hit.getSourceAsString(), OrderDoc.class);
                                orderDocs.add(doc);
                            }
                    );
                    sink.success(orderDocs);
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("index error ", e);
                    sink.error(e);
                }
            });
        });
    }

    public Mono<List<OrderResultDto>> getOrderData(OrderSearchParam param) {
        Mono<List<OrderDoc>> docs = this.searchOrders(param);
        return docs.flatMapMany(Flux::fromIterable)
                .concatMap(doc -> {
                    final Mono<ArgoOrder> order = getOrder(doc);
                    final Mono<OrderAddress> address = getOrderAddress(doc);
                    final Mono<OrderVendorItemLifecycle> item = getOrderVendorItem(doc);
                    return Flux.zip(order, address, item)
                            .map(o -> OrderResultDto.from(doc, o.getT1(), o.getT2(), o.getT3()));
                }).collectList();
    }

    private Mono<ArgoOrder> getOrder(OrderDoc param) {
        return reactiveOrderRepository
                .findFirstByVendorIdAndChannelIdAndOrderIdOrderByPublishedAtDesc(param.getVendorId(), param.getChannelId(), param.getOrderId());
    }

    private Mono<OrderAddress> getOrderAddress(OrderDoc param) {
        return reactiveOrderAddressRepository
                .findFirstByVendorIdAndChannelIdAndOrderIdOrderByPublishedAtDesc(param.getVendorId(), param.getChannelId(), param.getOrderId());
    }

    private Mono<OrderVendorItemLifecycle> getOrderVendorItem(OrderDoc param) {
        if (param.getVendorItemId() == null) {
            return Mono.empty();
        }
        UUID vendorItemId = UUID.fromString(param.getVendorItemId());
        return reactiveOrderVendorItemLifecycleRepository
                .findFirstByVendorIdAndChannelIdAndOrderIdAndVendorItemIdOrderByPublishedAtDesc(param.getVendorId(), param.getChannelId(), param.getOrderId(), vendorItemId);
    }
}
