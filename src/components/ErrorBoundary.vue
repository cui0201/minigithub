<template>
  <div v-if="hasError" class="error-boundary-glass">
    <div class="error-content">
      <h3 class="error-title">组件渲染异常</h3>
      <p class="error-message">{{ error?.message || '未知错误' }}</p>
      <button class="reload-btn" @click="reload">重新加载</button>
    </div>
  </div>
  <slot v-else />
</template>

<script setup>
import { ref, onErrorCaptured } from 'vue'

const hasError = ref(false)
const error = ref(null)

onErrorCaptured((err) => {
  hasError.value = true
  error.value = err
  return false
})

const reload = () => {
  hasError.value = false
  error.value = null
}
</script>

<style scoped>
.error-boundary-glass {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  padding: 2.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.37);
  background-color: var(--bg-primary);
}

.error-content {
  text-align: center;
  max-width: 400px;
  width: 100%;
}

.error-title {
  color: var(--text-primary);
  font-size: 1.25rem;
  margin-bottom: 1rem;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.error-message {
  color: var(--text-secondary);
  font-size: 0.95rem;
  margin-bottom: 2rem;
  line-height: 1.6;
  background: rgba(0, 0, 0, 0.2);
  padding: 1rem;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.05);
  word-break: break-word;
  font-family: monospace;
}

.reload-btn {
  background: var(--accent-primary);
  color: var(--text-primary);
  border: 1px solid transparent;
  padding: 0.75rem 2rem;
  border-radius: 8px;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.25);
  outline: none;
}

.reload-btn:hover {
  opacity: 0.9;
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.35);
}

.reload-btn:active {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}
</style>
