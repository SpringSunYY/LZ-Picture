<template>
  <view class="user-edit-page" :style="rootStyleVar">
    <view class="bg-blur bg-blur--primary"/>
    <view class="bg-blur bg-blur--secondary"/>

    <main class="ue-main">
      <!-- ============================================================
           1. 顶部引导头 —— 图标 + 标题 + 说明
           ============================================================ -->
      <view class="ue-header">
        <view class="ue-header-icon">
          <SvgIcon icon="userEdit" width="3em"/>
        </view>
        <text class="ue-header-title">修改个人资料</text>
        <text class="ue-header-sub">
          头像直接上传即更新;昵称、性别、生日、职业、个人简介在底部一键保存.
        </text>
      </view>

      <!-- ============================================================
           2. 表单卡片
           ============================================================ -->
      <view class="form-card">
        <up-form
            :model="formModel"
            :rules="formRules"
            errorType="message"
            labelPosition="top"
            ref="userFormRef"
            labelWidth="80"
        >
          <!-- 头像 —— 上传即更新,与 PC 端 handleAvatarSuccess 行为一致 -->
          <up-form-item prop="avatarUrl" label="用户头像" borderBottom="false">
            <view class="ue-form-cover">
              <CoverUpload
                  v-model="formModel.avatarUrl"
                  empty-text="点击上传头像"
                  :readonly="avatarUploading"
                  @change="onAvatarUploaded"
              />
              <view v-if="avatarUploading" class="ue-cover-tip">
                <text class="ue-cover-tip-text">头像同步中...</text>
              </view>
            </view>
          </up-form-item>

          <!-- 用户名(只读) -->
          <up-form-item prop="userName" label="用户名" borderBottom="false">
            <up-input
                v-model="formModel.userName"
                shape="circle"
                type="text"
                :disabled="true"
                placeholder="用户名不可修改"
            >
              <template #prefix>
                <SvgIcon icon="user" width="1.5em"/>
              </template>
            </up-input>
          </up-form-item>

          <!-- 昵称 -->
          <up-form-item prop="nickName" label="昵称">
            <up-input
                v-model="formModel.nickName"
                shape="circle"
                type="text"
                :maxlength="16"
                placeholder="请输入昵称"
                clearable
            >
              <template #prefix>
                <SvgIcon icon="userEdit" width="1.5em"/>
              </template>
            </up-input>
          </up-form-item>

          <!-- 性别 -->
          <up-form-item prop="sex" label="性别" borderBottom="false">
            <up-radio-group
                v-model="formModel.sex"
                placement="row"
                shape="square"
                :active-color="primaryColor"
                gap="40rpx"
            >
              <up-radio
                  v-for="opt in sexOptions"
                  :key="opt.value"
                  :name="opt.value"
                  :label="opt.label"
              />
            </up-radio-group>
          </up-form-item>

          <!-- 生日 —— hasInput 自带输入框,v-model 绑定时间戳,confirm 后自动更新 -->
          <up-form-item prop="birthday" label="生日" borderBottom="false">
            <up-datetime-picker
                v-model="formModel.birthday"
                mode="date"
                hasInput
                format="YYYY-MM-DD"
                placeholder="请选择生日"
            />
          </up-form-item>

          <!-- 职业 -->
          <up-form-item prop="occupation" label="职业">
            <up-input
                v-model="formModel.occupation"
                shape="circle"
                type="text"
                :maxlength="16"
                placeholder="请输入职业"
                clearable
            >
              <template #prefix>
                <SvgIcon icon="idea" width="1.5em"/>
              </template>
            </up-input>
          </up-form-item>

          <!-- 个人简介 -->
          <up-form-item prop="introductory" label="个人简介" borderBottom="false">
            <up-textarea
                v-model="formModel.introductory"
                :maxlength="512"
                :count="true"
                :height="280"
                placeholder="简单介绍一下自己,让更多人认识你"
                border="none"
            />
          </up-form-item>
        </up-form>

        <TipCard text="头像上传成功后会自动保存到你的账户,其他资料修改后请点底部按钮一键保存."/>
      </view>

      <!-- ============================================================
           3. 提交按钮 —— 固定底部,跟随 safe-area
           ============================================================ -->
      <view
          class="primary-btn"
          :class="{ 'primary-btn--disabled': submitting }"
          @tap="onSubmit"
      >
        <text class="primary-btn-text">{{ submitting ? '保存中...' : '保存修改' }}</text>
      </view>
    </main>
  </view>
</template>

<script setup>
import {computed, reactive, ref} from 'vue'
import {onLoad} from '@dcloudio/uni-app'
import {useStore} from 'vuex'
import SvgIcon from '@/components/SvgIcon.vue'
import TipCard from '@/components/TipCard.vue'
import CoverUpload from '@/components/common/CoverUpload.vue'
import {
  getMyUserInfoByUserName,
  updateUserInfo,
  updateUserInfoAvatar,
} from '@/api/user/user'
import {toast, toastHide, toastLoading, toastWarn} from '@/utils/toast'
import {useDict} from '@/utils/useDict'
import {useUser} from '@/utils/useUser'
import {themePresets} from '@/constants/theme'

defineOptions({
  name: 'UserEdit',
})

definePage({
  style: {
    navigationBarTitleText: '修改个人资料',
  },
})

const store = useStore()
const {getInfo: getInfoAction} = useUser()

/** 当前主题主色 —— 与 AppTabbar.vue / space.vue 同款取法,给 up-radio 的激活色用 */
const primaryColor = computed(() => {
  const key = store.state.preferences?.theme?.key
  return themePresets[key]?.primary || '#4f46e5'
})

// ===================== 性别字典 =====================
// 与 Web 端 useDict('u_user_sex') 同源,字典未加载时退回静态文案,避免空选项
const {u_user_sex} = useDict('u_user_sex')

const FALLBACK_SEX = [
  {value: '0', label: '未知'},
  {value: '1', label: '男'},
  {value: '2', label: '女'},
]

const sexOptions = computed(() =>
    u_user_sex.value && u_user_sex.value.length
        ? u_user_sex.value.map((d) => ({value: d.dictValue, label: d.dictLabel}))
        : FALLBACK_SEX,
)

// ===================== 表单状态 =====================
const submitting = ref(false)
const avatarUploading = ref(false)
const userFormRef = ref(null)

/**
 * 生日:formModel.birthday 始终存 "YYYY-MM-DD" 字符串(给后端 + 表单校验用)
 * up-datetime-picker 内部要的是时间戳,所以额外用一个 ref 包住
 */
const formModel = reactive({
  userId: '',
  userName: '',
  avatarUrl: '',
  nickName: '',
  sex: '0',
  birthday: '',
  occupation: '',
  introductory: '',
})

/** 表单校验 —— uview-plus(底层 async-validator)规则,
 * 一个字段一个对象,内置 min/max/pattern/required 足够覆盖,无需自定义 validator */
const formRules = {
  nickName: {
    required: true,
    min: 2,
    max: 16,
    message: '昵称长度需在 2-16 字符之间',
    trigger: ['blur', 'change'],
  },
  introductory: {
    max: 512,
    message: '个人简介不能超过 512 字符',
    trigger: ['blur', 'change'],
  },
  birthday: {
    pattern: /^\d{4}-\d{2}-\d{2}$/,
    message: '生日格式应为 YYYY-MM-DD',
    trigger: ['change'],
  },
}

/**
 * 完整复刻 profile.vue#getUserInfo 的两步拉数据:
 *   1) store GetInfo action -> 自动 commit 基础字段(userName/nickName/avatar/phone/userId)
 *   2) getMyUserInfoByUserName -> 完整 MyUserInfo(头像/性别/生日/职业/简介)用于表单回填
 * 表单回填完后,把详情推给全局缓存 + 广播,
 * profile 那边只要在 onShow 里读一下就能无刷新同步
 */
const loadUserInfo = async () => {
  // 1) 让 store 走 GetInfo 拉一次,内部 commit 把基础字段写进 vuex
  await getInfoAction().catch((e) => {
    console.warn('store GetInfo 失败', e)
  })

  const username = store.state.user.userName
  if (!username) {
    toastWarn('未登录,无法加载个人资料')
    return
  }
  try {
    const res = await getMyUserInfoByUserName(username)
    if (res?.data) {
      const u = res.data
      formModel.userId = u.userId ?? ''
      formModel.userName = u.userName ?? username
      formModel.avatarUrl = u.avatarUrl ?? ''
      formModel.nickName = u.nickName ?? ''
      formModel.sex = u.sex ?? '0'
      formModel.birthday = String(u.birthday ?? '').trim()
      formModel.occupation = u.occupation ?? ''
      formModel.introductory = u.introductory ?? ''
    }
  } catch (e) {
    toastWarn('获取个人信息失败,请稍后重试')
  }
}

onLoad(() => {
  uni.setNavigationBarTitle({title: '修改个人资料'})
  loadUserInfo()
})

// ===================== 头像 —— 上传即更新 =====================
/**
 * CoverUpload 上传成功后会 emit('change', body),body 形如 { code, msg, data }
 * data 里有 url / thumbnailUrl,与 PC 端 handleAvatarSuccess 行为一致
 *
 * 注意:
 * - 用户在 query 里强调"头像上传就直接更新了,不要点击提交才更新"
 * - 所以这里直接调 updateUserInfoAvatar,不走 onSubmit 的批量提交
 * - CoverUpload 已经把 modelValue(avatarUrl)更新好了,这里只负责把新 URL 同步给后端
 */
const onAvatarUploaded = async (body) => {
  if (!body || !body.data) return
  const url = body.data.thumbnailUrl || body.data.url
  if (!url) {
    toastWarn('头像上传成功,但未拿到地址')
    return
  }
  if (!formModel.userId) {
    // 详情还没拉回来就上传了,这里兜底从 store 取
    formModel.userId = String(store.state.user.userId || '')
  }
  if (!formModel.userId) {
    toastWarn('用户信息未就绪,请稍后再试')
    return
  }
  avatarUploading.value = true
  toastLoading('头像同步中...')
  try {
    const res = await updateUserInfoAvatar({
      userId: formModel.userId,
      avatarUrl: url,
    })
    if (res?.code === 200) {
      toastHide()
      toast('头像更新成功')
      // 1) 让 store 走 GetInfo,把新头像/昵称 commit 进 vuex(由 GetInfo 内部统一 commit)
      await getInfoAction().catch((e) => console.warn('头像后 GetInfo 失败', e))
      // store subscribe 会自动把头像 commit 同步到 storage + 广播
    } else {
      throw new Error(res?.msg || '头像更新失败')
    }
  } catch (e) {
    toastHide()
    toastWarn(e?.message || '头像更新失败')
  } finally {
    avatarUploading.value = false
  }
}

// ===================== 提交 =====================
const onSubmit = () => {
  if (submitting.value) return
  userFormRef.value?.setRules(formRules)
  userFormRef.value?.validate()
      .then(() => doSubmit())
      .catch((error) => {
        const first = Array.isArray(error) ? error[0] : error
        toastWarn(first?.message || '请检查输入')
      })
}

const doSubmit = async () => {
  submitting.value = true
  toastLoading('保存中...')
  try {
    // birthday 是空串时不要传字段,避免后端校验失败;
    // 其余字段统一 trim,杜绝空格入库
    const payload = {
      userId: formModel.userId,
      nickName: String(formModel.nickName ?? '').trim(),
      sex: formModel.sex,
      birthday: String(formModel.birthday ?? '').trim() || undefined,
      occupation: String(formModel.occupation ?? '').trim(),
      introductory: String(formModel.introductory ?? '').trim(),
    }
    const res = await updateUserInfo(payload)
    if (res?.code === 200) {
      toastHide()
      toast('资料修改成功')
      // 1) 让 store 走 GetInfo,把新昵称/头像 commit 进 vuex(由 GetInfo 内部统一 commit)
      await getInfoAction().catch((e) => console.warn('资料后 GetInfo 失败', e))
      // 给 toast 一帧时间渲染再回退;profile 在 onShow 里读 store 派生即可
      setTimeout(() => {
        uni.navigateBack({delta: 1})
      }, 600)
    } else {
      throw new Error(res?.msg || '资料修改失败')
    }
  } catch (e) {
    toastHide()
    toastWarn(e?.message || '资料修改失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style lang="scss" scoped>
$surface-lowest: #ffffff;
$surface: #faf8ff;
$surface-container: #e2e7ff;
$surface-container-high: #dae2fd;
$on-surface: #131b2e;
$on-surface-variant: #464555;
$outline: #777587;
$outline-variant: #c7c4d8;
$primary-container: #4f46e5;
$ue-radius-lg: 32rpx;
$ue-radius-md: 24rpx;
$ue-radius-xl: 40rpx;

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
.user-edit-page {
  position: relative;
  width: 100%;
  min-height: 100vh;
  background-color: $surface;
  /* 底部留出提交按钮 + safe-area */
  padding: 24rpx 32rpx 180rpx;
  box-sizing: border-box;
  overflow: hidden;
}

.bg-blur {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  z-index: 0;

  &--primary {
    top: -160rpx;
    right: -120rpx;
    width: 480rpx;
    height: 480rpx;
    background-color: rgba($primary-container, 0.18);
    filter: blur(120rpx);
  }

  &--secondary {
    top: 420rpx;
    left: -140rpx;
    width: 380rpx;
    height: 380rpx;
    background-color: rgba($surface-container, 0.7);
    filter: blur(80rpx);
  }
}

/* 内容浮在 bg-blur 之上 */
.ue-main,
.ue-header,
.form-card {
  position: relative;
  z-index: 1;
}

.ue-main {
  display: flex;
  flex-direction: column;
}

/* ========== 引导头 ========== */
.ue-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16rpx 0 8rpx;
  text-align: center;
}

.ue-header-icon {
  width: 150rpx;
  height: 150rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: $app-primary-light;
}

.ue-header-title {
  margin-top: 24rpx;
  font-size: 38rpx;
  font-weight: 700;
  color: $on-surface;
  letter-spacing: -1rpx;
  line-height: 1.2;
}

.ue-header-sub {
  margin-top: 10rpx;
  max-width: 560rpx;
  font-size: 22rpx;
  color: $outline;
  letter-spacing: 0.2rpx;
  line-height: 1.5;
}

/* ========== 表单卡片 ========== */
.form-card {
  width: 100%;
  margin-top: 32rpx;
  padding: 36rpx 32rpx 28rpx;
  border-radius: $ue-radius-xl;
  background-color: $surface-lowest;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.05);
  box-sizing: border-box;
}

.ue-form-cover {
  width: 100%;
  position: relative;
}

.ue-cover-tip {
  margin-top: 12rpx;
  display: flex;
  justify-content: center;
}

.ue-cover-tip-text {
  font-size: 22rpx;
  color: $app-primary;
  letter-spacing: 0.2rpx;
  line-height: 1.4;
}

/* ========== 提交按钮 ========== */
.primary-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 92rpx;
  margin-top: 32rpx;
  border-radius: $ue-radius-md;
  background-color: $app-primary;
  box-shadow: 0 6rpx 16rpx rgba(79, 70, 229, 0.3);
  transition: transform 0.15s, opacity 0.15s;

  &:active {
    transform: scale(0.99);
  }

  &--disabled {
    opacity: 0.6;
  }
}

.primary-btn-text {
  font-size: 30rpx;
  font-weight: 600;
  color: #ffffff;
  line-height: 1;
}
</style>