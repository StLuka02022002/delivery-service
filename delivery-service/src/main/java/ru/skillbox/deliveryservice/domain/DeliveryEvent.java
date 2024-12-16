package ru.skillbox.deliveryservice.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DeliveryEvent implements Event {

    public static final String EVENT = "delivery";

    private Long orderId;

    private Integer count;

    private String status;

    private String creationTime;

    private String modifiedTime;

    private String departureAddress;

    private String destinationAddress;

    private BigDecimal cost;

    private String username;

    @Override
    public String getEvent() {
        return EVENT;
    }

    public static DeliveryEvent toDeliveryEvent(InventoryEvent event){
        return DeliveryEvent.builder()
                .orderId(event.getOrderId())
                .status(OrderStatus.DELIVERED.toString())
                .creationTime(event.getCreationTime())
                .modifiedTime(event.getModifiedTime())
                .departureAddress(event.getDepartureAddress())
                .destinationAddress(event.getDestinationAddress())
                .cost(event.getCost())
                .count(event.getCount())
                .username(event.getUsername())
                .build();
    }
}
