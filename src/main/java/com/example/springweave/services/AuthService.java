package com.example.springweave.services;

import com.example.springweave.models.AdminUser;
import com.example.springweave.models.Customer;
import com.example.springweave.repositories.AdminUserRepository;
import com.example.springweave.repositories.CustomerRepository;
import com.example.springweave.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Login Client
    public String loginCustomer(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        if (!passwordEncoder.matches(password, customer.getPasswordHash())) {
            throw new RuntimeException("Identifiants invalides");
        }

        return jwtService.generateToken(customer.getEmail(), Map.of("role", "ROLE_CUSTOMER"));
    }

    // Login Admin avec vérification TOTP
    public String loginAdmin(String email, String password, String totpCode) {
        AdminUser admin = adminUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin non trouvé"));

        if (!passwordEncoder.matches(password, admin.getPasswordHash())) {
            throw new RuntimeException("Identifiants invalides");
        }

        // Ici, vous devriez appeler un service TOTP (ex: Google Authenticator library)
        // verifyTotp(admin.getTwoFactorSecret(), totpCode);

        return jwtService.generateToken(admin.getEmail(), Map.of("role", "ROLE_" + admin.getRole().name()));
    }
}