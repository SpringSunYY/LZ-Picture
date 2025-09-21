<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup lang="ts">
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue';
import * as echarts from 'echarts';
import 'echarts-liquidfill';
import 'echarts/theme/macarons' // 引入主题

const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '100%'},
  total: {type: Number, default: 100},
  current: {type: Number, default: 50},
  chartName: {type: String, default: 'xxx占比'},
});

const chart = ref<echarts.EChartsType | null>(null);
const chartRef = ref<HTMLDivElement | null>(null);

// ECharts 实例的全局变量，方便在 draw 函数中使用
let myChartInstance: echarts.EChartsType | null = null;
// 存储原始的 option 配置
let baseOption: echarts.EChartsOption | null = null;
// 动画角度，用于 custom 系列
let angle = 0;
let timer: number | null = null;

// 计算圆周上点的坐标
function getCirlPoint(x0: number, y0: number, r: number, angle: number) {
  const x1 = x0 + r * Math.cos((angle * Math.PI) / 180);
  const y1 = y0 + r * Math.sin((angle * Math.PI) / 180);
  return {x: x1, y: y1};
}

// 初始化图表，只在组件加载时调用一次
const initChart = () => {
  if (!chartRef.value) return;

  if (myChartInstance) {
    myChartInstance.dispose();
  }
  myChartInstance = echarts.init(chartRef.value as HTMLDivElement);

  const percentage = props.current / props.total;

  // 基础配置，包含静态元素和水球图（水球图有自己的动画）
  baseOption = {
    // backgroundColor: '#020f18',
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
      backgroundColor: 'rgba(37,37,36,0.5)',
      borderWidth: 0,
      show: true,
      textStyle: {
        color: '#ffffff'
      },
      trigger: 'item',
      formatter: (params: any) => {
        if (params.seriesType === 'liquidFill') {
          return `${props.chartName}<br/>总数：${props.total}<br/>数量：${props.current}<br/>占比：${(params.value * 100).toFixed(0)}%`;
        }
        return null;
      },
    },
    series: [
      // 内线和外线（自定义系列，需要动画）
      {
        name: "内线",
        type: "custom",
        coordinateSystem: "none",
        data: [0], // 必须有 data
        renderItem: (params, api) => {
          // 这里的 renderItem 是根据 angle 动态计算的
          return {
            type: "arc",
            shape: {
              cx: api.getWidth() / 2,
              cy: api.getHeight() / 2,
              r: Math.min(api.getWidth(), api.getHeight()) / 2.3,
              startAngle: ((0 + angle) * Math.PI) / 180,
              endAngle: ((90 + angle) * Math.PI) / 180,
            },
            style: {stroke: '#0ff', fill: "transparent", lineWidth: 3.5},
            silent: true,
          };
        },
      },
      {
        name: "内线",
        type: "custom",
        coordinateSystem: "none",
        data: [0],
        renderItem: (params, api) => {
          return {
            type: "arc",
            shape: {
              cx: api.getWidth() / 2,
              cy: api.getHeight() / 2,
              r: Math.min(api.getWidth(), api.getHeight()) / 2.3,
              startAngle: ((180 + angle) * Math.PI) / 180,
              endAngle: ((270 + angle) * Math.PI) / 180,
            },
            style: {stroke: '#0ff', fill: "transparent", lineWidth: 3.5},
            silent: true,
          };
        },
      },
      {
        name: "外线",
        type: "custom",
        coordinateSystem: "none",
        data: [0],
        renderItem: (params, api) => {
          return {
            type: "arc",
            shape: {
              cx: api.getWidth() / 2,
              cy: api.getHeight() / 2,
              r: Math.min(api.getWidth(), api.getHeight()) / 2.1,
              startAngle: ((270 + -angle) * Math.PI) / 180,
              endAngle: ((40 + -angle) * Math.PI) / 180,
            },
            style: {stroke: '#0ff', fill: "transparent", lineWidth: 3.5},
            silent: true,
          };
        },
      },
      {
        name: "外线",
        type: "custom",
        coordinateSystem: "none",
        data: [0],
        renderItem: (params, api) => {
          return {
            type: "arc",
            shape: {
              cx: api.getWidth() / 2,
              cy: api.getHeight() / 2,
              r: Math.min(api.getWidth(), api.getHeight()) / 2.1,
              startAngle: ((90 + -angle) * Math.PI) / 180,
              endAngle: ((220 + -angle) * Math.PI) / 180,
            },
            style: {stroke: '#0ff', fill: "transparent", lineWidth: 3.5},
            silent: true,
          };
        },
      },
      // 线头点（自定义系列，需要动画）
      {
        name: "线头点",
        type: "custom",
        coordinateSystem: "none",
        data: [0],
        renderItem: (params, api) => {
          const x0 = api.getWidth() / 2;
          const y0 = api.getHeight() / 2;
          const r = Math.min(api.getWidth(), api.getHeight()) / 2.1;
          const point = getCirlPoint(x0, y0, r, 90 + -angle);
          return {
            type: "circle",
            shape: {cx: point.x, cy: point.y, r: 5},
            style: {stroke: '#0ff', fill: '#0ff'},
            silent: true,
          };
        },
      },
      {
        name: "线头点",
        type: "custom",
        coordinateSystem: "none",
        data: [0],
        renderItem: (params, api) => {
          const x0 = api.getWidth() / 2;
          const y0 = api.getHeight() / 2;
          const r = Math.min(api.getWidth(), api.getHeight()) / 2.1;
          const point = getCirlPoint(x0, y0, r, 270 + -angle);
          return {
            type: "circle",
            shape: {cx: point.x, cy: point.y, r: 5},
            style: {stroke: '#0ff', fill: '#0ff'},
            silent: true,
          };
        },
      },
      // 水球图
      {
        type: 'liquidFill',
        radius: '78%',
        center: ['50%', '50%'],
        color: ['#0ff', '#0ff', '#0ff'],
        data: [percentage, percentage, percentage],
        label: {
          show: true,
          textStyle: {color: '#fff', fontSize: 16},
          formatter: (params: any) => {
            return `${props.current}(${(params.value * 100).toFixed(0)}%)`;
          },
        },
        backgroundStyle: {borderWidth: 1, color: 'transparent'},
        outline: {
          show: true,
          itemStyle: {borderColor: '#0ff', borderWidth: 2},
          borderDistance: 3,
        },
      },
    ],
  };

  // 首次加载时设置option
  myChartInstance.setOption(baseOption as echarts.EChartsOption, true);
};

// 动画更新函数
function animateChart() {
  if (myChartInstance) {
    angle = (angle + 3) % 360; // 保持角度在 0-359 之间

    // 使用 setOption 的增量更新，只更新需要改变的 serie
    // ECharts 会自动处理动画
    myChartInstance.setOption({
      series: [
        // 更新内线和外线系列的角度
        {
          name: "内线", renderItem: (params, api) => { /* ... 重新定义 renderItem ... */
            return {
              type: "arc",
              shape: {
                cx: api.getWidth() / 2,
                cy: api.getHeight() / 2,
                r: Math.min(api.getWidth(), api.getHeight()) / 2.3,
                startAngle: ((0 + angle) * Math.PI) / 180,
                endAngle: ((90 + angle) * Math.PI) / 180,
              },
              style: {stroke: '#0ff', fill: "transparent", lineWidth: 3.5},
              silent: true,
            };
          }
        },
        {
          name: "内线", renderItem: (params, api) => {
            return {
              type: "arc",
              shape: {
                cx: api.getWidth() / 2,
                cy: api.getHeight() / 2,
                r: Math.min(api.getWidth(), api.getHeight()) / 2.3,
                startAngle: ((180 + angle) * Math.PI) / 180,
                endAngle: ((270 + angle) * Math.PI) / 180,
              },
              style: {stroke: '#0ff', fill: "transparent", lineWidth: 3.5},
              silent: true,
            };
          }
        },
        {
          name: "外线", renderItem: (params, api) => {
            return {
              type: "arc",
              shape: {
                cx: api.getWidth() / 2,
                cy: api.getHeight() / 2,
                r: Math.min(api.getWidth(), api.getHeight()) / 2.1,
                startAngle: ((270 + -angle) * Math.PI) / 180,
                endAngle: ((40 + -angle) * Math.PI) / 180,
              },
              style: {stroke: '#0ff', fill: "transparent", lineWidth: 3.5},
              silent: true,
            };
          }
        },
        {
          name: "外线", renderItem: (params, api) => {
            return {
              type: "arc",
              shape: {
                cx: api.getWidth() / 2,
                cy: api.getHeight() / 2,
                r: Math.min(api.getWidth(), api.getHeight()) / 2.1,
                startAngle: ((90 + -angle) * Math.PI) / 180,
                endAngle: ((220 + -angle) * Math.PI) / 180,
              },
              style: {stroke: '#0ff', fill: "transparent", lineWidth: 3.5},
              silent: true,
            };
          }
        },
        // 更新线头点的位置
        {
          name: "线头点",
          renderItem: (params, api) => {
            const x0 = api.getWidth() / 2;
            const y0 = api.getHeight() / 2;
            const r = Math.min(api.getWidth(), api.getHeight()) / 2.1;
            const point = getCirlPoint(x0, y0, r, 90 + -angle);
            return {
              type: "circle",
              shape: {cx: point.x, cy: point.y, r: 5},
              style: {stroke: '#0ff', fill: '#0ff'},
              silent: true,
            };
          },
        },
        {
          name: "线头点",
          renderItem: (params, api) => {
            const x0 = api.getWidth() / 2;
            const y0 = api.getHeight() / 2;
            const r = Math.min(api.getWidth(), api.getHeight()) / 2.1;
            const point = getCirlPoint(x0, y0, r, 270 + -angle);
            return {
              type: "circle",
              shape: {cx: point.x, cy: point.y, r: 5},
              style: {stroke: '#0ff', fill: '#0ff'},
              silent: true,
            };
          },
        },
        // 注意：liquidFill 系列的数据和动画是由 ECharts 内部管理的，
        // 如果 percentage 改变，只需要更新 `data`，ECharts 会自动处理动画。
        // 如果 percentage 不变，则无需在此处更新 liquidFill 系列。
        // 为了简化，这里不直接更新 liquidFill，只更新 custom 系列。
      ],
    });
  }
}

const handleResize = () => {
  myChartInstance?.resize();
};

onMounted(() => {
  nextTick(() => {
    initChart();
    // 启动动画定时器
    if (timer) clearInterval(timer);
    timer = window.setInterval(animateChart, 100); // 100ms 刷新一次动画
    window.addEventListener('resize', handleResize);
  });
});

onBeforeUnmount(() => {
  if (timer) clearInterval(timer);
  window.removeEventListener('resize', handleResize);
  myChartInstance?.dispose();
  myChartInstance = null;
});

// 监听 prop 变化，重新设置数据，ECharts 会自动更新水球图
watch([() => props.current, () => props.total], () => {
  if (myChartInstance) {
    myChartInstance.clear();
    initChart();
  }
});

</script>
