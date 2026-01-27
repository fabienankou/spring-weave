package com.example.springweave.controllers;

import com.example.springweave.dtos.CustomerResponse;
import com.example.springweave.models.Customer;
import com.example.springweave.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        return ResponseEntity.ok(convertToResponse(customer));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CustomerResponse>> getAllCustomers(Pageable pageable) {
        Page<Customer> customers = customerRepository.findAll(pageable);
        Page<CustomerResponse> responses = customers.map(this::convertToResponse);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable UUID id,
            @RequestBody Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        if (customerDetails.getName() != null) {
            customer.setName(customerDetails.getName());
        }
        if (customerDetails.getPhone() != null) {
            customer.setPhone(customerDetails.getPhone());
        }
        if (customerDetails.getCity() != null) {
            customer.setCity(customerDetails.getCity());
        }
        if (customerDetails.getAddress() != null) {
            customer.setAddress(customerDetails.getAddress());
        }

        Customer updated = customerRepository.save(customer);
        return ResponseEntity.ok(convertToResponse(updated));
    }

    private CustomerResponse convertToResponse(Customer customer) {
        return new CustomerResponse(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getKycStatus().toString(),
            customer.getCreditScore(),
            customer.getCreditLimit(),
            customer.getAvailableCredit(),
            customer.isPremium(),
            customer.getCountry(),
            customer.getCity()
        );
    }
}
