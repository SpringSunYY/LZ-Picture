<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup lang="ts">
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue'
import * as echarts from 'echarts'

// Props
const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '100%'},
  chartName: {type: String, default: '用户在线统计'},
  total: {type: Number, default: 1200},
  count: {type: Number, default: 1000},
  seed: {type: Number, default: 2} // 旋转速度倍率
})

// echarts 实例
const chart = ref<echarts.EChartsType | null>(null)
const chartRef = ref<HTMLDivElement | null>(null)

let angle = 0
let timer: number | null = null

// 动态计算字号
const getFontSize = (ratio: number) => {
  if (!chartRef.value) return 14
  const size = Math.min(chartRef.value.clientWidth, chartRef.value.clientHeight)
  return Math.round(size * ratio)
}

// 初始化图表
const initChart = () => {
  if (chart.value) {
    chart.value.dispose()
    chart.value = null
  }
  chart.value = echarts.init(chartRef.value as HTMLDivElement)

  const option: echarts.EChartsOption = {
    title: {
      text: props.chartName,
      left: '0.5%',
      top: '0.5%',
      textStyle: {color: '#fff', fontSize: `${getFontSize(0.1)}px sans-serif`}
    },
    graphic: {
      elements: [
        {
          type: 'text',
          left: 'center',
          top: '2%',
          style: {text: '0', fill: '#fff', font: `${getFontSize(0.05)}px sans-serif`}
        },
        {
          type: 'text',
          top: 'middle',
          right: '2%',
          style: {text: '25', fill: '#fff', font: `${getFontSize(0.05)}px sans-serif`}
        },
        {
          type: 'text',
          left: 'center',
          bottom: '1%',
          style: {text: '50', fill: '#fff', font: `${getFontSize(0.05)}px sans-serif`}
        },
        {
          type: 'text',
          left: '2%',
          top: 'middle',
          style: {text: '75', fill: '#fff', font: `${getFontSize(0.05)}px sans-serif`}
        }
      ]
    },
    series: [
      // 外圈动态
      {
        type: 'pie',
        radius: ['99%', '100%'],
        startAngle: angle,
        clockwise: false,
        emphasis: {scale: false},
        animation: false,
        label: {show: false},
        itemStyle: {color: ({dataIndex}) => (dataIndex === 0 || dataIndex === 2 ? 'transparent' : '#3BB9BA')},
        data: [{value: 65}, {value: 15}, {value: 5}, {value: 15}]
      },
      // 外圈内边框
      {
        type: 'pie',
        radius: ['86%', '86.5%'],
        silent: true,
        animation: false,
        label: {show: false},
        itemStyle: {color: '#285D73'},
        data: [100]
      },
      // 内圈动态
      {
        type: 'pie',
        radius: ['86.5%', '87.5%'],
        startAngle: -angle,
        clockwise: true,
        emphasis: {scale: false},
        animation: false,
        label: {show: false},
        itemStyle: {color: ({dataIndex}) => (dataIndex % 2 === 0 ? '#56B4D9' : 'transparent')},
        data: Array(6).fill({value: 100})
      },
      // 内圈背景
      {
        type: 'pie',
        radius: ['72.5%', '80.5%'],
        silent: true,
        animation: false,
        label: {show: false},
        itemStyle: {color: '#1D303F'},
        data: [100]
      },
      // 刻度条
      {
        type: 'gauge',
        radius: '80%',
        startAngle: 90,
        endAngle: -269.9999,
        pointer: {show: false},
        axisLine: {
          show: true,
          lineStyle: {
            width: 25,
            color: [
              [props.count / props.total, 'rgba(0,255,255,0.34)'],
              [1, '#1D303F']
            ]
          }
        },
        axisTick: {show: false},
        splitLine: {
          show: true,
          distance: -26,
          length: 27,
          lineStyle: {color: 'rgba(29,48,63,0.07)', width: 13}
        },
        axisLabel: {show: false}
      },
      // 中心数值
      {
        type: 'pie',
        radius: ['68%'],
        silent: true,
        animation: false,
        itemStyle: {color: 'rgba(20,43,52,0)'},
        label: {
          show: true,
          position: 'center',
          formatter: `{count|${props.count}}\n{desc|当前在线用户}\n{totalDesc|总数 }{total|${props.total}}`,
          rich: {
            count: {color: '#00eaff', fontSize: getFontSize(0.15), fontWeight: 'bold'},
            desc: {color: '#fff', fontSize: getFontSize(0.07), padding: [0, 0, 20, 0]},
            totalDesc: {color: 'rgba(255,255,255,0.68)', fontSize: getFontSize(0.06)},
            total: {fontSize: getFontSize(0.06), color: '#AACBFF'}
          }
        },
        data: [100]
      }
    ]
  }

  chart.value.setOption(option)

  // 启动旋转动画（只改角度）
  if (timer) clearInterval(timer)
  timer = window.setInterval(() => {
    angle = (angle + props.seed) % 360
    chart.value?.setOption({
      series: [
        {startAngle: angle},
        {},
        {startAngle: -angle}
      ]
    } as echarts.EChartsOption, false, false) // 不合并，不触发动画
  }, 30)
}

// resize
const handleResize = () => {
  chart.value?.resize()
  initChart()
}

onMounted(() => {
  nextTick(() => {
    initChart()
    window.addEventListener('resize', handleResize)
  })
})

onBeforeUnmount(() => {
  if (timer) clearInterval(timer)
  window.removeEventListener('resize', handleResize)
  chart.value?.dispose()
  chart.value = null
})

watch(() => [props.count, props.total], () => {
  initChart()
})
</script>
