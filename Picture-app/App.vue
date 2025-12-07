<template>
  <view>
  </view>
</template>

<script>
export default {
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
