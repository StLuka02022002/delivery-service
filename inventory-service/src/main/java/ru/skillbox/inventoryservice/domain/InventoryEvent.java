package ru.skillbox.inventoryservice.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InventoryEvent implements Event {

    public static final String EVENT = "inventory";

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

    public static InventoryEvent toInventoryEvent(PaymentEvent event) {
        return InventoryEvent.builder()
                .orderId(event.getOrderId())
                .status(OrderStatus.INVENTED.toString())
                .creationTime(event.getCreationTime())
                .modifiedTime(event.getModifiedTime())
                .departureAddress(event.getDepartureAddress())
                .destinationAddress(event.getDestinationAddress())
                .username(event.getUsername())
                .cost(event.getCost())
                .count(event.getCount())
                .build();
    }
}
