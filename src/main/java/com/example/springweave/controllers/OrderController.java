package com.example.springweave.controllers;

import com.example.springweave.dtos.CreateOrderRequest;
import com.example.springweave.dtos.OrderResponse;
import com.example.springweave.models.Order;
import com.example.springweave.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        OrderResponse response = convertToResponse(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID id) {
        Order order = orderService.getOrderById(id);
        OrderResponse response = convertToResponse(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderService.getAllOrders(pageable);
        Page<OrderResponse> responses = orders.map(this::convertToResponse);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<OrderResponse>> getCustomerOrders(
            @PathVariable UUID customerId,
            Pageable pageable) {
        Page<Order> orders = orderService.getOrdersByCustomer(customerId, pageable);
        Page<OrderResponse> responses = orders.map(this::convertToResponse);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        Order order = orderService.updateOrderStatus(id, status);
        OrderResponse response = convertToResponse(order);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/payment-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updatePaymentStatus(
            @PathVariable UUID id,
            @RequestParam String paymentStatus) {
        Order order = orderService.updatePaymentStatus(id, paymentStatus);
        OrderResponse response = convertToResponse(order);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    private OrderResponse convertToResponse(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOrderNumber(),
            order.getStatus().toString(),
            order.getPaymentStatus().toString(),
            order.getTotalAmount(),
            order.getShippingAddress(),
            order.getCreatedAt()
        );
    }
}
