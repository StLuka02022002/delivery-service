package ru.skillbox.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.inventoryservice.client.OrderClient;
import ru.skillbox.inventoryservice.domain.*;
import ru.skillbox.inventoryservice.repository.InventoryRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;
    private final OrderClient orderClient;

    @Override
    public List<Inventory> getInventors() {
        return repository.findAll();
    }

    @Override
    public Inventory getInventoryByOrderId(Long orderId) {
        return repository.findByOrderId(orderId).orElseThrow(() ->
                new EntityNotFoundException("Inventory with order_id = " + orderId + " not found"));
    }

    @Override
    public Inventory getInventor(Long id) {
        return repository.findByOrderId(id).orElseThrow(() ->
                new EntityNotFoundException("Inventory with id = " + id + " not found"));
    }

    @Override
    public Inventory createInventor(Inventory inventor) {
        return repository.save(inventor);
    }

    @Override
    public Inventory updateInventor(Long id, Inventory inventor) {
        if (exists(id)) {
            inventor.setId(id);
            return repository.save(inventor);
        }
        return null;
    }

    @Override
    public Inventory addCount(Long id, Integer count) {
        Inventory inventory = getInventor(id);
        Integer inventoryCount = inventory.getCount() + count;
        inventory.setCount(inventoryCount);
        return repository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory deleteCount(Long id, Integer count) {
        Inventory inventory = getInventor(id);
        return deleteCount(inventory, count);
    }

    @Override
    @Transactional
    public Inventory deleteCountByOrderId(Long orderId, Integer count) {
        Inventory inventory = getInventoryByOrderId(orderId);
        return deleteCount(inventory, count);
    }

    @Override
    @Transactional
    public void deleteInventor(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Override
    public InventoryEvent processInventory(PaymentEvent event) {
        if (get(event.getOrderId(), event.getCount())) {
            InventoryEvent inventoryEvent = InventoryEvent.toInventoryEvent(event);
            StatusDto status = new StatusDto(OrderStatus.INVENTED, ServiceName.INVENTORY_SERVICE, "Invented order");
            orderClient.updateOrderStatus(event.getOrderId(), status);
            System.out.println("inventory result");
            return inventoryEvent;
        } else {
            StatusDto status = new StatusDto(OrderStatus.INVENTMENT_FAILED, ServiceName.INVENTORY_SERVICE, "Failed get order from inventory");
            orderClient.updateOrderStatus(event.getOrderId(), status);
            return null;
        }

    }

    private boolean get(Long orderId, Integer count) {
        try {
            Inventory inventory = deleteCountByOrderId(orderId, count);
            return inventory != null;
        } catch (Exception e) {
            return false;
        }
    }

    private Inventory deleteCount(Inventory inventory, Integer count) {
        Integer inventoryCount = inventory.getCount() - count;
        if (inventoryCount < 0) {
            throw new IllegalArgumentException("Count must be more zero");
        }
        inventory.setCount(inventoryCount);
        return repository.save(inventory);
    }
}
