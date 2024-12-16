package ru.skillbox.paymentservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentEvent implements Event {

    public static final String EVENT = "payment";

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

    public static PaymentEvent toPaymentEvent(OrderKafkaDto event) {
        return PaymentEvent.builder()
                .orderId(event.getId())
                .status(OrderStatus.PAID.toString())
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
