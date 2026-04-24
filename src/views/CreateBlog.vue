<template>
  <div class="create-page">
    <div class="create-container">
      <div class="flex items-center gap-3 mb-8">
        <button class="back-link" @click="$router.push('/')">← 返回</button>
        <h1 class="section-title !mb-0">{{ isEdit ? '✏️ 编辑文章' : '📝 发布文章' }}</h1>
      </div>

      <!-- 错误提示 -->
      <div v-if="errorMsg" class="create-alert">
        <span>⚠️</span> {{ errorMsg }}
      </div>

      <!-- 表单 -->
      <form @submit.prevent="handleSubmit" class="glass-card !p-8">
        <div class="form-group mb-6">
          <label class="form-label">文章标题</label>
          <input
            v-model="form.title"
            class="input-glow"
            type="text"
            placeholder="输入一个吸引人的标题..."
            required
          />
        </div>

        <div class="form-group mb-6">
          <div class="flex items-center justify-between mb-3">
            <label class="form-label !mb-0">文章内容</label>
            <span class="text-xs" style="color: var(--text-secondary);">支持 Markdown 语法</span>
          </div>
          <textarea
            v-model="form.content"
            class="input-glow input-textarea"
            placeholder="在这里写下你的想法..."
            rows="16"
            required
          ></textarea>
        </div>

        <div class="flex justify-end gap-3">
          <router-link to="/" class="btn-outline no-underline">取消</router-link>
          <button
            type="submit"
            class="btn-glow"
            :disabled="submitting"
          >
            {{ submitting ? '✨ 提交中...' : (isEdit ? '💾 保存修改' : '🚀 发布文章') }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createBlog, getBlogById, updateBlog } from '@/api/blog'
import { useUserStore } from '@/store/user'
import { showToast } from '@/utils/toast'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isEdit = computed(() => !!route.params.id)
const submitting = ref(false)
const errorMsg = ref('')

const form = reactive({
  title: '',
  content: '',
})

onMounted(async () => {
  if (isEdit.value) {
    try {
      const res = await getBlogById(route.params.id)
      if (res.code === 200) {
        form.title = res.data.title
        form.content = res.data.content
      }
    } catch (e) {
      errorMsg.value = '加载文章失败'
    }
  }
})

const handleSubmit = async () => {
  if (!form.title || !form.content) {
    errorMsg.value = '请填写标题和内容'
    return
  }

  submitting.value = true
  errorMsg.value = ''

  try {
    let res
    if (isEdit.value) {
      res = await updateBlog(route.params.id, {
        title: form.title,
        content: form.content,
      })
    } else {
      res = await createBlog({
        title: form.title,
        content: form.content,
      })
    }

    if (res.code === 200) {
      showToast(isEdit.value ? '文章已更新 ✅' : '文章发布成功 ✨', 'success')
      const blogId = isEdit.value ? route.params.id : res.data.id
      router.push(`/blog/${blogId}`)
    } else {
      errorMsg.value = res.message || '提交失败'
    }
  } catch (e) {
    errorMsg.value = e.message || '提交失败，请重试'
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.create-page {
  padding: 120px 24px 60px;
  min-height: 100vh;
}
.create-container {
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
}
.create-alert {
  padding: 12px 16px;
  border-radius: 12px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  color: #fca5a5;
  font-size: 14px;
  margin-bottom: 20px;
}
.form-group {
  margin-bottom: 4px;
}
.form-label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: var(--text-secondary);
  font-weight: 500;
}
.input-textarea {
  resize: vertical;
  min-height: 300px;
  font-family: 'JetBrains Mono', monospace;
  line-height: 1.6;
}
.btn-outline {
  display: inline-flex;
  align-items: center;
  padding: 10px 24px;
  border-radius: 10px;
  border: 1px solid var(--border-color);
  color: var(--text-primary);
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  background: transparent;
  font-size: 15px;
}
.btn-outline:hover {
  border-color: var(--accent-primary);
  box-shadow: var(--glow-blue);
  transform: translateY(-2px);
}
</style>
