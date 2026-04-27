package com.minigithub.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("blog")
public class Blog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    private String visibility; // public / private

    private Long authorId;

    private Integer viewCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Integer status; // 0:草稿, 1:已发布
}
