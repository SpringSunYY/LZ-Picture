<template>
  <div class="ranking-table-container" :style="{ height: height, width: width }">
    <table class="ranking-table">
      <thead ref="theadRef">
        <tr>
          <th>序号</th>
          <th v-for="(column, index) in columns" :key="index">{{ column.label }}</th>
        </tr>
      </thead>
      <tbody
        ref="tableBodyRef"
        @mouseenter="handleMouseEnter"
        @mouseleave="handleMouseLeave"
        @wheel.passive="handleWheel"
        @mousemove="handleMouseMove"
      >
        <tr
          v-for="(item, index) in data"
          :key="index"
          @click="handleRowClick(item)"
          @mouseenter="(event)=>handleRowMouseEnter(item,event)"
          @mouseleave="handleRowMouseLeave"
          :class="{ 'is-at-bottom': isAtBottom && index === data.length - 1 }"
        >
          <td>{{ index + 1 }}</td>
          <td v-for="(column, colIndex) in columns" :key="colIndex">
            {{ item[column.prop] ?? '' }}
          </td>
        </tr>
      </tbody>
    </table>

    <div v-show="tooltipContent" class="tooltip" :style="tooltipStyle">
      <div v-for="(value, key) in tooltipContent" :key="key">
        <strong>{{ getLabelByProp(key) }}:</strong> {{ value }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, onUnmounted, ref, watch, reactive } from 'vue';

const tableBodyRef = ref<HTMLElement | null>(null);
const theadRef = ref<HTMLElement | null>(null);
let animationFrameId: number | null = null;
let scrollTimeout: ReturnType<typeof setTimeout> | null = null;
let isHovering: boolean = false;
const isAtBottom = ref<boolean>(false);
const needsScrolling = ref<boolean>(false);

const tooltipContent = ref<Record<string, any> | null>(null);
const tooltipStyle = reactive({
  top: '10px',
  left: '0px',
  opacity: 0
});

const props = defineProps({
  columns: {
    type: Array as () => { label: string; prop: string }[],
    default: () => [
      { label: '图片编号', prop: 'pictureId' },
      { label: '图片名称', prop: 'name' },
      { label: '浏览量', prop: 'lookCount' },
      { label: '收藏量', prop: 'collectCount' },
      { label: '点赞', prop: 'likeCount' },
      { label: '分享', prop: 'shareCount' }
    ]
  },
  data: {
    type: Array as () => Record<string, any>[],
    default: () => [
      { pictureId: '001', name: '图片A', lookCount: 100, collectCount: 50, likeCount: 80, shareCount: 10 },
      { pictureId: '002', name: '图片B', lookCount: 200, collectCount: 70, likeCount: 120, shareCount: 20 },
      { pictureId: '003', name: '图片C', lookCount: 150, collectCount: 60, likeCount: 90, shareCount: 15 },
      { pictureId: '004', name: '图片D', lookCount: 300, collectCount: 90, likeCount: 200, shareCount: 30 },
      { pictureId: '005', name: '图片E', lookCount: 80, collectCount: 40, likeCount: 50, shareCount: 5 },
      { pictureId: '006', name: '图片F', lookCount: 90, collectCount: 45, likeCount: 60, shareCount: 8 },
      { pictureId: '007', name: '图片G', lookCount: 110, collectCount: 55, likeCount: 70, shareCount: 12 }
    ]
  },
  height: { type: String, default: '100%' },
  width: { type: String, default: '100%' },
  scrollInterval: { type: Number, default: 2000 },
  scrollSpeed: { type: Number, default: 1 }
});

const emit = defineEmits(['scrolledToBottom', 'rowClicked']);

// 计算tbody的高度
const tbodyHeight = computed(() => {
  const containerElement = document.querySelector('.ranking-table-container');
  const theadElement = theadRef.value;

  if (!containerElement || !theadElement) {
    return 'calc(100% - 44px)'; // 默认表头高度
  }

  const containerHeight = containerElement.clientHeight;
  const theadHeight = theadElement.offsetHeight;
  return `${containerHeight - theadHeight}px`;
});

const getLabelByProp = (prop: string) => {
  const col = props.columns.find(c => c.prop === prop);
  return col ? col.label : prop;
};

// 检查是否需要滚动
const checkIfScrollingNeeded = () => {
  const tableBody = tableBodyRef.value;
  if (!tableBody) return false;

  // 等待DOM更新完成后再检查
  return tableBody.scrollHeight > tableBody.clientHeight;
};

const stopAllScrolling = () => {
  if (scrollTimeout) {
    clearTimeout(scrollTimeout);
    scrollTimeout = null;
  }
  if (animationFrameId !== null) {
    cancelAnimationFrame(animationFrameId);
    animationFrameId = null;
  }
};

const startAutoScroll = () => {
  // 检查是否需要滚动
  if (!checkIfScrollingNeeded()) {
    needsScrolling.value = false;
    isAtBottom.value = true;
    emit('scrolledToBottom');
    return;
  }

  needsScrolling.value = true;
  stopAllScrolling();

  scrollTimeout = setTimeout(() => {
    const scroll = () => {
      const tableBody = tableBodyRef.value;
      if (!tableBody || isHovering) {
        animationFrameId = null;
        return;
      }

      if (tableBody.scrollTop + tableBody.clientHeight >= tableBody.scrollHeight - 1) {
        isAtBottom.value = true;
        emit('scrolledToBottom');
        stopAllScrolling();
        animationFrameId = null;
      } else {
        isAtBottom.value = false;
        tableBody.scrollTop += props.scrollSpeed;
        animationFrameId = requestAnimationFrame(scroll);
      }
    };
    animationFrameId = requestAnimationFrame(scroll);
  }, props.scrollInterval);
};

const handleMouseEnter = () => {
  isHovering = true;
  stopAllScrolling();
};

const handleMouseLeave = () => {
  isHovering = false;
  if (!isAtBottom.value && needsScrolling.value) {
    setTimeout(() => {
      if (!isHovering) startAutoScroll();
    }, 100);
  }
};

const handleWheel = (event: WheelEvent) => {
  stopAllScrolling();
  const tableBody = tableBodyRef.value;
  if (!tableBody) return;

  tableBody.scrollTop += event.deltaY;
  event.preventDefault();

  if (tableBody.scrollTop + tableBody.clientHeight >= tableBody.scrollHeight - 1) {
    isAtBottom.value = true;
    emit('scrolledToBottom');
  } else {
    isAtBottom.value = false;
  }
};

const handleRowMouseEnter = (item: Record<string, any>, event: MouseEvent) => {
  tooltipContent.value = item;
  updateTooltipPosition(event);
  tooltipStyle.opacity = 1;
};

const updateTooltipPosition = (event: MouseEvent) => {
  const containerElement = document.querySelector('.ranking-table-container');
  if (!containerElement) return;

  const containerRect = containerElement.getBoundingClientRect();
  const mouseX = event.clientX;
  const mouseY = event.clientY;

  // 计算相对于容器的鼠标位置
  const relativeX = mouseX - containerRect.left;
  const relativeY = mouseY - containerRect.top;

  const tooltipWidthEstimate = 250;
  const tooltipHeightEstimate = 100;
  const gap = 10; // 鼠标与tooltip的间距

  // 计算最终位置，优先显示在鼠标上方
  let finalLeft = relativeX - tooltipWidthEstimate / 2; // 居中显示
  let finalTop = relativeY - tooltipHeightEstimate - gap; // 显示在鼠标上方

  // 左右边界检查
  if (finalLeft < 0) {
    finalLeft = gap; // 左边界
  } else if (finalLeft + tooltipWidthEstimate > containerRect.width) {
    finalLeft = containerRect.width - tooltipWidthEstimate - gap; // 右边界
  }

  // 上下边界检查
  if (finalTop < 0) {
    // 如果上方空间不足，显示在鼠标下方
    finalTop = relativeY + gap;
  }

  // 确保不超出下边界
  if (finalTop + tooltipHeightEstimate > containerRect.height) {
    finalTop = containerRect.height - tooltipHeightEstimate - gap;
  }

  tooltipStyle.left = `${Math.max(0, finalLeft)}px`;
  tooltipStyle.top = `${Math.max(0, finalTop)}px`;
};

const handleMouseMove = (event: MouseEvent) => {
  // 如果tooltip正在显示，则更新其位置
  if (tooltipContent.value) {
    updateTooltipPosition(event);
  }
};

const handleRowMouseLeave = () => {
  tooltipContent.value = null;
  tooltipStyle.opacity = 0;
};

const handleRowClick = (item: Record<string, any>) => {
  emit('rowClicked', item);
};

// 初始化tbody高度和滚动检查
const initializeTable = () => {
  nextTick(() => {
    const tableBody = tableBodyRef.value;
    if (tableBody) {
      tableBody.style.height = tbodyHeight.value;
      // 稍微延迟一下，确保高度设置完成
      setTimeout(() => {
        startAutoScroll();
      }, 50);
    }
  });
};

watch(() => props.data, () => {
  stopAllScrolling();
  isAtBottom.value = false;
  initializeTable();
}, { deep: true });

watch(() => props.height, () => {
  stopAllScrolling();
  isAtBottom.value = false;
  initializeTable();
});

onMounted(() => {
  initializeTable();
});

onUnmounted(() => stopAllScrolling());
</script>

<style scoped>
.ranking-table-container {
  overflow: hidden;
  position: relative;
  border-radius: 8px;
  width: 100%;
  box-sizing: border-box;
}

.ranking-table {
  width: 100%;
  border-collapse: collapse;
  color: #fff;
  font-family: 'PingFang SC', sans-serif;
  text-align: center;
  height: 100%;
  display: flex;
  flex-direction: column;
}

thead {
  background-color: #0a1f44;
  position: sticky;
  top: 0;
  z-index: 10;
  flex-shrink: 0;
}

th {
  padding: 12px 8px;
  font-size: 14px;
  font-weight: normal;
  color: #fff;
}

tbody {
  flex: 1;
  display: block;
  overflow-y: auto;
  overflow-x: hidden;
  scrollbar-width: none;
}

tbody::-webkit-scrollbar {
  width: 8px;
}

tbody::-webkit-scrollbar-track {
  border-radius: 4px;
  background: rgba(4,16,34,0.5);
}

tbody::-webkit-scrollbar-thumb {
  background-color: #0a1f44;
  border-radius: 4px;
  border: 2px solid transparent;
  background-clip: content-box;
}

tr {
  display: table;
  width: 100%;
  table-layout: fixed;
  transition: background-color 0.3s ease;
  cursor: pointer;
}

tr:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

tr.is-at-bottom {
  background-color: rgba(255, 255, 255, 0.1);
}

td {
  padding: 10px 8px;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* Tooltip */
.tooltip {
  position: absolute;
  background-color: rgba(60,60,60,0.95);
  color: #fff;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 13px;
  z-index: 1000;
  pointer-events: none;
  box-shadow: 0 4px 12px rgba(0,0,0,0.3);
  max-width: 300px;
  word-wrap: break-word;
  white-space: pre-wrap;
  overflow-wrap: break-word;
  transition: opacity 0.2s ease-in-out;
  border: 1px solid rgba(255,255,255,0.1);
}

.tooltip div {
  margin-bottom: 4px;
}

.tooltip div:last-child {
  margin-bottom: 0;
}

.tooltip strong {
  margin-right: 5px;
  color: rgba(255,255,255,0.8);
}
</style>
