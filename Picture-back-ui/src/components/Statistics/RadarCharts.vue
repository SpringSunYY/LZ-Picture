<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup lang="ts">
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue'
import * as echarts from 'echarts'
import 'echarts/theme/macarons'

const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '100%'},
  chartName: {type: String, default: '年龄段人数占比'},
  chartData: {
    type: Object,
    default: () => ({
      indicator: [
        {text: '18以下'},
        {text: '19-30'},
        {text: '31-40'},
        {text: '41-50'},
        {text: '51-60'},
        {text: '60以上'},
        {text: '未知'}
      ],
      data: [
        {name: '总人数', data: [160, 310, 410, 260, 185, 123, 52]},
        {name: '男', data: [80, 150, 200, 120, 90, 60, 20]},
        {name: '女', data: [70, 140, 180, 130, 90, 60, 30]},
        {name: '未知', data: [10, 20, 30, 10, 5, 3, 2]}
      ]
    })
  },
  //默认总数索引
  defaultTotalIndex: {
    type: Number,
    default: 0
  },
  // 默认隐藏的索引
  defaultHiddenIndex: {
    type: Array,
    default: [-1] // -1 表示不隐藏
  },
  //默认颜色
  defaultColor: {
    type: Array,
    default: () => ['#4A99FF', '#4BFFFC', '#FFB74A', '#816d85', '#FF4A4A']
  }
})

const chart = ref<echarts.EChartsType | null>(null)
const chartRef = ref<HTMLDivElement | null>(null)

// 构建 series
const buildSeries = (indicator, data) => {
  // 计算最大值
  // 查找所有数据绝对最大值
  const totalData = data[props.defaultTotalIndex].data;
  const absoluteMax = Math.max(...totalData);
  const total = totalData.reduce((sum, v) => sum + v, 0)
  //排除统计总数data，拿到其他data
  const otherData = data.filter((_, index) => index !== props.defaultTotalIndex)
  //确定好最大值。
  let max;
  if (absoluteMax < 10) {
    max = Math.ceil(absoluteMax) + 1; // 添加一个小缓冲区
  } else if (absoluteMax < 100) {
    max = Math.ceil(absoluteMax / 10) * 10; // 10
  } else if (absoluteMax < 500) {
    max = Math.ceil(absoluteMax / 50) * 50; //50
  } else {
    max = Math.ceil(absoluteMax / 100) * 100; //100
  }

  // 如果最大值为 0 或非常小，确保始终有一个合理的缓冲区
  if (max <= absoluteMax) {
    max = absoluteMax + Math.max(1, Math.ceil(absoluteMax * 0.01));
  }

  //设置最大值
  indicator.forEach(item => item.max = max);
  const colorArr = props.defaultColor
  const series = []

  // 数据
  const dataArr = data.map(item => {
    return item.data
  })
  const legendData = data.map(item => item.name)
  dataArr.forEach((arr, idx) => {
    series.push({
      name: legendData[idx],
      type: 'radar',
      data: [arr],
      lineStyle: {color: colorArr[idx]},
      areaStyle: {color: colorArr[idx], opacity: idx === 0 ? 0.2 : 0.3},
      itemStyle: {color: colorArr[idx]},
      symbolSize: 6,
      tooltip: {
        trigger: 'item'
      }
    })
  })
  // 单点 tooltip 系列
  totalData.forEach((v, i) => {
    dataArr.forEach((arr, seriesIdx) => {
      series.push({
        name: legendData[seriesIdx],
        type: 'radar',
        data: [arr.map((val, j) => j === i ? val : 0)],
        lineStyle: {color: 'transparent'},
        areaStyle: {color: 'transparent'},
        symbolSize: 10,
        itemStyle: {color: colorArr[seriesIdx]},
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: () => {
            const name = indicator[i].text
            // console.log(name)
            const currentTotal = totalData[i]
            const currentPercent = currentTotal === 0 ? 0 : ((currentTotal / total) * 100).toFixed(2)
            var text = ''
            for (let j = 0; j < otherData.length; j++) {
              text = text + `${otherData[j].name}: ${otherData[j].data[i]} (${(otherData[j].data[i] / currentTotal * 100).toFixed(2)}%)<br/>`
            }

            return `${name}${props.chartName}<br/>
                    ${name}: ${currentTotal} (${currentPercent}%)<br/>
                    ${text}
                    总计: ${total}`
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
  chart.value = echarts.init(chartRef.value!, 'macarons')

  const {indicator, data} = props.chartData
  const legendData = data.map((d: any) => d.name)

  // 动态生成 legend.selected
  const selectedMap: Record<string, boolean> = {}
  legendData.forEach((name, idx) => {
    selectedMap[name] = !props.defaultHiddenIndex.includes(idx)
  })

  const option = {
    title: {
      text: props.chartName,
      textStyle: {color: '#00E4FF', fontSize: 16},
      top: '5%',
      left: '2%'
    },
    tooltip: {
      trigger: 'item',
      formatter: (params: any) => {
        const seriesName = params.seriesName; // 对应 legendData
        const value = params.value;           // 当前 series 的值数组
        let text = `${seriesName}<br/>`;
        value.forEach((v: number, i: number) => {
          const name = indicator[i].text;
          text += `${name}: ${v}<br/>`;
        });
        return text;
      }
    },
    legend: {
      orient: 'vertical',
      icon: 'circle',
      data: legendData,
      bottom: 30,
      right: 20,
      itemWidth: 14,
      itemHeight: 14,
      itemGap: 21,
      textStyle: {fontSize: 14, color: '#00E4FF'},
      selected: selectedMap
    },
    radar: {
      name: {textStyle: {color: '#ffffff', fontSize: 16}},
      indicator,
      splitArea: {
        show: true,
        areaStyle: {color: ['rgba(255,255,255,0)', 'rgba(255,255,255,0)']}
      },
      axisLine: {lineStyle: {color: '#153269'}},
      splitLine: {lineStyle: {color: '#113865', width: 1}},
      center: ['40%', '50%'],
      radius: '70%'
    },
    series: buildSeries(indicator, data)
  }

  chart.value.setOption(option, true)
}

// 自适应窗口大小
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

watch(() => props.chartData, () => initChart(), {deep: true})
</script>
