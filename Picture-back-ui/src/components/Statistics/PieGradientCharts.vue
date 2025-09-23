<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue';
import * as echarts from 'echarts';
import 'echarts/theme/macarons';

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
      {name: '意外险', value: 450},
      {name: '医疗险', value: 600},
      {name: '资金型', value: 1000},
      {name: '寿险', value: 870},
      {name: '重疾险', value: 260},
      {name: '团财险', value: 180}
    ]
  },
});

const chart = ref(null);
const chartRef = ref(null);

const initChart = (data) => {
  if (!chartRef.value) return;

  if (chart.value) {
    chart.value.dispose();
    chart.value = null;
  }

  // 辅助函数：将数据与占位符数据混合
  const mixedData = [];
  const totalValue = data.reduce((sum, item) => Number(sum) + Number(item.value), 0);
  const placeHolderStyle = {
    normal: {
      label: {show: false},
      labelLine: {show: false},
      color: 'rgba(0, 0, 0, 0)',
      borderColor: 'rgba(0, 0, 0, 0)',
      borderWidth: 0
    }
  };
  const rich = {
    white: {
      color: '#ddd',
      align: 'center',
      padding: [5, 0]
    }
  };

  for (const item of data) {
    mixedData.push({
      value: item.value,
      name: item.name,
      itemStyle: {
        normal: {
          borderWidth: 5,
          shadowBlur: 5,
          borderColor: new echarts.graphic.LinearGradient(0, 0, 1, 1, [{
            offset: 0,
            color: '#7777eb'
          }, {
            offset: 1,
            color: '#70ffac'
          }]),
          shadowColor: 'rgba(142, 152, 241, 0.6)'
        }
      }
    }, {
      value: totalValue * 0.04, // 动态计算占位符大小
      name: '',
      itemStyle: placeHolderStyle
    });
  }

  chart.value = echarts.init(chartRef.value, 'macarons');

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
    grid: {
      left: '5%',
      top: '20%',
      bottom: '20%',
    },
    tooltip: {
      show: true,
      trigger: 'item',
      backgroundColor: 'transparent',
      borderColor: 'transparent',
      textStyle: {
        color: '#fff'
      },
      formatter: function (params) {
        if (params.name !== '') {
          const percent = ((params.value / totalValue) * 100).toFixed(2);
          return `${params.name}: ${params.value} <br/>占比: ${percent}% <br/>总数: ${totalValue}`;
        } else {
          return null;
        }
      }
    },
    legend: {
      show: true,
      icon: 'circle',
      orient: 'vertical',
      right: '1%',
      bottom: '5%',
      height: 200,
      textStyle: {
        color: '#fff'
      },
      data: data.map(item => item.name)
    },
    toolbox: {
      show: false
    },
    series: [{
      name: '',
      type: 'pie',
      clockWise: false,
      radius: [80,85],
      hoverAnimation: false,
      itemStyle: {
        normal: {
          label: {
            show: true,
            position: 'outside',
            color: '#ddd',
            formatter: function (params) {
              if (params.name !== '') {
                const percent = ((params.value / totalValue) * 100).toFixed(0);
                return params.name + '\n{white|' + percent + '%}';
              } else {
                return '';
              }
            },
            rich: rich
          },
          labelLine: {
            show: false
          }
        }
      },
      data: mixedData
    }]
  };

  chart.value.setOption(option);
};

// 处理窗口大小变化
const handleResize = () => {
  chart.value?.resize();
};

// 监听 props.chartData 的变化，当数据改变时重新渲染图表
watch(
    () => props.chartData,
    (newData) => {
      if (newData && newData.length > 0) {
        nextTick(() => {
          initChart(newData);
        });
      }
    },
    {deep: true, immediate: true}
);

// 组件挂载时执行
onMounted(() => {
  window.addEventListener('resize', handleResize);
});

// 组件卸载前执行，清理资源
onBeforeUnmount(() => {
  if (chart.value) {
    chart.value.dispose();
    chart.value = null;
  }
  window.removeEventListener('resize', handleResize);
});
</script>
