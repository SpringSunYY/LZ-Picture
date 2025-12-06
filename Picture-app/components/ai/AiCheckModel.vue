<template>
  <view class="action-buttons" ref="inputContainerRef">
    <view class="left-buttons">
      <!-- 图片生成类型下拉 -->
      <view class="dropdown-wrapper" ref="imageGenDropdownRef">
        <view class="action-button dropdown-toggle" @tap.stop="toggleDropdown('imageGen')">
          <zui-svg-icon icon="image" class="icon" />
          <text>{{ selectedImageOption?.dictLabel || '文生图' }}</text>
          <zui-svg-icon
            icon="right"
            class="chevron-icon"
            :class="{ rotated: showImageDropdown }"
          />
        </view>
        <view v-if="showImageDropdown" class="dropdown-menu">
          <view
            v-for="option in (ai_model_params_type.value || [])"
            :key="option.dictValue"
            class="dropdown-item"
            :class="{ 'is-selected': selectedImageOption?.dictValue === option.dictValue }"
            @tap.stop="selectOption('imageGen', option)"
          >
            <text>{{ option.dictLabel }}</text>
          </view>
        </view>
      </view>

      <!-- 模型选择下拉 -->
      <view class="dropdown-wrapper" ref="imageModelDropdownRef">
        <view
          class="action-button dropdown-toggle model-button"
          @tap.stop="toggleDropdown('imageModel')"
        >
          <zui-svg-icon icon="image" class="icon" />
          <view class="selected-models-container">
            <template v-if="selectedModelOptions && selectedModelOptions.length > 0">
              <view
                v-for="model in selectedModelOptions"
                :key="model.modelKey"
                class="selected-model-tag"
                @tap.stop="toggleModelSelection(model)"
              >
                <text>{{ model.modelLabel }}</text>
                <zui-svg-icon icon="close" class="model-remove-icon" />
              </view>
            </template>
            <text v-else class="placeholder-text">选择模型</text>
          </view>
          <zui-svg-icon
            icon="right"
            class="chevron-icon"
            :class="{ rotated: showModelDropdown }"
          />
        </view>
        <view v-if="showModelDropdown" class="dropdown-menu">
          <view
            v-for="option in modelList"
            :key="option.modelKey"
            class="dropdown-item"
            :class="{
              'is-selected': selectedModelOptions.find((item) => item.modelKey === option.modelKey),
            }"
            @tap.stop="toggleModelSelection(option)"
          >
            <view class="model-item-content">
              <text class="model-label">{{ option.modelLabel }}</text>
              <text class="model-info">所需{{ option.pointsNeed }}积分</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 图片比例下拉 -->
      <view class="dropdown-wrapper" ref="imageRatioDropdownRef">
        <view class="action-button dropdown-toggle" @tap.stop="toggleDropdown('imageRatio')">
          <text>{{ selectedRatioDisplay }}</text>
          <zui-svg-icon
            icon="right"
            class="chevron-icon"
            :class="{ rotated: showRatioDropdown }"
          />
        </view>
        <view v-if="showRatioDropdown" class="dropdown-menu ratio-dropdown-menu">
          <view
            v-for="option in imageRatioOptions"
            :key="option.label"
            class="dropdown-item"
            :class="{ 'is-selected': selectedRatioOption.label === option.label }"
            @tap.stop="selectRatio(option)"
          >
            <text>{{ option.label }}</text>
            <text class="ratio-value">{{ option.width }}x{{ option.height }}</text>
          </view>
          <view class="custom-ratio-item">
            <text class="custom-label">自定义:</text>
            <view class="custom-ratio-inputs">
              <input
                type="number"
                :value="customWidth"
                @input="handleCustomWidthInput"
                :min="256"
                :max="3024"
                placeholder="宽"
                @tap.stop
                class="custom-input"
              />
              <text class="x-separator">×</text>
              <input
                type="number"
                :value="customHeight"
                @input="handleCustomHeightInput"
                :min="256"
                :max="3024"
                placeholder="高"
                @tap.stop
                class="custom-input"
              />
            </view>
          </view>
        </view>
      </view>

      <!-- 生成数量输入 -->
      <view class="dropdown-wrapper">
        <input
          type="number"
          :value="numbers"
          @input="handleNumbersInput"
          :min="1"
          :max="9"
          @blur="validateInput"
          class="action-button number-input"
          placeholder="请输入生成数量"
        />
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch, getCurrentInstance } from 'vue'
import { useDict } from '@/utils/useDict'
import { listModel } from '@/api/ai/model'
import ZuiSvgIcon from '@/uni_modules/zui-svg-icon/components/zui-svg-icon/zui-svg-icon.vue'

const { proxy } = getCurrentInstance()

const { ai_model_params_type } = useDict('ai_model_params_type')

const props = defineProps({
  modelValue: {
    type: Object,
    default: null,
  },
})

const numbers = ref(1)

const modelList = ref([])
const modelQuery = ref({
  modelType: null,
})

const imageGenDropdownRef = ref(null)
const imageModelDropdownRef = ref(null)
const imageRatioDropdownRef = ref(null)
const inputContainerRef = ref(null)

const showImageDropdown = ref(false)
const showModelDropdown = ref(false)
const showRatioDropdown = ref(false)

const selectedImageOption = ref({
  dictValue: '1',
  dictLabel: '文生图',
})

const selectedModelOptions = ref([])

const selectedRatioOption = ref({
  label: '9:16 标清 1K',
  width: 682,
  height: 1024,
})

const customWidth = ref(1024)
const customHeight = ref(1792)

const selectedRatioDisplay = computed(() => {
  return `${selectedRatioOption.value.label} (${selectedRatioOption.value.width}x${selectedRatioOption.value.height})`
})

const imageRatioOptions = [
  { label: '1:1  标清 1K', width: 1024, height: 1024 },
  { label: '4:3  标清 1K', width: 1024, height: 768 },
  { label: '3:4  标清 1K', width: 768, height: 1024 },
  { label: '16:9 标清 1K', width: 1024, height: 682 },
  { label: '9:16 标清 1K', width: 682, height: 1024 },
  { label: '1:1  高清 2K', width: 2048, height: 2048 },
  { label: '4:3  高清 2K', width: 2304, height: 1728 },
  { label: '3:4  高清 2K', width: 1536, height: 2048 },
  { label: '16:9 高清 2K', width: 2560, height: 1440 },
  { label: '9:16 高清 2K', width: 1440, height: 2560 },
  { label: '21:9 高清 2K', width: 3024, height: 1296 },
]

const closeAllDropdowns = () => {
  showImageDropdown.value = false
  showModelDropdown.value = false
  showRatioDropdown.value = false
}

const toggleDropdown = (dropdownName) => {
  if (dropdownName !== 'imageGen') showImageDropdown.value = false
  if (dropdownName !== 'imageModel') showModelDropdown.value = false
  if (dropdownName !== 'imageRatio') showRatioDropdown.value = false

  if (dropdownName === 'imageGen') {
    showImageDropdown.value = !showImageDropdown.value
  } else if (dropdownName === 'imageModel') {
    showModelDropdown.value = !showModelDropdown.value
  } else if (dropdownName === 'imageRatio') {
    showRatioDropdown.value = !showRatioDropdown.value
  }
}

const selectOption = (dropdownName, option) => {
  if (dropdownName === 'imageGen') {
    selectedImageOption.value = option
  }
  closeAllDropdowns()
  modelQuery.value.modelType = option.dictValue
  selectedModelOptions.value = []
  getModelList()
  resetModel()
}

const toggleModelSelection = (model) => {
  const index = selectedModelOptions.value.findIndex(
    (item) => item.modelKey === model.modelKey,
  )
  if (index > -1) {
    selectedModelOptions.value.splice(index, 1)
  } else {
    selectedModelOptions.value.push(model)
  }
  resetModel()
}

const selectRatio = (option) => {
  selectedRatioOption.value = option
  if (option.label !== '自定义') {
    closeAllDropdowns()
  }
  resetModel()
}

const handleCustomWidthInput = (e) => {
  const value = parseInt(e.detail.value) || 0
  customWidth.value = Math.max(256, Math.min(3024, value))
  validateCustomInputs()
}

const handleCustomHeightInput = (e) => {
  const value = parseInt(e.detail.value) || 0
  customHeight.value = Math.max(256, Math.min(3024, value))
  validateCustomInputs()
}

const validateCustomInputs = () => {
  if (customWidth.value >= 256 && customHeight.value >= 256) {
    selectedRatioOption.value = {
      label: '自定义',
      width: customWidth.value,
      height: customHeight.value,
    }
    resetModel()
  }
}

const handleNumbersInput = (e) => {
  const value = parseInt(e.detail.value) || 1
  numbers.value = Math.max(1, Math.min(9, value))
  resetModel()
}

const validateInput = (e) => {
  const target = e.target
  if (!target) return

  let value = target.value
  if (value === '') return

  value = value.replace(/[^\d]/g, '')

  const numValue = Number(value)
  const min = target.min !== '' ? Number(target.min) : 1
  const max = target.max !== '' ? Number(target.max) : 9

  let newValue = numValue
  if (numValue > max) newValue = max
  if (numValue < min) newValue = min

  if (newValue !== numValue || value !== String(numValue)) {
    target.value = String(newValue)
    numbers.value = newValue
    resetModel()
  }
}

const handleClickOutside = (event) => {
  // #ifdef H5
  const target = event.target

  const isClickInsideContainer =
    inputContainerRef.value && inputContainerRef.value.$el && inputContainerRef.value.$el.contains(target)

  if (!isClickInsideContainer) {
    closeAllDropdowns()
    return
  }

  const isClickInsideDropdown =
    (imageGenDropdownRef.value && imageGenDropdownRef.value.$el && imageGenDropdownRef.value.$el.contains(target)) ||
    (imageModelDropdownRef.value && imageModelDropdownRef.value.$el && imageModelDropdownRef.value.$el.contains(target)) ||
    (imageRatioDropdownRef.value && imageRatioDropdownRef.value.$el && imageRatioDropdownRef.value.$el.contains(target))

  if (!isClickInsideDropdown) {
    closeAllDropdowns()
  }
  // #endif
}

const getModelList = async () => {
  selectedModelOptions.value = []
  try {
    const res = await listModel(modelQuery.value)
    if (res.code === 200) {
      modelList.value = res.data || []
    }
  } catch (error) {
    console.error('获取模型列表失败:', error)
  }
}

onMounted(() => {
  getModelList()
  // #ifdef H5
  if (typeof document !== 'undefined') {
    document.addEventListener('click', handleClickOutside)
  }
  // #endif
})

onUnmounted(() => {
  // #ifdef H5
  if (typeof document !== 'undefined') {
    document.removeEventListener('click', handleClickOutside)
  }
  // #endif
})

watch([customWidth, customHeight], () => {
  if (customWidth.value >= 256 && customHeight.value >= 256) {
    selectedRatioOption.value = {
      label: '自定义',
      width: customWidth.value,
      height: customHeight.value,
    }
    resetModel()
  }
})

watch(
  () => props.modelValue,
  async (newVal) => {
    if (!newVal) return

    numbers.value = newVal.numbers ?? 1

    if (newVal.modelType !== modelQuery.value.modelType) {
      modelQuery.value.modelType = newVal.modelType
      await getModelList()
    }

    const typeOption = (ai_model_params_type.value || []).find(
      (item) => item.dictValue === newVal.modelType,
    )
    if (typeOption) {
      selectedImageOption.value = typeOption
    }

    if (newVal.modelKeys && newVal.modelKeys.length) {
      selectedModelOptions.value =
        modelList.value?.filter((m) => newVal.modelKeys.includes(m.modelKey)) || []
    } else {
      selectedModelOptions.value = []
    }

    if (newVal.width && newVal.height) {
      const ratioOption = imageRatioOptions.find(
        (r) => r.width === newVal.width && r.height === newVal.height,
      )
      if (ratioOption) {
        selectedRatioOption.value = ratioOption
      } else {
        selectedRatioOption.value = {
          label: '自定义',
          width: newVal.width,
          height: newVal.height,
        }
        customWidth.value = newVal.width
        customHeight.value = newVal.height
      }
    }

    resetModel()
  },
  { deep: true },
)

const emit = defineEmits(['update:modelValue'])

const modelInfo = ref({
  modelType: selectedImageOption.value?.dictValue || '1',
  modelKeys: selectedModelOptions.value.map((model) => model.modelKey),
  width: selectedRatioOption.value.width,
  height: selectedRatioOption.value.height,
  pointsNeed: 0,
})

const resetModel = () => {
  const modelKeys = []
  let pointsNeed = 0

  selectedModelOptions.value.forEach((model) => {
    modelKeys.push(model.modelKey)
    pointsNeed += Number(model.pointsNeed || 0)
  })

  modelInfo.value = {
    modelType: selectedImageOption.value?.dictValue || '1',
    modelKeys: modelKeys,
    width: selectedRatioOption.value.width,
    height: selectedRatioOption.value.height,
    numbers: numbers.value,
    pointsNeed: pointsNeed,
  }

  emit('update:modelValue', modelInfo.value)
}
</script>

<style lang="scss" scoped>
.action-buttons {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  gap: 16rpx;
  padding-top: 16rpx;
  width: 100%;

  .left-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 16rpx;
    flex-grow: 1;
    width: 100%;
  }

  .dropdown-wrapper {
    position: relative;
    min-width: 0;
    box-sizing: border-box;

    .action-button {
      width: auto;
      flex-shrink: 1;
    }
  }

  .action-button {
    display: flex;
    align-items: center;
    gap: 12rpx;
    padding: 16rpx 24rpx;
    border-radius: 40rpx;
    background-color: rgba(255, 255, 255, 0.15);
    color: #fff;
    font-size: 28rpx;
    border: none;
    transition: background-color 0.2s ease;
    white-space: nowrap;
    min-width: 0;
    box-sizing: border-box;

    &.model-button {
      align-items: flex-start;
      height: auto;
      white-space: normal;
      flex-grow: 1;
    }

    &.number-input {
      width: 240rpx;
      text-align: center;
    }

    &:active {
      background-color: rgba(255, 255, 255, 0.25);
    }

    .icon {
      width: 36rpx;
      height: 36rpx;
      flex-shrink: 0;
    }

    .chevron-icon {
      width: 36rpx;
      height: 36rpx;
      margin-left: auto;
      transition: transform 0.3s ease;
      flex-shrink: 0;

      &.rotated {
        transform: rotate(90deg);
      }
    }

    .selected-models-container {
      display: flex;
      align-items: flex-start;
      flex-wrap: wrap;
      gap: 8rpx;
      flex-grow: 1;
      padding: 0 8rpx;
      align-content: flex-start;
      flex-basis: 0;
    }

    .selected-model-tag {
      flex-shrink: 0;
      display: flex;
      align-items: center;
      gap: 8rpx;
      background-color: rgba(255, 255, 255, 0.25);
      padding: 4rpx 12rpx;
      border-radius: 24rpx;
      font-size: 24rpx;
      white-space: nowrap;

      .model-remove-icon {
        font-size: 24rpx;
        color: rgba(255, 255, 255, 0.7);
      }
    }

    .placeholder-text {
      padding-left: 12rpx;
    }
  }

  .dropdown-menu {
    position: absolute;
    bottom: 100%;
    left: 0;
    margin-bottom: 16rpx;
    background-color: #444;
    border-radius: 16rpx;
    padding: 16rpx 0;
    min-width: 280rpx;
    max-height: 440rpx;
    overflow-y: auto;
    box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.3);
    z-index: 10;
    transform-origin: bottom;

    &.ratio-dropdown-menu {
      .dropdown-item {
        justify-content: space-between;

        .ratio-value {
          font-size: 24rpx;
          color: rgba(255, 255, 255, 0.7);
          margin-left: 20rpx;
        }
      }
    }

    .dropdown-item {
      padding: 16rpx 32rpx;
      color: #fff;
      font-size: 28rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      transition: background-color 0.2s;

      &:active {
        background-color: #555;
      }

      &.is-selected {
        background-color: #666;
      }

      .model-item-content {
        display: flex;
        flex-direction: column;
        gap: 4rpx;
        flex: 1;

        .model-label {
          font-size: 28rpx;
        }

        .model-info {
          font-size: 24rpx;
          color: rgba(255, 255, 255, 0.7);
        }
      }
    }

    .custom-ratio-item {
      padding: 16rpx 32rpx;
      display: flex;
      flex-direction: column;
      gap: 16rpx;

      .custom-label {
        font-size: 28rpx;
        color: #ddd;
      }

      .custom-ratio-inputs {
        display: flex;
        gap: 16rpx;
        align-items: center;

        .custom-input {
          width: 160rpx;
          height: 64rpx;
          background-color: #555;
          border: 2rpx solid #777;
          color: #fff;
          border-radius: 12rpx;
          padding: 0 16rpx;
          font-size: 28rpx;
          text-align: center;
        }

        .x-separator {
          color: #bbb;
        }
      }
    }
  }
}

// 手机端样式
@media (max-width: 768px) {
  .action-buttons {
    .action-button {
      padding: 12rpx 20rpx;
      font-size: 26rpx;
      gap: 8rpx;

      .icon,
      .chevron-icon {
        width: 32rpx;
        height: 32rpx;
      }
    }
  }
}
</style>
