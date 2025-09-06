<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue'
import * as echarts from 'echarts'
import 'echarts/theme/macarons'

const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '400px'},
  chartData: {
    type: Object,
    default: () => ({
      maleData: [80, 150, 200, 120, 90, 60, 20],
      femaleData: [70, 140, 180, 130, 90, 60, 30],
      legendData: ['年龄段总人数', '男性', '女性'],
      indicator: [
        {text: '18以下'},
        {text: '19-30'},
        {text: '31-40'},
        {text: '41-50'},
        {text: '51-60'},
        {text: '60以上'},
        {text: '未知'}
      ]
    })
  }
})

const chart = ref(null)
const chartRef = ref(null)

// 构建 series
const buildSeries = (legendData, indicator, maleData, femaleData) => {
  const ageTotalData = maleData.map((v, i) => v + femaleData[i])
  const totalPeople = ageTotalData.reduce((sum, v) => sum + v, 0)
  const max = Math.max(...ageTotalData, ...maleData, ...femaleData) * 1.1
  indicator.forEach(item => item.max = max)
  const colorArr = ['#4A99FF', '#4BFFFC', '#FFB74A']

  const series = []

  // 总人数、男性、女性系列
  const dataArr = [ageTotalData, maleData, femaleData]
  dataArr.forEach((arr, idx) => {
    series.push({
      name: legendData[idx],
      type: 'radar',
      data: [arr],
      lineStyle: {color: colorArr[idx]},
      areaStyle: {color: colorArr[idx], opacity: idx === 0 ? 0.2 : 0.3},
      itemStyle: {color: colorArr[idx]},
      symbolSize: 6,
      // 主系列tooltip配置 - 仅在点上触发
      tooltip: {
        trigger: 'item'
      }
    })
  })

  // 单点 tooltip 系列
  ageTotalData.forEach((v, i) => {
    dataArr.forEach((arr, seriesIdx) => {
      series.push({
        name: legendData[seriesIdx],
        type: 'radar',
        data: [arr.map((val, j) => j === i ? val : 0)],
        lineStyle: {color: 'transparent'},
        areaStyle: {color: 'transparent'},
        symbolSize: 10,
        itemStyle: {color: colorArr[seriesIdx]},
        // 单点tooltip配置
        tooltip: {
          show: true,
          trigger: 'item',  // 仅在点上触发
          formatter: () => {
            const ageName = indicator[i].text
            const ageTotal = ageTotalData[i]
            const male = maleData[i]
            const female = femaleData[i]
            const agePercent = ((ageTotal / totalPeople) * 100).toFixed(2)
            const malePercent = ((male / ageTotal) * 100).toFixed(2)
            const femalePercent = ((female / ageTotal) * 100).toFixed(2)
            return `${ageName}<br/>
                    ${ageName}总人数: ${ageTotal} (占总人数: ${agePercent}%)<br/>
                    男性人数: ${male} (占年龄段: ${malePercent}%)<br/>
                    女性人数: ${female} (占年龄段: ${femalePercent}%)<br/>
                    总人数: ${totalPeople}`
          }
        },
        z: 10
      })
    })
  })
  return series
}

// 初始化图表
const initChart = () => {
  if (chart.value) {
    chart.value.dispose()
    chart.value = null
  }
  chart.value = echarts.init(chartRef.value, 'macarons')

  const {legendData, indicator, maleData, femaleData} = props.chartData

  const option = {
    color: ['#4A99FF', '#4BFFFC', '#FFB74A'],
    // 全局tooltip配置 - 关键修改
    tooltip: {
      trigger: 'item',  // 仅在数据项上触发
      triggerOn: 'mousemove|click'  // 鼠标移动到点上或点击时触发
    },
    legend: {
      orient: 'vertical',
      icon: 'circle',
      data: legendData,
      bottom: 35,
      right: 40,
      itemWidth: 14,
      itemHeight: 14,
      itemGap: 21,
      textStyle: {fontSize: 14, color: '#00E4FF'}
    },
    radar: {
      name: {textStyle: {color: '#000000', fontSize: 16}},
      indicator: indicator,
      splitArea: {show: true, areaStyle: {color: ['rgba(255,255,255,0)', 'rgba(255,255,255,0)']}},
      axisLine: {lineStyle: {color: '#153269'}},
      splitLine: {lineStyle: {color: '#113865', width: 1}}
    },
    series: buildSeries(legendData, indicator, maleData, femaleData)
  }

  chart.value.setOption(option)
}

// resize
const handleResize = () => chart.value?.resize()

onMounted(() => {
  nextTick(() => {
    initChart()
    window.addEventListener('resize', handleResize)
  })
})

onBeforeUnmount(() => {
  chart.value?.dispose()
  window.removeEventListener('resize', handleResize)
})

// 监听数据变化
watch(() => props.chartData, () => initChart(), {deep: true})
</script>
