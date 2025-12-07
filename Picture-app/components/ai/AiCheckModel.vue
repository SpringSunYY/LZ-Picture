<template>
  <view class="action-buttons" ref="inputContainerRef">
    <view class="left-buttons">
      <!-- 模型选择下拉 -->
      <view class="dropdown-wrapper" ref="imageModelDropdownRef">
        <view
            class="action-button dropdown-toggle model-button"
            @tap.stop="toggleDropdown('imageModel')"
        >
          <view class="selected-models-container">
            <template v-if="selectedModelOptions && selectedModelOptions.length > 0">
              <view
                  v-for="model in selectedModelOptions"
                  :key="model.modelKey"
                  class="selected-model-tag"
                  @tap.stop="toggleModelSelection(model)"
              >
                <text>{{ model.modelLabel }}</text>
                <view class="model-remove-icon" @tap.stop="toggleModelSelection(model)">
                  <!-- #ifdef H5 -->
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                    <path d="M18 6L6 18M6 6l12 12" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                          stroke-linejoin="round"/>
                  </svg>
                  <!-- #endif -->
                  <!-- #ifndef H5 -->
                  <text class="close-text">×</text>
                  <!-- #endif -->
                </view>
              </view>
            </template>
            <text v-else class="placeholder-text">选择模型</text>
          </view>
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
      <view class="dropdown-wrapper number-input-wrapper">
        <view class="number-input-controls">
          <view class="number-btn decrease-btn" @tap="decreaseNumber" :class="{ disabled: numbers <= 1 }">
            <text>−</text>
          </view>
          <input
              type="number"
              :value="numbers"
              @input="handleNumbersInput"
              :min="1"
              :max="9"
              @blur="validateInput"
              class="action-button number-input"
              placeholder="数量"
          />
          <view class="number-btn increase-btn" @tap="increaseNumber" :class="{ disabled: numbers >= 9 }">
            <text>+</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import {computed, onMounted, onUnmounted, ref, watch} from 'vue'
import {listModel} from '@/api/ai/model'

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

const imageModelDropdownRef = ref(null)
const imageRatioDropdownRef = ref(null)
const inputContainerRef = ref(null)

const showModelDropdown = ref(false)
const showRatioDropdown = ref(false)

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
  {label: '1:1  标清 1K', width: 1024, height: 1024},
  {label: '4:3  标清 1K', width: 1024, height: 768},
  {label: '3:4  标清 1K', width: 768, height: 1024},
  {label: '16:9 标清 1K', width: 1024, height: 682},
  {label: '9:16 标清 1K', width: 682, height: 1024},
  {label: '1:1  高清 2K', width: 2048, height: 2048},
  {label: '4:3  高清 2K', width: 2304, height: 1728},
  {label: '3:4  高清 2K', width: 1536, height: 2048},
  {label: '16:9 高清 2K', width: 2560, height: 1440},
  {label: '9:16 高清 2K', width: 1440, height: 2560},
  {label: '21:9 高清 2K', width: 3024, height: 1296},
]

const closeAllDropdowns = () => {
  showModelDropdown.value = false
  showRatioDropdown.value = false
}

const toggleDropdown = (dropdownName) => {
  if (dropdownName !== 'imageModel') showModelDropdown.value = false
  if (dropdownName !== 'imageRatio') showRatioDropdown.value = false

  if (dropdownName === 'imageModel') {
    showModelDropdown.value = !showModelDropdown.value
  } else if (dropdownName === 'imageRatio') {
    showRatioDropdown.value = !showRatioDropdown.value
  }
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

const decreaseNumber = () => {
  if (numbers.value > 1) {
    numbers.value--
    resetModel()
  }
}

const increaseNumber = () => {
  if (numbers.value < 9) {
    numbers.value++
    resetModel()
  }
}

const validateInput = (e) => {
  // UniApp 中 @blur 事件可能传递的是 detail.value 而不是 e.target.value
  let value = e.detail?.value ?? e.target?.value

  // 如果 value 为 undefined、null 或空字符串，使用当前 numbers 值
  if (value === undefined || value === null || value === '') {
    value = String(numbers.value || 1)
  } else {
    value = String(value)
  }

  // 移除非数字字符
  value = value.replace(/[^\d]/g, '')

  // 如果移除后为空，使用默认值 1
  if (value === '') {
    value = '1'
  }

  const numValue = Number(value)
  const min = 1
  const max = 9

  let newValue = numValue
  if (numValue > max) newValue = max
  if (numValue < min) newValue = min

  if (newValue !== numbers.value) {
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

// 防止递归更新的标志
const isUpdatingFromProps = ref(false)

watch(
    () => props.modelValue,
    async (newVal, oldVal) => {
      if (!newVal) return

      // 防止递归更新：如果正在从内部更新，则跳过
      if (isUpdatingFromProps.value) return

      // 深度比较，如果值没有实际变化，则不更新
      if (oldVal && JSON.stringify(newVal) === JSON.stringify(oldVal)) return

      isUpdatingFromProps.value = true

      numbers.value = newVal.numbers ?? 1

      if (newVal.modelType !== modelQuery.value.modelType) {
        modelQuery.value.modelType = newVal.modelType
        await getModelList()
      }

      if (newVal.modelKeys && newVal.modelKeys.length) {
        const newModelKeys = newVal.modelKeys || []
        const currentModelKeys = selectedModelOptions.value.map(m => m.modelKey)
        if (JSON.stringify(newModelKeys.sort()) !== JSON.stringify(currentModelKeys.sort())) {
          selectedModelOptions.value =
              modelList.value?.filter((m) => newVal.modelKeys.includes(m.modelKey)) || []
        }
      } else {
        if (selectedModelOptions.value.length > 0) {
          selectedModelOptions.value = []
        }
      }

      if (newVal.width && newVal.height) {
        const ratioOption = imageRatioOptions.find(
            (r) => r.width === newVal.width && r.height === newVal.height,
        )
        if (ratioOption) {
          if (selectedRatioOption.value.width !== ratioOption.width ||
              selectedRatioOption.value.height !== ratioOption.height) {
            selectedRatioOption.value = ratioOption
          }
        } else {
          if (selectedRatioOption.value.width !== newVal.width ||
              selectedRatioOption.value.height !== newVal.height) {
            selectedRatioOption.value = {
              label: '自定义',
              width: newVal.width,
              height: newVal.height,
            }
            customWidth.value = newVal.width
            customHeight.value = newVal.height
          }
        }
      }

      isUpdatingFromProps.value = false
    },
    {deep: true},
)

const emit = defineEmits(['update:modelValue'])

const modelInfo = ref({
  modelType: props.modelValue?.modelType || '1',
  modelKeys: selectedModelOptions.value.map((model) => model.modelKey),
  width: selectedRatioOption.value.width,
  height: selectedRatioOption.value.height,
  numbers: numbers.value,
  pointsNeed: 0,
})

const resetModel = () => {
  // 如果正在从 props 更新，则不触发 emit
  if (isUpdatingFromProps.value) return

  const modelKeys = []
  let pointsNeed = 0

  selectedModelOptions.value.forEach((model) => {
    modelKeys.push(model.modelKey)
    pointsNeed += Number(model.pointsNeed || 0)
  })

  const newModelInfo = {
    modelType: props.modelValue?.modelType || modelQuery.value.modelType || '1',
    modelKeys: modelKeys,
    width: selectedRatioOption.value.width,
    height: selectedRatioOption.value.height,
    numbers: numbers.value,
    pointsNeed: pointsNeed,
  }

  // 只有当值真正变化时才 emit
  const oldModelInfo = modelInfo.value
  if (JSON.stringify(newModelInfo) !== JSON.stringify(oldModelInfo)) {
    modelInfo.value = newModelInfo
    emit('update:modelValue', modelInfo.value)
  }
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

    &.number-input-wrapper {
      .number-input-controls {
        display: flex;
        align-items: center;
        gap: 0;
        background-color: rgba(255, 255, 255, 0.15);
        border-radius: 40rpx;
        padding: 0;
        overflow: hidden;

        .number-btn {
          width: 64rpx;
          height: 64rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          background-color: rgba(255, 255, 255, 0.1);
          color: #fff;
          font-size: 36rpx;
          font-weight: bold;
          transition: background-color 0.2s;
          flex-shrink: 0;

          &.decrease-btn {
            border-radius: 40rpx 0 0 40rpx;
          }

          &.increase-btn {
            border-radius: 0 40rpx 40rpx 0;
          }

          &:active:not(.disabled) {
            background-color: rgba(255, 255, 255, 0.25);
          }

          &.disabled {
            opacity: 0.4;
            color: rgba(255, 255, 255, 0.5);
          }

          text {
            line-height: 1;
          }
        }

        .number-input {
          border: none;
          background: transparent;
          padding: 16rpx 8rpx;
          margin: 0;
          min-width: 80rpx;
          width: auto;
          flex: 1;
          text-align: center;
          font-size: 28rpx;
          color: #fff;
          height: 64rpx;
          line-height: 64rpx;
          box-sizing: border-box;

          // #ifdef H5
          min-width: 60px;
          width: 60px;
          height: 32px;
          line-height: 32px;
          // #endif
        }
      }
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
      width: auto;
      text-align: center;
      flex: 1;
      min-width: 80rpx;
      font-size: 28rpx;
      height: 64rpx;
      line-height: 64rpx;
      box-sizing: border-box;

      // #ifdef H5
      min-width: 60px;
      width: 60px;
      height: 32px;
      line-height: 32px;
      // #endif
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
        width: 32rpx;
        height: 32rpx;
        flex-shrink: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        color: rgba(255, 255, 255, 0.7);
        cursor: pointer;
        transition: color 0.2s;

        &:active {
          color: rgba(255, 255, 255, 1);
        }

        svg {
          width: 100%;
          height: 100%;
        }

        .close-text {
          font-size: 32rpx;
          line-height: 1;
          font-weight: bold;
        }
      }
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
</style>
