<template>
  <div class="detail-page">
    <div class="detail-container">
      <!-- 加载中 -->
      <div v-if="loading" class="loading">
        <div class="spinner"></div>
        <p class="mt-4">加载中...</p>
      </div>

      <!-- 文章不存在 -->
      <div v-else-if="!blog" class="empty-state">
        <div class="empty-icon">🔍</div>
        <h3 class="text-xl mt-4">文章不存在</h3>
        <p class="mt-2" style="color: var(--text-secondary);">这篇文章可能已被删除或不存在</p>
        <router-link to="/" class="btn-outline inline-block mt-6">返回首页</router-link>
      </div>

      <!-- 文章内容 -->
      <div v-else class="article-wrapper">
        <!-- 返回 -->
        <button class="back-link mb-6" @click="$router.push('/')">
          ← 返回文章列表
        </button>

        <!-- 文章头部 -->
        <div class="article-header glass-card !p-8 mb-8">
          <h1 class="article-title">{{ blog.title }}</h1>
          <div class="article-meta">
            <div class="flex items-center gap-3">
              <div class="author-avatar">{{ blog.authorName?.charAt(0).toUpperCase() || '?' }}</div>
              <div>
                <div class="font-medium" style="color: var(--accent-primary);">{{ blog.authorName }}</div>
                <div class="text-xs mt-1" style="color: var(--text-secondary);">
                  📅 {{ formatDate(blog.createTime) }}
                </div>
              </div>
            </div>
            <div class="flex items-center gap-4">
              <span style="color: var(--text-secondary);">👁️ {{ blog.viewCount || 0 }} 次浏览</span>
              <div v-if="isOwner" class="flex gap-2">
                <router-link :to="'/edit/' + blog.id" class="btn-outline text-sm !px-4 !py-1.5 no-underline">
                  编辑
                </router-link>
                <button class="btn-danger text-sm !px-4 !py-1.5" @click="handleDelete">
                  删除
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 文章正文 -->
        <div class="article-content glass-card !p-8">
          <div class="content-markdown" v-html="renderedContent"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getBlogById, deleteBlog } from '@/api/blog'
import { showToast } from '@/utils/toast'
import MarkdownIt from 'markdown-it'

const md = new MarkdownIt({ html: true, breaks: true, linkify: true })

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const blog = ref(null)
const loading = ref(true)

const isOwner = computed(() => {
  if (!blog.value || !userStore.isLoggedIn) return false
  return blog.value.authorId === userStore.userId
})

const renderedContent = computed(() => {
  if (!blog.value?.content) return ''
  return md.render(blog.value.content)
})

const formatDate = (dateStr) => {
  const d = new Date(dateStr)
  return d.toLocaleDateString('zh-CN', {
    year: 'numeric', month: 'long', day: 'numeric',
    hour: '2-digit', minute: '2-digit',
  })
}

const fetchBlog = async () => {
  loading.value = true
  try {
    const res = await getBlogById(route.params.id)
    if (res.code === 200) {
      blog.value = res.data
    }
  } catch (e) {
    console.error('获取文章失败:', e)
  } finally {
    loading.value = false
  }
}

const handleDelete = async () => {
  if (!confirm('确定要删除这篇文章吗？')) return
  try {
    const res = await deleteBlog(route.params.id)
    if (res.code === 200) {
      showToast('删除成功', 'success')
      router.push('/')
    }
  } catch (e) {
    showToast(e.message || '删除失败', 'error')
  }
}

onMounted(fetchBlog)
</script>

<style scoped>
.detail-page {
  padding: 120px 24px 60px;
  min-height: 100vh;
}
.detail-container {
  max-width: 800px;
  margin: 0 auto;
}
.back-link {
  background: none;
  border: none;
  color: var(--accent-primary);
  font-size: 14px;
  cursor: pointer;
  padding: 8px 0;
  transition: all 0.3s;
}
.back-link:hover {
  color: var(--accent-glow);
  text-shadow: 0 0 10px rgba(34, 211, 238, 0.3);
}
.article-title {
  font-size: clamp(24px, 3vw, 36px);
  font-weight: 800;
  line-height: 1.3;
  margin-bottom: 20px;
  background: linear-gradient(135deg, var(--text-primary), var(--accent-primary));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}
.author-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--accent-primary), var(--accent-secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 16px;
  flex-shrink: 0;
}
.btn-outline {
  display: inline-flex;
  align-items: center;
  padding: 10px 20px;
  border-radius: 10px;
  border: 1px solid var(--border-color);
  color: var(--text-primary);
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  background: transparent;
}
.btn-outline:hover {
  border-color: var(--accent-primary);
  box-shadow: var(--glow-blue);
  transform: translateY(-2px);
}
.btn-danger {
  display: inline-flex;
  align-items: center;
  padding: 10px 20px;
  border-radius: 10px;
  border: 1px solid rgba(239, 68, 68, 0.4);
  color: #ef4444;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  background: rgba(239, 68, 68, 0.1);
}
.btn-danger:hover {
  background: rgba(239, 68, 68, 0.2);
  box-shadow: 0 0 20px rgba(239, 68, 68, 0.3);
  transform: translateY(-2px);
}

/* Markdown 内容样式 */
.content-markdown {
  line-height: 1.8;
  color: var(--text-primary);
}
.content-markdown :deep(h1),
.content-markdown :deep(h2),
.content-markdown :deep(h3) {
  margin: 24px 0 16px;
  font-weight: 700;
}
.content-markdown :deep(h1) { font-size: 28px; }
.content-markdown :deep(h2) { font-size: 22px; }
.content-markdown :deep(h3) { font-size: 18px; }
.content-markdown :deep(p) { margin: 12px 0; }
.content-markdown :deep(code) {
  background: rgba(56, 189, 248, 0.1);
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 14px;
  color: var(--accent-primary);
}
.content-markdown :deep(pre) {
  background: rgba(2, 6, 23, 0.8);
  padding: 20px;
  border-radius: 12px;
  overflow-x: auto;
  border: 1px solid var(--border-color);
  margin: 16px 0;
}
.content-markdown :deep(pre code) {
  background: none;
  padding: 0;
  color: var(--text-primary);
}
.content-markdown :deep(blockquote) {
  border-left: 3px solid var(--accent-primary);
  padding: 12px 20px;
  margin: 16px 0;
  background: rgba(56, 189, 248, 0.05);
  border-radius: 0 8px 8px 0;
  color: var(--text-secondary);
}
.content-markdown :deep(ul),
.content-markdown :deep(ol) {
  padding-left: 24px;
  margin: 12px 0;
}
.content-markdown :deep(li) {
  margin: 4px 0;
}
.content-markdown :deep(a) {
  color: var(--accent-primary);
  text-decoration: underline;
}
.content-markdown :deep(img) {
  max-width: 100%;
  border-radius: 12px;
  margin: 16px 0;
}
.content-markdown :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 16px 0;
}
.content-markdown :deep(th),
.content-markdown :deep(td) {
  padding: 10px 16px;
  border: 1px solid var(--border-color);
  text-align: left;
}
.content-markdown :deep(th) {
  background: rgba(56, 189, 248, 0.05);
  font-weight: 600;
}
.content-markdown :deep(hr) {
  border: none;
  height: 1px;
  background: var(--border-color);
  margin: 24px 0;
}
</style>
