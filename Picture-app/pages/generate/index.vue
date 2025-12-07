<template>
  <view class="ai-generate">
    <!-- 上方输入区域 -->
    <view class="input-section">
      <!-- Tab 切换 -->
      <view class="tab-container">
        <view class="tab-header">
          <view
              class="tab-item"
              :class="{ active: activeTab === '1' }"
              @tap="clickActiveTab('1')"
          >
            <text>文生图</text>
          </view>
          <view
              class="tab-item"
              :class="{ active: activeTab === '2' }"
              @tap="clickActiveTab('2')"
          >
            <text>图生图</text>
          </view>
        </view>
        <view v-if="activeTab === '2'" class="tab-content">
          <AiPictureUpload v-model="fileInfo" class="upload-container"/>
        </view>
      </view>

      <!-- 创意描述 -->
      <view class="prompt">
        <text class="prompt-title">创意描述</text>
        <textarea
            v-model="prompt"
            placeholder="请输入内容"
            class="text-area"
            :maxlength="800"
            :auto-height="true"
            :show-confirm-bar="false"
        />
        <view class="char-count">{{ prompt.length }}/800</view>
        <view class="tag-container">
          <AiRecommend v-model="prompt"/>
        </view>
      </view>

      <!-- 模型选择 -->
      <view class="model">
        <AiCheckModel v-model="modelInfo" @update:modelValue="handleModelInfoUpdate"/>
      </view>

      <!-- 生成按钮 -->
      <view class="generate">
        <view class="generate-tip">
          <text>预计需要消耗{{ modelInfo.pointsNeed }}积分</text>
        </view>
        <GenerateButton :is-loading="isGenerating" @click="submitGenerate" class="generate-btn"/>
      </view>
    </view>

    <!-- 下方生成列表 -->
    <scroll-view
        class="list-section"
        scroll-y
        @scrolltolower="handleScrollToLower"
        :lower-threshold="200"
    >
      <view v-if="generateList.length > 0">
        <view
            class="content"
            v-for="generate in generateList"
            :key="generate.logId"
        >
          <view class="content-header">
            <view class="header-left">
              <view class="header-info">
                <zui-svg-icon icon="space" class="header-icon"/>
                <text class="header-text">
                  {{
                    (ai_model_params_type.value || []).find(
                        (item) => item.dictValue === generate.modelType
                    )?.dictLabel || '未知'
                  }}
                </text>
                <text class="header-divider">|</text>
                <text class="header-text">{{ generate.modelName }}</text>
                <text class="header-text">{{ generate.width }}x{{ generate.height }}</text>
              </view>
            </view>
            <view class="header-right">
              <view class="header-action" @tap="handlePrompt(generate)">
                <zui-svg-icon icon="edit" class="action-icon"/>
              </view>
              <view
                  class="header-action"
                  @tap="handleReload(generate)"
              >
                <zui-svg-icon icon="reload" class="action-icon"/>
              </view>
            </view>
          </view>

          <view class="content-text">
            <TextView :text="generate.prompt" :max-lines="3"/>
          </view>

          <view class="content-picture">
            <AiPictureView
                v-if="generate.logStatus === AiLogStatusEnum.SUCCESS"
                class="picture"
                :image-url="generate.fileUrls"
            />
            <AiLoading
                class="picture"
                v-else-if="generate.logStatus === AiLogStatusEnum.REQUESTING"
            />
            <view class="picture-overlay">
              <view class="overlay-right-top">
                <view class="action-button-wrapper" @tap.stop="openByUrl(generate.fileUrls)">
                  <DownloadSvgButton/>
                </view>
                <view class="action-button-wrapper" @tap.stop="handleDelete(generate)">
                  <DeleteButton/>
                </view>
              </view>
              <view class="overlay-bottom">
                <AiBatchButton
                    @handle-refer-to="handleReferTo(generate)"
                    @handle-release="handlePublic(generate)"
                    @handle-reload="handleReload(generate)"
                />
              </view>
            </view>
          </view>
        </view>

        <view class="load-more-trigger">
          <LoadingData v-if="isLoadingMore"/>
          <NoMoreData
              v-else-if="noMore && generateList.length > 0"
              text="没有更多数据了哦，快去生成吧！！！"
          />
        </view>
      </view>

      <view v-else class="no-data">
        <text class="no-data-title">开始创作</text>
        <text class="no-data-subtitle">创造你的下一张图片</text>
      </view>
    </scroll-view>

    <!-- 发布弹窗 -->
    <AiPublishModal
        ref="publishModalRef"
        :initial-item="selectedItem"
        @success="handlePublicSuccess"
    />
    <AppTabbar/>
  </view>
</template>

<script setup>
import {onMounted, onUnmounted, ref} from 'vue'
import AiCheckModel from '@/components/ai/AiCheckModel.vue'
import GenerateButton from '@/components/button/GenerateButton.vue'
import AiPictureUpload from '@/components/ai/AiPictureUpload.vue'
import ZuiSvgIcon from '@/uni_modules/zui-svg-icon/components/zui-svg-icon/zui-svg-icon.vue'
import TextView from '@/components/TextView.vue'
import AiPictureView from '@/components/ai/AiPictureView.vue'
import DeleteButton from '@/components/button/DeleteButton.vue'
import DownloadSvgButton from '@/components/button/DownloadSvgButton.vue'
import AiBatchButton from '@/components/button/AiBatchButton.vue'
import NoMoreData from '@/components/NoMoreData.vue'
import LoadingData from '@/components/LoadingData.vue'
import AiLoading from '@/components/ai/AiLoading.vue'
import {usePasswordVerify} from '@/utils/auth'
import AiRecommend from '@/components/ai/AiRecommend.vue'
import {toast} from '@/utils/common'
import AiPublishModal from '@/components/ai/AiPublishModal.vue'
import {useDict} from '@/utils/useDict'
import {generate, queryTask,} from '@/api/picture/picture'
import {deleteGenerateLogInfo, listGenerateLogInfo} from '@/api/ai/model'
import AppTabbar from "@/components/AppTabbar.vue";
import {
  AiLogStatusEnum,
  AiGenerateHasPublicEnum,
} from '@/utils/enums'

// 字典数据
const {ai_model_params_type} = useDict('ai_model_params_type')

const activeTab = ref('1')

// 默认模型信息
const defaultModelInfo = {
  modelType: '1',
  modelKeys: [],
  numbers: 1,
  width: 682,
  height: 1024,
  pointsNeed: 0,
}

// 列表相关
const generateList = ref([])
const generateQuery = ref({
  pageNum: 1,
  pageSize: 15,
})
const isLoadingMore = ref(false)
const noMore = ref(false)

const getGenerateList = async () => {
  if (isLoadingMore.value || noMore.value) return
  isLoadingMore.value = true

  try {
    const res = await listGenerateLogInfo(generateQuery.value)
    if (res.code === 200 && res.data) {
      if (!generateList.value) {
        generateList.value = []
      }
      if (res.data.rows && res.data.rows.length > 0) {
        generateList.value = [...generateList.value, ...res.data.rows]
        if (res.data.rows.length < (generateQuery.value.pageSize ?? 15)) {
          noMore.value = true
        }
        // 如果还有未完成的，开始轮询
        res.data.rows.forEach((item) => {
          if (item.logStatus === AiLogStatusEnum.REQUESTING) {
            setTimeout(async () => {
              await pollGenerateTask(item)
            }, 5000)
          }
        })
      } else {
        noMore.value = true
      }
    }
  } catch (error) {
    console.error('获取生成列表失败:', error)
  } finally {
    isLoadingMore.value = false
  }
}

const loadMoreData = () => {
  generateQuery.value.pageNum = 1 + (generateQuery.value.pageNum || 0)
  getGenerateList()
}

const handleScrollToLower = () => {
  if (!isLoadingMore.value && !noMore.value) {
    loadMoreData()
  }
}

// 生成操作
const isGenerating = ref(false)
const modelInfo = ref(defaultModelInfo)
const prompt = ref('')
const fileInfo = ref('')

// 为每个类型维护独立的模型信息（包括图片比例和生成数量）
const modelInfoByType = ref({
  '1': {
    ...defaultModelInfo,
    modelType: '1',
    width: 682,
    height: 1024,
    numbers: 1,
  }, // 文生图
  '2': {
    ...defaultModelInfo,
    modelType: '2',
    width: 682,
    height: 1024,
    numbers: 1,
  }, // 图生图
})

// 初始化当前类型的模型信息
modelInfo.value = {...modelInfoByType.value[activeTab.value]}

const handleReferTo = (generate) => {
  activeTab.value = '2'
  fileInfo.value = generate.fileUrls
}

const handleReload = (generate) => {
  // 保存当前类型的模型信息
  if (activeTab.value) {
    modelInfoByType.value[activeTab.value] = {...modelInfo.value}
  }
  
  activeTab.value = '2'
  fileInfo.value = generate.fileUrls
  prompt.value = generate.prompt
  
  const reloadModelInfo = {
    modelType: generate.modelType,
    modelKeys: [generate.modelKey],
    numbers: 1,
    width: generate.width,
    height: generate.height,
    pointsNeed: generate.pointsUsed,
  }
  
  modelInfo.value = reloadModelInfo
  // 同步更新到对应类型
  modelInfoByType.value[generate.modelType] = {...reloadModelInfo}
}

const handlePrompt = (generate) => {
  prompt.value = generate.prompt
}

// 处理模型信息更新，同步保存到对应类型
const handleModelInfoUpdate = (newModelInfo) => {
  // 同步更新当前类型的模型信息
  if (activeTab.value && modelInfoByType.value[activeTab.value]) {
    modelInfoByType.value[activeTab.value] = {...newModelInfo}
  }
}

const clickActiveTab = (key) => {
  // 保存当前类型的模型信息
  if (activeTab.value) {
    modelInfoByType.value[activeTab.value] = {...modelInfo.value}
  }
  
  // 切换到新类型
  activeTab.value = key
  
  // 恢复新类型的模型信息
  if (modelInfoByType.value[key]) {
    modelInfo.value = {...modelInfoByType.value[key]}
  } else {
    // 如果新类型没有保存的信息，使用默认值
    modelInfo.value = {...defaultModelInfo, modelType: key}
    modelInfoByType.value[key] = {...modelInfo.value}
  }
}

const {verify} = usePasswordVerify()

const submitGenerate = async () => {
  // 校验参数
  if (
      !modelInfo.value?.width ||
      modelInfo.value?.width < 256 ||
      !modelInfo.value?.height ||
      modelInfo.value?.height < 256
  ) {
    toast('请填写图片尺寸,宽高不可小于256')
    return
  }

  if (!modelInfo.value?.modelKeys || modelInfo.value?.modelKeys.length <= 0) {
    toast('请选择模型')
    return
  }

  if (!modelInfo.value?.numbers || modelInfo.value?.numbers <= 0) {
    toast('请填写数量')
    return
  }

  if (!prompt.value || prompt.value.length <= 0) {
    toast('请填写提示词')
    return
  }

  toast('开始校验密码')
  const verified = await verify('生成图片')
  if (!verified) return

  isGenerating.value = true
  toast('正在生成图片，请不要刷新界面...')

  try {
    const res = await generate({
      prompt: prompt.value,
      modelKeys: modelInfo.value?.modelKeys,
      modelType: modelInfo.value?.modelType || '',
      width: modelInfo.value?.width,
      height: modelInfo.value?.height,
      numbers: modelInfo.value?.numbers || 1,
      inputFile: fileInfo.value || null,
    })

    if (res.code === 200 && res.data) {
      res.data.forEach((item) => {
        if (item.logStatus === AiLogStatusEnum.FAILED) {
          toast('模型：' + item.modelLabel + '生成失败')
        } else if (item.logStatus === AiLogStatusEnum.SUCCESS) {
          toast('模型：' + item.modelLabel + '生成成功')
        } else {
          toast('模型：' + item.modelLabel + '生成中...')
        }
      })

      generateQuery.value.pageNum = 1
      generateList.value = []
      noMore.value = false
      isLoadingMore.value = false
      await getGenerateList()
    }
  } catch (error) {
    toast('生成失败,请刷新页面')
    console.error('生成失败:', error)
  } finally {
    isGenerating.value = false
  }
}

// 轮询相关
const pollingMap = new Map()

const pollGenerateTask = async (item) => {
  const logId = item.logId
  try {
    const res = await queryTask(logId)
    if (res.code === 200 && res.data) {
      if (res.data.logStatus === AiLogStatusEnum.SUCCESS) {
        stopPolling(logId)
        generateList.value = generateList.value.map((item) => {
          if (item.logId === logId) {
            return {
              ...item,
              logStatus: res.data?.logStatus || item.logStatus,
              fileUrls: res.data?.fileUrls || item.fileUrls,
            }
          }
          return item
        })
      } else if (res.data.logStatus === AiLogStatusEnum.REQUESTING) {
        const timer = setTimeout(() => pollGenerateTask(item), 5000)
        pollingMap.set(logId, timer)
      } else {
        toast(
            item.modelName +
            '生成失败，请检查生成内容是否可能侵犯版权，使用的积分已经返回您的账户'
        )
        generateList.value = generateList.value.filter(
            (item) => item.logId !== logId
        )
        stopPolling(logId)
      }
    }
  } catch (e) {
    console.error('轮询任务失败:', e)
    stopPolling(logId)
  }
}

const stopPolling = (logId) => {
  const timer = pollingMap.get(logId)
  if (timer) {
    clearTimeout(timer)
    pollingMap.delete(logId)
  }
}

// 删除
const handleDelete = async (item) => {
  const logId = item.logId
  uni.showModal({
    title: '删除生成记录',
    content: '您确定删除此次生成记录吗？删除之后不可恢复哦',
    confirmText: '确认',
    cancelText: '取消',
    success: async (res) => {
      if (res.confirm) {
        try {
          const result = await deleteGenerateLogInfo(logId)
          if (result.code === 200) {
            toast('删除成功')
            generateList.value = generateList.value.filter(
                (item) => item.logId !== logId
            )
          } else {
            toast('删除失败')
          }
        } catch (error) {
          toast('删除失败')
          console.error('删除失败:', error)
        }
      }
    },
  })
}

// 发布作品
const publishModalRef = ref(null)
const selectedItem = ref(null)

const handlePublic = (item) => {
  if (item.hasPublic === AiGenerateHasPublicEnum.HAS_PUBLIC_0) {
    toast('该作品已发布，请勿重复发布')
    return
  }
  selectedItem.value = item
  if (publishModalRef.value) {
    publishModalRef.value.openModal()
  }
}

const handlePublicSuccess = () => {
  const logId = selectedItem.value?.logId
  if (logId) {
    generateList.value = generateList.value.map((item) => {
      if (item.logId === logId) {
        return {
          ...item,
          hasPublic: AiGenerateHasPublicEnum.HAS_PUBLIC_0,
        }
      }
      return item
    })
  }
  selectedItem.value = null
}

// 打开URL（下载）
const openByUrl = (url) => {
  if (!url) return
  // #ifdef H5
  window.open(url)
  // #endif
  // #ifndef H5
  uni.previewImage({
    urls: [url],
    current: url,
  })
  // #endif
}

// 组件卸载时清理轮询
onUnmounted(() => {
  pollingMap.forEach((timer) => clearTimeout(timer))
  pollingMap.clear()
})

// 初始化
onMounted(() => {
  getGenerateList()
})
</script>

<style scoped lang="scss">
// 主题颜色 - 柔和的紫色调
$primary-color: #7c6bd9;
$primary-light: #9a8de8;
$primary-dark: #6a5bc4;
$bg-primary: #1e1e2e;
$bg-secondary: #252535;
$bg-card: #2a2a3a;
$border-color: rgba(124, 107, 217, 0.2);
$text-primary: #ffffff;
$text-secondary: rgba(255, 255, 255, 0.75);
$text-muted: rgba(255, 255, 255, 0.55);

.ai-generate {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  width: 100%;
  background: linear-gradient(180deg, $bg-primary 0%, $bg-secondary 100%);
}

.input-section {
  background: linear-gradient(180deg, $bg-card 0%, $bg-secondary 100%);
  display: flex;
  flex-direction: column;
  padding: 40rpx 32rpx;
  width: 100%;
  border-bottom: 2rpx solid $border-color;

  .tab-container {
    width: 100%;
    margin-bottom: 40rpx;

    .tab-header {
      display: flex;
      justify-content: center;
      background-color: rgba(124, 107, 217, 0.08);
      border: 1rpx solid $border-color;
      border-radius: 20rpx;
      padding: 6rpx;
      box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);

      .tab-item {
        flex: 1;
        text-align: center;
        padding: 20rpx 24rpx;
        color: $text-secondary;
        font-size: 30rpx;
        transition: all 0.3s;
        border-radius: 14rpx;
        font-weight: 500;

        &.active {
          background-color: rgba(124, 107, 217, 0.25);
          color: $text-primary;
          font-weight: 600;
          box-shadow: 0 2rpx 8rpx rgba(124, 107, 217, 0.2);
        }
      }
    }

    .tab-content {
      margin-top: 32rpx;

      .upload-container {
        width: 100%;
        height: 600rpx;
      }
    }
  }

  .prompt {
    color: $text-primary;
    margin-bottom: 40rpx;

    .prompt-title {
      font-size: 44rpx;
      font-weight: bold;
      color: $primary-light;
      display: block;
      margin-bottom: 24rpx;
    }

    .text-area {
      width: 100%;
      min-height: 240rpx;
      background-color: rgba(124, 107, 217, 0.06);
      color: $text-primary;
      border: 1rpx solid $border-color;
      border-radius: 16rpx;
      padding: 24rpx;
      font-size: 30rpx;
      box-sizing: border-box;
      line-height: 1.6;
      transition: all 0.3s;

      &:focus {
        border-color: rgba(124, 107, 217, 0.4);
        background-color: rgba(124, 107, 217, 0.08);
        box-shadow: 0 0 0 2rpx rgba(124, 107, 217, 0.1);
      }
    }

    .char-count {
      text-align: right;
      color: $text-muted;
      font-size: 24rpx;
      margin-top: 12rpx;
    }

    .tag-container {
      margin-top: 32rpx;
    }
  }

  .model {
    margin-bottom: 32rpx;
  }

  .generate {
    margin-top: 32rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 24rpx 0;

    .generate-tip {
      margin-bottom: 20rpx;
      color: $text-secondary;
      font-size: 26rpx;
      padding: 12rpx 24rpx;
      background-color: rgba(124, 107, 217, 0.08);
      border-radius: 16rpx;
      border: 1rpx solid $border-color;
    }
  }
}

.list-section {
  background-color: $bg-primary;
  flex: 1;
  width: 100%;

  .content {
    margin: 24rpx 24rpx;
    padding: 32rpx;
    background-color: rgba(124, 107, 217, 0.04);
    border-radius: 20rpx;
    border: 1rpx solid $border-color;
    margin-bottom: 24rpx;

    .content-header {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      align-items: center;
      gap: 16rpx;
      margin-bottom: 16rpx;

      .header-left {
        flex: 1;
        min-width: 0;

        .header-info {
          display: flex;
          align-items: center;
          flex-wrap: wrap;
          gap: 16rpx;

          .header-icon {
            font-size: 36rpx;
            color: $primary-light;
          }

          .header-text {
            color: $text-primary;
            font-size: 28rpx;
            font-weight: 600;
          }

          .header-divider {
            color: $text-muted;
            margin: 0 12rpx;
            font-weight: 300;
          }
        }
      }

      .header-right {
        display: flex;
        gap: 16rpx;

        .header-action {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 64rpx;
          height: 64rpx;
          background-color: rgba(124, 107, 217, 0.12);
          border: 1rpx solid $border-color;
          border-radius: 14rpx;
          transition: all 0.3s;

          &:active {
            background-color: rgba(124, 107, 217, 0.25);
            transform: scale(0.95);
          }

          .action-icon {
            font-size: 32rpx;
            color: $text-primary;
          }
        }
      }
    }

    .content-text {
      color: $text-secondary;
      text-indent: 2em;
      font-size: 28rpx;
      line-height: 1.8;
      margin-bottom: 32rpx;
      padding: 20rpx;
      background-color: rgba(124, 107, 217, 0.04);
      border-radius: 14rpx;
      border-left: 3rpx solid rgba(124, 107, 217, 0.4);
    }

    .content-picture {
      height: auto;
      min-height: 500rpx;
      background-color: rgba(124, 107, 217, 0.06);
      border-radius: 20rpx;
      position: relative;
      overflow: hidden;
      border: 1rpx solid $border-color;

      .picture {
        height: 100%;
        width: 100%;
      }

      .picture-overlay {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        display: flex;
        opacity: 0;
        transition: opacity 0.3s ease;

        .overlay-right-top {
          position: absolute;
          top: 32rpx;
          right: 32rpx;
          display: flex;
          gap: 16rpx;

          .action-button-wrapper {
            display: flex;
            align-items: center;
            justify-content: center;
          }
        }

        .overlay-bottom {
          position: absolute;
          bottom: 32rpx;
          left: 32rpx;
        }
      }

      &:active .picture-overlay {
        opacity: 1;
      }

      .picture-overlay .overlay-right-top,
      .picture-overlay .overlay-bottom {
        z-index: 10;
      }
    }
  }

  .load-more-trigger {
    padding: 40rpx;
  }

  .no-data {
    padding: 80rpx 40rpx;
    text-align: center;
    margin-top: 200rpx;

    .no-data-title {
      display: block;
      color: $primary-light;
      font-size: 64rpx;
      font-weight: bold;
      margin-bottom: 32rpx;
    }

    .no-data-subtitle {
      display: block;
      color: $text-secondary;
      font-size: 32rpx;
    }
  }
}
</style>
