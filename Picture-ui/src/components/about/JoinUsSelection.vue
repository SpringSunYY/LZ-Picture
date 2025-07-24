<template>
  <section
    class="relative py-20 md:py-24 text-center bg-gradient-to-br from-orange-500 to-red-600 dark:from-orange-800 dark:to-red-900 text-white rounded-xl shadow-2xl overflow-hidden animate-fade-in-up"
  >
    <!-- 背景抽象图案 -->
    <div
      class="absolute inset-0 opacity-10 dark:opacity-20 bg-[url('/placeholder.svg?height=600&width=900')] bg-cover bg-center"
    ></div>
    <div class="content-paragraph relative z-10 max-w-3xl mx-auto px-4">
      <h2 class="text-5xl font-bold tracking-tight sm:text-6xl mb-6">加入我们，共创未来！</h2>
      <p class="mt-4 text-xl sm:text-2xl leading-relaxed">
        我们一直在寻找有才华、有热情的人加入我们的团队。如果你对创新充满激情，并希望与一群志同道合的人一起工作，共同创造未来，请查看我们的招聘页面。
      </p>
      <a
        href="#"
        class="mt-10 inline-flex items-center px-12 py-5 border border-white text-xl font-medium rounded-full shadow-lg text-white bg-transparent hover:bg-white hover:text-orange-600 dark:hover:text-orange-800 transition duration-300 ease-in-out transform hover:-translate-y-1"
      >
        查看职位空缺
      </a>
    </div>
  </section>
</template>

<script setup lang="ts" name="JoinUsSelection">
import './index.css'
</script>

<style scoped></style>
