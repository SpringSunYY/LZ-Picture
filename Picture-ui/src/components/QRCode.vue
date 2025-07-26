<template>
  <div class="qrcode-container">
    <a-qrcode
      :size="size"
      :color="color"
      :icon-size="iconSize"
      :error-level="errorLevel"
      :value="value"
      :icon="iconPath"
    />
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  value: {
    default: 'https://springsun.online',
    type: String,
  },
  size: {
    default: 320,
    type: Number,
  },
  errorLevel: {
    default: 'H',
    type: String,
  },
  color: {
    default: '#1755c8',
    type: String,
  },
  iconSize: {
    default: 80,
    type: Number,
  },
  iconPath: {
    default: '/src/assets/logo.svg',
    type: String,
  },
})
</script>

<style scoped lang="scss">
.qrcode-container {
  width: 100%;
  max-width: 100%;  // 最大宽度为父容器的宽度
  height: auto;
  display: flex;
  justify-content: center;  // 水平居中
  align-items: center;  // 垂直居中
  padding: 20px;  // 添加一些内边距避免内容触碰到边界

  .ant-qrcode {
    max-width: 100%;  // 限制二维码最大宽度
    max-height: 100%;  // 限制二维码最大高度
    width: 100%;  // 让二维码适应父容器
    height: auto;  // 保持宽高比
  }
}

@media (max-width: 600px) {
  .qrcode-container {
    padding: 10px;
  }
  .ant-qrcode {
    width: 80%;  // 小屏幕下让二维码占用较小空间
  }
}
</style>
