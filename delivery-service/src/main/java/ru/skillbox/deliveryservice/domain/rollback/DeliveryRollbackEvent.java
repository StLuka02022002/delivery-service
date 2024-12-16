package ru.skillbox.deliveryservice.domain.rollback;

import lombok.Builder;
import lombok.Data;
import ru.skillbox.deliveryservice.domain.Event;
import ru.skillbox.deliveryservice.domain.InventoryEvent;
import ru.skillbox.deliveryservice.domain.OrderStatus;

import java.math.BigDecimal;

@Data
@Builder
public class DeliveryRollbackEvent implements Event {

    public static final String EVENT = "delivery-rollback";

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

    public static DeliveryRollbackEvent toDeliveryRollbackEvent(InventoryEvent event){
        return DeliveryRollbackEvent.builder()
                .orderId(event.getOrderId())
                .status(OrderStatus.DELIVERY_FAILED.toString())
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
