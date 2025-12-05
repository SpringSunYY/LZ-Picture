<template>
  <view>
    <!-- 全局密码验证组件 -->
    <AiPasswordVerify />
  </view>
</template>

<script>
import AiPasswordVerify from '@/components/ai/AiPasswordVerify.vue'

export default {
  components: {
    AiPasswordVerify
  },
  onLaunch() {
    console.log('App Launch')
  },
  onShow() {
    console.log('App Show')
  },
  onHide() {
    console.log('App Hide')
  },
  onLastPageBackPress() {
    console.log('onLastPageBackPress')
  },
  onPageNotFound(options) {
    console.log('页面不存在:', options)
    uni.redirectTo({
      url: '/pages/user/login',
    })
  },
}
</script>

<style>
/*每个页面公共css */
.uni-row {
  flex-direction: row;
}

.uni-column {
  flex-direction: column;
}
</style>
