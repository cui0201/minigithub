import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { title: 'MiniGitHub - 首页' },
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录 - MiniGitHub' },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册 - MiniGitHub' },
  },
  {
    path: '/blog/:id',
    name: 'BlogDetail',
    component: () => import('@/views/BlogDetail.vue'),
    meta: { title: '文章详情 - MiniGitHub' },
  },
  {
    path: '/create',
    name: 'CreateBlog',
    component: () => import('@/views/CreateBlog.vue'),
    meta: { title: '发布文章 - MiniGitHub', requiresAuth: true },
  },
  {
    path: '/edit/:id',
    name: 'EditBlog',
    component: () => import('@/views/CreateBlog.vue'),
    meta: { title: '编辑文章 - MiniGitHub', requiresAuth: true },
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile.vue'),
    meta: { title: '个人中心 - MiniGitHub', requiresAuth: true },
  },
  {
    path: '/user/:username',
    name: 'UserPage',
    component: () => import('@/views/UserPage.vue'),
    meta: { title: '用户 - MiniGitHub' },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || 'MiniGitHub'
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router
