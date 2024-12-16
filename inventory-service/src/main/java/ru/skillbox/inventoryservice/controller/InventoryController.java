package ru.skillbox.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/")
    public ResponseEntity<List<Inventory>> getInventories() {
        List<Inventory> inventories = inventoryService.getInventors();
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventor(id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Inventory> getInventoryByOrderId(@PathVariable Long orderId) {
        Inventory inventory = inventoryService.getInventoryByOrderId(orderId);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        Inventory createdInventory = inventoryService.createInventor(inventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        Inventory updatedInventory = inventoryService.updateInventor(id, inventory);
        if (updatedInventory != null) {
            return ResponseEntity.ok(updatedInventory);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping("/{id}/addCount")
    public ResponseEntity<Inventory> addCount(@PathVariable Long id, @RequestParam Integer count) {
        Inventory inventory = inventoryService.addCount(id, count);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/{id}/deleteCount")
    public ResponseEntity<Inventory> deleteCount(@PathVariable Long id, @RequestParam Integer count) {
        Inventory inventory = inventoryService.deleteCount(id, count);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/order/{orderId}/deleteCount")
    public ResponseEntity<Inventory> deleteCountByOrderId(@PathVariable Long orderId, @RequestParam Integer count) {
        Inventory inventory = inventoryService.deleteCountByOrderId(orderId, count);
        return ResponseEntity.ok(inventory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventor(id);
        return ResponseEntity.noContent().build();
    }
}
