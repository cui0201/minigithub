package com.minigithub.user.controller;

import com.minigithub.common.Result;
import com.minigithub.user.dto.UserResponse;
import com.minigithub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class UserInternalController {
    private final UserService userService;

    @GetMapping("/user/{id}")
    public Result<UserResponse> getUserById(@PathVariable("id") Long id) {
        UserResponse user = userService.getUserInfo(id);
        return Result.success(user);
    }

    @PostMapping("/users/batch")
    public Result<Map<Long, UserResponse>> getUsersBatch(@RequestBody List<Long> userIds) {
        Map<Long, UserResponse> userMap = userIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> {
                            try {
                                return userService.getUserInfo(id);
                            } catch (Exception e) {
                                return null;
                            }
                        }
                ));
        return Result.success(userMap);
    }
}
