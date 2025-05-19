<template>
  <a-layout class="container">
    <a-layout-content class="content">
      <a-row type="flex" align="middle" justify="center">
        <a-col :span="24" class="text-center">
          <a-typography>
            <a-typography-title :level="1" class="error-code animate__animated animate__rubberBand">
              404
            </a-typography-title>
            <a-typography-text class="error-text" type="secondary">
              您访问的页面不存在
            </a-typography-text>
          </a-typography>

          <div class="action-area">
            <a-button type="primary" shape="round" size="large" @click="goHome" class="home-button">
              <template #icon>
                <HomeOutlined />
              </template>
              返回首页
            </a-button>
          </div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script setup lang="ts">
import { HomeOutlined } from '@ant-design/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>

<style scoped lang="scss">
.container {
  margin: 0 auto;
  //min-height: 100vh;
  background: var(--bg-color);
}

.content {
  //display: flex;
  align-items: center;
  //min-height: calc(100vh - 64px);
}

.text-center {
  text-align: center;
}

.error-code {
  font-size: 8em;
  margin-bottom: 24px;
  color: var(--primary-color);
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
  animation-duration: 1s;
}

.error-text {
  font-size: 1.5em;
  display: block;
  margin-bottom: 48px;
}

.action-area {
  margin-top: 40px;
}

.home-button {
  padding: 0 40px;
  height: 48px;
  font-size: 16px;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .error-code {
    font-size: 4em;
  }

  .error-text {
    font-size: 1.2em;
  }

  .home-button {
    height: 40px;
    padding: 0 32px;
  }
}
</style>
