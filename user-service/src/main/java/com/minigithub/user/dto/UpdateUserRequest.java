package com.minigithub.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String avatar;

    @Email(message = "邮箱格式不正确")
    private String email;
}
