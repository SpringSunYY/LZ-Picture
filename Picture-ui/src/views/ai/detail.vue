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
            <a-avatar
              :src="formatDnsUrl(picture.userInfoVo?.avatarUrl || '')"
              alt="User Avatar"
              class="user-avatar"
            />
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
              @click="openByUrl(picture?.thumbnailUrl || '')"
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
    />
    <a-modal v-model:open="openShare" title="分享图片" @ok="openShare = !openShare">
      <QRCode :value="shareLink" />
      <QuickCopy :content="shareLink" />
    </a-modal>
    <!--举报图片-->
    <a-modal v-model:open="openReport" :footer="null" centered destroyOnClose>
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          {{ title }}
          <a-tooltip :title="titleDesc">
            <question-circle-outlined class="title-tip-icon" />
          </a-tooltip>
        </div>
      </template>
      <a-form
        :model="formReport"
        :rules="rulesReport"
        @finish="handleSubmitReport"
        ref="formRef"
        labelAlign="left"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="举报类型" name="reportType">
          <a-radio-group v-model:value="formReport.reportType" name="radioGroup">
            <a-radio v-for="dict in p_report_type" :value="dict.dictValue" :key="dict.dictValue">
              {{ dict.dictLabel }}
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item name="reason">
          <template #label>
            <span style="display: inline-flex; align-items: center">
              举报原因
              <a-tooltip
                title="请描述您详细的举报原因，对您造成的影响，例：图片侵权，请列举您的版权信息"
              >
                <InfoCircleOutlined
                  style="
                    margin-left: 4px;
                    color: #999;
                    font-size: 14px;
                    position: relative;
                    top: 1px;
                  "
                />
              </a-tooltip>
            </span>
          </template>
          <a-textarea
            :showCount="true"
            placeholder="请输入内容"
            :auto-size="{ minRows: 5 }"
            v-model:value="formReport.reason"
          />
        </a-form-item>
        <a-form-item name="contact">
          <template #label>
            <span style="display: inline-flex; align-items: center">
              联系方式
              <a-tooltip
                title="请输入您的联系方式，手机号码、微信（推荐）等信息，例：微信：123456789，便于我们联系您处理举报信息。"
              >
                <InfoCircleOutlined
                  style="
                    margin-left: 4px;
                    color: #999;
                    font-size: 14px;
                    position: relative;
                    top: 1px;
                  "
                />
              </a-tooltip>
            </span>
          </template>
          <a-textarea
            placeholder="请输入联系方式"
            :auto-size="{ minRows: 2 }"
            :showCount="true"
            :max-length="512"
            v-model:value="formReport.contact"
          />
        </a-form-item>
        <div class="form-footer">
          <a-button @click="openReport = false">取消</a-button>
          <a-button type="primary" html-type="submit" :loading="submittingReport">提交</a-button>
        </div>
      </a-form>
    </a-modal>
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
import { formatDnsUrl } from '@/utils/common.ts'
import {
  InfoCircleOutlined,
  LikeOutlined,
  QuestionCircleOutlined,
  ShareAltOutlined,
  StarOutlined,
} from '@ant-design/icons-vue'
import SvgIcon from '@/components/SvgIcon.vue'
import QuickCopy from '@/components/QuickCopy.vue'
import QRCode from '@/components/QRCode.vue'
import type { UserReportInfoAdd } from '@/types/picture/userReportInfo'
import { addUserReportInfo } from '@/api/picture/userReportInfo.ts'
import { addUserBehaviorInfo } from '@/api/picture/userBehaviorInfo.ts'
import { message } from 'ant-design-vue'
import { useConfig } from '@/utils/config.ts'
import AiInput from '@/components/AiInput.vue'
import { defaultModelInfo, type ModelInfo } from '@/types/ai/model.d.ts'
import { openByUrl } from '@/utils/file.ts'

const { proxy } = getCurrentInstance()!
const { ai_model_params_type, p_report_type } = proxy?.useDict(
  'ai_model_params_type',
  'p_report_type',
)
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
const openReport = ref(false)
const title = ref('举报图片')
const titleDesc = ref('请选择举报图片类型')
const formReport = ref<UserReportInfoAdd>({
  targetType: '0',
  targetId: picture.value.pictureId,
  reportType: '0',
  reason: '',
  contact: '',
})
const rulesReport = ref({
  reason: [
    {
      required: true,
      message: '请输入举报内容',
      trigger: 'blur',
    },
    //长度最短为32
    {
      min: 16,
      message: '请输入16个字符以上的内容',
      trigger: 'blur',
    },
  ],
  reportType: [
    {
      required: true,
      message: '请选择举报类型',
      trigger: 'change',
    },
  ],
  contact: [
    {
      required: true,
      message: '请输入联系方式',
      trigger: 'blur',
    },
    //长度最短为32
    {
      min: 16,
      message: '请输入16个字符以上的内容',
      trigger: 'blur',
    },
  ],
})
const submittingReport = ref(false)
const handleReport = async () => {
  titleDesc.value = await useConfig('picture:report:content')
  openReport.value = true
  title.value = '举报图片'
  formReport.value = {
    targetType: '0',
    targetId: picture.value.pictureId,
    reportType: '0',
    reason: '',
  }
}
const handleSubmitReport = () => {
  submittingReport.value = true
  addUserReportInfo(formReport.value).then((res) => {
    if (res.code === 200) {
      message.success('举报成功')
      openReport.value = false
      submittingReport.value = false
    } else {
      message.error('举报失败')
    }
  })
}
// endregion
//region 用户行为
const addUserBehavior = (behaviorType: string) => {
  const targetType = '0'
  let msg = '点赞成功'
  //如果是分享
  if (behaviorType === '2') {
    shareLink.value = window.location.href
    console.log('shareLink', shareLink.value)
  }

  addUserBehaviorInfo({
    behaviorType: behaviorType,
    targetType: targetType,
    targetId: pictureId.value,
    shareLink: shareLink.value,
  }).then((res) => {
    if (res.code === 200 && res.data != undefined && res.data) {
      switch (behaviorType) {
        case '0':
          msg = '点赞成功'
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
          picture.value.shareCount = Number(picture.value?.shareCount || 0) + 1
          handleShare()
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
          handleShare()
          break
      }
    }
    message.success(msg)
  })
}
const openShare = ref(false)
const shareLink = ref('')
const handleShare = () => {
  openShare.value = true
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
