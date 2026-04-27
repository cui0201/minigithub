<template>
  <div class="absolute inset-0">
    <vue-particles
      id="tsparticles"
      :options="particlesOptions"
    />
  </div>
</template>

<script setup>
const particlesOptions = {
  fpsLimit: 60,
  fullScreen: { enable: false },
  particles: {
    number: { value: 60, density: { enable: true } },
    color: { value: ['#38bdf8', '#a78bfa', '#22d3ee', '#818cf8'] },
    shape: { type: 'circle' },
    opacity: { value: { min: 0.1, max: 0.3 }, animation: { enable: true, speed: 0.5 } },
    size: { value: { min: 1, max: 3 }, animation: { enable: true, speed: 1 } },
    links: { enable: true, distance: 150, color: '#38bdf8', opacity: 0.08, width: 1 },
    move: { enable: true, speed: 0.6, outModes: { default: 'bounce' } },
  },
  interactivity: {
    events: {
      onHover: { enable: true, mode: 'grab' },
      onClick: { enable: true, mode: 'push' },
    },
    modes: {
      grab: { distance: 180, links: { opacity: 0.15 } },
      push: { quantity: 3 },
    },
  },
  detectRetina: true,
}
</script>
