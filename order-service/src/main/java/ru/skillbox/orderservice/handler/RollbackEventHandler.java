package ru.skillbox.orderservice.handler;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import ru.skillbox.orderservice.domain.rollback.DeliveryRollbackEvent;
import ru.skillbox.orderservice.domain.rollback.InventoryRollbackEvent;
import ru.skillbox.orderservice.domain.rollback.PaymentRollbackEvent;
import ru.skillbox.orderservice.handler.binding.RollbackBinding;

@Service
@EnableBinding(RollbackBinding.class)
public class RollbackEventHandler {

    @StreamListener(PaymentRollbackEvent.EVENT)
    public void handleEvent(PaymentRollbackEvent event) {
        System.out.println("Error return from PaymentService: " + event);
    }

    @StreamListener(InventoryRollbackEvent.EVENT)
    public void handleEvent(InventoryRollbackEvent event) {
        System.out.println("Error return from InventoryEvent: " + event);
    }

    @StreamListener(DeliveryRollbackEvent.EVENT)
    public void handleEvent(DeliveryRollbackEvent event) {
        System.out.println("Error return from DeliveryEvent " + event);
    }
}
