package com.minigithub.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;
    private Integer viewCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
