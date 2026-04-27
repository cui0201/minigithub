<template>
  <div class="min-h-screen bg-gradient-to-b from-[#020617] via-[#0a0f2e] to-[#020617] relative">
    <!-- 粒子背景 -->
    <div class="absolute inset-0 z-0">
      <ParticlesBg />
    </div>

    <div class="relative z-10">
      <!-- Nav -->
      <nav class="flex items-center justify-between px-4 sm:px-8 py-4 max-w-5xl mx-auto">
        <router-link to="/" class="flex items-center gap-2 no-underline group">
          <span class="text-2xl">💻</span>
          <span class="text-lg font-bold bg-gradient-to-r from-sky-400 via-blue-400 to-purple-400 bg-clip-text text-transparent">
            MiniGitHub
          </span>
        </router-link>
        <div class="flex items-center gap-3">
          <template v-if="userStore.isLoggedIn">
            <button v-if="isAuthor" @click="editBlog" class="px-4 py-2 rounded-lg border border-white/10 text-white/70 hover:text-white hover:border-white/20 text-sm transition-all">编辑</button>
            <button v-if="isAuthor" @click="deleteBlog" class="px-4 py-2 rounded-lg border border-white/10 text-red-400/70 hover:text-red-400 hover:border-red-400/30 text-sm transition-all">删除</button>
          </template>
        </div>
      </nav>

      <!-- Loading -->
      <div v-if="loading" class="max-w-3xl mx-auto px-6 py-12">
        <div class="animate-pulse space-y-4">
          <div class="h-8 bg-white/5 rounded w-3/4"></div>
          <div class="h-4 bg-white/5 rounded w-1/4"></div>
          <div class="h-4 bg-white/5 rounded w-full mt-8"></div>
          <div class="h-4 bg-white/5 rounded w-full"></div>
          <div class="h-4 bg-white/5 rounded w-2/3"></div>
        </div>
      </div>

      <!-- Private Notice -->
      <div v-else-if="isPrivate" class="max-w-3xl mx-auto px-6 py-16 text-center">
        <div class="glass-card !p-12 max-w-sm mx-auto">
          <div class="text-6xl mb-6">🔒</div>
          <h2 class="text-2xl font-bold text-white/90 mb-2">This content is private</h2>
          <p class="text-white/50 mb-8">Only the author can view this post.</p>
          <router-link to="/login" class="btn-glow no-underline">Sign in</router-link>
        </div>
      </div>

      <!-- Content -->
      <div v-else class="max-w-3xl mx-auto px-6 py-8">
        <div class="glass-card !p-8 sm:!p-12">
          <!-- Meta -->
          <div class="flex items-center gap-3 text-sm text-white/30 mb-6 pb-6 border-b border-white/5">
            <div class="w-8 h-8 rounded-full bg-gradient-to-br from-sky-400 to-purple-400 flex items-center justify-center text-xs font-bold text-white">
              {{ (blog.authorName || 'A').charAt(0).toUpperCase() }}
            </div>
            <span class="text-white/60">{{ blog.authorName }}</span>
            <span>·</span>
            <span>{{ formatDate(blog.createTime) }}</span>
            <span v-if="blog.visibility === 'private'" class="ml-auto flex items-center gap-1 text-amber-400/60">
              🔒 私密
            </span>
          </div>

          <!-- Title -->
          <h1 class="text-3xl sm:text-4xl font-bold text-white/90 mb-8 leading-tight">{{ blog.title }}</h1>

          <!-- Content -->
          <div class="prose prose-invert max-w-none" v-html="renderedContent"></div>
        </div>

        <!-- Back -->
        <div class="mt-8 text-center">
          <router-link to="/" class="text-white/40 hover:text-white/70 text-sm transition-colors">
            ← 返回首页
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getBlogById, deleteBlog as apiDeleteBlog } from '@/api/blog'
import { ElMessageBox } from 'element-plus'
import ParticlesBg from '@/components/ParticlesBg.vue'
import { marked } from 'marked'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const blog = ref({})
const loading = ref(true)
const isPrivate = ref(false)
const isAuthor = ref(false)
const renderedContent = ref('')

const fetchBlog = async () => {
  loading.value = true
  isPrivate.value = false
  try {
    const res = await getBlogById(route.params.id)
    if (res.code === 403) {
      isPrivate.value = true
      return
    }
    if (res.code === 200 && res.data) {
      blog.value = res.data
      isAuthor.value = res.data.authorId === userStore.userId
      renderMarkdown()
    }
  } catch (e) {
    console.error('获取文章失败:', e)
  } finally {
    loading.value = false
  }
}

const renderMarkdown = () => {
  if (!blog.value.content) return ''
  try {
    renderedContent.value = marked(blog.value.content)
  } catch {
    renderedContent.value = blog.value.content.replace(/\n/g, '<br/>')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return d.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}

const editBlog = () => router.push({ name: 'EditBlog', params: { id: blog.value.id } })

const deleteBlog = async () => {
  try {
    await ElMessageBox.confirm('确定要删除这篇文章吗？', '确认删除', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    })
    const res = await apiDeleteBlog(blog.value.id)
    if (res.code === 200) {
      router.push('/')
    }
  } catch {
    // cancelled
  }
}

onMounted(() => fetchBlog())
watch(() => route.params.id, fetchBlog)
</script>

<style scoped>
/* Prose 风格 */
:deep(.prose) h1 { @apply text-2xl font-bold text-white/90 mt-8 mb-4; }
:deep(.prose) h2 { @apply text-xl font-bold text-white/85 mt-6 mb-3 pb-2 border-b border-white/5; }
:deep(.prose) h3 { @apply text-lg font-semibold text-white/80 mt-5 mb-2; }
:deep(.prose) p { @apply text-white/60 leading-relaxed mb-4; }
:deep(.prose) a { @apply text-sky-400 hover:text-sky-300; }
:deep(.prose) strong { @apply text-white/80; }
:deep(.prose) code { @apply px-2 py-0.5 rounded bg-white/5 text-sm text-sky-400 font-mono; }
:deep(.prose) pre { @apply bg-white/5 border border-white/5 rounded-xl p-4 overflow-x-auto mb-4; }
:deep(.prose) pre code { @apply bg-transparent p-0 text-white/60; }
:deep(.prose) blockquote { @apply border-l-4 border-sky-400/30 pl-4 text-white/40 italic my-4; }
:deep(.prose) ul, :deep(.prose) ol { @apply pl-5 text-white/60 mb-4; }
:deep(.prose) li { @apply mb-1; }
:deep(.prose) img { @apply rounded-xl max-w-full my-6 border border-white/5; }
:deep(.prose) hr { @apply border-white/5 my-8; }
</style>
