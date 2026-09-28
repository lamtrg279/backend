package com.printledger.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.printledger.backend.dto.JobIntakeRequest;
import com.printledger.backend.dto.JobRequest;
import com.printledger.backend.dto.JobResponse;
import com.printledger.backend.service.JobService;

@RestController
@RequestMapping("api/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<JobResponse> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{jobNumber}")
    public ResponseEntity<JobResponse> getMethodName(@PathVariable Long jobNumber) {
        return ResponseEntity.ok(jobService.getJobById(jobNumber));
    }

    @PutMapping("/{jobNumber}")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long jobNumber, @RequestBody JobRequest job) {
        return ResponseEntity.ok(jobService.updateJob(jobNumber, job));
    }

    @PostMapping("/intake")
    public ResponseEntity<JobResponse> intakeJob(@RequestBody JobIntakeRequest request) {
        JobResponse createdJob = jobService.intakeJob(request);
        return ResponseEntity.status(201).body(createdJob);
    }
}
