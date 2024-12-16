package ru.skillbox.orderservice.handler.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import ru.skillbox.orderservice.domain.DeliveryEvent;

public interface DeliveryOrderProcessorBinding {

    @Input(DeliveryEvent.EVENT)
    SubscribableChannel input();
}
