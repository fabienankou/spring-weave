
package com.example.springweave.Config;

import com.example.springweave.repositories.VendorRepository;
import com.example.springweave.security.ApiKeyAuthenticationFilter;
import com.example.springweave.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final VendorRepository vendorRepository;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, VendorRepository vendorRepository) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.vendorRepository = vendorRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Accès Public
                        .requestMatchers("/api/auth/**", "/api/products/public/**").permitAll()
                        // Accès Vendeurs (API Key)
                        .requestMatchers("/api/vendor/**").hasRole("VENDOR")
                        // Accès Admin
                        .requestMatchers("/api/admin/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        // Accès Clients
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                        .anyRequest().authenticated()
                )
                // Ajout des filtres personnalisés
                .addFilterBefore(new ApiKeyAuthenticationFilter(vendorRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}