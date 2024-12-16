package ru.skillbox.orderservice.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class OrderKafkaDto implements Event {

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

    public static OrderKafkaDto toKafkaDto(Order order, String username) {

        return new OrderKafkaDto(
                order.getId(),
                order.getStatus().toString(),
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString(),
                order.getDepartureAddress(),
                order.getDestinationAddress(),
                order.getCost(),
                order.getCount(),
                username
        );

    }

    @Override
    public String getEvent() {
        return "";
    }
}
