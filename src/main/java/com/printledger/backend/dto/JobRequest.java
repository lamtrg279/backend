package com.printledger.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.printledger.backend.entity.JobStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequest {
    private Long customerId;
    private BigDecimal totalPrice;
    private JobStatus status;
    private LocalDateTime dueDate;
}
