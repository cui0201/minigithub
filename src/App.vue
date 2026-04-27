<template>
  <router-view />
</template>

<script setup>
</script>

<style>
/* 全局过渡动画 */
.page-enter-active,
.page-leave-active {
  transition: all 0.3s ease;
}
.page-enter-from {
  opacity: 0;
  transform: translateY(12px) scale(0.98);
}
.page-leave-to {
  opacity: 0;
  transform: translateY(-12px) scale(0.98);
}

body {
  background: #020617;
}
</style>
