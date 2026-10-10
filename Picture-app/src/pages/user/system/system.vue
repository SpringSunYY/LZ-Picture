<template>
  <view class="system-page" :style="rootStyleVar">
    <view class="bg-blur bg-blur-primary"/>
    <view class="bg-blur bg-blur-secondary"/>

    <main class="sys-main">
      <!-- ============================================================
           2. Profile Snapshot Card
           ============================================================ -->
      <view class="sys-card sys-profile">
        <view class="sys-profile-left">
          <view class="sys-profile-avatar-wrap">
            <image
                class="sys-profile-avatar"
                :src="initCover(userInfo.avatarUrl)"
                mode="aspectFill"
            />
          </view>
          <view class="sys-profile-id">
            <view class="sys-profile-name-row">
              <text class="sys-profile-name">{{ userInfo.nickName || '未登录' }}</text>
              <text class="sys-profile-pro-badge">PRO</text>
            </view>
            <view class="sys-profile-sub-row">
              <text class="sys-profile-sub-id">LZ-ID: {{ userInfo.userName || '--' }}</text>
              <text class="sys-profile-sub-dot">·</text>
              <text class="sys-profile-sub-creator">认证创作者</text>
            </view>
          </view>
        </view>
        <view class="sys-profile-qr">
          <SvgIcon icon="my" width="1.1em"/>
        </view>
      </view>

      <!-- ============================================================
           3. 云端存储配额 —— 直接复用 CloudStorageBar 组件
           ============================================================ -->
      <CloudStorageBar
          :used="usedStorage"
          :total="totalStorage"
          subtitle="已安全多副本加密备份"
          expand-text="扩容套餐"
          :show-clean-btn="false"
      />

      <!-- ============================================================
           4. 账户与安全 —— MenuGroup 组件
           ============================================================ -->
      <view class="sys-section">
        <text class="sys-section-label">账户与安全</text>
        <MenuGroup :items="accountSecurity"/>
      </view>

      <!-- ============================================================
           5. 存储与缓存 —— MenuGroup 组件
           ============================================================ -->
      <view class="sys-section">
        <text class="sys-section-label">存储与主题</text>
        <MenuGroup :items="storageItems"/>
      </view>

      <!-- ============================================================
           6. 关于与通用 —— MenuGroup 组件
           ============================================================ -->
      <view class="sys-section">
        <text class="sys-section-label">关于与通用</text>
        <MenuGroup :items="aboutItems"/>
      </view>

      <!-- ============================================================
           7. 退出当前登录(底部按钮,确认用系统 showConfirm,与 profile.vue 一致)
           ============================================================ -->
      <view class="sys-foot">
        <view class="sys-foot-logout" @tap="onSwitchAccount">
          <SvgIcon icon="logout" class="sys-foot-logout-icon" width="1.1em"/>
          <text class="sys-foot-logout-text">退出当前登录</text>
        </view>
        <view class="sys-foot-tip">
          <SvgIcon icon="verifiedIdentity" width="0.85em" class="sys-foot-tip-icon"/>
          <text class="sys-foot-tip-text">
            退出后不会删除已同步的云端图片资产，重新登录即可恢复
          </text>
        </view>
      </view>
    </main>
  </view>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue'
import {useStore} from 'vuex'
import {useUser} from '@/utils/useUser'
import {getMyUserInfoByUserName} from '@/api/user/user'
import {initCover} from '@/utils/common'
import SvgIcon from '@/components/SvgIcon.vue'
import CloudStorageBar from '@/components/CloudStorageBar.vue'
import MenuGroup from '@/components/MenuGroup.vue'

defineOptions({
  name: 'SystemSettings',
})

// ===================== 主题 CSS 变量(与 profile.vue 一致) =====================
const store = useStore()

/**
 * 当前主题的中文名(展示在「主题偏好」菜单的 meta 上)
 * 走 getter 派生 —— 与 preferences.vue 的逻辑保持一致,
 * 用户在 preferences 页切换主题后,这里会自动跟随更新
 */
const currentThemeName = computed(() => store.getters.activeTheme?.name || '默认主题')

// ===================== 用户信息(从 store + 详情接口拉) =====================
const {getInfo: getInfoAction, logout: logoutAction} = useUser()

const userInfo = ref({
  nickName: '',
  userName: '',
  avatarUrl: '',
})

const loadUserInfo = () => {
  // store 优先拿兜底
  getInfoAction().catch((e) => {
    console.warn('store GetInfo 失败', e)
  })
  const u = store.state.user.userName
  if (!u) return
  getMyUserInfoByUserName(u).then((res) => {
    if (res?.data) userInfo.value = res.data
  })
}

// ===================== 云存储配额(演示数据,接后端后改为 userInfo 字段) =====================
const usedStorage = ref(3.4 * 1024 * 1024 * 1024)   // 3.4 GB
const totalStorage = ref(10 * 1024 * 1024 * 1024)  // 10 GB

// ===================== 三组菜单数据(直接喂给 MenuGroup) =====================
// 设计稿里要做的: 绑定手机号 / 微信授权 / 修改密码 / 实名认证  / 清理缓存 / 云存储管理 / 检查版本 / 隐私 / 协议与关于
const accountSecurity = [
  {
    label: '绑定手机号',
    icon: 'system',
    meta: store.state.user.phone || '****',
  },
  {
    label: '微信账号授权',
    icon: 'weChat',
    tag: '已授权认证',
    tagStyle: {background: '#eaedff', color: 'var(--app-primary, #4f46e5)'},
  },
  {
    label: '修改登录密码',
    icon: 'password',
    meta: '定期更换保护数字资产',
  },
  {
    label: '修改支付密码',
    icon: 'paymentPassword',
    meta: '保护数字资产',
    path:'/pages/user/auth/paymentPassword'
  },
  {
    label: '实名创作者认证',
    icon: 'verifiedIdentity',
    tag: '已核验',
    tagStyle: {background: '#eaedff', color: 'var(--app-primary, #4f46e5)'},
  },
]

// 存储与缓存: 小程序端 <image> 的网络缓存由微信内核层管理,JS 拿不到路径也无法清除,
const storageItems = [
  {
    label: '云存储空间配额管理',
    icon: 'cloud',
    meta: '3.4 GB / 10 GB',
  },
  {
    label: '主题偏好',
    icon: 'theme',
    meta: currentThemeName,
    path: '/pages/user/system/preferences',
  },
]

const aboutItems = [
  {
    label: '检查新版本',
    icon: 'reload',
    meta: 'v2.4.0 (最新)',
  },
  {
    label: '隐私与授权权限管理',
    icon: 'password',
  },
  {
    label: '开源协议与关于我们',
    icon: 'idea',
  },
]

/**
 * 退出当前登录
 */
const onSwitchAccount = () => logoutAction()

onMounted(() => {
  loadUserInfo()
})
</script>

<style lang="scss" scoped>
/* ============================================================
 * 页面专属 token —— 与 profile.vue / login.vue 同一套语言
 * ============================================================ */
$surface-lowest: #ffffff;
$surface-low: #f2f3ff;
$surface: #faf8ff;
$surface-container: #eaedff;
$on-surface: #131b2e;
$on-surface-variant: #464555;
$outline: #777587;
$outline-variant: #c7c4d8;
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

.system-page {
  position: relative;
  min-height: 100vh;
  padding: 24rpx 32rpx 60rpx;
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
    background-color: rgba($primary, 0.18);
    filter: blur(120rpx);
  }

  &-secondary {
    top: 360rpx;
    left: -140rpx;
    width: 380rpx;
    height: 380rpx;
    background-color: rgba($surface-container, 0.7);
    filter: blur(80rpx);
  }
}

/* ============================================================
 * 主体: 由 .sys-main 开始,顶部空间交给 uniapp 原生导航栏
 * ============================================================ */
.sys-main {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
}

/* ============================================================
 * 2. Profile Snapshot Card
 * ============================================================ */
.sys-card {
  background-color: $surface-lowest;
  border-radius: 24rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.05);
  box-sizing: border-box;
}

.sys-profile {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.sys-profile-left {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.sys-profile-avatar-wrap {
  position: relative;
  flex-shrink: 0;
  width: 112rpx;
  height: 112rpx;
}

.sys-profile-avatar {
  width: 112rpx;
  height: 112rpx;
  border-radius: 50%;
  background-color: $surface-container;
  object-fit: cover;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.06);
}

.sys-profile-id {
  flex: 1;
  min-width: 0;
  margin-left: 24rpx;
}

.sys-profile-name-row {
  display: flex;
  align-items: center;
}

.sys-profile-name {
  font-size: 30rpx;
  font-weight: 600;
  color: $on-surface;
  letter-spacing: -0.5rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 320rpx;
}

.sys-profile-pro-badge {
  margin-left: 12rpx;
  padding: 4rpx 10rpx;
  border-radius: 999rpx;
  background-color: var(--app-primary-light, rgba($primary, 0.08));
  font-size: 20rpx;
  font-weight: 700;
  color: var(--app-primary, $primary);
  letter-spacing: 0.5rpx;
  line-height: 1;
  flex-shrink: 0;
}

.sys-profile-sub-row {
  display: flex;
  align-items: center;
  margin-top: 6rpx;
}

.sys-profile-sub-id {
  font-size: 22rpx;
  color: $on-surface-variant;
  letter-spacing: 0.2rpx;
}

.sys-profile-sub-dot {
  margin: 0 8rpx;
  font-size: 18rpx;
  color: $outline-variant;
}

.sys-profile-sub-creator {
  font-size: 22rpx;
  font-weight: 500;
  color: var(--app-primary, $primary);
}

.sys-profile-qr {
  flex-shrink: 0;
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background-color: $surface-container;
  display: flex;
  align-items: center;
  justify-content: center;
  color: $on-surface-variant;
  transition: transform 0.15s;

  &:active {
    transform: scale(0.95);
  }
}

.sys-section {
  margin-top: 32rpx;
}

.sys-section-label {
  display: block;
  margin-bottom: 16rpx;
  padding: 0 8rpx;
  font-size: 22rpx;
  font-weight: 500;
  color: $outline;
  letter-spacing: 1rpx;
  line-height: 1.4;
  text-transform: uppercase;
}

/* ============================================================
 * 7. 退出登录 + 提示
 * ============================================================ */
.sys-foot {
  margin-top: 48rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.sys-foot-logout {
  width: 100%;
  height: 96rpx;
  border-radius: 24rpx;
  background-color: $app-primary-light; // error-container, 浅红色
  display: flex;
  align-items: center;
  justify-content: center;
  transition: opacity 0.15s, transform 0.15s;
  box-shadow: 0 4rpx 12rpx rgba(186, 26, 26, 0.1);

  &:active {
    opacity: 0.9;
    transform: scale(0.99);
  }
}

.sys-foot-logout-icon {
  margin-right: 12rpx;
  line-height: 1;
}

.sys-foot-logout-text {
  font-size: 30rpx;
  font-weight: 600;
  color: $app-primary;
  letter-spacing: 0.5rpx;
  line-height: 1;
}

.sys-foot-tip {
  display: flex;
  align-items: center;
  margin-top: 16rpx;
  padding: 0 24rpx;
}

.sys-foot-tip-icon {
  margin-right: 4rpx;
  color: $outline;
  flex-shrink: 0;
}

.sys-foot-tip-text {
  font-size: 22rpx;
  color: $outline;
  letter-spacing: 0.2rpx;
  line-height: 1.4;
}
</style>