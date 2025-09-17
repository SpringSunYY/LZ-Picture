<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"></div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue';
import * as echarts from 'echarts';
// 如果您需要主题，可以取消注释并引入，例如：
// import 'echarts/theme/macarons';

// 定义组件Props，适配合格率统计需求
const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '100%'},
  autoResize: {type: Boolean, default: true},
  // 图表核心数据：原始数据
  chartData: {
    type: Object,
    default: () => ({
      // 默认数据，您可以根据需要传入 names (x轴) 和 totals (y轴/实际值)
      names: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
      totals: [2220, 1682, 2791, 3000, 4090, 3230, 2910]
    })
  },
  chartName: {type: String, default: '交易总量统计'}, // 图表标题
  chartTitle: {type: Array, default: ['交易量', '平均值']} // legend 显示的系列名称
});

const chartRef = ref(null);
let chart = null;
let originalDataForComparison = []; // 用于存储原始数据，以便计算与上一期的差值

// 初始化图表
const initChart = async () => {
  await nextTick();
  if (!chartRef.value) return;

  // 销毁已有图表避免重复实例
  if (chart) {
    chart.dispose();
    chart = null;
  }

  // 初始化ECharts实例
  chart = echarts.init(chartRef.value); // 如果需要主题，传入第二个参数: 'macarons'
  setOptions();
};

// 处理数据并设置ECharts配置项
const setOptions = () => {
  if (!chart) return;
  if (!props.chartData || !props.chartData.names || !props.chartData.totals) {
    console.error("Chart data is invalid.");
    return;
  }

  const xData = props.chartData.names; // X轴名称
  const yData = props.chartData.totals; // Y轴数值

  // --- 计算平均值 ---
  const average_val = yData.reduce((sum, val) => sum + val, 0) / yData.length;
  const total_val = yData.reduce((sum, val) => sum + val, 0);
  const average_data = yData.map(() => average_val); // 为每个点填充平均值

  // --- 计算与上一期的增减量 (绝对值) ---
  originalDataForComparison = [...yData]; // 复制原始数据用于比较
  const differenceData = []; // 存储增减量
  yData.forEach((item, index) => {
    if (index > 0) {
      const prevValue = originalDataForComparison[index - 1];
      const difference = item - prevValue;
      differenceData.push(difference);
    } else {
      differenceData.push(null); // 第一项没有前一期可比较
    }
  });

  // ECharts配置项
  const option = {
    grid: {
      left: '2%',
      right: '4%',
      bottom: '10%',
      top: '14%',
      containLabel: true
    },
    tooltip: {
      show: true,
      trigger: 'axis', // 使用 'axis' 触发，方便对比同一X轴上的多个系列
      backgroundColor: 'rgba(37,37,36,0.5)',
      borderWidth: 0,
      textStyle: {
        color: '#ffffff',
      },
      // 自定义 tooltip 格式化器
      formatter: function (params) {
        let html = '';
        // params 是一个数组，包含当前 tooltip 区域内所有 series 的数据项
        const linedemoSeries = params.find(p => p.seriesName === props.chartTitle[0]); // 找到交易量系列
        const averageSeries = params.find(p => p.seriesName === props.chartTitle[1]); // 找到平均值系列

        // 1. 处理交易量系列 (linedemo)
        if (linedemoSeries) {
          const currentVal = linedemoSeries.value;
          const dataIndex = linedemoSeries.dataIndex;
          const prevVal = dataIndex > 0 ? originalDataForComparison[dataIndex - 1] : null;
          let trendText = '';

          if (prevVal !== null) {
            const difference = currentVal - prevVal;
            // 计算绝对差值。如果您需要百分比，请修改此处计算逻辑。
            // 例如：const percentageDifference = ((currentVal - prevVal) / prevVal) * 100;
            const differenceDisplay = difference.toFixed(2); // 保留两位小数
            const trendSign = difference >= 0 ? '+' : ''; // 正号
            const trendColor = difference >= 0 ? '#00ff00' : '#ff0000'; // 绿色表示增加, 红色表示减少

            // 显示增减量（绝对值）
            trendText = `<span style="color: ${trendColor};">  (${trendSign}${differenceDisplay})</span>`;
          }
          // 显示系列名称，X轴名称，当前值，以及增减趋势
          html += `${linedemoSeries.seriesName} <br/> ${linedemoSeries.name} : ${currentVal}${trendText}<br/>`;
          html += `总计 : ${total_val}<br/>`;
        }

        // 2. 处理平均值系列
        if (averageSeries) {
          // 显示平均值系列名称、X轴名称、平均值
          // 注意：averageSeries.value 是一个数值，.toFixed(2) 用于格式化
          html += `${averageSeries.seriesName} : ${averageSeries.value.toFixed(2)}<br/>`;
        }

        return html;
      },
      extraCssText: 'box-shadow: 0 0 10px rgba(37,47,77,0.8)'
    },
    legend: {
      data: props.chartTitle, // 从 props 获取图例名称
      textStyle: {
        color: '#fff'
      }
    },
    // dataZoom 组件：实现X轴的缩放和平移
    dataZoom: [{
      type: 'slider', // 滑块形式
      xAxisIndex: [0], // 关联到第一个X轴
      filterMode: 'weakFilter', // 弱过滤，不影响非 axisIndex 指定的轴
      height: 20, // 滑块高度
      bottom: 20, // 距离底部位置
      start: 0,   // 初始显示比例
      end: 100,   // 初始显示比例
      handleStyle: { // 滑块样式
        color: '#67B59F'
      },
      backgroundColor: 'rgba(220,220,220,0.3)',
      borderColor: 'rgba(170,170,170,0.6)'
    }, {
      type: 'inside', // 内部缩放（鼠标滚轮）
      xAxisIndex: [0], // 关联到第一个X轴
      start: 0,
      end: 100
    }],
    title: {
      text: props.chartName,
      x: "2%",
      y: "2%",
      textStyle: {
        color: '#fff',
        fontSize: '18'
      },
    },
    xAxis: {
      type: 'category',
      data: xData,
      boundaryGap: false, // X轴刻度紧贴数据
      axisLabel: {
        textStyle: {
          color: '#DFDFDF'
        }
      },
      axisLine: {
        show: false
      },
      axisTick: {
        show: false
      }
    },
    yAxis: {
      axisLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLabel: {
        textStyle: {
          color: '#DFDFDF'
        }
      },
      splitLine: {
        show: true,
        lineStyle: {
          color: 'rgba(255,255,255,0.2)',
        }
      },
    },
    series: [
      { // 交易量系列 (bar + line)
        type: 'bar',
        name: props.chartTitle[0], // 使用 props.chartTitle[0] 作为系列名
        tooltip: {
          show: false // Bar 系列在 axis trigger 下不单独显示 tooltip，由 line 系列统一处理
        },
        animation: false,
        barWidth: 1.4,
        hoverAnimation: false,
        data: yData,
        itemStyle: {
          normal: {
            color: '#67B59F',
            opacity: 0.6,
            label: {
              show: false
            }
          }
        }
      },
      { // 交易量折线图，用于 hover 触发 tooltip
        type: 'line',
        name: props.chartTitle[0], // 必须与 bar 系列名称一致，以在 tooltip 中正确分组
        smooth: true,
        symbolSize: 15,
        animation: true,
        lineWidth: 1.2,
        hoverAnimation: true,
        data: yData,
        symbol: 'circle',
        itemStyle: {
          normal: {
            color: '#67B59F',
            shadowBlur: 10,
            shadowColor: "rgba(37,47,77,0.8)",
            label: {
              show: true,
              position: 'top',
              textStyle: {
                color: '#67B59F',
              }
            }
          }
        },
        areaStyle: {
          normal: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: 'rgba(78,197,163,0.7)'
            }, {
              offset: 1,
              color: 'rgba(89,170,147,0.02)'
            }]),
          }
        }
      },
      { // 平均值系列
        name: props.chartTitle[1], // 使用 props.chartTitle[1] 作为系列名
        type: 'line',
        data: average_data,
        symbol: 'none', // 不显示数据点
        lineStyle: {
          normal: {
            color: '#67B59F', // 与主线颜色一致
            width: 2,
            type: 'dashed' // 虚线
          }
        },
        // markLine 可选，用于视觉上的全局平均值参考
        // 如果 tooltip 已经能很好地显示平均值，此 markLine 可省略或保留
        markLine: {
          silent: true, // 使其不触发 tooltip
          lineStyle: {
            type: 'dashed',
            color: '#67B59F' // 平均值线的颜色
          },
          data: [{
            type: 'average', // ECharts 内置的平均值类型
            name: '全局平均线' // markLine 的名称，可在 legend 中显示（如果 legend 中包含的话）
          }]
        }
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
