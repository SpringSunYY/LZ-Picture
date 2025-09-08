<template>
  <div class="chart-container">
    <div :class="className" :style="{ height, width }" ref="chartRef"/>
    <div class="back" @click="goBack" v-show="showBack">返回</div>
  </div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, watch, nextTick} from 'vue';
import * as echarts from 'echarts';
import {getGeoJson} from '@/api/file.js';

const props = defineProps({
  className: {type: String, default: 'chart'},
  width: {type: String, default: '100%'},
  height: {type: String, default: '100%'},
  initCountry: {type: String, default: 'china'},
  initName: {type: String, default: '中华人民共和国'},
  chartName: {type: String, default: '用户分布'},
});

const chart = ref(null);
const chartRef = ref(null);
const chartTitle = ref(props.chartName);
const geoJsonFeatures = ref([]);
const showBack = ref(false);
const parentInfo = ref([]); // 下钻历史：[{name, level}]
const isChartReady = ref(false); // 图表是否完全初始化标志
const resizeTimer = ref(null); // 防抖定时器
const isRendering = ref(false); // 渲染状态标志

/**
 * 层级推断逻辑（基于当前地图整体层级，而非单个区域层级）
 */
const formateLevel = (currentLevel) => {
  console.log('当前地图层级:', currentLevel);
  switch (currentLevel) {
    case props.initCountry: // 国家（如 china）
      return 'province'; // 下一级：省份
    case 'province': // 省份
      return 'city'; // 下一级
    case 'city': // 城市
      return 'county'; // 下一级
    case 1:
      return 'province';
    case 2:
      return 'city';
    case 3:
      return 'county';
    default:
      console.warn('未知层级:', currentLevel);
      return ''; // 无法推断时返回空，避免后续错误
  }
};

/**
 * 初始化下钻历史
 */
const initializeParentInfo = () => {
  if (props.initName === '中华人民共和国') {
    parentInfo.value = [{name: '中华人民共和国', level: 'china'}];
  } else {
    parentInfo.value = [{name: props.initName, level: 'province'}];
  }
  console.log('初始化下钻历史:', parentInfo.value);
};

/**
 * 生成地图/散点数据（修复：空数据场景返回空数组）
 */
function getMapData() {
  if (geoJsonFeatures.value.length === 0) {
    return {mapData: [], pointData: []}; // 无数据时返回空，避免渲染异常
  }

  const tmp = geoJsonFeatures.value.map(feature => {
    const {name, fullname, adcode, level, center} = feature.properties || {};
    const value = Math.round(Math.random() * 5000) + 200;
    const login = Math.round(value * (0.5 + Math.random() * 0.4));
    return {name, fullname, cityCode: adcode, level, center, value, login};
  }).sort((a, b) => a.value - b.value); // 升序排序

  const mapData = tmp.map(item => ({
    name: item.name,
    value: item.value,
    login: item.login,
    level: item.level,
    cityCode: item.cityCode,
    fullname: item.fullname
  }));

  const pointData = tmp.map(item => ({
    name: item.name,
    value: [
      item.center?.[0] || (116 + Math.random()),
      item.center?.[1] || (30 + Math.random()),
      item.value
    ],
    cityCode: item.cityCode,
    fullname: item.fullname
  }));

  return {mapData, pointData};
}

/**
 * 渲染地图 - 完全重写，分离坐标系避免冲突
 */
function renderMap() {
  if (!chart.value || isRendering.value) return;

  isRendering.value = true;
  const mapName = 'map';

  // 1. 先注册地图（确保 geo 组件能找到地图数据）
  if (geoJsonFeatures.value.length > 0) {
    echarts.registerMap(mapName, {features: geoJsonFeatures.value});
  }

  const {mapData, pointData} = getMapData();
  const values = mapData.map(d => d.value);
  const min = values.length ? Math.min(...values) : 0;
  const max = values.length ? Math.max(...values) : 10000;
  //计算总数
  const totalUsers = mapData.reduce((sum, d) => sum + (d.value || 0), 0);
  const totalLogins = mapData.reduce((sum, d) => sum + (d.login || 0), 0);

  // 处理 visualMap 极值相同的情况
  let visualMapMin = min;
  let visualMapMax = max;
  if (min === max) {
    visualMapMin = max === 0 ? 0 : max * 0.8;
    visualMapMax = max === 0 ? 1000 : max;
  }

  const yCategories = mapData.map(d => d.name);
  const barSeriesData = mapData.map(d => ({
    name: d.name,
    value: d.value,
    login: d.login,
    cityCode: d.cityCode,
    level: d.level,
    fullname: d.fullname
  }));

  // 关键修复：完全重构配置，避免坐标系冲突
  const option = {
    // 避免动画期间的坐标系冲突
    animation: false,
    title: [{
      left: 'center',
      top: 10,
      text: chartTitle.value,
      textStyle: {color: 'rgb(179, 239, 255)', fontSize: 16}
    }],
    tooltip: {
      trigger: 'item',
      formatter: (params) => {
        if (!params?.data) return '';
        const d = params.data;
        return `<div style="text-align:left">
            ${d.fullname || d.name}<br/>
            用户人数：${d.value || 0} 人<br/>
            登录人数：${d.login || 0} 人<br/>
            <hr style="border:0;border-top:1px solid #666;margin:4px 0"/>
            总用户数：${totalUsers} 人<br/>
            总登录数：${totalLogins} 人
          </div>`;
      },
      backgroundColor: 'rgba(60, 60, 60, 0.7)',
      borderColor: '#333',
      borderWidth: 1,
      textStyle: {color: '#fff'}
    },
    graphic: [
      {
        type: 'group',
        right: 20,
        bottom: 20,
        children: [
          {
            type: 'rect',
            shape: {width: 180, height: 60, r: 8},
            style: {
              fill: 'rgba(0,0,0,0.01)',  // 半透明背景
              stroke: '#00cfff',        // 外边框颜色
              lineWidth: 1,
              shadowBlur: 8,
              shadowColor: 'rgba(0,0,0,0.25)'
            }
          },
          {
            type: 'text',
            style: {
              text: `总用户数：${totalUsers} 人\n总登录数：${totalLogins} 人`,
              x: 10, // 相对于 rect 的内边距
              y: 10,
              fill: '#fff',
              font: '14px Microsoft YaHei',
              lineHeight: 20
            }
          }
        ]
      }
    ],
    // 关键修复：将 grid 放在 geo 之后，确保坐标系初始化顺序正确
    geo: {
      map: mapName,
      // zoom: 1.1,
      roam: true,
      center: null,
      layoutCenter: ['42%', '50%'], // 水平、垂直居中
      layoutSize: '100%',           // 控制地图缩放比例
      label: {
        normal: {show: true, color: 'rgb(249, 249, 249)'},
        emphasis: {show: true, color: '#f75a00'}
      },
      itemStyle: {
        normal: {
          areaColor: '#24CFF4',
          borderColor: '#53D9FF',
          borderWidth: 1.3,
          shadowBlur: 15,
          shadowColor: 'rgb(58,115,192)',
          shadowOffsetX: 0,
          shadowOffsetY: 6
        },
        emphasis: {areaColor: '#8dd7fc', borderWidth: 1.6, shadowBlur: 25}
      }
    },
    ...(barSeriesData.length > 0 ? {
      grid: {
        right: '1%',
        top: '10%',
        bottom: '20%',
        width: '12%',
        containLabel: false,
        show: false,
        // 确保grid有明确的z-index，避免与geo冲突
        z: 2
      },
      xAxis: {
        type: 'value',
        position: 'top',
        axisLine: {lineStyle: {color: '#455B77'}},
        axisTick: {show: false},
        axisLabel: {
          interval: 'auto',      // 显示标签
          rotate: 45,       // 倾斜 45 度
          textStyle: {color: '#ffffff'},
          fontSize: 10
        },
        splitNumber: 5, // 尝试显示最多 5 个刻度
        minInterval: 'auto', // 最小间隔
        splitLine: {show: false},
        show: true
      },
      yAxis: {
        type: 'category',
        axisLine: {lineStyle: {color: '#ffffff'}},
        axisTick: {
          show: false
        },
        axisLabel: {
          textStyle: {color: '#c0e6f9'}
        },
        data: yCategories,
        inverse: false,
        show: true
      }
    } : {}),
    visualMap: {
      min: visualMapMin,
      max:
      visualMapMax,
      left: '3%',
      bottom: '5%',
      calculable: true,
      seriesIndex: [0], // 仅关联地图系列
      inRange: {color: ['#24CFF4', '#2E98CA', '#1E62AC']},
      textStyle: {color: '#24CFF4'},
    },
    series: [
      // 地图系列
      {
        name: '用户人数',
        type: 'map',
        geoIndex: 0, // 关联第 0 个 geo 组件
        map: mapName,
        roam: true,
        zoom: 1.2,
        label: {show: false},
        data: mapData,
        // 确保样式完整，避免tooltip错误
        itemStyle: {
          normal: {
            areaColor: '#24CFF4',
            borderColor: '#53D9FF'
          }
        }
      },
      // 散点系列
      {
        name: '散点',
        type: 'effectScatter',
        coordinateSystem: 'geo',
        geoIndex: 0, // 明确关联 geo 组件
        rippleEffect: {brushType: 'fill'},
        itemStyle: {
          color: '#F4E925',
          shadowBlur: 6,
          shadowColor: '#333',
          // 确保样式完整
          opacity: 0.8
        },
        symbolSize: (val) => {
          const v = val?.[2] || 0;
          const minSize = 3, maxSize = 10;
          if (visualMapMax === visualMapMin) return (minSize + maxSize) / 2;
          return minSize + (v - visualMapMin) / (visualMapMax - visualMapMin) * (maxSize - minSize);
        },
        showEffectOn: 'render',
        data: pointData // 仅当有数据时渲染
      },
      // 只有在有数据时才添加柱状图系列
      ...(barSeriesData.length > 0 ? [{
        name: '柱状',
        type: 'bar',
        // 完全移除 coordinateSystem 声明，让ECharts自动推断
        data: barSeriesData,
        barGap: '-100%',
        barCategoryGap: '30%',
        barWidth: 6,
        itemStyle: {
          normal: {
            color: '#11AAFE',
            barBorderRadius: [0, 6, 6, 0],
            // 确保样式完整，避免tooltip错误
            opacity: 0.8
          }
        },
        // 确保有明确的z-index
        z: 3
      }] : []),
    ]
  };

  try {
    // 关键修复：使用 clear() 方法完全清除之前的配置
    chart.value.clear();
    // 然后设置新配置
    chart.value.setOption(option);
    chart.value.hideLoading();

    // 标记图表已准备就绪
    isChartReady.value = true;
  } catch (error) {
    console.error('图表渲染失败:', error);
    isChartReady.value = false;
  } finally {
    isRendering.value = false;
  }
}

/**
 * 加载地图数据
 */
async function loadMapData() {
  const currentInfo = parentInfo.value[parentInfo.value.length - 1];
  if (!currentInfo?.level) return;

  try {
    isChartReady.value = false; // 重置图表状态
    chart.value?.showLoading();
    // 构建符合后端要求的层级参数
    let requestLevel = currentInfo.level;
    if (currentInfo.level !== 'china' && !requestLevel.startsWith(props.initCountry)) {
      requestLevel = `${props.initCountry}/${currentInfo.level}`;
    }

    const res = await getGeoJson(requestLevel, currentInfo.name);
    if (!res?.geoJson) {
      console.warn('无地图数据，回退上一级');
      parentInfo.value.pop();
      return;
    }

    const data = JSON.parse(res.geoJson);
    geoJsonFeatures.value = data.features || [];
    chartTitle.value = `${currentInfo.fullname || currentInfo.name}${props.chartName}`;

    // 等待一帧再渲染，确保数据完全更新
    await nextTick();
    renderMap();

    // 处理无下级数据的情况
    if (geoJsonFeatures.value.length === 0 && parentInfo.value.length > 1) {
      console.warn('无下级数据，自动回退');
      goBack();
    }
  } catch (err) {
    console.error('地图数据加载失败:', err);
    geoJsonFeatures.value = [];
    renderMap();
  } finally {
    chart.value?.hideLoading();
  }
}

/**
 * 下钻操作（修复：基于当前地图层级推断下一级）
 */
function handleDrillDown(data) {
  if (!data?.name) {
    console.warn('无效数据，无法下钻');
    return;
  }

  const currentLevelInfo = parentInfo.value[parentInfo.value.length - 1];
  const nextLevel = formateLevel(currentLevelInfo.level); // 修复：用当前地图层级推断下一级
  if (!nextLevel) {
    console.warn('已达最低层级，无法下钻');
    return;
  }

  parentInfo.value.push({
    name: data.fullname || data.name,
    level: nextLevel
  });

  loadMapData();
  showBack.value = parentInfo.value.length > 1;
}

/**
 * 返回上一级
 */
function goBack() {
  if (parentInfo.value.length <= 1) {
    console.log('已达最高层级');
    return;
  }

  parentInfo.value.pop();
  if (parentInfo.value.length === 0) {
    initializeParentInfo();
  }

  loadMapData();
  showBack.value = parentInfo.value.length > 1;
}

/**
 * 窗口缩放处理（彻底重写，使用更安全的方式）
 */
const handleResize = () => {
  // 清除之前的定时器
  if (resizeTimer.value) {
    clearTimeout(resizeTimer.value);
  }

  // 检查图表基本状态
  if (!chart.value || isRendering.value) {
    console.log('图表不可用或正在渲染，跳过 resize');
    return;
  }

  // 关键修复：使用更长的防抖时间和更安全的resize方式
  resizeTimer.value = setTimeout(() => {
    try {
      if (chart.value && !chart.value.isDisposed()) {
        // 如果图表未就绪，直接重新渲染而不是resize
        if (!isChartReady.value) {
          console.log('图表未就绪，执行重新渲染');
          renderMap();
        } else {
          // 使用更安全的resize方式
          chart.value.resize({
            width: 'auto',
            height: 'auto',
            silent: true  // 静默模式，减少事件触发
          });
        }
      }
    } catch (error) {
      // console.warn('resize 失败，尝试重新渲染:', error);
      // resize失败时，尝试重新渲染
      isChartReady.value = false;
      setTimeout(() => {
        if (chart.value && !chart.value.isDisposed()) {
          renderMap();
        }
      }, 300);
    }
  }, 300); // 延长到500ms，确保DOM完全稳定
};

/**
 * 初始化图表
 */
const initChart = async () => {
  if (!chartRef.value) return;

  try {
    // 确保 ECharts 实例唯一
    if (chart.value) {
      chart.value.dispose();
    }

    chart.value = echarts.init(chartRef.value);

    initializeParentInfo();
    await loadMapData();

    // 绑定点击事件（避免重复绑定）
    chart.value.off('click');
    chart.value.on('click', (params) => {
      if ((params.seriesType === 'map' || params.seriesType === 'bar') && params.data) {
        handleDrillDown(params.data);
      }
    });
  } catch (error) {
    console.error('图表初始化失败:', error);
  }
};

/**
 * 绑定窗口缩放事件（分离函数，确保在图表完全初始化后再绑定）
 */
const bindResizeEvent = () => {
  // 先解绑之前的事件，避免重复绑定
  window.removeEventListener('resize', handleResize);
  // 重新绑定事件
  window.addEventListener('resize', handleResize, {passive: true});
};

// 生命周期钩子
onMounted(() => {
  nextTick(async () => {
    await initChart();
    // 图表初始化完成后延迟绑定 resize 事件，确保图表完全渲染
    setTimeout(() => {
      bindResizeEvent();
    }, 1000); // 延长到1秒，确保所有初始化完成
  });
});

onBeforeUnmount(() => {
  // 销毁实例+解绑事件，避免内存泄漏
  if (resizeTimer.value) {
    clearTimeout(resizeTimer.value);
    resizeTimer.value = null;
  }

  if (chart.value) {
    try {
      if (!chart.value.isDisposed()) {
        chart.value.dispose();
      }
    } catch (error) {
      console.warn('图表销毁时出错:', error);
    }
    chart.value = null;
  }

  window.removeEventListener('resize', handleResize);
  isChartReady.value = false;
  isRendering.value = false;
});

// 监听 initName 变化
watch(() => props.initName, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    initializeParentInfo();
    loadMapData();
  }
});

</script>

<style scoped>
.chart-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.back {
  position: absolute;
  left: 25px;
  top: 25px;
  color: rgb(179, 239, 255);
  font-size: 16px;
  cursor: pointer;
  z-index: 100;
  border: 1px solid #53D9FF;
  padding: 5px 10px;
  border-radius: 5px;
  background-color: rgba(36, 207, 244, 0.2);
  transition: background-color 0.2s ease;
}

.back:hover {
  background-color: rgba(36, 207, 244, 0.4);
}
</style>
