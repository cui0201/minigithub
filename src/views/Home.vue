<template>
  <div class="min-h-screen bg-slate-950 text-slate-100">
    <AppHeader show-nav subtitle="开发者写作空间">
      <template #actions>
        <template v-if="userStore.isLoggedIn">
          <router-link
            to="/create"
            class="inline-flex items-center rounded-xl bg-sky-400 px-4 py-2 text-sm font-semibold text-slate-950 no-underline transition hover:bg-sky-300"
          >
            写文章
          </router-link>
          <router-link
            to="/profile"
            class="inline-flex items-center gap-2 rounded-xl border border-white/10 px-3 py-2 text-sm text-slate-200 no-underline transition hover:border-white/20 hover:bg-white/5"
          >
            <span class="flex h-7 w-7 items-center justify-center rounded-full bg-white/10 text-xs font-semibold">
              {{ (userStore.username || 'U').charAt(0).toUpperCase() }}
            </span>
            <span class="hidden sm:inline">{{ userStore.username }}</span>
          </router-link>
        </template>
        <template v-else>
          <router-link to="/login" class="text-sm text-slate-300 no-underline transition hover:text-white">登录</router-link>
          <router-link
            to="/register"
            class="rounded-xl border border-white/10 px-4 py-2 text-sm text-slate-100 no-underline transition hover:border-white/20 hover:bg-white/5"
          >
            注册
          </router-link>
        </template>
      </template>
    </AppHeader>

    <main class="flex flex-col items-center px-4 pb-20 pt-14 sm:px-6 sm:pt-16 lg:px-8 lg:pt-20">
      <section class="relative w-full max-w-7xl overflow-hidden rounded-[36px] border border-white/10 bg-[radial-gradient(circle_at_top,_rgba(56,189,248,0.2),_transparent_24%),radial-gradient(circle_at_20%_80%,_rgba(255,255,255,0.06),_transparent_22%),linear-gradient(180deg,rgba(15,23,42,0.98),rgba(2,6,23,1))] px-6 py-20 sm:px-10 sm:py-24 lg:px-16 lg:py-28">
        <div class="pointer-events-none absolute inset-x-10 top-0 h-px bg-gradient-to-r from-transparent via-white/20 to-transparent"></div>
        <div class="pointer-events-none absolute left-1/2 top-10 h-56 w-56 -translate-x-1/2 rounded-full bg-sky-400/10 blur-3xl"></div>

        <div class="relative mx-auto flex max-w-5xl flex-col items-center text-center">
          <div class="mb-8 inline-flex items-center rounded-full border border-sky-400/20 bg-sky-400/10 px-4 py-1.5 text-xs font-medium uppercase tracking-[0.24em] text-sky-200">
            MiniGitHub Notes
          </div>
          <h1 class="hero-title mx-auto max-w-4xl text-[2.2rem] font-semibold leading-[1.16] text-white sm:text-[3.6rem] lg:text-[5rem]">
            把技术写作的重心，放回页面正中央。
          </h1>
          <p class="hero-copy mx-auto mt-8 max-w-2xl text-base leading-8 text-slate-300 sm:text-lg">
            首页先负责建立阅读气氛，再让文章和写作入口自然落到视线中心。文字需要透气，结构也需要退后。
          </p>

          <div class="mt-14 flex flex-wrap items-center justify-center gap-4">
            <router-link
              to="/create"
              class="inline-flex items-center rounded-full bg-sky-400 px-7 py-3 text-sm font-semibold text-slate-950 no-underline transition hover:bg-sky-300"
            >
              开始写文章
            </router-link>
            <a
              href="#articles"
              class="inline-flex items-center rounded-full border border-white/10 px-7 py-3 text-sm font-medium text-slate-200 no-underline transition hover:border-white/20 hover:bg-white/5"
            >
              浏览文章
            </a>
          </div>
        </div>

        <div class="relative mx-auto mt-20 grid max-w-5xl gap-5 lg:grid-cols-3">
          <div class="rounded-[26px] border border-white/10 bg-white/[0.04] p-6 text-left backdrop-blur-sm">
            <p class="text-sm text-slate-400">公开文章</p>
            <p class="mt-4 text-4xl font-semibold tracking-tight text-white">{{ total }}</p>
            <p class="mt-4 text-sm leading-7 text-slate-400">首页流只保留公开内容，避免私密文章混进公共阅读区。</p>
          </div>

          <div class="rounded-[26px] border border-white/10 bg-white/[0.04] p-6 text-left backdrop-blur-sm">
            <p class="text-sm text-slate-400">当前状态</p>
            <p class="mt-4 text-xl font-semibold leading-8 text-white">
              {{ userStore.isLoggedIn ? '已登录，可以直接开始写作。' : '未登录，写作前需要先登录。' }}
            </p>
            <p class="mt-4 text-sm leading-7 text-slate-400">保持路径简单，减少用户在入口阶段的犹豫。</p>
          </div>

          <div class="rounded-[26px] border border-white/10 bg-white/[0.04] p-6 text-left backdrop-blur-sm">
            <p class="text-sm text-slate-400">阅读体验</p>
            <p class="mt-4 text-xl font-semibold leading-8 text-white">写作、预览、详情页走同一套 Markdown 转换。</p>
            <p class="mt-4 text-sm leading-7 text-slate-400">内容格式更一致，页面切换之后不会再突然变形。</p>
          </div>
        </div>
      </section>

      <section id="articles" class="mt-14 w-full max-w-6xl">
        <div class="mb-8 flex flex-wrap items-end justify-between gap-4">
          <div class="max-w-xl">
            <h2 class="text-2xl font-semibold text-white">最新文章</h2>
            <p class="mt-2 text-sm leading-7 text-slate-400">文章列表作为第二视觉层，宽度收敛、留白更多，让首屏和内容区各自有呼吸感。</p>
          </div>
          <div class="text-sm text-slate-400">第 {{ currentPage }} / {{ totalPages }} 页</div>
        </div>

        <div v-if="loading" class="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
          <div v-for="item in 6" :key="item" class="rounded-[24px] border border-white/10 bg-white/5 p-6">
            <div class="h-4 w-24 animate-pulse rounded bg-white/10"></div>
            <div class="mt-5 h-6 w-3/4 animate-pulse rounded bg-white/10"></div>
            <div class="mt-4 space-y-2">
              <div class="h-4 w-full animate-pulse rounded bg-white/10"></div>
              <div class="h-4 w-5/6 animate-pulse rounded bg-white/10"></div>
              <div class="h-4 w-2/3 animate-pulse rounded bg-white/10"></div>
            </div>
          </div>
        </div>

        <div v-else-if="loadError" class="mx-auto max-w-2xl rounded-[24px] border border-amber-500/20 bg-amber-500/10 px-6 py-8 text-center">
          <p class="text-base font-medium text-amber-100">{{ loadError }}</p>
          <p class="mt-2 text-sm text-amber-100/70">后端接口恢复后，文章列表会在这里正常出现。</p>
          <button
            type="button"
            class="mt-5 rounded-xl border border-amber-200/20 px-4 py-2 text-sm text-amber-50 transition hover:bg-white/5"
            @click="fetchBlogs"
          >
            重新加载
          </button>
        </div>

        <div v-else-if="blogs.length === 0" class="mx-auto max-w-2xl rounded-[24px] border border-dashed border-white/15 bg-white/[0.03] px-6 py-14 text-center">
          <h3 class="text-xl font-medium text-white">还没有文章</h3>
          <p class="mt-3 text-sm leading-6 text-slate-400">首页已经准备好了，第一篇文章发出来之后，这里就会开始有内容。</p>
          <router-link
            to="/create"
            class="mt-6 inline-flex rounded-xl bg-sky-400 px-5 py-3 text-sm font-semibold text-slate-950 no-underline transition hover:bg-sky-300"
          >
            去写第一篇
          </router-link>
        </div>

        <div v-else class="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
          <button
            v-for="blog in blogs"
            :key="blog.id"
            type="button"
            class="article-card text-left"
            @click="openBlog(blog)"
          >
            <div class="flex items-center justify-between gap-3">
              <div class="flex min-w-0 items-center gap-3">
                <span class="flex h-10 w-10 shrink-0 items-center justify-center rounded-2xl border border-white/10 bg-white/5 text-sm font-semibold text-white">
                  {{ (blog.authorName || '?').charAt(0).toUpperCase() }}
                </span>
                <div class="min-w-0">
                  <p class="truncate text-sm font-medium text-slate-200">{{ blog.authorName || '未知作者' }}</p>
                  <p class="text-xs text-slate-500">{{ formatDate(blog.createTime) }}</p>
                </div>
              </div>
              <span v-if="blog.visibility === 'private'" class="rounded-full border border-amber-400/20 bg-amber-400/10 px-2 py-1 text-[11px] text-amber-200">
                私密
              </span>
            </div>

            <h3 class="mt-5 text-xl font-semibold leading-8 text-white">{{ blog.title }}</h3>
            <p class="excerpt mt-3 text-sm leading-7 text-slate-400">{{ getExcerpt(blog.content) }}</p>

            <div class="mt-6 flex items-center justify-between text-xs text-slate-500">
              <span>阅读 {{ blog.viewCount || 0 }}</span>
              <span>{{ blog.visibility === 'private' ? '仅作者可见' : '公开内容' }}</span>
            </div>
          </button>
        </div>

        <div v-if="totalPages > 1" class="mt-10 flex flex-wrap justify-center gap-2">
          <button
            v-for="page in displayPages"
            :key="page"
            type="button"
            class="inline-flex h-10 min-w-10 items-center justify-center rounded-xl border px-3 text-sm transition"
            :class="page === currentPage
              ? 'border-sky-300/40 bg-sky-400 text-slate-950'
              : 'border-white/10 bg-white/5 text-slate-300 hover:border-white/20 hover:bg-white/10'"
            @click="goToPage(page)"
          >
            {{ page }}
          </button>
        </div>
      </section>
    </main>

    <Teleport to="body">
      <Transition name="modal">
        <div v-if="showPrivateModal" class="fixed inset-0 z-[100] flex items-center justify-center px-4" @click.self="showPrivateModal = false">
          <div class="absolute inset-0 bg-slate-950/80"></div>
          <div class="relative w-full max-w-md rounded-[28px] border border-white/10 bg-slate-900 p-8">
            <h3 class="text-2xl font-semibold text-white">这篇文章是私密内容</h3>
            <p class="mt-3 text-sm leading-6 text-slate-400">当前首页只展示公开流内容。私密文章只有作者本人可以进入阅读页。</p>
            <div class="mt-8 flex flex-wrap gap-3">
              <router-link
                to="/login"
                class="rounded-xl bg-sky-400 px-5 py-3 text-sm font-semibold text-slate-950 no-underline transition hover:bg-sky-300"
                @click="showPrivateModal = false"
              >
                去登录
              </router-link>
              <button
                type="button"
                class="rounded-xl border border-white/10 px-5 py-3 text-sm text-slate-200 transition hover:border-white/20 hover:bg-white/5"
                @click="showPrivateModal = false"
              >
                关闭
              </button>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getBlogList } from '@/api/blog'
import AppHeader from '@/components/AppHeader.vue'
import { useUserStore } from '@/store/user'
import { toPlainExcerpt } from '@/utils/markdown'

const router = useRouter()
const userStore = useUserStore()

const blogs = ref([])
const loading = ref(true)
const loadError = ref('')
const currentPage = ref(1)
const total = ref(0)
const totalPages = ref(1)
const showPrivateModal = ref(false)

const displayPages = computed(() => {
  const pages = []
  const start = Math.max(1, currentPage.value - 2)
  const end = Math.min(totalPages.value, start + 4)

  for (let index = start; index <= end; index += 1) {
    pages.push(index)
  }

  return pages
})

async function fetchBlogs() {
  loading.value = true
  loadError.value = ''

  try {
    const res = await getBlogList({ page: currentPage.value, size: 9 })
    blogs.value = res.data.records || []
    total.value = res.data.total || 0
    totalPages.value = Math.max(1, Math.ceil(total.value / 9))
  } catch (error) {
    blogs.value = []
    total.value = 0
    totalPages.value = 1
    loadError.value = error.message || '文章列表加载失败，请稍后再试。'
  } finally {
    loading.value = false
  }
}

function goToPage(page) {
  currentPage.value = page
  fetchBlogs()
  document.getElementById('articles')?.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

function getExcerpt(content) {
  return toPlainExcerpt(content, 110)
}

function formatDate(dateStr) {
  if (!dateStr) return ''

  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', { year: 'numeric', month: 'short', day: 'numeric' })
}

function openBlog(blog) {
  if (blog.visibility === 'private' && blog.authorId !== userStore.userId) {
    showPrivateModal.value = true
    return
  }

  router.push({ name: 'BlogDetail', params: { id: blog.id } })
}

onMounted(() => {
  fetchBlogs()
})
</script>

<style scoped>
.hero-title {
  text-wrap: balance;
}

.hero-copy {
  text-wrap: pretty;
}

.article-card {
  background: rgba(15, 23, 42, 0.9);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 24px;
  padding: 24px;
  transition: transform 0.2s ease, border-color 0.2s ease, background 0.2s ease;
}

.article-card:hover {
  background: rgba(15, 23, 42, 1);
  border-color: rgba(125, 211, 252, 0.28);
  transform: translateY(-2px);
}

.excerpt {
  display: -webkit-box;
  overflow: hidden;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
}

.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
  transform: translateY(8px);
}
</style>
