<template>
  <view class="app-tabbar-wrap">
    <u-tabbar
      :list="list"
      :mid-button="true"
      :hide-tab-bar="true"
    ></u-tabbar>
  </view>
</template>

<script>
export default {
  data() {
    return {
      list: [
        {
          iconPath: 'home',
          selectedIconPath: 'home-fill',
          text: '首页',
          customIcon: false,
          pagePath: '/pages/index'
        },
        {
          iconPath: 'photo',
          selectedIconPath: 'photo-fill',
          text: '发现',
          customIcon: false,
          pagePath: '/pages/ai/discover'
        },
        {
          iconPath: 'plus-circle',
          selectedIconPath: 'plus-circle-fill',
          text: '发布',
          midButton: true,
          customIcon: false,
          pagePath: '/pages/publish/index'
        },
        {
          iconPath: 'chat',
          selectedIconPath: 'chat-fill',
          text: '消息',
          customIcon: false,
          pagePath: '/pages/message/index'
        },
        {
          iconPath: 'account',
          selectedIconPath: 'account-fill',
          text: '我的',
          customIcon: false,
          pagePath: '/pages/user/profile'
        }
      ]
    }
  }
}
</script>

<style scoped>
.app-tabbar-wrap {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 999;
}
</style>


