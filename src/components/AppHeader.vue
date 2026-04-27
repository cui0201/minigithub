<template>
  <header class="sticky top-0 z-50 border-b border-white/10 bg-slate-950/85 backdrop-blur-xl">
    <div class="mx-auto grid h-[4.5rem] max-w-7xl grid-cols-[auto_1fr_auto] items-center gap-4 px-4 sm:px-6 lg:px-8">
      <div class="flex items-center gap-6">
        <router-link to="/" class="flex items-center gap-3 no-underline">
          <span class="flex h-9 w-9 items-center justify-center rounded-xl border border-sky-400/20 bg-sky-400/10 text-sm font-semibold text-sky-200">
            MG
          </span>
          <div>
            <p class="text-sm font-semibold text-white">MiniGitHub</p>
            <p v-if="subtitle" class="text-xs text-slate-400">{{ subtitle }}</p>
          </div>
        </router-link>
      </div>

      <nav v-if="showNav" class="hidden items-center justify-center gap-8 md:flex">
        <router-link to="/" class="header-link" active-class="header-link-active">首页</router-link>
        <router-link to="/create" class="header-link" active-class="header-link-active">写文章</router-link>
      </nav>

      <div class="flex items-center justify-end gap-3">
        <slot name="actions" />
      </div>
    </div>
  </header>
</template>

<script setup>
defineProps({
  showNav: {
    type: Boolean,
    default: false,
  },
  subtitle: {
    type: String,
    default: '',
  },
})
</script>

<style scoped>
.header-link {
  color: rgb(148 163 184);
  font-size: 0.95rem;
  font-weight: 500;
  text-decoration: none;
  transition: color 0.2s ease;
}

.header-link:hover,
.header-link-active {
  color: rgb(241 245 249);
}
</style>
