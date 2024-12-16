package ru.skillbox.paymentservice.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class OrderKafkaDto implements Event{

    public static final String EVENT = "orders";

    private Long id;

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
