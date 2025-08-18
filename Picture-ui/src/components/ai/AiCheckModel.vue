<template>
  <div class="action-buttons">
    <div class="left-buttons">
      <div class="dropdown-wrapper" ref="imageGenDropdownRef">
        <button class="action-button dropdown-toggle" @click.stop="toggleDropdown('imageGen')">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="lucide lucide-image"
          >
            <rect width="18" height="18" x="3" y="3" rx="2" ry="2" />
            <circle cx="9" cy="9" r="2" />
            <path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21" />
          </svg>
          <span>{{ selectedImageOption.dictLabel }}</span>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="lucide lucide-chevron-down"
            :class="{ rotated: showImageDropdown }"
          >
            <path d="m6 9 6 6 6-6" />
          </svg>
        </button>
        <ul v-if="showImageDropdown" class="dropdown-menu">
          <li
            v-for="option in ai_model_params_type"
            :key="option.dictValue"
            :class="{ 'is-selected': selectedImageOption === option.dictValue }"
            @click.stop="selectOption('imageGen', option)"
          >
            {{ option.dictLabel }}
          </li>
        </ul>
      </div>
      <div class="dropdown-wrapper" ref="imageModelDropdownRef">
        <button
          class="action-button dropdown-toggle model-button"
          @click.stop="toggleDropdown('imageModel')"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="lucide lucide-image"
          >
            <rect width="18" height="18" x="3" y="3" rx="2" ry="2" />
            <circle cx="9" cy="9" r="2" />
            <path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21" />
          </svg>
          <div class="selected-models-container">
            <template v-if="selectedModelOptions.length > 0">
              <span
                v-for="model in selectedModelOptions"
                :key="model.modelKey"
                class="selected-model-tag"
                @click.stop="toggleModelSelection(model)"
              >
                {{ model.modelLabel }}
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="12"
                  height="12"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="3"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="lucide lucide-x model-remove-icon"
                >
                  <path d="M18 6 6 18" />
                  <path d="m6 6 12 12" />
                </svg>
              </span>
            </template>
            <span v-else class="placeholder-text">选择模型</span>
          </div>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="lucide lucide-chevron-down"
            :class="{ rotated: showModelDropdown }"
          >
            <path d="m6 9 6 6 6-6" />
          </svg>
        </button>
        <ul v-if="showModelDropdown" class="dropdown-menu">
          <li
            v-for="option in modelList"
            :key="option.modelKey"
            :class="{
              'is-selected': selectedModelOptions.find((item) => item.modelKey === option.modelKey),
            }"
            @click.stop="toggleModelSelection(option)"
            :title="`${option.modelDescription}+\n\n所需${option.pointsNeed}积分`"
          >
            {{ option.modelLabel }}
          </li>
        </ul>
      </div>
      <div class="dropdown-wrapper" ref="imageRatioDropdownRef">
        <button class="action-button dropdown-toggle" @click.stop="toggleDropdown('imageRatio')">
          <span>{{ selectedRatioDisplay }}</span>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="lucide lucide-chevron-down"
            :class="{ rotated: showRatioDropdown }"
          >
            <path d="m6 9 6 6 6-6" />
          </svg>
        </button>
        <ul v-if="showRatioDropdown" class="dropdown-menu ratio-dropdown-menu">
          <li
            v-for="option in imageRatioOptions"
            :key="option.label"
            :class="{ 'is-selected': selectedRatioOption.label === option.label }"
            @click.stop="selectRatio(option)"
          >
            <span>{{ option.label }}</span>
            <span class="ratio-value">{{ option.width }}x{{ option.height }}</span>
          </li>
          <li
            class="custom-ratio-item"
            @click.stop="selectRatio({ label: '自定义', width: customWidth, height: customHeight })"
          >
            <label>自定义:</label>
            <div class="custom-ratio-inputs">
              <input
                type="number"
                v-model.number="customWidth"
                min="256"
                max="3024"
                placeholder="宽"
                @click.stop
                @input="validateInput"
                @blur="validateInput"
              />
              <span class="x-separator">×</span>
              <input
                type="number"
                v-model.number="customHeight"
                min="256"
                max="3024"
                placeholder="高"
                @click.stop
                @input="validateInput"
                @blur="validateInput"
              />
            </div>
          </li>
        </ul>
      </div>
      <div class="dropdown-wrapper">
        <input
          type="number"
          v-model.number="numbers"
          :min="1"
          :max="9"
          @input="resetModel"
          @blur="validateInput"
          @click.stop
          class="action-button"
          style="width: 120px"
          placeholder="请输入生成数量"
        />
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup name="AiCheckModel">
import { computed, getCurrentInstance, onMounted, onUnmounted, ref, watch } from 'vue'
import type { ModelParamsInfo, ModelParamsInfoRequest, ModelInfo } from '@/types/ai/model'
import { listModel } from '@/api/ai/model.ts'
import type { Dict } from '@/types/common'

const { proxy } = getCurrentInstance()!
const { ai_model_params_type } = proxy?.useDict('ai_model_params_type')

interface ImageRatioOption {
  label: string
  width?: number
  height?: number
}

// 添加props接收modelValue
const props = defineProps<{
  modelValue?: ModelInfo
}>()
const numbers = ref(1)
//region 模型信息
const modelList = ref<ModelParamsInfo[]>()
const modelQuery = ref<ModelParamsInfoRequest>({
  modelType: null,
})

const isMobile = ref(false)

const checkIsMobile = () => {
  isMobile.value = window.innerWidth < 768
}

const inputContainerRef = ref<HTMLElement | null>(null)

// 新增下拉菜单的引用
const imageGenDropdownRef = ref<HTMLElement | null>(null)
const imageModelDropdownRef = ref<HTMLElement | null>(null)
const imageRatioDropdownRef = ref<HTMLElement | null>(null)

const showImageDropdown = ref(false)
const showModelDropdown = ref(false)
const showRatioDropdown = ref(false)

const selectedImageOption = ref<Dict>({
  dictValue: '1',
  dictLabel: '文生图',
})
const selectedModelOptions = ref<ModelParamsInfo[]>([])
const selectedRatioOption = ref<ImageRatioOption>({
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

const toggleDropdown = (dropdownName: 'imageGen' | 'imageModel' | 'imageRatio') => {
  if (dropdownName !== 'imageGen') showImageDropdown.value = false
  if (dropdownName !== 'imageModel') showModelDropdown.value = false
  if (dropdownName !== 'imageRatio') showRatioDropdown.value = false
  if (dropdownName === 'imageGen') showImageDropdown.value = !showImageDropdown.value
  else if (dropdownName === 'imageModel') showModelDropdown.value = !showModelDropdown.value
  else if (dropdownName === 'imageRatio') showRatioDropdown.value = !showRatioDropdown.value
}

const selectOption = (dropdownName: 'imageGen', option: Dict) => {
  if (dropdownName === 'imageGen') selectedImageOption.value = option
  closeAllDropdowns()
  modelQuery.value.modelType = option.dictValue
  selectedModelOptions.value = []
  getModelList()
  resetModel()
}

const toggleModelSelection = (model: ModelParamsInfo) => {
  const index = selectedModelOptions.value.findIndex(
    (item: ModelParamsInfo) => item.modelKey === model.modelKey,
  )
  if (index > -1) {
    selectedModelOptions.value.splice(index, 1)
  } else {
    selectedModelOptions.value.push(model)
  }
  resetModel()
}

const selectRatio = (option: ImageRatioOption) => {
  selectedRatioOption.value = option
  if (option.label !== '自定义') {
    closeAllDropdowns()
  }
  resetModel()
}
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as Node

  // 检查点击事件是否发生在 inputContainerRef 内部
  const isClickInsideContainer = inputContainerRef.value && inputContainerRef.value.contains(target)

  // 如果点击发生在外部，并且目前是展开状态，则收起输入框并关闭所有下拉菜单
  if (!isClickInsideContainer) {
    closeAllDropdowns()
  } else {
    // 如果点击在容器内部，但不是在下拉菜单按钮或菜单内容上，也应该关闭下拉菜单
    const isClickInsideDropdown =
      (imageGenDropdownRef.value && imageGenDropdownRef.value.contains(target)) ||
      (imageModelDropdownRef.value && imageModelDropdownRef.value.contains(target)) ||
      (imageRatioDropdownRef.value && imageRatioDropdownRef.value.contains(target))

    if (!isClickInsideDropdown) {
      closeAllDropdowns()
    }
  }
}
const getModelList = async () => {
  selectedModelOptions.value = []
  const res = await listModel(modelQuery.value)
  modelList.value = res.data
}
getModelList()
onMounted(() => {
  checkIsMobile()
  window.addEventListener('resize', checkIsMobile)
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkIsMobile)
  document.removeEventListener('click', handleClickOutside)
})

watch([customWidth, customHeight], () => {
  selectedRatioOption.value = {
    label: '自定义',
    width: customWidth.value,
    height: customHeight.value,
  }
  resetModel()
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
    const typeOption = ai_model_params_type.value.find(
      (item) => item.dictValue === newVal.modelType,
    )
    if (typeOption) selectedImageOption.value = typeOption
    console.log(ai_model_params_type)
    if (newVal.modelKeys?.length) {
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
        selectedRatioOption.value = { label: '自定义', width: newVal.width, height: newVal.height }
        customWidth.value = newVal.width
        customHeight.value = newVal.height
      }
    }
    const modelKeys: string[] = []
    let pointsNeed = 0
    selectedModelOptions.value.map((model) => {
      modelKeys.push(model.modelKey)
      pointsNeed += Number(model.pointsNeed)
    })
    modelInfo.value = {
      modelType: selectedImageOption.value.dictValue,
      modelKeys: modelKeys,
      width: selectedRatioOption.value.width,
      height: selectedRatioOption.value.height,
      numbers: numbers.value,
      pointsNeed: pointsNeed,
    }
  },
  { deep: true },
)

const emit = defineEmits(['update:modelValue'])
const modelInfo = ref<ModelInfo>({
  modelType: selectedImageOption.value.dictValue,
  modelKeys: selectedModelOptions.value.map((model) => model.modelKey),
  width: selectedRatioOption.value.width,
  height: selectedRatioOption.value.height,
  pointsNeed: 0,
})
const resetModel = () => {
  const modelKeys: string[] = []
  let pointsNeed = 0
  selectedModelOptions.value.map((model) => {
    modelKeys.push(model.modelKey)
    pointsNeed += Number(model.pointsNeed)
  })
  modelInfo.value = {
    modelType: selectedImageOption.value.dictValue,
    modelKeys: modelKeys,
    width: selectedRatioOption.value.width,
    height: selectedRatioOption.value.height,
    numbers: numbers.value,
    pointsNeed: pointsNeed,
  }
  //数据返回给父组件，使用v-model可以直接绑定
  emit('update:modelValue', modelInfo.value)
  console.log('modelInfo:', modelInfo.value)
}

// 添加以下方法来处理输入验证
const validateInput = (event: Event, strict = false) => {
  const target = event.target as HTMLInputElement
  // 只保留 0-9 及可选的小数点
  target.value = target.value.replace(/[^\d.]/g, '')

  if (target.value === '') return

  const value = Number(target.value)
  const min = target.min !== '' ? Number(target.min) : 64
  const max = target.max !== '' ? Number(target.max) : 4096

  let newValue = value
  if (value > max) newValue = max
  if (value < min) newValue = min

  if (strict || newValue !== value) {
    target.value = String(newValue)
    target.dispatchEvent(new Event('input', { bubbles: true }))
  }
}
</script>
<style lang="scss" scoped>
.action-buttons {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  gap: 8px;
  padding-top: 8px;
  width: 100%;

  .left-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
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
    gap: 6px;
    padding: 8px 12px;
    border-radius: 20px;
    background-color: rgba(255, 255, 255, 0.15);
    color: #fff;
    font-size: 14px;
    border: none;
    cursor: pointer;
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

    &:hover {
      background-color: rgba(255, 255, 255, 0.25);
    }

    svg {
      flex-shrink: 0;
      width: 18px;
      height: 18px;
      color: #fff;
    }

    &.dropdown-toggle svg:last-child {
      margin-left: auto;
      transition: transform 0.3s ease;

      &.rotated {
        transform: rotate(180deg);
      }
    }

    .selected-models-container {
      display: flex;
      align-items: flex-start;
      flex-wrap: wrap;
      gap: 4px;
      flex-grow: 1;
      padding: 0 4px;
      align-content: flex-start;
      flex-basis: 0;
    }

    .selected-model-tag {
      flex-shrink: 0;
      display: flex;
      align-items: center;
      gap: 4px;
      background-color: rgba(255, 255, 255, 0.25);
      padding: 2px 6px;
      border-radius: 12px;
      font-size: 12px;
      white-space: nowrap;

      .model-remove-icon {
        cursor: pointer;
        width: 10px;
        height: 10px;
        color: rgba(255, 255, 255, 0.7);

        &:hover {
          color: #fff;
        }
      }
    }

    .placeholder-text {
      padding-left: 6px;
    }
  }

  .dropdown-menu {
    position: absolute;
    bottom: 100%;
    left: 0;
    margin-bottom: 8px;
    background-color: #444;
    border-radius: 8px;
    padding: 8px 0;
    list-style: none;
    min-width: 140px;
    max-height: 220px;
    overflow-y: auto;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
    z-index: 10;
    transform-origin: bottom;

    &.ratio-dropdown-menu {
      li {
        justify-content: space-between;

        .ratio-value {
          font-size: 12px;
          color: rgba(255, 255, 255, 0.7);
          margin-left: 10px;
        }
      }
    }

    li {
      padding: 8px 16px;
      cursor: pointer;
      color: #fff;
      font-size: 14px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      transition: background-color 0.2s;

      &:hover {
        background-color: #555;
      }

      &.is-selected {
        background-color: #666;
      }
    }

    .custom-ratio-item {
      padding: 8px 16px;
      display: flex;
      flex-direction: column;
      gap: 8px;

      label {
        font-size: 14px;
        color: #ddd;
      }

      .custom-ratio-inputs {
        display: flex;
        gap: 8px;
        align-items: center;

        input {
          width: 80px;
          height: 32px;
          background-color: #555;
          border: 1px solid #777;
          color: #fff;
          border-radius: 6px;
          padding: 0 8px;
          font-size: 14px;
          -moz-appearance: textfield;

          &::-webkit-outer-spin-button,
          &::-webkit-inner-spin-button {
            -webkit-appearance: none;
            margin: 0;
          }

          &::placeholder {
            color: #bbb;
          }
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
      padding: 6px 10px;
      font-size: 13px;
      gap: 4px;

      svg {
        width: 16px;
        height: 16px;
      }
    }
  }
}

@media (max-width: 480px) {
  .action-buttons {
    .action-button {
      padding: 6px 10px;
      font-size: 12px;
      gap: 4px;

      svg {
        width: 14px;
        height: 14px;
      }
    }
  }
}

input[type='number'] {
  -moz-appearance: textfield;

  &::-webkit-outer-spin-button,
  &::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
}
</style>
