<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup lang="ts">
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue';
import * as echarts from 'echarts';
import 'echarts-liquidfill'; // 引入 liquidfill 插件

const props = defineProps({
  className: {
    type: String,
    default: 'chart',
  },
  width: {
    type: String,
    default: '100%',
  },
  height: {
    type: String,
    default: '100%',
  },
  total: {
    type: Number,
    default: 100,
  },
  current: {
    type: Number,
    default: 20, // 初始值设为 0，以确保计算正确
  },
  chartName: {type: String, default: 'xxx占比'},
});

const chartRef = ref<HTMLDivElement | null>(null);
let myChartInstance: echarts.EChartsType | null = null;

// 绘制图表的函数
const drawChart = () => {
  if (!chartRef.value) return;

  // 如果 ECharts 实例已存在，先销毁
  if (myChartInstance) {
    myChartInstance.dispose();
  }

  myChartInstance = echarts.init(chartRef.value as HTMLDivElement);

  // 计算填充百分比，确保不会出现 NaN 或 Infinity
  const percentage = props.total > 0 ? props.current / props.total : 0;
  const displayPercentage = (percentage * 100).toFixed(0);

  const option: echarts.EChartsOption = {
    title: {
      text: props.chartName,
      left: "2%",
      top: "2%",
      textStyle: {
        color: "#fff",
        fontSize: 16,
        fontWeight: "normal",
      }
    },
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(37,37,36,0.5)', // Tooltip 背景色
      borderWidth: 0,
      textStyle: {
        color: '#ffffff',
      },
      formatter: function (params: any) {
        // params.value 是 liquidFill 的 data 值 (0-1 之间)
        // 这里我们用 props.current 和 props.total 来展示
        const currentVal = Number(props.current);
        const totalVal = Number(props.total);
        const calculatedPercentage = totalVal > 0 ? (currentVal / totalVal * 100).toFixed(0) : '0';

        // params.seriesName 是 '预估量'
        return `${params.seriesName}<br/>数量: ${currentVal}<br/>总数: ${totalVal}<br/>占比: ${calculatedPercentage}%`;
      },
    },
    series: [
      {
        name: props.chartName,
        type: 'liquidFill',
        radius: '90%',
        center: ['50%', '50%'],
        backgroundStyle: {
          color: 'transparent',
        },
        // data 接收 0-1 之间的值
        data: [percentage, percentage], // 使用计算出的比例
        amplitude: 20, // 水波振幅
        label: {
          // 标签设置
          position: ['50%', '45%'],
          // 显示文本格式为 "当前数(百分比%)"
          formatter: `${props.current}(${displayPercentage}%)`,
          textStyle: {
            fontSize: '20px', // 文本字号
            color: '#fff',
          },
        },
        outline: {
          borderDistance: 3,
          itemStyle: {
            borderWidth: 2,
            borderColor: {
              type: 'linear',
              x: 1,
              y: 0,
              x2: 0,
              y2: 0,
              colorStops: [
                {offset: 0, color: '#007DFF'},
                {offset: 0.6, color: 'rgba(45, 67, 114, 1)'},
                {offset: 1, color: 'rgba(45, 67, 114, 1)'},
              ],
              globalCoord: false,
            },
          },
        },
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
            {offset: 1, color: 'rgba(31, 222, 225, 1)'},
            {offset: 0.85, color: '#007DFF80'},
            {offset: 0.35, color: '#004a99'},
            {offset: 0, color: 'rgba(31, 222, 225, 1)'},
          ]),
        },
      },
    ],
  };

  myChartInstance.setOption(option, true);
};

// 监听 props.current 和 props.total 的变化
// 当这些值改变时，重新绘制图表
watch([() => props.current, () => props.total], () => {
  drawChart();
}, {immediate: true}); // immediate: true 表示组件挂载时也立即执行一次

// 处理窗口大小改变事件
const handleResize = () => {
  myChartInstance?.resize();
};

onMounted(() => {
  // nextTick 确保 DOM 已经渲染完毕，图表容器可用
  nextTick(() => {
    drawChart();
    window.addEventListener('resize', handleResize);
  });
});

onBeforeUnmount(() => {
  // 组件销毁时，移除事件监听器并销毁 ECharts 实例
  window.removeEventListener('resize', handleResize);
  if (myChartInstance) {
    myChartInstance.dispose();
    myChartInstance = null;
  }
});
</script>

<style scoped>
/* 您可以在这里添加组件的样式 */
.chart {
  width: 100%;
  height: 100%;
}
</style>
