<template>
  <div class="ai-generate flex flex-col md:flex-row w-full min-h-screen">
    <div class="left w-full md:w-1/3 p-4">
      <a-tabs v-model:activeKey="activeTab" class="tab-container" size="large" centered>
        <a-tab-pane key="1" tab="文生图"></a-tab-pane>
        <a-tab-pane key="2" tab="图生图">
          <AiPictureUpload style="width: 100%; height: 300px" />
        </a-tab-pane>
      </a-tabs>
      <div class="prompt mt-4">
        <h1 class="text-xl text-blue-4 font-bold">创意描述</h1>
        <a-textarea
          placeholder="请输入内容"
          :border="false"
          :auto-size="{ minRows: 5, maxRows: 10 }"
          :maxlength="800"
          :allowClear="true"
          show-count
          class="text-area mt-1"
          v-model:value="prompt"
        />
        <div class="tag-container mt-4">
          <a-space wrap>
            <h1 class="text-xl font-600">推荐</h1>
            <a-tag color="success">success</a-tag>
            <a-tag color="processing">processing</a-tag>
            <a-tag color="error">error</a-tag>
            <a-tag color="warning">warning</a-tag>
            <a-tag color="default">default</a-tag>
          </a-space>
        </div>
      </div>
      <div class="model mt-4">
        <AiCheckModel v-model="modelInfo" />
      </div>
      <div class="generate">
        <a-tooltip placement="top">
          <template #title>
            <span>预计需要消耗{{ modelInfo.pointsNeed }}积分</span>
          </template>
          <generate-button :is-loading="isGenerating" @click="submitGenerate" style="width: 60%" />
        </a-tooltip>
      </div>
    </div>
    <div
      class="right w-full md:w-2/3 p-4 overflow-y-auto"
      @scroll="handleScroll"
      ref="scrollContainer"
    >
      <div class="content" v-for="generate in generateList" :key="generate.logId">
        <div class="content-heard">
          <div class="heard-left">
            <a-space align="center" direction="horizontal" :wrap="true">
              <div class="px-1">
                <SvgIcon name="space" size="1.3em" />
              </div>
              <h1 class="text-xl font-bold text-white px-0.5">
                {{
                  ai_model_params_type.find((item) => item.dictValue === generate.modelType)
                    .dictLabel
                }}
              </h1>
              <div>
                <SvgIcon name="dividingLine" size="1em"></SvgIcon>
              </div>
              <div class="text-white">
                {{ generate.modelName }}
              </div>
              <div class="text-white">{{ generate.width }}x{{ generate.height }}</div>
            </a-space>
          </div>
          <div class="heard-right">
            <a-space>
              <div class="px-1 heard-right-svg">
                <a-tooltip title="使用本次创意生成">
                  <svg-icon name="edit" size="1em" />
                </a-tooltip>
              </div>
              <div class="px-1 heard-right-svg">
                <a-tooltip title="重新生成需要10积分">
                  <svg-icon name="reload" size="1em" />
                </a-tooltip>
              </div>
            </a-space>
          </div>
        </div>
        <div class="content-text text-white mt-1">
          <TextView :text="generate.prompt" :max-lines="3" />
        </div>
        <div class="content-picture mt-5">
          <AiPictureView
            v-if="generate.logStatus === AiLogStatusEnum.SUCCESS"
            class="picture"
            :image-url="generate.fileUrls"
          />
          <AiLoading
            class="picture"
            v-else-if="generate.logStatus === AiLogStatusEnum.REQUESTING"
          />
          <div class="picture-overlay">
            <a-space class="overlay-right-top">
              <DownloadSvgButton
                class="action-button"
                @click.stop="
                  () => {
                    console.log('删除')
                  }
                "
              />
              <DeleteButton
                @click.stop="
                  () => {
                    console.log('删除')
                  }
                "
              />
            </a-space>
            <div class="overlay-bottom">
              <AiBatchButton />
            </div>
          </div>
        </div>
      </div>
      <div ref="loadMoreTrigger" class="load-more-trigger">
        <LoadingData v-if="isLoadingMore" />
        <NoMoreData v-else-if="noMore" text="没有更多数据了哦，快去生成吧！！！" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, nextTick, onMounted, onUnmounted, ref } from 'vue'
import AiCheckModel from '@/components/AiCheckModel.vue'
import GenerateButton from '@/components/button/GenerateButton.vue'
import AiPictureUpload from '@/components/AiPictureUpload.vue'
import SvgIcon from '@/components/SvgIcon.vue'
import TextView from '@/components/TextView.vue'
import AiPictureView from '@/components/AiPictureView.vue'
import DeleteButton from '@/components/button/DeleteButton.vue'
import DownloadSvgButton from '@/components/button/DownloadSvgButton.vue'
import AiBatchButton from '@/components/button/AiBatchButton.vue'
import {
  AiLogStatusEnum,
  type GenerateLogInfoQuery,
  type ModerInfo,
  type UserGenerateLogInfoVo,
} from '@/types/ai/model.d.ts'
import { generate, listGenerateLogInfo } from '@/api/ai/model.ts'
import { message } from 'ant-design-vue'
import NoMoreData from '@/components/NoMoreData.vue'
import LoadingData from '@/components/LoadingData.vue'
import AiLoading from '@/components/AiLoading.vue'

const { proxy } = getCurrentInstance()!
const { ai_model_params_type } = proxy?.useDict('ai_model_params_type')
const activeTab = ref('1')

//region 列表
const generateList = ref<UserGenerateLogInfoVo[]>([])
const generateQuery = ref<GenerateLogInfoQuery>({
  pageNum: 1,
  pageSize: 10,
})
const isLoadingMore = ref(false)
const noMore = ref(false)
const getGenerateList = async () => {
  if (isLoadingMore.value || noMore.value) return
  isLoadingMore.value = true
  await listGenerateLogInfo(generateQuery.value).then((res) => {
    if (!generateList.value) {
      generateList.value = []
    }
    generateList.value = [...generateList.value, ...res.rows]
    if (res.rows.length < generateQuery.value.pageSize) {
      noMore.value = true
    }
  })
  isLoadingMore.value = false
}
const loadMoreData = () => {
  generateQuery.value.pageNum = 1 + generateQuery.value.pageNum
  getGenerateList()
}
getGenerateList()
const scrollContainer = ref<HTMLElement | null>(null)
const handleScroll = () => {
  const container = scrollContainer.value
  if (container) {
    const { scrollTop, scrollHeight, clientHeight } = container
    if (scrollTop + clientHeight >= scrollHeight - 200 && !isLoadingMore.value) {
      loadMoreData()
    }
  }
}

onMounted(() => {
  nextTick(() => {
    if (scrollContainer.value) {
      scrollContainer.value.addEventListener('scroll', handleScroll)
    }
  })
})

onUnmounted(() => {
  if (scrollContainer.value) {
    scrollContainer.value.removeEventListener('scroll', handleScroll)
  }
})
//endregion
//region 生成操作
const isGenerating = ref(false)
const modelInfo = ref<ModerInfo>({
  width: 682,
  height: 1024,
  modelType: '',
  modelKeys: [],
  numbers: 1,
})
const prompt = ref('')
const submitGenerate = async () => {
  console.log('提交生成')
  console.log(modelInfo.value)
  console.log(prompt.value)
  //校验参数是否填写
  if (
    !modelInfo.value?.width ||
    modelInfo.value?.width < 256 ||
    !modelInfo.value?.height ||
    modelInfo.value?.height < 0
  ) {
    message.warn('请填写图片尺寸,宽高不可小于256')
    return
  }
  if (!modelInfo.value?.modelKeys || modelInfo.value?.modelKeys.length <= 0) {
    message.warn('请选择模型')
    return
  }
  if (!modelInfo.value?.numbers || modelInfo.value?.numbers <= 0) {
    message.warn('请填写数量')
  }
  if (!prompt.value || prompt.value.length <= 0) {
    message.warn('请填写提示词')
    return
  }
  isGenerating.value = true
  message.success('正在生成图片，请不要刷新界面...', 5)
  console.log('开始生成图片', modelInfo.value)
  try {
    const res = await generate({
      prompt: prompt.value,
      modelKeys: modelInfo.value?.modelKeys,
      modelType: modelInfo.value?.modelType || '',
      width: modelInfo.value?.width,
      height: modelInfo.value?.height,
      numbers: modelInfo.value?.numbers || 1,
    })
    if (res.code === 200) {
      message.success(res.msg)
      generateQuery.value.pageNum = 1
      generateList.value = []
      noMore.value = false
      isLoadingMore.value = false
      await getGenerateList()
    }
  } finally {
    isGenerating.value = false
  }
}
//endregion
</script>

<style scoped lang="scss">
$bg-left-color: #2c2c2c;
$bg-left-border-color: rgba(218, 218, 218, 0.71);
$bg-right-svg-color: rgba(218, 218, 218, 0.11);
$bg-right-image-color: rgba(218, 218, 218, 0.05);
$bg-right-color: #000000;
$text-color: #ffffff;

.ai-generate {
  min-height: 100vh;
}

.left {
  background-color: $bg-left-color;
  min-height: 100vh;
  display: flex;
  flex-direction: column;

  .prompt,
  .tab-container {
    color: $text-color;

    .ant-tabs-tab {
      font-size: 32px;
    }
  }

  .generate {
    //底部居中
    margin-top: auto;
    display: flex;
    justify-content: center;
    padding: 20px 0;
  }

  /* 修改 textarea 背景色和文字色 */
  ::v-deep(.text-area .ant-input) {
    background-color: $bg-left-color;
    font-size: 1.2em;
    color: $text-color;
    border-color: $bg-left-border-color;
  }

  ::v-deep(.text-area .ant-input::placeholder) {
    color: #aaa;
    opacity: 1;
  }

  ::v-deep(.ant-input-textarea-show-count::after) {
    color: $text-color;
  }

  :deep(.ant-tabs-tab-active) {
    color: #ffffff;
    font-size: 1.5em;
  }

  .tab-container {
    width: 100%;
  }
}

.right {
  background-color: $bg-right-color;
  height: 100vh;
  overflow-y: auto;
  min-width: 50vh;

  .content {
    margin: 2em 2em;

    .content-heard {
      display: flex;
      flex-wrap: wrap; // 允许换行
      justify-content: space-between; // 左右分布
      align-items: center; // 垂直居中
      gap: 1em; // 添加元素间距

      .heard-left,
      .heard-right {
        // 确保两侧内容可以响应式换行
        min-width: 0; // 允许子元素收缩
      }

      .heard-right .heard-right-svg {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 32px;
        height: 32px;
        cursor: pointer;
        background-color: $bg-right-svg-color;
        border-radius: 15%;
      }

      // 响应式处理 - 在小屏幕上垂直排列
      @media (max-width: 768px) {
        flex-direction: column;
        align-items: flex-start;

        .heard-left,
        .heard-right {
          width: 100%;
          margin-bottom: 0.5em;
        }

        .heard-right {
          display: flex;
          justify-content: flex-end;
        }
      }
    }

    .content-text {
      //首行缩进
      text-indent: 2em;
    }

    .content-picture {
      height: 45vh;
      background-color: $bg-right-image-color;
      border-radius: 2vh;
      position: relative;

      .picture {
        height: 100%;
      }

      .picture-overlay {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        display: flex;
        opacity: 0; /* 默认隐藏 */
        pointer-events: none; /* 避免挡住下面的组件点击 */
        transition: opacity 0.3s ease; /* 渐变效果 */

        .overlay-right-top {
          position: absolute;
          top: 1em;
          right: 1em;
        }

        .overlay-bottom {
          position: absolute;
          bottom: 1em;
          left: 1em;
        }
      }

      &:hover .picture-overlay {
        opacity: 1; /* 悬停时显示 */
      }

      .picture-overlay > * {
        pointer-events: auto; /* 只有子元素可以点 */
      }
    }
  }
}
</style>
