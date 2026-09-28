package com.printledger.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.printledger.backend.dto.JobIntakeRequest;
import com.printledger.backend.dto.JobMaterialRequest;
import com.printledger.backend.dto.JobPartRequest;
import com.printledger.backend.dto.JobRequest;
import com.printledger.backend.dto.JobResponse;
import com.printledger.backend.entity.Customer;
import com.printledger.backend.entity.InventoryItem;
import com.printledger.backend.entity.Job;
import com.printledger.backend.entity.JobMaterial;
import com.printledger.backend.entity.JobPart;
import com.printledger.backend.entity.JobStatus;
import com.printledger.backend.repository.CustomerRepository;
import com.printledger.backend.repository.InventoryItemRepository;
import com.printledger.backend.repository.JobRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class JobService {
    private final JobRepository jobRepository;
    private final CustomerRepository customerRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public JobService(JobRepository jobRepository, CustomerRepository customerRepository,
            InventoryItemRepository inventoryItemRepository) {
        this.jobRepository = jobRepository;
        this.customerRepository = customerRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public void validateData(JobRequest request) {
        if (request.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer cannot be null or empty");
        }
    }

    public JobResponse saveJob(JobRequest request) {
        validateData(request);
        Customer customer = getCustomerOrThrow(request.getCustomerId());

        Job job = Job.builder()
                .customer(customer)
                .totalPrice(request.getTotalPrice())
                .status(request.getStatus())
                .dueDate(request.getDueDate())
                .build();
        return JobResponse.fromEntity(jobRepository.save(job));
    }

    // Retrieve a job by its IDJJ
    public JobResponse getJobById(Long jobId) {
        return JobResponse.fromEntity(getJobEntityOrThrow(jobId));
    }

    // Retrieve all jobs
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream().map(JobResponse::fromEntity).toList();
    }

    // Update an existing job
    public JobResponse updateJob(Long jobId, JobRequest request) {
        validateData(request);
        Job existingJob = getJobEntityOrThrow(jobId);
        Customer customer = getCustomerOrThrow(request.getCustomerId());

        existingJob.setCustomer(customer);
        existingJob.setTotalPrice(request.getTotalPrice());
        existingJob.setStatus(request.getStatus());
        existingJob.setDueDate(request.getDueDate());

        return JobResponse.fromEntity(jobRepository.save(existingJob));
    }

    // Retrieve the Job entity or throw an exception if not found
    private Job getJobEntityOrThrow(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));
    }

    // Retrieve the Customer entity or throw an exception if not found
    private Customer getCustomerOrThrow(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));
    }

    // Intake a new job along with its parts and materials
    public JobResponse intakeJob(JobIntakeRequest intakeRequest) {
        Customer customer = getCustomerOrThrow(intakeRequest.getCustomerId());
        Job job = Job.builder()
                .customer(customer)
                .dueDate(intakeRequest.getDueDate())
                .dateCreated(LocalDateTime.now())
                .status(JobStatus.OPEN)
                .jobParts(new ArrayList<>())
                .totalPrice(BigDecimal.ZERO)
                .build();

        // Initialize the job parts and materials before saving
        for (JobPartRequest partRequest : intakeRequest.getJobParts()) {
            JobPart jobPart = JobPart.builder()
                    .job(job)
                    .description(partRequest.getDescription())
                    .quantity(partRequest.getQuantity())
                    .unitUOM(partRequest.getUnitUOM())
                    .jobMaterials(new ArrayList<>())
                    .price(BigDecimal.ZERO)
                    .status(JobStatus.OPEN)
                    .build();

            for (JobMaterialRequest materialRequest : partRequest.getMaterials()) {
                InventoryItem inventoryItem = inventoryItemRepository.findById(materialRequest.getInventoryItemId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Inventory item not found with id: " + materialRequest.getInventoryItemId()));

                JobMaterial jobMaterial = JobMaterial.builder()
                        .job(job)
                        .jobPart(jobPart)
                        .inventoryItem(inventoryItem)
                        .description(inventoryItem.getDescription())
                        .pulledQuantity(0)
                        .plannedQuantity(materialRequest.getRequiredQuantity())
                        .unitPrice(inventoryItem.getUnitPrice())
                        .unitUOM(materialRequest.getUnitUOM())
                        .build();
                jobPart.getJobMaterials().add(jobMaterial);
            }
            job.getJobParts().add(jobPart);
        }

        return JobResponse.fromEntity(jobRepository.save(job));
    }
}
