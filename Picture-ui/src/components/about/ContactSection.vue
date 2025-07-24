<template>
  <section class="grid md:grid-cols-2 gap-16 items-center py-12 animate-fade-in-up">
    <div class="order-2 md:order-1 animate-fade-in-left">
      <h2 class="text-5xl font-bold tracking-tight mb-6 text-purple-500 dark:text-green-300">
        联系我们
      </h2>
      <div class="content-paragraph text-lg leading-relaxed space-y-4 text-gray-700 dark:text-gray-300">
        <p>这是我们的工作室第一个产品，我们不知道是否能够成功，但是也要勇于尝试。</p>
        <p>
          所以我们也需要展示我们主要的业务，帮助我们能够逐步壮大，完善自己。如果您有需要可以联系我们。
        </p>
      </div>
    </div>
    <div class="relative w-full aspect-video rounded-xl overflow-hidden shadow-2xl order-1 md:order-2 transform hover:scale-105 transition-transform duration-500 animate-fade-in-right">
      <img src="@/assets/images/card.png" alt="Our Team" class="object-cover w-full h-full" />
    </div>
  </section>
</template>

<script setup lang="ts" name="ContactSection">
import "./index.css"

</script>

<style scoped>

</style>
