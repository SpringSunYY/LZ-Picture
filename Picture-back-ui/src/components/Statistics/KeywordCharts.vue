<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue';
import * as echarts from 'echarts';

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
    default: '一天的时间流逝'
  },
  chartData: {
    type: Array,
    default: () => [
      {name: '听音乐', value: 2},
      {name: '看电影', value: 12},
      {name: '跑步', value: 22},
      {name: '瑜伽', value: 42},
      {name: '发呆', value: 52},
      {name: '阅读', value: 62},
      {name: '唱歌', value: 72},
      {name: '跳舞', value: 72},
      {name: '追星', value: 72},
      {name: '看星星', value: 72},
      {name: '看月亮', value: 72},
      {name: '吃汉堡', value: 72},
      {name: '做早餐', value: 72},
      {name: '爬山', value: 72},
      {name: '旅行', value: 24},
      {name: '喝奶茶', value: 72},
      {name: '买衣服', value: 72},
      {name: '理财', value: 72},
      {name: '洗衣服', value: 72},
      {name: '收纳', value: 80},
      {name: '早起', value: 72},
      {name: '熬夜', value: 65},
      {name: '追剧', value: 72},
      {name: '逛街', value: 72},
      {name: '敲代码', value: 72},
      {name: '创作', value: 72},
      {name: '做梦', value: 72},
      {name: '悲伤', value: 72},
      {name: '开心', value: 72}
    ]
  },
  fontSizeRange: {
    type: Array,
    default: () => [8, 24] // 默认字体大小范围 [最小, 最大]
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
  }
});

const chart = ref(null); // 图表实例
const chartRef = ref(null); // DOM 引用

// 生成随机颜色
const getRandomColor = () => {
  const randomIndex = Math.floor(Math.random() * props.defaultColor.length);
  return props.defaultColor[randomIndex];
};

// 计算数据总和
const calculateTotal = (data) => {
  return data.reduce((sum, item) => Number(sum) + (Number(item.value) || 0), 0);
};

// 获取数据中的最小和最大 value 值
const getMinMaxValue = (data) => {
  if (!data || data.length === 0) {
    return {min: 0, max: 0};
  }
  const values = data.map(item => item.value);
  return {
    min: Math.min(...values),
    max: Math.max(...values)
  };
};

// 计算字体大小的函数
const getFontSize = (value, minDataValue, maxDataValue, minFontSize, maxFontSize) => {
  if (maxDataValue === minDataValue) {
    return minFontSize;
  }
  const valueRatio = (value - minDataValue) / (maxDataValue - minDataValue);
  const calculatedFontSize = minFontSize + valueRatio * (maxFontSize - minFontSize);
  return Math.max(minFontSize, Math.min(maxFontSize, calculatedFontSize));
};

// 初始化图表
const initChart = (data) => {
  if (!data || data.length === 0) {
    return;
  }

  if (chart.value) {
    chart.value.dispose();
    chart.value = null;
  }

  const {min: minChartValue, max: maxChartValue} = getMinMaxValue(data);

  const seriesData = data.map((item) => {
    return {
      name: item.name,
      value: item.value,
      label: {
        show: true,
        formatter: '{b}',
        color: getRandomColor(),
        fontSize: getFontSize(item.value, minChartValue, maxChartValue, props.fontSizeRange[0], props.fontSizeRange[1])
      },
      itemStyle: {
        color: 'transparent',
        borderWidth: 0
      },
      symbolSize: 1
    };
  });

  const total = calculateTotal(data);

  chart.value = echarts.init(chartRef.value);

  const option = {
    title: {
      show: true,
      text: props.chartName,
      textStyle: {
        fontSize: 16,
        color: '#ffffff',
      },
      top: '5%',
      left: '5%',
    },
    tooltip: {
      show: true,
      trigger: 'item',
      formatter: function (params) {
        const percentage = total > 0 ? ((params.data.value / total) * 100).toFixed(1) : '0.0';
        return `${params.data.name}: ${params.data.value} (${percentage}%)<br/>总计: ${total}`;
      },
      backgroundColor: 'rgba(0,0,0,0.5)',
      textStyle: {
        color: '#fff'
      }
    },
    toolbox: {
      show: true,
      feature: {
        restore: {}
      }
    },
    series: [
      {
        type: 'graph',
        layout: 'force',
        roam: 'scale',
        force: {
          // --- 关键调整：让动画更快稳定 ---
          // 显著增加斥力 (repulsion) 和重力 (gravity)，促使节点更快地分开和聚集。
          // 减小边长 (edgeLength)，增加节点间的相互作用力。
          // 增加摩擦力 (friction) 至接近 1，使动量衰减更快，动画更快停止。
          repulsion: 100, // 增加斥力，确保词语快速分开
          gravity: 0.3,   // 增加重力，加速节点向中心聚集并稳定
          edgeLength: 5,  // 减小边长，增强节点间作用力
          friction: 0.99, // 极高的摩擦力，使动画几乎立即衰减并停止
          // --- 关键点：力导向布局在找到稳定状态后会自动停止 ---
          // ECharts 的力导向布局算法会在能量（力的总和）达到一个局部最小值时停止。
          // 通过上述参数的调整，这个“稳定状态”会非常快地达到。
          // `layoutAnimation: true` 保持，它控制的是初始布局的动画过程，而非持续的移动。
        },
        label: {
          show: true,
          position: 'inside' // 标签显示在节点内部
        },
        lineStyle: {
          opacity: 0 // 隐藏连线
        },
        top: "1%",
        bottom: "1%",
        left: "1%",
        right: "1%",
        data: seriesData,
        links: [], // 没有连线
        animation: true,
        // --- 动画时长调整 ---
        // animationDuration 决定了 ECharts 尝试完成布局动画的时长。
        // 配合高摩擦力，即使是较短的时长，也能在较短时间内看到稳定效果。
        // 我们将其设置为 1500ms (1.5秒)，确保在动画结束时，力导向算法已经找到了一个近似稳定的布局。
        animationDuration: 1500,
        animationEasing: 'cubicOut' // 动画缓动效果
      }
    ]
  };

  chart.value.setOption(option);
};

// 处理窗口大小变化
const handleResize = () => {
  chart.value?.resize();
};

onMounted(() => {
  nextTick(() => {
    initChart(props.chartData);
    window.addEventListener('resize', handleResize);
  });
});

onBeforeUnmount(() => {
  if (chart.value) {
    chart.value.dispose();
    chart.value = null;
  }
  window.removeEventListener('resize', handleResize);
});

// 监听数据变化，重新渲染图表
watch(
    () => props.chartData,
    (newData) => {
      initChart(newData);
    },
    {deep: true}
);

// 监听字体大小范围变化，重新渲染图表
watch(
    () => props.fontSizeRange,
    (newRange) => {
      initChart(props.chartData);
    },
    {deep: true}
);
</script>

<style scoped>
.chart {
  padding: 10px;
  box-sizing: border-box; /* 确保 padding 被包含在宽度和高度内 */
}
</style>
