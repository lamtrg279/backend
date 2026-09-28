package com.printledger.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.printledger.backend.entity.Job;
import com.printledger.backend.entity.JobStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {
    private Long jobNumber;
    private Long customerId;
    private BigDecimal totalPrice;
    private JobStatus status;
    private LocalDateTime dueDate;

    public static JobResponse fromEntity(Job job) {
        return JobResponse.builder()
                .jobNumber(job.getJobNumber())
                .customerId(job.getCustomer().getId())
                .totalPrice(job.getTotalPrice())
                .status(job.getStatus())
                .dueDate(job.getDueDate())
                .build();
    }

}
