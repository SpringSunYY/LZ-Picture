<template>
  <u-modal
    v-model="openPublic"
    :show-cancel-button="false"
    :show-confirm-button="false"
    width="700"
    :close-on-click-overlay="false"
  >
    <view class="custom-modal">
      <view class="custom-modal-title">
        <text class="title-icon">🚀</text>
        <text class="title-text">发布图片</text>
        <text class="title-tip">?</text>
      </view>

      <view class="form-container">
        <!-- 图片名称 -->
        <view class="form-item">
          <view class="form-label">
            <text>图片名称</text>
            <text class="required">*</text>
          </view>
          <input
            v-model="pictureInfo.name"
            class="form-input"
            placeholder="请输入图片名称"
            :maxlength="32"
          />
          <view class="char-count">{{ pictureInfo.name.length }}/32</view>
        </view>

        <!-- 图片分类 -->
        <view class="form-item">
          <view class="form-label">
            <text>图片分类</text>
            <text class="required">*</text>
          </view>
          <picker
            mode="selector"
            :range="categoryOptions"
            range-key="label"
            @change="handleCategoryChange"
          >
            <view class="picker-view">
              <text v-if="selectedCategory" class="picker-text">{{ selectedCategory.label }}</text>
              <text v-else class="picker-placeholder">请选择图片分类</text>
              <text class="picker-arrow">›</text>
            </view>
          </picker>
        </view>

        <!-- 图片空间 -->
        <view class="form-item">
          <view class="form-label">
            <text>图片空间</text>
            <text class="required">*</text>
          </view>
          <picker
            mode="selector"
            :range="spaceOptions"
            range-key="label"
            @change="handleSpaceChange"
          >
            <view class="picker-view">
              <text v-if="selectedSpace" class="picker-text">{{ selectedSpace.label }}</text>
              <text v-else class="picker-placeholder">请选择图片空间</text>
              <text class="picker-arrow">›</text>
            </view>
          </picker>
        </view>

        <!-- 图片文件夹 -->
        <view class="form-item">
          <view class="form-label">
            <text>图片文件夹</text>
          </view>
          <picker
            mode="selector"
            :range="folderOptions"
            range-key="label"
            @change="handleFolderChange"
          >
            <view class="picker-view">
              <text v-if="selectedFolder" class="picker-text">{{ selectedFolder.label }}</text>
              <text v-else class="picker-placeholder">请选择图片文件夹</text>
              <text class="picker-arrow">›</text>
            </view>
          </picker>
        </view>

        <!-- 标签 -->
        <view class="form-item">
          <view class="form-label">
            <text>标签</text>
          </view>
          <view class="tags-container">
            <view
              v-for="(tag, index) in pictureInfo.tags"
              :key="index"
              class="tag-item"
              @tap="removeTag(index)"
            >
              <text>{{ tag }}</text>
              <text class="tag-remove">×</text>
            </view>
            <input
              v-if="pictureInfo.tags.length < 5"
              v-model="tagInput"
              class="tag-input"
              placeholder="输入标签后回车"
              @confirm="addTag"
              :maxlength="9"
            />
          </view>
        </view>

        <!-- 图片简介 -->
        <view class="form-item">
          <view class="form-label">
            <text>图片简介</text>
          </view>
          <textarea
            v-model="pictureInfo.introduction"
            class="form-textarea"
            placeholder="请输入图片简介"
            :maxlength="512"
            :auto-height="true"
          />
          <view class="char-count">{{ pictureInfo.introduction.length }}/512</view>
        </view>

        <!-- 图片状态和积分 -->
        <view class="form-row">
          <view class="form-item form-item-half">
            <view class="form-label">
              <text>图片状态</text>
              <text class="required">*</text>
            </view>
            <radio-group @change="handleStatusChange">
              <label
                v-for="dict in p_picture_status"
                :key="dict.dictValue"
                class="radio-label"
              >
                <radio :value="dict.dictValue" :checked="pictureInfo.pictureStatus === dict.dictValue" />
                <text>{{ dict.dictLabel }}</text>
              </label>
            </radio-group>
          </view>

          <view class="form-item form-item-half">
            <view class="form-label">
              <text>积分</text>
            </view>
            <input
              v-model.number="pictureInfo.pointsNeed"
              type="number"
              class="form-input number-input"
              placeholder="请输入所需积分"
            />
          </view>
        </view>

        <!-- 按钮 -->
        <view class="form-footer">
          <view class="footer-button cancel-button" @tap="openPublic = false">
            <text>取消</text>
          </view>
          <view class="footer-button submit-button" @tap="handleSubmitPicture" :class="{ loading: pictureLoading }">
            <text v-if="!pictureLoading">提交</text>
            <text v-else>提交中...</text>
          </view>
        </view>
      </view>
    </view>
  </u-modal>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { toast } from '@/utils/toast'
import { useStore } from 'vuex'
import { useDict } from '@/utils/useDict'

// 这里需要导入 API 函数，根据实际项目调整
// import { mySpaceInfo } from '@/api/picture/space.ts'
// import { listPictureTagInfo } from '@/api/picture/pictureTag.ts'
// import { listSpaceFolderInfo } from '@/api/picture/spaceFolder.ts'
// import { listPictureCategoryInfo } from '@/api/picture/pictureCategory'
// import { addPictureInfoByAi } from '@/api/picture/picture.ts'
// import { handleTree } from '@/utils/lz.ts'

const props = defineProps({
  initialItem: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['success'])

const store = useStore()

// 使用字典工具函数
const { p_picture_status } = useDict('p_picture_status')

const openPublic = ref(false)
const pictureInfo = ref({
  name: '',
  introduction: '',
  categoryId: '',
  spaceId: '',
  folderId: '',
  tags: [],
  pictureStatus: '0',
  logId: '',
  pointsNeed: 0,
})

const tagInput = ref('')

// 选项数据
const categoryOptions = ref([])
const spaceOptions = ref([])
const folderOptions = ref([])

const selectedCategory = ref(null)
const selectedSpace = ref(null)
const selectedFolder = ref(null)

onMounted(() => {
  // 字典已自动加载，直接使用即可
})

watch(
  () => props.initialItem,
  (newItem) => {
    if (newItem) {
      pictureInfo.value = {
        introduction: newItem.prompt,
        tags: [newItem.modelName],
        pictureStatus: '0',
        logId: newItem.logId,
        pointsNeed: 0,
        spaceId: '',
        folderId: '',
        categoryId: '',
        name: '',
      }
      // getMySpaceList()
      // getTagList()
      // getPictureCategoryList()
    }
  },
  { immediate: true },
)

const handleCategoryChange = (e) => {
  const index = e.detail.value
  selectedCategory.value = categoryOptions.value[index]
  pictureInfo.value.categoryId = selectedCategory.value.value
}

const handleSpaceChange = (e) => {
  const index = e.detail.value
  selectedSpace.value = spaceOptions.value[index]
  pictureInfo.value.spaceId = selectedSpace.value.value
  // getFolderList()
}

const handleFolderChange = (e) => {
  const index = e.detail.value
  selectedFolder.value = folderOptions.value[index]
  pictureInfo.value.folderId = selectedFolder.value.value
}

const handleStatusChange = (e) => {
  pictureInfo.value.pictureStatus = e.detail.value
}

const addTag = () => {
  const tag = tagInput.value.trim()
  if (!tag) return

  if (tag.length > 9) {
    toast('标签名称不能超过9个字符')
    return
  }

  if (pictureInfo.value.tags.length >= 5) {
    toast('最多只能选择5个标签')
    return
  }

  if (!pictureInfo.value.tags.includes(tag)) {
    pictureInfo.value.tags.push(tag)
  }

  tagInput.value = ''
}

const removeTag = (index) => {
  pictureInfo.value.tags.splice(index, 1)
}

const pictureLoading = ref(false)

const handleSubmitPicture = async () => {
  // 验证
  if (!pictureInfo.value.name) {
    toast('请输入图片名称')
    return
  }

  if (!pictureInfo.value.categoryId) {
    toast('请选择图片分类')
    return
  }

  if (!pictureInfo.value.spaceId) {
    toast('请选择图片空间')
    return
  }

  // 积分校验
  if (pictureInfo.value.pointsNeed && pictureInfo.value.pointsNeed > 0) {
    if (pictureInfo.value.pointsNeed % 10 !== 0) {
      toast('消耗积分必须为0或者10的倍数')
      return
    }
  }

  pictureLoading.value = true

  try {
    // const res = await addPictureInfoByAi(pictureInfo.value)
    // if (res.code === 200) {
    //   toast('发布成功，如果是公开请等待审核')
    //   openPublic.value = false
    //   emit('success')
    // }
    
    // 临时模拟成功
    toast('发布成功，如果是公开请等待审核')
    openPublic.value = false
    emit('success')
  } catch (e) {
    toast('发布失败')
  } finally {
    pictureLoading.value = false
  }
}

defineExpose({
  openModal: () => {
    openPublic.value = true
  },
})
</script>

<style lang="scss" scoped>
.custom-modal {
  padding: 40rpx;
  max-height: 80vh;
  overflow-y: auto;
}

.custom-modal-title {
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
  font-size: 36rpx;
  font-weight: bold;

  .title-icon {
    margin-right: 16rpx;
    font-size: 40rpx;
  }

  .title-text {
    color: #1890ff;
  }

  .title-tip {
    margin-left: 16rpx;
    color: #999;
    font-size: 28rpx;
  }
}

.form-container {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 16rpx;

  &.form-item-half {
    flex: 1;
  }
}

.form-row {
  display: flex;
  gap: 32rpx;
}

.form-label {
  display: flex;
  align-items: center;
  font-size: 28rpx;
  color: #333;

  .required {
    color: #ff4d4f;
    margin-left: 4rpx;
  }
}

.form-input {
  padding: 20rpx 24rpx;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 28rpx;
  background-color: #fff;

  &.number-input {
    text-align: center;
  }
}

.form-textarea {
  padding: 20rpx 24rpx;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 28rpx;
  min-height: 160rpx;
  background-color: #fff;
}

.char-count {
  text-align: right;
  font-size: 24rpx;
  color: #999;
}

.picker-view {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 24rpx;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  background-color: #fff;

  .picker-text {
    font-size: 28rpx;
    color: #333;
  }

  .picker-placeholder {
    font-size: 28rpx;
    color: #999;
  }

  .picker-arrow {
    font-size: 32rpx;
    color: #999;
  }
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  align-items: center;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
  padding: 12rpx 20rpx;
  background-color: #f0f0f0;
  border-radius: 8rpx;
  font-size: 24rpx;

  .tag-remove {
    color: #999;
    font-size: 32rpx;
    font-weight: bold;
  }
}

.tag-input {
  flex: 1;
  min-width: 200rpx;
  padding: 12rpx 20rpx;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 24rpx;
  background-color: #fff;
}

.radio-label {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-right: 32rpx;
  font-size: 28rpx;
}

.form-footer {
  display: flex;
  justify-content: flex-end;
  gap: 24rpx;
  margin-top: 40rpx;
  padding-top: 40rpx;
  border-top: 2rpx solid #eee;
}

.footer-button {
  padding: 20rpx 48rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
  text-align: center;

  &.cancel-button {
    background-color: #f5f5f5;
    color: #333;
  }

  &.submit-button {
    background-color: #1890ff;
    color: #fff;

    &.loading {
      opacity: 0.6;
    }
  }

  &:active {
    opacity: 0.8;
  }
}
</style>

