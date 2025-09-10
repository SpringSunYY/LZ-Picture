<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"></div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue'
import * as echarts from 'echarts'
import 'echarts/theme/macarons' // 引入主题

const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '100%'},
  autoResize: {type: Boolean, default: true},
  chartData: {
    type: Object,
    default: () => ({
      chartXData: ['2024-10-01', '2024-10-02', '2024-10-03', '2024-10-04', '2024-10-05', '2024-10-06', '2024-10-07', '2024-10-08', '2024-10-09', '2024-10-10', '2024-10-11', '2024-10-12', '2024-10-13', '2024-10-14', '2024-10-15', '2024-10-16', '2024-10-17', '2024-10-18', '2024-10-19', '2024-10-20', '2024-10-21', '2024-10-22', '2024-10-23', '2024-10-24', '2024-10-25', '2024-10-26', '2024-10-27', '2024-10-28', '2024-10-29', '2024-10-30', '2024-10-31'],
      chartYData: [
        {
          value: [509, 917, 2455, 2610, 2719, 3033, 3044, 3085, 2708, 2809, 2117, 2000, 1455, 1210, 719, 733, 944, 2285, 2208, 3372, 3936, 3693, 2962, 2810, 3519, 2455, 2610, 2719, 2484, 2078, 5000],
          name: '用户注册'
        }
      ]
    })
  },
  chartName: {type: String, default: '折线图'}
})

const chartRef = ref(null)
let chart = null

// 初始化图表
const initChart = async () => {
  await nextTick()
  if (!chartRef.value) return

  if (chart) {
    chart.dispose()
    chart = null
  }

  chart = echarts.init(chartRef.value, 'macarons')
  setOptions()
}

// 设置配置
const setOptions = () => {
  if (!chart) return

  const xData = props.chartData.chartXData || []
  const seriesData = (props.chartData.chartYData || []).map(yDatum => ({
    name: yDatum.name || '',
    type: 'line',
    symbol: 'circle',
    symbolSize: 10,
    data: yDatum.value || [],
    markPoint: {
      data: [],
      label: {textStyle: {color: '#fff'}}
    }
  }))

  chart.setOption({
    backgroundColor: 'transparent',
    title: {
      text: props.chartName,
      left: '6%',
      top: '6%',
      textStyle: {color: '#ffffff', fontSize: 16}
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {type: 'line', label: {color: '#fff'}}
    },
    grid: {top: 80, bottom: 100, left: 60, right: 30, backgroundColor: 'transparent'},
    legend: {top: '11%', left: 'center', textStyle: {color: '#90979c'}},
    xAxis: [{
      type: 'category',
      axisLine: {lineStyle: {color: 'rgba(255,255,255,0.73)'}},
      splitLine: {show: false},
      axisTick: {show: false},
      data: xData,
      splitArea: {show: false},   // 取消交替底色
    }],
    yAxis: [{
      type: 'value',
      splitLine: {show: false},
      axisLine: {lineStyle: {color: 'rgb(255,255,255)'}},
      splitArea: {show: false},   // 取消交替底色
    }],
    dataZoom: [
      {
        type: 'slider',
        show: true,
        xAxisIndex: [0],
        bottom: 50,
        height: 30,
        start: 10,
        end: 80,
        handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
        handleSize: '110%',
        handleStyle: {color: '#5B3AAE'},
        fillerColor: 'rgba(67,55,160,0.4)',
        borderColor: 'rgba(204,187,225,0.5)'
      },
      {
        type: 'inside',
        start: 10,
        end: 80
      }
    ],
    series: seriesData
  })
}

// 窗口 resize
const resizeChart = () => chart?.resize()

onMounted(() => {
  initChart()
  if (props.autoResize) window.addEventListener('resize', resizeChart)
})

onBeforeUnmount(() => {
  chart?.dispose()
  chart = null
  if (props.autoResize) window.removeEventListener('resize', resizeChart)
})

// 监听数据变化
watch(
    () => props.chartData,
    () => {
      setOptions()
    },
    {deep: true}
)
</script>

<style scoped>
.chart {
  overflow: hidden;
}
</style>
