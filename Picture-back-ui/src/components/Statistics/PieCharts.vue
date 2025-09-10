<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue'
import * as echarts from 'echarts'
import 'echarts/theme/macarons' // 引入主题

const props = defineProps({
  className: {
    type: String,
    default: 'chart'
  },
  width: {
    type: String,
    default: '100%'
  },
  height: {
    type: String,
    default: '100%'
  },
  chartName: {
    type: String,
    default: '饼图'
  },
  chartData: {
    type: Array,
    default: () => [
      {name: 'YY', value: 100},
      {name: 'XC', value: 100}
    ]
  }
})

const chart = ref(null) // Statistics 实例
const chartRef = ref(null) // DOM 引用

const getBrightColor = () => {
  const r = Math.floor(Math.random() * 155 + 100) // 100-255
  const g = Math.floor(Math.random() * 155 + 100)
  const b = Math.floor(Math.random() * 155 + 100)
  return `rgb(${r},${g},${b},0.9)`
}
// 初始化图表
const initChart = (data) => {
  if (chart.value) {
    chart.value.dispose()
    chart.value = null
  }


  const coloredData = (data) =>
      data.map(item => ({
        ...item,
        itemStyle: {
          color: getBrightColor()
        }
      }))
  chart.value = echarts.init(chartRef.value, 'macarons')

  const option = {
    title: {
      text: props.chartName,
      textStyle: {
        fontSize: 16,
        color: '#2e95f3'
      },
      top: '5%',
      left: '2%'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    legend: {
      left: 'center',
      bottom: '10%',
      textStyle: {fontSize: 14, color: '#ffffff'}
    },
    series: [
      {
        name: props.chartName,
        type: 'pie',
        roseType: 'radius',
        top: '10%',
        radius: [20, 95],
        center: ['50%', '38%'],
        label: {
          formatter: '{b}',
          textStyle: {
            fontSize: 14,
            color: '#2e95f3'
          }
        },
        data: coloredData(data || props.chartData),
        animationEasing: 'cubicInOut',
        animationDuration: 2600
      }
    ]
  }

  chart.value.setOption(option)
}

// resize
const handleResize = () => {
  chart.value?.resize()
}

// 挂载
onMounted(() => {
  nextTick(() => {
    initChart(props.chartData)
    window.addEventListener('resize', handleResize)
  })
})

// 卸载
onBeforeUnmount(() => {
  if (chart.value) {
    chart.value.dispose()
    chart.value = null
  }
  window.removeEventListener('resize', handleResize)
})

// 监听数据变化
watch(
    () => props.chartData,
    (newData) => {
      initChart(newData)
    },
    {deep: true}
)
</script>
