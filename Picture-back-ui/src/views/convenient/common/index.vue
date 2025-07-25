<template>
  <div class="page-container">
    <h1 class="page-title">功能合集</h1>

    <div class="features-grid">
      <div
          v-for="operation in operations"
          :key="operation.name"
          v-show="checkPermi(operation?.permission)"
          class="feature-card"
          @click="clickOperation(operation)"
          @mouseover="hoveredFeature = operation"
          @mouseleave="hoveredFeature = null"
      >
        <!-- 图标占位符 -->
        <div class="feature-icon">
          <SvgIcon :icon-class="operation?.icon||'operation'" class-name="icon-placeholder"/>
        </div>

        <h2 class="feature-card-title">{{ operation.name }}</h2>
        <p class="feature-card-short-desc">{{ operation.shortDescription }}</p>

        <!-- 浮动描述提示 -->
        <transition name="tooltip-scale-fade">
          <div v-if="hoveredFeature === operation" class="feature-tooltip">
            {{ operation.description }}
          </div>
        </transition>
      </div>
    </div>

    <el-dialog
        v-model="openDownloadPictureHot"
        :title="title"
        width="500"
    >
      <el-form ref="downloadHotRef" :model="formDownloadPictureHot" :rules="ruleDownloadPictureHot" label-width="80px">
        <el-form-item label="统计类型" prop="type">
          <el-select v-model="formDownloadPictureHot.type" placeholder="请选择统计类型"
                     @change="handleChangeDownloadPictureHotType">
            <el-option
                v-for="dict in p_statistics_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间" prop="statisticsName">
          <el-date-picker
              v-model="formDownloadPictureHot.date"
              :type="dateTypeDownloadPictureHot"
              placeholder="Pick a week"
          />
        </el-form-item>
        <el-form-item label="期数" prop="stages">
          <el-input-number :min="formDownloadPictureHot.minStages" :max="formDownloadPictureHot.maxStages"
                           v-model="formDownloadPictureHot.stages" placeholder="请输入期数"/>
        </el-form-item>
        <el-form-item label="数量" prop="number">
          <el-input-number :min="10" :max="100"
                           v-model="formDownloadPictureHot.number" placeholder="请输入数量"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="openDownloadPictureHot = false">关闭</el-button>
          <el-button type="primary" :disabled="isDisableDownloadPictureHot" @click="downloadPictureHot">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import SvgIcon from "@/components/SvgIcon/index.vue";
import {checkPermi} from "@/utils/permission.js";
import {ElMessage} from "element-plus";
import {getStatisticsInfoStages} from "@/api/picture/statisticsInfo.js";

const {proxy} = getCurrentInstance();
const {p_statistics_type} = proxy.useDict('p_statistics_type');
// region定义功能列表数据
const operations = ref([
  {
    key: 'downloadHot',
    permission: ['picture:download:hot'],
    name: '下载热门图片',
    shortDescription: '下载最近热门的图片',
    description: '下载最近热门的图片，选择需要下载的数量和类型，下载最热门的图片',
    icon: 'hot'
  },
  {
    key: '',
    permission: ['1'],
    name: '数据可视化',
    shortDescription: '实时图表展示',
    description: '实时数据图表展示，支持自定义图表类型和数据源，帮助用户快速洞察数据趋势。',
    icon: 'bug'
  },
  {
    key: '',
    permission: ['1'],
    name: '用户管理',
    shortDescription: '注册、登录、权限',
    description: '用户注册、登录、权限管理及个人信息编辑功能，提供安全可靠的用户认证体系。',
  },
  {
    key: '',
    permission: ['1'],
    name: '任务调度',
    shortDescription: '定时任务监控',
    description: '创建、编辑和监控定时任务，支持多种触发方式和任务日志记录，提高自动化水平。',
  },
  {
    key: '',
    permission: ['1'],
    name: '通知中心',
    shortDescription: '消息提醒系统',
    description: '集成站内信、邮件、短信等多渠道通知，确保重要信息及时送达用户。',
  },
  {
    key: '',
    permission: ['1', '2'],
    name: '国际化支持',
    shortDescription: '多语言切换',
    description: '提供多语言切换功能，支持不同国家和地区的用户使用，提升产品全球化能力。',
  }, {
    key: '',
    permission: ['1'],
    name: '文件上传',
    shortDescription: '支持多种文件格式',
    description: '支持多种文件格式上传，提供进度显示和断点续传，确保大文件传输的稳定性和效率。',
    icon: 'bug'
  },
  {
    key: '',
    permission: ['1'],
    name: '数据可视化',
    shortDescription: '实时图表展示',
    description: '实时数据图表展示，支持自定义图表类型和数据源，帮助用户快速洞察数据趋势。',
    icon: 'bug'
  },
  {
    key: '',
    permission: ['1'],
    name: '用户管理',
    shortDescription: '注册、登录、权限',
    description: '用户注册、登录、权限管理及个人信息编辑功能，提供安全可靠的用户认证体系。',
  },
  {
    key: '',
    permission: ['1'],
    name: '任务调度',
    shortDescription: '定时任务监控',
    description: '创建、编辑和监控定时任务，支持多种触发方式和任务日志记录，提高自动化水平。',
  },
  {
    key: '',
    permission: ['1'],
    name: '通知中心',
    shortDescription: '消息提醒系统',
    description: '集成站内信、邮件、短信等多渠道通知，确保重要信息及时送达用户。',
  },
  {
    key: '',
    permission: ['1', '2'],
    name: '国际化支持',
    shortDescription: '多语言切换',
    description: '提供多语言切换功能，支持不同国家和地区的用户使用，提升产品全球化能力。',
  },
]);
// endregion
// 用于存储当前悬停功能对象的响应式变量
const hoveredFeature = ref(null);

const title = ref('下载热门图片')

function clickOperation(operation) {
  title.value = operation.name
  switch (operation.key) {
    case 'downloadHot':
      handleChangeDownloadPictureHotType()
      openDownloadPictureHot.value = true
      break;
    default:
      noOperation()
      break;
  }
}

//下载热门图片操作
const openDownloadPictureHot = ref(false)
const formDownloadPictureHot = ref({
  type: '2',
  number: 10,
  stages: 0,
  maxStages: 1,
  minStages: 0
})
const isDisableDownloadPictureHot = ref(false)
const dateTypeDownloadPictureHot = ref('dates')
const ruleDownloadPictureHot = ref({
  type: [{
    required: true,
    message: '请选择类型',
    trigger: 'change'
  }],
  number: [{
    required: true,
    message: '请输入数量',
    trigger: 'blur'
  }],
  stages: [{
    required: true,
    message: '请输入期数',
    trigger: 'blur'
  }]
})

function downloadPictureHot() {

}

function handleChangeDownloadPictureHotType() {
  let commonKey = null
  if (formDownloadPictureHot.value.type === '1') {
    dateTypeDownloadPictureHot.value = 'dates'
  } else if (formDownloadPictureHot.value.type === '2') {
    dateTypeDownloadPictureHot.value = 'date'
    commonKey = "picture:statistics:hot:day"
  } else if (formDownloadPictureHot.value.type === '3') {
    dateTypeDownloadPictureHot.value = 'week'
    commonKey = "picture:statistics:hot:week"
  } else if (formDownloadPictureHot.value.type === '4') {
    dateTypeDownloadPictureHot.value = 'month'
    commonKey = "picture:statistics:hot:month"
  } else if (formDownloadPictureHot.value.type === '5') {
    dateTypeDownloadPictureHot.value = 'year'
    commonKey = "picture:statistics:hot:year"
  } else if (formDownloadPictureHot.value.type === '6') {
    commonKey = "picture:statistics:hot:total"
  }
  getStatisticsInfoStages({type: formDownloadPictureHot.value.type, commonKey: commonKey}).then(res => {
    var maxStages = Number(res.data);
    // 确保数据有效性
    if (maxStages > 0) {
      formDownloadPictureHot.value.maxStages = maxStages
      formDownloadPictureHot.value.stages = maxStages
      ElMessage.success('获取期数成功,最新一期为：' + maxStages)
      isDisableDownloadPictureHot.value = false
    } else {
      isDisableDownloadPictureHot.value = true
      ElMessage.error('获取期数失败,此类型无法导出数据')
    }
  })
}

function noOperation() {
  ElMessage.error('当前功能还没实现哦');
}
</script>

<style scoped>
.page-container {
  padding: 2rem;
  background-color: #f0f2f5;
  min-height: 90vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-family: 'Inter', 'Helvetica Neue', Helvetica, Arial, sans-serif;
}

.page-title {
  font-size: 2.8rem;
  font-weight: 800;
  color: #2c3e50;
  margin-bottom: 3.5rem;
  letter-spacing: -0.02em;
}

.features-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.5rem;
  max-width: 90rem;
  width: 100%;
}

@media (min-width: 768px) {
  .features-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (min-width: 1024px) {
  .features-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

.feature-card {
  background-color: #ffffff;
  border-radius: 1rem;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
  padding: 2rem;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.2, 0.8, 0.2, 1);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  position: relative;
  overflow: visible;
  border: 1px solid #e0e0e0;
}

.feature-card:hover {
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  transform: translateY(-8px) scale(1.02);
  border-color: #a0c4ff;
}

/* 新增图标样式 */
.feature-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 60px; /* 图标容器大小 */
  height: 60px;
  border-radius: 50%; /* 圆形背景 */
  background-color: #e6f0ff; /* 浅蓝色背景 */
  color: #4a90e2; /* 蓝色图标颜色 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05); /* 柔和阴影 */
}

.icon-placeholder {
  width: 32px; /* SVG图标实际大小 */
  height: 32px;
  stroke: currentColor; /* 使用父元素的颜色 */
}


.feature-card-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #34495e;
  margin-bottom: 0.75rem;
}

.feature-card-short-desc {
  color: #7f8c8d;
  font-size: 1rem;
  line-height: 1.6;
}

/* 浮动描述提示样式 */
.feature-tooltip {
  position: absolute;
  bottom: calc(100% + 15px);
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(255, 255, 255, 0.95);
  color: #333;
  padding: 1rem 1.25rem;
  border-radius: 0.75rem;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  z-index: 10;
  white-space: normal;
  max-width: 300px;
  text-align: left;
  font-size: 0.95rem;
  line-height: 1.5;
  border: 1px solid rgba(220, 220, 220, 0.7);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  pointer-events: none;
}

.feature-tooltip::after {
  display: none;
}

/* Vue Transition 动画样式 */
.tooltip-scale-fade-enter-active, .tooltip-scale-fade-leave-active {
  transition: opacity 0.3s ease-out, transform 0.3s ease-out;
}

.tooltip-scale-fade-enter-from, .tooltip-scale-fade-leave-to {
  opacity: 0;
  transform: translateX(-50%) scale(0.9);
}
</style>
