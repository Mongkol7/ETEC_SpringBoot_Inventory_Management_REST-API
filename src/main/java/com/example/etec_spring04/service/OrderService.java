package com.example.etec_spring04.service;

import java.util.List;

import com.example.etec_spring04.dto.Request.OrderRequest;
import com.example.etec_spring04.dto.Response.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getOrdersByUserId(Long userId);

    OrderResponse updateOrderStatus(Long id, String status);

    void cancelOrder(Long id);
}
