package com.example.springweave.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
    UUID id,
    String name,
    String description,
    String category,
    BigDecimal price,
    String currency,
    boolean isAvailable,
    int viewsCount,
    double averageRating
) {}
