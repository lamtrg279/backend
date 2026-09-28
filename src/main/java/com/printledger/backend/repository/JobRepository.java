package com.printledger.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printledger.backend.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

}
