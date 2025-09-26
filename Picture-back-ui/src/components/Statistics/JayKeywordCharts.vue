<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"/>
</template>

<script setup>
import {nextTick, onBeforeUnmount, onMounted, ref, watch, getCurrentInstance} from 'vue';
import * as echarts from 'echarts';
import 'echarts-wordcloud';
import {isHttp} from "@/utils/validate.js";
import {generateRandomColor} from "@/utils/ruoyi.js";

const {proxy} = getCurrentInstance();

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
    default: ''
  },
  chartData: {
    type: Array,
    default: () => [
      // 一、录音室专辑《Jay》（2000）- 10首
      {name: '可爱女人', value: 1800},
      {name: '完美主义', value: 250},
      {name: '星晴', value: 2200},
      {name: '娘子', value: 1200},
      {name: '斗牛', value: 1500},
      {name: '黑色幽默', value: 2800},
      {name: '伊斯坦堡', value: 150},
      {name: '印第安老斑鸠', value: 200},
      {name: '龙卷风', value: 3500},
      {name: '反方向的钟', value: 2000},

      // 二、录音室专辑《范特西》（2001）- 10首
      {name: '爱在西元前', value: 3000},
      {name: '爸我回来了', value: 2500},
      {name: '简单爱', value: 5000},
      {name: '忍者', value: 2200},
      {name: '开不了口', value: 4500},
      {name: '上海一九四三', value: 3200},
      {name: '对不起', value: 300},
      {name: '威廉古堡', value: 2800},
      {name: '双截棍', value: 4800},
      {name: '安静', value: 4200},

      // 三、录音室专辑《八度空间》（2002）- 10首
      {name: '半兽人', value: 2800},
      {name: '半岛铁盒', value: 4000},
      {name: '暗号', value: 3200},
      {name: '龙拳', value: 2500},
      {name: '火车叨位去', value: 1800},
      {name: '分裂', value: 3000},
      {name: '爷爷泡的茶', value: 2200},
      {name: '回到过去', value: 3500},
      {name: '米兰的小铁匠', value: 1500},
      {name: '最后的战役', value: 3800},

      // 四、录音室专辑《叶惠美》（2003）- 11首
      {name: '以父之名', value: 3500},
      {name: '懦夫', value: 1200},
      {name: '晴天', value: 8000},
      {name: '三年二班', value: 2800},
      {name: '东风破', value: 3200},
      {name: '你听得到', value: 2200},
      {name: '同一种调调', value: 1800},
      {name: '她的睫毛', value: 2000},
      {name: '爱情悬崖', value: 1500},
      {name: '梯田', value: 1000},
      {name: '双刀', value: 1200},

      // 五、录音室专辑《七里香》（2004）- 10首
      {name: '我的地盘', value: 3000},
      {name: '七里香', value: 6046},
      {name: '借口', value: 4200},
      {name: '外婆', value: 2200},
      {name: '将军', value: 1800},
      {name: '搁浅', value: 5002},
      {name: '乱舞春秋', value: 2500},
      {name: '困兽之斗', value: 1500},
      {name: '园游会', value: 3200},
      {name: '止战之殇', value: 1400},

      // 六、录音室专辑《十一月的萧邦》（2005）- 10首
      {name: '夜曲', value: 5265},
      {name: '蓝色风暴', value: 2200},
      {name: '发如雪', value: 4500},
      {name: '黑色毛衣', value: 3200},
      {name: '四面楚歌', value: 1800},
      {name: '枫', value: 3800},
      {name: '浪漫手机', value: 2500},
      {name: '逆鳞', value: 1500},
      {name: '麦芽糖', value: 2000},
      {name: '珊瑚海', value: 3000},

      // 七、录音室专辑《依然范特西》（2006）- 10首
      {name: '夜的第七章', value: 3800},
      {name: '听妈妈的话', value: 4500},
      {name: '千里之外', value: 6000},
      {name: '本草纲目', value: 5200},
      {name: '退后', value: 3500},
      {name: '红模仿', value: 800},
      {name: '心雨', value: 400},
      {name: '白色风车', value: 3000},
      {name: '迷迭香', value: 2800},
      {name: '菊花台', value: 5800},

      // 八、录音室专辑《我很忙》（2007）- 10首
      {name: '牛仔很忙', value: 3200},
      {name: '彩虹', value: 1200},
      {name: '青花瓷', value: 6536},
      {name: '阳光宅男', value: 2500},
      {name: '蒲公英的约定', value: 4200},
      {name: '无双', value: 1800},
      {name: '我不配', value: 3800},
      {name: '扯', value: 1500},
      {name: '甜甜的', value: 2800},
      {name: '最长的电影', value: 3500},

      // 九、录音室专辑《魔杰座》（2008）- 11首
      {name: '龙战骑士', value: 2200},
      {name: '给我一首歌的时间', value: 4500},
      {name: '蛇舞', value: 1500},
      {name: '花海', value: 5897},
      {name: '魔术先生', value: 1800},
      {name: '说好的幸福呢', value: 4800},
      {name: '兰亭序', value: 5534},
      {name: '流浪诗人', value: 1200},
      {name: '时光机', value: 2800},
      {name: '乔克叔叔', value: 1500},
      {name: '稻香', value: 7340},

      // 十、录音室专辑《跨时代》（2010）- 11首
      {name: '跨时代', value: 3200},
      {name: '说了再见', value: 3800},
      {name: '烟花易冷', value: 4500},
      {name: '免费教学录音带', value: 50},
      {name: '好久不见', value: 2500},
      {name: '雨下一整晚', value: 3000},
      {name: '嘻哈空姐', value: 30},
      {name: '我落泪情绪零碎', value: 2800},
      {name: '爱的飞行日记', value: 2200},
      {name: '自导自演', value: 2500},
      {name: '超人不会飞', value: 3000},

      // 十一、录音室专辑《惊叹号》（2011）- 11首
      {name: '惊叹号', value: 1800},
      {name: '迷魂曲', value: 2200},
      {name: 'Mine Mine', value: 2500},
      {name: '公主病', value: 1500},
      {name: '你好吗', value: 3200},
      {name: '疗伤烧肉粽', value: 1200},
      {name: '琴伤', value: 2800},
      {name: '水手怕水', value: 1800},
      {name: '世界未末日', value: 2000},
      {name: '皮影戏', value: 1500},
      {name: '超跑女神', value: 1200},

      // 十二、录音室专辑《十二新作》（2012）- 11首
      {name: '四季列车', value: 1800},
      {name: '手语', value: 2200},
      {name: '公公偏头痛', value: 2500},
      {name: '明明就', value: 4200},
      {name: '傻笑', value: 2800},
      {name: '比较大的大提琴', value: 20},
      {name: '爱你没差', value: 2000},
      {name: '红尘客栈', value: 4500},
      {name: '梦想启动', value: 1500},
      {name: '大笨钟', value: 3200},
      {name: '哪里都是你', value: 3800},

      // 十三、录音室专辑《哎哟，不错哦》（2014）- 11首
      {name: '阳明山', value: 1800},
      {name: '窃爱', value: 2200},
      {name: '算什么男人', value: 4200},
      {name: '天涯过客', value: 3200},
      {name: '怎么了', value: 2500},
      {name: '一口气全念对', value: 1500},
      {name: '我要夏天', value: 2800},
      {name: '手写的从前', value: 4500},
      {name: '鞋子特大号', value: 2000},
      {name: '听爸爸的话', value: 3000},
      {name: '美人鱼', value: 3800},

      // 十四、录音室专辑《周杰伦的床边故事》（2016）- 10首
      {name: '床边故事', value: 2500},
      {name: '说走就走', value: 2200},
      {name: '一点点', value: 450},
      {name: '前世情人', value: 3000},
      {name: '英雄', value: 200},
      {name: '不该', value: 3500},
      {name: '土耳其冰淇淋', value: 2800},
      {name: '告白气球', value: 7272},
      {name: 'Now You See Me', value: 2500},
      {name: '爱情废柴', value: 3000},

      // 十五、录音室专辑《最伟大的作品》（2022）- 7首
      {name: '最伟大的作品', value: 800},
      {name: '还在流浪', value: 700},
      {name: '说好不哭', value: 300},
      {name: '我是如此相信', value: 500},
      {name: 'Mojito', value: 600},
      {name: '等你下课', value: 700},
      {name: '不爱我就拉倒', value: 400},

      // 十六、EP《霍元甲》（2006）- 2首
      {name: '霍元甲', value: 3200},
      {name: '献世', value: 1800},

      // 十七、EP《黄金甲》（2006）- 2首
      {name: '黄金甲', value: 2500},
      {name: '菊花台', value: 5800},

      // 十八、EP《天地一斗》（2011）- 1首
      {name: '天地一斗', value: 1500},

      // 十九、EP《天台电影原声带》（2013）- 11首
      {name: '天台', value: 2200},
      {name: '快门慢舞', value: 1800},
      {name: '哪里都是你', value: 3800},
      {name: '兄弟的酒', value: 1500},
      {name: '悲伤的斜对面', value: 1200},
      {name: '梦幻的婚礼', value: 1000},
      {name: '浪子膏', value: 800},
      {name: '天台的月光', value: 2000},
      {name: '竹竿舞', value: 500},
      {name: '英雄之歌', value: 1800},
      {name: '大明星', value: 1200},

      // 二十、EP《不能说的秘密电影原声带》（2007）- 3首
      {name: '不能说的秘密', value: 2200},
      {name: '晴天娃娃', value: 1500},
      {name: '彩虹', value: 1200},

      // 二十一、单曲（未收录于专辑）- 23首
      {name: '一路向北', value: 4846},
      {name: '画沙', value: 1800},
      {name: '默', value: 2800},
      {name: 'Try', value: 1200},
      {name: '告白气球（Remix）', value: 1200},
      {name: '七里香（Live）', value: 1000},
      {name: '晴天（Live）', value: 900},
      {name: '稻香（Remix摇滚版）', value: 4000},
      {name: '纽约地铁（Demo完整版）', value: 900},
      {name: '即兴曲（钢琴版）', value: 800},
      {name: 'Six Degrees', value: 1200},
      {name: 'Christmas Star', value: 1500},
      {name: '我不配（Demo）', value: 1800},
      {name: '搁浅（Live）', value: 2200},
      {name: '发如雪（Live）', value: 2500},
      {name: '青花瓷（Live）', value: 3200},
      {name: '兰亭序（Live）', value: 3000},
      {name: '说好的幸福呢（Live）', value: 2800},
      {name: '给我一首歌的时间（Live）', value: 3000},
      {name: '花海（Live）', value: 3200},
      {name: '夜曲（Live）', value: 3500},
      {name: '东风破（Live）', value: 2800},
      {name: '以父之名（Live）', value: 3000}
    ]
  },
  sizeRange: {
    type: Array,
    default: () => [8, 64]
  },
  rotationRange: {
    type: Array,
    default: () => [-45, 90]
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
  maskImageSrc: {
    type: String,
    default: "https://picture.springsun.online/picture/public/jay.png"
  }
});

const chart = ref(null);
const chartRef = ref(null);

const loadMaskImage = async (src, containerWidth, containerHeight) => {
  return new Promise(async (resolve, reject) => {
    if (!src) {
      resolve(null);
      return;
    }

    if (src.startsWith('data:')) {
      const validPrefixes = ['data:image/png;base64,', 'data:image/jpeg;base64,', 'data:image/gif;base64,'];
      const isInvalid = validPrefixes.every(prefix => !src.startsWith(prefix));
      if (isInvalid) {
        reject(new Error('base64格式错误，必须以 data:image/png;base64, 等图片格式开头'));
        return;
      }
    } else if (isHttp(src)) {
      src = await proxy.$download.downloadNetwork(src)
    }

    const img = new Image();
    img.crossOrigin = 'anonymous';
    const timeoutTimer = setTimeout(() => {
      reject(new Error('图片加载超时（5秒）'));
    }, 5000);

    img.onload = () => {
      clearTimeout(timeoutTimer);
      if (img.width < 10 || img.height < 10) {
        reject(new Error('图片尺寸过小（至少10x10像素）'));
        return;
      }

      // 创建一个与容器等宽高的 canvas
      const canvas = document.createElement('canvas');
      const ctx = canvas.getContext('2d');
      canvas.width = containerWidth;
      canvas.height = containerHeight;

      // 保持图片比例，将图片绘制到 canvas 上
      const scale = Math.min(containerWidth / img.width, containerHeight / img.height);
      const scaledWidth = img.width * scale;
      const scaledHeight = img.height * scale;
      const x = (containerWidth - scaledWidth) / 2;
      const y = (containerHeight - scaledHeight) / 2;

      ctx.drawImage(img, x, y, scaledWidth, scaledHeight);
      resolve(canvas);
    };

    img.onerror = (err) => {
      clearTimeout(timeoutTimer);
      reject(new Error(`图片加载失败: ${err.message || '未知错误'}`));
    };

    img.src = src;
  });
};

const initChart = async () => {
  if (!props.chartData.length || !chartRef.value) return;

  const containerWidth = chartRef.value.offsetWidth;
  const containerHeight = chartRef.value.offsetHeight;

  let maskImage = null;
  try {
    maskImage = await loadMaskImage(props.maskImageSrc, containerWidth, containerHeight);
  } catch (err) {
    console.warn('遮罩图加载失败，使用默认样式:', err.message);
    maskImage = null;
  }

  if (chart.value) chart.value.dispose();
  chart.value = echarts.init(chartRef.value);

  const dataWithColor = props.chartData.map(item => ({
    ...item,
    textStyle: {
      color: generateRandomColor(props.defaultColor),
      animation: 'unifiedFade 1000ms easeOutQuart forwards'
    }
  }));

  chart.value.setOption({
    title: {
      text: props.chartName,
      left: 'center',
      textStyle: {fontSize: 18}
    },
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(0,0,0,0.5)',
      textStyle: {
        color: '#fff'
      },
      formatter: params => `${params.name}: ${params.value}`
    },
    animation: true,
    animationDuration: 500,
    animationEasing: 'easeOutQuad', // 缓动效果（比默认的 bounce 更利落，无多余弹跳）
    series: [{
      type: 'wordCloud',
      maskImage: maskImage,
      sizeRange: props.sizeRange,
      rotationRange: props.rotationRange,
      width: containerWidth,
      height: containerHeight,
      data: dataWithColor,
      gridSize: 8,
      layoutAnimation: true,
      animation: true,
      animationDuration: 1000,
      animationDelay: 300,
      animationEasing: 'easeOutQuad',
      animationLoop: false,
    }]
  }, true);
  window.addEventListener('resize', handleResize);
};

const handleResize = () => {
  if (chart.value) {
    chart.value.resize();
    // 重新初始化图表以重新计算遮罩图片
    initChart();
  }
};

onMounted(() => nextTick(initChart));
onBeforeUnmount(() => {
  chart.value?.dispose();
  window.removeEventListener('resize', handleResize);
});

watch([() => props.chartData, () => props.maskImageSrc], () => nextTick(initChart), {deep: true});
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
