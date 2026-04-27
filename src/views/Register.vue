<template>
  <AuthShell
    eyebrow="Create Account"
    title="把账号入口也做成稳的页面。"
    description="注册页不应该在宽屏时空得发虚，也不该在窄屏时挤得发闷。我们给它一套更稳定的骨架。"
    subtitle="注册"
    secondary-label="登录"
    secondary-to="/login"
  >
    <template #aside>
      <div class="mt-8 rounded-[24px] border border-white/10 bg-white/[0.04] p-6">
        <p class="text-sm text-slate-400">创建账号后</p>
        <ul class="mt-4 space-y-3 text-sm leading-7 text-slate-300">
          <li>可以进入统一的 Markdown 编辑器</li>
          <li>可以管理自己的私密与公开文章</li>
          <li>可以在详情页里稳定查看排版结果</li>
        </ul>
      </div>
    </template>

    <div>
      <div class="text-center mb-8">
        <div class="register-logo">MG</div>
        <h1 class="mt-5 text-3xl font-semibold text-white">注册新账号</h1>
        <p class="mt-2 text-sm text-slate-400">开启你的代码写作之旅</p>
      </div>

      <div v-if="errorMsg" class="auth-alert">
        <span>⚠️</span> {{ errorMsg }}
      </div>

      <form @submit.prevent="handleRegister" class="space-y-4">
        <!-- 用户名 -->
        <div class="form-group">
          <label class="form-label text-sm text-slate-400">用户名</label>
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
          <label class="form-label text-sm text-slate-400">邮箱</label>
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
          <label class="form-label text-sm text-slate-400">密码</label>
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
          <label class="form-label text-sm text-slate-400">确认密码</label>
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
          class="mt-2 w-full rounded-full bg-sky-400 px-4 py-3 text-base font-semibold text-slate-950 transition hover:bg-sky-300 disabled:cursor-not-allowed disabled:opacity-60"
          :disabled="loading"
        >
          {{ loading ? '注册中...' : '创建账号' }}
        </button>
      </form>

      <div class="auth-divider">
        <span>已有账号？</span>
      </div>

      <div class="text-center mt-6">
        <router-link to="/login" class="auth-link">
          登录你的账号
        </router-link>
      </div>
    </div>
  </AuthShell>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { register } from '@/api/user'
import AuthShell from '@/components/AuthShell.vue'

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
  width: 3.75rem;
  height: 3.75rem;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 1.25rem;
  border: 1px solid rgba(125, 211, 252, 0.25);
  background: rgba(56, 189, 248, 0.1);
  color: rgb(186 230 253);
  font-size: 1.125rem;
  font-weight: 700;
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
  font-size: 15px;
  font-weight: 600;
  transition: all 0.3s;
}
.auth-link:hover {
  color: var(--accent-glow);
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
