<template>
  <PageShell subtitle="个人中心" show-nav max-width="5xl">
    <template #actions>
      <router-link
        to="/"
        class="rounded-full border border-white/10 px-4 py-2 text-sm text-slate-200 no-underline transition hover:border-white/20 hover:bg-white/5"
      >
        返回首页
      </router-link>
    </template>

    <div class="mb-8 max-w-2xl">
      <h1 class="text-4xl font-semibold tracking-tight text-white">个人中心</h1>
      <p class="mt-3 text-sm leading-7 text-slate-400">这里应该像账户页，而不是漂浮在空白中的一张卡片。</p>
    </div>

    <div v-if="!userStore.isLoggedIn" class="rounded-[28px] border border-white/10 bg-slate-900/80 px-6 py-14 text-center">
        <div class="empty-icon">🔒</div>
        <h3 class="text-xl mt-4">请先登录</h3>
        <router-link to="/login" class="mt-6 inline-flex rounded-full bg-sky-400 px-5 py-3 text-sm font-semibold text-slate-950 no-underline transition hover:bg-sky-300">
          前往登录
        </router-link>
    </div>

    <div v-else class="grid gap-6 lg:grid-cols-[minmax(0,1fr)_280px]">
      <div class="rounded-[28px] border border-white/10 bg-slate-900/90 p-6 sm:p-8">
        <!-- 用户头像 -->
        <div class="flex flex-col gap-6 sm:flex-row sm:items-center mb-8">
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
            退出登录
          </button>
        </div>
      </div>

      <aside class="rounded-[28px] border border-white/10 bg-white/[0.04] p-6 lg:sticky lg:top-24">
        <p class="text-sm text-slate-400">账号信息</p>
        <p class="mt-4 text-xl font-semibold leading-8 text-white">布局在宽屏时保持两栏，在窄屏时自然折叠为单列。</p>
        <p class="mt-4 text-sm leading-7 text-slate-400">这样在笔记本和更宽的桌面屏幕上都不会只剩下一小块内容悬在左边。</p>
      </aside>
    </div>
  </PageShell>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import PageShell from '@/components/PageShell.vue'

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
  border-radius: 999px;
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
}
</style>
