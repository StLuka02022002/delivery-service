package ru.skillbox.inventoryservice.domain.rollback;

import lombok.Builder;
import lombok.Data;
import ru.skillbox.inventoryservice.domain.Event;
import ru.skillbox.inventoryservice.domain.OrderStatus;
import ru.skillbox.inventoryservice.domain.PaymentEvent;

import java.math.BigDecimal;

@Data
@Builder
public class InventoryRollbackEvent implements Event {

    public static final String EVENT = "inventory-rollback";

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

    public static InventoryRollbackEvent toInventoryEvent(PaymentEvent event) {
        return InventoryRollbackEvent.builder()
                .orderId(event.getOrderId())
                .status(OrderStatus.INVENTMENT_FAILED.toString())
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
