<template>
  <div class="app-container" @mousemove="handleMouseMove">
    <!-- 粒子背景 -->
    <ParticlesBg />

    <!-- 鼠标光晕跟随 -->
    <div
      class="cursor-glow"
      :style="{ left: mouseX + 'px', top: mouseY + 'px' }"
    />

    <!-- 导航栏 -->
    <nav class="navbar" :class="{ scrolled: isScrolled }">
      <div class="navbar-inner">
        <router-link to="/" class="flex items-center gap-3 no-underline">
          <span class="text-2xl">💻</span>
          <span class="text-xl font-bold" style="background: linear-gradient(135deg, #38bdf8, #a78bfa); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
            MiniGitHub
          </span>
        </router-link>

        <div class="flex items-center gap-6">
          <!-- 导航链接 -->
          <router-link to="/" class="nav-link">首页</router-link>

          <template v-if="userStore.isLoggedIn">
            <router-link to="/create" class="nav-link">发布</router-link>
            <!-- 用户下拉 -->
            <div class="relative" ref="userMenuRef">
              <div class="user-avatar" @click="showDropdown = !showDropdown">
                <span>{{ userStore.username.charAt(0).toUpperCase() }}</span>
              </div>
              <div class="dropdown-menu" :class="{ show: showDropdown }">
                <router-link to="/profile" class="dropdown-item" @click="showDropdown = false">
                  👤 个人中心
                </router-link>
                <router-link :to="'/user/' + userStore.username" class="dropdown-item" @click="showDropdown = false">
                  📋 我的主页
                </router-link>
                <div class="dropdown-divider"></div>
                <button class="dropdown-item danger" @click="handleLogout">
                  🚪 退出登录
                </button>
              </div>
            </div>
          </template>
          <template v-else>
            <router-link to="/login" class="nav-link">登录</router-link>
            <router-link to="/register" class="btn-glow text-sm !px-5 !py-2 !rounded-lg no-underline">
              注册
            </router-link>
          </template>
        </div>
      </div>
    </nav>

    <!-- 页面内容 -->
    <main class="main-content">
      <router-view v-slot="{ Component, route }">
        <transition name="page" mode="out-in">
          <component :is="Component" :key="route.path" />
        </transition>
      </router-view>
    </main>

    <!-- Toast通知 -->
    <div v-if="toastState?.show" class="toast" :class="toastState.type">
      {{ toastState.message }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { toastState } from '@/utils/toast'
import ParticlesBg from '@/components/ParticlesBg.vue'

const router = useRouter()
const userStore = useUserStore()

const showDropdown = ref(false)
const userMenuRef = ref(null)
const isScrolled = ref(false)
const mouseX = ref(-500)
const mouseY = ref(-500)

const handleMouseMove = (e) => {
  mouseX.value = e.clientX
  mouseY.value = e.clientY
}

const handleLogout = () => {
  userStore.logout()
  showDropdown.value = false
  router.push('/')
}

// 滚动监听
const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

// 点击外部关闭下拉
const handleClickOutside = (e) => {
  if (userMenuRef.value && !userMenuRef.value.contains(e.target)) {
    showDropdown.value = false
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.main-content {
  position: relative;
  z-index: 1;
  min-height: 100vh;
}
.navbar-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.nav-link {
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 15px;
  position: relative;
  padding: 4px 0;
  transition: color 0.3s;
}
.nav-link::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--accent-primary), var(--accent-secondary));
  transition: width 0.3s;
  border-radius: 1px;
}
.nav-link:hover {
  color: var(--text-primary);
}
.nav-link:hover::after {
  width: 100%;
}
.nav-link.router-link-active {
  color: var(--accent-primary);
}
.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--accent-primary), var(--accent-secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-weight: 700;
  font-size: 14px;
  border: 2px solid transparent;
  transition: all 0.3s;
}
.user-avatar:hover {
  border-color: var(--accent-primary);
  box-shadow: var(--glow-blue);
}
.dropdown-menu {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  min-width: 160px;
  background: rgba(15, 23, 42, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 8px;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-8px);
  transition: all 0.3s;
  z-index: 200;
}
.dropdown-menu.show {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}
.dropdown-item {
  display: block;
  width: 100%;
  padding: 10px 16px;
  border-radius: 8px;
  color: var(--text-primary);
  text-decoration: none;
  font-size: 14px;
  background: none;
  border: none;
  text-align: left;
  cursor: pointer;
  transition: background 0.2s;
}
.dropdown-item:hover {
  background: rgba(255, 255, 255, 0.05);
}
.dropdown-item.danger {
  color: #ef4444;
}
.dropdown-divider {
  height: 1px;
  background: var(--border-color);
  margin: 4px 0;
}
.toast {
  position: fixed;
  bottom: 32px;
  right: 32px;
  padding: 14px 24px;
  border-radius: 12px;
  background: rgba(15, 23, 42, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid var(--border-color);
  color: var(--text-primary);
  z-index: 1000;
  animation: slideUp 0.3s ease;
  box-shadow: var(--glow-blue);
}
.toast.error {
  border-color: rgba(239, 68, 68, 0.5);
  box-shadow: 0 0 20px rgba(239, 68, 68, 0.3);
}
.toast.success {
  border-color: rgba(34, 197, 94, 0.5);
  box-shadow: 0 0 20px rgba(34, 197, 94, 0.3);
}
@keyframes slideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
