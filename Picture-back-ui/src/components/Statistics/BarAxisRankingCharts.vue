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
import {markRaw, nextTick, onBeforeUnmount, onMounted, ref, watch} from 'vue'
import * as echarts from 'echarts'
import 'echarts/theme/macarons'
import {generateRandomColor} from "@/utils/ruoyi.js";

const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '100%'},
  chartName: {type: String, default: '排行榜'},
  chartData: {
    type: Object,
    default: () => ({
      names: ['产品A', '产品B', '产品C', '产品D', '产品E', '产品F', '产品G', '产品H'],
      totals: [120, 200, 150, 80, 0, 110, 130, 0]
    })
  },
  chartCarousel: {type: Number, default: 1000},
  chartItemTotal: {type: Number, default: 5},
  // 柱子方向：'left' (名字左、柱子向右) | 'right' (名字右、柱子向左)
  chartDirection: {
    type: String,
    default: 'left',
    validator: (value) => ['left', 'right'].includes(value)
  },
  defaultColor: {
    type: Array,
    default: () => [
      '#5B8FF9', '#5AD8A6', '#5D7092', '#F6BD16', '#E86A92',
      '#7262FD', '#269A29', '#8E36BE', '#41A7E2', '#7747A3',
      '#FF7F50', '#FFDAB9', '#ADFF2F', '#00CED1', '#9370DB',
      '#3CB371', '#FF69B4', '#FFB6C1', '#DA70D6', '#98FB98',
      '#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7',
      '#DDA0DD', '#98D8C8', '#F7DC6F', '#BB8FCE', '#85C1E9'
    ]
  },
})

const chart = ref(null)
const chartRef = ref(null)
const intervalId = ref(null)
const currentIndex = ref(0)
let resizeObserver = null

// 防抖（resize）
const debounce = (fn, delay = 200) => {
  let timer = null
  return (...args) => {
    clearTimeout(timer)
    timer = setTimeout(() => fn(...args), delay)
  }
}

/**
 * 返回：排序后的全部数据（过滤 value > 0）
 * [{name, value}, ...]
 */
const getSortedAllData = () => {
  const names = (props.chartData && props.chartData.names) || []
  const totals = (props.chartData && props.chartData.totals) || []
  return names.map((n, i) => ({name: n, value: totals[i]}))
      .filter(item =>  Number(item.value) > 0)
      .sort((a, b) => b.value - a.value);
}

/**
 * 获取当前要显示的一屏数据（从 startIndex 开始，显示 visibleItems 条）
 * 返回 { currentNameList, currentValueList, sortedAllData }
 */
const getCurrentData = (startIndex, visibleItems) => {
  const sortedAllData = getSortedAllData()
  const len = sortedAllData.length
  const currentNameList = []
  const currentValueList = []
  const currentDisplayNameList = []
  if (len === 0) {
    return {currentNameList, currentValueList, currentDisplayNameList, sortedAllData}
  }

  const showCount = Math.min(visibleItems, len)
  for (let i = 0; i < showCount; i++) {
    const idx = (startIndex + i) % len
    const item = sortedAllData[idx]
    currentNameList.push(item.name)
    currentValueList.push(item.value)
    // 名称截断
    currentDisplayNameList.push(item.name.length > 4 ? item.name.slice(0, 4) + '…' : item.name)
  }

  return {currentNameList, currentValueList, currentDisplayNameList, sortedAllData}
}

/**
 * 更新图表（关键：传入 startIndex 切换一屏）
 */
const updateChart = (startIndex = 0) => {
  if (!chart.value) return
  if (!props.chartData || !Array.isArray(props.chartData.names) || !Array.isArray(props.chartData.totals)) return
  const visibleItems = Math.min((props.chartData.names || []).length, props.chartItemTotal)
  const {
    currentNameList,
    currentValueList,
    currentDisplayNameList,
    sortedAllData
  } = getCurrentData(startIndex, visibleItems)

  const isRight = props.chartDirection === 'right'

  const seriesData = currentValueList.map((v, i) => ({
    value: v,
    name: currentNameList[i],
    itemStyle: {
      color: new echarts.graphic.LinearGradient(
          isRight ? 1 : 0, 0, isRight ? 0 : 1, 0,
          [
            {offset: 0, color: generateRandomColor(props.defaultColor)},
          ]
      )
    },
    label: {
      show: true,
      position: isRight ? 'left' : 'right',
      formatter: '{c}',
      color: '#ffffff',
      fontSize: 14
    }
  }))
  const option = {
    title: {
      text: props.chartName,
      textStyle: {color: '#ffffff', fontSize: 16},
      left: 'center',
      top: '2%'
    },
    tooltip: {
      trigger: 'item',
      axisPointer: {type: 'shadow'},
      formatter: function (params) {
        const realRank = sortedAllData.findIndex(item => item.name === params.name) + 1
        return `<div style="font-size:13px;line-height:20px;">
                  <b>排名：</b>${realRank}<br/>
                  <b>名称：</b>${params.name}<br/>
                  <b>数值：</b>${params.value}
                </div>`
      }
    },
    grid: isRight
        ? {top: '26%', left: '10%', right: '20%', bottom: '3%'}   // 柱子在左，名字在右
        : {top: '26%', left: '20%', right: '10%', bottom: '3%'}, // 柱子在右，名字在左
    yAxis: {
      type: 'category',
      inverse: true,
      axisLabel: {
        color: '#ffffff',
        fontSize: 14,
        align: isRight ? 'left' : 'right'
      },
      axisTick: {show: true, inside: true},
      splitArea: {show: false},
      position: isRight ? 'right' : 'left',
      data: currentDisplayNameList
    },
    xAxis: {
      type: 'value',
      axisLine: {show: true},
      position:'top',
      axisTick: {show: true}, // 显示刻度线
      axisLabel: {
        show: true,          // 显示刻度值
        color: '#66FFFF',    // 刻度颜色
        fontSize: 12,
        formatter: function (value) {
          return value;       // 可以格式化成你想要的样式
        },
        inside: false
      },
      splitArea: {show: false},
      inverse: isRight,       // 柱子朝向反转
      splitLine: {
        show: true,
        lineStyle: {
          color: 'rgba(102,255,255,.5)',
          width: 1,
          type: 'solid'
        },
      }
    },
    series: [{
      type: 'bar',
      barWidth: '10px',
      data: seriesData,
      itemStyle: {
        borderRadius: [10, 10, 10, 10]
      }
    }]
  }

  chart.value.setOption(option, true)
}

/**
 * 自动轮播（仅在数据总数 > 可见条数时启用）
 */
const startCarousel = () => {
  stopCarousel()
  const sortedAllData = getSortedAllData()
  const filteredLength = sortedAllData.length
  if (filteredLength > props.chartItemTotal) {
    intervalId.value = setInterval(() => {
      currentIndex.value = (currentIndex.value + 1) % filteredLength
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

const handleMouseEnter = () => stopCarousel()
const handleMouseLeave = () => startCarousel()

/**
 * 滚轮切换：真正切换「整屏数据」
 * 如果总条数 <= 可见条数 => 不做切换（没有必要）
 */
const handleWheel = (event) => {
  // 阻止页面滚动（在非 passive 情况下有效）
  try {
    event.preventDefault?.()
  } catch (e) { /* ignore */
  }

  const sortedAllData = getSortedAllData()
  const filteredLength = sortedAllData.length
  const visibleItems = Math.min(filteredLength, props.chartItemTotal)
  if (filteredLength <= visibleItems) return // 一屏已包含全部，不需要翻页

  stopCarousel()
  currentIndex.value = event.deltaY > 0
      ? (currentIndex.value + 1) % filteredLength
      : (currentIndex.value - 1 + filteredLength) % filteredLength

  updateChart(currentIndex.value)
}

const initChart = async () => {
  if (chart.value) chart.value.dispose()
  if (!chartRef.value) return
  // 确保 DOM 已就绪
  await nextTick()
  chart.value = markRaw(echarts.init(chartRef.value, 'macarons'))
  currentIndex.value = 0
  updateChart(0)
  startCarousel()
}

const resizeChart = debounce(() => chart.value?.resize(), 200)

onMounted(async () => {
  await nextTick()
  initChart()
  if (chartRef.value) {
    resizeObserver = new ResizeObserver(resizeChart)
    resizeObserver.observe(chartRef.value)
  }
  window.addEventListener('resize', resizeChart)
})

onBeforeUnmount(() => {
  if (chart.value) chart.value.dispose()
  stopCarousel()
  resizeObserver?.disconnect()
  window.removeEventListener('resize', resizeChart)
})

// 数据/方向变化时：重置并重启（确保当前 index 在新数据下合理）
watch(() => props.chartData, () => initChart(), {deep: true})
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
</style>
