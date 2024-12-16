package ru.skillbox.deliveryservice.service;

import ru.skillbox.deliveryservice.domain.DeliveryEvent;
import ru.skillbox.deliveryservice.domain.InventoryEvent;

public interface DeliveryService {

    DeliveryEvent processDelivery(InventoryEvent event);
}
