package com.minigithub.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateBlogRequest {
    private String title;
    private String content;
    private String visibility;
}
