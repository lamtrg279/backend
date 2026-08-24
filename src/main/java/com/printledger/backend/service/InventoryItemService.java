package com.printledger.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.printledger.backend.entity.InventoryItem;
import com.printledger.backend.entity.InventoryItemStatus;
import com.printledger.backend.repository.InventoryItemRepository;

@Service
@Transactional
public class InventoryItemService {
    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public InventoryItem saveInventoryItem(InventoryItem inventoryItem) {
        validateRequiredFields(inventoryItem);
        return inventoryItemRepository.save(inventoryItem);
    }

    public InventoryItem getInventoryItemById(Long id) {
        return inventoryItemRepository.findById(id).orElse(null);
    }

    public List<InventoryItem> getAllInventoryItems() {
        return inventoryItemRepository.findAll();
    }

    public InventoryItem updateInventoryItem(long id, InventoryItem inventoryItem) {
        validateRequiredFields(inventoryItem);
        InventoryItem existingInventoryItem = inventoryItemRepository.findById(id).orElse(null);
        existingInventoryItem.setDescription(inventoryItem.getDescription());
        existingInventoryItem.setAvailableQty(inventoryItem.getAvailableQty());
        existingInventoryItem.setUnitPrice(inventoryItem.getUnitPrice());
        return inventoryItemRepository.save(existingInventoryItem);
    }

    public void deleteInventoryItemById(Long id) {
        InventoryItem existingInventoryItem = inventoryItemRepository.findById(id).orElse(null);
        if (existingInventoryItem == null) {
            throw new IllegalArgumentException("Inventory item not found");
        }
        if (existingInventoryItem.getStatus() == InventoryItemStatus.ACTIVE) {
            throw new IllegalArgumentException("Cannot delete an active inventory item");
        }
        inventoryItemRepository.deleteById(id);
    }

    public void validateRequiredFields(InventoryItem inventoryItem) {
        if (inventoryItem.getDescription() == null || inventoryItem.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Inventory item description is required");
        }
        if (inventoryItem.getAvailableQty() == null) {
            throw new IllegalArgumentException("Inventory item quantity is required");
        }
        if (inventoryItem.getUnitPrice() == null) {
            throw new IllegalArgumentException("Inventory item price is required");
        }
    }
}
