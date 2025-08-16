<template>
  <div class="ai-generate">
    <div class="main-content-area">
      <main class="gallery-scroll-area" @scroll="handleScroll" ref="scrollContainer">
        <div class="main-content-wrapper">
          <div v-if="generateGroups.length === 0" class="empty-state">
            <div :key="'create'" class="create-content" @click="openAiInput = !openAiInput">
              <h3 class="create-title">开始创作</h3>
              <div class="create-options" style="width: 40vh">
                <button class="create-option-card">
                  <span class="card-icon">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path
                        d="M12 20V4M4 12H20"
                        stroke="currentColor"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      />
                    </svg>
                  </span>
                  <span class="card-text">从零开始</span>
                </button>
              </div>
              <p class="create-prompt-text">创作你的下一张图片。</p>
            </div>
          </div>
          <div v-else>
            <div v-for="group in generateGroups" :key="group.date" class="image-group">
              <h2 class="group-date">{{ group.date }}</h2>
              <div class="image-grid">
                <template v-for="item in group.items" :key="item.logId">
                  <div
                    v-if="item.logStatus && item.logStatus === AiLogStatusEnum.SUCCESS"
                    class="image-card"
                    :class="{ selected: isImageSelected(item, item.logId) }"
                    @click="handleImageSelect(item, item.logId)"
                  >
                    <img :src="item.fileUrls" :alt="item.prompt" class="generated-image" />
                    <div class="image-card-overlay">
                      <div class="overlay-text">{{ item.prompt }}</div>
                      <div class="overlay-actions">
                        <button
                          @click.stop="handlePublic(item)"
                          v-if="item.hasPublic === AiGenerateHasPublicEnum.HAS_PUBLIC_1"
                          class="overlay-button"
                        >
                          发布作品
                        </button>
                      </div>
                    </div>
                  </div>
                  <div v-else>
                    <AiLoading class="generated-loading" />
                  </div>
                </template>
              </div>
            </div>
          </div>

          <div v-if="isLoadingMore" class="loading-more">
            <svg class="loading-spinner" viewBox="0 0 50 50">
              <circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
            </svg>
            <p>加载中...</p>
          </div>
          <NoMoreData
            v-else-if="noMore && generateGroups.length > 0"
            text="没有更多数据了哦，快去生成吧！！！"
          />
        </div>
      </main>
    </div>

    <aside class="sidebar">
      <div class="sidebar-panel">
        <transition name="fade-and-slide" mode="out-in">
          <div v-if="generateInfo" :key="'detail'" class="image-detail-content">
            <div class="detail-header">
              <h3 class="detail-title">图片详情</h3>
              <button @click="clearSelection" class="close-button">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M6 18L18 6M6 6L18 18"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                </svg>
              </button>
            </div>
            <AiPictureView style="height: 50vh" :image-url="generateInfoSrc" />
            <a-space align="center" direction="horizontal" :wrap="true">
              <h1 class="text-xl font-bold text-white px-0.5">
                {{
                  ai_model_params_type.find((item) => item.dictValue === generateInfo.modelType)
                    .dictLabel
                }}
              </h1>
              <div class="text-white">
                {{ generateInfo.modelName }}
              </div>
              <div class="text-white">{{ generateInfo.width }}x{{ generateInfo.height }}</div>
            </a-space>
            <div class="detail-info">
              <h4 class="info-title mt">提示词</h4>
              <TextView :max-lines="3" :text="generateInfo.prompt" class="prompt-text" />
            </div>
            <div class="detail-actions">
              <GenerateButton
                @click="handleReGenerate(generateInfo, generateInfo.logId)"
                class="action-button"
              />
              <ReferToButton
                @click="handleReferTo(generateInfo, generateInfo.logId)"
                class="action-button"
              />
              <DownloadButton @click="openByUrl(generateInfo.fileUrls)" class="action-button" />
            </div>
          </div>
          <div v-else :key="'create'" class="create-content" @click="openAiInput = !openAiInput">
            <h3 class="create-title">开始创作</h3>
            <div class="create-options">
              <button class="create-option-card">
                <span class="card-icon">
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path
                      d="M12 20V4M4 12H20"
                      stroke="currentColor"
                      stroke-width="2"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                    />
                  </svg>
                </span>
                <span class="card-text">从零开始</span>
              </button>
            </div>
            <p class="create-prompt-text">创作你的下一张图片。</p>
          </div>
        </transition>
      </div>
    </aside>
    <AiInput
      @success="generateSuccess"
      v-show="openAiInput"
      :file-info="fileInfo"
      :model-info="modelInfo"
      :prompt="prompt"
    />

    <a-modal v-model:open="openPublic" :width="700" destroy-on-close :footer="null">
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          发布图片
          <a-tooltip
            title="您可以设置图片为公开，这样其他用户也可以查看到您的图片哦，如果设置为私有就只可以自己查看哦，同时也可以设置积分哦。"
          >
            <question-circle-outlined class="title-tip-icon" />
          </a-tooltip>
        </div>
      </template>
      <a-form
        :model="pictureInfo"
        @finish="handleSubmitPicture"
        :label-col="{ span: 6 }"
        :rules="rules"
        :wrapper-col="{ span: 16 }"
      >
        <a-row justify="center">
          <a-col :span="24">
            <!-- 图片名称 -->
            <a-form-item label="图片名称" name="name">
              <a-input
                v-model:value="pictureInfo.name"
                showCount
                :maxlength="32"
                allowClear
                placeholder="请输入图片名称"
              />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <!-- 分类选择 -->
            <a-form-item label="图片分类" name="categoryId">
              <a-cascader
                v-model:value="pictureInfo.categoryId"
                :options="pictureCategoryList"
                expand-trigger="hover"
                placeholder="请选择图片分类"
                :fieldNames="{
                  label: 'name',
                  value: 'categoryId',
                  children: 'children',
                }"
              />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="图片空间" name="spaceId">
              <a-select
                show-search
                v-model:value="pictureInfo.spaceId"
                :options="spaceList"
                :filter-option="false"
                :fieldNames="{
                  label: 'spaceName',
                  value: 'spaceId',
                }"
                @search="handleSearchSpace"
                @select="handleSelectSpace"
                placeholder="请选择图片空间"
                :not-found-content="spaceLoading"
              />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="图片文件夹">
              <a-cascader
                v-model:value="pictureInfo.folderId"
                :options="folderList"
                placeholder="请选择图片文件夹"
                change-on-select
                :fieldNames="{
                  label: 'folderName',
                  value: 'folderId',
                  children: 'children',
                }"
              />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="标签">
              <a-select
                mode="tags"
                v-model:value="pictureInfo.tags"
                :options="tagList"
                placeholder="请输入图片标签"
                :filter-option="false"
                :fieldNames="{
                  label: 'name',
                  value: 'name',
                }"
                @search="handleSearchTag"
                @select="handleSelectTag"
                :not-found-content="tagLoading"
              />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <!-- 图片描述 -->
            <a-form-item label="图片简介" name="introduction">
              <a-textarea
                v-model:value="pictureInfo.introduction"
                :rows="4"
                show-count
                :maxlength="512"
                placeholder="请输入图片简介"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item :label-col="{ span: 10 }" name="pictureStatus">
              <template #label>
                <span style="display: inline-flex; align-items: center">
                  图片状态
                  <a-tooltip
                    title="公共：所有的用户可见，别的用户使用您的图片生成，或者使用您的图片简介作为参考，查看您的原图，您将获得积分奖励。&#10;私有：仅自己或者团队可见"
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
              <a-radio-group v-model:value="pictureInfo.pictureStatus" name="radioGroup">
                <a-radio
                  v-for="dict in p_picture_status"
                  :value="dict.dictValue"
                  :key="dict.dictValue"
                >
                  {{ dict.dictLabel }}
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item name="pointsNeed">
              <template #label>
                <span style="display: inline-flex; align-items: center">
                  积分
                  <a-tooltip
                    title="如果您是公开可以设置积分，可以为0表示查看您原图的时候无需积分，反之为需要积分，积分必须为0或10的倍数"
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
              <a-input-number
                v-model:value="pictureInfo.pointsNeed"
                :min="0"
                :step="10"
                style="width: 90%"
                placeholder="请输入所需积分"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <div class="form-footer">
          <a-button @click="openPublic = false">取消</a-button>
          <a-button type="primary" html-type="submit" :loading="pictureLoading">提交</a-button>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, nextTick, onMounted, onUnmounted, ref } from 'vue'
import ReferToButton from '@/components/button/ReferToButton.vue'
import DownloadButton from '@/components/button/DownloadButton.vue'
import GenerateButton from '@/components/button/GenerateButton.vue'
import AiInput from '@/components/AiInput.vue'
import AiPictureView from '@/components/AiPictureView.vue'
import {
  AiGenerateHasPublicEnum,
  AiLogStatusEnum,
  defaultModelInfo,
  type GenerateLogInfoQuery,
  type GenerateLogInfoVo,
  type ModelInfo,
} from '@/types/ai/model.d.ts'
import { listGenerateLogInfo, queryTask } from '@/api/ai/model.ts'
import { formatDateTime } from '@/utils/common.ts'
import NoMoreData from '@/components/NoMoreData.vue'
import TextView from '@/components/TextView.vue'
import AiLoading from '@/components/AiLoading.vue'
import { openByUrl } from '@/utils/file.ts'
import { message } from 'ant-design-vue'
import type { PictureAiUpload } from '@/types/picture/picture'
import { InfoCircleOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue'
import type { PictureTagInfoQuery, PictureTagInfoVo } from '@/types/picture/pictureTag'
import { mySpaceInfo } from '@/api/picture/space.ts'
import type { Space, SpaceQuery } from '@/types/picture/space'
import { listPictureTagInfo } from '@/api/picture/pictureTag.ts'
import type { SpaceFolderInfoQuery, SpaceFolderInfoVo } from '@/types/picture/spaceFolder'
import { listSpaceFolderInfo } from '@/api/picture/spaceFolder.ts'
import { handleTree } from '@/utils/lz.ts'
import { debounce } from 'lodash-es'
import { listPictureCategoryInfoByAi } from '@/api/picture/pictureCategory.ts'
import type { PictureCategoryInfoVo } from '@/types/picture/pictureCategory'
import { addPictureInfoByAi } from '@/api/picture/picture.ts'
import { useRouter } from 'vue-router'

const { proxy } = getCurrentInstance()!
const { ai_model_params_type, p_picture_status } = proxy?.useDict(
  'ai_model_params_type',
  'p_picture_status',
)

interface GalleryGroup {
  date: string
  items: GenerateLogInfoVo[]
}

//region 图片列表
const generateGroups = ref<GalleryGroup[]>([])
const generateQuery = ref<GenerateLogInfoQuery>({
  pageNum: 1,
  pageSize: 15,
})
const isLoadingMore = ref(false)
const noMore = ref(false)
const getGenerateList = async () => {
  if (isLoadingMore.value || noMore.value) return
  isLoadingMore.value = true
  await listGenerateLogInfo(generateQuery.value).then((res) => {
    if (!generateGroups.value) {
      generateGroups.value = []
    }
    if (res.rows && res.rows.length > 0) {
      res.rows.forEach((item: GenerateLogInfoVo) => {
        const date = formatDateTime(item.createTime)
        //只要年月日
        const group = generateGroups.value.find((group) => group.date === date)
        if (group) {
          group.items.push(item)
        } else {
          generateGroups.value.push({
            date,
            items: [item],
          })
        }
        if (item.logStatus === AiLogStatusEnum.REQUESTING) {
          setTimeout(async () => {
            await pollGenerateTask(item)
          }, 5000)
        }
      })
    }
    if (!res.rows || res.rows.length < (generateQuery.value.pageSize || 15)) {
      noMore.value = true
    }
  })
  isLoadingMore.value = false
}
const loadMoreData = () => {
  generateQuery.value.pageNum = 1 + (generateQuery.value.pageNum || 0)
  getGenerateList()
}

// 定义一个定时器引用,每个任务都需要
const pollingMap = new Map<string, NodeJS.Timeout>()
//轮训获取生成结果
const pollGenerateTask = async (item: GenerateLogInfoVo) => {
  const logId = item.logId
  //如果成功，从generateList拿到对应的数据，修改他的数据
  const date = formatDateTime(item.createTime)
  const group = generateGroups.value.find((g) => g.date === date)
  try {
    const res = await queryTask(logId)
    if (res.code === 200 && res.data) {
      //如果成功
      if (res.data.logStatus === AiLogStatusEnum.SUCCESS) {
        stopPolling(logId)
        if (group) {
          group.items = group.items.map((item) => {
            if (item.logId === logId) {
              return {
                ...item,
                fileUrls: res.data?.fileUrls,
                logStatus: res.data?.logStatus,
              }
            }
            return item
          })
        }
      } else if (res.data.logStatus === AiLogStatusEnum.REQUESTING) {
        // 5秒后继续轮询
        const timer = setTimeout(() => pollGenerateTask(item), 5000)
        pollingMap.set(logId, timer)
      } else {
        message.error(
          item.modelName + '生成失败，请检查生成内容是否可能侵犯版权，使用的积分已经返回您的账户',
          5,
        )
        //删除对应的数据
        if (group) {
          group.items = group.items.filter((item) => {
            return item.logId !== logId
          })
        }

        stopPolling(logId)
      }
    }
  } catch (e) {
    stopPolling(logId)
  }
}

// 停止轮询
const stopPolling = (logId: string) => {
  const timer = pollingMap.get(logId)
  if (timer) {
    clearTimeout(timer)
    pollingMap.delete(logId)
  }
}

// const isLoadingMore = ref(false)
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
  getGenerateList()
})

onUnmounted(() => {
  if (scrollContainer.value) {
    scrollContainer.value.removeEventListener('scroll', handleScroll)
  }
})
//endregion
//region 图片详情
const generateInfo = ref<GenerateLogInfoVo | null>(null)
const generateInfoIndex = ref<string | null>(null)

const generateInfoSrc = ref<string>('')
const handleImageSelect = (item: GenerateLogInfoVo, index: string) => {
  if (isImageSelected(item, index)) {
    clearSelection()
    openAiInput.value = false
  } else {
    generateInfo.value = item
    generateInfoIndex.value = index
    generateInfoSrc.value = item.fileUrls
  }
}
const isImageSelected = (item: GenerateLogInfoVo, index: string) => {
  return generateInfo.value?.logId === item.logId && generateInfoIndex.value === index
}
const clearSelection = () => {
  generateInfo.value = null
  generateInfoIndex.value = null
}

const openAiInput = ref(false)
//endregion
//region 创建图片
const modelInfo = ref<ModelInfo>(defaultModelInfo)
const prompt = ref('')
const fileInfo = ref('')
//生成图片
const handleReGenerate = (item: GenerateLogInfoVo, index: string) => {
  generateInfoIndex.value = index
  fileInfo.value = item.fileUrls
  prompt.value = item.prompt
  openAiInput.value = true
  modelInfo.value = {
    modelType: item.modelType,
    modelKeys: [item.modelKey],
    numbers: 1,
    width: item.width,
    height: item.height,
    pointsNeed: item.pointsUsed,
  }
}
//引用图片
const handleReferTo = (item: GenerateLogInfoVo, index: string) => {
  generateInfoIndex.value = index
  fileInfo.value = item.fileUrls
  openAiInput.value = true
}
//图片生成成功后，重新加载图片列表
const generateSuccess = () => {
  generateGroups.value = []
  isLoadingMore.value = false
  noMore.value = false
  generateQuery.value.pageNum = 1
  getGenerateList()
}
//endregion
//region 发布作品
const openPublic = ref(false)
const pictureInfo = ref<PictureAiUpload>({
  name: '',
  introduction: '',
  categoryId: '',
  spaceId: '',
  folderId: '',
  tags: [],
  pictureStatus: '0',
  logId: '',
})
const rules = {
  pictureUrl: [
    {
      required: true,
      message: '请选择图片',
      trigger: 'change',
    },
  ],
  name: [
    {
      required: true,
      message: '请输入图片名称',
      trigger: 'blur',
    },
  ],
  categoryId: [
    {
      required: true,
      message: '请选择图片分类',
      trigger: 'change',
    },
  ],
  spaceId: [
    {
      required: true,
      message: '请选择图片空间',
      trigger: 'change',
    },
  ],
  pictureStatus: [{ required: true, message: '图片状态不能为空' }],
}
//标签
const tagList = ref<PictureTagInfoVo[]>([])
const tagQuery = ref<PictureTagInfoQuery>({})
const tagLoading = ref(false)
const getTagList = () => {
  tagLoading.value = true
  listPictureTagInfo(tagQuery.value).then((res) => {
    tagList.value = res?.rows || []
    tagLoading.value = false
  })
}
const handleSearchSpace = debounce((value: string) => {
  spaceQuery.value.spaceName = value
  getMySpaceList()
}, 300)
const handleSelectTag = (value: string) => {
  //如果标签字符长度大于9个
  if (value.length > 9) {
    //删除选择
    pictureInfo.value.tags = pictureInfo.value.tags?.filter((item) => item !== value)
    message.error('标签名称不能超过9个字符')
    return
  }
  if ((pictureInfo.value.tags?.length ?? 0) > 5) {
    message.error('最多只能选择5个标签')
    pictureInfo.value.tags = pictureInfo.value.tags?.slice(0, 5)
    return
  }
}
//文件夹
const folderList = ref<SpaceFolderInfoVo[]>([])
const folderQuery = ref<SpaceFolderInfoQuery>({
  spaceId: '',
})
const getFolderList = () => {
  // 获取文件夹列表
  listSpaceFolderInfo(folderQuery.value).then((res) => {
    folderList.value = handleTree(
      JSON.parse(JSON.stringify(res?.rows || [])),
      'folderId',
      'parentId',
      'children',
    )
  })
}
//空间
const spaceList = ref<Space[]>([])
const spaceQuery = ref<SpaceQuery>({})
const spaceLoading = ref(false)
const getMySpaceList = () => {
  spaceLoading.value = true
  // 获取我的空间列表
  mySpaceInfo(spaceQuery.value).then((res) => {
    if (res.code === 200) {
      spaceList.value = res?.rows || []
    } else {
      message.error('获取空间列表失败')
    }
    spaceLoading.value = false
  })
}
const handleSelectSpace = () => {
  pictureInfo.value.folderId = ''
  folderQuery.value.spaceId = pictureInfo.value.spaceId
  getFolderList()
}
const handleSearchTag = debounce((value: string) => {
  tagQuery.value.name = value
  getTagList()
}, 300)

//分类
const pictureCategoryList = ref<PictureCategoryInfoVo[]>([])
const getPictureCategoryList = async () => {
  listPictureCategoryInfoByAi().then((res) => {
    pictureCategoryList.value = handleTree(
      JSON.parse(JSON.stringify(res?.rows || [])),
      'categoryId',
      'parentId',
      'children',
    )
    // console.log('pictureCategoryList', pictureCategoryList.value)
  })
}
const handlePublic = (item: GenerateLogInfoVo) => {
  console.log('发布作品', item)
  pictureInfo.value = {
    introduction: item.prompt,
    tags: [item.modelName],
    pictureStatus: '0',
    logId: item.logId,
    pointsNeed: 0,
    spaceId: '',
    folderId: '',
    categoryId: '',
    name: '',
  }
  openPublic.value = true
  getMySpaceList()
  getTagList()
  getPictureCategoryList()
}
const pictureLoading = ref(false)
const handleSubmitPicture = async () => {
  console.log('提交图片', pictureInfo.value)
  let categoryId = null
  if (pictureInfo.value.categoryId && Array.isArray(pictureInfo.value.categoryId)) {
    categoryId = pictureInfo.value.categoryId[pictureInfo.value.categoryId.length - 1]
  }
  let folderId = null
  if (pictureInfo.value.folderId && Array.isArray(pictureInfo.value.folderId)) {
    folderId = pictureInfo.value.folderId[pictureInfo.value.folderId.length - 1]
  }
  pictureInfo.value = {
    ...pictureInfo.value,
    categoryId,
    folderId,
  }
  pictureLoading.value = true
  try {
    const res = await addPictureInfoByAi(pictureInfo.value)
    if (res.code === 200) {
      message.success('发布成功，如果是公开请等待审核')
      openPublic.value = false
      generateQuery.value.pageNum = 1
      generateGroups.value = []
      await getGenerateList()
    }
  } finally {
    pictureLoading.value = false
  }
}
//endregion
</script>

<style scoped lang="scss">
// SCSS 变量，便于维护和颜色调整
$color-bg-primary: #18181b;
$color-bg-secondary: #27272a;
$color-bg-tertiary: #3f3f46;
$color-text-primary: #f4f4f5;
$color-text-secondary: #a1a1aa;
$color-border: #3f3f46;
$color-accent: #6366f1;
$color-accent-hover: color.adjust($color-accent, $lightness: 5%); // 使用SCSS函数处理颜色
$color-hover: #2e2e32;
$color-shadow: rgba(0, 0, 0, 0.4);

// 混入（Mixin）用于可复用的样式
@mixin dark-card-shadow {
  box-shadow: 0 4px 12px $color-shadow;
}

@mixin dark-card-hover {
  transform: scale(1.03);
  box-shadow: 0 8px 24px $color-shadow;
}

// 根容器使用 flex 布局，实现左右自适应
.ai-generate {
  font-family: 'Inter', sans-serif;
  color: $color-text-primary;
  background-color: $color-bg-primary;
  display: flex;
  height: 100vh;
}

.main-content-area {
  flex-grow: 1; // 占据剩余空间
  flex-shrink: 1;
  display: flex;
  flex-direction: column;
  min-width: 0; // 确保在小屏幕上能够正确收缩
  padding-left: 1.5em;
}

.gallery-scroll-area {
  flex-grow: 1;
  overflow-y: auto; // 修复滚动bug，让这个内部容器可以滚动
  -webkit-overflow-scrolling: touch;
}

.main-content-wrapper {
  width: 100%;
  margin: 0 auto;
  padding-top: 16px;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 20vh;
  color: $color-text-secondary;
}

.image-group {
  margin-bottom: 32px;
  padding-right: 10px;
  padding-left: 10px;
}

.group-date {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 24px;
  color: $color-text-primary;
  padding-left: 8px;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
}

.image-card {
  position: relative;
  overflow: hidden;
  border-radius: 12px;
  height: 400px;
  cursor: pointer;
  @include dark-card-shadow;
  border: 2px solid transparent;
  transition:
    transform 0.3s cubic-bezier(0.25, 0.8, 0.25, 1),
    box-shadow 0.3s,
    border-color 0.3s;

  &:hover {
    @include dark-card-hover;
  }

  &.selected {
    border-color: $color-accent;
    @include dark-card-hover;
  }
}

.generated-loading {
  //高度宽度居中
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.generated-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  display: block;
}

.image-card-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent 60%);
  color: $color-text-primary;
  padding: 12px;
  opacity: 0;
  transition: opacity 0.3s ease-in-out;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: flex-start;

  .image-card:hover &,
  .image-card.selected & {
    opacity: 1;
  }
}

.overlay-text {
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

.overlay-actions {
  margin-top: 8px;
}

.overlay-button {
  background-color: $color-accent;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 6px 10px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: $color-accent-hover;
  }
}

.loading-more {
  text-align: center;
  padding: 32px;
  color: $color-text-secondary;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.loading-spinner {
  animation: rotate 2s linear infinite;
  width: 40px;
  height: 40px;

  .path {
    stroke: $color-accent;
    stroke-linecap: round;
    animation: dash 1.5s ease-in-out infinite;
  }
}

@keyframes rotate {
  100% {
    transform: rotate(360deg);
  }
}

@keyframes dash {
  0% {
    stroke-dasharray: 1, 150;
    stroke-dashoffset: 0;
  }
  50% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -35;
  }
  100% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -124;
  }
}

.sidebar {
  flex-basis: 500px; // 侧边栏基础宽度
  flex-shrink: 0; // 阻止侧边栏收缩
  height: 100%;
  background-color: $color-bg-secondary;
  border-left: 1px solid $color-border;
  position: relative;
  overflow-y: auto;

  @media (max-width: 1024px) {
    display: none; // 小屏幕下隐藏
  }
}

.sidebar-panel {
  padding: 24px;
}

.image-detail-content,
.create-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-title,
.create-title {
  font-size: 18px;
  font-weight: 600;
}

.close-button {
  background: none;
  border: none;
  cursor: pointer;
  color: $color-text-secondary;
  transition: color 0.2s;

  &:hover {
    color: $color-text-primary;
  }

  svg {
    width: 24px;
    height: 24px;
  }
}

.detail-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-title {
  font-size: 14px;
  font-weight: 500;
  color: $color-text-secondary;
}

.prompt-text {
  font-size: 14px;
  color: $color-text-primary;
  line-height: 1.6;
  background-color: $color-bg-tertiary;
  padding: 12px;
  border-radius: 8px;
}

.detail-actions {
  display: flex;
  gap: 12px;
}

.action-button {
  flex: 1;
  background-color: $color-bg-tertiary;
  color: $color-text-primary;
  border: 1px solid $color-border;
  border-radius: 8px;
  padding: 12px 16px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: background-color 0.2s;

  &:hover {
    background-color: $color-hover;
  }

  &.primary {
    background-color: $color-accent;
    border-color: $color-accent;

    &:hover {
      background-color: $color-accent-hover;
    }
  }
}

.create-content {
  text-align: center;
  padding-top: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
}

.create-options {
  width: 100%;
}

.create-option-card {
  width: 100%;
  background-color: $color-bg-tertiary;
  color: $color-text-primary;
  border: 1px solid $color-border;
  border-radius: 12px;
  padding: 24px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  transition: all 0.2s ease-in-out;

  &:hover {
    background-color: $color-hover;
    transform: translateY(-2px);
    @include dark-card-shadow;
  }
}

.card-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: $color-accent;
  display: flex;
  justify-content: center;
  align-items: center;

  svg {
    width: 24px;
    height: 24px;
    color: #fff;
  }
}

.create-prompt-text {
  font-size: 14px;
  color: $color-text-secondary;
}

// 过渡动画
.fade-and-slide-enter-active,
.fade-and-slide-leave-active {
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.fade-and-slide-enter-from,
.fade-and-slide-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
