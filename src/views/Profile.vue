<template>
  <div class="profile-page">
    <div class="profile-container">
      <h1 class="section-title">👤 个人中心</h1>

      <div v-if="!userStore.isLoggedIn" class="empty-state">
        <div class="empty-icon">🔒</div>
        <h3 class="text-xl mt-4">请先登录</h3>
        <router-link to="/login" class="btn-glow inline-block mt-6 no-underline">
          前往登录
        </router-link>
      </div>

      <div v-else class="glass-card !p-8">
        <!-- 用户头像 -->
        <div class="flex items-center gap-6 mb-8">
          <div class="profile-avatar">
            {{ userStore.username.charAt(0).toUpperCase() }}
          </div>
          <div>
            <h2 class="text-2xl font-bold">{{ userStore.user?.username }}</h2>
            <p class="text-sm mt-1" style="color: var(--text-secondary);">{{ userStore.user?.email }}</p>
          </div>
        </div>

        <!-- 信息 -->
        <div class="space-y-6">
          <div class="form-group">
            <label class="form-label">用户名</label>
            <div class="info-display">{{ userStore.user?.username || '-' }}</div>
          </div>
          <div class="form-group">
            <label class="form-label">邮箱</label>
            <div class="info-display">{{ userStore.user?.email || '-' }}</div>
          </div>
          <div class="form-group">
            <label class="form-label">注册时间</label>
            <div class="info-display">{{ formatDate(userStore.user?.createTime) }}</div>
          </div>
        </div>

        <!-- 退出 -->
        <div class="mt-8 pt-6" style="border-top: 1px solid var(--border-color);">
          <button class="btn-danger" @click="handleLogout">
            🚪 退出登录
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const d = new Date(dateStr)
  return d.toLocaleDateString('zh-CN', {
    year: 'numeric', month: 'long', day: 'numeric',
    hour: '2-digit', minute: '2-digit',
  })
}

const handleLogout = () => {
  userStore.logout()
  router.push('/')
}
</script>

<style scoped>
.profile-page {
  padding: 120px 24px 60px;
  min-height: 100vh;
}
.profile-container {
  max-width: 600px;
  margin: 0 auto;
}
.profile-avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--accent-primary), var(--accent-secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  font-weight: 700;
  flex-shrink: 0;
  border: 3px solid rgba(56, 189, 248, 0.3);
  box-shadow: var(--glow-blue);
}
.form-group {
  margin-bottom: 4px;
}
.form-label {
  display: block;
  margin-bottom: 6px;
  font-size: 13px;
  color: var(--text-secondary);
}
.info-display {
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid var(--border-color);
  border-radius: 10px;
  font-size: 15px;
}
.btn-danger {
  padding: 12px 28px;
  border-radius: 10px;
  border: 1px solid rgba(239, 68, 68, 0.4);
  color: #ef4444;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  background: rgba(239, 68, 68, 0.1);
  font-size: 15px;
}
.btn-danger:hover {
  background: rgba(239, 68, 68, 0.2);
  box-shadow: 0 0 20px rgba(239, 68, 68, 0.3);
  transform: translateY(-2px);
}
</style>
