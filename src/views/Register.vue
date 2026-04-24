<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="text-center mb-8">
        <div class="register-logo">✨</div>
        <h1 class="text-2xl font-bold mt-4 mb-2" style="background: linear-gradient(135deg, #a78bfa, #22d3ee); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
          注册新账号
        </h1>
        <p class="text-sm" style="color: var(--text-secondary);">开启你的代码之旅</p>
      </div>

      <div v-if="errorMsg" class="auth-alert">
        <span>⚠️</span> {{ errorMsg }}
      </div>

      <form @submit.prevent="handleRegister" class="space-y-4">
        <!-- 用户名 -->
        <div class="form-group">
          <label class="form-label text-sm" style="color: var(--text-secondary);">用户名</label>
          <div class="input-wrapper">
            <input
              v-model="form.username"
              class="input-glow"
              type="text"
              placeholder="3-20位字符"
              @input="checkUsername"
              required
            />
            <span v-if="usernameStatus" class="input-status">{{ usernameStatus }}</span>
          </div>
        </div>

        <!-- 邮箱 -->
        <div class="form-group">
          <label class="form-label text-sm" style="color: var(--text-secondary);">邮箱</label>
          <input
            v-model="form.email"
            class="input-glow"
            type="email"
            placeholder="your@email.com"
            required
          />
        </div>

        <!-- 密码 -->
        <div class="form-group">
          <label class="form-label text-sm" style="color: var(--text-secondary);">密码</label>
          <input
            v-model="form.password"
            class="input-glow"
            type="password"
            placeholder="至少6位"
            @input="checkPasswordStrength"
            required
          />
          <!-- 密码强度条 -->
          <div class="strength-bar mt-2">
            <div class="strength-fill" :style="{ width: strengthPercent + '%', background: strengthColor }"></div>
          </div>
          <div class="text-xs mt-1" :style="{ color: strengthColor }">{{ strengthText }}</div>
        </div>

        <!-- 确认密码 -->
        <div class="form-group">
          <label class="form-label text-sm" style="color: var(--text-secondary);">确认密码</label>
          <div class="input-wrapper">
            <input
              v-model="form.confirmPassword"
              class="input-glow"
              :class="{ 'border-green-500/50': form.password && form.confirmPassword && form.password === form.confirmPassword, 'border-red-500/50': form.confirmPassword && form.password !== form.confirmPassword }"
              type="password"
              placeholder="再次输入密码"
              required
            />
            <span v-if="form.confirmPassword" class="input-status">
              {{ form.password === form.confirmPassword ? '✅' : '❌' }}
            </span>
          </div>
        </div>

        <button
          type="submit"
          class="btn-glow w-full !py-3 text-base mt-2"
          :disabled="loading"
        >
          {{ loading ? '✨ 注册中...' : '✨ 创建账号' }}
        </button>
      </form>

      <div class="auth-divider">
        <span>—— 已有账号？ ——</span>
      </div>

      <div class="text-center mt-6">
        <router-link to="/login" class="auth-link">
          登录你的账号 →
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { register } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
})
const errorMsg = ref('')
const loading = ref(false)
const usernameStatus = ref('')
const strengthPercent = ref(0)
const strengthText = ref('')
const strengthColor = ref('#94a3b8')

const checkUsername = () => {
  const len = form.username.length
  if (len === 0) {
    usernameStatus.value = ''
  } else if (len < 3) {
    usernameStatus.value = '❌ 太短'
  } else if (len > 20) {
    usernameStatus.value = '❌ 太长'
  } else {
    usernameStatus.value = '✅'
  }
}

const checkPasswordStrength = () => {
  const pwd = form.password
  let score = 0

  if (pwd.length >= 6) score += 20
  if (pwd.length >= 10) score += 20
  if (/[A-Z]/.test(pwd)) score += 20
  if (/[0-9]/.test(pwd)) score += 20
  if (/[^A-Za-z0-9]/.test(pwd)) score += 20

  strengthPercent.value = score

  if (score < 20) {
    strengthText.value = '太弱了'
    strengthColor.value = '#ef4444'
  } else if (score < 40) {
    strengthText.value = '有点弱'
    strengthColor.value = '#f97316'
  } else if (score < 60) {
    strengthText.value = '一般般'
    strengthColor.value = '#eab308'
  } else if (score < 80) {
    strengthText.value = '还不错'
    strengthColor.value = '#22c55e'
  } else {
    strengthText.value = '非常强'
    strengthColor.value = '#38bdf8'
  }
}

const handleRegister = async () => {
  if (!form.username || !form.email || !form.password || !form.confirmPassword) {
    errorMsg.value = '请填写所有字段'
    return
  }
  if (form.password !== form.confirmPassword) {
    errorMsg.value = '两次密码不一致'
    return
  }
  if (form.username.length < 3 || form.username.length > 20) {
    errorMsg.value = '用户名长度需要在3-20之间'
    return
  }
  if (form.password.length < 6) {
    errorMsg.value = '密码长度至少6位'
    return
  }

  loading.value = true
  errorMsg.value = ''

  try {
    const res = await register({
      username: form.username,
      email: form.email,
      password: form.password,
    })

    if (res.code === 200) {
      userStore.setToken(res.data)
      await userStore.fetchUser()
      router.push('/')
    } else {
      errorMsg.value = res.message || '注册失败'
    }
  } catch (e) {
    errorMsg.value = e.message || '注册失败，请检查网络'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-logo {
  font-size: 48px;
  animation: pulse 2s ease-in-out infinite;
}
@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.1); opacity: 0.8; }
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
.input-wrapper {
  position: relative;
}
.input-status {
  position: absolute;
  right: 14px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 14px;
}
.strength-bar {
  height: 4px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 2px;
  overflow: hidden;
}
.strength-fill {
  height: 100%;
  border-radius: 2px;
  transition: all 0.3s ease;
}
</style>
