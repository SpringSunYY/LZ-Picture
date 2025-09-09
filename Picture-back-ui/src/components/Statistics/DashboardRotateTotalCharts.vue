<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts'

// Props
const props = defineProps({
  className: { type: String, default: 'chart' },
  width: { type: String, default: '100%' },
  height: { type: String, default: '100%' },
  chartName: { type: String, default: '用户注册统计' },
  total: { type: Number, default: 1200 },
  seed: { type: Number, default: 2 } // 旋转速度倍率
})

const chartRef = ref<HTMLDivElement | null>(null)
let myChart: echarts.EChartsType | null = null
let animationId: number | null = null
let currentAngle = 0
let lastTime = 0

// 辅助函数 - 生成第二层旋转环数据
function _pie2() {
  const dataArr: any[] = []
  for (let i = 0; i < 8; i++) {
    dataArr.push({
      name: (i + 1).toString(),
      value: i % 2 === 0 ? 25 : 20,
      itemStyle: {
        normal: {
          color: i % 2 === 0 ? "rgba(88,142,197,0.5)" : "rgba(0,0,0,0)",
          borderWidth: 0,
          borderColor: "rgba(0,0,0,0)"
        }
      }
    })
  }
  return dataArr
}

// 辅助函数 - 生成第三层旋转环数据
function _pie3() {
  const dataArr: any[] = []
  for (let i = 0; i < 100; i++) {
    dataArr.push({
      name: (i + 1).toString(),
      value: i % 2 === 0 ? 25 : 20,
      itemStyle: {
        normal: {
          color: i % 2 === 0 ? "rgb(126,190,255)" : "rgba(0,0,0,0)",
          borderWidth: 0,
          borderColor: "rgba(0,0,0,0)"
        }
      }
    })
  }
  return dataArr
}

// 初始化图表
const initChart = () => {
  if (!chartRef.value) return

  myChart = echarts.init(chartRef.value, null, {
    renderer: 'canvas', // 使用canvas渲染，性能更好
    useDirtyRect: true // 开启脏矩形优化
  })

  const option: echarts.EChartsOption = {
    // 开启动画优化
    animation: false, // 关闭默认动画，使用自定义动画
    title: {
      text: props.chartName,
      left: '0.5%',
      top: '0.5%',
      textStyle: {
        color: '#fff',
        fontSize: 15
      }
    },
    series: [
      // 最外层装饰环
      {
        type: 'pie',
        zlevel: 1,
        silent: true,
        radius: ['98%', '97%'],
        hoverAnimation: false,
        animation: false,
        color: "rgba(88,142,197,0.5)",
        label: { normal: { show: false } },
        labelLine: { normal: { show: false } },
        data: [1]
      },
      // 第二层旋转环（慢速旋转）
      {
        type: 'pie',
        zlevel: 2,
        silent: true,
        radius: ['90%', '91%'],
        startAngle: 50,
        hoverAnimation: false,
        animation: false,
        label: { normal: { show: false } },
        labelLine: { normal: { show: false } },
        data: _pie2()
      },
      // 第三层装饰环
      {
        type: 'pie',
        zlevel: 3,
        silent: true,
        radius: ['88%', '87%'],
        animation: false,
        label: { normal: { show: false } },
        labelLine: { normal: { show: false } },
        data: _pie2()
      },
      // 第四层主要旋转环
      {
        type: 'pie',
        zlevel: 4,
        silent: true,
        radius: ['84%', '83%'],
        startAngle: 0,
        animation: false,
        label: { normal: { show: false } },
        labelLine: { normal: { show: false } },
        data: _pie3()
      },
      // 第五层彩色环
      {
        type: 'pie',
        zlevel: 5,
        silent: true,
        radius: ['80%', '78%'],
        color: ["#fc8d89", "#46d3f3", "rgba(203,203,203,.2)"],
        startAngle: 50,
        hoverAnimation: false,
        animation: false,
        label: { normal: { show: false } },
        data: [50, 20, 40]
      },
      // 外层刻度盘
      {
        name: "",
        type: 'gauge',
        splitNumber: 30,
        min: 0,
        max: 100,
        radius: '73%',
        center: ['50%', '50%'],
        startAngle: 90,
        endAngle: -269.9999,
        animation: false,
        axisLine: {
          show: false,
          lineStyle: {
            width: 0,
            shadowBlur: 0,
            color: [[1, '#0dc2fe']]
          }
        },
        axisTick: {
          show: false,
          lineStyle: {
            color: 'auto',
            width: 2
          },
          length: 20,
          splitNumber: 5
        },
        splitLine: {
          show: true,
          length: 32,
          lineStyle: {
            color: 'auto'
          }
        },
        axisLabel: { show: false },
        pointer: { show: false },
        detail: { show: false }
      },
      // 内层刻度盘（带数值显示）
      {
        name: '统计',
        type: 'gauge',
        splitNumber: 30,
        min: 0,
        max: 100,
        radius: '68%',
        center: ['50%', '50%'],
        startAngle: 90,
        endAngle: -269.9999,
        animation: false,
        axisLine: {
          show: true,
          lineStyle: {
            width: 0,
            shadowBlur: 0,
            color: [[0, '#0dc2fe'], [1, '#0dc2fe']]
          }
        },
        axisTick: {
          show: true,
          lineStyle: {
            color: '#0dc2fe',
            width: 2
          },
          length: 20,
          splitNumber: 5
        },
        splitLine: {
          show: true,
          length: 20,
          lineStyle: {
            color: '#0dc2fe'
          }
        },
        axisLabel: { show: false },
        pointer: { show: false },
        detail: {
          borderColor: '#fff',
          shadowColor: '#fff',
          shadowBlur: 2,
          offsetCenter: [0, '0%'],
          textStyle: {
            color: '#fff',
            fontSize: 25
          },
          formatter: '{value}'
        },
        data: [{ name: "", value: props.total }]
      },
      // 中间装饰环
      {
        type: 'pie',
        zlevel: 20,
        silent: true,
        radius: ['60%', '59%'],
        hoverAnimation: false,
        animation: false,
        color: '#2dc0c9',
        data: [1],
        labelLine: { normal: { show: false } }
      },
      // 中心环形图
      {
        name: '中间环形图',
        type: 'pie',
        radius: ['35%', '55%'],
        avoidLabelOverlap: false,
        hoverAnimation: false,
        animation: false,
        itemStyle: {
          normal: {
            color: '#0dc2fe',
            borderColor: 'rgba(61,66,104,0.38)'
          }
        },
        label: {
          normal: { show: false, position: 'center' },
          emphasis: {
            show: true,
            textStyle: {
              fontSize: '30',
              fontWeight: 'bold'
            }
          }
        },
        labelLine: { normal: { show: false } },
        data: [25, 25, 25, 25, 25, 25]
      }
    ]
  }

  myChart.setOption(option)
}

// 使用requestAnimationFrame优化的旋转动画
const animate = (currentTime: number) => {
  if (!myChart) return

  // 计算时间差，确保动画流畅度不受帧率影响
  const deltaTime = lastTime ? currentTime - lastTime : 16.67
  lastTime = currentTime

  // 根据时间差和速度计算角度增量（连续旋转，不取模）
  const angleIncrement = (props.seed * deltaTime) / 16.67
  currentAngle += angleIncrement // 连续累加，不限制在360度内

  // 计算各层的实际角度（统一逆时针旋转，避免卡顿）
  const layer2Angle = -currentAngle * 0.4 // 第二层逆时针慢速旋转
  const layer4Angle = -currentAngle * 0.3 // 第四层逆时针旋转（避免卡顿）
  const layer5Angle = -currentAngle * 0.2 // 第五层逆时针中速旋转

  // 批量更新多个旋转层的角度
  myChart.setOption({
    series: [
      {}, // 占位
      { startAngle: 50 + layer2Angle }, // 第二层逆时针慢速旋转
      {}, // 占位
      { startAngle: layer4Angle }, // 第四层逆时针旋转
      { startAngle: 50 + layer5Angle } // 第五层逆时针中速旋转
    ]
  }, {
    notMerge: false,
    lazyUpdate: false, // 关闭延迟更新，确保动画流畅
    silent: true
  })

  animationId = requestAnimationFrame(animate)
}

// 启动动画
const startAnimation = () => {
  if (animationId !== null) return
  lastTime = 0
  animationId = requestAnimationFrame(animate)
}

// 停止动画
const stopAnimation = () => {
  if (animationId !== null) {
    cancelAnimationFrame(animationId)
    animationId = null
    lastTime = 0
  }
}

// 窗口大小变化时自适应
const handleResize = () => {
  myChart?.resize()
}

// 生命周期钩子
onMounted(() => {
  initChart()
  startAnimation()

  // 绑定鼠标事件
  if (myChart) {
    myChart.on('mouseover', stopAnimation)
    myChart.on('mouseout', startAnimation)
  }

  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  stopAnimation()

  if (myChart) {
    myChart.off('mouseover', stopAnimation)
    myChart.off('mouseout', startAnimation)
    myChart.dispose()
    myChart = null
  }

  window.removeEventListener('resize', handleResize)
})

// 监听props变化
watch(() => [props.total, props.seed], () => {
  if (!myChart) return

  // 动态更新总数
  myChart.setOption({
    series: [
      {}, {}, {}, {}, {}, {},
      {
        data: [{ name: "", value: props.total }]
      }
    ]
  })
  // 重启动画以应用新的速度
  stopAnimation()
  startAnimation()
}, { deep: true })
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
  /* 开启硬件加速 */
  transform: translateZ(0);
  backface-visibility: hidden;
  perspective: 1000px;
}
</style>
