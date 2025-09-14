<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup lang="ts">
import {ref, onMounted, onBeforeUnmount, watch, nextTick, computed} from 'vue'
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
      //和datas的values一一对应
      indicators: [
        {text: '18以下'},
        {text: '19-30'},
        {text: '31-40'},
        {text: '41-50'},
        {text: '51-60'},
        {text: '60以上'},
        {text: '未知'}
      ],
      datas: [
        {name: '总人数', values: [0, 310, 410, 260, 185, 123, 52]},
        {name: '男', values: [0, 150, 200, 120, 90, 60, 20]},
        {name: '女', values: [0, 140, 180, 130, 90, 60, 30]},
        {name: '未知', values: [0, 20, 30, 10, 5, 3, 2]}
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
    default: [1] // -1 表示不隐藏
  },
  //默认颜色
  defaultColor: {
    type: Array,
    default: () => ['#4A99FF', '#816d85', '#FFB74A', '#4BFFFC']
  }
})

const chart = ref<echarts.EChartsType | null>(null)
const chartRef = ref<HTMLDivElement | null>(null)

// 检查数组中所有值是否都为0
const allValuesAreZero = (values: number[]): boolean => {
  return values.every(v => Number(v) === 0);
}

// 构建 series
const buildSeries = (indicators, data) => {
  // 先过滤出有效的指标索引（至少有一个系列在该位置不为0）
  const validIndicatorIndexes = []
  indicators.forEach((indicator, index) => {
    const hasValidData = data.some(series => Number(series.values[index]) !== 0)
    if (hasValidData) {
      validIndicatorIndexes.push(index)
    }
  })

  // 如果没有有效指标，返回空数组
  if (validIndicatorIndexes.length === 0) {
    return []
  }

  // 基于有效指标索引，重新构建indicators和数据
  const filteredIndicators = validIndicatorIndexes.map(index => ({...indicators[index]}))
  const filteredData = data.map(series => ({
    name: series.name,
    values: validIndicatorIndexes.map(index => series.values[index])
  }))

  // 计算最大值
  const totalData = filteredData[props.defaultTotalIndex].values;
  const absoluteMax = Math.max(...totalData);
  const total = totalData.reduce((sum, v) => Number(sum) + Number(v), 0)
  // 排除统计总数data，拿到其他data
  const otherData = filteredData.filter((_, index) => index !== props.defaultTotalIndex)

  // 确定好最大值。
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

  // 设置最大值
  filteredIndicators.forEach(item => item.max = max);
  const colorArr = props.defaultColor
  const series = []

  // 数据
  const dataArr = filteredData.map(item => {
    return item.values
  })
  const legendData = filteredData.map(item => item.name)

  dataArr.forEach((arr, idx) => {
    // 优化：如果当前 series 的所有值都为0，则不显示该 series
    if (!allValuesAreZero(arr)) {
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
      });
    }
  });

  // 单点 tooltip 系列
  totalData.forEach((v, i) => {
    // 优化：如果当前 indicator 的所有 data 对应的值都为0，则不生成 tooltip 系列
    const allSeriesForIndicatorAreZero = otherData.every(item => Number(item.values[i]) === 0);
    if (!allSeriesForIndicatorAreZero && Number(totalData[i]) !== 0) {
      dataArr.forEach((arr, seriesIdx) => {
        // 优化：如果当前 series 在此 indicator 上的值为0，则不单独为这个点生成 tooltip 系列
        if (arr[i] !== 0) {
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
                const name = filteredIndicators[i].text
                const currentTotal = totalData[i]
                // 这里用原始total计算百分比，保持数据一致性
                const originalTotal = data[props.defaultTotalIndex].values.reduce((sum, v) => Number(sum) + Number(v), 0)
                const currentPercent = currentTotal === 0 ? 0 : ((currentTotal / originalTotal) * 100).toFixed(2)
                var text = ''
                for (let j = 0; j < otherData.length; j++) {
                  // 优化：只显示非零的数据
                  if (otherData[j].values[i] !== 0) {
                    text = text + `${otherData[j].name}: ${otherData[j].values[i]} (${(otherData[j].values[i] / currentTotal * 100).toFixed(2)}%)<br/>`
                  }
                }

                return `${name}${props.chartName}<br/>
                        ${name}: ${currentTotal} (${currentPercent}%)<br/>
                        ${text}
                        总计: ${originalTotal}`
              }
            },
            z: 10
          })
        }
      })
    }
  })

  return {series, indicators: filteredIndicators}
}

// 初始化图表
const initChart = () => {
  if (!props.chartData || !props.chartData.indicators || !props.chartData.datas) return
  if (chart.value) {
    chart.value.dispose()
    chart.value = null
  }
  chart.value = echarts.init(chartRef.value!, 'macarons')

  const {indicators, datas} = props.chartData
  const legendData = datas.map((d: any) => d.name)

  // 动态生成 legend.selected
  const selectedMap: Record<string, boolean> = {}
  legendData.forEach((name, idx) => {
    selectedMap[name] = !props.defaultHiddenIndex.includes(idx)
  })

  // 构建过滤后的series和indicators
  const buildResult = buildSeries(indicators, datas)

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
        // 这里的 tooltipFormatter 需要根据实际情况调整，因为它目前是针对 radar type 的通用formatter
        // 如果需要更精细的控制，可以参考 buildSeries 中单点 tooltip 的 formatter
        const seriesName = params.seriesName; // 对应 legendData
        const value = params.value;           // 当前 series 的值数组
        let text = `${seriesName}<br/>`;
        value.forEach((v: number, i: number) => {
          // 优化：只显示非零的数据
          if (v !== 0 && buildResult.indicators && buildResult.indicators[i]) {
            const name = buildResult.indicators[i].text;
            text += `${name}: ${v}<br/>`;
          }
        });
        return text;
      }
    },
    legend: {
      orient: 'vertical',
      icon: 'circle',
      data: legendData,
      bottom: 20,
      right: 20,
      itemWidth: 14,
      itemHeight: 14,
      itemGap: 10,
      textStyle: {fontSize: 14, color: '#00E4FF'},
      selected: selectedMap
    },
    radar: {
      name: {textStyle: {color: '#ffffff', fontSize: 16}},
      indicator: buildResult.indicators || [],
      splitArea: {
        show: true,
        areaStyle: {color: ['rgba(255,255,255,0)', 'rgba(255,255,255,0)']}
      },
      axisLine: {lineStyle: {color: '#153269'}},
      splitLine: {lineStyle: {color: '#113865', width: 1}},
      center: ['40%', '50%'],
      radius: '70%'
    },
    series: buildResult.series || []
  }

  chart.value.setOption(option, true)
}

// 自适应窗口大小
const handleResize = () => chart.value?.resize()
let resizeObserver: ResizeObserver | null = null
const observeResize = () => {
  if (!chartRef.value) return
  resizeObserver = new ResizeObserver(() => {
    chart.value?.resize()
  })
  resizeObserver.observe(chartRef.value)
}

onMounted(() => {
  nextTick(() => {
    initChart()
    window.addEventListener('resize', handleResize)
  })
  observeResize()
})

onBeforeUnmount(() => {
  chart.value?.dispose()
  window.removeEventListener('resize', handleResize)
  if (resizeObserver && chartRef.value) {
    resizeObserver.unobserve(chartRef.value);
  }
})

watch(() => props.chartData, () => initChart(), {deep: true})
</script>
