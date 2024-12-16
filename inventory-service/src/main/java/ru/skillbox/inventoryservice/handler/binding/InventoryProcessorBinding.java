package ru.skillbox.inventoryservice.handler.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import ru.skillbox.inventoryservice.domain.InventoryEvent;
import ru.skillbox.inventoryservice.domain.rollback.InventoryRollbackEvent;
import ru.skillbox.inventoryservice.domain.PaymentEvent;

public interface InventoryProcessorBinding {

    @Input(PaymentEvent.EVENT)
    SubscribableChannel input();

    @Output(InventoryEvent.EVENT)
    MessageChannel output();

    @Output(InventoryRollbackEvent.EVENT)
    MessageChannel outputRollback();
}
