<template>
  <div>
    <a-row class="picture-detail" :gutter="[24, 24]">
      <!-- 左侧：标题、简介、图片 -->
      <a-col :xs="24" :md="16" class="left-view">
        <div class="image-wrapper">
          <ImageView :src="picture.thumbnailUrl" alt="点击预览" :zoom-scale="1.15">
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
          </ImageView>
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
            <TextView :text="picture.introduction || '作者没有抒写他的故事哦'" :max-lines="2" />
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
            <a-descriptions-item label="发布时间">{{ picture?.publishTime }}</a-descriptions-item>
          </a-descriptions>
          <Tags
            v-if="picture?.pictureTags"
            :values="picture?.pictureTags"
            :colors="['pink', 'pink', 'orange', 'green', 'cyan', 'blue', 'purple']"
          />
        </a-card>
        <a-card title="" :bordered="false" class="card action-card" v-if="picture.pictureId !== ''">
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
            <a-tooltip title="举报">
              <a-button class="icon-button" @click="handleReport">
                <SvgIcon name="report" />
              </a-button>
            </a-tooltip>
            <!-- v-if="picture?.moreInfo?.applyType !== PictureApplyTypeEnum.PICTURE_APPLY_TYPE_0"-->
            <a-tooltip>
              <template #title>
                <div style="max-width: 350px; padding: 8px; font-size: 14px; line-height: 1.6">
                  使用 {{ picture?.moreInfo?.pointsNeed || 0 }} 积分查看原图<br />
                  注意事项：<br />
                  1. 当前资源仅供展示，可以使用积分查看图片原图，请勿直接商用；<br />
                  2. 使用前请自行核实版权归属；<br />
                  3. 平台不承担任何版权纠纷责任。<br />
                  4. 如若存在侵权行为，请及时联系平台，我们会及时处理。<br />
                </div>
              </template>
              <a-button
                :loading="downloadPictureLoading"
                class="icon-button"
                type="warn"
                @click="downloadPicture"
              >
                <template #icon>
                  <SvgIcon name="viewPicture" />
                </template>
                <span style="font-size: 16px; padding-left: 8px; color: green">{{
                  picture?.moreInfo?.pointsNeed || 0
                }}</span>
                <span style="font-size: 16px; padding-left: 8px">积分</span>
              </a-button>
            </a-tooltip>
            <!--            <a-tooltip v-else>
                          <template #title>
                            <div style="max-width: 350px; padding: 8px; font-size: 14px; line-height: 1.6">
                              使用 {{ picture?.moreInfo?.priceNeed || 0 }} 元购买原图<br />
                              注意事项：<br />
                              1. 当前资源作者拥有原图信息；<br />
                              2. 如果存在版权纠纷，请联系平台，平台会为您联系作者，作者会及时处理；<br />
                              3. 平台不承担任何版权纠纷责任。<br />
                              4. 如若版权虚假，存在侵权行为，请及时联系平台，我们会及时处理。<br />
                            </div>
                          </template>
                          <a-button
                            :loading="buyPictureLoading"
                            class="icon-button download-bounce"
                            type="warn"
                            @click="buyPicture"
                          >
                            <template #icon>
                              <SvgIcon name="download" />
                            </template>
                            <span style="font-size: 16px; padding-left: 8px; color: green">
                              {{ picture?.moreInfo?.priceNeed || 0 }}
                            </span>
                            <span>元</span>
                          </a-button>
                        </a-tooltip>-->
          </a-space>
        </a-card>
      </a-col>
    </a-row>
    <VerticalFallLayout
      style="margin: 0 1em"
      :loading="loading"
      @load-more="loadMore"
      :no-more="noMore"
      :picture-list="pictureList"
    />

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

    <a-modal v-model:open="openOriginal" :footer="null" centered destroyOnClose :width="600">
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          LZ-Picture
          <a-tooltip title="感谢您使用本平台，如果觉得不错可以在关于我们请平台工作人员喝杯咖啡">
            <question-circle-outlined class="title-tip-icon" />
          </a-tooltip>
        </div>
      </template>
      <ImageView :src="originalPictureUrl" alt="点击预览" :zoom-scale="1.05" />
      <div class="form-footer">
        <a-button type="primary" @click="resetOriginalUrl">重置URL</a-button>
        <a-button @click="openOriginal = false">关闭</a-button>
      </div>
    </a-modal>

    <a-modal v-model:open="openShare" title="分享图片" @ok="openShare = !openShare">
      <QRCode :value="shareLink" />
      <QuickCopy :content="shareLink" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref } from 'vue'
import ImageView from '@/components/ImageView.vue'
import Tags from '@/components/Tags.vue'
import { getPictureDetailInfo, getPictureInfoDetailRecommend } from '@/api/picture/picture.ts'
import { useRoute } from 'vue-router'
import type {
  PictureDetailInfoVo,
  PictureInfoRecommendRequest,
  PictureInfoVo,
} from '@/types/picture/picture'
import { formatDnsUrl, formatSize } from '@/utils/common.ts'
import {
  FireOutlined,
  InfoCircleOutlined,
  LikeOutlined,
  QuestionCircleOutlined,
  ShareAltOutlined,
  StarOutlined,
} from '@ant-design/icons-vue'
import SvgIcon from '@/components/SvgIcon.vue'
import { addUserBehaviorInfo } from '@/api/picture/userBehaviorInfo.ts'
import { message } from 'ant-design-vue'
import { usePasswordVerify } from '@/utils/auth.ts'
import { addUserReportInfo } from '@/api/picture/userReportInfo.ts'
import type { UserReportInfoAdd } from '@/types/picture/userReportInfo'
import { useConfig } from '@/utils/config.ts'
import VerticalFallLayout from '@/components/VerticalFallLayout.vue'
import { getPictureOriginalLogInfo } from '@/api/common/file.ts'
import QRCode from '@/components/QRCode.vue'
import QuickCopy from '@/components/QuickCopy.vue'
import TextView from '@/components/TextView.vue'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_report_type } = proxy?.useDict('p_report_type')
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
  moreInfo: {},
  publishTime: '2025-04-10 10:30:00',
  userName: '荔枝',
  userInfoVo: {
    userId: '-1',
    userName: '荔枝',
    avatarUrl: '',
  },
})

const getPictureInfo = () => {
  // console.log('pictureId', route.query)
  // console.log('pictureId', pictureId.value)
  getPictureDetailInfo(pictureId.value).then((res) => {
    if (res.code === 200) {
      picture.value = res?.data || {}
    }
  })
}
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
//region 购买图片
const buyPictureLoading = ref(false)
const buyPicture = async () => {
  message.warning('当前功能还没开放哦', 5)
}
//endregion

//region 下载图片
const downloadPictureLoading = ref(false)
const originalPictureUrl = ref('')
const openOriginal = ref(false)
const { verify } = usePasswordVerify()
const downloadPicture = async () => {
  if (originalPictureUrl.value !== '') {
    openOriginal.value = true
    return
  }
  try {
    downloadPictureLoading.value = true
    if ((picture.value?.moreInfo?.pointsNeed ?? 0) != 0) {
      message.success('开始校验密码', 1)
      const verified = await verify('查看原图')
      if (!verified) return
      /*      await downloadImage(
        picture.value.pictureId,
        picture.value?.name + '.' + picture.value?.picFormat,
      )
      message.success('资源获取成功，之后可以在下载记录中获取原图', 3)*/
      message.success(
        '密码校验成功，如果图片需要积分请勿一直点击重置URL，每一次重置后查看都需要积分的哦...',
        5,
      )
    }
    message.success('获取图片资源中...', 3)
    message.info('请不要刷新页面', 3)
    const res = await getPictureOriginalLogInfo(picture.value.pictureId)
    message.success('资源获取成功，如果觉得不错可以在关于我们请平台工作人员喝杯咖啡', 3)
    message.success('图片查看有效时间五分钟，如果图片路径失效，请点击重置URL', 5)
    if (res.code === 200) {
      openOriginal.value = true
      originalPictureUrl.value = res.data.pictureUrl || ''
    }
  } finally {
    downloadPictureLoading.value = false
  }
}
const resetOriginalUrl = () => {
  originalPictureUrl.value = ''
  openOriginal.value = false
}
//endregion
const clickLook = () => {
  message.success('点我干嘛呀😒😒😒😒😒', 1)
}
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

// region 图片推荐列表
const loading = ref(false)
const noMore = ref(false)
const pictureList = ref<PictureInfoVo[]>([])

const pictureQuery = ref<PictureInfoRecommendRequest>({
  currentPage: 0,
  pageSize: 50,
  pictureId: pictureId.value,
})

async function loadMore() {
  if (loading.value || noMore.value) return
  message.loading('正在为您获取图片推荐...', 1)
  const res = await getPictureInfoDetailRecommend(pictureQuery.value)
  pictureList.value = res?.rows || []
  if (pictureList.value.length >= pictureQuery.value.pageSize) {
    pictureQuery.value.currentPage++
    message.success(`已为您推荐${pictureList.value.length}张图片`)
  } else {
    message.success('已为您获取全部图片推荐')
    noMore.value = true
  }
  loading.value = false
}

//endregion
getPictureInfo()
</script>

<style scoped lang="scss">
.form-footer {
  text-align: right;
  padding: 16px 0 0;
  margin-top: 24px;
  border-top: 1px solid #f0f0f0;

  .ant-btn {
    margin-left: 10px;
  }
}

.custom-modal-title {
  display: flex;
  align-items: center;
  font-size: 16px;

  .title-tip-icon {
    margin-left: 8px;
    color: rgba(57, 57, 57, 0.45);
    cursor: help;
    transition: color 0.3s;

    &:hover {
      color: #1890ff;
    }
  }
}

.picture-detail {
  padding: 20px 32px;
  background-color: #f9f9f9;
  max-width: 100%;

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
      background-color: rgba(211, 211, 211, 0.35);
    }

    .card:hover {
      //  放大
      transform: scale(1.02);
      transition: all 0.2s ease-in-out;
      border-radius: 12px;
      background-color: rgba(211, 211, 211, 0.55);
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
