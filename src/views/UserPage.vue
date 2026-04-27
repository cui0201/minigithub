<template>
  <div class="user-page">
    <div class="user-container">
      <div class="glass-card !p-8 mb-8 flex items-center gap-6">
        <div class="user-avatar-lg">
          {{ (userInfo?.username || username).charAt(0).toUpperCase() }}
        </div>
        <div>
          <h1 class="text-2xl font-bold">{{ userInfo?.username || username }}</h1>
          <p class="text-sm mt-1" style="color: var(--text-secondary);">
            👤 用户主页 · 共 {{ blogs.length }} 篇文章
          </p>
        </div>
      </div>

      <div v-if="errorMsg" class="create-alert mb-6">
        <span>⚠️</span> {{ errorMsg }}
      </div>

      <h2 class="section-title">📝 {{ userInfo?.username || username }} 的文章</h2>

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
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getUserBlogList } from '@/api/blog'
import { getUserByUsername } from '@/api/user'

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
.user-page {
  padding: 120px 24px 60px;
  min-height: 100vh;
}
.user-container {
  max-width: 800px;
  margin: 0 auto;
}
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
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
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
