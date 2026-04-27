<template>
  <div class="min-h-screen bg-slate-950 text-slate-100">
    <AppHeader :subtitle="subtitle">
      <template #actions>
        <router-link
          :to="secondaryTo"
          class="rounded-full border border-white/10 px-4 py-2 text-sm text-slate-200 no-underline transition hover:border-white/20 hover:bg-white/5"
        >
          {{ secondaryLabel }}
        </router-link>
      </template>
    </AppHeader>

    <main class="px-4 pb-16 pt-10 sm:px-6 lg:px-8 lg:pt-14">
      <div class="mx-auto grid max-w-6xl gap-8 lg:grid-cols-[minmax(0,1.1fr)_460px] lg:items-center">
        <section class="rounded-[32px] border border-white/10 bg-[radial-gradient(circle_at_top,_rgba(56,189,248,0.18),_transparent_30%),linear-gradient(180deg,rgba(15,23,42,0.98),rgba(2,6,23,1))] px-6 py-10 sm:px-8 lg:px-10 lg:py-12">
          <div class="inline-flex rounded-full border border-sky-400/20 bg-sky-400/10 px-4 py-1.5 text-xs uppercase tracking-[0.22em] text-sky-200">
            {{ eyebrow }}
          </div>
          <h1 class="mt-6 max-w-xl text-4xl font-semibold leading-tight text-white lg:text-5xl">
            {{ title }}
          </h1>
          <p class="mt-5 max-w-xl text-base leading-8 text-slate-300 lg:text-lg">
            {{ description }}
          </p>
          <slot name="aside" />
        </section>

        <section class="rounded-[30px] border border-white/10 bg-slate-900/90 p-6 shadow-[0_24px_80px_rgba(0,0,0,0.35)] sm:p-8">
          <slot />
        </section>
      </div>
    </main>
  </div>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue'

defineProps({
  eyebrow: {
    type: String,
    required: true,
  },
  title: {
    type: String,
    required: true,
  },
  description: {
    type: String,
    required: true,
  },
  subtitle: {
    type: String,
    required: true,
  },
  secondaryLabel: {
    type: String,
    required: true,
  },
  secondaryTo: {
    type: String,
    required: true,
  },
})
</script>
