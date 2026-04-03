package com.minigithub.user.controller;

import com.minigithub.common.JwtUtil;
import com.minigithub.common.Result;
import com.minigithub.user.dto.*;
import com.minigithub.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterRequest request) {
        String token = userService.register(request);
        return Result.success("注册成功", token);
    }

    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        return Result.success("登录成功", token);
    }

    @GetMapping("/profile")
    public Result<UserResponse> getProfile(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        UserResponse user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    @PutMapping("/profile")
    public Result<UserResponse> updateProfile(HttpServletRequest request,
                                               @Valid @RequestBody UpdateUserRequest updateRequest) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        UserResponse user = userService.updateUser(userId, updateRequest);
        return Result.success("更新成功", user);
    }

    @GetMapping("/{username}")
    public Result<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse user = userService.getUserByUsername(username);
        return Result.success(user);
    }
}
