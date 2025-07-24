<template>
  <div>
    <!--关于我们-->
    <AboutLayout />
    <main class="max-w-7xl mx-auto px-2 sm:px-6 lg:px-5 py-10 space-y-10">
      <!--我们的故事-->
      <StorySection />
      <!--我们的愿景-->
      <VisionSection />
      <!-- 联系我们部分 -->
      <ContactSection />
      <!--加入我们-->
      <JoinUsSelection />
      <!-- 赞助我们与联系我们 -->
      <ContactSponsorsSection />
      <!-- 团队部分 -->
      <TeamSection />
      <!--版权-->
      <CopyrightSection />
      <!-- 帮助中心部分 -->
      <HelpCenterSection />
    </main>
  </div>
</template>

<script setup lang="ts" name="AboutView">
import AboutLayout from '@/components/about/AboutLayout.vue'
import StorySection from '@/components/about/StorySection.vue'
import VisionSection from '@/components/about/VisionSection.vue'
import ContactSection from '@/components/about/ContactSection.vue'
import TeamSection from '@/components/about/TeamSection.vue'
import HelpCenterSection from '@/components/about/HelpCenterSection.vue'
import CopyrightSection from '@/components/about/CopyrightSection.vue'
import ContactSponsorsSection from '@/components/about/ContactSponsorsSection.vue'
import JoinUsSelection from '@/components/about/JoinUsSelection.vue'
</script>

<style scoped></style>
