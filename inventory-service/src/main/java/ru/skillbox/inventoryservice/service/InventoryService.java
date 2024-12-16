package ru.skillbox.inventoryservice.service;

import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.InventoryEvent;
import ru.skillbox.inventoryservice.domain.PaymentEvent;

import java.util.List;

public interface InventoryService {
    List<Inventory> getInventors();
    Inventory getInventoryByOrderId(Long orderId);
    Inventory getInventor(Long id);
    Inventory createInventor(Inventory inventor);
    Inventory updateInventor(Long id, Inventory inventor);
    Inventory addCount(Long id, Integer count);
    Inventory deleteCount(Long id, Integer count);
    Inventory deleteCountByOrderId(Long id, Integer count);
    void deleteInventor(Long id);
    boolean exists(Long id);
    InventoryEvent processInventory(PaymentEvent event);
}
