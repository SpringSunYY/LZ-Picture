<template>
  <section class="content-paragraph py-12 text-center animate-fade-in-up">
    <h2 class="text-5xl font-bold tracking-tight mb-16 text-yellow-500 dark:text-gray-200">
      我们的团队
    </h2>
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-12">
      <div
        class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-8 transform transition-transform duration-500 hover:scale-105 hover:shadow-2xl animate-fade-in-up"
      >
        <img
          src="@/assets/images/profile.jpg"
          alt="Jane Doe - CEO"
          class="w-40 h-40 rounded-full mx-auto mb-6 object-cover border-4 border-green-500 dark:border-green-600 shadow-md"
        />
        <h3 class="text-3xl font-semibold text-gray-900 dark:text-gray-100">YY</h3>
        <h5 class="text-xl text-gray-600 dark:text-gray-400 mt-2">CEO</h5>
        <p class="mt-4 text-base text-gray-700 dark:text-gray-300 content-paragraph">
          Jane 开发工程师，是公司的灵魂人物，带领团队不断突破创新。
        </p>
      </div>

      <div
        class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-8 transform transition-transform duration-500 hover:scale-105 hover:shadow-2xl animate-fade-in-up animation-delay-100"
      >
        <img
          src="@/assets/images/profile.jpg"
          alt="John Smith - CTO"
          class="w-40 h-40 rounded-full mx-auto mb-6 object-cover border-4 border-teal-500 dark:border-teal-600 shadow-md"
        />
        <h3 class="text-3xl font-semibold text-gray-900 dark:text-gray-100">YY</h3>
        <h5 class="text-xl text-gray-600 dark:text-gray-400 mt-2">CTO</h5>
        <p class="mt-4 text-base text-gray-700 dark:text-gray-300 content-paragraph">
          负责公司的技术战略和创新，确保我们的产品始终走在行业前沿，为用户提供最先进的解决方案。
        </p>
      </div>

      <div
        class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-8 transform transition-transform duration-500 hover:scale-105 hover:shadow-2xl animate-fade-in-up animation-delay-200"
      >
        <img
          src="@/assets/images/profile.jpg"
          alt="Emily White - Marketing Director"
          class="w-40 h-40 rounded-full mx-auto mb-6 object-cover border-4 border-orange-500 dark:border-orange-600 shadow-md"
        />
        <h3 class="text-3xl font-semibold text-gray-900 dark:text-gray-100">YY</h3>
        <h5 class="text-xl text-gray-600 dark:text-gray-400 mt-2">市场总监</h5>
        <p class="mt-4 text-base text-gray-700 dark:text-gray-300 content-paragraph">
          带领团队将我们的品牌推向市场，通过创新的营销策略，与用户建立深厚的情感连接。
        </p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts" name="TeamSection">
import './index.css'
</script>

<style scoped></style>
