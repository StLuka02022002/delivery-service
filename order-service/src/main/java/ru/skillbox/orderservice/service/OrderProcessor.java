package ru.skillbox.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.OrderKafkaDto;

@Service
@EnableBinding(OrderProcessorBinding.class)
public class OrderProcessor {

    private final MessageChannel output;

    @Autowired
    public OrderProcessor(OrderProcessorBinding orderProcessorBinding) {
        this.output = orderProcessorBinding.output();
    }

    public void process(Order order, String username) {
        OrderKafkaDto event = OrderKafkaDto.toKafkaDto(order, username);
        output.send(MessageBuilder.withPayload(event).build());
    }
}

