package com.example.springweave.security;

import com.example.springweave.repositories.VendorRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
@Getter
@Setter
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final VendorRepository vendorRepository;

    public ApiKeyAuthenticationFilter(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("X-API-KEY");

        if (apiKey != null) {
            vendorRepository.findByApiKey(apiKey).ifPresent(vendor -> {
                var auth = new UsernamePasswordAuthenticationToken(
                        vendor.getEmail(),
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_VENDOR"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            });
        }

        filterChain.doFilter(request, response);
    }
}