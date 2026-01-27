package com.example.springweave.dtos;

import java.util.UUID;
import java.util.List;

public record CreateOrderRequest(
    UUID customerId,
    List<OrderItemRequest> items,
    String shippingAddress,
    String paymentMethod
) {}
