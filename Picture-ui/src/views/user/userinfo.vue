<template>
  <div class="user-profile">
    <div class="profile-header">
      <div class="avatar-section">
        <div class="avatar-wrapper">
          <a-avatar
            :size="{ xs: 180, sm: 200, md: 240, lg: 240, xl: 240, xxl: 240 }"
            :src="userInfo?.avatarUrl || '/placeholder.svg?height=240&width=240'"
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
        <div class="stat-value">{{ userInfo?.pointsBalance || 0 }}</div>
        <div class="stat-label">积分余额</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ userInfo?.pointsEarned || 0 }}</div>
        <div class="stat-label">赚取积分</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ userInfo?.pointsUsed || 0 }}</div>
        <div class="stat-label">使用积分</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ userInfo?.rechargeAmount || 0 }}</div>
        <div class="stat-label">充值金额(元)</div>
      </div>
    </div>

    <div class="profile-details">
      <a-tabs default-active-key="1">
        <template #rightExtra>
          <a-space :size="[8, 16]" wrap style="max-width: 350px">
            <a-button @click="handleUpdateUserInfo">修改信息</a-button>
            <a-button @click="handleUpdatePassword">修改密码</a-button>
            <a-button @click="handleUpdateAccountPassword">修改支付密码</a-button>
          </a-space>
        </template>
        <a-tab-pane key="1" tab="基本信息">
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
        <a-tab-pane key="2" tab="登录信息">
          <div class="details-grid" v-for="item in userInfo?.loginLogInfoVos" :key="item.infoId">
            <div class="detail-item">
              <div class="detail-label">登录时间</div>
              <div class="detail-value">{{ item?.loginTime }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">登录方式</div>
              <div class="detail-value">
                <dict-tag :options="u_login_type" :value="item?.loginType" />
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-label">IP属地</div>
              <div class="detail-value">{{ item?.loginLocation || '未知' }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">IP地址</div>
              <div class="detail-value">{{ item?.ipaddr || '未知' }}</div>
            </div>
          </div>
        </a-tab-pane>
        <a-tab-pane v-if="checkPermiSingle('points')" key="3" tab="充值记录">
          <PointsRechargeTable></PointsRechargeTable>
        </a-tab-pane>
        <a-tab-pane v-if="checkPermiSingle('picture:userBehaviorInfo')" key="4" tab="行为记录">
          <UserBehaviorTable></UserBehaviorTable>
        </a-tab-pane>
        <a-tab-pane v-if="checkPermiSingle('picture:list')" key="5" tab="浏览记录">
          <UserViewInfoTable></UserViewInfoTable>
        </a-tab-pane>
      </a-tabs>
    </div>

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
          <a-tooltip title="初始账户密码已经通过短信发送至您的手机，一定要记住你的密码哦">
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
        <a-form-item name="oldPassword" label="旧的密码">
          <a-input-password
            v-model:value="accountPasswordForm.oldPassword"
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
        <a-form-item name="confirmPassword" label="确认密码">
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
import { getMyUserInfoByUserName, updateUserInfo, updateUserInfoPassword } from '@/api/user/user.ts'
import { LockOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { validateConfirmPassword, validatePassword } from '@/types/user/validators.d.ts'
import type { AccountPasswordUploadRequest } from '@/types/points/account.d.ts'
import { updateAccountPassword } from '@/api/points/account.ts'
import UserBehaviorTable from '@/components/UserBehaviorTable.vue'
import DictTag from '@/components/DictTag.vue'
import PointsRechargeTable from '@/components/PointsRechargeTable.vue'
import { checkPermiSingle } from '@/utils/permission.ts'
import UserViewInfoTable from '@/components/UserViewInfoTable.vue'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { u_user_sex, u_user_status, u_login_type } = proxy?.useDict(
  'u_user_sex',
  'u_user_status',
  'u_login_type',
)

const userStore = useUserStore()
const { userName: userName } = storeToRefs(userStore) // 使用 storeToRefs 提取响应式状态
// 模拟用户数据
const userInfo = ref<MyUserInfo>()

//获取用户信息
const getMyUserInfo = async () => {
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
const openAccountPassword = ref(false)
const accountPasswordLoading = ref(false)
const accountPasswordForm = ref<AccountPasswordUploadRequest>({
  userId: '',
  password: '',
  confirmPassword: '',
  oldPassword: '',
})
const rulesAccountPassword = {
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      trigger: 'blur',
      validator: (_: any, value: string) => validatePassword(value, 6, 10),
    },
  ],
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' },
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
}
const handleUpdateAccountPassword = () => {
  openAccountPassword.value = true
  title.value = '修改账户密码'
}
const handleSubmitAccountPassword = async () => {
  accountPasswordLoading.value = true
  accountPasswordForm.value.userId = userInfo.value?.userId || ''
  try {
    const res = await updateAccountPassword(accountPasswordForm.value)
    if (res.code === 200 && res.data === 1) {
      message.success('修改账户密码成功')
      openAccountPassword.value = false
      accountPasswordForm.value = {
        userId: '',
        password: '',
        confirmPassword: '',
        oldPassword: '',
      }
    } else {
      message.error('修改密码失败,请检查密码是否正确')
    }
  } finally {
    accountPasswordLoading.value = false
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
    padding: 30px;
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
</style>
