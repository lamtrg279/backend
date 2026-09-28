package com.printledger.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobIntakeRequest {
    private Long customerId;
    private LocalDateTime dueDate;
    private List<JobPartRequest> jobParts;
}
