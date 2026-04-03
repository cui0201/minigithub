package com.minigithub.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    USER_ALREADY_EXISTS(1001, "用户名已存在"),
    USER_NOT_FOUND(1002, "用户不存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    INVALID_TOKEN(1004, "无效的令牌"),
    TOKEN_EXPIRED(1005, "令牌已过期"),
    BLOG_NOT_FOUND(2001, "文章不存在"),
    NO_PERMISSION(2002, "无权限操作");

    private final int code;
    private final String message;
}
