<template>
  <div class="image-detail-page">
    <main class="main-content">
      <div class="image">
        <AiPictureView :image-url="picture.thumbnailUrl" class="image-content" />
        <!--        <img :src="picture.thumbnailUrl" alt="Main Image" class="main-image" @contextmenu.prevent />-->
      </div>

      <div class="details-section">
        <div class="header-controls">
          <div class="user-profile">
            <a-tooltip title="查看作者更多创作">
              <a-avatar
                @click="handleUserInfo(picture?.userInfoVo?.userName || '')"
                :src="initCover(picture.userInfoVo?.avatarUrl || '')"
                alt="User Avatar"
                class="user-avatar"
              />
            </a-tooltip>
            <div class="user-info">
              <div class="user-name">{{ picture.userInfoVo?.nickName }}</div>
              <div class="user-ip">IP属地: {{ picture.userInfoVo?.ipAddress }}</div>
            </div>
          </div>
          <div class="action-buttons">
            <a-space direction="horizontal" align="center" style="padding: 0" :wrap="true">
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
              <a-tooltip title="举报">
                <a-button class="icon-button" @click="handleReport">
                  <SvgIcon name="report" />
                </a-button>
              </a-tooltip>
              <button class="follow-button">+ 关注</button>
            </a-space>
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
                  ai_model_params_type.find((item) => item.dictValue === picture.moreInfo.modelType)
                    ?.dictLabel || '文生图'
                }}
              </h1>
              <div class="text-white">
                {{ picture.moreInfo?.modelName || '即梦AI 图生图3.0' }}
              </div>
              <div class="text-white">{{ picture.picWidth }}x{{ picture.picHeight }}</div>
            </a-space>
          </div>
        </div>

        <div class="action-buttons-bottom">
          <a-tooltip title="开始生成，图片后台将使用无水印原图">
            <GenerateButton @click="handleReGenerate" class="main-button" />
          </a-tooltip>
          <a-tooltip title="用作参考图，后台将会使用无水印原图作为参考图">
            <ReferToButton @click="handleReferTo" class="secondary-button" />
          </a-tooltip>
          <a-tooltip :title="'预计消耗积分' + (picture?.moreInfo?.pointsNeed || 0)">
            <DownloadButton
              :loading="downloadPictureLoading"
              @click="downloadPicture"
              class="download-button"
            />
          </a-tooltip>
        </div>
      </div>
    </main>

    <AiInput
      @success="generateSuccess"
      v-show="openAiInput"
      :file-info="fileInfo"
      :model-info="modelInfo"
      :prompt="prompt"
      :target-id="pictureId"
    />
    <a-modal v-model:open="openShare" title="分享图片" @ok="openShare = !openShare">
      <QRCode :value="shareLink" />
      <QuickCopy :content="shareLink" />
    </a-modal>
    <!--举报图片-->
    <PictureReportModel
      ref="reportModalRef"
      :targetId="picture.pictureId"
      @success="handleReportSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref } from 'vue'
import GenerateButton from '@/components/button/GenerateButton.vue'
import ReferToButton from '@/components/button/ReferToButton.vue'
import DownloadButton from '@/components/button/DownloadButton.vue'
import { useRoute, useRouter } from 'vue-router'
import { getPictureDetailInfo } from '@/api/picture/picture.ts'
import type { PictureDetailInfoVo } from '@/types/picture/picture'
import AiPictureView from '@/components/ai/AiPictureView.vue'
import { initCover } from '@/utils/common.ts'
import { LikeOutlined, ShareAltOutlined, StarOutlined } from '@ant-design/icons-vue'
import SvgIcon from '@/components/SvgIcon.vue'
import QuickCopy from '@/components/QuickCopy.vue'
import QRCode from '@/components/QRCode.vue'
import { message } from 'ant-design-vue'
import AiInput from '@/components/ai/AiInput.vue'
import { defaultModelInfo, type ModelInfo } from '@/types/ai/model.d.ts'
import { downloadImage } from '@/utils/file.ts'
import { usePasswordVerify } from '@/utils/auth.ts'
import PictureReportModel from '@/components/picture/PictureReportModel.vue'
import { useUserBehavior } from '@/utils/useUserBehavior.ts'

const { proxy } = getCurrentInstance()!
const { ai_model_params_type } = proxy?.useDict('ai_model_params_type')
//region生成图片
const openAiInput = ref(false)
const modelInfo = ref<ModelInfo>(defaultModelInfo)
const prompt = ref('')
const fileInfo = ref('')
//生成图片
const handleReGenerate = () => {
  fileInfo.value = picture.value?.thumbnailUrl || ''
  prompt.value = picture.value?.introduction || ''
  openAiInput.value = true
  modelInfo.value = {
    modelType: picture.value?.moreInfo?.modelType || '',
    modelKeys: [picture.value?.moreInfo?.modelKey || ''],
    numbers: 1,
    width: picture.value?.picWidth,
    height: picture.value?.picHeight,
    pointsNeed: picture.value?.moreInfo?.modelPoints || 0,
  }
}
//引用图片
const handleReferTo = () => {
  fileInfo.value = picture.value?.thumbnailUrl || ''
  openAiInput.value = true
}
//图片生成成功后，重新加载图片列表
const generateSuccess = () => {
  message.success('生成成功,请前往我的资产查看')
}
//endregion
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

//region 举报图片
const reportModalRef = ref<any>(null)

const handleReport = () => {
  reportModalRef.value.handleOpen()
}

const handleReportSuccess = () => {
  // 举报成功后的处理，比如显示一个提示
  console.log('举报成功，可以在这里进行一些后续操作')
}
// endregion
//region 用户行为
// 使用组合式函数
const { openShare, shareLink, addUserBehavior, handleShare } = useUserBehavior(picture)

const downloadPictureLoading = ref(false)
const { verify } = usePasswordVerify()
const downloadPicture = async () => {
  console.log('downloadPicture')
  try {
    message.success('开始校验密码', 1)
    const verified = await verify('查看原图')
    if (!verified) return
    downloadPictureLoading.value = true
    await downloadImage(
      picture.value.pictureId,
      picture.value?.name + '.' + picture.value?.picFormat,
    )
    message.success('下载图片获取成功，之后可以在下载记录中获取原图', 3)
  } finally {
    downloadPictureLoading.value = false
  }
}
//endregion
//region 查看作者
const router = useRouter()
const handleUserInfo = (username: string) => {
  console.log('handleUserInfo', username)
  if (!username || username.trim() === '') {
    message.warn('用户不存在')
    return
  }
  router.push({
    name: 'aiUser',
    query: {
      username: username,
    },
  })
}
//endregion
</script>

<style lang="scss" scoped>
$bg-color: #18181b; // 页面背景
$panel-bg-color: #1e1e1e; // 详情面板背景
$image-bg-color: #333; // 图片背景颜色
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
$content-padding: 20px; // 详情内容边距

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

.image {
  flex: 2;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: $image-bg-color;
  box-sizing: border-box;
  padding: 0;

  .image-content {
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
  padding-bottom: 12px;
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
      background-color: color.adjust($accent-color, $lightness: -10%);
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
  .download-button,
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
    min-width: 150px;
  }

  .download-button {
    background-color: $button-bg-color;
  }

  .main-button {
    background-color: $accent-color;
    color: $white;

    &:hover {
      background-color: color.adjust($accent-color, $lightness: -10%);
    }
  }

  .secondary-button {
    background-color: $prompt-bg-color;
    color: $accent-color;
    border: 1px solid $border-color;
    gap: 6px;

    &:hover {
      background-color: color.adjust($prompt-bg-color, $lightness: -5%);
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

    .image-content {
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
