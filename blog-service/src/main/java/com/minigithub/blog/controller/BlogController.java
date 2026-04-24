package com.minigithub.blog.controller;

import com.minigithub.blog.dto.*;
import com.minigithub.blog.service.BlogService;
import com.minigithub.common.JwtUtil;
import com.minigithub.common.PageResult;
import com.minigithub.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public Result<BlogResponse> createBlog(HttpServletRequest request,
                                            @Valid @RequestBody CreateBlogRequest createRequest) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        BlogResponse blog = blogService.createBlog(userId, createRequest);
        return Result.success("发布成功", blog);
    }

    @PutMapping("/{id}")
    public Result<BlogResponse> updateBlog(HttpServletRequest request,
                                            @PathVariable("id") Long id,
                                            @Valid @RequestBody UpdateBlogRequest updateRequest) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        BlogResponse blog = blogService.updateBlog(userId, id, updateRequest);
        return Result.success("更新成功", blog);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteBlog(HttpServletRequest request,
                                    @PathVariable("id") Long id) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        blogService.deleteBlog(userId, id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/{id}")
    public Result<BlogResponse> getBlogById(@PathVariable("id") Long id) {
        BlogResponse blog = blogService.getBlogById(id);
        return Result.success(blog);
    }

    @GetMapping("/list")
    public Result<PageResult<BlogResponse>> getBlogList(@RequestParam(name = "page", defaultValue = "1") int page,
                                                          @RequestParam(name = "size", defaultValue = "10") int size) {
        PageResult<BlogResponse> result = blogService.getBlogList(page, size);
        return Result.success(result);
    }

    @GetMapping("/user/{userId}")
    public Result<PageResult<BlogResponse>> getUserBlogList(@PathVariable("userId") Long userId,
                                                              @RequestParam(name = "page", defaultValue = "1") int page,
                                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        PageResult<BlogResponse> result = blogService.getUserBlogList(userId, page, size);
        return Result.success(result);
    }

    @GetMapping("/my")
    public Result<PageResult<BlogResponse>> getMyBlogList(HttpServletRequest request,
                                                           @RequestParam(name = "page", defaultValue = "1") int page,
                                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        PageResult<BlogResponse> result = blogService.getUserBlogList(userId, page, size);
        return Result.success(result);
    }
}
