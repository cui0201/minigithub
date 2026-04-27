<template>
  <div class="not-found-page">
    <div class="glass-card not-found-card">
      <div class="error-icon">🔍</div>
      <h1 class="error-code">404</h1>
      <h2 class="error-title">页面不存在</h2>
      <p class="error-subtitle">你访问的页面可能已被移除、名称已更改或暂时不可用。</p>
      <router-link to="/">
        <el-button type="primary" size="large" class="home-button">
          返回首页
        </el-button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.not-found-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--bg-primary);
  padding: 2rem;
}

.not-found-card {
  text-align: center;
  max-width: 480px;
  width: 100%;
  padding: 3rem 2.5rem;
}

.glass-card {
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

.error-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  line-height: 1;
  opacity: 0.85;
}

.error-code {
  font-size: 6rem;
  font-weight: 700;
  color: var(--accent-primary);
  margin: 0;
  line-height: 1.1;
  letter-spacing: -2px;
}

.error-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 1rem 0 0.75rem;
}

.error-subtitle {
  font-size: 0.95rem;
  color: var(--text-secondary);
  margin: 0 0 2rem;
  line-height: 1.6;
}

.home-button {
  width: 160px;
  font-weight: 500;
}

a {
  text-decoration: none;
}
</style>
