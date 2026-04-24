<template>
  <div id="tsparticles-container">
    <Particles
      id="tsparticles"
      :options="particlesOptions"
    />
  </div>
</template>

<script setup>
import Particles from '@tsparticles/vue3'
import { loadSlim } from '@tsparticles/slim'

const particlesOptions = {
  fullScreen: {
    enable: true,
    zIndex: 0
  },
  background: {
    color: '#020617'
  },
  fpsLimit: 60,
  interactivity: {
    events: {
      onHover: {
        enable: true,
        mode: 'grab',
        parallax: {
          enable: true,
          force: 60,
          smooth: 10
        }
      },
      resize: true
    },
    modes: {
      grab: {
        distance: 180,
        links: {
          opacity: 0.3
        }
      },
      push: {
        quantity: 4
      },
      repulse: {
        distance: 100,
        duration: 0.4
      }
    }
  },
  particles: {
    color: {
      value: ['#38bdf8', '#a78bfa', '#22d3ee']
    },
    links: {
      color: '#38bdf8',
      distance: 150,
      enable: true,
      opacity: 0.12,
      width: 1
    },
    move: {
      direction: 'none',
      enable: true,
      outModes: {
        default: 'bounce'
      },
      random: false,
      speed: 1.2,
      straight: false
    },
    number: {
      density: {
        enable: true,
        area: 800
      },
      value: 80
    },
    opacity: {
      value: 0.5,
      random: {
        enable: true,
        minimumValue: 0.15
      },
      animation: {
        enable: true,
        speed: 1,
        minimumValue: 0.1
      }
    },
    size: {
      value: { min: 1, max: 3 },
      random: {
        enable: true,
        minimumValue: 1
      },
      animation: {
        enable: true,
        speed: 2,
        minimumValue: 0.3
      }
    }
  },
  detectRetina: true
}

async function particlesInit(engine) {
  await loadSlim(engine)
}
</script>

<style scoped>
#tsparticles-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
}
</style>
