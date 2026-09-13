package com.example.etec_spring04.service;

import java.util.List;

import com.example.etec_spring04.dto.Request.UserRequest;
import com.example.etec_spring04.dto.Response.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);
}
