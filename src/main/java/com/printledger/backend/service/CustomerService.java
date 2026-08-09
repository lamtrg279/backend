package com.printledger.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.printledger.backend.entity.Customer;
import com.printledger.backend.entity.CustomerStatus;
import com.printledger.backend.repository.CustomerRepository;

@Service // This annotation indicates that this class is a service component in the
         // Spring framework.
@Transactional // @Transactional: Protects database-changing operations with a transaction.

public class CustomerService {
    private final CustomerRepository customerRepository;

    // Receives the repository that this service uses to access customer data.
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Validates and saves a new customer, using ACTIVE as the default status.
    public Customer createCustomer(Customer customer) {
        validateRequiredFields(customer);
        if (customer.getStatus() == null) {
            customer.setStatus(CustomerStatus.ACTIVE);
        }
        return customerRepository.save(customer);
    }

    // Finds one customer by ID or reports that the customer does not exist.
    @Transactional(readOnly = true) // @Transactional(readOnly = true): Optimizes read-only operations by avoiding
                                    // unnecessary locking and flushing.
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    // Retrieves every customer from the database.
    @Transactional(readOnly = true) // Only reads customer records
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Finds an existing customer, updates its fields, and saves the changes.
    public Customer updateCustomer(Long id, Customer customerDetails) {
        validateRequiredFields(customerDetails);
        Customer existingCustomer = getCustomerById(id);

        existingCustomer.setCustName(customerDetails.getCustName());
        existingCustomer.setAddress1(customerDetails.getAddress1());
        existingCustomer.setAddress2(customerDetails.getAddress2());
        existingCustomer.setCity(customerDetails.getCity());
        existingCustomer.setState(customerDetails.getState());
        existingCustomer.setZip(customerDetails.getZip());
        existingCustomer.setPhone(customerDetails.getPhone());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setStatus(customerDetails.getStatus());

        return customerRepository.save(existingCustomer);
    }

    // Deletes a customer only when it is inactive and has no associated jobs.
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        if (customer.getStatus() != CustomerStatus.INACTIVE) {
            throw new RuntimeException("Only inactive customers can be deleted.");
        }
        if (customer.getJobs() != null && !customer.getJobs().isEmpty()) { // Check for null to prevent a
                                                                           // NullPointerException before checking if
                                                                           // the list contains any jobs
            throw new RuntimeException("Cannot delete customer with associated jobs.");
        }
        customerRepository.delete(customer);
    }

    // Checks that all required customer fields contain usable values.
    public void validateRequiredFields(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer object cannot be null.");
        }

        if (customer.getCustName() == null || customer.getCustName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required.");
        }

        if (customer.getAddress1() == null || customer.getAddress1().trim().isEmpty()) {
            throw new IllegalArgumentException("Address1 is required.");
        }

        if (customer.getCity() == null || customer.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("City is required.");
        }

        if (customer.getState() == null || customer.getState().trim().isEmpty()) {
            throw new IllegalArgumentException("State is required.");
        }

        if (customer.getZip() == null || customer.getZip().trim().isEmpty()) {
            throw new IllegalArgumentException("Zip code is required.");
        }

    }
}
