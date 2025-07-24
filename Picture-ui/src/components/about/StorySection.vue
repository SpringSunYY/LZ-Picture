<template>
  <!-- Our Story Section -->
  <section class="grid md:grid-cols-2 gap-16 items-center py-12 animate-fade-in-up">
    <div class="order-2 md:order-1 animate-fade-in-left">
      <h2 class="text-5xl font-bold tracking-tight mb-6 text-green-500 dark:text-green-300">
        我们的故事
      </h2>
      <div
        class="content-paragraph text-lg leading-relaxed space-y-4 text-gray-700 dark:text-gray-300"
      >
        <p>
          我们工作室成立于 2022 年，源于对软件开发的热爱，对技术的提升。
          从一个简单的大学生期末作业，从一个简单的想法开始，到开发出属于我们的产品。
          我们一步步发展壮大，始终秉持着为客户创造价值的核心理念。我们相信，通过持续的学习和不懈的努力，可以为世界带来积极的改变。
        </p>
        <p>
          在过去的几年里，我们克服了无数挑战，更经历了无数次失败，我们不仅积累了宝贵的经验，更坚定了我们的决心。
          我们每一次的进步都离不开团队的辛勤付出和用户的坚定支持，
          更为所取得的成就感到自豪，并期待着未来的更多可能。
        </p>
      </div>
    </div>
    <div
      class="relative w-full aspect-video rounded-xl overflow-hidden shadow-2xl order-1 md:order-2 transform hover:scale-105 transition-transform duration-500 animate-fade-in-right"
    >
      <img :src="profileImage" alt="Our Story" class="object-cover w-full h-full" />
    </div>
  </section>
</template>

<script setup lang="ts" name="StorySection">
import './index.css'

import profileImage from '@/assets/images/profile.jpg'
</script>

<style scoped></style>
