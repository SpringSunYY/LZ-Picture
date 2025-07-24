<template>
  <section class="grid md:grid-cols-2 gap-16 items-center py-12 animate-fade-in-up">
    <div
      class="relative w-full aspect-video rounded-xl overflow-hidden shadow-2xl transform hover:scale-105 transition-transform duration-500 animate-fade-in-left"
    >
      <img src="@/assets/images/vision.jpeg" alt="Our Vision" class="object-cover w-full h-full" />
    </div>
    <div class="animate-fade-in-right">
      <h2 class="text-5xl font-bold tracking-tight mb-6 text-teal-600 dark:text-teal-300">
        我们的愿景
      </h2>
      <div
        class="content-paragraph text-lg leading-relaxed space-y-4 text-gray-700 dark:text-gray-300"
      >
        <p>
          我们的愿景是成为行业领导者，不断探索前沿技术，为全球客户提供最先进、最可靠的解决方案。我们致力于构建一个开放、协作和创新的生态系统，让每个人都能从我们的产品和服务中受益。
        </p>
        <p>
          我们坚信，通过技术的力量，可以连接世界，赋能个体，并共同创造一个更加美好的未来。我们将持续投入研发，倾听用户声音，不断超越自我，以实现这一宏伟愿景，为社会贡献我们的力量。
        </p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts" name="VisionSection">
import './index.css'
// 组件逻辑可以在这里添加
</script>

<style scoped></style>
