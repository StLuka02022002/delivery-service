package ru.skillbox.orderservice.handler.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import ru.skillbox.orderservice.domain.rollback.DeliveryRollbackEvent;
import ru.skillbox.orderservice.domain.rollback.InventoryRollbackEvent;
import ru.skillbox.orderservice.domain.rollback.PaymentRollbackEvent;

public interface RollbackBinding {

    @Input(PaymentRollbackEvent.EVENT)
    SubscribableChannel inputPaymentRollBack();

    @Input(InventoryRollbackEvent.EVENT)
    SubscribableChannel inputInventoryRollBack();

    @Input(DeliveryRollbackEvent.EVENT)
    SubscribableChannel inputDeliveryRollBack();
}
