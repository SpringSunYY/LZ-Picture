<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"></div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue';
import * as echarts from 'echarts';
import 'echarts/theme/macarons'; // 引入主题

// 定义组件Props，适配合格率统计需求
const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '100%'},
  autoResize: {type: Boolean, default: true},
  // 图表核心数据：原始月度合格率数据
  chartData: {
    type: Object,
    default: () => ({
      names: ['产品A', '产品B', '产品C', '产品D', '产品E', '产品F', '产品G', '产品H'],
      values: [120, 200, 150, 80, 70, 110, 130, 180]
    })
  },
  chartName: {type: String, default: '用户每日登录'}, // 图表标题
  chartTitle: {type: Array, default: ["数据", "趋势"]}
});

const chartRef = ref(null);
let chart = null;

// 初始化图表
const initChart = async () => {
  await nextTick();
  if (!chartRef.value) return;

  // 销毁已有图表避免重复实例
  if (chart) {
    chart.dispose();
    chart = null;
  }

  // 初始化ECharts实例（使用macarons主题）
  chart = echarts.init(chartRef.value, 'macarons');
  setOptions();
};

// 处理数据并设置ECharts配置项
const setOptions = () => {
  if (!chart) return;
  if (!props.chartData || !props.chartData.names || !props.chartData.values) return;
  const xData = props.chartData.names; // X轴：
  const yData = props.chartData.values; // Y轴
  const differenceData = []; // 用于存储与上一期相比的增减量

  // 处理原始数据：提取X轴和处理Y轴数据
  yData.forEach((item, index) => {
    // console.log(item);
    // 计算与上一期相比的增减量
    if (index > 0) {
      const previousValue = Number(yData[index - 1]);
      const difference = Number(item) - previousValue;
      differenceData.push(difference);
    } else {
      differenceData.push(null); // 第一期没有前一期可以比较
    }
  });

  // ECharts配置项
  const option = {
    // backgroundColor: "#111c4e", // 图表背景色
    title: {
      text: props.chartName,
      left: "center",
      top: "2%",
      textStyle: {
        color: "#fff",
        fontSize: 20
      }
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "line",
        lineStyle: {opacity: 0} // 隐藏坐标轴指示器线
      },
      // 自定义提示框：显示名称、值和增减量
      formatter: function (params) {
        let tooltipContent = '';
        // 假设 params[0] 是我们关注的主要数据系列
        const currentItem = params[0];

        if (currentItem) {
          const name = currentItem.name; // X轴名称
          const value = currentItem.value; // 当前值

          // 显示月份名称
          tooltipContent += `${name}<br/>`;

          // 显示主数据系列的值，并格式化为带百分比的数值
          tooltipContent += `${currentItem.seriesName}：${value}`;

          // 添加与上一期的增减量信息
          const diff = differenceData[currentItem.dataIndex];
          // 仅当不是第一项且有增减量时显示
          if (currentItem.dataIndex > 0 && diff !== null) {
            const sign = diff >= 0 ? '+' : '-'; // 符号
            const color = diff >= 0 ? 'color: #00ff00;' : 'color: #ff0000;'; // 绿色表示增加, 红色表示减少
            // 显示增减量，保留两位小数，并加上百分比符号
            tooltipContent += `<br/><span style="${color}">较上一期：${sign}${Math.abs(diff).toFixed(2)}</span>`;
          }
        }
        return tooltipContent;
      }
    },
    legend: {
      data: props.chartTitle, // 图例数据
      top: "12%",
      textStyle: {
        color: "#fff"
      }
    },
    grid: {
      left: "3%",
      right: "3%",
      bottom: "1%",
      top: "20%",
      height: "80%",
      containLabel: true,
      z: 22 // 层级（确保不被覆盖）
    },
    xAxis: [{
      type: "category",
      data: xData,
      axisTick: {alignWithLabel: true}, // 刻度与标签对齐
      axisLine: {lineStyle: {color: "#0c3b71"}}, // X轴线颜色
      splitArea: {show: false},   // 取消交替底色
      axisLabel: {
        show: true,
        color: "rgb(255,255,255)",
        fontSize: 12
      } // X轴标签样式
    }],
    yAxis: [
      // 主Y轴：显示百分比
      {
        type: "value",
        splitLine: {show: false}, // 隐藏网格线
        axisTick: {show: false}, // 隐藏刻度线
        axisLine: {lineStyle: {color: "#0c3b71"}}, // Y轴线颜色
        splitArea: {show: false},   // 取消交替底色
        axisLabel: {
          color: "rgb(170,170,170)",
          formatter: "{value}" // 显示百分比符号
        }
      },
      // 副Y轴：用于背景色分割（不显示标签）
      {
        type: "value",
        splitNumber: 12, // 分割段数
        splitLine: {show: false},
        axisLine: {show: false}, // 隐藏Y轴线
        axisTick: {show: false}, // 隐藏刻度线
        axisLabel: {show: false}, // 隐藏标签
        // 交替背景色（增强视觉层次）
        splitArea: {
          show: true,
          areaStyle: {
            color: ["rgba(250,250,250,0.0)", "rgba(250,250,250,0.05)"]
          }
        }
      }
    ],
    series: [
      // 1. 合格率柱状图（前景）
      {
        name: props.chartTitle[0],
        type: "bar",
        barWidth: "30%", // 柱子宽度
        itemStyle: {
          normal: {
            barBorderRadius: [30, 30, 0, 0], // 只保留顶部圆角
            // 线性渐变颜色（从顶部到底部）
            color: new echarts.graphic.LinearGradient(
                0, 0, 0, 1,
                [
                  {offset: 0, color: "#00feff"},
                  {offset: 0.5, color: "#027eff"},
                  {offset: 1, color: "#0286ff"}
                ]
            )
          }
        },
        data: yData,
        zlevel: 11 // 层级（高于背景柱子）
      },
      // 2. 趋势线（折线图）
      {
        name: props.chartTitle[1],
        type: "line",
        smooth: true, // 平滑曲线
        symbol: "none", // 隐藏数据点
        lineStyle: {
          color: "#ffcc00", // 黄色趋势线
          width: 2 // 线宽
        },
        data: yData,
        zlevel: 12 // 层级（最高，覆盖所有元素）
      }
    ]
  };

  // 应用配置项
  chart.setOption(option);
};

// 窗口 resize 时重置图表大小
const resizeChart = () => chart?.resize();

// 生命周期：挂载时初始化图表
onMounted(() => {
  initChart();
  if (props.autoResize) {
    window.addEventListener('resize', resizeChart);
  }
});

// 生命周期：卸载前销毁图表和事件监听
onBeforeUnmount(() => {
  chart?.dispose();
  chart = null;
  if (props.autoResize) {
    window.removeEventListener('resize', resizeChart);
  }
});

// 监听Props数据变化：重新渲染图表
watch(
    () => props.chartData,
    () => {
      setOptions();
    },
    {deep: true} // 深度监听（对象内部属性变化）
);

// 监听图表容器尺寸变化（可选：处理动态宽高）
watch(
    [() => props.width, () => props.height],
    () => {
      nextTick(() => resizeChart());
    }
);
</script>

<style scoped>
.chart {
  overflow: hidden;
}
</style>
