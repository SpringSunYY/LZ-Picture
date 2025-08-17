<template>
  <div class="image-detail-page">
    <main class="main-content">
      <div class="image">
        <AiPictureView :image-url="picture.thumbnailUrl" class="image-content"/>
        <!--        <img :src="picture.thumbnailUrl" alt="Main Image" class="main-image" @contextmenu.prevent />-->
      </div>

      <div class="details-section">
        <div class="header-controls">
          <div class="user-profile">
            <a-avatar :src="picture.userInfoVo?.avatarUrl" alt="User Avatar" class="user-avatar" />
            <div class="user-info">
              <div class="user-name">{{ picture.userInfoVo?.nickName }}</div>
              <div class="user-ip">IP属地: {{ picture.userInfoVo?.ipAddress }}</div>
            </div>
          </div>
          <div class="action-buttons">
            <a-tooltip :title="picture.isLike ? '取消点赞' : '点赞'">
              <button class="icon-button favorite">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  :fill="picture.isLike ? '#ff0000' : 'none'"
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
                <span>{{ picture.likeCount }}</span>
              </button>
            </a-tooltip>
            <button class="follow-button">+ 关注</button>
          </div>
        </div>

        <div class="image-description-container">
          <h2 class="section-title">图片提示词</h2>
          <div class="description-content">
            {{ picture.introduction }}
          </div>
          <div class="info-meta">
            <a-space align="center" direction="horizontal" :wrap="true">
              <h1 class="text-xl font-bold text-white px-0.5">
                {{
                  ai_model_params_type.find((item) => item.dictValue === picture.modelType)
                    ?.dictLabel || '文生图'
                }}
              </h1>
              <div class="text-white">
                {{ picture.modelName || '即梦AI 图生图3.0' }}
              </div>
              <div class="text-white">{{ picture.picWidth }}x{{ picture.picHeight }}</div>
            </a-space>
          </div>
        </div>

        <div class="action-buttons-bottom">
          <GenerateButton class="main-button" />
          <ReferToButton class="secondary-button" />
          <DownloadButton class="download-button" />
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref } from 'vue'
import GenerateButton from '@/components/button/GenerateButton.vue'
import ReferToButton from '@/components/button/ReferToButton.vue'
import DownloadButton from '@/components/button/DownloadButton.vue'
import { useRoute } from 'vue-router'
import { getPictureDetailInfo } from '@/api/picture/picture.ts'
import type { PictureDetailInfoVo } from '@/types/picture/picture'
import AiPictureView from '@/components/AiPictureView.vue'

const { proxy } = getCurrentInstance()!
const { ai_model_params_type } = proxy?.useDict('ai_model_params_type')
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
//region详情
const picture = ref<PictureDetailInfoVo>({
  pictureId: '',
  thumbnailUrl: '',
  name: '',
  introduction: '',
  categoryName: '',
  picSize: 0,
  picWidth: 0,
  picHeight: 0,
  picScale: 0.0,
  picFormat: '',
  moreInfo: {},
  publishTime: '2025-04-10 10:30:00',
  userName: '荔枝',
  userInfoVo: {
    userId: '-1',
    userName: '荔枝',
    avatarUrl: '',
  },
})
const route = useRoute()
const pictureId = ref<string>(route.query.pictureId as string)
const getPictureInfo = () => {
  // console.log('pictureId', route.query)
  // console.log('pictureId', pictureId.value)
  getPictureDetailInfo(pictureId.value).then((res) => {
    if (res.code === 200) {
      picture.value = res?.data || {}
    }
  })
}
getPictureInfo()
//endregion
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
$content-padding: 20px; //详情内容边距

.image-detail-page {
  background-color: $bg-color;
  min-height: 100vh;
  width: 100%;
  color: $text-color;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans',
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

.image {
  flex: 2;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: $image-bg-color;
  box-sizing: border-box;
  padding: 0;
  .image-content{
    width: 100%;
    height: 100vh;
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

    .user-ip {
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


// 移动端适配
@media (max-width: $mobile-breakpoint) {
  .main-content {
    flex-direction: column;
    min-height: auto;
    gap: 0;
  }
  .image {
    order: 1;
    .image-content{
      height: 60vh;
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
}

</style>
