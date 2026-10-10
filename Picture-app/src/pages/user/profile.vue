<template>
  <view class="profile-page" :style="rootStyleVar">
    <!-- 顶部背景装饰光斑（M3 surface tint 思路：极淡的主色对位） -->
    <view class="bg-blur bg-blur-primary"/>
    <view class="bg-blur bg-blur-secondary"/>

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
                :src="initCover(userInfo.avatarUrl)"
                mode="aspectFill"
            />
          </view>

          <view class="hero-card-id-text">
            <view class="hero-card-name-row">
              <text class="hero-card-name">{{ userInfo.nickName }}</text>
            </view>

            <view class="hero-card-badge-pill">
              <text class="hero-card-badge-pill-text">LZ-Picture 创作者</text>
            </view>

            <text class="hero-card-uid">LZ-ID: {{ userInfo.userName }}</text>
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
    <view class="storage-card">
      <view class="storage-card-head">
        <view class="storage-card-head-left">
          <view class="storage-card-icon">
            <SvgIcon icon="cloud" class="storage-card-icon-svg"/>
          </view>
          <text class="storage-card-title">云端无损存储空间</text>
        </view>

        <view class="storage-card-gift-btn" @tap="onExpandStorage">
          <SvgIcon icon="points" class="storage-card-gift-icon"/>
          <text class="storage-card-gift-text">扩容礼包</text>
        </view>
      </view>

      <view class="storage-card-meta">
        <text class="storage-card-meta-text">{{ formatSize(usedStorage) }} / {{ formatSize(totalStorage) }}</text>
        <text class="storage-card-meta-percent">{{ storagePercent }}%</text>
      </view>

      <view class="storage-card-progress">
        <view class="storage-card-progress-bar" :style="{ width: storagePercent + '%' }"/>
      </view>

      <view class="storage-card-foot">
        <text class="storage-card-foot-text">{{ storageSubtitle }}</text>
        <view class="storage-card-clean" @tap="onSmartClean">
          <text class="storage-card-clean-text">智能清理</text>
          <SvgIcon icon="right" class="storage-card-clean-arrow"/>
        </view>
      </view>
    </view>

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
    <view class="menu-group">
      <text class="menu-group-label">我的</text>
      <view class="menu-card">
        <view
            v-for="(link, i) in myLinks"
            :key="link.label"
            class="menu-item"
            :class="{ 'menu-item-divider': i !== myLinks.length - 1 }"
            @tap="onQuickLink(link)"
        >
          <view class="menu-item-icon">
            <SvgIcon :icon="link.icon" class="menu-item-icon-svg"/>
          </view>
          <view class="menu-item-label-wrap">
            <text class="menu-item-label">{{ link.label }}</text>
          </view>
          <view class="menu-item-trail">
            <SvgIcon icon="right" class="menu-item-arrow"/>
          </view>
        </view>
      </view>
    </view>

    <!-- ============================================================
         7. 服务与偏好设置 (iOS 风格圆角群组卡片 —— 含原"系统设置")
         ============================================================ -->
    <view class="menu-group">
      <text class="menu-group-label">服务与偏好设置</text>

      <view class="menu-card">
        <view
            v-for="(item, i) in settingsList"
            :key="item.label"
            class="menu-item"
            :class="{ 'menu-item-divider': i !== settingsList.length - 1 }"
            @tap="onSettingsItem(item)"
        >
          <view class="menu-item-icon">
            <SvgIcon :icon="item.icon" class="menu-item-icon-svg"/>
          </view>

          <view class="menu-item-label-wrap">
            <text class="menu-item-label">{{ item.label }}</text>
            <view v-if="item.tag" class="menu-item-inline-tag" :style="item.tagStyle">
              <text class="menu-item-inline-tag-text">{{ item.tag }}</text>
            </view>
          </view>

          <view class="menu-item-trail">
            <view v-if="item.badge" class="menu-item-dot-badge">
              <text class="menu-item-dot-badge-text">{{ item.badge }}</text>
            </view>
            <text v-else-if="item.meta" class="menu-item-meta">{{ item.meta }}</text>
            <SvgIcon icon="right" class="menu-item-arrow"/>
          </view>
        </view>
      </view>
    </view>

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
import {onMounted, ref} from 'vue'
import {useStore} from 'vuex'
import {useUser} from '@/utils/useUser'
import {getMyUserInfoByUserName} from '@/api/user/user'
import {formatSize, initCover} from '@/utils/common'
import {getAccountInfo} from '@/api/points/account'
import {toast, showConfirm} from '@/utils/toast'
import SvgIcon from "@/components/SvgIcon.vue";

// ===================== 用户信息(走 useUser,沿用 login.vue 的封装) =====================
const store = useStore()
const {getInfo: getInfoAction, logout: logoutAction} = useUser()

const userInfo = ref({
  nickName: '',
  userName: '',
  avatarUrl: ''
})

/**
 * 用户 store 里只有 userName / nickName / avatar,头像可能没值
 * 详情接口返回完整 userInfo(nickName / userName / avatarUrl)
 * 这里两个都拉: store 优先做兜底,接口拉详情覆盖
 */
const getUserInfo = () => {
  // 1) 先让 store 走 GetInfo 拉一次,确保 token 有效 + 兜底基础字段
  getInfoAction().catch((e) => {
    console.warn('store GetInfo 失败', e)
  })

  // 2) 再用详情接口拿完整 userInfo(头像 URL 等)
  const username = store.state.user.userName
  if (!username) {
    console.warn('当前未登录，Vuex 中没有 userName')
    return
  }
  getMyUserInfoByUserName(username).then(res => {
    if (res?.data) userInfo.value = res.data
  })
}
// ===================== 账户信息(保留原结构不动) =====================
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
const storagePercent = ref(Math.round((usedStorage.value / totalStorage.value) * 100))
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
    tagStyle: {}
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
const onExpandStorage = () => toast('扩容礼包活动')
const onSmartClean = () => toast('智能清理:扫描大文件与重复内容')
const onRecharge = () => toast('进入积分充值页')
const onAllActions = () => toast('查看全部操作记录')
const onOrderAction = (item) => toast(`查看${item.label}记录`)
const onQuickLink = (item) => toast(`进入${item.label}`)
const onSettingsItem = (item) => toast(`打开${item.label}`)
/**
 * 切换 / 退出账户
 * 必须先弹模态确认 —— 用户点"确定"才走 logout + reLaunch 到登录页
 * 后端 /logout 是有 token 时才调用,失败也会继续清本地(避免后端抖动把页面卡住)
 */
const onSwitchAccount = async () => {
  // 未登录直接跳登录页,不再弹确认框
  if (!store.state.user.token) {
    uni.reLaunch({url: '/pages/user/login'})
    return
  }
  const res = await showConfirm('确定要退出当前创作者账户吗?退出后需要重新登录。')
  if (!res.confirm) return
  try {
    await logoutAction()
    toast('已退出登录')
  } catch (e) {
    // 即便 store 那侧抛错,本地态已经被清理,继续跳转兜底
    console.warn('退出登录过程出错,继续跳登录页', e)
  }
  uni.reLaunch({url: '/pages/user/login'})
}

onMounted(() => {
  getUserInfo()
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
.storage-card,
.recharge-banner,
.profile-section,
.menu-group,
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
 * 3. 云存储配额
 * ============================================================ */
.storage-card {
  width: 100%;
  margin-top: 24rpx;
  padding: 32rpx;
  border-radius: 32rpx;
  background-color: $surface-lowest;
  box-shadow: 0 6rpx 18rpx rgba(19, 27, 46, 0.04);
  box-sizing: border-box;
}

.storage-card-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 18rpx;
}

.storage-card-head-left {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.storage-card-icon {
  width: 40rpx;
  height: 40rpx;
  border-radius: 12rpx;
  background-color: $surface-container;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12rpx;
  flex-shrink: 0;
}

.storage-card-icon-svg {
  font-size: 24rpx;
  color: $primary;
  line-height: 1;
}

.storage-card-title {
  font-size: 28rpx;
  font-weight: 500;
  color: $on-surface;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.storage-card-gift-btn {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  padding: 8rpx 16rpx;
  border-radius: 999rpx;
  background-color: var(--app-primary-light, rgba(79, 70, 229, 0.08));
  transition: transform 0.15s, background-color 0.15s;

  &:active {
    transform: scale(0.96);
    background-color: var(--app-primary-disabled, rgba(79, 70, 229, 0.35));
  }
}

.storage-card-gift-icon {
  font-size: 22rpx;
  color: var(--app-primary, #4f46e5);
  line-height: 1;
}

.storage-card-gift-text {
  margin-left: 6rpx;
  font-size: 20rpx;
  font-weight: 700;
  color: var(--app-primary, #4f46e5);
  line-height: 1;
}

.storage-card-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12rpx;
}

.storage-card-meta-text {
  font-size: 22rpx;
  color: $on-surface-variant;
  letter-spacing: -0.5rpx;
}

.storage-card-meta-percent {
  font-size: 22rpx;
  font-weight: 700;
  color: $primary;
  font-family: 'JetBrains Mono', monospace;
}

.storage-card-progress {
  width: 100%;
  height: 10rpx;
  border-radius: 999rpx;
  background-color: $surface-container;
  overflow: hidden;
  padding: 1rpx;
  box-sizing: border-box;
}

.storage-card-progress-bar {
  height: 100%;
  border-radius: 999rpx;
  background: linear-gradient(90deg, $primary-container, $primary, $secondary);
  transition: width 0.4s ease-out;
}

.storage-card-foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 14rpx;
  padding-top: 4rpx;
}

.storage-card-foot-text {
  font-size: 20rpx;
  color: $outline;
  letter-spacing: 0.2rpx;
  flex: 1;
  margin-right: 16rpx;
}

.storage-card-clean {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  padding: 6rpx 14rpx;
  border-radius: 999rpx;
  background-color: var(--app-primary-light, rgba(79, 70, 229, 0.08));
  transition: background-color 0.15s;

  &:active {
    background-color: var(--app-primary-disabled, rgba(79, 70, 229, 0.35));
  }
}

.storage-card-clean-text {
  font-size: 20rpx;
  font-weight: 700;
  color: var(--app-primary, #4f46e5);
  line-height: 1;
}

.storage-card-clean-arrow {
  margin-left: 4rpx;
  font-size: 22rpx;
  color: var(--app-primary, #4f46e5);
  line-height: 1;
}

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
 * 7. 服务与偏好设置 + 我的 共享 iOS 风格圆角群组卡片
 *    (原 6. 我的 的 .profile-links / .link-item 样式已统一到这里)
 * ============================================================ */
.menu-group {
  width: 100%;
  margin-top: 32rpx;
}

.menu-group-label {
  display: block;
  margin-bottom: 12rpx;
  padding: 0 8rpx;
  font-size: 22rpx;
  font-weight: 500;
  color: $outline;
  letter-spacing: 0.5rpx;
  line-height: 1.4;
}

.menu-card {
  width: 100%;
  border-radius: 24rpx;
  background-color: $surface-lowest;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.04);
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 28rpx 32rpx;
  box-sizing: border-box;
  transition: background-color 0.15s;

  &:active {
    background-color: $surface-container;
  }
}

/* 用纯色细线分层替代 border —— 避免 border 与 padding 冲突 */
.menu-item-divider {
  position: relative;

  &::after {
    content: '';
    position: absolute;
    left: 96rpx; // 图标宽 64 + 间距 20 + 左 padding 32 - 2 = 114, 这里取 96 贴齐 label 起点
    right: 32rpx;
    bottom: 0;
    height: 1rpx;
    background-color: $surface-low;
  }
}

.menu-item-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 18rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
  // 统一淡色背景 + 主题色图标(原数据里的 iconStyle 全部移除了,改由 SvgIcon 自动读取主题色)
  background-color: rgba($primary-container, 0.08);
  color: $primary;
}

.menu-item-icon-svg {
  font-size: 32rpx;
  line-height: 1;
}

.menu-item-label-wrap {
  flex: 1;
  display: flex;
  align-items: center;
  min-width: 0;
}

.menu-item-label {
  font-size: 28rpx;
  font-weight: 500;
  color: $on-surface;
  line-height: 1;
  white-space: nowrap;
}

.menu-item-inline-tag {
  margin-left: 10rpx;
  padding: 4rpx 10rpx;
  border-radius: 999rpx;
  flex-shrink: 0;
}

.menu-item-inline-tag-text {
  font-size: 18rpx;
  font-weight: 700;
  line-height: 1.2;
}

.menu-item-trail {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.menu-item-meta {
  font-size: 22rpx;
  font-weight: 500;
  color: $outline;
  margin-right: 8rpx;
  line-height: 1;
}

.menu-item-arrow {
  font-size: 28rpx;
  color: $outline-variant;
  line-height: 1;
}

/* 红点角标 */
.menu-item-dot-badge {
  width: 32rpx;
  height: 32rpx;
  border-radius: 50%;
  background-color: $secondary;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 8rpx;
  flex-shrink: 0;
}

.menu-item-dot-badge-text {
  font-size: 18rpx;
  font-weight: 700;
  color: #ffffff;
  line-height: 1;
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
  font-weight: 500;
  color: $outline;
  letter-spacing: 0.5rpx;
  line-height: 1;
}
</style>