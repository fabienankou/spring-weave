package com.example.springweave.Config;

import com.example.springweave.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Accès techniques (H2, Swagger)
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 2. Auth Publique
                        .requestMatchers("/api/auth/**").permitAll()

                        // 3. Gestion des Admins (CRITIQUE : On laisse POST ouvert pour créer le 1er admin)
                        .requestMatchers(HttpMethod.POST, "/api/admins").permitAll()
                        .requestMatchers("/api/admins/**").hasRole("ADMIN")

                        // 4. Gestion des Vendeurs
                        .requestMatchers(HttpMethod.GET, "/api/vendors/**").permitAll() // Voir les vendeurs : Public
                        .requestMatchers(HttpMethod.POST, "/api/vendors").hasRole("ADMIN") // Créer un vendeur : Admin seulement

                        // 5. Gestion des Produits
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll() // Tout le monde peut voir
                        .requestMatchers(HttpMethod.POST, "/api/products").hasAnyRole("VENDOR", "ADMIN") // Création sécurisée
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyRole("VENDOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyRole("VENDOR", "ADMIN")

                        // 6. Clients
                        .requestMatchers("/api/orders/**").hasRole("CUSTOMER")

                        // 7. Tout le reste fermé
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}