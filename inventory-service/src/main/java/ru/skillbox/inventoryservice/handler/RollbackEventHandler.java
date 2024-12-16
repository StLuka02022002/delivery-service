package ru.skillbox.inventoryservice.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import ru.skillbox.inventoryservice.domain.rollback.DeliveryRollbackEvent;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.handler.binding.RollbackBinding;
import ru.skillbox.inventoryservice.service.InventoryService;

@Service
@EnableBinding(RollbackBinding.class)
public class RollbackEventHandler {

    @Autowired
    private InventoryService inventoryService;

    @StreamListener(DeliveryRollbackEvent.EVENT)
    public void handleEvent(DeliveryRollbackEvent event) {
        rollbackInventory(event.getOrderId(), event.getCount());
        System.out.println("Error return from DeliveryEventEvent: " + event);
    }

    private void rollbackInventory(Long orderId, Integer count) {
        try {
            Inventory inventory = inventoryService.addCount(orderId, count);
        } catch (Exception e) {
            System.out.println("Error rollback");
        }
    }
}
