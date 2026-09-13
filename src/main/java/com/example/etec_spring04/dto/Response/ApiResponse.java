package com.example.etec_spring04.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private String message;
    private T data;
    private boolean success;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(String msg, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setMessage(msg);
        response.setData(data);
        response.setSuccess(true);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    public static <T> ApiResponse<T> error(String msg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setMessage(msg);
        response.setData(null);
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
}
