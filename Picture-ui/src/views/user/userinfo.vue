<template>
  <div class="user-profile">
    <div class="profile-header">
      <div class="avatar-section">
        <div class="avatar-wrapper">
          <a-avatar
            :size="{ xs: 180, sm: 200, md: 240, lg: 240, xl: 240, xxl: 240 }"
            :src="userInfo.avatarUrl || '/placeholder.svg?height=240&width=240'"
          />
          <!--          <div class="status-badge" :class="'status-' + userInfo.status">-->
          <!--            {{ getStatusText(userInfo.status) }}-->
          <!--          </div>-->
        </div>
      </div>
      <div class="user-intro">
        <h1 class="user-name">{{ userInfo.nickName || '未设置昵称' }}</h1>
        <p class="user-occupation">{{ userInfo.occupation || '未设置职业' }}</p>
        <p class="user-bio">{{ userInfo.introductory || '这个人很懒，还没有填写个人简介...' }}</p>
        <div class="user-meta">
          <div class="meta-item">
            <span class="meta-icon"><i class="fas fa-mobile-alt"></i></span>
            {{ userInfo.countryCode ? `+${userInfo.countryCode}` : '' }}
            {{ userInfo.phone || '未设置' }}
          </div>
          <div class="meta-item">
            <span class="meta-icon"><i class="fas fa-venus-mars"></i></span>
            {{ getSexText(userInfo.sex) }}
          </div>
          <div class="meta-item">
            <span class="meta-icon"><i class="fas fa-birthday-cake"></i></span>
            {{ formatDate(userInfo.birthday) }}
          </div>
          <div class="meta-item">
            <span class="meta-icon"><i class="fas fa-globe"></i></span>
            {{ userInfo.preferredLanguageLocale || '未设置' }}
          </div>
          <div class="meta-item">
            <span class="meta-icon"><i class="fas fa-globe"></i></span>
            {{ userInfo.ipAddress || '未知' }}
          </div>
        </div>
      </div>
    </div>

    <div class="profile-stats">
      <div class="stat-card">
        <div class="stat-value">{{ userInfo.pointsBalance || 0 }}</div>
        <div class="stat-label">积分余额</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ userInfo.pointsEarned || 0 }}</div>
        <div class="stat-label">赚取积分</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ userInfo.pointsUsed || 0 }}</div>
        <div class="stat-label">使用积分</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ userInfo.rechargeAmount || 0 }}</div>
        <div class="stat-label">充值金额(元)</div>
      </div>
    </div>

    <div class="profile-details">
      <a-tabs default-active-key="1">
        <a-tab-pane key="1" tab="账户信息">
          <div class="details-grid">
            <!--            <div class="detail-item">-->
            <!--              <div class="detail-label">用户ID</div>-->
            <!--              <div class="detail-value">{{ userInfo.userId }}</div>-->
            <!--            </div>-->
            <div class="detail-item">
              <div class="detail-label">用户名</div>
              <div class="detail-value">{{ userInfo.userName }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">昵称</div>
              <div class="detail-value">{{ userInfo.nickName }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">职业</div>
              <div class="detail-value">{{ userInfo.occupation }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">手机号码</div>
              <div class="detail-value">{{ userInfo.phone }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">账户状态</div>
              <div class="detail-value">
                <a-tag :color="getAccountStatusColor(userInfo.accountStatus)">
                  {{ getAccountStatusText(userInfo.accountStatus) }}
                </a-tag>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-label">性别</div>
              <div class="detail-value">
                <a-tag color="red">
                  {{ getSexText(userInfo.sex) }}
                </a-tag>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-label">IP属地</div>
              <div class="detail-value">{{ userInfo.ipAddress }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">注册时间</div>
              <div class="detail-value">{{ formatDate(userInfo.createTime) }}</div>
            </div>
          </div>
        </a-tab-pane>
        <a-tab-pane key="2" tab="登录信息">
          <div class="details-grid">
            <div class="detail-item">
              <div class="detail-label">最后登录时间</div>
              <div class="detail-value">{{ formatDate(userInfo.lastLoginTime) }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">最后登录IP</div>
              <div class="detail-value">{{ userInfo.lastLoginIp || '未知' }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">IP属地</div>
              <div class="detail-value">{{ userInfo.ipAddress || '未知' }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">时间</div>
              <div class="detail-value">{{ formatDate(userInfo.updateTime) }}</div>
            </div>
          </div>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>

<script setup lang="ts" name="userinfo">
import { ref } from 'vue'

// 模拟用户数据
const userInfo = ref({
  userId: 'U1001234567',
  userName: 'zhangsan',
  phone: '13812345678',
  countryCode: '86',
  nickName: '张三',
  avatarUrl:
    'https://litchi-picture.oss-cn-guangzhou.aliyuncs.com/picture/YY00075T-1909672678168596480-compressed.webp',
  password: '******',
  status: '0',
  salt: '******',
  sex: '1',
  birthday: '1990-01-01 00:00:00',
  occupation: '软件工程师',
  preferredLanguageLocale: '中文',
  introductory:
    '热爱生活，热爱编程。专注于前端开发和用户体验设计，喜欢尝试新技术，空闲时间喜欢摄影和旅行。热爱生活，热爱编程。专注于前端开发和用户体验设计，喜欢尝试新技术，空闲时间喜欢摄影和旅行。热爱生活，热爱编程。专注于前端开发和用户体验设计，喜欢尝试新技术，空闲时间喜欢摄影和旅行。热爱生活，热爱编程。专注于前端开发和用户体验设计，喜欢尝试新技术，空闲时间喜欢摄影和旅行。热爱生活，热爱编程。专注于前端开发和用户体验设计，喜欢尝试新技术，空闲时间喜欢摄影和旅行。',
  ipAddress: '北京',
  lastLoginTime: '2023-05-20 15:30:45',
  lastLoginIp: '192.168.1.1',
  createTime: '2022-01-01 10:00:00',
  updateTime: '2023-05-20 15:30:45',
  isDelete: '0',
  pointsEarned: 5000,
  pointsUsed: 3200,
  rechargeAmount: 1500.0,
  accountStatus: '0',
  pointsBalance: 1800,
})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '未知'
  return dateString
}

// 获取性别文本
const getSexText = (sex) => {
  const sexMap = {
    0: '未知',
    1: '男',
    2: '女',
  }
  return sexMap[sex] || '未知'
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '正常',
    1: '异常',
    2: '禁用',
  }
  return statusMap[status] || '未知'
}

// 获取状态颜色
const getStatusColor = (status) => {
  const colorMap = {
    0: 'green',
    1: 'orange',
    2: 'red',
  }
  return colorMap[status] || 'default'
}

// 获取账户状态文本
const getAccountStatusText = (status) => {
  const statusMap = {
    0: '正常',
    1: '异常',
    2: '禁用',
  }
  return statusMap[status] || '未知'
}

// 获取账户状态颜色
const getAccountStatusColor = (status) => {
  const colorMap = {
    0: 'green',
    1: 'orange',
    2: 'red',
  }
  return colorMap[status] || 'default'
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
          color: $text-primary;
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
