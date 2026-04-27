<template>
  <PageShell subtitle="用户主页" show-nav max-width="6xl">
    <template #actions>
      <router-link
        to="/"
        class="rounded-full border border-white/10 px-4 py-2 text-sm text-slate-200 no-underline transition hover:border-white/20 hover:bg-white/5"
      >
        返回首页
      </router-link>
    </template>

    <div class="rounded-[28px] border border-white/10 bg-slate-900/90 p-6 sm:p-8 mb-8 flex flex-col gap-6 sm:flex-row sm:items-center">
        <div class="user-avatar-lg">
          {{ (userInfo?.username || username).charAt(0).toUpperCase() }}
        </div>
        <div>
          <h1 class="text-3xl font-semibold tracking-tight">{{ userInfo?.username || username }}</h1>
          <p class="text-sm mt-2 leading-7" style="color: var(--text-secondary);">
            用户主页 · 共 {{ blogs.length }} 篇文章
          </p>
        </div>
      </div>

      <div v-if="errorMsg" class="create-alert mb-6">
        <span>⚠️</span> {{ errorMsg }}
      </div>

      <div class="mb-8">
        <h2 class="text-2xl font-semibold text-white">{{ userInfo?.username || username }} 的文章</h2>
        <p class="mt-2 text-sm leading-7 text-slate-400">列表区在宽屏下展开，在窄屏下自动压回单列，不会出现卡片宽度忽大忽小的情况。</p>
      </div>

      <div v-if="loading" class="loading">
        <div class="spinner"></div>
        <p class="mt-4">加载中...</p>
      </div>

      <div v-else-if="blogs.length === 0" class="empty-state">
        <div class="empty-icon">📭</div>
        <p class="mt-4">暂无文章</p>
      </div>

      <div v-else class="blog-grid">
        <div
          v-for="blog in blogs"
          :key="blog.id"
          class="blog-card"
          @click="$router.push('/blog/' + blog.id)"
        >
          <h3 class="text-lg font-semibold mb-2">{{ blog.title }}</h3>
          <p class="text-sm mb-3" style="color: var(--text-secondary);">
            {{ getExcerpt(blog.content) }}
          </p>
          <div class="flex items-center justify-between text-xs" style="color: var(--text-secondary);">
            <span>{{ formatDate(blog.createTime) }}</span>
            <span>👁️ {{ blog.viewCount }}</span>
          </div>
        </div>
      </div>
  </PageShell>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getUserBlogList } from '@/api/blog'
import { getUserByUsername } from '@/api/user'
import PageShell from '@/components/PageShell.vue'

const route = useRoute()
const username = ref(route.params.username)
const blogs = ref([])
const loading = ref(true)
const userInfo = ref(null)
const errorMsg = ref('')

const getExcerpt = (content) => {
  return content?.replace(/[#*`\n]/g, ' ').trim().substring(0, 100) + '...' || ''
}

const formatDate = (dateStr) => {
  const d = new Date(dateStr)
  return d.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}

onMounted(async () => {
  try {
    // 先通过用户名查用户信息拿到 userId
    const userRes = await getUserByUsername(route.params.username)
    if (userRes.code === 200 && userRes.data) {
      userInfo.value = userRes.data
      // 再用 userId 查文章列表
      const blogRes = await getUserBlogList(userRes.data.id, { page: 1, size: 20 })
      if (blogRes.code === 200) {
        blogs.value = blogRes.data.records || []
      }
    } else {
      errorMsg.value = '用户不存在'
    }
  } catch (e) {
    console.error('获取用户文章失败:', e)
    errorMsg.value = '获取数据失败'
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.user-avatar-lg {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--accent-primary), var(--accent-secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 700;
  flex-shrink: 0;
  border: 3px solid rgba(167, 139, 250, 0.3);
  box-shadow: var(--glow-purple);
}
.blog-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 20px;
}
.blog-card {
  background: var(--bg-card);
  backdrop-filter: var(--glass-blur);
  border: 1px solid var(--border-color);
  border-radius: 16px;
  padding: 20px;
  transition: all 0.3s ease;
  cursor: pointer;
}
.blog-card:hover {
  border-color: rgba(167, 139, 250, 0.3);
  box-shadow: var(--glow-purple);
  transform: translateY(-4px);
}
.create-alert {
  padding: 12px 16px;
  border-radius: 12px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  color: #fca5a5;
  font-size: 14px;
}
</style>
