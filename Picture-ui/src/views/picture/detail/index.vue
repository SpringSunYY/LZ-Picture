<template>
  <div>
    <a-row class="picture-detail-user" :gutter="[24, 24]">
      <!-- 左侧：标题、简介、图片 -->
      <a-col :xs="24" :md="16" class="left-view">
        <div class="image-wrapper">
          <FancyImage
            max-width="800px"
            max-height="600px"
            :src="picture.pictureUrl"
            alt="点击预览"
            :zoom-scale="1.15"
          >
            <template #content>
              <div>
                <p style="font-size: 24px; color: white">{{ picture.name }}</p>
                <p style="color: white">{{ picture.introduction || '这张图还没有简介。' }}</p>
              </div>
            </template>
          </FancyImage>
        </div>
      </a-col>

      <!-- 右侧：作者 + 图片信息 -->
      <a-col :xs="24" :md="8" class="right-info">
        <!-- 作者信息 -->
        <a-card :bordered="false" class="card author-card">
          <a-space align="center">
            <a-avatar :src="author.avatar" size="large" />
            <div>
              <div class="nickname">{{ author.nickname }}</div>
              <div class="ip-region">IP属地：{{ author.ipRegion }}</div>
            </div>
          </a-space>
        </a-card>
        <!-- 图片信息 -->
        <a-card title="图片信息" :bordered="false" class="card">
          <div class="title-block">
            <h1 class="picture-name">{{ picture.name }}</h1>
            <p class="picture-desc">{{ picture.introduction || '这张图还没有简介。' }}</p>
          </div>
        </a-card>
        <!-- 图片信息 -->
        <a-card title="" :bordered="false" class="card">
          <div class="info-row" v-for="item in infoList" :key="item.label">
            <span class="label">{{ item.label }}</span>
            <span class="value">{{ item.value }}</span>
          </div>
          <Tags
            :values="['标签1', '标签2', '标签2', '超级超级长的标签无敌长', '喜洋洋与会螳螂']"
            :colors="['#00ff0d']"
          />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive } from 'vue'
import FancyImage from '@/components/FancyImage/index.vue'
import Tags from '@/components/Tags/index.vue'

const picture = reactive({
  pictureId: 'abc123',
  pictureUrl:
    'https://litchi-picture.oss-cn-guangzhou.aliyuncs.com/picture/YY00015T-1910265413049126912-compressed.webp',
  name: '迷雾中的山脉',
  introduction: '清晨的山林笼罩在薄雾中，宁静而神秘。',
  categoryName: '自然风光',
  picSize: 3456789,
  picWidth: 1920,
  picHeight: 1080,
  picScale: 1.78,
  picFormat: 'jpeg',
  pointsNeed: 12,
  createTime: '2025-04-10 10:30:00',
})

const author = reactive({
  avatar: 'https://api.dicebear.com/7.x/adventurer/svg?seed=Mountain',
  nickname: '山里来的摄影师',
  ipRegion: '云南',
})

const formatSize = (size) => {
  if (!size) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']
  let i = 0
  while (size >= 1024 && i < units.length - 1) {
    size /= 1024
    i++
  }
  return `${size.toFixed(2)} ${units[i]}`
}

const infoList = computed(() => [
  { label: '分类', value: picture.categoryName || '未分类' },
  { label: '格式', value: picture.picFormat?.toUpperCase() },
  { label: '尺寸', value: `${picture.picWidth} × ${picture.picHeight}` },
  { label: '比例', value: picture.picScale },
  { label: '体积', value: formatSize(picture.picSize) },
  { label: '所需积分', value: `${picture.pointsNeed} 积分` },
  { label: '上传时间', value: picture.createTime },
])
</script>

<style scoped lang="scss">
.picture-detail-user {
  padding: 20px 32px;
  background-color: #f9f9f9;

  .left-view {
    .title-block {
      margin-bottom: 16px;

      .picture-name {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 8px;
      }

      .picture-desc {
        font-size: 14px;
        color: #666;
        margin: 0;
      }
    }

    .image-wrapper {
      max-height: 75vh;
      overflow: hidden;
      background: #fff;
      display: flex;
      justify-content: center;
      align-items: center;

      .main-picture {
        max-height: 100%;
        max-width: 100%;
        object-fit: contain;
      }
    }
  }

  .right-info {
    display: flex;
    flex-direction: column;
    gap: 16px;

    .card {
      //border-radius: 12px;
      background-color: #fff;
    }

    .author-card {
      margin-top: 10px;

      .nickname {
        font-size: 16px;
        font-weight: 600;
      }

      .ip-region {
        font-size: 13px;
        color: #999;
      }
    }

    .info-row {
      display: flex;
      justify-content: space-between;
      margin: 8px 0;

      .label {
        color: #999;
      }

      .value {
        font-weight: 500;
      }
    }
  }

  @media (max-width: 768px) {
    flex-direction: column;

    .left-view,
    .right-info {
      width: 100%;
    }

    .image-wrapper {
      max-height: none;
    }
  }
}
</style>
