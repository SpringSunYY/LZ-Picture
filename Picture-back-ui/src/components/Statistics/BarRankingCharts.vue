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
import {ref, onMounted, onBeforeUnmount, watch, nextTick, markRaw} from 'vue'
import * as echarts from 'echarts'
import 'echarts/theme/macarons'

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
  chartItemTotal: {type: Number, default: 5}
})

const chart = ref(null)
const chartRef = ref(null)
const intervalId = ref(null)
const currentIndex = ref(0)
let resizeObserver = null

// 防抖函数，避免频繁触发 resize
const debounce = (fn, delay = 200) => {
  let timer = null
  return (...args) => {
    clearTimeout(timer)
    timer = setTimeout(() => fn(...args), delay)
  }
}

const getCurrentData = (startIndex, visibleItems) => {
  const {names = [], totals = []} = props.chartData
  const filteredData = names.map((name, index) => ({
    name,
    value: totals[index]
  })).filter(item => item.value > 0)

  filteredData.sort((a, b) => b.value - a.value)

  const currentNameList = []
  const currentValueList = []
  const currentDisplayNameList = []

  for (let i = 0; i < Math.min(visibleItems, filteredData.length); i++) {
    const idx = (startIndex + i) % filteredData.length
    const item = filteredData[idx]
    currentNameList.push(item.name)
    currentValueList.push(item.value)
    currentDisplayNameList.push(item.name.length > 4 ? item.name.slice(0, 4) + '…' : item.name)
  }

  return {currentNameList, currentValueList, currentDisplayNameList, sortedAllData: filteredData}
}

const updateChart = (startIndex = 0) => {
  if (!chart.value) return
  if (!props.chartData || !props.chartData.totals || !props.chartData.totals.length || !props.chartData.names || !props.chartData.names.length) return;
  const visibleItems = Math.min(props.chartData.names.length, props.chartItemTotal)
  const {
    currentNameList,
    currentValueList,
    currentDisplayNameList,
    sortedAllData
  } = getCurrentData(startIndex, visibleItems)

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
    grid: {top: '13%', left: '20%', right: '5%', bottom: '3%'},
    yAxis: {
      type: 'category',
      inverse: true,
      axisLabel: {color: '#ffffff', fontSize: 14},
      axisLine: {show: false},
      axisTick: {show: false},
      splitArea: {show: false},
      data: currentDisplayNameList
    },
    xAxis: {
      type: 'value',
      axisLine: {show: false},
      axisTick: {show: false},
      splitLine: {show: false},
      axisLabel: {show: false},
      splitArea: {show: false}
    },
    series: [{
      type: 'bar',
      barWidth: '10px',
      data: currentValueList.map((v, i) => ({value: v, name: currentNameList[i]})),
      label: {show: true, position: 'right', formatter: '{c}', color: '#ffffff', fontSize: 14},
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
          {offset: 0, color: '#00BBFD'},
          {offset: 1, color: '#0085FA'}
        ]),
        borderRadius: [10, 10, 10, 10]
      }
    }]
  }

  chart.value.setOption(option, true)
}

const startCarousel = () => {
  stopCarousel()
  if (!props.chartData.totals || !props.chartData.totals.length) return
  const filteredLength = props.chartData.totals.filter(v => v > 0).length
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
const handleWheel = (event) => {
  const filteredLength = props.chartData.totals.filter(v => v > 0).length
  if (!filteredLength) return
  stopCarousel()
  currentIndex.value = event.deltaY > 0
      ? (currentIndex.value + 1) % filteredLength
      : (currentIndex.value - 1 + filteredLength) % filteredLength
  updateChart(currentIndex.value)
}

const initChart = () => {
  if (chart.value) chart.value.dispose()
  if (!chartRef.value) return
  chart.value = markRaw(echarts.init(chartRef.value, 'macarons'))
  currentIndex.value = 0
  updateChart()
  startCarousel()
}

const resizeChart = debounce(() => chart.value?.resize(), 200)

onMounted(async () => {
  await nextTick()
  initChart()
  // 容器变化监听
  resizeObserver = new ResizeObserver(resizeChart)
  resizeObserver.observe(chartRef.value)
  // 窗口变化监听
  window.addEventListener('resize', resizeChart)
})

onBeforeUnmount(() => {
  if (chart.value) chart.value.dispose()
  stopCarousel()
  resizeObserver?.disconnect()
  window.removeEventListener('resize', resizeChart)
})

watch(() => props.chartData, () => initChart(), {deep: true})
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
</style>
