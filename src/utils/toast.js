import { ref, computed } from 'vue'

// 全局响应式 toast 状态
const toastState = ref(null)

export function showToast(message, type = 'info') {
  toastState.value = { message, type }
}

export { toastState }
