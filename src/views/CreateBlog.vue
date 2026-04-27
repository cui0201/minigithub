<template>
  <div class="min-h-screen bg-slate-950 text-slate-100">
    <AppHeader :subtitle="isEdit ? '编辑文章' : '写文章'">
      <template #actions>
        <router-link
          to="/"
          class="rounded-xl border border-white/10 px-4 py-2 text-sm text-slate-200 no-underline transition hover:border-white/20 hover:bg-white/5"
        >
          返回首页
        </router-link>
      </template>
    </AppHeader>

    <main class="mx-auto max-w-6xl px-4 pb-16 pt-10 sm:px-6 lg:px-8 lg:pt-14">
      <div class="mb-10 flex flex-wrap items-end justify-between gap-4">
        <div class="max-w-2xl">
          <h1 class="text-4xl font-semibold tracking-tight text-white">{{ isEdit ? '编辑文章' : '新建文章' }}</h1>
          <p class="mt-3 text-sm leading-7 text-slate-400">标题、内容预览和发布设置放在同一处，减少来回切换，让编辑过程更像一个完整工作台。</p>
        </div>
        <div class="rounded-full border border-white/10 bg-white/5 px-4 py-2.5 text-sm text-slate-400">
          {{ content.length }} 字符
        </div>
      </div>

      <div v-if="errorMessage" class="mb-6 rounded-2xl border border-amber-500/20 bg-amber-500/10 px-5 py-4 text-sm text-amber-100">
        {{ errorMessage }}
      </div>

      <section class="grid gap-6 lg:grid-cols-[minmax(0,1fr)_320px]">
        <div class="rounded-[30px] border border-white/10 bg-slate-900/90 p-6 sm:p-8 lg:p-10">
          <label class="block text-sm font-medium text-slate-300">文章标题</label>
          <input
            v-model="title"
            type="text"
            placeholder="给这篇文章一个清晰的标题"
            class="mt-4 w-full rounded-3xl border border-white/10 bg-white/[0.06] px-6 py-5 text-xl font-semibold leading-8 text-white outline-none transition placeholder:text-slate-500 focus:border-sky-300/40 focus:bg-white/[0.08] sm:text-2xl"
          />

          <div class="mt-8 flex flex-wrap gap-3 border-b border-white/10 pb-5">
            <button
              type="button"
              class="rounded-full px-4 py-2 text-sm transition"
              :class="editMode === 'write' ? 'bg-sky-400 text-slate-950' : 'bg-white/5 text-slate-300 hover:bg-white/10'"
              @click="editMode = 'write'"
            >
              编辑
            </button>
            <button
              type="button"
              class="rounded-full px-4 py-2 text-sm transition"
              :class="editMode === 'preview' ? 'bg-sky-400 text-slate-950' : 'bg-white/5 text-slate-300 hover:bg-white/10'"
              @click="editMode = 'preview'"
            >
              预览
            </button>
          </div>

          <div v-if="editMode === 'write'" class="mt-8">
            <textarea
              v-model="content"
              rows="20"
              placeholder="使用 Markdown 编写正文，换行和标题会按阅读页效果转换。"
              class="min-h-[560px] w-full resize-y rounded-[28px] border border-white/10 bg-slate-950 px-6 py-5 font-mono text-[15px] leading-8 text-slate-100 outline-none transition placeholder:text-slate-500 focus:border-sky-300/40"
            ></textarea>
          </div>

          <div v-else class="mt-8 min-h-[560px] rounded-[28px] border border-white/10 bg-slate-950 px-6 py-6">
            <div v-if="content.trim()" class="prose prose-invert editor-prose max-w-none" v-html="previewContent"></div>
            <div class="preview-empty flex h-full min-h-[500px] items-center justify-center rounded-[24px] border border-dashed border-white/10 px-6 text-center text-sm leading-7 text-slate-500">
              预览区会按照文章详情页的 Markdown 渲染方式显示
            </div>
          </div>
        </div>

        <aside class="space-y-6">
          <div class="rounded-[28px] border border-white/10 bg-white/5 p-6 lg:sticky lg:top-24">
            <h2 class="text-lg font-semibold text-white">发布设置</h2>

            <label class="mt-5 flex items-start gap-3 rounded-2xl border border-white/10 bg-slate-950/70 p-4">
              <input
                v-model="isPrivate"
                type="checkbox"
                class="mt-1 h-4 w-4 rounded border-white/20 bg-white/5 text-sky-400 focus:ring-sky-400/30"
              />
              <div>
                <p class="text-sm font-medium text-slate-100">设为私密文章</p>
                <p class="mt-1 text-sm leading-6 text-slate-400">私密文章不会出现在首页流里，只有作者本人可以查看详情页。</p>
              </div>
            </label>

            <div class="mt-5 rounded-2xl border border-white/10 bg-slate-950/70 p-4 text-sm leading-6 text-slate-400">
              当前渲染支持常见 Markdown 语法，段落换行会在预览和详情页保持一致。
            </div>
          </div>

          <div class="rounded-[28px] border border-white/10 bg-white/5 p-6">
            <h2 class="text-lg font-semibold text-white">操作</h2>
            <div class="mt-5 flex flex-col gap-3">
              <button
                type="button"
                class="rounded-xl bg-sky-400 px-4 py-3 text-sm font-semibold text-slate-950 transition hover:bg-sky-300 disabled:cursor-not-allowed disabled:opacity-50"
                :disabled="submitDisabled"
                @click="submitBlog"
              >
                {{ submitting ? '提交中...' : (isEdit ? '保存修改' : '发布文章') }}
              </button>
              <router-link
                to="/"
                class="rounded-xl border border-white/10 px-4 py-3 text-center text-sm text-slate-200 no-underline transition hover:border-white/20 hover:bg-white/5"
              >
                取消
              </router-link>
            </div>
          </div>
        </aside>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createBlog, getBlogById, updateBlog } from '@/api/blog'
import AppHeader from '@/components/AppHeader.vue'
import { renderMarkdown } from '@/utils/markdown'

const route = useRoute()
const router = useRouter()

const title = ref('')
const content = ref('')
const isPrivate = ref(false)
const editMode = ref('write')
const submitting = ref(false)
const errorMessage = ref('')

const isEdit = computed(() => Boolean(route.params.id))
const submitDisabled = computed(() => !title.value.trim() || !content.value.trim() || submitting.value)
const previewContent = computed(() => renderMarkdown(content.value))

async function loadBlog() {
  if (!isEdit.value) return

  errorMessage.value = ''

  try {
    const res = await getBlogById(route.params.id)
    title.value = res.data.title || ''
    content.value = res.data.content || ''
    isPrivate.value = res.data.visibility === 'private'
  } catch (error) {
    errorMessage.value = error.message || '文章加载失败，暂时无法编辑。'
  }
}

async function submitBlog() {
  if (submitDisabled.value) return

  submitting.value = true
  errorMessage.value = ''

  try {
    const payload = {
      title: title.value.trim(),
      content: content.value,
      visibility: isPrivate.value ? 'private' : 'public',
    }

    if (isEdit.value) {
      await updateBlog(route.params.id, payload)
    } else {
      await createBlog(payload)
    }

    router.push('/')
  } catch (error) {
    errorMessage.value = error.message || '提交失败，请稍后再试。'
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadBlog()
})

watch(() => route.params.id, () => {
  title.value = ''
  content.value = ''
  isPrivate.value = false
  loadBlog()
})
</script>

<style scoped>
.editor-prose :deep(h1) {
  margin-top: 2rem;
  font-size: 1.875rem;
  font-weight: 600;
  color: rgb(255 255 255);
}

.editor-prose :deep(h2) {
  margin-top: 1.75rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  font-size: 1.5rem;
  font-weight: 600;
  color: rgb(255 255 255);
}

.editor-prose :deep(h3) {
  margin-top: 1.5rem;
  font-size: 1.25rem;
  font-weight: 600;
  color: rgb(241 245 249);
}

.editor-prose :deep(p) {
  margin-bottom: 1rem;
  line-height: 2rem;
  color: rgb(203 213 225);
}

.editor-prose :deep(code) {
  padding: 0.125rem 0.375rem;
  border-radius: 0.375rem;
  background: rgba(255, 255, 255, 0.1);
  color: rgb(186 230 253);
}

.editor-prose :deep(pre) {
  margin-bottom: 1.25rem;
  overflow-x: auto;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 1rem;
  background: rgb(15 23 42);
  padding: 1rem;
}

.editor-prose :deep(pre code) {
  padding: 0;
  background: transparent;
  color: rgb(241 245 249);
}

.editor-prose :deep(blockquote) {
  margin: 1.25rem 0;
  padding-left: 1rem;
  border-left: 2px solid rgba(125, 211, 252, 0.4);
  color: rgb(148 163 184);
}

.editor-prose :deep(ul),
.editor-prose :deep(ol) {
  margin-bottom: 1rem;
  padding-left: 1.25rem;
  color: rgb(203 213 225);
}

.editor-prose :deep(li) {
  margin-bottom: 0.5rem;
}
</style>
