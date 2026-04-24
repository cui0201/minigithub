<template>
  <div class="auth-container">
    <div class="auth-card">
      <!-- Logo区 -->
      <div class="text-center mb-8">
        <div class="login-logo">💻</div>
        <h1 class="text-2xl font-bold mt-4 mb-2" style="background: linear-gradient(135deg, #38bdf8, #a78bfa); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
          登录 MiniGitHub
        </h1>
        <p class="text-sm" style="color: var(--text-secondary);">进入你的代码宇宙</p>
      </div>

      <!-- 错误提示 -->
      <div v-if="errorMsg" class="auth-alert">
        <span>⚠️</span> {{ errorMsg }}
      </div>

      <!-- 表单 -->
      <form @submit.prevent="handleLogin" class="space-y-5">
        <div class="form-group">
          <label class="form-label text-sm" style="color: var(--text-secondary);">用户名</label>
          <input
            v-model="form.username"
            class="input-glow"
            type="text"
            placeholder="输入用户名"
            required
          />
        </div>
        <div class="form-group">
          <label class="form-label text-sm" style="color: var(--text-secondary);">密码</label>
          <input
            v-model="form.password"
            class="input-glow"
            type="password"
            placeholder="输入密码"
            required
          />
        </div>
        <button
          type="submit"
          class="btn-glow w-full !py-3 text-base"
          :disabled="loading"
        >
          {{ loading ? '🚀 登录中...' : '🚀 进入宇宙' }}
        </button>
      </form>

      <!-- 分割线 -->
      <div class="auth-divider">
        <span>—— 还没有账号？ ——</span>
      </div>

      <!-- 底部 -->
      <div class="text-center mt-6">
        <router-link to="/register" class="auth-link">
          创建新账号 →
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { login } from '@/api/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const form = reactive({
  username: '',
  password: '',
})
const errorMsg = ref('')
const loading = ref(false)

const handleLogin = async () => {
  if (!form.username || !form.password) {
    errorMsg.value = '请填写用户名和密码'
    return
  }

  loading.value = true
  errorMsg.value = ''

  try {
    const res = await login({
      username: form.username,
      password: form.password,
    })

    if (res.code === 200) {
      userStore.setToken(res.data)
      await userStore.fetchUser()
      const redirect = route.query.redirect || '/'
      router.push(redirect)
    } else {
      errorMsg.value = res.message || '登录失败'
    }
  } catch (e) {
    errorMsg.value = e.message || '登录失败，请检查网络'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-logo {
  font-size: 48px;
  animation: float 3s ease-in-out infinite;
}
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}
.auth-alert {
  padding: 12px 16px;
  border-radius: 12px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  color: #fca5a5;
  font-size: 14px;
  margin-bottom: 20px;
}
.auth-divider {
  text-align: center;
  margin: 24px 0;
  color: var(--text-secondary);
  font-size: 13px;
  position: relative;
}
.auth-link {
  color: var(--accent-primary);
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s;
}
.auth-link:hover {
  color: var(--accent-glow);
  text-shadow: 0 0 10px rgba(34, 211, 238, 0.3);
}
.form-group {
  margin-bottom: 4px;
}
.form-label {
  display: block;
  margin-bottom: 8px;
}
</style>
