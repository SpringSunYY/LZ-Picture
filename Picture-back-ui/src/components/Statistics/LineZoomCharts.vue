<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"></div>
</template>

<script setup>
import {nextTick, onBeforeUnmount, onMounted, ref, watch} from 'vue'
import * as echarts from 'echarts'
import 'echarts/theme/macarons'

const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '100%'},
  autoResize: {type: Boolean, default: true},
  chartData: {
    type: Object,
    default: () => ({
      names: ['2024-10-01', '2024-10-02', '2024-10-03'],
      values: [{values: [509, 917, 2455], name: '用户注册'}]
    })
  },
  chartName: {type: String, default: '折线图'},
  showAvgLine: {type: Boolean, default: true}
})

const chartRef = ref(null)
let chart = null
const selectedMap = ref({})

const initChart = async () => {
  await nextTick()
  if (!chartRef.value) return
  if (chart) chart.dispose()
  chart = echarts.init(chartRef.value, 'macarons')

  // 监听图例切换
  chart.on('legendselectchanged', (params) => {
    selectedMap.value = params.selected
    setOptions()
  })
  setOptions()
}

const setOptions = () => {
  if (!chart) return
  const {names: xData, values: rawValues} = props.chartData
  if (!rawValues?.length || !xData?.length) return

  // 1. 获取可见系列用于计算平均值
  const activeSeries = rawValues.filter(s => selectedMap.value[s.name] !== false)
  // 2. 计算平均线（只有 showAvgLine 为 true 且至少 2 条可见数据时才计算）
  const avgValues = (props.showAvgLine && activeSeries.length >= 2)
    ? xData.map((_, index) => {
        const sum = activeSeries.reduce((acc, curr) => acc + (Number(curr.values[index]) || 0), 0)
        return Number((sum / activeSeries.length).toFixed(2))
      })
    : []

  // 3. 构造 Series（核心：每次渲染都生成全新的数组，杜绝重复）
  const avgSeriesObj = {
    name: '全线平均',
    type: 'line',
    smooth: true,
    symbolSize: 8,
    lineStyle: {width: 3, type: [20, 5], color: '#FFD700'},
    itemStyle: {color: '#FFD700'},
    data: avgValues,
    z: 10,
    markLine: {
      silent: true,
      symbol: 'none',
      label: {
        position: 'end', formatter: '总均值', color: '#FFD700',
        fontSize: 12,
        fontWeight: 'bold'
      },
      data: [{type: 'average', name: '平均值'}]
    }
  }

  const finalSeries = [
    ...rawValues.map(yDatum => ({
      name: yDatum.name,
      type: 'line',
      symbol: 'circle',
      symbolSize: 8,
      data: yDatum.values,
    })),
    ...(avgValues.length > 0 ? [avgSeriesObj] : [])
  ]

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
      axisPointer: {type: 'line', label: {color: '#fff'}},
      formatter: function (params) {
        if (!params || params.length === 0) return '';

        // 1. 去重逻辑（处理 markLine 引起的重复）
        const uniqueParams = [];
        const map = new Map();
        for (const item of params) {
          if (!map.has(item.seriesName)) {
            map.set(item.seriesName, true);
            uniqueParams.push(item);
          }
        }

        // 标题
        let relVal = `<div style="margin-bottom:8px; font-weight:bold; border-bottom:1px solid rgba(255,255,255,0.3); padding-bottom:4px; color:#fff;">${uniqueParams[0].name}</div>`;

        const isSingleSeries = uniqueParams.length === 1;

        uniqueParams.forEach(item => {
          const dataIndex = item.dataIndex;
          const seriesName = item.seriesName;
          const currentValue = item.value;

          // 2. 找到该系列完整数据并计算周期总计
          const target = finalSeries.find(s => s.name === seriesName);
          const allValues = target ? target.data : [];
          const totalSum = allValues.reduce((acc, curr) => acc + (Number(curr) || 0), 0);
          const avg = totalSum / allValues.length;
          // 3. 计算环比逻辑
          let changeText = '';
          if (dataIndex > 0) {
            const prevValue = allValues[dataIndex - 1];
            const diff = (currentValue - prevValue);
            let ratio = (prevValue && prevValue !== 0) ? ((diff / Math.abs(prevValue)) * 100).toFixed(1) + '%' : '100%';
            const colorStyle = diff > 0 ? '#ff4d4f' : (diff < 0 ? '#52c41a' : '#ccc');
            const symbol = diff > 0 ? '▲' : (diff < 0 ? '▼' : '-');
            changeText = `<span style="color:${colorStyle}; font-size:11px;">${symbol} ${Math.abs(diff).toFixed(0)} (${ratio})</span>`;
          } else {
            changeText = `<span style="color:#999; font-size:11px;">--</span>`;
          }

          const isAvg = seriesName === '全线平均';

          // 4. 单线时简洁展示，多线时横向排列
          if (isSingleSeries) {
            relVal += `
              <div style="margin-top:6px; line-height:1.8;">
                <div>
                  <span style="display:inline-block;margin-right:6px;border-radius:50%;width:8px;height:8px;background-color:${item.color};"></span>
                  <span style="${isAvg ? 'color:#FFD700; font-weight:bold;' : 'color:#ddd;'}">${seriesName}:</span>
                  <b style="margin-left:6px; color:#fff;">${currentValue}</b>
                </div>
                <div style="color:#aaa; font-size:11px; padding-left:14px;">总计: <span style="color:#eee;">${totalSum.toLocaleString()}</span> &nbsp; 平均: <span style="color:#eee;">${avg.toLocaleString()}</span> &nbsp; ${changeText}</div>
              </div>`;
          } else {
            relVal += `
            <div style="display:flex; justify-content:space-between; align-items:center; margin-top:6px; min-width:320px;">
              <span style="display:flex; align-items:center; flex: 1;">
                <span style="display:inline-block;margin-right:8px;border-radius:50%;width:8px;height:8px;background-color:${item.color};"></span>
                <span style="${isAvg ? 'color:#FFD700; font-weight:bold;' : 'color:#ddd;'}">${seriesName}:</span>
                <b style="margin-left:8px; color:#fff;">${currentValue}</b>
              </span>

              <span style="color:#aaa; font-size:11px; margin: 0 15px; white-space:nowrap;">
                总计: <span style="color:#eee;">${totalSum.toLocaleString()}</span>
              </span>
              <span style="color:#aaa; font-size:11px; margin: 0 15px; white-space:nowrap;">
                平均: <span style="color:#eee;">${avg.toLocaleString()}</span>
              </span>

              ${changeText}
            </div>`;
          }
        });
        return relVal;
      }
    },
    grid: {left: '6%', right: '6%', bottom: '18%', top: '18%'},
    legend: {
      top: '2%',
      left: 'center',
      textStyle: {color: '#90979c'},
      selected: selectedMap.value
    },
    xAxis: [{
      type: 'category',
      data: xData,
      splitLine: {show: false},
      axisLine: {show: false}, // 完全隐藏轴线
      axisTick: {show: false},
      axisLabel: {color: '#ffffff'},
      backgroundColor: 'transparent',
      splitArea: {show: false} // 隐藏分割区域
    }],
    yAxis: [{
      type: 'value',
      splitLine: {show: false},
      axisLine: {show: false}, // 完全隐藏轴线
      axisTick: {show: false},
      axisLabel: {color: '#ffffff'},
      backgroundColor: 'transparent',
      splitArea: {show: false} // 隐藏分割区域
    }],
    dataZoom: [{type: 'slider', bottom: '3%', height: '8%', start: 10, end: 80}, {type: 'inside'}],
    series: finalSeries
  }, true) // <--- 必须为 true，防止合并旧数据
}

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

watch(() => props.chartData, () => {
  setOptions()
}, {deep: true})
</script>

<style scoped>
.chart {
  overflow: hidden;
}
</style>
