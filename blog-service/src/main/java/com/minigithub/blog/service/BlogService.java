package com.minigithub.blog.service;

import com.minigithub.blog.dto.*;
import com.minigithub.common.PageResult;

public interface BlogService {
    BlogResponse createBlog(Long authorId, CreateBlogRequest request);

    BlogResponse updateBlog(Long userId, Long blogId, UpdateBlogRequest request);

    void deleteBlog(Long userId, Long blogId);

    BlogResponse getBlogById(Long blogId);

    PageResult<BlogResponse> getBlogList(int page, int size);

    PageResult<BlogResponse> getUserBlogList(Long userId, int page, int size);
}
