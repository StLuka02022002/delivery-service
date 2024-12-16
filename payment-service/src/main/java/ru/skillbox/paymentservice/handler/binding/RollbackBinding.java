package ru.skillbox.paymentservice.handler.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import ru.skillbox.paymentservice.domain.rollback.DeliveryRollbackEvent;
import ru.skillbox.paymentservice.domain.rollback.InventoryRollbackEvent;

public interface RollbackBinding {

    @Input(InventoryRollbackEvent.EVENT)
    SubscribableChannel inputInventoryRollBack();

    @Input(DeliveryRollbackEvent.EVENT)
    SubscribableChannel inputDeliveryRollBack();
}
