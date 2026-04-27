<template>
  <div class="min-h-screen bg-slate-950 text-slate-100">
    <AppHeader :subtitle="subtitle" :show-nav="showNav">
      <template #actions>
        <slot name="actions" />
      </template>
    </AppHeader>

    <main :class="['mx-auto w-full px-4 pb-16 pt-10 sm:px-6 lg:px-8 lg:pt-14', maxWidthClass]">
      <slot />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import AppHeader from '@/components/AppHeader.vue'

const props = defineProps({
  subtitle: {
    type: String,
    default: '',
  },
  showNav: {
    type: Boolean,
    default: false,
  },
  maxWidth: {
    type: String,
    default: '7xl',
  },
})

const maxWidthMap = {
  '4xl': 'max-w-4xl',
  '5xl': 'max-w-5xl',
  '6xl': 'max-w-6xl',
  '7xl': 'max-w-7xl',
}

const maxWidthClass = computed(() => maxWidthMap[props.maxWidth] || 'max-w-7xl')
</script>
