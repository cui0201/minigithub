<template>
  <div class="min-h-screen bg-gradient-to-b from-[#020617] via-[#0a0f2e] to-[#020617] relative overflow-hidden">
    <!-- ===== 粒子背景 ===== -->
    <div class="absolute inset-0 z-0">
      <ParticlesBg />
    </div>

    <!-- ===== 鼠标视差光晕 ===== -->
    <div
      class="fixed pointer-events-none z-10 w-[600px] h-[600px] rounded-full bg-gradient-to-r from-sky-500/5 to-purple-500/5 blur-[120px] transition-all duration-1000 ease-out"
      :style="{ left: mouseX - 300 + 'px', top: mouseY - 300 + 'px' }"
    ></div>

    <!-- ===== 导航栏 ===== -->
    <nav
      class="fixed top-0 left-0 right-0 z-50 transition-all duration-300 px-4 sm:px-8"
      :class="{ 'navbar scrolled': scrolled }"
    >
      <div class="max-w-7xl mx-auto flex items-center justify-between h-16">
        <!-- Logo -->
        <router-link to="/" class="flex items-center gap-2 no-underline group">
          <span class="text-2xl">💻</span>
          <span class="text-xl font-bold bg-gradient-to-r from-sky-400 via-blue-400 to-purple-400 bg-clip-text text-transparent">
            MiniGitHub
          </span>
        </router-link>

        <!-- Nav Links -->
        <div class="hidden md:flex items-center gap-8">
          <router-link to="/" class="text-white/60 hover:text-white transition-colors text-sm">首页</router-link>
          <router-link to="/create" class="text-white/60 hover:text-white transition-colors text-sm">写文章</router-link>
        </div>

        <!-- User -->
        <div class="flex items-center gap-3">
          <template v-if="userStore.isLoggedIn">
            <router-link to="/create" class="btn-glow text-sm !px-4 !py-2 !rounded-lg no-underline">
              + 新文章
            </router-link>
            <router-link to="/profile" class="flex items-center gap-2 no-underline group">
              <div class="w-8 h-8 rounded-full bg-gradient-to-br from-sky-400 to-purple-400 flex items-center justify-center text-xs font-bold text-white shadow-lg shadow-sky-400/20">
                {{ (userStore.username || 'U').charAt(0).toUpperCase() }}
              </div>
              <span class="text-sm text-white/60 group-hover:text-white hidden sm:block">
                {{ userStore.username }}
              </span>
            </router-link>
          </template>
          <template v-else>
            <router-link to="/login" class="text-white/60 hover:text-white transition-colors text-sm">登录</router-link>
            <router-link to="/register" class="text-white/60 hover:text-white transition-colors text-sm border border-white/10 px-4 py-2 rounded-lg hover:border-white/20">注册</router-link>
          </template>
        </div>
      </div>
    </nav>

    <!-- ===== Hero 区 ===== -->
    <section class="hero-section flex flex-col items-center justify-center text-center px-6 pt-20">
      <div class="max-w-4xl mx-auto">
        <div class="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-sky-400/10 border border-sky-400/20 text-sky-400 text-sm mb-8">
          <span class="w-2 h-2 rounded-full bg-sky-400 animate-pulse"></span>
          开发者博客 · 用代码说话
        </div>

        <h1 class="hero-title text-5xl sm:text-6xl md:text-7xl font-bold leading-tight mb-6">
          <span class="text-white/90">在</span>
          <span class="bg-gradient-to-r from-sky-400 via-blue-400 to-purple-400 bg-clip-text text-transparent">代码</span>
          <span class="text-white/90">的宇宙中</span>
          <br />
          <span class="text-white/90">每一个想法都是</span>
          <span class="bg-gradient-to-r from-amber-300 to-orange-400 bg-clip-text text-transparent">一颗星辰</span>
          <span class="text-white/90">✨</span>
        </h1>

        <p class="text-lg text-white/40 max-w-2xl mx-auto mb-10">
          记录技术思考，分享开发经验，汇聚每一个灵感的火花
        </p>

        <div class="flex items-center justify-center gap-4 flex-wrap">
          <router-link to="/create" class="btn-glow no-underline">
            📝 开始写文章
          </router-link>
          <a href="#blogs" class="px-8 py-3.5 rounded-xl border border-white/10 text-white/70 hover:text-white hover:border-white/20 hover:bg-white/5 transition-all no-underline cursor-pointer">
            ↓ 浏览文章
          </a>
        </div>
      </div>
    </section>

    <!-- ===== 文章列表区 ===== -->
    <section id="blogs" class="px-6 pb-24 relative z-20">
      <div class="max-w-7xl mx-auto">
        <div class="flex items-center justify-between mb-10">
          <h2 class="text-2xl font-bold text-white/90">
            📝 最新文章
            <span class="text-sm font-normal text-white/30 ml-3">共 {{ total }} 篇</span>
          </h2>
        </div>

        <!-- Loading Skeleton -->
        <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div v-for="i in 6" :key="i" class="blog-card animate-pulse">
            <div class="h-4 bg-white/5 rounded w-3/4 mb-4"></div>
            <div class="h-3 bg-white/5 rounded w-1/2 mb-6"></div>
            <div class="h-3 bg-white/5 rounded w-full mb-2"></div>
            <div class="h-3 bg-white/5 rounded w-2/3"></div>
          </div>
        </div>

        <!-- Empty -->
        <div v-else-if="blogs.length === 0" class="empty-state">
          <div class="empty-icon">📭</div>
          <h3 class="text-xl text-white/70 mb-2">暂无文章</h3>
          <p class="text-white/40 mb-8">还没有人发布文章，快来发布第一篇吧！</p>
          <router-link to="/create" class="btn-glow no-underline">发布文章</router-link>
        </div>

        <!-- Blog Cards -->
        <div v-else class="blog-grid">
          <div
            v-for="blog in blogs"
            :key="blog.id"
            @click="openBlog(blog)"
            :class="['blog-card', { 'is-private': blog.visibility === 'private' }]"
          >
            <div class="flex items-center gap-2 text-xs text-white/30 mb-3">
              <div class="w-5 h-5 rounded-full bg-gradient-to-br from-sky-400 to-purple-400 flex items-center justify-center text-[8px] font-bold text-white">
                {{ (blog.authorName || '?').charAt(0).toUpperCase() }}
              </div>
              <span>{{ blog.authorName }}</span>
              <span>·</span>
              <span>{{ formatDate(blog.createTime) }}</span>
            </div>

            <h3 class="text-lg font-semibold text-white/90 mb-2 group-hover:text-white">
              {{ blog.title }}
            </h3>

            <p class="text-sm text-white/40 mb-4 line-clamp-3">
              {{ getExcerpt(blog.content) }}
            </p>

            <div class="flex items-center justify-between mt-auto">
              <div class="flex gap-1">
                <span class="card-tag">技术</span>
                <span class="card-tag">博客</span>
              </div>
              <div class="flex items-center gap-2 text-xs text-white/30">
                <span>👁️ {{ blog.viewCount || 0 }}</span>
                <span v-if="blog.visibility === 'private'" class="text-amber-400/60">🔒 私密</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Pagination -->
        <div v-if="totalPages > 1" class="flex justify-center gap-3 mt-12">
          <button
            v-for="p in displayPages"
            :key="p"
            @click="goToPage(p)"
            class="w-10 h-10 rounded-xl text-sm transition-all"
            :class="p === currentPage
              ? 'bg-gradient-to-r from-sky-500/20 to-purple-500/20 border border-sky-500/30 text-sky-400'
              : 'bg-white/5 border border-white/10 text-white/50 hover:text-white/80 hover:border-white/20'"
          >
            {{ p }}
          </button>
        </div>
      </div>
    </section>

    <!-- Private Modal -->
    <Teleport to="body">
      <Transition name="modal">
        <div v-if="showPrivateModal" class="fixed inset-0 z-[100] flex items-center justify-center" @click.self="showPrivateModal = false">
          <div class="absolute inset-0 bg-black/60 backdrop-blur-sm"></div>
          <div class="relative glass-card !p-10 text-center max-w-sm w-full mx-4">
            <div class="text-6xl mb-6">🔒</div>
            <h3 class="text-2xl font-bold text-white/90 mb-3">This post is private</h3>
            <p class="text-white/50 mb-8 leading-relaxed">Only the author can view this article.</p>
            <div class="flex gap-3 justify-center">
              <router-link to="/login" class="btn-glow !px-6 !py-2.5 !rounded-xl no-underline" @click="showPrivateModal = false">登录</router-link>
              <button class="px-6 py-2.5 rounded-xl border border-white/10 text-white/70 hover:bg-white/5 transition-all" @click="showPrivateModal = false">关闭</button>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>

    <!-- Footer -->
    <footer class="site-footer">
      Built with ❤️ using <span class="heart">❤</span> Vue3 + Spring Cloud<br />
      <span class="text-xs text-white/20 mt-2 block">MiniGitHub © 2026</span>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getBlogList } from '@/api/blog'
import ParticlesBg from '@/components/ParticlesBg.vue'

const router = useRouter()
const userStore = useUserStore()

const blogs = ref([])
const loading = ref(true)
const currentPage = ref(1)
const total = ref(0)
const totalPages = ref(1)
const scrolled = ref(false)
const showPrivateModal = ref(false)

const mouseX = ref(0)
const mouseY = ref(0)

const handleMouseMove = (e) => {
  mouseX.value = e.clientX
  mouseY.value = e.clientY
}

const handleScroll = () => {
  scrolled.value = window.scrollY > 60
}

onMounted(() => {
  window.addEventListener('mousemove', handleMouseMove, { passive: true })
  window.addEventListener('scroll', handleScroll, { passive: true })
  fetchBlogs()
})

onUnmounted(() => {
  window.removeEventListener('mousemove', handleMouseMove)
  window.removeEventListener('scroll', handleScroll)
})

const displayPages = computed(() => {
  const pages = []
  const start = Math.max(1, currentPage.value - 2)
  const end = Math.min(totalPages.value, start + 4)
  for (let i = start; i <= end; i++) pages.push(i)
  return pages
})

const fetchBlogs = async () => {
  loading.value = true
  try {
    const res = await getBlogList({ page: currentPage.value, size: 12 })
    if (res.code === 200) {
      blogs.value = res.data.records || []
      total.value = res.data.total || 0
      totalPages.value = Math.ceil(total.value / 12) || 1
    }
  } catch (e) {
    console.error('获取文章失败:', e)
  } finally {
    loading.value = false
  }
}

const goToPage = (page) => {
  currentPage.value = page
  fetchBlogs()
  document.getElementById('blogs')?.scrollIntoView({ behavior: 'smooth' })
}

const getExcerpt = (content) => {
  if (!content) return ''
  return content.replace(/[#*`\n\[\]]/g, ' ').trim().substring(0, 100) + (content.length > 100 ? '...' : '')
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return d.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' })
}

const openBlog = (blog) => {
  if (blog.visibility === 'private' && blog.authorId !== userStore.userId) {
    showPrivateModal.value = true
    return
  }
  router.push({ name: 'BlogDetail', params: { id: blog.id } })
}
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.modal-enter-active,
.modal-leave-active {
  transition: all 0.3s ease;
}
.modal-enter-from,
.modal-leave-to {
  opacity: 0;
  transform: scale(0.95);
}
</style>
