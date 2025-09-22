<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue';
import * as echarts from 'echarts';
import 'echarts/theme/macarons';
import {generateRandomColor} from "@/utils/ruoyi.js"; // 引入主题，用于图表渲染

// 定义组件的属性 (Props)
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
  // 数据单位
  dataUnit:{
    type: String,
    default: ''
  },
  defaultColor: {
    type: Array,
    default: () => [
      '#5B8FF9', '#5AD8A6', '#5D7092', '#F6BD16', '#E86A92',
      '#7262FD', '#269A29', '#8E36BE', '#41A7E2', '#7747A3',
      '#FF7F50', '#FFDAB9', '#ADFF2F', '#00CED1', '#9370DB',
      '#3CB371', '#FF69B4', '#FFB6C1', '#DA70D6', '#98FB98',
      '#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7',
      '#DDA0DD', '#98D8C8', '#F7DC6F', '#BB8FCE', '#85C1E9'
    ]
  },
});

const chart = ref(null); // ECharts 图表实例
const chartRef = ref(null); // DOM 元素引用

// 辅助函数：生成虚线环状图数据
const dashed = (val) => {
  const dataArr = [];
  for (let i = 0; i < val; i++) {
    if (i % 2 === 0) {
      dataArr.push({
        name: (i + 1).toString(),
        value: 10,
        itemStyle: {
          color: "rgb(0,0,0,0)" // 奇数段透明
        }
      });
    } else {
      dataArr.push({
        name: (i + 1).toString(),
        value: 20,
        itemStyle: {
          color: "rgb(0,0,0,0)",
          borderWidth: 2,
          borderColor: "rgba(33, 141, 224,0.18)" // 偶数段带有边框
        }
      });
    }
  }
  return dataArr;
};


// 辅助函数：将十六进制颜色转换为带透明度的 rgba 格式
const getAlphaColor = (hex, alpha) => {
  const r = parseInt(hex.slice(1, 3), 16);
  const g = parseInt(hex.slice(3, 5), 16);
  const b = parseInt(hex.slice(5, 7), 16);
  return `rgba(${r}, ${g}, ${b}, ${alpha})`;
};

// 初始化图表
const initChart = (data) => {
  if (!chartRef.value) return;

  // 销毁旧的图表实例，防止重复渲染
  if (chart.value) {
    chart.value.dispose();
    chart.value = null;
  }

  // 计算数据总和
  const total = data.reduce((sum, item) => Number(sum) + Number(item.value), 0);

  // 为数据项添加随机颜色和样式
  const styledData = data.map((item) => {
    const randomColor = generateRandomColor(props.defaultColor);
    return {
      ...item,
      itemStyle: {
        borderColor: randomColor,
        borderWidth: 2,
        shadowBlur: 20,
        shadowColor: randomColor,
        shadowOffsetX: 25,
        shadowOffsetY: 20,
        color: getAlphaColor(randomColor, 0.05),
      }
    };
  });

  // 初始化 ECharts 实例，并使用 'macarons' 主题
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
    tooltip: {
      trigger: "item",
      formatter: (params) => {
        // 格式化 tooltip，同时显示当前值、百分比和总数
        return `${params.name}: ${params.value}${props.dataUnit} (${params.percent}%)<br/>总数: ${total}${props.dataUnit}`;
      },
    },
    legend: {
      bottom: "5%",
      right: "5%",
      orient: "vertical",
      data: styledData,
      icon: "circle",
      itemWidth: 10,
      itemHeight: 10,
      itemGap: 5,
      formatter(name) {
        const item = styledData.find((item) => item.name === name);
        if (!item) return '';
        const percentage = ((item.value / total) * 100).toFixed(2);
        // 使用富文本 (rich) 格式化图例
        return `{name|${name}}`;
      },
      textStyle: {
        rich: {
          name: {color: "#fff", fontSize: 16},
        },
      },
    },
    series: [
      {
        type: "pie",
        // 调整 radius 和 center 以增大饼图尺寸
        radius: ['20%', '65%'], // 主饼图的内外半径，外半径设置为85%高度
        center: ['50%', '50%'], // 将饼图中心移至容器中央
        roseType: "radius",
        zlevel: 10,
        startAngle: 100,
        label: {
          show: false, // 允许显示标签
          position: "outside", // 标签放在外面
          formatter: "{b}",
          textStyle: {
            fontSize: 14, // 增大标签字体
            color: '#2e95f3' // 标签颜色
          },
        },
        data: styledData,
      },
      // 虚线环状图层，需要根据主饼图半径进行调整
      // 它们会围绕主饼图存在，因此也需要适当调整其半径
      {
        type: "pie",
        radius: ["14%", "14.3%"], // 保持相对大小，但整体会跟随主饼图缩放
        center: ["50%", "50%"], // 与主饼图中心保持一致
        hoverAnimation: false,
        clockWise: false,
        itemStyle: {normal: {color: "#017383"}},
        label: {show: false},
        data: dashed(18)
      },
      {
        type: "pie",
        radius: ["30%", "30.3%"],
        center: ["50%", "50%"],
        hoverAnimation: false,
        clockWise: false,
        itemStyle: {normal: {color: "#017383"}},
        label: {show: false},
        data: dashed(40)
      },
      {
        type: "pie",
        radius: ["45%", "45.3%"],
        center: ["50%", "50%"],
        hoverAnimation: false,
        clockWise: false,
        itemStyle: {normal: {color: "#017383"}},
        label: {show: false},
        data: dashed(60)
      },
      {
        type: "pie",
        radius: ["60%", "60.3%"],
        center: ["50%", "50%"],
        hoverAnimation: false,
        clockWise: false,
        itemStyle: {normal: {color: "#017383"}},
        label: {show: false},
        data: dashed(70)
      },
      {
        type: "pie",
        radius: ["76%", "76.3%"],
        center: ["50%", "50%"],
        hoverAnimation: false,
        clockWise: false,
        itemStyle: {normal: {color: "#017383"}},
        label: {show: false},
        data: dashed(80)
      }, {
        type: "pie",
        radius: ["90%", "90.3%"],
        center: ["50%", "50%"],
        hoverAnimation: false,
        clockWise: false,
        itemStyle: {normal: {color: "#017383"}},
        label: {show: false},
        data: dashed(80)
      },
    ],
  };

  chart.value.setOption(option);
};

// 处理窗口大小变化，实现图表响应式
const handleResize = () => {
  chart.value?.resize();
};

// 监听数据变化，重新渲染图表
watch(
    () => props.chartData,
    (newData) => {
      initChart(newData);
    },
    {deep: true}
);

// 组件挂载时执行
onMounted(() => {
  nextTick(() => {
    initChart(props.chartData);
    window.addEventListener('resize', handleResize);
  });
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
