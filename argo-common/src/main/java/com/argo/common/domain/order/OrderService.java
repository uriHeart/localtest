package com.argo.common.domain.order;

import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.channel.SalesChannelService;
import com.argo.common.domain.common.util.JsonUtil;
import com.argo.common.domain.order.doc.OrderDoc;
import com.argo.common.domain.order.dto.OrderResultDto;
import com.argo.common.domain.order.reactive.ReactiveOrderAddressRepository;
import com.argo.common.domain.order.reactive.ReactiveOrderRepository;
import com.argo.common.domain.order.reactive.ReactiveOrderVendorItemLifecycleRepository;
import com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle;
import com.argo.common.domain.vendor.Vendor;
import com.argo.common.domain.vendor.VendorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringJoiner;

@Service
@Slf4j
@AllArgsConstructor
public class OrderService {
    private RestHighLevelClient client;
    private ObjectMapper objectMapper;
    private ReactiveOrderRepository reactiveOrderRepository;
    private ReactiveOrderAddressRepository reactiveOrderAddressRepository;
    private ReactiveOrderVendorItemLifecycleRepository reactiveOrderVendorItemLifecycleRepository;
    private VendorService vendorService;
    private SalesChannelService salesChannelService;

    public void saveOrder(ArgoOrder order, OrderAddress orderAddress, List<OrderVendorItemLifecycle> orderVendorItemLifecycles) {
        if (order == null) {
            return;
        }
        reactiveOrderRepository.save(order).subscribe();
        reactiveOrderVendorItemLifecycleRepository.saveAll(orderVendorItemLifecycles).subscribe();
        if (orderAddress != null) {
            reactiveOrderAddressRepository.save(orderAddress).subscribe();
        }
        this.buildOrderDocument(order, orderAddress, orderVendorItemLifecycles);
    }

    public Mono<Void> addOrder(Long vendorId, Long channelId, String orderId, String vendoItemId) {
       Mono<ArgoOrder> order = reactiveOrderRepository.findFirstByVendorIdAndChannelIdAndOrderIdOrderByPublishedAtDesc(vendorId, channelId, orderId);
       Mono<OrderAddress> orderAddress = reactiveOrderAddressRepository.findFirstByVendorIdAndChannelIdAndOrderIdOrderByPublishedAtDesc(vendorId, channelId, orderId);
       Mono<OrderVendorItemLifecycle> vendorItem = reactiveOrderVendorItemLifecycleRepository.findFirstByVendorIdAndChannelIdAndOrderIdAndVendorItemIdOrderByVendorItemIdDescPublishedAtDesc(vendorId, channelId, orderId, vendoItemId);
       return Mono.create(sink -> {
           Mono.zip(order, orderAddress, vendorItem).subscribe(o -> this.buildOrderDocument(o.getT1(), o.getT2(), Lists.newArrayList(o.getT3())));
           sink.success();
       });
    }

    private String getDocId(ArgoOrder order, OrderVendorItemLifecycle item) {
        StringJoiner strJoiner = new StringJoiner(",");
        strJoiner.add(String.valueOf(order.getVendorId()))
                .add(String.valueOf(order.getChannelId()))
                .add(order.getOrderId()).add(item.getVendorItemId());
        return Hashing.sha256()
                .hashString(strJoiner.toString(), StandardCharsets.UTF_8)
                .toString();
    }

    private void buildOrderDocument(ArgoOrder order, OrderAddress orderAddress, List<OrderVendorItemLifecycle> vendorItems) {
        SalesChannel salesChannel = salesChannelService.getSalesChannel(order.getChannelId());
        vendorItems.forEach(
                item -> this.indexingOrder(OrderDoc.builder()
                        .id(this.getDocId(order, item))
                        .orderId(order.getOrderId())
                        .vendorId(order.getVendorId())
                        .channelId(order.getChannelId())
                        .publishedAt(item.getPublishedAt())
                        .collectedAt(order.getMetadata().getCollectedAt())
                        .orderedAt(order.getMetadata().getOrderedAt())
                        .orderStatus(order.getState())
                        .salesChannelCode(salesChannel.getCode())
                        .salesChannelName(salesChannel.getName())
                        .recipientName(orderAddress.getRecipient().getName())
                        .originalPostalCode(orderAddress.getOriginalPostalCode())
                        .vendorItemId(item.getVendorItemId())
                        .vendorItemLifeCycleStatus(item.getState())
                        .originalPrice(item.getMetadata().getOriginalPrice())
                        .paymentAmount(item.getMetadata().getPaymentAmount())
                        .paymentMethod(item.getMetadata().getPaymentMethod())
                        .salesPrice(item.getMetadata().getSalesPrice())
                        .quantity(item.getQuantity())
                        .sourceItemId(item.getSourceItemId())
                        .sourceItemName(item.getSourceItemName())
                        .sourceItemOption(item.getSourceItemOption())
                        .build())
        );
    }

    private void indexingOrder(OrderDoc orderDoc){
        String json;
        try {
            json = objectMapper.writeValueAsString(orderDoc);
        } catch (JsonProcessingException e) {
            json = "";
        }

        IndexRequest indexRequest = new IndexRequest("order_doc").id(orderDoc.getId());
        indexRequest.source(json, XContentType.JSON);

        client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {  //2
            @Override
            public void onResponse(IndexResponse indexResponse) {
                log.info("index success : " + indexResponse.toString());
            }

            @Override
            public void onFailure(Exception e) {
                log.error("index error ", e);
            }
        });
    }

    public Mono<List<OrderDoc>> searchOrders(OrderSearchParam param) {
        BoolQueryBuilder filter = QueryBuilders.boolQuery();

        if (param.getOrderId() != null) {
            filter.filter(QueryBuilders.matchQuery("orderId", param.getOrderId()));
        } else {
            if (param.getFrom() != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                filter.filter(QueryBuilders.rangeQuery("publishedAt")
                        .gte(formatter.format(param.getFrom())).lte(formatter.format(param.getTo())));
            }

            filter.filter(QueryBuilders.matchQuery("vendorId", param.getVendorId()));

            if (param.getSalesChannelId() != null) {
                filter.filter(QueryBuilders.matchQuery("channelId", param.getSalesChannelId()));
            }
        }

        SearchRequest request = new SearchRequest("order_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(10000);
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
                    final Mono<Vendor> vendor = getVendor(doc.getVendorId());
                    final Mono<SalesChannel> channel = getChannel(doc.getChannelId());
                    return Flux.zip(order, address, item, vendor, channel)
                            .map(o -> OrderResultDto
                                    .from(doc, o.getT1(), o.getT2(), o.getT3(), o.getT4(), o.getT5()));
                }).collectList();
    }

    private Mono<Vendor> getVendor(Long vendorId) {
        return Mono.just(vendorService.getVendor(vendorId));
    }

    private Mono<SalesChannel> getChannel(Long channelId) {
        return Mono.just(salesChannelService.getSalesChannel(channelId));
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
        return reactiveOrderVendorItemLifecycleRepository
                .findFirstByVendorIdAndChannelIdAndOrderIdAndVendorItemIdOrderByVendorItemIdDescPublishedAtDesc(param.getVendorId(), param.getChannelId(), param.getOrderId(), param.getVendorItemId());
    }
}
