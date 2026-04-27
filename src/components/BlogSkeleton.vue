<template>
  <div class="blog-grid">
    <div v-for="n in count" :key="n" class="blog-skeleton-card">
      <el-skeleton animated :count="1">
        <template #template>
          <div class="skeleton-avatar-row">
            <el-skeleton-item variant="circle" class="skeleton-avatar" />
            <div class="skeleton-author-info">
              <el-skeleton-item variant="text" class="skeleton-author-name" />
              <el-skeleton-item variant="text" class="skeleton-date" />
            </div>
          </div>
          <el-skeleton-item variant="h1" class="skeleton-title" />
          <div class="skeleton-excerpt">
            <el-skeleton-item variant="text" class="skeleton-line" />
            <el-skeleton-item variant="text" class="skeleton-line" />
            <el-skeleton-item variant="text" class="skeleton-line skeleton-line-short" />
          </div>
          <div class="skeleton-tags">
            <el-skeleton-item variant="text" class="skeleton-tag" />
            <el-skeleton-item variant="text" class="skeleton-tag" />
            <el-skeleton-item variant="text" class="skeleton-tag skeleton-tag-small" />
          </div>
        </template>
      </el-skeleton>
    </div>
  </div>
</template>

<script setup>
defineProps({
  count: {
    type: Number,
    default: 3
  }
})
</script>

<style scoped>
.blog-grid {
  display: grid;
  gap: 24px;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
}
.blog-skeleton-card {
  background: var(--bg-card, rgba(255,255,255,0.05));
  border: 1px solid var(--border-color, rgba(255,255,255,0.1));
  border-radius: 16px;
  padding: 24px;
}
.skeleton-avatar-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}
.skeleton-avatar { width: 40px; height: 40px; }
.skeleton-author-info { display: flex; flex-direction: column; gap: 6px; flex: 1; }
.skeleton-author-name { width: 120px; height: 14px; }
.skeleton-date { width: 80px; height: 12px; }
.skeleton-title { width: 85%; height: 22px; margin-bottom: 14px; }
.skeleton-excerpt { display: flex; flex-direction: column; gap: 10px; margin-bottom: 18px; }
.skeleton-line { width: 100%; height: 14px; }
.skeleton-line-short { width: 65%; }
.skeleton-tags { display: flex; gap: 8px; align-items: center; }
.skeleton-tag { width: 60px; height: 26px; border-radius: 6px; }
.skeleton-tag-small { width: 44px; }
.blog-skeleton-card :deep(.el-skeleton) { background: transparent; }
.blog-skeleton-card :deep(.el-skeleton-item) { background: var(--border-color, rgba(255,255,255,0.1)); border-radius: 4px; }
</style>
