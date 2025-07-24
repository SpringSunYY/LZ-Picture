<template>
  <section class="relative hero-section">
    <div class="relative z-10 max-w-4xl mx-auto px-4 text-center">
      <h1 class="text-6xl font-extrabold tracking-tight sm:text-7xl md:text-8xl animate-fade-in-up">
        关于我们
      </h1>
      <p class="mt-6 text-xl sm:text-2xl md:text-3xl max-w-3xl mx-auto leading-relaxed animate-fade-in-up animation-delay-200">
        我们（荔枝软件开发工作室）致力于通过创新和卓越，为您的生活和工作带来积极的改变，致力于一览无余的无限进步。
      </p>
    </div>
    <!-- 背景装饰元素 -->
    <div class="hero-decoration">
      <div class="decoration-circle circle-1"></div>
      <div class="decoration-circle circle-2"></div>
      <div class="decoration-circle circle-3"></div>
      <div class="decoration-circle circle-4"></div>
      <div class="decoration-circle circle-5"></div>
      <div class="decoration-circle circle-6"></div>
    </div>
  </section>
</template>

<script setup lang="ts" name="AboutView">
import "./index.css"

</script>

<style scoped>
.hero-section {
  position: relative;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 4rem 2rem;
  margin: 0;
  overflow: hidden;
  color: white;
  box-shadow: 0 20px 40px rgba(102, 126, 234, 0.3);
}

.hero-decoration {
  position: absolute;
  top: 0;
  right: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 0 20px rgba(255, 255, 255, 0.1);
}

.circle-1 {
  width: 150px;
  height: 150px;
  top: -75px;
  right: -75px;
  animation: float-1 8s ease-in-out infinite;
}

.circle-2 {
  width: 100px;
  height: 100px;
  top: 60%;
  left: 10%;
  animation: float-2 6s ease-in-out infinite reverse;
}

.circle-3 {
  width: 80px;
  height: 80px;
  bottom: 20px;
  right: 20%;
  animation: float-3 7s ease-in-out infinite;
}

.circle-4 {
  width: 60px;
  height: 60px;
  bottom: 10px;
  right: 10%;
  animation: float-4 5s ease-in-out infinite;
}

.circle-5 {
  width: 40px;
  height: 40px;
  top: 10px;
  left: 10%;
  animation: float-5 3s ease-in-out infinite;
}

.circle-6 {
  width: 20px;
  height: 20px;
  bottom: 30px;
  right: 40%;
  animation: float-6 2s ease-in-out infinite;
}

@keyframes float-1 {
  0%, 100% { transform: translateY(0px) translateX(0px); }
  50% { transform: translateY(-30px) translateX(15px); }
}

@keyframes float-2 {
  0%, 100% { transform: translateY(0px) translateX(0px); }
  50% { transform: translateY(10px) translateX(-10px); }
}

@keyframes float-3 {
  0%, 100% { transform: translateY(0px) translateX(0px); }
  50% { transform: translateY(-20px) translateX(-10px); }
}

@keyframes float-4 {
  0%, 100% { transform: translateY(0px) translateX(0px); }
  50% { transform: translateY(50px) translateX(40px); }
}

@keyframes float-5 {
  0%, 100% { transform: translateY(0px) translateX(0px); }
  50% { transform: translateY(20px) translateX(20px); }
}

@keyframes float-6 {
  0%, 100% { transform: translateY(0px) translateX(0px); }
  50% { transform: translateY(10px) translateX(10px); }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .hero-section {
    padding: 3rem 1.5rem;
  }

  .hero-section h1 {
    font-size: 2.5rem;
  }

  .hero-section p {
    font-size: 1.25rem;
  }
}
</style>
