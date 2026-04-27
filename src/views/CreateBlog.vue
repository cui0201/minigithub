<template>
  <div class="min-h-screen bg-gradient-to-b from-[#020617] via-[#0a0f2e] to-[#020617] relative">
    <div class="absolute inset-0 z-0">
      <ParticlesBg />
    </div>

    <div class="relative z-10">
      <!-- Nav -->
      <nav class="flex items-center justify-between px-4 sm:px-8 py-4 max-w-3xl mx-auto">
        <router-link to="/" class="flex items-center gap-2 no-underline group">
          <span class="text-2xl">💻</span>
          <span class="text-lg font-bold bg-gradient-to-r from-sky-400 via-blue-400 to-purple-400 bg-clip-text text-transparent">
            MiniGitHub
          </span>
        </router-link>
        <span class="text-sm text-white/40">{{ isEdit ? '编辑文章' : '新文章' }}</span>
      </nav>

      <div class="max-w-3xl mx-auto px-6 py-8">
        <div class="glass-card !p-8">
          <!-- Title -->
          <div class="mb-6">
            <input
              v-model="title"
              type="text"
              placeholder="输入文章标题..."
              class="input-glow !border-0 !border-b !border-white/10 !rounded-none !px-0 !py-3 text-2xl font-bold text-white/90 placeholder-white/20"
            />
          </div>

          <!-- Edit/Preview Tabs -->
          <div class="flex gap-4 mb-6 border-b border-white/5">
            <button
              @click="editMode = 'write'"
              class="pb-3 text-sm transition-colors border-b-2"
              :class="editMode === 'write' ? 'text-sky-400 border-sky-400' : 'text-white/40 border-transparent hover:text-white/70'"
            >✏️ 编辑</button>
            <button
              @click="editMode = 'preview'"
              class="pb-3 text-sm transition-colors border-b-2"
              :class="editMode === 'preview' ? 'text-sky-400 border-sky-400' : 'text-white/40 border-transparent hover:text-white/70'"
            >👁️ 预览</button>
          </div>

          <!-- Write -->
          <div v-if="editMode === 'write'">
            <textarea
              v-model="content"
              rows="18"
              placeholder="使用 Markdown 编写你的文章..."
              class="input-glow font-mono text-sm resize-y min-h-[300px]"
            ></textarea>
          </div>

          <!-- Preview -->
          <div v-else class="min-h-[300px]">
            <div v-if="content" class="prose prose-invert max-w-none" v-html="previewContent"></div>
            <div v-else class="text-white/30 text-center py-12">
              <div class="text-4xl mb-3">👀</div>
              <p>还没开始写呢～</p>
            </div>
          </div>

          <!-- Visibility & Submit -->
          <div class="flex items-center justify-between mt-8 pt-6 border-t border-white/5">
            <label class="flex items-center gap-2 cursor-pointer group">
              <input type="checkbox" v-model="isPrivate" class="w-4 h-4 rounded border-white/20 bg-white/5 text-sky-400 focus:ring-sky-400/30" />
              <span class="text-sm text-white/40 group-hover:text-white/70 transition-colors">
                🔒 私密文章
              </span>
            </label>
            <div class="flex items-center gap-3">
              <router-link to="/" class="px-5 py-2.5 rounded-xl border border-white/10 text-white/60 hover:text-white hover:border-white/20 text-sm no-underline transition-all">
                取消
              </router-link>
              <button
                @click="submitBlog"
                :disabled="!title.trim() || submitting"
                class="btn-glow !px-6 !py-2.5 !rounded-xl text-sm disabled:opacity-40 disabled:cursor-not-allowed"
              >
                {{ submitting ? '发布中...' : (isEdit ? '保存修改' : '发布文章') }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createBlog, getBlogById, updateBlog } from '@/api/blog'
import ParticlesBg from '@/components/ParticlesBg.vue'
import { marked } from 'marked'

const route = useRoute()
const router = useRouter()

const title = ref('')
const content = ref('')
const isPrivate = ref(false)
const editMode = ref('write')
const submitting = ref(false)

const isEdit = computed(() => !!route.params.id)

const previewContent = computed(() => {
  if (!content.value) return ''
  try {
    return marked(content.value)
  } catch {
    return content.value.replace(/\n/g, '<br/>')
  }
})

onMounted(async () => {
  if (isEdit.value) {
    const res = await getBlogById(route.params.id)
    if (res.code === 200 && res.data) {
      title.value = res.data.title || ''
      content.value = res.data.content || ''
      isPrivate.value = res.data.visibility === 'private'
    }
  }
})

const submitBlog = async () => {
  if (!title.value.trim() || submitting.value) return
  submitting.value = true
  try {
    const visibility = isPrivate.value ? 'private' : 'public'
    if (isEdit.value) {
      await updateBlog(route.params.id, { title: title.value, content: content.value, visibility })
    } else {
      await createBlog({ title: title.value, content: content.value, visibility })
    }
    router.push('/')
  } catch (e) {
    console.error('提交失败:', e)
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
:deep(.prose) h1 { @apply text-2xl font-bold text-white/90 mt-6 mb-4; }
:deep(.prose) h2 { @apply text-xl font-bold text-white/85 mt-5 mb-3; }
:deep(.prose) p { @apply text-white/60 mb-4 leading-relaxed; }
:deep(.prose) code { @apply px-2 py-0.5 rounded bg-white/5 text-sm text-sky-400; }
:deep(.prose) pre { @apply bg-white/5 border border-white/5 rounded-xl p-4 overflow-x-auto mb-4; }
:deep(.prose) blockquote { @apply border-l-4 border-white/10 pl-4 text-white/40 italic my-4; }
:deep(.prose) ul, :deep(.prose) ol { @apply pl-5 text-white/60 mb-4; }
</style>
