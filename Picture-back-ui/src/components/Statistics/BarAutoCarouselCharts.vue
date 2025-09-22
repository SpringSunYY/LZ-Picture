<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"></div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch} from 'vue';
import * as echarts from 'echarts';

// 定义组件接收的属性
const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '100%'},
  chartData: {
    type: Object,
    default: () => ({
      names: [
        '2021/09/01', '2021/09/02', '2021/09/03', '2021/09/04', '2021/09/05',
        '2021/09/06', '2021/09/07', '2021/09/08', '2021/09/09', '2021/09/10',
        '2021/09/11', '2021/09/12'
      ],
      values: [67, 97, 80, 76, 52, 63, 24, 97, 56, 78, 84, 45]
    })
  },
  chartName: {type: String, default: '项目数'},
  autoPlay: {type: Boolean, default: true} // 默认开启自动轮播
});

// Vue 3 响应式引用
const chartRef = ref(null);
let myChart = null;
let timer = null;
let count = 0;

/**
 * 辅助函数：计算图表指标（总数、平均值、增减数据）
 */
const calculateChartMetrics = (names, values) => {
  const total = values.reduce((sum, current) => sum + current, 0);
  const average = (total / values.length).toFixed(2);
  const diffData = values.map((value, index) => {
    if (index === 0) return '-';
    const prevValue = values[index - 1];
    const diff = value - prevValue;
    const isIncrease = diff > 0;
    return {
      value: diff,
      isIncrease: isIncrease,
      percent: ((Math.abs(diff) / (prevValue || 1)) * 100).toFixed(2) + '%'
    };
  });
  return {total, average, diffData};
};

/**
 * 设置 ECharts 的配置项
 */
const setOptions = () => {
  if (!myChart || !props.chartData || !props.chartData.values || props.chartData.values.length === 0) {
    return;
  }

  const {names, values} = props.chartData;
  const {total, average, diffData} = calculateChartMetrics(names, values);
  const legendData = [props.chartName];

  const option = {
    title: {
      text: props.chartName,
      left: "2%",
      top: "8%",
      textStyle: {color: "#fff", fontSize: 18}
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {type: 'none'},
      position: (point, params, dom, rect, size) => {
        let x = point[0];
        let y = point[1];
        if (x + size.contentSize[0] > size.viewSize[0]) {
          x = x - size.contentSize[0];
        }
        if (y + size.contentSize[1] > size.viewSize[1]) {
          y = y - size.contentSize[1];
        }
        return [x, y];
      },
      formatter: params => {
        const name = params[0].name;
        const projectData = params.find(p => p.seriesName === props.chartName);
        if (!projectData) return '';
        const currentIndex = names.indexOf(name);
        const diffInfo = diffData[currentIndex];
        const diffText = diffInfo === '-' ? `上一期：-` : `上一期：${diffInfo.isIncrease ? '↑' : '↓'}${Math.abs(diffInfo.value)}个 (${diffInfo.percent})`;
        return `
          <div style="font-size: 14px;font-family: Source Han Sans CN-Medium;font-weight: 500;color: #FFFFFF;margin-bottom:12px;">${name}</div>
          <div style="font-size: 14px;font-family: Source Han Sans CN-Medium;font-weight: 500;color: #FFFFFF;margin-bottom:4px;">${projectData.seriesName}：${projectData.value}</div>
          <div style="font-size: 14px;font-family: Source Han Sans CN-Medium;font-weight: 500;color: #FFFFFF;margin-bottom:4px;">${diffText}</div>
          <div style="font-size: 14px;font-family: Source Han Sans CN-Medium;font-weight: 500;color: #FFFFFF;margin-top:12px;">总计：${total} | 平均：${average}</div>
        `;
      },
      extraCssText: 'opacity: 0.8;background-color:#050F1B;padding:16px;box-shadow: 1px 6px 15px 1px rgba(0,0,0,0.13);border-radius: 4px;filter: blur(undefinedpx);border:none;'
    },
    legend: {
      data: legendData,
      top: "20",
      left: 'center',
      itemWidth: 12,
      itemHeight: 12,
      itemGap: 25,
      textStyle: {fontSize: 13, color: "#82AFC6", fontFamily: 'Source Han Sans CN-Normal', padding: [0, 0, 0, 2]}
    },
    dataZoom: [
      {type: 'inside', xAxisIndex: 0, filterMode: 'weakFilter', zoomOnMouseWheel: true, moveOnMouseMove: true},
      {
        type: 'slider',
        xAxisIndex: 0,
        filterMode: 'weakFilter',
        height: 20,
        bottom: '5%',
        textStyle: {color: '#82AFC6'},
        handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,21.5H6.7V20.1h6.6V21.5z',
        handleSize: '80%',
        showDetail: false
      }],
    grid: {top: '20%', right: '20', left: '10', bottom: '12%', containLabel: true},
    xAxis: {
      type: 'category',
      data: names,
      axisTick: {show: false},
      axisLine: {lineStyle: {color: "#1a6d84"}},
      axisLabel: {show: true, textStyle: {color: '#82AFC6', fontSize: 13, fontFamily: 'Source Han Sans CN-Normal'}}
    },
    yAxis: [{
      type: 'value',
      boundaryGap: ['0%', '20%'],
      alignTicks: true,
      splitNumber: 5,
      nameTextStyle: {
        color: '#82AFC6',
        fontSize: 13,
        fontFamily: 'Source Han Sans CN-Normal',
        align: "left",
        verticalAlign: "center"
      },
      axisLabel: {color: '#82AFC6', fontSize: 13, fontFamily: 'Source Han Sans CN-Normal'},
      axisLine: {show: false},
      axisTick: {show: false},
      splitLine: {lineStyle: {color: 'rgba(49, 218, 255, 0.5)', type: "dashed"}}
    }],
    series: [
      {
        type: 'bar',
        name: props.chartName,
        data: values,
        itemStyle: {
          color: {
            type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [{offset: 0, color: 'rgba(49, 218, 255, 1)'}, {offset: 1, color: 'rgb(8,56,133)'}],
            global: false
          },
          borderRadius: [10, 10, 0, 0]
        },
        barWidth: 15,
        markLine: {
          data: [{name: '平均值', yAxis: average}],
          symbol: 'none',
          lineStyle: {type: 'dashed', color: '#FFD700', width: 2},
          label: {show: false}
        }
      },
      {
        type: 'line',
        name: props.chartName,
        data: values,
        yAxisIndex: 0,
        symbolSize: 8,
        emphasis: {
          scale: 1.5,
          itemStyle: {
            color: "rgba(6, 201, 112, 1)",
            borderColor: 'rgba(6, 201, 112, 1)',
            borderWidth: 2,
          }
        },
        areaStyle: {
          color: {
            type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [{offset: 0, color: 'rgba(6, 201, 112, 0.3)'}, {offset: 1, color: 'rgba(47,145,255,0)'}],
            global: false
          },
        },
        lineStyle: {color: "rgba(6, 201, 112, 1)", width: 2},
        itemStyle: {color: "rgba(6, 201, 112, 1)", borderColor: 'rgba(6, 201, 112, 1)', borderWidth: 2},
        smooth: true,
      }
    ]
  };
  myChart.setOption(option, {notMerge: true});
};

/**
 * 启动定时器，自动高亮和显示 Tooltip
 */
const startAnimation = () => {
  stopAnimation(); // 先清除旧的定时器
  if (!myChart || !props.chartData || props.chartData.values.length === 0) {
    return;
  }
  const dataLength = props.chartData.values.length;
  timer = setInterval(() => {
    myChart.dispatchAction({type: 'downplay', seriesIndex: 1});
    const dataIndex = count % dataLength;
    myChart.dispatchAction({type: 'highlight', seriesIndex: 1, dataIndex: dataIndex});
    myChart.dispatchAction({type: 'showTip', seriesIndex: 1, dataIndex: dataIndex});
    count++;
  }, 2000);
};

/**
 * 停止定时器
 */
const stopAnimation = () => {
  if (timer) {
    clearInterval(timer);
    timer = null;
  }
};

/**
 * 初始化图表实例，设置配置项
 */
const initChart = () => {
  if (!chartRef.value) return;
  // 仅在 myChart 不存在时创建实例
  if (!myChart) {
    myChart = echarts.init(chartRef.value);
    window.addEventListener('resize', resizeChart);
    chartRef.value.addEventListener('mouseover', handleMouseOver);
    chartRef.value.addEventListener('mouseout', handleMouseOut);
  }
  setOptions();
};

/**
 * 窗口大小改变时调整图表大小
 */
const resizeChart = () => {
  myChart?.resize();
};

/**
 * 鼠标移入事件处理，停止动画
 */
const handleMouseOver = () => {
  if (props.autoPlay) {
    stopAnimation();
  }
};

/**
 * 鼠标移出事件处理，恢复动画
 */
const handleMouseOut = () => {
  if (props.autoPlay) {
    startAnimation();
  }
};

// 组件挂载时执行
onMounted(() => {
  // 延迟图表初始化，避免阻塞主线程
  requestAnimationFrame(() => {
    initChart();
    if (props.autoPlay) {
      startAnimation();
    }
  });
});

// 组件卸载前执行，清理资源
onBeforeUnmount(() => {
  stopAnimation();
  if (myChart) {
    myChart.dispose();
  }
  window.removeEventListener('resize', resizeChart);
  if (chartRef.value) {
    chartRef.value.removeEventListener('mouseover', handleMouseOver);
    chartRef.value.removeEventListener('mouseout', handleMouseOut);
  }
});

// 监听 chartData 变化，更新图表数据
watch(
    () => props.chartData,
    () => {
      setOptions();
    },
    {deep: true}
);

// 监听 autoPlay 变化，控制动画
watch(
    () => props.autoPlay,
    (newVal) => {
      if (newVal) {
        startAnimation();
      } else {
        stopAnimation();
      }
    }
);

// 监听 width 和 height 变化，调整图表大小
watch([() => props.width, () => props.height], () => {
  resizeChart();
});
</script>

<style scoped>
.chart {
  overflow: hidden;
}
</style>
