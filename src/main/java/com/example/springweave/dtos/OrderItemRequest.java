package com.example.springweave.dtos;

import java.util.UUID;

public record OrderItemRequest(
    UUID productId,
    Integer quantity
) {}
