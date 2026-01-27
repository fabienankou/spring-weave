package com.example.springweave.services;

import com.example.springweave.dtos.CreateOrderRequest;
import com.example.springweave.dtos.OrderItemRequest;
import com.example.springweave.models.*;
import com.example.springweave.models.enums.OrderStatus;
import com.example.springweave.models.enums.PaymentStatus;
import com.example.springweave.repositories.OrderRepository;
import com.example.springweave.repositories.OrderItemRepository;
import com.example.springweave.repositories.CustomerRepository;
import com.example.springweave.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setShippingAddress(request.shippingAddress());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (var itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(product.getPrice());
            item.setTotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));

            order.getItems().add(item);
            grandTotal = grandTotal.add(item.getTotal());
        }

        order.setTotalAmount(grandTotal);
        return orderRepository.save(order);
    }

    public Order getOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
    }

    public Page<Order> getOrdersByCustomer(UUID customerId, Pageable pageable) {
        return orderRepository.findByCustomerId(customerId, pageable);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Transactional
    public Order updateOrderStatus(UUID orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.valueOf(status));
        return orderRepository.save(order);
    }

    @Transactional
    public Order updatePaymentStatus(UUID orderId, String paymentStatus) {
        Order order = getOrderById(orderId);
        order.setPaymentStatus(PaymentStatus.valueOf(paymentStatus));
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }
}
