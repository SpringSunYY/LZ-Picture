<template>
  <button
    class="download-btn"
    :disabled="loading"
    @click="handleClick"
  >
    <span v-if="!loading">下载</span>
    <span v-else>下载中...</span>
    <svg v-if="loading" ref="svgRef" class="loading-border">
      <rect ref="rectRef" class="loading-path"/>
    </svg>
  </button>
</template>

<script lang="ts" setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'

const props = defineProps({
  loading: { type: Boolean, default: false }
})

const emit = defineEmits<{
  (e: 'click'): void
}>()

const svgRef = ref<SVGSVGElement | null>(null)
const rectRef = ref<SVGRectElement | null>(null)
let animationFrame: number

const updateRect = () => {
  if (!svgRef.value || !rectRef.value) return
  const btn = svgRef.value.parentElement
  if (!btn) return

  const width = btn.clientWidth
  const height = btn.clientHeight
  const style = window.getComputedStyle(btn)
  const radius = parseFloat(style.borderRadius)
  const strokeWidth = parseFloat(style.borderWidth) || 3

  rectRef.value.setAttribute('x', (strokeWidth/2).toString())
  rectRef.value.setAttribute('y', (strokeWidth/2).toString())
  rectRef.value.setAttribute('width', (width - strokeWidth).toString())
  rectRef.value.setAttribute('height', (height - strokeWidth).toString())
  rectRef.value.setAttribute('rx', radius.toString())
  rectRef.value.setAttribute('ry', radius.toString())
  rectRef.value.setAttribute('fill', 'none')
  rectRef.value.setAttribute('stroke', '#315cfd')
  rectRef.value.setAttribute('stroke-width', strokeWidth.toString())

  const length = rectRef.value.getTotalLength()
  rectRef.value.style.strokeDasharray = `0 ${length}`
  rectRef.value.style.strokeDashoffset = '0'

  let progress = 0
  const speed = length / 1000 // 调整速度，越大越快

  const animate = () => {
    progress += speed
    if (progress > length) progress = 0
    rectRef.value!.style.strokeDasharray = `${progress} ${length}`
    animationFrame = requestAnimationFrame(animate)
  }
  cancelAnimationFrame(animationFrame)
  animationFrame = requestAnimationFrame(animate)
}

onMounted(() => {
  nextTick(updateRect)
  window.addEventListener('resize', updateRect)
})

onBeforeUnmount(() => {
  cancelAnimationFrame(animationFrame)
  window.removeEventListener('resize', updateRect)
})

const handleClick = () => {
  if (!props.loading) {
    emit('click')
  }
}
</script>

<style scoped>
.download-btn {
  width: 100%;
  height: 60px;
  border: 3px solid rgba(110, 140, 250, 0.61);
  border-radius: 45px;
  cursor: pointer;
  background: white;
  font-size: 1.2em;
  font-weight: 550;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: visible;
}

.download-btn:disabled {
  cursor: not-allowed;
  opacity: 0.8;
}

.download-btn:hover:not(:disabled) {
  background: #315cfd;
  color: white;
  font-size: 1.5em;
}

.loading-border {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.loading-path {
  stroke-linecap: round;
}
</style>
