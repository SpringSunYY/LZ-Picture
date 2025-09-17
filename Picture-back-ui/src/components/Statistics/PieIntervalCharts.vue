<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue';
import * as echarts from 'echarts';
// 引入 ECharts 主题，您可以根据需要选择或移除
import 'echarts/theme/macarons'; // 例如：'macarons', 'dark', 'shine' 等

// 定义组件接收的 props，用于外部传入配置
const props = defineProps({
  // 图表容器的 CSS 类名
  className: {
    type: String,
    default: 'chart'
  },
  // 图表容器的宽度
  width: {
    type: String,
    default: '100%'
  },
  // 图表容器的高度
  height: {
    type: String,
    default: '100%'
  },
  // 图表标题
  chartName: {
    type: String,
    default: '饼图'
  },
  // 图表数据，默认包含示例数据
  chartData: {
    type: Array,
    default: () => [
      {name: '化肥企业', value: 38},
      {name: '农药企业', value: 30},
      {name: '种子企业', value: 22},
      {name: '农机销售', value: 10}
    ]
  }
});

// ECharts 实例的引用
const chart = ref(null);
// DOM 元素的 ref 引用，用于挂载 ECharts
const chartRef = ref(null);

// 生成随机的RGBA颜色字符串
const getRandomColor = () => {
  const r = Math.floor(Math.random() * 256);
  const g = Math.floor(Math.random() * 256);
  const b = Math.floor(Math.random() * 256);
  return `rgba(${r}, ${g}, ${b}, 1)`; // 默认不透明
};

// 生成带有指定透明度的颜色
const getTransparentColor = (color, alpha = 0.3) => {
  // 尝试从RGBA字符串中提取RGB值
  const rgbaMatch = color.match(/rgba\((\d+),\s*(\d+),\s*(\d+),\s*\d+(\.\d+)?\)/);
  if (rgbaMatch) {
    return `rgba(${rgbaMatch[1]}, ${rgbaMatch[2]}, ${rgbaMatch[3]}, ${alpha})`;
  }
  // 如果是RGB字符串，则添加透明度
  const rgbMatch = color.match(/rgb\((\d+),\s*(\d+),\s*(\d+)\)/);
  if (rgbMatch) {
    return `rgba(${rgbMatch[1]}, ${rgbMatch[2]}, ${rgbMatch[3]}, ${alpha})`;
  }
  // 如果是其他格式，则返回原始颜色并添加透明度（此处为简化处理）
  return `${color.replace(')', `, ${alpha})`)}`;
};

// 初始化图表
const initChart = (data) => {
  // 如果没有数据，则不进行初始化
  if (!data || !data.length) {
    return;
  }

  // 销毁已存在的 ECharts 实例，避免内存泄漏
  if (chart.value) {
    chart.value.dispose();
    chart.value = null;
  }

  // 计算实际数据的总和，用于中心标题和 Tooltip
  const sum = data.reduce((total, item) => total + item.value, 0);

  // 生成随机颜色列表
  const generatedColors = Array.from({length: data.length}, getRandomColor);

  // 为图表内部颜色列表做准备
  const colorList1 = []; // 实际使用的颜色列表（包含透明）
  const colorList2 = []; // 用于内层饼图的颜色列表（包含半透明）

  generatedColors.forEach((color) => {
    // colorList1：原色 + 透明色
    colorList1.push(color);
    colorList1.push('transparent');

    // colorList2：半透明颜色 + 透明色
    colorList2.push(getTransparentColor(color, 0.3)); // 内圈使用 0.3 的透明度
    colorList2.push('transparent');
  });

  // 预先计算每个数据项的百分比，并存储在 data 中，以便在 Tooltip 和 Label 中使用
  const percentages = data.map(item => ({
    ...item,
    percent: (item.value / sum * 100).toFixed(1) // 保留一位小数
  }));

  // 创建用于 ECharts 渲染的数据列表，包含实际数据和用于间距的透明项
  let valdata = [];
  percentages.forEach((item) => {
    valdata.push({
      value: item.value,
      name: item.name,
      _percent: item.percent // 存储计算好的百分比
    });
    // 添加一个透明的“空”数据项，用于在饼图块之间创建间距
    valdata.push({
      name: '', // 空名称
      value: 1, // 设置一个较小的值，不影响整体百分比计算，但会在图上占据一定空间
      itemStyle: {color: 'transparent'}, // 设置为透明，不显示
      _percent: 0 // 标记为0百分比
    });
  });

  // --- ECharts 配置项 ---
  const option = {
    // 中心标题配置
    title: {
      // 使用 props.chartName 作为总标题
      text: props.chartName,
      left: "49.5%", // 居中
      top: "42%",
      itemGap: 10, // 标题内元素间距
      textAlign: "center", // 居中对齐
      subtext: '总共:' + sum,
      subtextStyle: {
        color: '#fff',  // 白色文字（适配深色背景）
        fontSize: 14,
        lineHeight: 20,
      }
    },
    backgroundColor: '#243c54', // 图表背景色
    tooltip: {
      trigger: 'item', // 触发类型为数据项
      formatter: function (params) {
        // 检查是否是实际数据点，而不是用于间距的透明项
        if (params.data.name) {
          const currentPercent = params.data._percent; // 获取存储的百分比
          // 构建 tooltip 显示内容
          return `${params.marker}${params.name}<br/>
                  数量: ${params.value}<br/>
                  占比: ${currentPercent}%<br/>
                  总数: ${sum}`; // 显示总数
        } else {
          return ''; // 透明项不显示 tooltip
        }
      },
    },
    legend: {
      orient: 'vertical', // 图例垂直排列
      right: '5%',       // 距离右侧 5%
      bottom: '5%',
      // 仅显示实际数据的图例项
      data: data.map(item => ({
        name: item.name,
        icon: 'circle'     // 图例图标形状
      })),
      textStyle: {
        color: '#fff',     // 图例文字颜色
        fontSize: 14,
        rich: {
          icon: { // 用于显示数据项百分比的样式
            color: '#fff',
            fontSize: 12,
            width: 50,
            align: 'right',
            padding: [0, 0, 0, 0]
          }
        }
      },
      formatter: function (name) {
        // 查找对应的数据项，获取其百分比
        const item = data.find(d => d.name === name);
        if (item) {
          // 计算百分比并格式化
          const percent = ((item.value / sum) * 100).toFixed(1);
          return `${name}  ${percent}%`;
        }
        return name;
      }
    },
    series: [
      { // 外层饼图，用于显示透明间距
        type: 'pie',
        zlevel: 3, // 层级，确保在前面
        radius: ['39%', '48%'], // 半径范围
        center: ['50%', '50%'], // 中心位置
        itemStyle: {
          // 根据索引从 colorList2 中选择颜色
          color: function (params) {
            return colorList2[params.dataIndex % colorList2.length];
          }
        },
        label: {show: false}, // 不显示标签
        data: valdata // 使用处理后的数据
      },
      { // 内层饼图，显示实际数据和标签
        type: 'pie',
        zlevel: 1, // 层级，在下方
        silent: true, // 静默，鼠标悬停不会触发事件
        radius: ['52%', '83%'], // 半径范围
        center: ['50%', '50%'], // 中心位置
        itemStyle: {
          // 根据索引从 colorList1 中选择颜色
          color: function (params) {
            return colorList1[params.dataIndex % colorList1.length];
          }
        },
        label: {
          padding: [0, 0], // 标签内边距
          alignTo: 'labelLine', // 标签对齐到引导线
          formatter: (params) => {
            // 只对实际数据显示标签
            if (params.data.name) {
              // 使用预先计算好的正确百分比
              return `{dot|}  {d|${params.data._percent}%}\n\n{c|${params.name} }`;
            }
            return ''; // 透明项不显示标签
          },
          rich: { // 富文本样式
            c: { // 名称样式
              color: 'rgba(255, 255, 255, 1)',
              fontSize: 14,
              lineHeight: 20,
            },
            d: { // 百分比样式
              fontSize: 14,
              color: 'rgba(255, 255, 255, .7)'
            },
            dot: { // 图标样式（此处未实际使用，但保留结构）
              backgroundColor: 'auto',
              width: 0,
              height: 0,
              borderRadius: 3,
              fontSize: 16,
              padding: [3, -3, 3, -3]
            }
          }
        },
        labelLine: { // 引导线配置
          length: 20, // 引导线第一段长度
          length2: 20, // 引导线第二段长度
          show: function (params) { // 只有实际数据项显示引导线
            return params.data.name !== '';
          }
        },
        data: valdata // 使用处理后的数据
      },
      { // 最外层的一个薄圆环，作为背景装饰
        type: 'pie',
        radius: ['86%', '86.3%'], // 非常薄的半径范围
        center: ['50%', '50%'],
        hoverAnimation: false, // 关闭鼠标悬停动画
        itemStyle: {
          color: 'rgba(201, 254, 240, 0.15)' // 设置为半透明的装饰色
        },
        label: {show: false}, // 不显示标签
        data: [{value: 1}] // 只有一个数据点
      },
    ]
  };

  // 使用处理好的 option 初始化 ECharts 实例
  chart.value = echarts.init(chartRef.value, 'macarons'); // 'macarons' 是一个主题
  chart.value.setOption(option);
};

// 组件挂载时执行
onMounted(() => {
  // 使用 nextTick 确保 DOM 已经渲染完毕
  nextTick(() => {
    initChart(props.chartData); // 初始化图表
    // 添加窗口大小变化的监听器，用于图表自适应
    window.addEventListener('resize', handleResize);
  });
});

onBeforeUnmount(() => {
  // 销毁 ECharts 实例，释放内存
  if (chart.value) {
    chart.value.dispose();
    chart.value = null;
  }
  // 移除窗口大小变化的监听器
  window.removeEventListener('resize', handleResize);
});

watch(
    () => props.chartData,
    (newData) => {
      // 当数据发生变化时，重新初始化图表
      initChart(newData);
    },
    {deep: true} // 深度监听，确保对象内部变化也能被检测到
);

// 处理窗口大小变化，使图表自适应
const handleResize = () => {
  chart.value?.resize();
};
</script>

<style scoped>
/* 可以根据需要添加或修改样式 */
.chart {
  width: 100%;
  height: 100%;
}
</style>
