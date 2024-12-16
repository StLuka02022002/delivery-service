package ru.skillbox.orderservice.domain;

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
}
