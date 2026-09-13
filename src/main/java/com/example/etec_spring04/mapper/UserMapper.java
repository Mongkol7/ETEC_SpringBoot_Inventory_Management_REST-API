package com.example.etec_spring04.mapper;

import org.springframework.stereotype.Component;

import com.example.etec_spring04.dto.Request.UserRequest;
import com.example.etec_spring04.dto.Response.UserResponse;
import com.example.etec_spring04.entity.User;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }
        return User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .role(request.getRole())
                .build();
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public void updateEntity(User user, UserRequest request) {
        if (user == null || request == null) {
            return;
        }
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }
        user.setRole(request.getRole());
    }
}
