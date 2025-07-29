<template>
  <div class="relative">
    <div
      ref="textContainer"
      class="whitespace-normal"
      :style="containerDynamicStyle"
    >
      {{ displayContent }}
    </div>
    <button
      v-if="needsButton"
      @click="toggleExpand"
      class="mt-2 text-sm text-blue-600 hover:underline flex items-center gap-1"
    >
      {{ isExpanded ? '收起' : '展开' }}
      <component :is="isExpanded ? ChevronUp : ChevronDown" class="w-4 h-4" />
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick, watch } from 'vue';
import { ChevronDown, ChevronUp } from 'lucide-vue-next'; // 导入 Lucide 图标

interface Props {
  /** 要显示的完整文本内容。 */
  text: string;
  /** 可选：文本在截断前显示的最大行数。 */
  maxLines?: number;
  /** 可选：文本在截断前显示的最大字符数。 */
  maxChars?: number;
}

const props = defineProps<Props>();

const isExpanded = ref(false); // 控制文本是否展开的状态
const textContainer = ref<HTMLElement | null>(null); // 引用文本容器DOM元素
const needsButton = ref(false); // 控制“展开/收起”按钮是否显示
const displayContent = ref(''); // 实际显示在DOM中的文本内容（完整或截断）

// 用于缓存计算出的行高，避免重复计算
let cachedLineHeight: number | null = null;

// --- 字符截断逻辑 (保持不变，优先级高于行数截断) ---
const shouldTruncateByChars = computed(() => {
  return props.maxChars !== undefined && props.text.length > props.maxChars;
});

const truncatedTextByChars = computed(() => {
  if (shouldTruncateByChars.value) {
    return props.text.substring(0, props.maxChars!) + '...';
  }
  return props.text;
});

// --- 纯 JS 行数截断逻辑 ---

/**
 * 计算文本容器的单行行高。
 * 通过创建一个临时元素并测量其高度来获取。
 */
const calculateLineHeight = async (): Promise<number> => {
  if (cachedLineHeight !== null) return cachedLineHeight;
  if (!textContainer.value) return 0;

  // 保存原始样式和内容
  const originalText = textContainer.value.textContent;
  const originalStyle = textContainer.value.style.cssText;

  // 临时设置样式以测量单行高度
  textContainer.value.textContent = 'A'; // 任意单行文本
  textContainer.value.style.height = 'auto';
  textContainer.value.style.overflow = 'visible';
  textContainer.value.style.whiteSpace = 'nowrap'; // 确保是单行

  await nextTick(); // 等待DOM更新
  const lh = textContainer.value.offsetHeight;

  // 恢复原始样式和内容
  textContainer.value.textContent = originalText;
  textContainer.value.style.cssText = originalStyle;
  await nextTick(); // 等待DOM恢复

  cachedLineHeight = lh;
  return lh;
};

/**
 * 执行纯 JavaScript 的行数截断。
 * 使用二分查找找到最适合指定行数的文本长度。
 */
const performLineTruncation = async () => {
  if (!textContainer.value || props.maxLines === undefined) {
    displayContent.value = props.text;
    needsButton.value = false;
    return;
  }

  const lineHeight = await calculateLineHeight();
  if (lineHeight === 0) { // 如果无法测量行高，则不截断
    displayContent.value = props.text;
    needsButton.value = false;
    return;
  }

  const maxAllowedHeight = props.maxLines * lineHeight;

  // 1. 临时渲染完整文本以测量其真实滚动高度
  const originalStyle = textContainer.value.style.cssText; // 保存当前样式
  textContainer.value.textContent = props.text;
  textContainer.value.style.maxHeight = ''; // 清除任何最大高度限制
  textContainer.value.style.overflow = 'visible'; // 确保所有内容都可测量
  textContainer.value.style.whiteSpace = 'normal'; // 允许文本换行
  await nextTick(); // 等待DOM更新

  const fullTextHeight = textContainer.value.scrollHeight;

  // 2. 判断是否需要截断
  if (fullTextHeight <= maxAllowedHeight + 1) { // 增加1px容差，避免浮点数误差
    displayContent.value = props.text;
    needsButton.value = false;
  } else {
    needsButton.value = true; // 文本溢出，需要显示按钮
    if (!isExpanded.value) {
      // 3. 如果未展开，执行二分查找进行截断
      let currentText = props.text;
      let low = 0;
      let high = currentText.length;
      let bestFitIndex = 0; // 记录能适应的最大文本长度

      while (low <= high) {
        const mid = Math.floor((low + high) / 2);
        const testText = currentText.substring(0, mid);
        textContainer.value.textContent = testText + '...'; // 临时设置文本并添加省略号进行测量
        await nextTick(); // 等待DOM更新

        if (textContainer.value.scrollHeight <= maxAllowedHeight) {
          bestFitIndex = mid; // 当前文本长度可以适应，尝试更长的
          low = mid + 1;
        } else {
          high = mid - 1; // 当前文本长度过长，尝试更短的
        }
      }

      // 确保最终截断的文本长度不会超过原始文本，并且如果截断了就添加省略号
      let finalTruncatedText = currentText.substring(0, bestFitIndex);
      if (finalTruncatedText.length < currentText.length) {
          finalTruncatedText += '...';
      }
      displayContent.value = finalTruncatedText;

    } else {
      // 如果已展开，显示完整文本
      displayContent.value = props.text;
    }
  }

  // 恢复原始样式，因为 displayContent 会在下次渲染时更新 textContent
  textContainer.value.style.cssText = originalStyle;
  await nextTick();
};

// --- 主逻辑流：根据 props 更新内容并检查是否需要按钮 ---
const updateContentAndCheckTruncation = async () => {
  if (!textContainer.value) {
    // 如果DOM元素还未挂载，等待下次tick
    await nextTick();
    if (!textContainer.value) return; // 再次检查，防止意外
  }

  if (shouldTruncateByChars.value) {
    // 字符截断优先级最高
    needsButton.value = true;
    displayContent.value = isExpanded.value ? props.text : truncatedTextByChars.value;
  } else if (props.maxLines !== undefined) {
    // 如果设置了行数限制，执行纯 JS 行数截断
    await performLineTruncation();
  } else {
    // 没有字符限制也没有行数限制，显示完整文本，不需要按钮
    displayContent.value = props.text;
    needsButton.value = false;
  }
};

// 切换展开/收起状态
const toggleExpand = () => {
  isExpanded.value = !isExpanded.value;
  updateContentAndCheckTruncation(); // 状态改变后重新评估内容
};

// 组件挂载时执行一次检查
onMounted(() => {
  updateContentAndCheckTruncation();
  // 添加 ResizeObserver 监听容器宽度变化，以便在布局改变时重新计算截断
  if (textContainer.value) {
    const resizeObserver = new ResizeObserver(() => {
      cachedLineHeight = null; // 容器大小变化，行高可能变化，重置缓存
      updateContentAndCheckTruncation();
    });
    resizeObserver.observe(textContainer.value);
    // 注意：在实际项目中，您可能需要一个 onUnmounted 钩子来断开观察者
    // onUnmounted(() => resizeObserver.disconnect());
  }
});

// 监听 props 变化，重置状态并重新计算
watch([() => props.text, () => props.maxLines, () => props.maxChars], () => {
  isExpanded.value = false; // 内容或限制变化时，默认收起
  cachedLineHeight = null; // 重置行高缓存
  updateContentAndCheckTruncation();
}, { immediate: true }); // 立即执行一次监听器，确保初始状态正确

// 容器的动态样式，主要用于确保文本正常换行
const containerDynamicStyle = computed(() => {
  // 在纯JS截断模式下，我们不依赖CSS的max-height或overflow:hidden来截断
  // 而是通过直接设置displayContent来控制文本内容
  // 这里的样式主要是为了确保文本正常布局
  return {
    // 确保文本正常换行，而不是单行显示
    whiteSpace: 'normal',
    // 如果需要，可以添加 overflow: hidden; 来隐藏可能出现的滚动条，
    // 但理论上JS截断后不应该有溢出
    // overflow: 'hidden',
  };
});
</script>
