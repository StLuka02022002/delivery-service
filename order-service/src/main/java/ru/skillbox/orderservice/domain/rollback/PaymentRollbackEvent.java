package ru.skillbox.orderservice.domain.rollback;

import lombok.Builder;
import lombok.Data;
import ru.skillbox.orderservice.domain.Event;
import ru.skillbox.orderservice.domain.OrderKafkaDto;
import ru.skillbox.orderservice.domain.OrderStatus;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentRollbackEvent implements Event {

    public static final String EVENT = "payment-rollback";

    private Long orderId;

    private String status;

    private String creationTime;

    private String modifiedTime;

    private String departureAddress;

    private String destinationAddress;

    private BigDecimal cost;

    private Integer count;

    private String username;

    @Override
    public String getEvent() {
        return EVENT;
    }

    public static PaymentRollbackEvent toPaymentRollbackEvent(OrderKafkaDto event) {
        return PaymentRollbackEvent.builder()
                .orderId(event.getId())
                .status(OrderStatus.PAYMENT_FAILED.toString())
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
