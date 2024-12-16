package ru.skillbox.inventoryservice.handler.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import ru.skillbox.inventoryservice.domain.rollback.DeliveryRollbackEvent;

public interface RollbackBinding {

    @Input(DeliveryRollbackEvent.EVENT)
    SubscribableChannel inputDeliveryRollBack();
}
