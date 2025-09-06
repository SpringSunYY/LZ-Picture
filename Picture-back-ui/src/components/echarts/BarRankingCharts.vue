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
import { ref, onMounted, onBeforeUnmount, watch, nextTick } from 'vue'
import * as echarts from 'echarts'
import 'echarts/theme/macarons'

const props = defineProps({
  className: { type: String, default: 'chart' },
  width: { type: String, default: '100%' },
  height: { type: String, default: '250px' },
  chartName: { type: String, default: '排行榜' },
  chartData: {
    type: Object,
    default: () => ({
      nameList: ['产品A', '产品B', '产品C', '产品D', '产品E', '产品F', '产品G', '产品H'],
      valueList: [120, 200, 150, 80, 70, 110, 130, 180]
    })
  },
  chartCarousel: { type: Number, default: 500 },
  chartItemTotal: { type: Number, default: 5 }
})

const chart = ref(null)
const chartRef = ref(null)
const intervalId = ref(null)
const currentIndex = ref(0) // 当前轮播下标

// 获取当前数据
const getCurrentData = (startIndex, visibleItems) => {
  const { nameList = [], valueList = [] } = props.chartData
  const currentNameList = []
  const currentValueList = []
  for (let i = 0; i < visibleItems; i++) {
    const idx = (startIndex + i) % nameList.length
    currentNameList.push(nameList[idx]) // 去掉前缀，y轴更干净
    currentValueList.push(valueList[idx])
  }
  return { currentNameList, currentValueList }
}

// 更新图表
const updateChart = (startIndex = 0) => {
  if (!chart.value) return

  const { nameList = [], valueList = [] } = props.chartData
  const totalItems = nameList.length
  const visibleItems = Math.min(totalItems, props.chartItemTotal)
  const { currentNameList, currentValueList } = getCurrentData(startIndex, visibleItems)

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
        const rank = startIndex + params.dataIndex + 1 // 动态算排名
        const realName = params.name
        const value = params.value
        return `
          <div style="font-size:13px;line-height:20px;">
            <b>排名：</b>${rank}<br/>
            <b>名称：</b>${realName}<br/>
            <b>数值：</b>${value}
          </div>
        `
      }
    },
    grid: { top: '13%', left: '20%', right: '0%', bottom: '3%' },
    yAxis: {
      type: 'category',
      inverse: true,
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
      axisLabel: { show: false }
    },
    series: [
      {
        type: 'bar',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#00BBFD' },
            { offset: 1, color: '#0085FA' }
          ]),
          borderRadius: [10, 10, 10, 10]
        },
        label: {
          show: true,
          position: ['95%', '10%'],
          verticalAlign: 'bottom',
          align: 'center',
          formatter: '{c}',
          color: '#000000',
          fontSize: 14
        },
        barWidth: '10px',
        data: currentValueList
      }
    ]
  }

  chart.value.setOption(option, true)
}

// 自动轮播
const startCarousel = () => {
  stopCarousel()
  const { nameList = [] } = props.chartData
  if (nameList.length > props.chartItemTotal) {
    intervalId.value = setInterval(() => {
      currentIndex.value = (currentIndex.value + 1) % nameList.length
      updateChart(currentIndex.value)
    }, props.chartCarousel)
  }
}

const stopCarousel = () => {
  if (intervalId.value) {
    clearInterval(intervalId.value)
    intervalId.value = null
  }
}

// 鼠标移入 → 停止自动轮播
const handleMouseEnter = () => {
  stopCarousel()
}

// 鼠标移出 → 重新开始自动轮播
const handleMouseLeave = () => {
  startCarousel()
}

// 鼠标滚轮手动切换
const handleWheel = (event) => {
  const { nameList = [] } = props.chartData
  if (!nameList.length) return
  stopCarousel() // 一旦手动操作，停掉自动轮播
  if (event.deltaY > 0) {
    // 向下滚 → 下一页
    currentIndex.value = (currentIndex.value + 1) % nameList.length
  } else {
    // 向上滚 → 上一页
    currentIndex.value = (currentIndex.value - 1 + nameList.length) % nameList.length
  }
  updateChart(currentIndex.value)
}

// 初始化
const initChart = () => {
  if (chart.value) {
    chart.value.dispose()
    chart.value = null
  }
  if (!chartRef.value) return
  chart.value = echarts.init(chartRef.value, 'macarons')
  currentIndex.value = 0
  updateChart()
  startCarousel()
}

const resizeChart = () => {
  chart.value?.resize()
}

onMounted(async () => {
  await nextTick()
  initChart()
  window.addEventListener('resize', resizeChart)
})

onBeforeUnmount(() => {
  chart.value?.dispose()
  chart.value = null
  stopCarousel()
  window.removeEventListener('resize', resizeChart)
})

watch(
  () => props.chartData,
  () => initChart(),
  { deep: true }
)
</script>

<style scoped>
.chart {
  overflow: hidden;
}
</style>
