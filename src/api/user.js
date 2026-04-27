import request from '@/utils/request'

// 统一通过 Gateway 网关访问
const USER_BASE = '/api/user'

export function login(data) {
  return request.post(`${USER_BASE}/login`, data)
}

export function register(data) {
  return request.post(`${USER_BASE}/register`, data)
}

export function getProfile() {
  return request.get(`${USER_BASE}/profile`)
}

export function updateProfile(data) {
  return request.put(`${USER_BASE}/profile`, data)
}

export function getUserByUsername(username) {
  return request.get(`${USER_BASE}/${username}`)
}
