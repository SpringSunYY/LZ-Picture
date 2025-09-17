<template>
  <div class="screen-border" :style="{ width, height }">
    <div class="stars-container">
      <div
        v-for="star in stars"
        :key="star.id"
        class="star"
        :style="{
          left: star.x + '%',
          top: star.y + '%',
          animationDelay: star.delay + 's',
          animationDuration: star.duration + 's'
        }"
      ></div>
    </div>

    <div class="main-border">
      <div class="corner-decoration top-left">
        <div class="corner-lines vertical">
          <div class="line"></div>
          <div class="line"></div>
          <div class="line"></div>
        </div>
      </div>
      <div class="corner-decoration top-right">
        <div class="corner-lines vertical">
          <div class="line"></div>
          <div class="line"></div>
          <div class="line"></div>
        </div>
      </div>
      <div class="corner-decoration bottom-left">
        <div class="corner-lines horizontal">
          <div class="line"></div>
          <div class="line"></div>
          <div class="line"></div>
        </div>
      </div>
      <div class="corner-decoration bottom-right">
        <div class="corner-lines horizontal">
          <div class="line"></div>
          <div class="line"></div>
          <div class="line"></div>
        </div>
      </div>
    </div>

    <div v-if="title" class="title-container">
      <div class="title-background">
        <span class="title-text">{{ title }}</span>
      </div>
    </div>

    <div class="content-area">
      <slot></slot>
    </div>

    <div class="waves">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path d="M0,60 C300,120 900,0 1200,60 L1200,120 L0,120 Z" class="wave-path wave1"></path>
        <path d="M0,80 C400,20 800,100 1200,40 L1200,120 L0,120 Z" class="wave-path wave2"></path>
        <path d="M0,100 C200,40 1000,80 1200,20 L1200,120 L0,120 Z" class="wave-path wave3"></path>
      </svg>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const props = defineProps({
  width: {
    type: String,
    default: '100vw'
  },
  height: {
    type: String,
    default: '100vh'
  },
  title: {
    type: String,
    default: '图片模块数据分析'
  }
})

const stars = ref([])

const generateStars = () => {
  const starCount = 400
  for (let i = 0; i < starCount; i++) {
    stars.value.push({
      id: i,
      x: Math.random() * 100,
      y: Math.random() * 100,
      delay: Math.random() * 3,
      duration: 2 + Math.random() * 4
    })
  }
}

onMounted(() => {
  generateStars()
})
</script>

<style scoped>
.screen-border {
  position: relative;
  background: linear-gradient(135deg, #0a1428 0%, #1e3a8a 50%, #1e40af 100%);
  overflow: hidden;
}

/* 星空背景 */
.stars-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.star {
  position: absolute;
  width: 2px;
  height: 2px;
  background: #00ffff;
  border-radius: 50%;
  animation: twinkle infinite ease-in-out;
  box-shadow: 0 0 4px #00ffff;
}

@keyframes twinkle {
  0%, 100% { opacity: 0.3; transform: scale(0.8); }
  50% { opacity: 1; transform: scale(1.2); }
}

/* 主边框 */
.main-border {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.main-border::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: transparent;
  border: 2px solid #00ffff;
  clip-path: polygon(
    30px 0%,
    calc(50% - 80px) 0%,
    calc(50% - 60px) 20px,
    calc(50% + 60px) 20px,
    calc(50% + 80px) 0%,
    calc(100% - 30px) 0%,
    100% 30px,
    100% calc(100% - 30px),
    calc(100% - 30px) 100%,
    30px 100%,
    0% calc(100% - 30px),
    0% 30px
  );
  box-shadow:
    0 0 20px rgba(0, 255, 255, 0.5),
    inset 0 0 20px rgba(0, 255, 255, 0.1);
  animation: borderGlow 2s ease-in-out infinite alternate;
}

@keyframes borderGlow {
  0% {
    box-shadow:
      0 0 20px rgba(0, 255, 255, 0.5),
      inset 0 0 20px rgba(0, 255, 255, 0.1);
  }
  100% {
    box-shadow:
      0 0 30px rgba(0, 255, 255, 0.8),
      inset 0 0 30px rgba(0, 255, 255, 0.2);
  }
}

/* 角落装饰 */
.corner-decoration {
  position: absolute;
  width: 60px;
  height: 60px;
}

.top-left { top: 0; left: 0; }
.top-right { top: 0; right: 0; }
.bottom-left { bottom: 0; left: 0; }
.bottom-right { bottom: 0; right: 0; }

.corner-lines {
  position: relative;
  width: 100%;
  height: 100%;
}

.corner-lines.vertical {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 12px 8px;
}

.corner-lines.vertical .line { width: 20px; height: 3px; }

.corner-lines.horizontal {
  display: flex;
  flex-direction: row;
  gap: 4px;
  padding: 8px 12px;
  align-items: flex-end;
}

.corner-lines.horizontal .line { width: 3px; height: 20px; }

.corner-lines .line {
  background: #00ffff;
  box-shadow: 0 0 8px rgba(0, 255, 255, 0.6);
  animation: cornerBlink 1.5s ease-in-out infinite;
}

@keyframes cornerBlink {
  0%, 70% { opacity: 0.4; }
  35% { opacity: 1; }
}

/* 标题区域 - 无边框 */
.title-container {
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  z-index: 10;
  padding-top: 5px; /* 调整标题与顶部主边框的距离 */
}

.title-text {
  color: #ffffff;
  font-size: 36px;
  font-weight: 700;
  text-shadow:
    0 0 10px rgba(0, 255, 255, 0.8),
    0 0 20px rgba(0, 255, 255, 0.4),
    2px 2px 4px rgba(0, 0, 0, 0.8);
  letter-spacing: 3px;
  text-transform: uppercase;
  position: relative;
  z-index: 2;
  white-space: nowrap; /* 防止标题文字换行 */
}

/* 标题背景发光动画 */
@keyframes titleGlow {
  0% {
    box-shadow:
      0 0 25px rgba(0, 255, 255, 0.6),
      inset 0 0 20px rgba(0, 255, 255, 0.15),
      0 5px 15px rgba(0, 0, 0, 0.3);
  }
  100% {
    box-shadow:
      0 0 35px rgba(0, 255, 255, 0.9),
      inset 0 0 30px rgba(0, 255, 255, 0.25),
      0 8px 20px rgba(0, 0, 0, 0.4);
  }
}

/* 内容区域 */
.content-area {
  position: relative;
  padding: 5px;
  height: 100%;
  box-sizing: border-box;
  z-index: 5;
}

/* 底部波浪 */
.waves {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 120px;
  pointer-events: none;
}

.waves svg {
  width: 100%;
  height: 100%;
}

.wave-path {
  fill: rgba(0, 255, 255, 0.1);
  animation: waveMove 6s ease-in-out infinite;
}

.wave1 { animation-delay: 0s; }
.wave2 { animation-delay: -2s; fill: rgba(0, 255, 255, 0.05); }
.wave3 { animation-delay: -4s; fill: rgba(0, 255, 255, 0.03); }

@keyframes waveMove {
  0%, 100% { d: path("M0,60 C300,120 900,0 1200,60 L1200,120 L0,120 Z"); }
  50% { d: path("M0,80 C300,20 800,100 1200,40 L1200,120 L0,120 Z"); }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .content-area {
    padding: 60px 30px 40px;
  }

  .title-text {
    font-size: 16px;
  }

  .corner-decoration {
    width: 40px;
    height: 40px;
  }

  .corner-lines.vertical .line { width: 15px; height: 2px; }
  .corner-lines.horizontal .line { width: 2px; height: 15px; }
}
</style>
