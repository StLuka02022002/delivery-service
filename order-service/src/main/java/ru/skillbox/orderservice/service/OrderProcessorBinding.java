package ru.skillbox.orderservice.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import ru.skillbox.orderservice.domain.OrderKafkaDto;


public interface OrderProcessorBinding {

    @Output(OrderKafkaDto.EVENT)
    MessageChannel output();
}
