package ru.skillbox.inventoryservice.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import ru.skillbox.inventoryservice.domain.InventoryEvent;
import ru.skillbox.inventoryservice.domain.rollback.InventoryRollbackEvent;
import ru.skillbox.inventoryservice.domain.PaymentEvent;
import ru.skillbox.inventoryservice.handler.binding.InventoryProcessorBinding;
import ru.skillbox.inventoryservice.service.InventoryService;

@Service
@EnableBinding(InventoryProcessorBinding.class)
public class PaymentEventHandler implements EventHandler<PaymentEvent, InventoryEvent> {
    private final InventoryService inventoryService;

    @Autowired
    @Qualifier(InventoryRollbackEvent.EVENT)
    private MessageChannel outputRollback;

    @Autowired
    public PaymentEventHandler(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @StreamListener(PaymentEvent.EVENT)
    @SendTo(InventoryEvent.EVENT)
    public InventoryEvent handleEvent(PaymentEvent event) {
        try {
            InventoryEvent inventoryEvent = inventoryService.processInventory(event);
            if (inventoryEvent == null) {
                sendRollbackEvent(event);
            }
            return inventoryEvent;
        } catch (Exception ex) {
            sendRollbackEvent(event);
            throw ex;
        }
    }

    private void sendRollbackEvent(PaymentEvent event) {
        InventoryRollbackEvent rollbackEvent = InventoryRollbackEvent.toInventoryEvent(event);
        outputRollback.send(MessageBuilder.withPayload(rollbackEvent).build());
    }
}
