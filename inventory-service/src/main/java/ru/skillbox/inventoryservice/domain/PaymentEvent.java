package ru.skillbox.inventoryservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
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

}
