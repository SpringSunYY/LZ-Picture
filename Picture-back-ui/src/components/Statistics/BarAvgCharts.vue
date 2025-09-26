<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"></div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue';
import * as echarts from 'echarts';

const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '100%'},
  autoResize: {type: Boolean, default: true},
  chartData: {
    type: Object,
    default: () => ({
      // 默认数据（外部可以覆盖）
      names: [
        '2021/09/01', '2021/09/02', '2021/09/03', '2021/09/04', '2021/09/05',
        '2021/09/06', '2021/09/07', '2021/09/08', '2021/09/09', '2021/09/10',
        '2021/09/11', '2021/09/12'
      ],
      values: [67, 97, 80, 76, 52, 63, 24, 97, 56, 78, 84, 45]
    })
  },
  chartName: {type: String, default: '实际完工数统计'},
  chartTitle: {type: Array, default: () => ['每日新增', '趋势']}
});

const chartRef = ref(null);
let chart = null;

// 初始化图表
const initChart = async () => {
  await nextTick();
  if (!chartRef.value) return;
  if (chart) {
    chart.dispose();
    chart = null;
  }
  chart = echarts.init(chartRef.value);
  setOptions();
};

// 设置 ECharts 配置
const setOptions = () => {
  if (!chart) return;
  if (!props.chartData || !props.chartData.names || !props.chartData.values) return;
  const xData = props.chartData.names;
  const yData = props.chartData.values;

  // 总和 & 平均值
  const totalSum = yData.reduce((a, b) => Number(a) + Number(b), 0);
  const averageValue = totalSum / yData.length;

  const option = {
    title: {
      text: props.chartName,
      left: "2%",
      top: "2%",
      textStyle: {
        color: "#fff",
        fontSize: 18
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {type: 'shadow'},
      formatter: function (params) {
        const xAxisCategory = params[0].axisValue;
        const currentValue = params[0].value;

        const currentIndex = xData.indexOf(xAxisCategory);
        let percentageChange = '0';
        if (currentIndex > 0) {
          const lastValue = yData[currentIndex - 1];
          if (lastValue !== 0) {
            percentageChange = currentValue - lastValue;
          } else if (currentValue > 0) {
            percentageChange = '+';
          } else if (currentValue < 0) {
            percentageChange = '-∞';
          }
        }

        return `
           ${xAxisCategory}<br/>
          ${currentValue}(${percentageChange})<br/>
          <hr style="border-color: #45D0E3; border-width: 0.5px; margin: 5px 0;">
          总计 : ${totalSum}<br/>
          平均值 : ${averageValue.toFixed(2)}<br/>
        `;
      },
      backgroundColor: 'rgba(6,37,68,0.8)',
      borderColor: '#45D0E3',
      textStyle: {color: '#45D0E3'}
    },
    grid: {left: '20px', right: '20px', bottom: '60px', top: '16%', containLabel: true},
    legend: {
      left: 'center',
      top: '5%',
      textStyle: {color: '#45D0E3'},
      itemWidth: 15,
      itemHeight: 10,
      itemGap: 25,
    },
    dataZoom: [
      {
        type: 'slider',
        show: true,
        xAxisIndex: [0],
        start: 0,
        end: 100,
        backgroundColor: '#0A2D4F',
        fillerColor: '#45D0E380',
        borderColor: '#45D0E3',
        handleStyle: {color: '#45D0E3', borderColor: '#45D0E3'},
        textStyle: {color: '#45D0E3'},
        labelFormatter: value => xData[value] || value,
      },
      {type: 'inside', xAxisIndex: [0], start: 0, end: 100}
    ],
    xAxis: {
      type: 'category',
      data: xData,
      axisLine: {lineStyle: {color: '#45D0E3'}},
      axisTick: {alignWithLabel: true, lineStyle: {color: '#45D0E3'}},
      axisLabel: {color: '#45D0E3', fontSize: 14}
    },
    yAxis: {
      type: 'value',
      axisLabel: {color: '#45D0E3'},
      splitLine: {lineStyle: {type: 'dashed', color: '#45D0E3'}}
    },
    series: [
      {
        name: props.chartTitle[0],
        type: 'bar',
        data: yData,
        barWidth: 10,
        itemStyle: {color: '#45D0E3', borderRadius: [5, 5, 0, 0]},
        label: {show: true, position: 'top', color: '#45D0E3', fontSize: 12},
        markLine: {
          symbol: ['none', 'arrow'],
          symbolSize: 8,
          silent: true,
          lineStyle: {color: '#FFD700', width: 2},
          data: [{type: 'average', name: '平均值'}]
        }
      },
      {
        name: props.chartTitle[1],
        type: 'line',
        data: yData,
        smooth: true,
        symbol: 'circle',
        symbolSize: 5,
        itemStyle: {color: '#45D0E3'},
        lineStyle: {color: '#45D0E3'}
      }
    ]
  };

  chart.setOption(option);
};

// resize
const resizeChart = () => chart?.resize();

onMounted(() => {
  initChart();
  if (props.autoResize) {
    window.addEventListener('resize', resizeChart);
  }
});

onBeforeUnmount(() => {
  chart?.dispose();
  chart = null;
  if (props.autoResize) {
    window.removeEventListener('resize', resizeChart);
  }
});

watch(
    () => props.chartData,
    () => {
      setOptions();
    },
    {deep: true}
);

watch([() => props.width, () => props.height], () => {
  nextTick(() => resizeChart());
});
</script>

<style scoped>
.chart {
  overflow: hidden;
}
</style>
