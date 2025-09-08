<template>
  <div class="ranking-table-container" :style="{ height: height, width: width }">
    <table class="ranking-table">
      <thead>
      <tr>
        <th>排名</th>
        <th v-for="(header, index) in headers" :key="index">{{ header }}</th>
      </tr>
      </thead>
      <tbody ref="tableBodyRef" @mouseenter="handleMouseEnter" @mouseleave="handleMouseLeave" @wheel="handleWheel">
      <tr
        v-for="(item, index) in data"
        :key="index"
        @click="handleRowClick(item)"
        :class="{ 'is-at-bottom': isAtBottom && index === data.length - 1 }"
      >
        <td>{{ index + 1 }}</td>
        <td v-for="(header, headerIndex) in headers" :key="headerIndex">
          {{ item[Object.keys(item)[headerIndex]] ?? '' }}
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import {computed, nextTick, onMounted, onUnmounted, ref, watch} from 'vue';

const tableBodyRef = ref<HTMLElement | null>(null);
let animationFrameId: number | null = null; // 用于 requestAnimationFrame
let scrollTimeout: NodeJS.Timeout | null = null;    // 用于 setTimeout 控制轮播间隔
let isHovering: boolean = false;       // 标记鼠标是否悬停在表格上
const isAtBottom = ref<boolean>(false); // 标记是否滚动到底部

// --- Props ---
const props = defineProps({
  // 表头配置，例如: ['设备名称', '状态', '运行时间']
  headers: {
    type: Array as () => string[],
    default: () => ['设备名称', '状态', '运行时间'] // 示例表头
  },
  // 表格数据，例如: [{ deviceName: '设备A', status: '正常', uptime: '...' }]
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
  // 容器的总高度，用于设置表格容器的固定高度
  height: {
    type: String,
    default: '300px' // 默认高度
  },
  width: {
    type: String,
    default: '100%' // 默认宽度
  },
  // 表格内可见的行数，用于计算 tbody 的高度
  visibleRows: {
    type: Number,
    default: 5 // 默认显示 5 条
  },
  // 轮播间隔时间（毫秒）
  scrollInterval: {
    type: Number,
    default: 2000 // 默认 2 秒轮播一次
  },
  // 滚动速度（像素/帧）
  scrollSpeed: {
    type: Number,
    default: 1 // 默认每帧滚动 1 像素
  }
});

// --- Emits ---
const emit = defineEmits(['scrolledToBottom', 'rowClicked']);

// --- Computed Properties ---
// 计算 tbody 的高度，以容纳 visibleRows 指定数量的行
const tbodyHeight = computed(() => {
  const rowHeightEstimate = 40; // 估算每行的高度（px），根据实际 CSS 调整
  return `${props.visibleRows * rowHeightEstimate}px`;
});

// --- Methods ---

// 停止所有动画和定时器
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

// 开始自动轮播
const startAutoScroll = () => {
  // 只有当数据量大于可见行数时才可能需要滚动
  if (props.data.length <= props.visibleRows) {
    isAtBottom.value = true; // 数据不足，视为已到达底部
    emit('scrolledToBottom');
    return;
  }

  stopAllScrolling(); // 确保只有一个动画在运行

  // 使用 setTimeout 控制轮播开始的时机
  scrollTimeout = setTimeout(() => {
    const scroll = () => {
      const tableBody = tableBodyRef.value;
      if (!tableBody) return;

      // 检查是否滚动到底部
      // scrollTop: 滚动条顶部到元素顶部的距离
      // clientHeight: 元素的可见高度
      // scrollHeight: 元素的总高度（包括不可见部分）
      if (tableBody.scrollTop + tableBody.clientHeight >= tableBody.scrollHeight) {
        isAtBottom.value = true;
        emit('scrolledToBottom');
        stopAllScrolling(); // 滚动到底部，停止所有滚动
      } else {
        isAtBottom.value = false; // 还在滚动过程中
        // 滚动
        tableBody.scrollTop += props.scrollSpeed;
        animationFrameId = requestAnimationFrame(scroll);
      }
    };
    // 启动动画帧
    animationFrameId = requestAnimationFrame(scroll);
  }, props.scrollInterval);
};

// 鼠标悬停时停止自动轮播
const handleMouseEnter = () => {
  isHovering = true;
  stopAllScrolling();
};

// 鼠标离开时恢复自动轮播（如果之前是在自动轮播状态且未到底部）
const handleMouseLeave = () => {
  isHovering = false;
  // 只有在没有滚动到底部时才考虑恢复自动滚动
  if (!isAtBottom.value) {
    // 稍微延迟恢复，避免滚轮事件触发后立即又开始自动滚动
    setTimeout(() => {
      // 再次确认鼠标未悬停
      if (!isHovering) {
        startAutoScroll();
      }
    }, 100); // 延迟100ms
  }
};

// 鼠标滚轮处理，仅允许向下滚动
const handleWheel = (event: WheelEvent) => {
  // 只允许向下滚动
  if (event.deltaY > 0) {
    stopAllScrolling(); // 手动滚动时暂停所有滚动
    // 标记为手动滚动，这样 handleMouseLeave 不会立即恢复自动滚动
    // isScrollingManually = true; // 这个标记在这里可能不需要，因为 handleMouseLeave 已经检查 isHovering
    const tableBody = tableBodyRef.value;
    if (!tableBody) return;

    // 检查是否已经滚动到底部
    if (tableBody.scrollTop + tableBody.clientHeight >= tableBody.scrollHeight) {
      isAtBottom.value = true;
      emit('scrolledToBottom');
      event.preventDefault(); // 阻止默认滚动行为，防止页面整体滚动
      return;
    } else {
      isAtBottom.value = false; // 还在滚动过程中
    }

    // 滚动
    tableBody.scrollTop += event.deltaY;
    event.preventDefault(); // 阻止默认滚动行为
  }
  // 如果是向上滚动，则不进行任何操作，不阻止默认行为
};

// 处理行点击事件
const handleRowClick = (item: Record<string, any>) => {
  console.log('row clicked:', item);
  emit('rowClicked', item);
};

// --- Watchers ---
// 监听数据或配置变化，重新计算和启动滚动
watch([() => props.data, () => props.visibleRows, () => props.scrollInterval, () => props.height], () => {
  nextTick(() => {
    stopAllScrolling();
    isAtBottom.value = false;
    if (tableBodyRef.value) {
      // 重新设置 tbody 的高度
      tableBodyRef.value.style.height = tbodyHeight.value;
      // 重新启动滚动逻辑
      startAutoScroll();
    }
  });
}, {deep: true});

// --- Lifecycle Hooks ---
onMounted(() => {
  nextTick(() => {
    if (tableBodyRef.value) {
      // 设置 tbody 的高度，只显示 visibleRows 的内容
      tableBodyRef.value.style.height = tbodyHeight.value;
      // 启动自动滚动
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
  overflow: hidden; /* 关键：容器只显示指定的高度 */
  //border: 1px solid #0a1f44;
  background-color: #041022;
  border-radius: 8px;
  width: 100%;
  box-sizing: border-box; /* 确保 padding 和 border 包含在元素宽高内 */
}

.ranking-table {
  width: 100%;
  border-collapse: collapse;
  color: #fff;
  font-family: 'PingFang SC', sans-serif;
  text-align: center;
}

thead {
  //background-color: #0a1f44;
  position: sticky; /* 表头固定，重要！ */
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
  overflow-y: auto; /* 启用垂直滚动条 */
  /* 滚动条样式 */
  scrollbar-width: thin; /* Firefox */
  //scrollbar-color: #0a1f44 #041022; /* Firefox */
}

/* Chrome, Edge, Safari */
tbody::-webkit-scrollbar {
  width: 8px; /* 滚动条宽度 */
}

tbody::-webkit-scrollbar-track {
  //background: #041022; /* 滚动条轨道背景 */
  border-radius: 4px;
}

tbody::-webkit-scrollbar-thumb {
  background-color: #0a1f44; /* 滚动条滑块颜色 */
  border-radius: 4px;
}

tbody::-webkit-scrollbar-corner {
  background: #041022; /* 滚动条角落 */
}

tr {
  display: table; /* 保证 tr 撑开 width: 100% */
  width: 100%;
  table-layout: fixed;
  transition: background-color 0.3s ease;
  cursor: pointer; /* 鼠标移入显示手型，提示可点击 */
}

/* 鼠标悬停效果 */
tr:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* 滚动到底部时，最后一行可能需要特殊样式，例如高亮 */
tr.is-at-bottom {
  background-color: rgba(255, 255, 255, 0.1);
}

td {
  padding: 10px 8px;
  font-size: 14px;
  //border-bottom: 1px solid #0a1f44;
  overflow: hidden; /* 防止内容溢出 */
  text-overflow: ellipsis; /* 超出部分显示省略号 */
  white-space: nowrap; /* 不换行 */
}
</style>
