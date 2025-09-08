<template>
  <div
    :class="className"
    :style="{ height, width }"
    ref="chartRef"
    @mouseenter="handleMouseEnter"
    @mouseleave="handleMouseLeave"
    @wheel="handleWheel"
  ></div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch, nextTick, markRaw } from 'vue'
import * as echarts from 'echarts'
import 'echarts/theme/macarons' // 引入主题

const props = defineProps({
  className: { type: String, default: 'chart' },
  width: { type: String, default: '100%' },
  height: { type: String, default: '100%' },
  chartName: { type: String, default: '排行榜' },
  chartData: {
    type: Object,
    default: () => ({
      nameList: ['产品A', '产品B', '产品C', '产品D', '产品E', '产品F', '产品G', '产品H'],
      valueList: [120, 200, 150, 80, 70, 110, 130, 180]
    })
  },
  chartCarousel: { type: Number, default: 500 }, // 轮播间隔时间 (ms)
  chartItemTotal: { type: Number, default: 5 } // 每次显示的最多条目数
})

const chart = ref(null) // ECharts 实例
const chartRef = ref(null) // DOM 引用
const intervalId = ref(null) // 轮播定时器 ID
const currentIndex = ref(0) // 当前轮播的起始下标

/**
 * 获取当前轮播显示的数据
 * @param {number} startIndex - 当前轮播的起始下标
 * @param {number} visibleItems - 需要显示的条目数
 * @returns {object} - 包含当前名称列表、数值列表以及所有数据的真实排序列表
 */
const getCurrentData = (startIndex, visibleItems) => {
  const { nameList = [], valueList = [] } = props.chartData

  // 1. 创建一个包含所有数据及其原始信息的数组
  const allData = nameList.map((name, index) => ({
    name,
    value: valueList[index],
    originalIndex: index // 保存原始索引，以便查找
  }))

  // 2. 对所有数据按数值降序排序，以获得真实排名
  allData.sort((a, b) => b.value - a.value)

  const currentNameList = []
  const currentValueList = []

  // 3. 根据 startIndex 和 visibleItems，从排序后的 allData 中提取当前显示的数据
  for (let i = 0; i < visibleItems; i++) {
    // 使用模运算循环获取数据
    const idx = (startIndex + i) % allData.length
    currentNameList.push(allData[idx].name)
    currentValueList.push(allData[idx].value)
  }

  return { currentNameList, currentValueList, sortedAllData: allData }
}

/**
 * 更新 ECharts 图表配置
 * @param {number} startIndex - 当前轮播的起始下标
 */
const updateChart = (startIndex = 0) => {
  if (!chart.value) return

  const { nameList = [] } = props.chartData
  const totalItems = nameList.length
  // 确定本次图表需要显示的实际条目数，不能超过总条目数
  const visibleItems = Math.min(totalItems, props.chartItemTotal)

  // 获取当前显示的数据以及所有数据的排序列表
  const { currentNameList, currentValueList, sortedAllData } = getCurrentData(startIndex, visibleItems)

  const option = {
    title: {
      text: props.chartName,
      textStyle: { color: '#000000', fontSize: 16 },
      left: 'center',
      top: '2%'
    },
    tooltip: {
      trigger: 'item',
      axisPointer: { type: 'shadow' },
      formatter: function (params) {
        // params.name 是当前 tooltip 触发项的名称
        // 在 sortedAllData 中找到该名称对应的项，获取其真实排名
        const realRank = sortedAllData.findIndex(item => item.name === params.name) + 1
        const realName = params.name
        const value = params.value
        return `
          <div style="font-size:13px;line-height:20px;">
            <b>排名：</b>${realRank}<br/>
            <b>名称：</b>${realName}<br/>
            <b>数值：</b>${value}
          </div>
        `
      }
    },
    grid: { top: '13%', left: '20%', right: '0%', bottom: '3%' },
    yAxis: {
      type: 'category',
      inverse: true, // 反转Y轴，使数值最大的在最上方
      axisLabel: { color: '#000000', fontSize: 14 },
      axisLine: { show: false },
      axisTick: { show: false },
      splitArea: { show: false },
      data: currentNameList
    },
    xAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: { show: false },
      axisLabel: { show: false } // 不显示X轴刻度标签
    },
    series: [
      {
        type: 'bar',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#00BBFD' }, // 渐变色起点
            { offset: 1, color: '#0085FA' }  // 渐变色终点
          ]),
          borderRadius: [10, 10, 10, 10] // 圆角
        },
        label: {
          show: true,
          position: ['95%', '10%'], // 标签位置
          verticalAlign: 'bottom',
          align: 'center',
          formatter: '{c}', // 显示数值
          color: '#000000',
          fontSize: 14
        },
        barWidth: '10px', // 柱子宽度
        data: currentValueList
      }
    ]
  }

  // 设置图表选项，true 表示合并配置，覆盖旧配置
  chart.value.setOption(option, true)
}

/**
 * 启动自动轮播
 */
const startCarousel = () => {
  stopCarousel() // 先停止当前可能存在的轮播
  const { nameList = [] } = props.chartData
  // 只有当总条目数大于显示条目数时才开启轮播
  if (nameList.length > props.chartItemTotal) {
    intervalId.value = setInterval(() => {
      // 循环更新 currentIndex，实现轮播
      currentIndex.value = (currentIndex.value + 1) % nameList.length
      updateChart(currentIndex.value)
    }, props.chartCarousel)
  }
}

/**
 * 停止自动轮播
 */
const stopCarousel = () => {
  if (intervalId.value) {
    clearInterval(intervalId.value)
    intervalId.value = null
  }
}

/**
 * 鼠标移入事件：停止自动轮播
 */
const handleMouseEnter = () => {
  stopCarousel()
}

/**
 * 鼠标移出事件：重新开始自动轮播
 */
const handleMouseLeave = () => {
  startCarousel()
}

/**
 * 鼠标滚轮事件：手动切换图表
 * @param {Event} event - 鼠标滚轮事件对象
 */
const handleWheel = (event) => {
  const { nameList = [] } = props.chartData
  if (!nameList.length) return // 如果没有数据，则不处理

  stopCarousel() // 手动操作时，停止自动轮播

  if (event.deltaY > 0) {
    // 向下滚 → 显示下一页（下一组数据）
    currentIndex.value = (currentIndex.value + 1) % nameList.length
  } else {
    // 向上滚 → 显示上一页（上一组数据）
    // 使用 (currentIndex.value - 1 + nameList.length) % nameList.length 来处理负数取模
    currentIndex.value = (currentIndex.value - 1 + nameList.length) % nameList.length
  }
  updateChart(currentIndex.value)
}

/**
 * 初始化 ECharts 图表
 */
const initChart = () => {
  // 如果已有图表实例，先销毁
  if (chart.value) {
    chart.value.dispose()
    chart.value = null
  }
  // 确保 DOM 元素存在
  if (!chartRef.value) return

  // 使用 markRaw 包裹 echarts.init() 的结果，防止 ECharts 实例被 Vue 代理
  chart.value = markRaw(echarts.init(chartRef.value, 'macarons'))

  currentIndex.value = 0 // 重置轮播下标
  updateChart() // 初始化显示第一页数据
  startCarousel() // 启动自动轮播
}

/**
 * 响应窗口大小变化，重置图表尺寸
 */
const resizeChart = () => {
  // 使用可选链 ?. 确保 chart.value 存在
  chart.value?.resize()
}

// 组件挂载后执行
onMounted(async () => {
  await nextTick() // 等待 DOM 更新完成
  initChart() // 初始化图表
  window.addEventListener('resize', resizeChart) // 监听窗口大小变化
})

// 组件卸载前执行
onBeforeUnmount(() => {
  if (chart.value) {
    chart.value.dispose() // 销毁图表实例
    chart.value = null
  }
  stopCarousel() // 停止轮播
  window.removeEventListener('resize', resizeChart) // 移除事件监听器
})

// 监听 chartData 变化，数据更新时重新初始化图表
watch(
  () => props.chartData,
  () => initChart(),
  { deep: true } // 深度监听，确保对象内部变化也能触发
)
</script>

<style scoped>
.chart {
  overflow: hidden; /* 隐藏超出部分，确保图表在容器内 */
}
</style>
