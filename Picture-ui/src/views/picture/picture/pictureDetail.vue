<template>
  <div>
    <a-row class="picture-detail" :gutter="[24, 24]">
      <!-- 左侧：标题、简介、图片 -->
      <a-col :xs="24" :md="16" class="left-view">
        <div class="image-wrapper">
          <FancyImage :src="picture.thumbnailUrl" alt="点击预览" :zoom-scale="1.15">
            <template #content>
              <div style="position: relative; padding: 40px">
                <p style="font-size: 5vh; color: #00ff95; text-align: center">
                  {{ picture.name !== '' ? picture.name : 'LZ-Picture' }}
                </p>
                <p style="color: white; text-indent: 2em; font-size: 2vh">
                  {{
                    picture.introduction !== ''
                      ? picture.introduction
                      : '我始终相信，每一张图片都有属于自己的故事，而你，就是那个抒写故事的人。'
                  }}
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
          <a-space align="center" :wrap="true">
            <a-avatar :src="formatDnsUrl(picture?.userInfoVo?.avatarUrl)" size="large" />
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
            <u-fold unfold line="2">
              <p class="picture-desc">
                {{ picture.introduction || '作者还没有抒写他的故事哦。' }}
              </p>
            </u-fold>
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
            :colors="['pink', 'pink', 'orange', 'green', 'cyan', 'blue', 'purple']"
          />
        </a-card>
        <a-card title="" :bordered="false" class="card action-card">
          <a-space direction="horizontal" align="center" style="padding: 0" :wrap="true">
            <a-tooltip title="View">
              <a-button class="icon-button" @click="clickLook">
                <FireOutlined
                  :style="{
                    color: '#999',
                    verticalAlign: 'middle',
                    fontSize: '18px',
                  }"
                />
                {{ picture?.lookCount || 0 }}
              </a-button>
            </a-tooltip>
            <a-tooltip title="Like">
              <a-button class="icon-button" @click="addUserBehavior('0')">
                <LikeOutlined
                  :style="{
                    color: picture.isLike ? '#ff4d4f' : '#999',
                    verticalAlign: 'middle',
                    fontSize: '18px',
                  }"
                />
                {{ picture?.likeCount || 0 }}
              </a-button>
            </a-tooltip>
            <a-tooltip title="Star" @click="addUserBehavior('1')">
              <a-button class="icon-button">
                <StarOutlined
                  :style="{
                    color: picture.isCollect ? '#00ff95' : '#999',
                    verticalAlign: 'middle',
                    fontSize: '18px',
                  }"
                />
                {{ picture?.collectCount || 0 }}
              </a-button>
            </a-tooltip>
            <a-tooltip title="Share" @click="addUserBehavior('2')">
              <a-button class="icon-button">
                <ShareAltOutlined />
                {{ picture?.shareCount || 0 }}
              </a-button>
            </a-tooltip>
            <a-tooltip title="Comment">
              <a-button class="icon-button" @click="showComment = !showComment">
                <CommentOutlined />
              </a-button>
            </a-tooltip>
            <a-tooltip title="Download">
              <a-button
                :loading="downloadPictureLoading"
                class="icon-button download-bounce"
                type="warn"
                @click="downloadPicture"
              >
                <template #icon>
                  <SvgIcon name="download" />
                </template>
                <span style="font-size: 16px; padding-left: 8px; color: green">{{
                  picture.pointsNeed
                }}</span>
                <span style="font-size: 16px; padding-left: 8px">积分</span>
              </a-button>
            </a-tooltip>
          </a-space>
        </a-card>
        <a-card title="评论" v-if="showComment" :bordered="false" class="card">
          <Comment></Comment>
        </a-card>
      </a-col>
    </a-row>
    <verticalFallLayout></verticalFallLayout>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import FancyImage from '@/components/FancyImage.vue'
import Tags from '@/components/Tags.vue'
import { getPictureDetailInfo } from '@/api/picture/picture.ts'
import { useRoute } from 'vue-router'
import type { PictureDetailInfoVo } from '@/types/picture/picture'
import { formatDnsUrl, formatSize } from '@/utils/common.ts'
import {
  CommentOutlined,
  LikeOutlined,
  ShareAltOutlined,
  StarOutlined,
  FireOutlined,
} from '@ant-design/icons-vue'
import SvgIcon from '@/components/SvgIcon.vue'
import Comment from '@/components/Comment/Comment.vue'
import { addUserBehaviorInfo } from '@/api/picture/userBehaviorInfo.ts'
import { message } from 'ant-design-vue'
import { downloadImage } from '@/utils/file.ts'
import { usePasswordVerify } from '@/utils/auth.ts'
import verticalFallLayout from '@/views/picture/verticalFallLayout/index.vue'
// 获取当前路由信息
const route = useRoute()
const pictureId = ref<string>(route.query.pictureId as string)
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
  pointsNeed: 0,
  createTime: '2025-04-10 10:30:00',
  userName: '荔枝',
  userInfoVo: {
    userId: '-1',
    userName: '荔枝',
    avatarUrl: '',
  },
})

const showComment = ref(false)

const getPictureInfo = () => {
  // console.log('pictureId', route.query)
  // console.log('pictureId', pictureId.value)
  getPictureDetailInfo(pictureId.value).then((res) => {
    if (res.code === 200) {
      picture.value = res?.data || {}
    }
  })
}
const addUserBehavior = (behaviorType: string) => {
  console.log('behaviorType', behaviorType)
  const targetType = '0'
  let msg = '点赞成功'

  addUserBehaviorInfo({
    behaviorType: behaviorType,
    targetType: targetType,
    targetId: pictureId.value,
  }).then((res) => {
    if (res.code === 200 && res.data != undefined && res.data) {
      switch (behaviorType) {
        case '0':
          msg = '点赞成功'
          console.log('msg', msg)
          picture.value.likeCount = Number(picture.value?.likeCount || 0) + 1
          picture.value.isLike = !picture.value.isLike
          break
        case '1':
          msg = '收藏成功'
          picture.value.collectCount = Number(picture.value?.collectCount || 0) + 1
          picture.value.isCollect = !picture.value.isCollect
          break
        case '2':
          msg = '分享成功'
          console.log('msg', msg)
          picture.value.shareCount = Number(picture.value?.shareCount || 0) + 1
          break
      }
    } else {
      switch (behaviorType) {
        case '0':
          msg = '取消点赞成功'
          picture.value.likeCount = Number(picture.value?.likeCount || 0) - 1
          picture.value.isLike = !picture.value.isLike
          break
        case '1':
          msg = '取消收藏成功'
          picture.value.collectCount = Number(picture.value?.collectCount || 0) - 1
          picture.value.isCollect = !picture.value.isCollect
          break
        case '2':
          msg = '分享成功'
          break
      }
    }
    message.success(msg)
  })
}

//下载图片
const downloadPictureLoading = ref(false)
const { verify } = usePasswordVerify()
const downloadPicture = async () => {
  try {
    const verified = await verify('下载图片')
    if (!verified) return
    message.success('图片下载中...', 5)
    message.info('请不要刷新页面', 5)
    downloadPictureLoading.value = true
    const res = await downloadImage(
      picture.value.pictureId,
      picture.value?.name + '.' + picture.value?.picFormat,
    )
    message.success('资源获取成功，之后可以在下载记录中下载原图', 5)
  } finally {
    downloadPictureLoading.value = false
  }
}

const clickLook = () => {
  message.success('点我干嘛呀😒😒😒😒😒', 1)
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

      .download-bounce {
        animation: bounce 0.5s infinite ease-in-out;

        &:hover {
          animation-play-state: paused; // 悬停时暂停动画
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

@keyframes bounce {
  0%,
  100% {
    transform: translateY(0);
    animation-timing-function: cubic-bezier(0.3, 0.1, 0.1, 1); // 下落时缓动
  }
  25% {
    transform: translateY(-12px);
    animation-timing-function: cubic-bezier(0.1, 0.3, 0.5, 1); // 上升时弹性
  }
  50% {
    transform: translateY(0px); // 反弹高度减少
  }
  75% {
    transform: translateY(-6px); // 二次弹跳
  }
}
</style>
