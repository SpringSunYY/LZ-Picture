<template>
  <section class="py-12 text-center p-8 md:p-12 animate-fade-in-up">
    <h2 class="text-5xl font-bold tracking-tight mb-16 text-blue-500 dark:text-gray-200">
      赞助我们 / 联系我们
    </h2>
    <div class="content-paragraph text-lg leading-relaxed space-y-4 text-gray-700 dark:text-gray-300 mb-12">
      <p>
        我们非常欢迎各界朋友与我们建立合作关系，无论是商业赞助、技术合作还是媒体采访，我们都期待与您携手共创价值。如果您对我们的项目感兴趣，或者有任何疑问和建议，请随时通过以下方式联系我们。
      </p>
      <p>
        您的支持是我们持续创新和发展的动力。我们相信，通过开放合作，可以共同实现更大的目标。期待您的来信！
      </p>
    </div>
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
      <div class="flex flex-col items-center">
        <img
          src="@/assets/images/wx.png"
          alt="Sponsor Image 1"
          class="w-48 h-48 object-cover rounded-lg shadow-md mb-4"
        />
        <p class="text-xl font-semibold text-gray-900 dark:text-gray-100">商业合作</p>
        <p class="text-base text-gray-700 dark:text-gray-300 mt-2">探索潜在的商业合作机会</p>
      </div>

      <div class="flex flex-col items-center">
        <img
          src="@/assets/images/wxOffer.jpg"
          alt="Sponsor Image 2"
          class="w-48 h-48 object-cover rounded-lg shadow-md mb-4"
        />
        <p class="text-xl font-semibold text-gray-900 dark:text-gray-100">媒体与公关</p>
        <p class="text-base text-gray-700 dark:text-gray-300 mt-2">微信公众号</p>
      </div>

      <div class="flex flex-col items-center">
        <img
          src="@/assets/images/wxPay.jpg"
          alt="Sponsor Image 3"
          class="w-48 h-48 object-cover rounded-lg shadow-md mb-4"
        />
        <p class="text-xl font-semibold text-gray-900 dark:text-gray-100">微信</p>
        <p class="text-base text-gray-700 dark:text-gray-300 mt-2">微信支付</p>
      </div>

      <div class="flex flex-col items-center">
        <img
          src="@/assets/images/aliPay.jpg"
          alt="Sponsor Image 3"
          class="w-48 h-48 object-cover rounded-lg shadow-md mb-4"
        />
        <p class="text-xl font-semibold text-gray-900 dark:text-gray-100">支付宝</p>
        <p class="text-base text-gray-700 dark:text-gray-300 mt-2">支付宝支付</p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts" name="ContactSponsorsSection">
import "./index.css"

</script>

<style scoped>
</style>
