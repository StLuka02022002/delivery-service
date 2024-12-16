package ru.skillbox.paymentservice.handler.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import ru.skillbox.paymentservice.domain.OrderKafkaDto;
import ru.skillbox.paymentservice.domain.PaymentEvent;
import ru.skillbox.paymentservice.domain.rollback.PaymentRollbackEvent;

public interface PaymentProcessorBinding {

    @Input(OrderKafkaDto.EVENT)
    SubscribableChannel input();

    @Output(PaymentEvent.EVENT)
    MessageChannel output();

    @Output(PaymentRollbackEvent.EVENT)
    MessageChannel outputRollback();
}
