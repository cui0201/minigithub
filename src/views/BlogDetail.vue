<template>
  <div class="min-h-screen bg-slate-950 text-slate-100">
    <AppHeader subtitle="文章详情">
      <template #actions>
        <button
          v-if="isAuthor"
          type="button"
          class="rounded-xl border border-white/10 px-4 py-2 text-sm text-slate-200 transition hover:border-white/20 hover:bg-white/5"
          @click="editBlog"
        >
          编辑
        </button>
        <button
          v-if="isAuthor"
          type="button"
          class="rounded-xl border border-red-400/20 px-4 py-2 text-sm text-red-200 transition hover:bg-red-400/10"
          @click="deleteBlog"
        >
          删除
        </button>
      </template>
    </AppHeader>

    <main class="mx-auto max-w-4xl px-4 pb-16 pt-10 sm:px-6 lg:px-8 lg:pt-14">
      <div v-if="loading" class="rounded-[28px] border border-white/10 bg-slate-900/80 p-8 sm:p-10">
        <div class="h-4 w-24 animate-pulse rounded bg-white/10"></div>
        <div class="mt-6 h-10 w-3/4 animate-pulse rounded bg-white/10"></div>
        <div class="mt-10 space-y-3">
          <div class="h-4 w-full animate-pulse rounded bg-white/10"></div>
          <div class="h-4 w-full animate-pulse rounded bg-white/10"></div>
          <div class="h-4 w-5/6 animate-pulse rounded bg-white/10"></div>
          <div class="h-4 w-4/6 animate-pulse rounded bg-white/10"></div>
        </div>
      </div>

      <div v-else-if="isPrivate" class="rounded-[28px] border border-amber-500/20 bg-amber-500/10 p-8 sm:p-10">
        <h1 class="text-3xl font-semibold text-white">这篇文章是私密内容</h1>
        <p class="mt-4 max-w-2xl text-sm leading-7 text-amber-100/80">只有作者本人可以打开详情页。如果需要查看，请先登录作者账号。</p>
        <router-link
          to="/login"
          class="mt-6 inline-flex rounded-xl bg-sky-400 px-5 py-3 text-sm font-semibold text-slate-950 no-underline transition hover:bg-sky-300"
        >
          去登录
        </router-link>
      </div>

      <div v-else-if="loadError" class="rounded-[28px] border border-amber-500/20 bg-amber-500/10 p-8 sm:p-10">
        <h1 class="text-2xl font-semibold text-white">文章暂时无法打开</h1>
        <p class="mt-4 text-sm leading-7 text-amber-100/80">{{ loadError }}</p>
        <div class="mt-6 flex flex-wrap gap-3">
          <button
            type="button"
            class="rounded-xl bg-sky-400 px-5 py-3 text-sm font-semibold text-slate-950 transition hover:bg-sky-300"
            @click="fetchBlog"
          >
            重新加载
          </button>
          <router-link
            to="/"
            class="rounded-xl border border-white/10 px-5 py-3 text-sm text-slate-200 no-underline transition hover:border-white/20 hover:bg-white/5"
          >
            返回首页
          </router-link>
        </div>
      </div>

      <article v-else class="rounded-[28px] border border-white/10 bg-slate-900/90 p-8 sm:p-10 lg:p-12">
        <div class="flex flex-wrap items-start justify-between gap-5 border-b border-white/10 pb-8">
          <div class="min-w-0">
            <div class="flex flex-wrap items-center gap-3 text-sm text-slate-400">
              <router-link
                :to="`/user/${blog.authorName}`"
                class="font-medium text-slate-200 no-underline transition hover:text-white"
              >
                {{ blog.authorName }}
              </router-link>
              <span>{{ formatDate(blog.createTime) }}</span>
              <span>阅读 {{ blog.viewCount || 0 }}</span>
            </div>
            <h1 class="mt-5 max-w-3xl text-3xl font-semibold leading-tight text-white sm:text-4xl">{{ blog.title }}</h1>
          </div>

          <div class="flex flex-wrap items-center gap-2">
            <span class="rounded-full border border-white/10 bg-white/5 px-3 py-1 text-xs text-slate-300">
              {{ blog.visibility === 'private' ? '私密文章' : '公开文章' }}
            </span>
          </div>
        </div>

        <div class="prose prose-invert article-prose mt-10 max-w-none" v-html="renderedContent"></div>
      </article>

      <div class="mt-8">
        <router-link to="/" class="text-sm text-slate-400 no-underline transition hover:text-white">返回首页</router-link>
      </div>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { deleteBlog as apiDeleteBlog, getBlogById } from '@/api/blog'
import AppHeader from '@/components/AppHeader.vue'
import { useUserStore } from '@/store/user'
import { renderMarkdown } from '@/utils/markdown'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const blog = ref({})
const loading = ref(true)
const isPrivate = ref(false)
const isAuthor = ref(false)
const loadError = ref('')
const renderedContent = ref('')
const isPreviewMode = import.meta.env.DEV && route.query.preview === '1'

function loadPreviewBlog() {
  blog.value = {
    id: 1,
    title: '用更稳定的版式展示技术文章',
    authorId: 1,
    authorName: 'PreviewAuthor',
    createTime: new Date().toISOString(),
    viewCount: 128,
    visibility: 'public',
    content: `# 为什么详情页需要更稳\n\n技术文章详情页最重要的，不是装饰，而是让正文持续可读。\n\n## 这次预览重点\n\n- 标题区和正文区是否分层清楚\n- 段落与段落之间是否有足够留白\n- 列表、引用、代码块是否还会互相挤压\n\n> 正文区应该像阅读器，而不是控制台面板。\n\n\`\`\`js\nexport function formatArticle(title) {\n  return title.trim()\n}\n\`\`\`\n\n详情页需要把读者注意力稳定地留在内容本身。`, 
  }
  isAuthor.value = true
  renderedContent.value = renderMarkdown(blog.value.content)
}

async function fetchBlog() {
  loading.value = true
  isPrivate.value = false
  loadError.value = ''

  if (isPreviewMode) {
    loadPreviewBlog()
    loading.value = false
    return
  }

  try {
    const res = await getBlogById(route.params.id)
    blog.value = res.data
    isAuthor.value = res.data.authorId === userStore.userId
    renderedContent.value = renderMarkdown(res.data.content)
  } catch (error) {
    if (error.code === 403) {
      isPrivate.value = true
    } else {
      loadError.value = error.message || '文章加载失败，请稍后再试。'
    }
  } finally {
    loading.value = false
  }
}

function formatDate(dateStr) {
  if (!dateStr) return ''

  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}

function editBlog() {
  router.push({ name: 'EditBlog', params: { id: blog.value.id } })
}

async function deleteBlog() {
  try {
    await ElMessageBox.confirm('确定要删除这篇文章吗？删除后将无法恢复。', '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning',
    })

    await apiDeleteBlog(blog.value.id)
    router.push('/')
  } catch (error) {
    if (error === 'cancel' || error === 'close') return
    loadError.value = error.message || '删除失败，请稍后再试。'
  }
}

onMounted(() => {
  fetchBlog()
})

watch(() => route.params.id, () => {
  fetchBlog()
})
</script>

<style scoped>
.article-prose :deep(h1) {
  margin-top: 2rem;
  font-size: 1.625rem;
  font-weight: 600;
  color: rgb(255 255 255);
}

.article-prose :deep(h2) {
  margin-top: 2.25rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  font-size: 1.375rem;
  font-weight: 600;
  color: rgb(255 255 255);
}

.article-prose :deep(h3) {
  margin-top: 1.75rem;
  font-size: 1.125rem;
  font-weight: 600;
  color: rgb(241 245 249);
}

.article-prose :deep(p) {
  margin-bottom: 1.25rem;
  line-height: 2rem;
  color: rgb(203 213 225);
}

.article-prose :deep(a) {
  color: rgb(125 211 252);
  text-decoration: underline;
  text-decoration-color: rgba(56, 189, 248, 0.4);
  text-underline-offset: 4px;
}

.article-prose :deep(strong) {
  color: rgb(255 255 255);
}

.article-prose :deep(code) {
  padding: 0.125rem 0.375rem;
  border-radius: 0.375rem;
  background: rgba(255, 255, 255, 0.1);
  color: rgb(186 230 253);
}

.article-prose :deep(pre) {
  margin-bottom: 1.5rem;
  overflow-x: auto;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 1rem;
  background: rgb(2 6 23);
  padding: 1rem;
}

.article-prose :deep(pre code) {
  padding: 0;
  background: transparent;
  color: rgb(241 245 249);
}

.article-prose :deep(blockquote) {
  margin: 1.5rem 0;
  padding-left: 1rem;
  border-left: 2px solid rgba(125, 211, 252, 0.4);
  color: rgb(148 163 184);
}

.article-prose :deep(ul),
.article-prose :deep(ol) {
  margin-bottom: 1.25rem;
  padding-left: 1.25rem;
  color: rgb(203 213 225);
}

.article-prose :deep(li) {
  margin-bottom: 0.5rem;
}

.article-prose :deep(img) {
  margin: 1.5rem 0;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 1rem;
}

.article-prose :deep(hr) {
  margin: 2rem 0;
  border-color: rgba(255, 255, 255, 0.1);
}
</style>
