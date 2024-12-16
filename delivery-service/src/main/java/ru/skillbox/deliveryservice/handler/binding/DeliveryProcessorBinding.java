package ru.skillbox.deliveryservice.handler.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import ru.skillbox.deliveryservice.domain.DeliveryEvent;
import ru.skillbox.deliveryservice.domain.rollback.DeliveryRollbackEvent;
import ru.skillbox.deliveryservice.domain.InventoryEvent;

public interface DeliveryProcessorBinding {

    @Input(InventoryEvent.EVENT)
    SubscribableChannel input();

    @Output(DeliveryEvent.EVENT)
    MessageChannel output();

    @Output(DeliveryRollbackEvent.EVENT)
    MessageChannel outputRollback();
}
