package com.example.springweave.services;

import com.example.springweave.models.*;
import com.example.springweave.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public Order createOrder(Customer customer, List<OrderItemRequest> itemRequests, String address) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setShippingAddress(address);

        BigDecimal grandTotal = BigDecimal.ZERO;
        eventPublisher.publishEvent(new OrderCreatedEvent(order));
        for (OrderItemRequest req : itemRequests) {
            Product product = productService.findById(req.productId());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(req.quantity());
            item.setUnitPrice(product.getPrice());
            item.setTotal(product.getPrice().multiply(BigDecimal.valueOf(req.quantity())));

            order.getItems().add(item);
            grandTotal = grandTotal.add(item.getTotal());
        }

        order.setTotalAmount(grandTotal);
        return orderRepository.save(order);
    }
}