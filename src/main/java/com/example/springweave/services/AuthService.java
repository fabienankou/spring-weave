package com.example.springweave.services;

import com.example.springweave.dtos.CustomerResponse;
import com.example.springweave.dtos.RegisterRequest;
import com.example.springweave.models.Customer;
import com.example.springweave.repositories.CustomerRepository;
import com.example.springweave.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Login Client
    public String loginCustomer(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        if (!passwordEncoder.matches(password, customer.getPasswordHash())) {
            throw new RuntimeException("Identifiants invalides");
        }

        return jwtService.generateToken(customer.getEmail(), Map.of("role", "ROLE_CUSTOMER", "id", customer.getId().toString()));
    }

    // Register Client
    @Transactional
    public Customer registerCustomer(RegisterRequest request) {
        if (customerRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        if (customerRepository.findByPhone(request.phone()).isPresent()) {
            throw new RuntimeException("Numéro de téléphone déjà utilisé");
        }

        Customer customer = new Customer();
        customer.setEmail(request.email());
        customer.setName(request.name());
        customer.setPhone(request.phone());
        customer.setPasswordHash(passwordEncoder.encode(request.password()));
        customer.setCountry(request.country());
        customer.setCity(request.city());
        customer.setAddress(request.address());
        customer.setIsActive(true);

        return customerRepository.save(customer);
    }

    // Get Current User Info
    public CustomerResponse getCurrentUserInfo(String token) {
        String email = jwtService.extractEmail(token);
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

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
