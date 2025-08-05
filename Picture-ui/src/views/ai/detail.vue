<template>
  <div class="image-detail-page">
    <main class="main-content">
      <div class="image-section" @click="openModal">
        <img :src="staticData.mainImage" alt="Main Image" class="main-image" @contextmenu.prevent />
      </div>

      <div class="details-section">
        <div class="header-controls">
          <div class="user-profile">
            <img :src="staticData.user.avatar" alt="User Avatar" class="user-avatar" />
            <div class="user-info">
              <div class="user-name">{{ staticData.user.name }}</div>
              <div class="creation-date">{{ staticData.creationDate }}</div>
            </div>
          </div>
          <div class="action-buttons">
            <button class="icon-button favorite">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-heart"
              >
                <path
                  d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"
                />
              </svg>
              <span>{{ staticData.likes }}</span>
            </button>
            <button class="follow-button">+ 关注</button>
          </div>
        </div>

        <div class="image-description-container">
          <h2 class="section-title">图片提示词</h2>
          <div class="description-content">
            {{ staticData.description }}
          </div>
          <div class="info-meta">
            <span>图片比例：{{ staticData.ratio }} 1024 * 1024</span>
            <div class="meta-links">
              <button class="text-button">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="lucide lucide-copy"
                >
                  <rect width="14" height="14" x="8" y="8" rx="2" ry="2" />
                  <path d="M4 16c-1.1 0-2-.9-2-2V4c0-1.1.9-2 2-2h10c1.1 0 2 .9 2 2" />
                </svg>
                复制
              </button>
              <button class="text-button">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="lucide lucide-square-arrow-out-up-right"
                >
                  <path d="M21 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h6" />
                  <path d="M21 3L13 11" />
                  <path d="M21 8V3h-5" />
                </svg>
                词语详解
              </button>
            </div>
          </div>
        </div>

        <div class="action-buttons-bottom">
          <GenerateButton class="main-button" />
          <ReferToButton class="secondary-button" />
          <DownloadButton class="download-button" />
        </div>
      </div>
    </main>

    <Transition name="fade">
      <div v-if="isModalVisible" class="modal-overlay">
        <div class="modal-image-section">
          <button class="modal-close-button" @click="closeModal">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              class="lucide lucide-x"
            >
              <path d="M18 6 6 18" />
              <path d="m6 6 12 12" />
            </svg>
          </button>
          <img
            :src="staticData.mainImage"
            alt="Enlarged Image"
            class="enlarged-image"
            @contextmenu.prevent
          />
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import GenerateButton from '@/components/GenerateButton.vue'
import ReferToButton from '@/components/ReferToButton.vue'
import DownloadButton from '@/components/DownloadButton.vue'

const staticData = {
  mainImage:
    'https://p26-dreamina-sign.byteimg.com/tos-cn-i-tb4s082cfz/258a0578277b462d84a7e0de7125aede~tplv-tb4s082cfz-aigc_resize:2400:2400.webp?lk3s=4fa96020&x-expires=1756080000&x-signature=X4kD74tLQr9pRblwGoJUb0fnAIU%3D',
  user: {
    avatar:
      'https://p26-dreamina-sign.byteimg.com/tos-cn-i-tb4s082cfz/0b1c0b3d6f1a4e1e8b2c5c93c1d4a5b6~tplv-tb4s082cfz-aigc_resize:200:200.webp',
    name: 'AIGC_创作助手',
  },
  creationDate: '2025-07-20 创作',
  likes: 58,
  description: '傍晚，海边，微风，夕阳，远处的城市灯光亮起，唯美，治愈。',
  ratio: '3:4',
}

const isModalVisible = ref(false)
const isMoreMenuVisible = ref(false)

const openModal = () => {
  isModalVisible.value = true
}

const closeModal = () => {
  isModalVisible.value = false
}
</script>

<style lang="scss" scoped>
$bg-color: #18181b; // 页面背景
$panel-bg-color: #1e1e1e; // 详情面板背景
$image-bg-color: #333; //图片背景颜色
$prompt-bg-color: #2c2c2c; // 提示词背景
$text-color: #f0f0f0; // 主要文本颜色
$secondary-text-color: #a9a9a9; // 次要文本颜色
$accent-color: #1976d2; // 强调色，用于按钮
$border-color: #424242; // 边框颜色
$button-bg-color: #303030;
$button-hover-bg: #424242;
$white: #fff;
$radius: 4px;
$padding: 24px;
$mobile-breakpoint: 768px;
$content-padding: 5px; //详情内容边距

.image-detail-page {
  background-color: $bg-color;
  min-height: 100vh;
  width: 100%;
  color: $text-color;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans',
    sans-serif;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  box-sizing: border-box;
}

.main-content {
  display: flex;
  width: 100%;
  min-height: 100vh;
}

.image-section {
  flex: 2;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: $image-bg-color;
  box-sizing: border-box;
  padding: 0;

  .main-image {
    width: 100%;
    height: 100vh;
    object-fit: contain;
    cursor: zoom-in;
  }
}

.details-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: $padding;
  background-color: $panel-bg-color;
  border-left: 1px solid $border-color;
  box-sizing: border-box;
}

.header-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $padding;
  padding-bottom: $padding / 2;
  border-bottom: 1px solid $border-color;
}

.user-profile {
  display: flex;
  align-items: center;

  .user-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-right: 12px;
  }

  .user-info {
    display: flex;
    flex-direction: column;

    .user-name {
      font-weight: 600;
      font-size: 1rem;
      color: $text-color;
      margin-bottom: 2px;
    }

    .creation-date {
      font-size: 0.8rem;
      color: $secondary-text-color;
    }
  }
}

.action-buttons {
  display: flex;
  align-items: center;
  gap: 8px;

  .icon-button {
    background: transparent;
    color: $text-color;
    border: 1px solid $border-color;
    padding: 6px 12px;
    border-radius: 20px;
    display: flex;
    align-items: center;
    gap: 4px;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      background-color: $button-hover-bg;
    }

    svg {
      stroke: $secondary-text-color;
    }

    span {
      font-size: 0.875rem;
      color: $secondary-text-color;
    }
  }

  .follow-button {
    background-color: $accent-color;
    color: $white;
    border: none;
    padding: 6px 16px;
    border-radius: 20px;
    font-size: 0.875rem;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      background-color: darken($accent-color, 10%);
    }
  }

  .more-options-container {
    position: relative;

    .more-options {
      background: transparent;
      border: none;
      padding: 6px;

      &:hover {
        background-color: $button-hover-bg;
      }
    }

    .more-menu {
      position: absolute;
      top: 100%;
      right: 0;
      min-width: 120px;
      background-color: $white;
      border: 1px solid $border-color;
      border-radius: $radius;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      z-index: 10;
      display: flex;
      flex-direction: column;
      margin-top: 8px;

      .menu-item {
        background: transparent;
        border: none;
        padding: 8px 16px;
        text-align: left;
        cursor: pointer;
        font-size: 0.875rem;
        color: $text-color;

        &:hover {
          background-color: $button-hover-bg;
        }
      }
    }
  }
}

.image-description-container {
  margin-bottom: $padding;

  .section-title {
    font-size: 0.8rem;
    //font-weight: 600;
    margin-bottom: 5px;
    color: $text-color;
  }

  .description-content {
    font-size: 0.875rem;
    color: $secondary-text-color;
    line-height: 1.5;
    background-color: $prompt-bg-color;
    padding: 12px;
    border-radius: $radius;
  }

  .info-meta {
    margin-top: $content-padding;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 1rem;
    color: $secondary-text-color;

    .meta-links {
      display: flex;
      gap: 10px;
    }

    .text-button {
      background: none;
      border: none;
      color: $secondary-text-color;
      font-size: 0.75rem;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 4px;

      &:hover {
        color: $text-color;
      }

      svg {
        stroke: $secondary-text-color;
      }
    }
  }
}

.action-buttons-bottom {
  display: flex;
  gap: 12px;
  //margin-top: auto;
  padding-top: $padding;
  border-top: 1px solid $border-color;

  .main-button,
  .secondary-button {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-grow: 1;
    padding: 10px 16px;
    border-radius: 20px;
    font-size: 0.9rem;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.2s;
    border: none;
  }

  .download-button {
    background-color: $button-bg-color;
  }

  .main-button {
    background-color: $accent-color;
    color: $white;

    &:hover {
      background-color: darken($accent-color, 10%);
    }
  }

  .secondary-button {
    background-color: $prompt-bg-color;
    color: $accent-color;
    border: 1px solid $border-color;
    gap: 6px;

    &:hover {
      background-color: darken($prompt-bg-color, 5%);
    }

    svg {
      stroke: $text-color;
    }
  }
}

// 放大模式
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  z-index: 1000;
  background-color: #161718; // 深色背景，还原图片
}

.modal-image-section {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;

  .enlarged-image {
    max-width: 100%;
    max-height: 100vh;
    object-fit: contain;
    @media (max-width: $mobile-breakpoint) {
      max-height: 60vh;
    }
  }
}

.modal-close-button {
  position: absolute;
  top: 20px;
  right: 20px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 1001;

  &:hover {
    background: rgba(255, 255, 255, 0.3);
  }

  svg {
    stroke: $white;
  }
}

// 模态框过渡效果
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.fade-in-up-enter-active,
.fade-in-up-leave-active {
  transition:
    opacity 0.2s,
    transform 0.2s;
}

.fade-in-up-enter-from,
.fade-in-up-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

// 移动端适配
@media (max-width: $mobile-breakpoint) {
  .main-content {
    flex-direction: column;
    min-height: auto;
    gap: 0;
  }
  .image-section {
    order: 1;

    .main-image {
      height: auto;
      max-height: 60vh;
    }
  }
  .details-section {
    order: 2;
    padding: 12px;
    border-left: none;
  }
  .header-controls {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    gap: 12px;

    .user-profile {
      width: auto;
    }

    .action-buttons {
      width: auto;
    }
  }
  .action-buttons-bottom {
    flex-direction: column;

    .main-button,
    .secondary-button {
      flex-basis: 100%;
    }
  }
  .modal-overlay {
    flex-direction: column;

    .modal-image-section {
      order: 1;

      .enlarged-image {
        max-height: 60vh;
      }
    }
  }
}
</style>
