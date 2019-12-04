package com.argo.common.domain.order.dto;


import com.argo.common.domain.address.OriginalAddressDto;
import com.argo.common.domain.address.RefinedAddressDto;
import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.channel.SalesChannelDto;
import com.argo.common.domain.order.ArgoOrder;
import com.argo.common.domain.order.OrderAddress;
import com.argo.common.domain.order.doc.OrderDoc;
import com.argo.common.domain.order.vendoritem.OrderVendorItemLifecycle;
import com.argo.common.domain.vendor.Vendor;
import com.argo.common.domain.vendor.VendorDto;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResultDto {
    private String orderId; //주문아이디
    private Date orderedAt; //주문일시
    private Date collectedAt; //발주일시
    private Date paidAt; //결제일시

    private Long totalQuantity; //총 수량
    private String orderState; //오더 상태
    private String orderStateDescription; //오더 상태 설명

    private VendorDto vendor; //벤더 정보
    private SalesChannelDto salesChannel; //채널 정보
    private OrderProductDto orderProduct; //주문 상품정보

    private OrderAddressDto orderAddress; //주문 주소

    public static OrderResultDto from(OrderDoc doc, ArgoOrder order, OrderAddress address,
                                      OrderVendorItemLifecycle vendorItem, Vendor vendor, SalesChannel channel) {
        return OrderResultDto.builder()
                .orderId(order.getOrderId())
                .orderedAt(order.getMetadata().getOrderedAt())
                .paidAt(order.getPaidAt())
                .totalQuantity(order.getMetadata().getTotalQuantity())
                .orderState(order.getState())
                .vendor(VendorDto.builder()
                        .vendorId(order.getVendorId())
                        .vendorName(vendor.getName())
                        .build())
                .salesChannel(SalesChannelDto.builder()
                        .salesChannelId(order.getChannelId())
                        .salesChannelCode(doc.getSalesChannelCode())
                        .salesChannelName(channel.getName())
                        .build())
                .orderAddress(OrderAddressDto.builder()
                        .originalAddress(OriginalAddressDto.builder()
                                .postalCode(address.getOriginalPostalCode())
                                .address1(address.getOriginalAddress().getAddress1())
                                .address2(address.getOriginalAddress().getAddress2())
                                .fullAddress(address.getOriginalAddress().getFullAddress())
                                .build())
                        .refinedAddress(RefinedAddressDto.builder()
                                .postalCode5(address.getRefinedPostalCode())
                                .jibunAddress(address.getRefinedAddress().getJibunAddress())
                                .roadAddress(address.getRefinedAddress().getRoadAddress())
                                .roadName(address.getRefinedAddress().getRoadName())
                                .postalCode6(address.getRefinedAddress().getPostalCode6())
                                .buildingMainNumber(address.getRefinedAddress().getBuildingMainNumber())
                                .buildingSubNumber(address.getRefinedAddress().getBuildingSubNumber())
                                .buildingName(address.getRefinedAddress().getBuildingName())
                                .buildingDong(address.getRefinedAddress().getBuildingDong())
                                .buildingHo(address.getRefinedAddress().getBuildingHo())
                                .latitude(address.getRefinedAddress().getLatitude())
                                .longitude(address.getRefinedAddress().getLongitude())
                                .build())
                        .orderer(OrdererDto.builder()
                                .name(address.getOrderer().getName())
                                .phoneNumber1(address.getOrderer().getPhoneNumber1())
                                .phoneNumber2(address.getOrderer().getPhoneNumber2())
                                .build())
                        .recipient(RecipientDto.builder()
                                .name(address.getRecipient().getName())
                                .phoneNumber1(address.getRecipient().getPhoneNumber1())
                                .phoneNumber2(address.getRecipient().getPhoneNumber2())
                                .build())
                        .deliveryRequest(DeliveryRequestDto.builder()
                                .deliveryRequest(address.getDeliveryRequest().getDeliveryRequest())
                                .build())
                        .build())
                .orderProduct(OrderProductDto.builder()
                        .vendorItemId(vendorItem.getVendorItemId())
                        .productId(vendorItem.getSourceItemId())
                        .productName(vendorItem.getSourceItemName())
                        .productDesc(vendorItem.getSourceItemOption())
                        .quantity(vendorItem.getQuantity())
                        .productPrice(ProductPriceDto.builder()
                                .originalPrice(vendorItem.getMetadata().getOriginalPrice())
                                .salesPrice(vendorItem.getMetadata().getSalesPrice())
                                .paymentMethod(vendorItem.getMetadata().getPaymentMethod())
                                .build())
                        .skuDto(OrderSkuDto.builder()
                                .barcode(doc.getBarcode())
                                .skuId(doc.getSkuId())
                                .skuName(doc.getSkuName())
                                .skuSize(doc.getSkuSize())
                                .skuColor(doc.getSkuColor())
                                .build())
                        .build())
                .build();
    }
}
