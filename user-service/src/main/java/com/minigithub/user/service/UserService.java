package com.minigithub.user.service;

import com.minigithub.user.dto.*;

public interface UserService {
    String register(RegisterRequest request);

    String login(LoginRequest request);

    UserResponse getUserInfo(Long userId);

    UserResponse updateUser(Long userId, UpdateUserRequest request);

    UserResponse getUserByUsername(String username);
}
