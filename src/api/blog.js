import request from '@/utils/request'

// 统一通过 Gateway 网关访问
const BLOG_BASE = '/api/blog'

export function getBlogList(params) {
  return request.get(`${BLOG_BASE}/list`, { params })
}

export function getBlogById(id) {
  return request.get(`${BLOG_BASE}/${id}`)
}

export function createBlog(data) {
  return request.post(`${BLOG_BASE}`, data)
}

export function updateBlog(id, data) {
  return request.put(`${BLOG_BASE}/${id}`, data)
}

export function deleteBlog(id) {
  return request.delete(`${BLOG_BASE}/${id}`)
}

export function getUserBlogList(userId, params) {
  return request.get(`${BLOG_BASE}/user/${userId}`, { params })
}

export function getMyBlogList(params) {
  return request.get(`${BLOG_BASE}/my`, { params })
}
