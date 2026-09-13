package com.example.etec_spring04.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.etec_spring04.dto.Request.OrderItemRequest;
import com.example.etec_spring04.dto.Request.OrderRequest;
import com.example.etec_spring04.dto.Response.OrderResponse;
import com.example.etec_spring04.entity.Order;
import com.example.etec_spring04.entity.OrderItem;
import com.example.etec_spring04.entity.Product;
import com.example.etec_spring04.entity.User;
import com.example.etec_spring04.exception.BadRequestException;
import com.example.etec_spring04.exception.ResourceNotFoundException;
import com.example.etec_spring04.mapper.OrderMapper;
import com.example.etec_spring04.repository.OrderRepository;
import com.example.etec_spring04.repository.ProductRepository;
import com.example.etec_spring04.repository.UserRepository;
import com.example.etec_spring04.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
            throw new BadRequestException("Order must contain at least one item");
        }

        Order order = Order.builder()
                .user(user)
                .status(request.getStatus() != null && !request.getStatus().isBlank() ? request.getStatus() : "PENDING")
                .orderItems(new ArrayList<>())
                .build();

        double totalAmount = 0.0;

        for (OrderItemRequest itemReq : request.getOrderItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemReq.getProductId()));

            int availableStock = product.getStock() != null ? product.getStock() : 0;
            int requestedQty = itemReq.getQuantity() != null ? itemReq.getQuantity() : 0;

            if (availableStock < requestedQty) {
                throw new BadRequestException("Insufficient stock for product '" + product.getName()
                        + "' (ID: " + product.getId() + "). Available: " + availableStock
                        + ", Requested: " + requestedQty);
            }

            product.setStock(availableStock - requestedQty);
            productRepository.save(product);

            double price = product.getPrice() != null ? product.getPrice() : 0.0;
            double subtotal = price * requestedQty;

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(requestedQty)
                    .price(price)
                    .subtotal(subtotal)
                    .build();

            order.getOrderItems().add(orderItem);
            totalAmount += subtotal;
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new BadRequestException("Order is already cancelled");
        }

        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                if (product != null) {
                    int currentStock = product.getStock() != null ? product.getStock() : 0;
                    int returnQty = item.getQuantity() != null ? item.getQuantity() : 0;
                    product.setStock(currentStock + returnQty);
                    productRepository.save(product);
                }
            }
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
}
