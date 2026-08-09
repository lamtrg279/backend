package com.printledger.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printledger.backend.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
