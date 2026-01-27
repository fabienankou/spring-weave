package com.example.springweave.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    String orderNumber,
    String status,
    String paymentStatus,
    BigDecimal totalAmount,
    String shippingAddress,
    LocalDateTime createdAt
) {}
