package com.printledger.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printledger.backend.entity.InventoryItem;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
}
