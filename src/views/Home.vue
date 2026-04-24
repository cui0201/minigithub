<template>
  <div class="home-page">
    <!-- ========== HERO区 ========== -->
    <section class="hero-section">
      <div class="hero-content text-center max-w-3xl mx-auto px-6">
        <div class="hero-badge mb-4">✨ 开发者博客</div>
        <h1 class="hero-title">
          <span ref="typingRef" class="typing-text"></span>
        </h1>
        <p class="hero-subtitle">
          在代码的宇宙中，每一个想法都是一颗星辰
        </p>
        <div class="hero-actions mt-8 flex justify-center gap-4">
          <router-link to="/create" class="btn-glow no-underline">📝 开始写文章</router-link>
          <a href="#blogs" class="btn-outline no-underline" @click.prevent="scrollToBlogs">↓ 浏览文章</a>
        </div>
      </div>
      <!-- 装饰光效 -->
      <div class="hero-glow-1"></div>
      <div class="hero-glow-2"></div>
    </section>

    <!-- ========== 文章列表 ========== -->
    <section id="blogs" class="content-section">
      <div class="section-container">
        <div class="flex items-center justify-between mb-8">
          <h2 class="section-title">📝 最新文章</h2>
          <router-link v-if="userStore.isLoggedIn" to="/create" class="btn-glow text-sm !px-5 !py-2 no-underline">
            + 发布文章
          </router-link>
        </div>

        <!-- 加载中 -->
        <div v-if="loading" class="loading">
          <div class="spinner"></div>
          <p class="mt-4">加载中...</p>
        </div>

        <!-- 空状态 -->
        <div v-else-if="blogs.length === 0" class="empty-state">
          <div class="empty-icon">📭</div>
          <h3 class="text-xl mt-4">暂无文章</h3>
          <p class="mt-2">还没有人发布文章，快来发布第一篇吧！</p>
          <router-link v-if="userStore.isLoggedIn" to="/create" class="btn-glow inline-block mt-6 no-underline">
            发布文章
          </router-link>
        </div>

        <!-- 文章卡片网格 -->
        <div v-else class="blog-grid">
          <div
            v-for="blog in blogs"
            :key="blog.id"
            class="blog-card"
            @click="goToBlog(blog.id)"
          >
            <div class="flex items-center gap-2 mb-3">
              <div class="author-avatar-sm">{{ blog.authorName.charAt(0).toUpperCase() }}</div>
              <span class="text-sm" style="color: var(--accent-primary);">{{ blog.authorName }}</span>
              <span class="text-xs" style="color: var(--text-secondary);">· {{ formatDate(blog.createTime) }}</span>
            </div>
            <h3 class="text-lg font-semibold mb-2 line-clamp-2">{{ blog.title }}</h3>
            <p class="text-sm mb-4 line-clamp-3" style="color: var(--text-secondary);">
              {{ getExcerpt(blog.content) }}
            </p>
            <div class="flex items-center justify-between">
              <div class="flex gap-1">
                <span class="card-tag">技术</span>
                <span class="card-tag">博客</span>
              </div>
              <span class="text-xs" style="color: var(--text-secondary);">👁️ {{ blog.viewCount }}</span>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination" v-if="totalPages > 1">
          <button
            class="page-btn"
            :disabled="currentPage <= 1"
            @click="goToPage(currentPage - 1)"
          >
            ← 上一页
          </button>
          <button
            v-for="p in displayPages"
            :key="p"
            class="page-btn"
            :class="{ active: p === currentPage }"
            @click="goToPage(p)"
          >
            {{ p }}
          </button>
          <button
            class="page-btn"
            :disabled="currentPage >= totalPages"
            @click="goToPage(currentPage + 1)"
          >
            下一页 →
          </button>
        </div>
      </div>
    </section>

    <!-- ========== 标签云 ========== -->
    <section class="content-section" style="background: rgba(15, 23, 42, 0.3);">
      <div class="section-container">
        <h2 class="section-title text-center">🏷️ 技术标签</h2>
        <div class="tag-cloud">
          <span
            v-for="tag in tags"
            :key="tag"
            class="tag-item"
            :style="{ fontSize: tagSize(tag) + 'px' }"
          >
            {{ tag }}
          </span>
        </div>
      </div>
    </section>

    <!-- ========== Footer ========== -->
    <footer class="site-footer">
      <div class="max-w-4xl mx-auto">
        <div class="flex justify-center gap-6 mb-4">
          <a href="https://github.com" target="_blank" class="footer-link">GitHub</a>
          <router-link to="/" class="footer-link">首页</router-link>
        </div>
        <p>
          Built with <span class="heart">❤</span> using Vue3 + Spring Cloud
        </p>
        <p class="mt-2 text-xs opacity-50">MiniGitHub © 2026</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getBlogList } from '@/api/blog'
import Typed from 'typed.js'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const typingRef = ref(null)

const blogs = ref([])
const loading = ref(true)
const currentPage = ref(1)
const totalPages = ref(1)
const total = ref(0)

const tags = ['Vue3', 'Spring Boot', 'Java 21', 'MySQL', '微服务', 'Docker', 'Nginx', 'JWT', 'REST API', 'TypeScript', 'Tailwind CSS', 'Git']

const tagSize = (tag) => {
  const sizes = [14, 16, 18, 20, 22, 24]
  const idx = tag.length % sizes.length
  return sizes[idx]
}

const displayPages = computed(() => {
  const pages = []
  const start = Math.max(1, currentPage.value - 2)
  const end = Math.min(totalPages.value, start + 4)
  for (let i = start; i <= end; i++) pages.push(i)
  return pages
})

const getExcerpt = (content) => {
  return content.replace(/[#*`\n]/g, ' ').trim().substring(0, 120) + (content.length > 120 ? '...' : '')
}

const formatDate = (dateStr) => {
  const d = new Date(dateStr)
  return d.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}

const goToBlog = (id) => {
  router.push({ name: 'BlogDetail', params: { id } })
}

const goToPage = (page) => {
  currentPage.value = page
  fetchBlogs()
  window.scrollTo({ top: document.getElementById('blogs')?.offsetTop - 80, behavior: 'smooth' })
}

const scrollToBlogs = () => {
  document.getElementById('blogs')?.scrollIntoView({ behavior: 'smooth' })
}

const fetchBlogs = async () => {
  loading.value = true
  try {
    const res = await getBlogList({ page: currentPage.value, size: 10 })
    if (res.code === 200) {
      blogs.value = res.data.records || []
      total.value = res.data.total || 0
      totalPages.value = Math.ceil(total.value / 10) || 1
      if (currentPage.value > totalPages.value) {
        currentPage.value = totalPages.value
      }
    }
  } catch (e) {
    console.error('获取文章列表失败:', e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchBlogs()
  // 打字机效果
  nextTick(() => {
    if (typingRef.value) {
      new Typed(typingRef.value, {
        strings: [
          'Hello, 我是 ^1000  <span style="color: #38bdf8">媛媛</span>',
          '一个 ^500  <span style="color: #a78bfa">快乐显眼包</span> ^1000  &amp; ^500  <span style="color: #22d3ee">代码造梦师</span>',
          '欢迎来到我的 ^800  <span style="color: #38bdf8">数字宇宙</span> 🚀'
        ],
        typeSpeed: 60,
        backSpeed: 30,
        backDelay: 2000,
        loop: true,
        cursorChar: '|',
        contentType: 'html'
      })
    }
  })
})
</script>

<style scoped>
.hero-content {
  position: relative;
  z-index: 2;
}
.hero-badge {
  display: inline-block;
  padding: 6px 20px;
  border-radius: 20px;
  font-size: 13px;
  background: rgba(56, 189, 248, 0.1);
  border: 1px solid rgba(56, 189, 248, 0.3);
  color: var(--accent-primary);
}
.hero-title {
  font-size: clamp(28px, 5vw, 52px);
  font-weight: 800;
  line-height: 1.2;
  min-height: 2.4em;
  display: flex;
  align-items: center;
  justify-content: center;
}
.typing-text {
  display: inline;
}
.hero-subtitle {
  font-size: 18px;
  margin-top: 16px;
  color: var(--text-secondary);
}
.hero-actions a {
  font-size: 15px;
}
.btn-outline {
  display: inline-flex;
  align-items: center;
  padding: 12px 28px;
  border-radius: 12px;
  border: 1px solid var(--border-color);
  color: var(--text-primary);
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}
.btn-outline:hover {
  border-color: var(--accent-primary);
  box-shadow: var(--glow-blue);
  transform: translateY(-2px);
}
.hero-glow-1 {
  position: absolute;
  width: 400px;
  height: 400px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(56, 189, 248, 0.08) 0%, transparent 70%);
  top: -100px;
  right: -100px;
  z-index: 0;
  animation: glowFloat 8s ease-in-out infinite;
}
.hero-glow-2 {
  position: absolute;
  width: 300px;
  height: 300px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(167, 139, 250, 0.08) 0%, transparent 70%);
  bottom: -50px;
  left: -80px;
  z-index: 0;
  animation: glowFloat 10s ease-in-out infinite reverse;
}
@keyframes glowFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(30px, -20px) scale(1.1); }
}

/* Content section */
.content-section {
  padding: 80px 24px;
  position: relative;
}
.section-container {
  max-width: 1200px;
  margin: 0 auto;
}

/* Author avatar */
.author-avatar-sm {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--accent-primary), var(--accent-secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 700;
  flex-shrink: 0;
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 40px;
  flex-wrap: wrap;
}
.page-btn {
  padding: 8px 16px;
  border-radius: 10px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  color: var(--text-primary);
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
}
.page-btn:hover:not(:disabled) {
  border-color: var(--accent-primary);
  box-shadow: var(--glow-blue);
}
.page-btn.active {
  background: linear-gradient(135deg, rgba(56, 189, 248, 0.2), rgba(167, 139, 250, 0.2));
  border-color: var(--accent-primary);
}
.page-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.footer-link {
  color: var(--text-secondary);
  text-decoration: none;
  transition: color 0.3s;
  font-size: 14px;
}
.footer-link:hover {
  color: var(--accent-primary);
}
</style>
