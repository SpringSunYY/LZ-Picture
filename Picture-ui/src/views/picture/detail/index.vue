<template>
  <div>
    <a-row class="picture-detail" :gutter="[24, 24]">
      <!-- 左侧：标题、简介、图片 -->
      <a-col :xs="24" :md="16" class="left-view">
        <div class="image-wrapper">
          <FancyImage :src="picture.thumbnailUrl" alt="点击预览" :zoom-scale="1.15">
            <template #content>
              <div style="position: relative; padding: 40px">
                <p style="font-size: 5vh; color: #00ff95; text-align: center">{{ picture.name }}</p>
                <p style="color: white; text-indent: 2em;font-size: 2vh">
                  {{ picture.introduction || '这张图还没有简介。' }}
                </p>
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
            <a-avatar :src="picture.userInfoVo?.avatarUrl" size="large" />
            <div>
              <div class="nickname">{{ picture.userInfoVo?.nickName }}</div>
              <div class="ip-region">IP属地：{{ picture.userInfoVo?.ipAddress || '未知' }}</div>
            </div>
          </a-space>
        </a-card>
        <!-- 图片信息 -->
        <a-card title="" :bordered="false" class="card">
          <div class="title-block">
            <h1 class="picture-name">{{ picture.name }}</h1>
            <p class="picture-desc">
              {{ formatStrSize(picture.introduction || '', 90) || '这张图还没有简介。' }}
            </p>
          </div>
        </a-card>
        <!-- 图片信息 -->
        <a-card title="" :bordered="false" class="card">
          <a-descriptions title="图片信息" :column="{ xs: 2, sm: 1, md: 2 }">
            <a-descriptions-item label="分类">{{ picture.categoryName }}</a-descriptions-item>
            <a-descriptions-item label="空间">{{ picture.spaceName }}</a-descriptions-item>
            <a-descriptions-item label="格式">{{ picture.picFormat }}</a-descriptions-item>
            <a-descriptions-item label="体积"
              >{{ formatSize(picture.picSize || 0) }}
            </a-descriptions-item>
            <a-descriptions-item label="尺寸"
              >{{ picture.picWidth }} * {{ picture.picHeight }}
            </a-descriptions-item>
            <a-descriptions-item label="比例">{{ picture.picScale }}</a-descriptions-item>
          </a-descriptions>
          <Tags
            v-if="picture?.pictureTags"
            :values="picture?.pictureTags"
            :colors="['pink', 'red', 'orange', 'green', 'cyan']"
          />
        </a-card>
        <a-card title="" :bordered="false" class="card action-card">
          <a-space-compact align="center" style="padding: 0">
            <a-tooltip title="Like">
              <a-button class="icon-button">
                <LikeOutlined style="vertical-align: middle; font-size: 18px" />
                3200
              </a-button>
            </a-tooltip>
            <a-tooltip title="Star">
              <a-button class="icon-button"> <StarOutlined />320 </a-button>
            </a-tooltip>
            <a-tooltip title="Share">
              <a-button class="icon-button"> <ShareAltOutlined />320 </a-button>
            </a-tooltip>
            <a-tooltip title="Comment">
              <a-button class="icon-button">
                <CommentOutlined />
              </a-button>
            </a-tooltip>
            <a-tooltip title="Download">
              <a-button class="icon-button">
                <template #icon>
                  <SvgIcon name="download" />
                </template>
                <span style="font-size: 16px; padding-left: 8px; color: green">{{
                  picture.pointsNeed
                }}</span>
                <span style="font-size: 16px; padding-left: 8px">积分</span>
              </a-button>
            </a-tooltip>
          </a-space-compact>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import FancyImage from '@/components/FancyImage/index.vue'
import Tags from '@/components/Tags/index.vue'
import { getPictureDetailInfo } from '@/api/picture/picture.ts'
import { useRoute } from 'vue-router'
import type { PictureDetailInfoVo } from '@/types/picture/picture'
import { formatSize, formatStrSize } from '@/utils/common.ts'
import {
  CommentOutlined,
  LikeOutlined,
  ShareAltOutlined,
  StarOutlined,
} from '@ant-design/icons-vue'
import SvgIcon from '@/components/SvgIcon/index.vue'
// 获取当前路由信息
const route = useRoute()
const pictureId = ref<string>(route.query.pictureId as string)
const picture = ref<PictureDetailInfoVo>({
  pictureId: '荔枝云图库',
  thumbnailUrl:
    'https://litchi-picture.oss-cn-guangzhou.aliyuncs.com/picture/YY00037T-1910243995154518016.JPG',
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
  userName: '荔枝',
  userInfoVo: {
    userId: '1',
    userName: '荔枝',
    avatarUrl: 'https://api.dicebear.com/7.x/adventurer/svg?seed=Mountain',
  },
})

const getPictureInfo = () => {
  console.log('pictureId', route.query)
  console.log('pictureId', pictureId.value)
  getPictureDetailInfo(pictureId.value).then((res) => {
    if (res.code === 200) {
      picture.value = res?.data || {}
    }
  })
}
getPictureInfo()
</script>

<style scoped lang="scss">
.picture-detail {
  padding: 20px 32px;
  background-color: #f9f9f9;

  .left-view {
    .image-wrapper {
      overflow: hidden;
      background: #fff;
    }
  }

  .right-info {
    display: flex;
    flex-direction: column;
    gap: 16px;

    .card {
      width: 100%;
      //border-radius: 12px;
      background-color: #fff;
    }
    .action-card {
      .icon-button {
        display: flex;
        align-items: center;
        justify-content: center;

        .anticon {
          font-size: 18px;
        }
      }
    }
    .author-card {
      .nickname {
        font-size: 16px;
        font-weight: 600;
      }

      .ip-region {
        font-size: 13px;
        color: #999;
      }
    }

    .title-block {
      .picture-name {
        font-size: 24px;
        font-weight: bold;
        color: #88e868;
      }

      .picture-desc {
        text-indent: 2em;
        font-size: 14px;
        color: #666;
        margin: 0;
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
}
</style>
