package com.minigithub.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minigithub.blog.dto.*;
import com.minigithub.blog.entity.Blog;
import com.minigithub.blog.mapper.BlogMapper;
import com.minigithub.blog.service.BlogService;
import com.minigithub.common.BusinessException;
import com.minigithub.common.PageResult;
import com.minigithub.common.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogMapper blogMapper;
    private final RestTemplate restTemplate;

    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;

    @Override
    public BlogResponse createBlog(Long authorId, CreateBlogRequest request) {
        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setAuthorId(authorId);
        blog.setViewCount(0);
        blog.setStatus(1);
        blogMapper.insert(blog);
        String authorName = fetchAuthorName(authorId);
        return convertToResponse(blog, authorName);
    }

    @Override
    public BlogResponse updateBlog(Long userId, Long blogId, UpdateBlogRequest request) {
        Blog blog = blogMapper.selectById(blogId);
        if (blog == null) {
            throw new BusinessException(ResultCode.BLOG_NOT_FOUND);
        }
        if (!blog.getAuthorId().equals(userId)) {
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }

        if (request.getTitle() != null) {
            blog.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            blog.setContent(request.getContent());
        }

        blogMapper.updateById(blog);
        String authorName = fetchAuthorName(blog.getAuthorId());
        return convertToResponse(blog, authorName);
    }

    @Override
    public void deleteBlog(Long userId, Long blogId) {
        Blog blog = blogMapper.selectById(blogId);
        if (blog == null) {
            throw new BusinessException(ResultCode.BLOG_NOT_FOUND);
        }
        if (!blog.getAuthorId().equals(userId)) {
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }
        blogMapper.deleteById(blogId);
    }

    @Override
    public BlogResponse getBlogById(Long blogId) {
        Blog blog = blogMapper.selectById(blogId);
        if (blog == null) {
            throw new BusinessException(ResultCode.BLOG_NOT_FOUND);
        }

        // 增加浏览量
        blog.setViewCount(blog.getViewCount() + 1);
        blogMapper.updateById(blog);

        String authorName = fetchAuthorName(blog.getAuthorId());
        return convertToResponse(blog, authorName);
    }

    @Override
    public PageResult<BlogResponse> getBlogList(int page, int size) {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getStatus, 1);
        wrapper.orderByDesc(Blog::getCreateTime);

        Page<Blog> pageParam = new Page<>(page, size);
        Page<Blog> resultPage = blogMapper.selectPage(pageParam, wrapper);

        List<Blog> blogs = resultPage.getRecords();
        Map<Long, String> authorNames = fetchAuthorNamesBatch(blogs);

        return PageResult.of(
                blogs.stream().map(b -> convertToResponse(b, authorNames.getOrDefault(b.getAuthorId(), "未知用户"))).toList(),
                resultPage.getTotal(),
                (int) resultPage.getCurrent(),
                (int) resultPage.getSize()
        );
    }

    @Override
    public PageResult<BlogResponse> getUserBlogList(Long userId, int page, int size) {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getAuthorId, userId);
        wrapper.orderByDesc(Blog::getCreateTime);

        Page<Blog> pageParam = new Page<>(page, size);
        Page<Blog> resultPage = blogMapper.selectPage(pageParam, wrapper);

        String authorName = fetchAuthorName(userId);

        return PageResult.of(
                resultPage.getRecords().stream().map(b -> convertToResponse(b, authorName)).toList(),
                resultPage.getTotal(),
                (int) resultPage.getCurrent(),
                (int) resultPage.getSize()
        );
    }

    private String fetchAuthorName(Long userId) {
        try {
            String url = userServiceUrl + "/internal/user/" + userId;
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.get("data") != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> userData = (Map<String, Object>) response.get("data");
                return (String) userData.getOrDefault("username", "未知用户");
            }
        } catch (Exception e) {
            // 静默处理，返回默认值
        }
        return "未知用户";
    }

    private Map<Long, String> fetchAuthorNamesBatch(List<Blog> blogs) {
        Map<Long, String> result = new HashMap<>();
        Set<Long> userIds = blogs.stream().map(Blog::getAuthorId).collect(Collectors.toSet());
        for (Long userId : userIds) {
            result.put(userId, fetchAuthorName(userId));
        }
        return result;
    }

    private BlogResponse convertToResponse(Blog blog, String authorName) {
        return new BlogResponse(
                blog.getId(),
                blog.getTitle(),
                blog.getContent(),
                blog.getAuthorId(),
                authorName,
                blog.getViewCount(),
                blog.getCreateTime(),
                blog.getUpdateTime()
        );
    }
}
