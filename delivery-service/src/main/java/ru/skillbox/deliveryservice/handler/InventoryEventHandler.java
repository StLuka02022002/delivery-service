package ru.skillbox.deliveryservice.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import ru.skillbox.deliveryservice.domain.DeliveryEvent;
import ru.skillbox.deliveryservice.domain.rollback.DeliveryRollbackEvent;
import ru.skillbox.deliveryservice.domain.InventoryEvent;
import ru.skillbox.deliveryservice.handler.binding.DeliveryProcessorBinding;
import ru.skillbox.deliveryservice.service.DeliveryServiceImpl;

@Service
@EnableBinding(DeliveryProcessorBinding.class)
public class InventoryEventHandler implements EventHandler<InventoryEvent, DeliveryEvent> {
    private final DeliveryServiceImpl deliveryService;

    @Autowired
    @Qualifier(DeliveryRollbackEvent.EVENT)
    private MessageChannel outputRollback;

    @Autowired
    public InventoryEventHandler(DeliveryServiceImpl deliveryService) {
        this.deliveryService = deliveryService;
    }

    @StreamListener(InventoryEvent.EVENT)
    @SendTo(DeliveryEvent.EVENT)
    public DeliveryEvent handleEvent(InventoryEvent event) {
        try {
            DeliveryEvent deliveryEvent = deliveryService.processDelivery(event);
            if (deliveryEvent == null) {
                sendRollbackEvent(event);
            }
            return deliveryEvent;
        } catch (Exception ex) {
            sendRollbackEvent(event);
            throw ex;
        }
    }

    private void sendRollbackEvent(InventoryEvent event) {
        DeliveryRollbackEvent rollbackEvent = DeliveryRollbackEvent.toDeliveryRollbackEvent(event);
        outputRollback.send(MessageBuilder.withPayload(rollbackEvent).build());
    }
}
