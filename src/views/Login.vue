<template>
  <AuthShell
    eyebrow="Welcome Back"
    title="继续写作，也继续阅读。"
    description="登录页保持干净和稳定，让用户在任何宽度下都能快速进入账号，不被装饰干扰。"
    subtitle="登录"
    secondary-label="注册"
    secondary-to="/register"
  >
    <template #aside>
      <div class="mt-8 grid gap-4 sm:grid-cols-2">
        <div class="rounded-[24px] border border-white/10 bg-white/[0.04] p-5">
          <p class="text-sm text-slate-400">写作</p>
          <p class="mt-3 text-lg font-semibold text-white">继续在同一套编辑器里整理你的想法。</p>
        </div>
        <div class="rounded-[24px] border border-white/10 bg-white/[0.04] p-5">
          <p class="text-sm text-slate-400">阅读</p>
          <p class="mt-3 text-lg font-semibold text-white">从首页到详情页，内容和排版会保持一致。</p>
        </div>
      </div>
    </template>

    <div class="text-center mb-8">
      <div class="login-logo">MG</div>
      <h1 class="mt-5 text-3xl font-semibold text-white">登录 MiniGitHub</h1>
      <p class="mt-2 text-sm text-slate-400">进入你的代码写作空间</p>
    </div>

    <div v-if="errorMsg" class="auth-alert">
      <span>⚠️</span> {{ errorMsg }}
    </div>

    <form @submit.prevent="handleLogin" class="space-y-5">
      <div class="form-group">
        <label class="form-label text-sm text-slate-400">用户名</label>
        <input
          v-model="form.username"
          class="input-glow"
          type="text"
          placeholder="输入用户名"
          required
        />
      </div>
      <div class="form-group">
        <label class="form-label text-sm text-slate-400">密码</label>
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
        class="w-full rounded-full bg-sky-400 px-4 py-3 text-base font-semibold text-slate-950 transition hover:bg-sky-300 disabled:cursor-not-allowed disabled:opacity-60"
        :disabled="loading"
      >
        {{ loading ? '登录中...' : '登录' }}
      </button>
    </form>

    <div class="auth-divider">
      <span>还没有账号？</span>
    </div>

    <div class="text-center mt-6">
      <router-link to="/register" class="auth-link">
        创建新账号
      </router-link>
    </div>
  </AuthShell>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { login } from '@/api/user'
import AuthShell from '@/components/AuthShell.vue'

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
</style>
