package com.printledger.backend.dto;

import com.printledger.backend.entity.UOM;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobMaterialRequest {
    private String description;
    private Long inventoryItemId;
    private Integer requiredQuantity;
    private UOM unitUOM;
}