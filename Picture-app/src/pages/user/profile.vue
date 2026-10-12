<template>
  <view class="profile-page" :style="rootStyleVar">
    <!--    <view class="bg-blur bg-blur-primary"/>-->
    <!--    <view class="bg-blur bg-blur-secondary"/>-->

    <!-- ============================================================
         1. 顶部创作者身份看板 (Hero Creator Card)
         ============================================================ -->
    <view class="hero-card">
      <view class="hero-card-deco"/>
      <view class="hero-card-row">
        <!-- 左侧：头像 + 身份信息 -->
        <view class="hero-card-id">
          <view class="hero-card-avatar-wrap">
            <image
                class="hero-card-avatar"
                :src="initCover(avatarUrl)"
                mode="aspectFill"
            />
          </view>

          <view class="hero-card-id-text">
            <view class="hero-card-name-row">
              <text class="hero-card-name">{{ nickName }}</text>
            </view>

            <view class="hero-card-badge-pill">
              <text class="hero-card-badge-pill-text">LZ-Picture 创作者</text>
            </view>

            <text class="hero-card-uid">LZ-ID: {{ userName }}</text>
          </view>
        </view>

        <!-- 右侧：预览主页按钮 -->
        <view class="hero-card-preview-btn" @tap="onPreviewProfile">
          <SvgIcon icon="eyes" class="hero-card-preview-icon"/>
          <text class="hero-card-preview-text">预览主页</text>
        </view>
      </view>

      <!-- 创作者快捷状态标签条 -->
      <view class="hero-card-status-bar">
        <view class="hero-card-status-item">
          <view class="hero-card-status-dot hero-card-status-dot-success"/>
          <text class="hero-card-status-text">实名认证</text>
        </view>
        <view class="hero-card-status-item">
          <view class="hero-card-status-dot hero-card-status-dot-primary"/>
          <text class="hero-card-status-text">签约</text>
        </view>
        <view class="hero-card-status-item">
          <view class="hero-card-status-dot hero-card-status-dot-warn"/>
          <text class="hero-card-status-text">创建 PRO 会员</text>
        </view>
      </view>
    </view>

    <!-- ============================================================
         2. 核心资产数据罗盘 (Asset Metrics Compass)
         ============================================================ -->
    <view class="metrics-grid">
      <view
          v-for="(stat, idx) in accountStats"
          :key="stat.label"
          class="metric-item"
          :class="`metric-item-accent-${idx}`"
      >
        <text class="metric-item-value">{{ stat.value }}</text>
        <text class="metric-item-label">{{ stat.label }}</text>
      </view>
    </view>

    <!-- ============================================================
         3. 个人云存储配额进度可视化 (Cloud Storage Bar)
         ============================================================ -->
    <CloudStorageBar
        :used="usedStorage"
        :total="totalStorage"
        :subtitle="storageSubtitle"
    />

    <!-- ============================================================
         4. 积分充值横幅 (Banner 积分)
         ============================================================ -->
    <view class="recharge-banner" @tap="onRecharge">
      <image class="recharge-banner-img" :src="bannerImage" mode="aspectFill"/>
      <view class="recharge-banner-shade"/>
      <view class="recharge-banner-content">
        <text class="recharge-banner-title">积分充值</text>
        <text class="recharge-banner-subtitle">选择您的充值方式,解锁更多 AI 创作权益</text>
        <view class="recharge-banner-btn">
          <text class="recharge-banner-btn-text">立即查看</text>
        </view>
      </view>
    </view>

    <!-- ============================================================
         5. 操作记录 (还原原版 5 图标横排 + 全部操作)
         ============================================================ -->
    <view class="profile-section">
      <view class="section-header">
        <text class="section-title">操作记录</text>
        <text class="section-link" @tap="onAllActions">全部操作</text>
      </view>
      <view class="order-actions">
        <view
            class="order-item"
            v-for="item in orderActions"
            :key="item.label"
            @tap="onOrderAction(item)"
        >
          <view class="order-icon" :style="{ background: item.background }">
            <SvgIcon :icon="item.icon" class="order-icon-svg"/>
          </view>
          <text class="order-label">{{ item.label }}</text>
        </view>
      </view>
    </view>

    <!-- ============================================================
         6. 我的 (与下方"服务与偏好设置"使用同一套 menu-card 卡片样式)
         ============================================================ -->
    <MenuGroup label="我的" :items="myLinks"/>

    <!-- ============================================================
         7. 服务与偏好设置 (iOS 风格圆角群组卡片 —— 含原"系统设置")
         ============================================================ -->
    <MenuGroup label="服务与偏好设置" :items="settingsList"/>

    <!-- Web 端联动卡片 -->
    <ConnectionCardCard style="margin-top: 24rpx;"/>

    <!-- 底部退出/切换账号操作点 -->
    <view class="profile-foot">
      <view class="profile-foot-btn" @tap="onSwitchAccount">
        <text class="profile-foot-btn-text">切换身份或退出当前创作者账户</text>
      </view>
    </view>

    <AppTabbar/>
  </view>
</template>

<script setup>
import AppTabbar from '@/components/AppTabbar.vue'
import CloudStorageBar from '@/components/CloudStorageBar.vue'
import MenuGroup from '@/components/MenuGroup.vue'
import {computed, onMounted, ref} from 'vue'
import {useStore} from 'vuex'
import {useUser} from '@/utils/useUser'
import {initCover} from '@/utils/common'
import {getAccountInfo} from '@/api/points/account'
import {toast} from '@/utils/toast'
import SvgIcon from "@/components/SvgIcon.vue";
import ConnectionCardCard from "@/components/ConnectionCard.vue";

// ===================== 用户信息 —— 完全从 vuex 派生 =====================
// vuex state 是唯一真相源,store subscribe 自动写 storage + 广播;
// 这里只调一次 GetInfo action 触发 store 初始化,模板用 computed 派生
const store = useStore()
const {getInfo: getInfoAction, logout: logoutAction} = useUser()

const nickName = computed(() => store.state.user.nickName || '')
const userName = computed(() => store.state.user.userName || '')
const avatarUrl = computed(() => store.state.user.avatarUrl || '')

/**
 * 首次进入页面调一次 GetInfo
 */
const loadUserInfo = () => {
  getInfoAction().catch((e) => {
    console.warn('profile GetInfo 失败', e)
  })
}
// ===================== 账户信息 =====================
const accountInfo = ref({
  balance: 0,
  points: 0,
})

const accountStats = ref([
  {label: '积分余额', value: 38},
  {label: '赚取积分', value: 532},
  {label: '使用积分', value: 32},
  {label: '充值金额', value: 98}
])

const getAccount = () => {
  getAccountInfo().then((res) => {
    accountInfo.value = res.data
    accountStats.value = [
      {label: '积分余额', value: accountInfo.value.pointsBalance},
      {label: '赚取积分', value: accountInfo.value.pointsEarned},
      {label: '使用积分', value: accountInfo.value.pointsUsed},
      {label: '充值金额', value: accountInfo.value.rechargeAmount}
    ]
  })
}

// ===================== 云存储配额 =====================
// 字节数(后续接后端时,直接把 userInfo.usedStorage / totalStorage 这种数字字段塞进来即可)
const usedStorage = ref(3.4 * 1024 * 1024 * 1024)   // 演示数据:3.4 GB
const totalStorage = ref(10 * 1024 * 1024 * 1024)  // 演示数据:10 GB
const storageSubtitle = ref('RAW 原片 2.8 GB · 超清导出版 0.6 GB')

// ===================== 操作记录(原 orderActions 结构) =====================
const orderActions = [
  {label: '充值', icon: 'points-blue', type: 'points-blue', background: 'linear-gradient(135deg, #ffc7d6, #ff9fb1)'},
  {label: '下载', icon: 'download', type: 'download', background: 'linear-gradient(135deg, #f7d8ff, #d2abff)'},
  {label: '行为', icon: 'behavior', type: 'behavior', background: 'linear-gradient(135deg, #c7eaff, #7fd7ff)'},
  {label: '浏览', icon: 'view', type: 'view', background: 'linear-gradient(135deg, #fde1c9, #fbb27d)'},
  {label: '积分', icon: 'points', type: 'points', background: 'linear-gradient(135deg, #d7f5dc, #6fdc8a)'}
]

// ===================== 我的(原 quickLinks 拆出"系统设置"后保留两条) =====================
// 与 settingsList 使用同一份 MenuItem 形状(类型定义见 src/types/menu.d.ts)
// 不填写的字段(badge/tag/meta)在模板里走 v-if 隐藏
const myLinks = [
  {
    label: '我的',
    icon: 'my',
  },
  {
    label: '修改资料',
    icon: 'userEdit',
    path: '/pages/user/user/edit',
    meta: '修改个人资料和更新头像'
  },
  {
    label: '我的空间',
    icon: 'space',
  },
]

// ===================== 服务与偏好设置(原"系统设置"挪到这里 + 其他设置项) =====================
const settingsList = [
  {
    label: '系统设置',
    icon: 'system',
    badge: '',
    tag: '',
    meta: '',
    tagStyle: {},
    // 跳转路径:传了 path 的项,MenuGroup 内部会自己走 uni.navigateTo;没传的组件内部 toast 兜底
    path: '/pages/user/system/system',
  },
  {
    label: '系统通知与互动消息',
    icon: 'inform',
    badge: '3',
    tag: '',
    meta: '',
    tagStyle: {}
  },
  {
    label: '画质与偏好',
    icon: 'view',
    badge: '',
    tag: '',
    meta: 'WiFi 自动加载原图',
    tagStyle: {}
  },
  {
    label: '水印与版权保护设置',
    icon: 'picture',
    badge: '',
    tag: '',
    meta: '隐形数字盲水印',
    tagStyle: {}
  },
  {
    label: '关联 Web 端',
    icon: 'connection',
    badge: '',
    tag: '在线',
    meta: 'LZ-Picture 一键互联',
    tagStyle: {background: '#E8F8F0', color: '#07C160'}
  },
  {
    label: '关于荔智云图',
    icon: 'idea',
    badge: '',
    tag: '',
    meta: 'v2.4.0',
    tagStyle: {}
  }
]

// ===================== 横幅背景图 =====================
// 充值横幅背景图 —— 不带前导斜杠的相对路径,uni-app 编译器会拦截并改写为三端都能识别的资源
// 后续换图直接改这个字符串,图片请放在 static/ 根目录下
const bannerImage = '/static/recharge.jpeg'

// ===================== 点击事件(占位 toast,后续接路由) =====================
const onPreviewProfile = () => toast('预览个人主页')
const onRecharge = () => toast('尚未开发，请前往Web端操作')
const onAllActions = () => toast('查看全部操作记录')
const onOrderAction = (item) => toast(`查看${item.label}记录`)
/**
 * 退出当前登录
 */
const onSwitchAccount = () => logoutAction()

onMounted(() => {
  loadUserInfo()
  getAccount()
})
</script>

<style lang="scss" scoped>
/* ============================================================
 * 页面 token —— M3 surface 思路 + iOS 风格圆角群组卡片
 * 与 login.vue 同一套 $app-* 变量,主题色由 rootStyleVar 注入
 * ============================================================ */
$surface-lowest: #ffffff;
$surface-low: #f2f3ff;
$surface: #faf8ff;
$surface-container: #eaedff;
$surface-container-high: #e2e7ff;
$on-surface: #131b2e;
$on-surface-variant: #464555;
$outline: #777587;
$outline-variant: #c7c4d8;
$primary-container: #4f46e5;
$primary: #4f46e5;
$secondary: #b90538;

button {
  background-color: transparent;
  padding: 0;
  margin: 0;
  font-size: 28rpx;
  line-height: normal;

  &::after {
    border: none;
  }
}

/* ========== 页面结构 ========== */
.profile-page {
  position: relative;
  min-height: 100vh;
  padding: 32rpx 32rpx 220rpx; // 底部留 AppTabbar 高度
  background-color: $surface;
  box-sizing: border-box;
  overflow: hidden;
}

/* ========== 背景装饰光斑 ========== */
.bg-blur {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  z-index: 0;

  &-primary {
    top: -160rpx;
    right: -120rpx;
    width: 480rpx;
    height: 480rpx;
    background-color: rgba($primary-container, 0.18);
    filter: blur(120rpx);
  }

  &-secondary {
    top: 360rpx;
    left: -140rpx;
    width: 380rpx;
    height: 380rpx;
    background-color: rgba($surface-container-high, 0.7);
    filter: blur(80rpx);
  }
}

/* 公共层级:内容都浮在 bg-blur 之上 */
.hero-card,
.metrics-grid,
.recharge-banner,
.profile-section,
.profile-foot {
  position: relative;
  z-index: 1;
}

/* ============================================================
 * 1. Hero 创作者身份看板
 * ============================================================ */
.hero-card {
  position: relative;
  width: 100%;
  padding: 32rpx 32rpx 24rpx;
  border-radius: 32rpx;
  background-color: $surface-lowest;
  box-shadow: 0 8rpx 24rpx rgba(19, 27, 46, 0.05);
  overflow: hidden;
  box-sizing: border-box;
}

/* Hero 内部右上角淡主色光斑 */
.hero-card-deco {
  position: absolute;
  top: -60rpx;
  right: -60rpx;
  width: 220rpx;
  height: 220rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba($primary-container, 0.15), rgba($surface-container-high, 0.4));
  filter: blur(40rpx);
  pointer-events: none;
}

.hero-card-row {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.hero-card-id {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.hero-card-avatar-wrap {
  position: relative;
  flex-shrink: 0;
  width: 128rpx;
  height: 128rpx;
}

.hero-card-avatar {
  width: 128rpx;
  height: 128rpx;
  border-radius: 50%;
  background-color: $surface-container;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.06);
}

.hero-card-id-text {
  flex: 1;
  min-width: 0;
  margin-left: 24rpx;
  padding-right: 8rpx;
}

.hero-card-name-row {
  display: flex;
  align-items: center;
}

.hero-card-name {
  font-size: 36rpx;
  font-weight: 700;
  color: $on-surface;
  letter-spacing: -0.5rpx;
  max-width: 280rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.hero-card-badge-pill {
  display: inline-flex; // 改用 flex,让 line-height 居中行为可控
  align-items: center; // 文字垂直居中
  justify-content: center;
  margin-top: 10rpx;
  padding: 10rpx 12rpx;
  border-radius: 16rpx;
  background-color: var(--app-primary-light, rgba(79, 70, 229, 0.08));
  line-height: 1; // 容器锁死行高,避免继承撑高
}

.hero-card-badge-pill-text {
  font-size: 20rpx;
  font-weight: 700;
  color: var(--app-primary, #4f46e5);
  letter-spacing: 0.2rpx;
  line-height: 1; // 显式锁定,文字基线和容器居中对齐
}

.hero-card-uid {
  display: block;
  margin-top: 10rpx;
  font-size: 22rpx;
  color: $outline;
  letter-spacing: 0.5rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.hero-card-preview-btn {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  padding: 10rpx 18rpx;
  margin-top: 4rpx;
  border-radius: 999rpx;
  background-color: var(--app-primary-light, rgba(79, 70, 229, 0.08));
  transition: transform 0.15s, background-color 0.15s;

  &:active {
    transform: scale(0.96);
    background-color: var(--app-primary-disabled, rgba(79, 70, 229, 0.35));
  }
}

.hero-card-preview-icon {
  font-size: 26rpx;
  color: var(--app-primary, #4f46e5);
  line-height: 1;
}

.hero-card-preview-text {
  margin-left: 6rpx;
  font-size: 22rpx;
  font-weight: 500;
  color: var(--app-primary, #4f46e5);
  line-height: 1;
}

/* 状态标签条 */
.hero-card-status-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 28rpx;
  padding: 14rpx 24rpx;
  border-radius: 20rpx;
  background-color: rgba($surface-container, 0.6);
}

.hero-card-status-item {
  display: flex;
  align-items: center;
}

.hero-card-status-dot {
  width: 24rpx;
  height: 24rpx;
  border-radius: 50%;
  margin-right: 8rpx;
  flex-shrink: 0;

  &-success {
    background-color: #5ac725;
  }

  &-primary {
    background-color: $primary;
  }

  &-warn {
    background-color: #f9ae3d;
  }
}

.hero-card-status-text {
  font-size: 20rpx;
  font-weight: 700;
  color: $on-surface-variant;
  letter-spacing: 0.2rpx;
  line-height: 1.4;
}

/* ============================================================
 * 2. 核心资产数据罗盘
 * ============================================================ */
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16rpx;
  width: 100%;
  margin-top: 24rpx;
}

.metric-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24rpx 8rpx;
  border-radius: 24rpx;
  background-color: $surface-lowest;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.04);
  text-align: center;
}

.metric-item-value {
  display: block;
  font-size: 36rpx;
  font-weight: 700;
  color: $on-surface;
  letter-spacing: -0.5rpx;
  line-height: 1.2;
}

.metric-item-label {
  margin-top: 6rpx;
  font-size: 22rpx;
  color: $outline;
  line-height: 1.2;
}

/* 第 2 列强调色 secondary, 第 3 列 primary —— M3 多色对位 */
.metric-item-accent-1 .metric-item-value {
  color: $secondary;
}

.metric-item-accent-2 .metric-item-value {
  color: $primary;
}

/* ============================================================
 * 3. 云存储配额 —— 已抽出到 components/CloudStorageBar.vue
 *    这里不再保留 .storage-card* 样式,统一交给组件自己管理
 * ============================================================ */

/* ============================================================
 * 4. 积分充值横幅
 * ============================================================ */
.recharge-banner {
  position: relative;
  width: 100%;
  margin-top: 24rpx;
  height: 220rpx;
  border-radius: 32rpx;
  overflow: hidden;
  box-shadow: 0 10rpx 28rpx rgba(79, 70, 229, 0.25);
  transition: transform 0.15s;

  &:active {
    transform: scale(0.99);
  }
}

.recharge-banner-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.recharge-banner-shade {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  /* 渐变跟随主题色:rootStyleVar 注入的 --app-primary / --app-primary-dark / --app-primary-disabled */
  background: linear-gradient(
      270deg,
      var(--app-primary, #4f46e5) 0%,
      var(--app-primary-dark, #4338ca) 30%,
      var(--app-primary-disabled, #a5b4fc) 60%
  );
  opacity: 0.45; // 夜色再淡一点,让底图更通透
}

.recharge-banner-content {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  justify-content: center; // 文字高度垂直居中
  align-items: flex-end; // 文字整体居右
  text-align: right; // 多行 text 也右对齐
  width: 100%;
  height: 100%;
  padding: 0 32rpx;
  box-sizing: border-box;
  color: #ffffff;
}

.recharge-banner-title {
  font-size: 40rpx;
  font-weight: 700;
  letter-spacing: -0.5rpx;
  line-height: 1.2;
}

.recharge-banner-subtitle {
  margin-top: 12rpx;
  font-size: 24rpx;
  font-weight: 400;
  opacity: 0.92;
  line-height: 1.4;
}

.recharge-banner-btn {
  align-self: flex-end; // 文字整体居右时,按钮也跟着贴右
  margin-top: 20rpx;
  padding: 12rpx 28rpx;
  border-radius: 999rpx;
  background-color: rgba(255, 255, 255, 0.22);
  backdrop-filter: blur(8rpx);
}

.recharge-banner-btn-text {
  font-size: 24rpx;
  font-weight: 600;
  color: #ffffff;
  line-height: 1;
}

/* ============================================================
 * 5. 操作记录 (原 profile-section + order-actions 风格)
 * ============================================================ */
.profile-section {
  margin-top: 32rpx;
  background-color: $surface-lowest;
  border-radius: 32rpx;
  padding: 32rpx 28rpx 16rpx;
  box-shadow: 0 16rpx 40rpx rgba(28, 19, 77, 0.05);
  box-sizing: border-box;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #2f2f44;
}

.section-link {
  font-size: 24rpx;
  color: #a5a5bb;
}

.order-actions {
  display: flex;
  justify-content: space-between;
}

.order-item {
  flex: 1;
  text-align: center;
}

.order-icon {
  width: 88rpx;
  height: 88rpx;
  border-radius: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  margin: 0 auto 16rpx;
}

.order-icon-svg {
  font-size: 40rpx;
  color: #ffffff;
  line-height: 1;
}

.order-label {
  font-size: 24rpx;
  color: #63637a;
}

/* ============================================================
 * 8. 底部退出/切换账号
 * ============================================================ */
.profile-foot {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 32rpx;
  padding: 8rpx 0 0;
}

.profile-foot-btn {
  padding: 12rpx 32rpx;
  border-radius: 999rpx;
  transition: background-color 0.15s;

  &:active {
    background-color: $surface-container;
  }
}

.profile-foot-btn-text {
  font-size: 22rpx;
  color: $app-primary;
  font-weight: 500;
  letter-spacing: 0.5rpx;
  line-height: 1;
}
</style>