package ru.skillbox.paymentservice.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import ru.skillbox.paymentservice.domain.rollback.DeliveryRollbackEvent;
import ru.skillbox.paymentservice.domain.rollback.InventoryRollbackEvent;
import ru.skillbox.paymentservice.domain.User;
import ru.skillbox.paymentservice.handler.binding.RollbackBinding;
import ru.skillbox.paymentservice.service.UserService;

import java.math.BigDecimal;

@Service
@EnableBinding(RollbackBinding.class)
public class RollbackEventHandler {

    @Autowired
    private UserService userService;

    @StreamListener(InventoryRollbackEvent.EVENT)
    public void handleEvent(InventoryRollbackEvent event) {
        rollbackPay(event.getUsername(), event.getCost(), event.getCount());
        System.out.println("Error return from InventoryEvent: " + event);
    }

    @StreamListener(DeliveryRollbackEvent.EVENT)
    public void handleEvent(DeliveryRollbackEvent event) {
        rollbackPay(event.getUsername(), event.getCost(), event.getCount());
        System.out.println("Error return from InventoryEvent: " + event);
    }

    private void rollbackPay(String username, BigDecimal coast, Integer count) {
        try {
            User user = userService.addAmount(username, coast.multiply(BigDecimal.valueOf(count)));
        } catch (Exception e) {
            System.out.println("Error rollback");
        }
    }
}
