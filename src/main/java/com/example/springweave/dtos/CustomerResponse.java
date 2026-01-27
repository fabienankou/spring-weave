package com.example.springweave.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record CustomerResponse(
    UUID id,
    String name,
    String email,
    String phone,
    String kycStatus,
    Integer creditScore,
    BigDecimal creditLimit,
    BigDecimal availableCredit,
    boolean isPremium,
    String country,
    String city
) {}
