package com.printledger.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.printledger.backend.entity.InventoryItem;
import com.printledger.backend.service.InventoryItemService;

@RestController
@RequestMapping("api/inventoryItems")
public class InventoryItemController {
    private final InventoryItemService inventoryItemService;

    public InventoryItemController(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getInventoryItems(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryItemService.getInventoryItemById(id));
    }

    @GetMapping()
    public ResponseEntity<List<InventoryItem>> getAllInventoryItems() {
        return ResponseEntity.ok(inventoryItemService.getAllInventoryItems());
    }

    @PostMapping()
    public ResponseEntity<InventoryItem> createInventoryItem(@RequestBody InventoryItem inventoryItem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryItemService.saveInventoryItem(inventoryItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> updateInventoryItem(@PathVariable Long id,
            @RequestBody InventoryItem inventoryItem) {
        return ResponseEntity.ok(inventoryItemService.updateInventoryItem(id, inventoryItem));
    }
}
