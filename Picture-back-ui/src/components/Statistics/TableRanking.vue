<template>
  <div class="ranking-table-container" :style="{ height: height, width: width }">
    <table class="ranking-table">
      <thead>
        <tr>
          <th>序号</th>
          <th v-for="(header, index) in headers" :key="index">{{ header }}</th>
        </tr>
      </thead>
      <tbody
        ref="tableBodyRef"
        @mouseenter="handleMouseEnter"
        @mouseleave="handleMouseLeave"
        @wheel.passive="handleWheel"
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
          <td v-for="(header, headerIndex) in headers" :key="headerIndex">
            {{ item[Object.keys(item)[headerIndex]] ?? '' }}
          </td>
        </tr>
      </tbody>
    </table>

    <div v-show="tooltipContent" class="tooltip" :style="tooltipStyle">
      <div v-for="(value, key) in tooltipContent" :key="key">
        <strong>{{ getHeaderByField(key as string) }}:</strong> {{ value }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, nextTick, onMounted, onUnmounted, ref, watch, reactive} from 'vue';

// --- Refs ---
const tableBodyRef = ref<HTMLElement | null>(null);
let animationFrameId: number | null = null; // 用于 requestAnimationFrame
let scrollTimeout: ReturnType<typeof setTimeout> | null = null; // 用于 setTimeout 控制轮播间隔
let isHovering: boolean = false; // 标记鼠标是否悬停在表格上
const isAtBottom = ref<boolean>(false); // 标记是否滚动到底部

// Tooltip 相关
const tooltipContent = ref<Record<string, any> | null>(null); // 存储当前 tooltip 显示的内容
const tooltipStyle = reactive({
  top: '10px',
  left: '0px',
  opacity: 0 // 初始隐藏
});

// --- Props ---
const props = defineProps({
  headers: {
    type: Array as () => string[],
    default: () => ['设备名称', '状态', '运行时间'] // 示例表头
  },
  data: {
    type: Array as () => Record<string, any>[],
    default: () => [
      {deviceName: '服务器A', status: '正常', uptime: '738天2时21分29秒'},
      {deviceName: '路由器B', status: '正常', uptime: '738天2时21分29秒'},
      {deviceName: '交换机C', status: '正常', uptime: '738天2时21分29秒'},
      {deviceName: '防火墙D', status: '异常', uptime: '738天2时21分32秒'},
      {deviceName: '存储E', status: '正常', uptime: '738天2时21分29秒'},
      {deviceName: '打印机F', status: '正常', uptime: '738天2时21分30秒'},
      {deviceName: '显示器G', status: '正常', uptime: '738天2时21分29秒'},
      {deviceName: '摄像头H', status: '正常', uptime: '738天2时21分28秒'},
      {deviceName: 'UPS I', status: '异常', uptime: '688天12时27分41秒'},
      {deviceName: '负载均衡J', status: '正常', uptime: '738天2时21分29秒'},
      {deviceName: '网络接口K', status: '正常', uptime: '738天2时21分29秒'},
      {deviceName: '数据库L', status: '正常', uptime: '738天2时21分29秒'},
    ]
  },
  height: {
    type: String,
    default: '100%'
  },
  width: {
    type: String,
    default: '100%'
  },
  visibleRows: {
    type: Number,
    default: 7
  },
  scrollInterval: {
    type: Number,
    default: 2000
  },
  scrollSpeed: {
    type: Number,
    default: 1
  }
});

// --- Emits ---
const emit = defineEmits(['scrolledToBottom', 'rowClicked']);

// --- Computed Properties ---
const tbodyHeight = computed(() => {
  const rowHeightEstimate = 40; // 估算每行的高度（px）
  return `${props.visibleRows * rowHeightEstimate}px`;
});

// --- Methods ---
const getHeaderByField = (field: string): string => {
  return props.headers[field];
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
  if (props.data.length <= props.visibleRows) {
    isAtBottom.value = true;
    emit('scrolledToBottom');
    return;
  }
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
  if (!isAtBottom.value) {
    setTimeout(() => {
      if (!isHovering) {
        startAutoScroll();
      }
    }, 100);
  }
};

const handleWheel = (event: WheelEvent) => {
  if (event.deltaY > 0) {
    stopAllScrolling();
    const tableBody = tableBodyRef.value;
    if (!tableBody) return;
    if (tableBody.scrollTop + tableBody.clientHeight >= tableBody.scrollHeight - 1) {
      isAtBottom.value = true;
      emit('scrolledToBottom');
      event.preventDefault();
      return;
    } else {
      isAtBottom.value = false;
    }
    tableBody.scrollTop += event.deltaY;
    event.preventDefault();
  }
};

// --- Tooltip Handling ---
const handleRowMouseEnter = (item: Record<string, any>, event: MouseEvent) => {
  tooltipContent.value = item;
  tooltipStyle.opacity = 1; // 显示 tooltip

  if (event.target instanceof HTMLElement) {
    const currentRow = (event.target as HTMLElement).closest('tr');
    if (currentRow) {
      const rowRect = currentRow.getBoundingClientRect();
      const containerRect = (event.target as HTMLElement).closest('.ranking-table-container')!.getBoundingClientRect();

      // --- Tooltip 定位逻辑 ---
      // 1. 计算 tooltip 的 top 值：显示在当前行的上方
      //    rowRect.top 是行顶部相对于视口的位置
      //    containerRect.top 是容器顶部相对于视口的位置
      //    我们希望 tooltip 相对于容器 `.ranking-table-container` 定位
      const tooltipHeightEstimate = 100; // 估算 tooltip 的高度，用于向上定位
      const gap = 5; // 鼠标和 tooltip 之间的间隙

      // 计算 tooltip 的顶部位置，使其位于行上方
      // rowRect.top - containerRect.top 是行顶部相对于容器顶部的距离
      // 减去 tooltipHeightEstimate 和 gap，得到 tooltip 的顶部位置
      // Math.max(0, ...) 确保 top 不会是负数 (即 tooltip 不会跑到容器外面)
      tooltipStyle.top = `${Math.max(0, rowRect.top - containerRect.top - tooltipHeightEstimate - gap)}px`;

      // 2. 计算 tooltip 的 left 值：显示在当前行的左侧，并防止超出容器右边界
      const tooltipWidthEstimate = 250; // 估算 tooltip 的宽度
      const containerWidth = containerRect.width; // 容器宽度
      // rowRect.left - containerRect.left 是行左边界相对于容器的距离
      const leftPositionRelativeToContainer = rowRect.left - containerRect.left;

      let finalLeft = leftPositionRelativeToContainer;
      // 如果 tooltip 放在行左侧会超出容器右边界，则调整 left 使其靠右显示
      if (finalLeft + tooltipWidthEstimate > containerWidth) {
        finalLeft = containerWidth - tooltipWidthEstimate - 10; // 留 10px 边距
      }
      // 确保 left 不会超出容器左边界
      finalLeft = Math.max(0, finalLeft);
      tooltipStyle.left = `${finalLeft}px`;
    }
  }
};

const handleRowMouseLeave = () => {
  tooltipContent.value = null;
  tooltipStyle.opacity = 0; // 隐藏 tooltip
};

// --- Row Click Handler ---
const handleRowClick = (item: Record<string, any>) => {
  emit('rowClicked', item);
};

// --- Watchers ---
watch([() => props.data], () => {
  nextTick(() => {
    stopAllScrolling();
    isAtBottom.value = false;
    if (tableBodyRef.value) {
      tableBodyRef.value.style.height = tbodyHeight.value;
      startAutoScroll();
    }
  });
}, {deep: true});

// --- Lifecycle Hooks ---
onMounted(() => {
  nextTick(() => {
    if (tableBodyRef.value) {
      tableBodyRef.value.style.height = tbodyHeight.value;
      startAutoScroll();
    }
  });
});

onUnmounted(() => {
  stopAllScrolling();
});
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
}

thead {
  background-color: #0a1f44;
  position: sticky;
  top: 0;
  z-index: 10;
}

th {
  padding: 12px 8px;
  font-size: 14px;
  font-weight: normal;
  color: #fff;
}

tbody {
  display: block;
  overflow-y: auto;
  scrollbar-width: none; /* Firefox */
}

/* Chrome, Edge, Safari */
tbody::-webkit-scrollbar {
  width: 8px;
}

tbody::-webkit-scrollbar-track {
  border-radius: 4px;
  background: rgba(4, 16, 34, 0.5);
}

tbody::-webkit-scrollbar-thumb {
  background-color: #0a1f44;
  border-radius: 4px;
  border: 2px solid transparent;
  background-clip: content-box;
}

tbody::-webkit-scrollbar-corner {
  background: #041022;
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
  white-space: nowrap; /* 保持不换行，tooltip 会处理换行 */
}

/* Tooltip 样式 */
.tooltip {
  position: absolute;
  background-color: rgba(60, 60, 60, 0.85);
  color: #fff;
  padding: 8px 12px;
  border-radius: 4px;
  font-size: 13px;
  z-index: 1000;
  pointer-events: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  /* white-space: normal; /* 允许换行 */
  max-width: 300px; /* 限制 tooltip 最大宽度 */
  word-wrap: break-word; /* 允许长单词换行 */
  white-space: pre-wrap; /* 保留空格并允许换行 */
  overflow-wrap: break-word; /* 强制换行 */
}

.tooltip div {
  margin-bottom: 4px;
}
.tooltip div:last-child {
  margin-bottom: 0;
}
.tooltip strong {
  margin-right: 5px;
  color: rgba(255, 255, 255, 0.8);
}
</style>
