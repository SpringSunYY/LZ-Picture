<template>
  <div class="user-profile">
    <div class="profile-header">
      <div class="avatar-section">
        <div class="avatar-wrapper">
          <a-avatar
            :size="{ xs: 180, sm: 200, md: 240, lg: 240, xl: 240, xxl: 240 }"
            :src="formatDnsUrl(userInfo?.avatarUrl) || '/placeholder.svg?height=240&width=240'"
          />
          <!--          <div class="status-badge" :class="'status-' + user.status">-->
          <!--            {{ getStatusText(user.status) }}-->
          <!--          </div>-->
        </div>
      </div>
      <div class="user-intro">
        <h1 class="user-name">{{ userInfo?.nickName || '未设置昵称' }}</h1>
        <p class="user-occupation" style="color: #00ff95">
          {{ userInfo?.occupation || '未设置职业' }}
        </p>
        <p class="user-bio">{{ userInfo?.introductory || '这个人很懒，还没有填写个人简介...' }}</p>
        <div class="user-meta">
          <div class="meta-item">
            <span class="meta-icon"><i class="fas fa-mobile-alt"></i></span>
            {{ userInfo?.countryCode ? `${userInfo?.countryCode}` : '' }}
            {{ userInfo?.phone || '未设置' }}
          </div>
          <div class="meta-item">
            <span class="meta-icon"><i class="fas fa-venus-mars"></i></span>
            <dict-tag :options="u_user_sex" :value="userInfo?.sex" />
          </div>
          <div class="meta-item">
            <span class="meta-icon"><i class="fas fa-birthday-cake"></i></span>
            {{ userInfo?.birthday }}
          </div>
          <div class="meta-item">
            <span class="meta-icon"><i class="fas fa-globe"></i></span>
            {{ userInfo?.ipAddress || '未知' }}
          </div>
        </div>
      </div>
    </div>

    <div class="profile-stats">
      <div class="stat-card">
        <div class="stat-value">{{ accountInfo?.pointsBalance || 0 }}</div>
        <div class="stat-label">积分余额</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ accountInfo?.pointsEarned || 0 }}</div>
        <div class="stat-label">赚取积分</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ accountInfo?.pointsUsed || 0 }}</div>
        <div class="stat-label">使用积分</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ accountInfo?.rechargeAmount || 0 }}</div>
        <div class="stat-label">充值金额(元)</div>
      </div>
    </div>

    <div class="profile-details">
      <a-tabs default-active-key="1">
        <template #rightExtra>
          <!-- 桌面端显示 -->
          <div class="desktop-buttons" style="padding:10px">
            <a-space direction="horizontal" align="center" :wrap="true">
              <a-button @click="handleUpdateAvatar">修改头像</a-button>
              <a-button @click="handleUpdateUserInfo">修改信息</a-button>
              <a-button @click="handleUpdatePassword">修改密码</a-button>
              <a-button @click="handleUpdateAccountPassword">修改支付密码</a-button>
            </a-space>
          </div>

          <!-- 移动端显示下拉菜单 -->
          <div class="mobile-buttons" style="padding:10px">
            <a-dropdown :trigger="['click']" placement="bottomRight">
              <a-button >
                操作
                <DownOutlined />
              </a-button>
              <template #overlay>
                <a-menu>
                  <a-menu-item key="avatar" @click="handleUpdateAvatar">
                    <UserOutlined style="margin-right: 8px;" />
                    修改头像
                  </a-menu-item>
                  <a-menu-item key="info" @click="handleUpdateUserInfo">
                    <EditOutlined style="margin-right: 8px;" />
                    修改信息
                  </a-menu-item>
                  <a-menu-item key="password" @click="handleUpdatePassword">
                    <LockOutlined style="margin-right: 8px;" />
                    修改密码
                  </a-menu-item>
                  <a-menu-item key="account-password" @click="handleUpdateAccountPassword">
                    <SafetyOutlined style="margin-right: 8px;" />
                    修改支付密码
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
        </template>
        <a-tab-pane key="1" v-if="checkLogin()" tab="基本信息">
          <div class="details-grid">
            <!--            <div class="detail-item">-->
            <!--              <div class="detail-label">用户ID</div>-->
            <!--              <div class="detail-value">{{ user.userId }}</div>-->
            <!--            </div>-->
            <div class="detail-item">
              <div class="detail-label">用户名</div>
              <div class="detail-value">{{ userInfo?.userName }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">昵称</div>
              <div class="detail-value">{{ userInfo?.nickName }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">职业</div>
              <div class="detail-value">{{ userInfo?.occupation }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">手机号码</div>
              <div class="detail-value">{{ userInfo?.phone }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">账户状态</div>
              <div class="detail-value">
                <dict-tag :options="u_user_status" :value="userInfo?.status" />
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-label">性别</div>
              <div class="detail-value">
                <dict-tag :options="u_user_sex" :value="userInfo?.sex" />
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-label">IP属地</div>
              <div class="detail-value">{{ userInfo?.ipAddress }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">注册时间</div>
              <div class="detail-value">{{ userInfo?.createTime }}</div>
            </div>
          </div>
        </a-tab-pane>
        <a-tab-pane key="2" v-if="checkLogin()" tab="登录日志">
          <UserloginLogTable></UserloginLogTable>
        </a-tab-pane>
        <a-tab-pane v-if="checkPermiSingle('points')" key="3" tab="充值记录">
          <PointsRechargeTable></PointsRechargeTable>
        </a-tab-pane>
        <a-tab-pane v-if="checkPermiSingle('points')" key="4" tab="积分使用记录">
          <UserPointsUsageLog></UserPointsUsageLog>
        </a-tab-pane>
        <a-tab-pane v-if="checkPermiSingle('picture:download')" key="5" tab="图片下载记录">
          <PictureDownloadLogInfoTable></PictureDownloadLogInfoTable>
        </a-tab-pane>
        <a-tab-pane v-if="checkPermiSingle('picture:userBehaviorInfo')" key="6" tab="行为记录">
          <UserBehaviorTable></UserBehaviorTable>
        </a-tab-pane>
        <a-tab-pane v-if="checkPermiSingle('picture:list')" key="7" tab="浏览记录">
          <UserViewInfoTable></UserViewInfoTable>
        </a-tab-pane>
      </a-tabs>
    </div>
    <!--修改头像-->
    <a-modal
      v-model:open="openAvatar"
      :footer="null"
      :width="600"
      :closable="false"
      centered
      destroyOnClose
    >
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          {{ title }}
          <a-tooltip title="请上传您的头像，上传成功后就更新你的头像啦">
            <question-circle-outlined class="title-tip-icon" />
          </a-tooltip>
        </div>
      </template>
      <AvatarUpload @upload-success="handleAvatarSuccess" />
    </a-modal>
    <!--修改用户信息-->
    <a-modal v-model:open="open" :footer="null" :width="600" centered destroyOnClose>
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          {{ title }}
          <a-tooltip title="请输入您的基本信息">
            <question-circle-outlined class="title-tip-icon" />
          </a-tooltip>
        </div>
      </template>
      <a-form
        :model="formState"
        @finish="handleSubmit"
        ref="formRef"
        labelAlign="left"
        :rules="rules"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="昵称" name="nickName">
          <a-input
            v-model:value="formState.nickName"
            showCount
            :maxlength="16"
            placeholder="请输入昵称"
          />
        </a-form-item>
        <a-form-item label="性别" name="sex">
          <a-radio-group v-model:value="formState.sex" name="radioGroup">
            <a-radio v-for="dict in u_user_sex" :value="dict.dictValue" :key="dict.dictValue">
              {{ dict.dictLabel }}
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="生日" name="birthday">
          <a-date-picker
            v-model:value="formState.birthday"
            placeholder="请选择生日"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="职业" name="occupation">
          <a-input
            v-model:value="formState.occupation"
            showCount
            :maxlength="16"
            placeholder="请输入职业"
          />
        </a-form-item>
        <a-form-item label="个人简介" name="introductory">
          <a-textarea
            v-model:value="formState.introductory"
            placeholder="请输入个人简介"
            :rows="5"
            showCount
            :maxlength="512"
          />
        </a-form-item>
        <div class="form-footer">
          <a-button @click="open = false">取消</a-button>
          <a-button type="primary" html-type="submit" :loading="submitting"> 提交</a-button>
        </div>
      </a-form>
    </a-modal>
    <!--修改密码-->
    <a-modal v-model:open="openPassword" :footer="null" :width="500" centered destroyOnClose>
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          {{ title }}
          <a-tooltip title="一定要记住你的密码哦">
            <question-circle-outlined class="title-tip-icon" />
          </a-tooltip>
        </div>
      </template>
      <a-form
        :model="passwordForm"
        @finish="handleSubmitPassword"
        ref="formRef"
        labelAlign="left"
        :rules="rulesPassword"
      >
        <!-- 旧密码 -->
        <a-form-item name="oldPassword" label="旧的密码">
          <a-input-password
            v-model:value="passwordForm.oldPassword"
            placeholder="旧密码"
            :maxLength="20"
            size="large"
          >
            <template #prefix>
              <lock-outlined />
            </template>
          </a-input-password>
        </a-form-item>
        <!-- 新密码 -->
        <a-form-item name="password" label="新的密码">
          <a-input-password
            v-model:value="passwordForm.password"
            placeholder="新密码"
            :maxLength="20"
            size="large"
          >
            <template #prefix>
              <lock-outlined />
            </template>
          </a-input-password>
        </a-form-item>

        <!-- 确认密码 -->
        <a-form-item name="confirmPassword" label="确认密码">
          <a-input-password
            v-model:value="passwordForm.confirmPassword"
            placeholder="确认新密码"
            :maxLength="20"
            size="large"
          >
            <template #prefix>
              <lock-outlined />
            </template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" block size="large" :loading="passwordLoading">
            重置密码
          </a-button>
        </a-form-item>
      </a-form>
    </a-modal>
    <!--修改账户密码-->
    <a-modal v-model:open="openAccountPassword" :footer="null" :width="500" centered destroyOnClose>
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          {{ title }}
          <a-tooltip title="忘记密码可以重置密码，新账户也可以直接重置哦，但是一定要记住你的密码哦">
            <question-circle-outlined class="title-tip-icon" />
          </a-tooltip>
        </div>
      </template>
      <a-form
        :model="accountPasswordForm"
        @finish="handleSubmitAccountPassword"
        ref="formRef"
        labelAlign="left"
        :rules="rulesAccountPassword"
      >
        <!-- 旧密码 -->
        <!--        <a-form-item name="oldPassword" label="旧的密码">-->
        <!--          <a-input-password-->
        <!--            v-model:value="accountPasswordForm.oldPassword"-->
        <!--            placeholder="旧密码"-->
        <!--            :maxLength="20"-->
        <!--            size="large"-->
        <!--          >-->
        <!--            <template #prefix>-->
        <!--              <lock-outlined />-->
        <!--            </template>-->
        <!--          </a-input-password>-->
        <!--        </a-form-item>-->
        <!-- 国家码选择 -->
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item name="countryCode">
              <a-select
                v-model:value="accountPasswordForm.countryCode"
                size="large"
                placeholder="+86"
                show-search
                :disabled="true"
                option-filter-prop="label"
              >
                <a-select-option
                  v-for="country in countryList"
                  :key="country.dialCode"
                  :value="country.dialCode"
                  :label="`${country.name} ${country.dialCode}`"
                >
                  {{ country.flag }} {{ country.dialCode }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <!-- 手机号输入 -->
          <a-col :span="16">
            <a-form-item name="phone">
              <a-input v-model:value="accountPasswordForm.phone" placeholder="手机号" size="large">
                <template #prefix>
                  <phone-outlined />
                </template>
              </a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 图形验证码 -->
        <a-row :gutter="16" v-if="captchaEnabled">
          <a-col :span="8">
            <a-form-item name="code">
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img" alt="图形验证码" />
              </div>
            </a-form-item>
          </a-col>
          <a-col :span="16">
            <a-form-item name="code">
              <a-input
                v-model:value="accountPasswordForm.code"
                placeholder="图形验证码"
                size="large"
              />
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <!-- 短信验证码 -->
            <a-form-item name="smsCode">
              <a-input
                v-model:value="accountPasswordForm.smsCode"
                placeholder="短信验证码"
                size="large"
              >
                <template #suffix>
                  <a-button type="primary" :disabled="countdown > 0" @click="sendVerificationCode">
                    {{ countdown > 0 ? countdown + '秒' : '发送验证码' }}
                  </a-button>
                </template>
              </a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <!-- 新密码 -->
        <a-form-item name="password" label="">
          <a-input-password
            v-model:value="accountPasswordForm.password"
            placeholder="新密码"
            :maxLength="20"
            size="large"
          >
            <template #prefix>
              <lock-outlined />
            </template>
          </a-input-password>
        </a-form-item>

        <!-- 确认密码 -->
        <a-form-item name="confirmPassword" label="">
          <a-input-password
            v-model:value="accountPasswordForm.confirmPassword"
            placeholder="确认新密码"
            :maxLength="20"
            size="large"
          >
            <template #prefix>
              <lock-outlined />
            </template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            block
            size="large"
            :loading="accountPasswordLoading"
          >
            重置密码
          </a-button>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts" name="userinfo">
import { getCurrentInstance, ref } from 'vue'
import {
  type MyUserInfo,
  type UserInfoUpdate,
  type UserPasswordUploadRequest,
} from '@/types/user/user.d.ts'
import useUserStore from '@/stores/modules/user.ts'
import { storeToRefs } from 'pinia'
import {
  getMyUserInfoByUserName,
  updateUserInfo,
  updateUserInfoAvatar,
  updateUserInfoPassword,
} from '@/api/user/user.ts'
import {
  DownOutlined,
  EditOutlined,
  LockOutlined,
  PhoneOutlined,
  QuestionCircleOutlined,
  SafetyOutlined,
  UserOutlined,
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { validateConfirmPassword, validatePassword } from '@/types/user/validators.d.ts'
import type { ResetAccountPasswordBody } from '@/types/points/account.d.ts'
import {
  getAccountInfo,
  getAccountPasswordCode,
  resetAccountPassword,
} from '@/api/points/account.ts'
import UserBehaviorTable from '@/components/UserBehaviorTable.vue'
import DictTag from '@/components/DictTag.vue'
import PointsRechargeTable from '@/components/PointsRechargeTable.vue'
import { checkLogin, checkPermiSingle } from '@/utils/permission.ts'
import UserViewInfoTable from '@/components/UserViewInfoTable.vue'
import UserPointsUsageLog from '@/components/UserPointsUsageLog.vue'
import PictureDownloadLogInfoTable from '@/components/PictureDownloadLogInfoTable.vue'
import type { AccountInfoVo } from '@/types/points/account'
import UserloginLogTable from '@/components/UserloginLogTable.vue'
import { getCodeImg } from '@/api/user/login'
import { parsePhoneNumberFromString } from 'libphonenumber-js'
import AvatarUpload from '@/components/AvatarUpload.vue'
import { formatDnsUrl } from '@/utils/common.ts'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { u_user_sex, u_user_status } = proxy?.useDict('u_user_sex', 'u_user_status')

const userStore = useUserStore()
const { userName: userName } = storeToRefs(userStore) // 使用 storeToRefs 提取响应式状态
//用户数据
const userInfo = ref<MyUserInfo>()
//账户数据
const accountInfo = ref<AccountInfoVo>()
//获取用户信息
const getMyUserInfo = async () => {
  getAccountInfo().then((res) => {
    accountInfo.value = res.data
  })
  const res = await getMyUserInfoByUserName(userName.value)
  if (res.code === 200) {
    userInfo.value = res.data
  }
}
getMyUserInfo()

const open = ref(false)
const title = ref('修改用户信息')
const submitting = ref(false)
const formState = ref<UserInfoUpdate>({
  userId: '',
  nickName: '',
  sex: '0',
  occupation: '',
  introductory: '',
  birthday: null,
})

const rules = {
  nickName: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' },
  ],
  sex: [{ required: true, message: '请选择性别', trigger: 'blur' }],
}

const openPassword = ref(false)
const passwordLoading = ref(false)
const passwordForm = ref<UserPasswordUploadRequest>({
  userId: '',
  password: '',
  confirmPassword: '',
  oldPassword: '',
})
const rulesPassword = {
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      trigger: 'blur',
      validator: (_: any, value: string) => validatePassword(value, 8, 20),
    },
  ],
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' },
    {
      trigger: 'blur',
      validator: (_: any, value: string) => validatePassword(value, 8, 20),
    },
  ],
  confirmPassword: [
    { required: true, message: '请输入确认密码', trigger: 'blur' },
    {
      trigger: 'blur',
      validator: (_: any, value: string) => validatePassword(value, 8, 20),
    },
    {
      validator: (_: any, value: string) =>
        validateConfirmPassword(passwordForm.value.password, value),
      trigger: 'blur',
    },
  ],
}
const handleUpdatePassword = () => {
  openPassword.value = true
  title.value = '修改密码'
}
const handleSubmitPassword = async () => {
  passwordLoading.value = true
  passwordForm.value.userId = userInfo.value?.userId || ''
  try {
    const res = await updateUserInfoPassword(passwordForm.value)
    if (res.code === 200 && res.data === 1) {
      message.success('修改密码成功')
      openPassword.value = false
      passwordForm.value = {
        userId: '',
        password: '',
        confirmPassword: '',
        oldPassword: '',
      }
    } else {
      message.error('修改密码失败，请检查密码是否正确')
    }
  } finally {
    passwordLoading.value = false
  }
}

//账户密码
const countryList = ref([
  { code: 'CN', name: '中国', dialCode: '+86', flag: '🇨🇳' },
  { code: 'US', name: '美国', dialCode: '+1', flag: '🇺🇸' },
  { code: 'GB', name: '英国', dialCode: '+44', flag: '🇬🇧' },
])
const openAccountPassword = ref(false)
const accountPasswordLoading = ref(false)
const accountPasswordForm = ref<ResetAccountPasswordBody>({
  password: '',
  confirmPassword: '',
  countryCode: '+86',
  phone: '',
  smsCode: '',
  userId: '',
})
const rulesAccountPassword = {
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      trigger: 'blur',
      validator: (_: any, value: string) => validatePassword(value, 6, 10),
    },
  ],
  confirmPassword: [
    { required: true, message: '请输入确认密码', trigger: 'blur' },
    {
      trigger: 'blur',
      validator: (_: any, value: string) => validatePassword(value, 6, 10),
    },
    {
      validator: (_: any, value: string) =>
        validateConfirmPassword(accountPasswordForm.value.password, value),
      trigger: 'blur',
    },
  ],
  smsCode: [{ required: true, message: '请输入短信验证码', trigger: 'blur' }],
  code: [{ required: true, message: '请输入图形验证码', trigger: 'blur' }],
  countryCode: [{ required: true, message: '请选择国家代码', trigger: 'change' }],
  phone: [
    {
      validator: (_, value) => {
        const fullNumber = accountPasswordForm.value.countryCode + value
        const phoneNumber = parsePhoneNumberFromString(fullNumber)
        return phoneNumber?.isValid() ? Promise.resolve() : Promise.reject('请输入有效的国际手机号')
      },
      trigger: 'blur',
    },
  ],
}
const handleUpdateAccountPassword = () => {
  openAccountPassword.value = true
  getCode()
  title.value = '修改账户密码'
}
const handleSubmitAccountPassword = async () => {
  accountPasswordLoading.value = true
  accountPasswordForm.value.userId = userInfo.value?.userId || ''
  try {
    const res = await resetAccountPassword(accountPasswordForm.value)
    if (res.code === 200) {
      message.success('修改账户密码成功')
      openAccountPassword.value = false
      accountPasswordForm.value = {
        password: '',
        confirmPassword: '',
        countryCode: '+86',
        phone: '',
        smsCode: '',
        userId: '',
      }
    } else {
      message.error('修改密码失败,请检查密码是否正确')
    }
  } finally {
    accountPasswordLoading.value = false
  }
}
// 获取图形验证码
const captchaEnabled = ref(true)
const codeUrl = ref('')
const countdown = ref(0)
let timer = null
const getCode = () => {
  if (!captchaEnabled.value) return
  getCodeImg().then((res) => {
    //@ts-ignore
    captchaEnabled.value = res.captchaEnabled ?? true
    if (captchaEnabled.value) {
      //@ts-ignore
      codeUrl.value = 'data:image/gif;base64,' + res?.img
      //@ts-ignore
      accountPasswordForm.value.uuid = res?.uuid
    }
  })
}
const isSending = ref(false)
// 发送验证码
const sendVerificationCode = async () => {
  try {
    // 验证国际手机号
    const fullNumber = accountPasswordForm.value.countryCode + accountPasswordForm.value.phone
    const phoneNumber = parsePhoneNumberFromString(fullNumber)
    if (!phoneNumber?.isValid()) {
      message.error('手机号格式错误')
      return
    }

    // 验证图形验证码
    if (!accountPasswordForm.value.code) {
      message.error('请先输入图形验证码')
      return
    }
    accountPasswordForm.value.userId = userInfo.value?.userId || ''
    // 调用发送接口
    const res = await getAccountPasswordCode({
      countryCode: accountPasswordForm.value.countryCode,
      phone: accountPasswordForm.value.phone,
      code: accountPasswordForm.value.code,
      uuid: accountPasswordForm.value.uuid,
      userId: accountPasswordForm.value.userId,
    })
    if (res.code !== 200) {
      message.error(res.msg)
    }
    message.success('验证码已发送')
    isSending.value = true
    countdown.value = 60
    // 倒计时处理
    timer = setInterval(() => {
      if (countdown.value > 0) {
        countdown.value--
      } else {
        clearInterval(timer)
        isSending.value = false
      }
    }, 1000)
  } catch (error) {
    console.log('验证失败:', error)
    message.error('验证码发送失败')
    isSending.value = false
    getCode() // 刷新图形验证码
  }
}

const handleUpdateUserInfo = () => {
  //清除原来的数据
  formState.value = {}
  formState.value = {
    userId: userInfo.value?.userId || '',
    nickName: userInfo.value?.nickName || '',
    sex: userInfo.value?.sex || '0',
    birthday: userInfo.value?.birthday ? dayjs(userInfo.value.birthday) : null,
    occupation: userInfo.value?.occupation || '',
    introductory: userInfo.value?.introductory || '',
  }
  console.log(formState)
  open.value = true
}
//修改用户信息
const handleSubmit = async () => {
  console.log(formState.value)
  const res = await updateUserInfo(formState.value)
  if (res.code === 200) {
    message.success('修改成功')
    await getMyUserInfo()
    open.value = false
  }
}

//修改头像
const openAvatar = ref(false)
const avatarUrl = ref('')
const handleUpdateAvatar = () => {
  title.value = '修改头像'
  openAvatar.value = true
}

const handleAvatarSuccess = async (value: any) => {
  avatarUrl.value = value
  console.log('上传成功，头像地址：', value)
  const res = await updateUserInfoAvatar({
    avatarUrl: value.data?.thumbnailUrl || '',
    userId: userInfo.value?.userId || '',
  })
  if (res.code === 200) {
    message.success('更新头像成功')
    await getMyUserInfo()
    openAvatar.value = false
  }
}
</script>

<style lang="scss" scoped>
// Variables
$primary-color: #3f51b5;
$secondary-color: #7986cb;
$background-color: #f5f7fa;
$card-bg-color: #ffffff;
$text-primary: #333333;
$text-secondary: #666666;
$text-light: #999999;
$success-color: #4caf50;
$warning-color: #ff9800;
$error-color: #f44336;
$border-radius: 12px;
$box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
$green-color: #4caf50;
$blue-color: #2196f3;
$red-color: #f44336;
$purple-color: #9c27b0;

.custom-modal-title {
  display: flex;
  align-items: center;
  font-size: 16px;

  .title-tip-icon {
    margin-left: 8px;
    color: rgba(57, 57, 57, 0.45);
    cursor: help;
    transition: color 0.3s;

    &:hover {
      color: #1890ff;
    }
  }
}

.form-footer {
  text-align: right;
  padding: 16px 0 0;
  margin-top: 24px;
  border-top: 1px solid #f0f0f0;

  .ant-btn {
    margin-left: 10px;
  }
}

.user-profile {
  max-width: 1440px;
  margin: 0 auto;
  padding: 30px 20px;
  //background-color: $background-color;
  //min-height: 100vh;
  opacity: 0;
  animation: fadeIn 2s ease forwards;

  .profile-header {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 40px;

    @media (min-width: 768px) {
      flex-direction: row;
      align-items: flex-start;
    }

    .avatar-section {
      margin-bottom: 30px;

      @media (min-width: 768px) {
        margin-bottom: 0;
        margin-right: 40px;
      }

      .avatar-wrapper {
        position: relative;

        .status-badge {
          position: absolute;
          bottom: 10px;
          right: 10px;
          padding: 4px 12px;
          border-radius: 20px;
          font-size: 12px;
          font-weight: 600;
          color: white;

          &.status-0 {
            background-color: $success-color;
          }

          &.status-1 {
            background-color: $warning-color;
          }

          &.status-2 {
            background-color: $error-color;
          }
        }
      }
    }

    .user-intro {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: center;

      .user-name {
        font-size: 32px;
        font-weight: 700;
        margin-bottom: 8px;
        color: $text-primary;
      }

      .user-occupation {
        font-size: 18px;
        color: $text-secondary;
        margin-bottom: 16px;
      }

      .user-bio {
        font-size: 16px;
        line-height: 1.6;
        color: $text-secondary;
        margin-bottom: 24px;
        //max-width: 600px;
      }

      .user-meta {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;

        .meta-item {
          display: flex;
          align-items: center;
          color: $text-secondary;
          font-size: 14px;

          .meta-icon {
            margin-right: 8px;
            color: $secondary-color;
          }
        }
      }
    }
  }

  .profile-stats {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
    margin-bottom: 40px;

    @media (min-width: 768px) {
      grid-template-columns: repeat(4, 1fr);
    }

    .stat-card {
      background-color: $card-bg-color;
      border-radius: $border-radius;
      padding: 24px;
      text-align: center;
      box-shadow: $box-shadow;
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-5px);
      }

      &:nth-child(1) .stat-value {
        color: $green-color; // 积分余额 - 绿色
      }

      &:nth-child(2) .stat-value {
        color: $blue-color; // 赚取积分 - 蓝色
      }

      &:nth-child(3) .stat-value {
        color: $red-color; // 使用积分 - 红色
      }

      &:nth-child(4) .stat-value {
        color: $purple-color; // 充值金额 - 紫色
      }

      .stat-value {
        font-size: 28px;
        font-weight: 700;
        margin-bottom: 8px;
      }

      .stat-label {
        font-size: 14px;
        color: $text-secondary;
      }
    }
  }

  .profile-details {
    background-color: $card-bg-color;
    border-radius: $border-radius;
    padding: 20px;
    box-shadow: $box-shadow;

    .details-grid {
      display: grid;
      grid-template-columns: repeat(1, 1fr);
      gap: 24px;

      @media (min-width: 576px) {
        grid-template-columns: repeat(2, 1fr);
      }

      @media (min-width: 992px) {
        grid-template-columns: repeat(4, 1fr);
      }

      .detail-item {
        .detail-label {
          font-size: 14px;
          color: $text-light;
          margin-bottom: 8px;
        }

        .detail-value {
          font-size: 16px;
          font-weight: 500;
        }
      }
    }
  }
}

@keyframes fadeIn {
  to {
    opacity: 1;
  }
}

@media (max-width: 576px) {
  .desktop-buttons {
    display: none;
  }
  .mobile-buttons {
    display: block;
  }
}

@media (min-width: 577px) {
  .desktop-buttons {
    display: block;
  }
  .mobile-buttons {
    display: none;
  }
}
:deep(.ant-dropdown-menu-item) {
  display: flex !important;
  align-items: center !important;

  .anticon {
    vertical-align: baseline !important;
    line-height: 1 !important;
  }
}
</style>
