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

const chart = ref(null) // 图表实例
const chartRef = ref(null) // DOM 引用

// 生成明亮的随机颜色
const getBrightColor = () => {
  const r = Math.floor(Math.random() * 155 + 150) // 200-255之间
  const g = Math.floor(Math.random() * 155 + 150)
  const b = Math.floor(Math.random() * 155 + 150)
  return `rgb(${r},${g},${b},0.9)`
}

// 计算数据总和
const calculateTotal = (data) => {
  return data.reduce((sum, item) => Number(sum) + (Number(item.value) || 0), 0)
}

// 初始化图表
const initChart = (data) => {
  if (!data || !data.length) {
    return
  }

  // 销毁已有实例
  if (chart.value) {
    chart.value.dispose()
    chart.value = null
  }

  // 为数据项添加颜色
  const coloredData = data.map(item => ({
    ...item,
    // itemStyle: {
    //   color: getBrightColor()
    // }
  }))

  // 计算总数
  const total = calculateTotal(data)

  // 初始化图表
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
      // 格式化tooltip，添加总数显示
      formatter: function(params) {
        return `${params.seriesName} <br/>${params.name} : ${params.value} (${params.percent.toFixed(1)}%)<br/>总数: ${total}`
      }
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
        data: coloredData,
        animationEasing: 'cubicInOut',
        animationDuration: 2600
      }
    ]
  }

  chart.value.setOption(option)
}

// 处理窗口大小变化
const handleResize = () => {
  chart.value?.resize()
}

// 组件挂载时初始化
onMounted(() => {
  nextTick(() => {
    initChart(props.chartData)
    window.addEventListener('resize', handleResize)
  })
})

// 组件卸载时清理
onBeforeUnmount(() => {
  if (chart.value) {
    chart.value.dispose()
    chart.value = null
  }
  window.removeEventListener('resize', handleResize)
})

// 监听数据变化，重新渲染图表
watch(
  () => props.chartData,
  (newData) => {
    initChart(newData)
  },
  {deep: true}
)
</script>
