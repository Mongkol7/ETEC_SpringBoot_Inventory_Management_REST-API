package com.example.etec_spring04.dto.Response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private Long userId;
    private String username;
    private List<OrderItemResponse> orderItems;
    private LocalDateTime updatedAt;
}
