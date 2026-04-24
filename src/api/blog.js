import request from '@/utils/request'

const BLOG_BASE = '/blog-service'

export function getBlogList(params) {
  return request.get(`${BLOG_BASE}/blog/list`, { params })
}

export function getBlogById(id) {
  return request.get(`${BLOG_BASE}/blog/${id}`)
}

export function createBlog(data) {
  return request.post(`${BLOG_BASE}/blog`, data)
}

export function updateBlog(id, data) {
  return request.put(`${BLOG_BASE}/blog/${id}`, data)
}

export function deleteBlog(id) {
  return request.delete(`${BLOG_BASE}/blog/${id}`)
}

export function getUserBlogList(userId, params) {
  return request.get(`${BLOG_BASE}/blog/user/${userId}`, { params })
}

export function getMyBlogList(params) {
  return request.get(`${BLOG_BASE}/blog/my`, { params })
}
