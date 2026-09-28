package com.printledger.backend.dto;

import java.util.List;

import com.printledger.backend.entity.UOM;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPartRequest {
    private String description;
    private Integer quantity;
    private UOM unitUOM;
    private List<JobMaterialRequest> materials;
}