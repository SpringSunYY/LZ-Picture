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
                <p style="color: white; text-indent: 2em; font-size: 2vh">
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
          <a-space align="center" :wrap="true">
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
            <u-fold unfold line="2">
              <p class="picture-desc">
                {{ picture.introduction || '这张图还没有简介。' }}
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
          <a-space-compact direction="horizontal" align="center" style="padding: 0" :wrap="true">
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
                class="icon-button"
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
          </a-space-compact>
        </a-card>
        <a-card title="评论" v-if="showComment" :bordered="false" class="card">
          <Comment></Comment>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import FancyImage from '@/components/FancyImage.vue'
import Tags from '@/components/Tags.vue'
import { getPictureDetailInfo } from '@/api/picture/picture.ts'
import { useRoute } from 'vue-router'
import type { PictureDetailInfoVo } from '@/types/picture/picture'
import { formatSize } from '@/utils/common.ts'
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
// 获取当前路由信息
const route = useRoute()
const pictureId = ref<string>(route.query.pictureId as string)
const picture = ref<PictureDetailInfoVo>({
  pictureId: '荔智云图',
  thumbnailUrl: '',
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
  downloadPictureLoading.value = false
}

const clickLook = () => {
  message.success('点我干嘛呀😒😒😒😒😒',1)
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
