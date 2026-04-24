import request from '@/utils/request'

// 使用 user-service 的直连地址
const USER_BASE = '/user-service'

export function login(data) {
  return request.post(`${USER_BASE}/user/login`, data)
}

export function register(data) {
  return request.post(`${USER_BASE}/user/register`, data)
}

export function getProfile() {
  return request.get(`${USER_BASE}/user/profile`)
}

export function updateProfile(data) {
  return request.put(`${USER_BASE}/user/profile`, data)
}

export function getUserByUsername(username) {
  return request.get(`${USER_BASE}/user/${username}`)
}
