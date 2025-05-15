<template>
  <div class="payment">
    <a-page-header
      style="margin: 0 20vh"
      title="套餐支付"
      :sub-title="packageInfo ? packageInfo.packageName : ''"
      @back="goBack"
    />

    <div class="payment-content" v-if="packageInfo">
      <!-- 支付流程内容 -->
      <div class="payment-flow">
        <a-steps :current="currentStep" class="payment-steps">
          <a-step title="确认订单" />
          <a-step title="选择支付方式" />
          <a-step title="支付" />
          <a-step title="支付结果" />
        </a-steps>

        <div class="step-content">
          <!-- 步骤1: 确认订单 -->
          <div v-if="currentStep === 0" class="order-confirmation">
            <a-card title="订单确认" class="order-card">
              <div class="package-info">
                <div
                  class="package-header"
                  :class="packageInfo.isLongTerm === '1' ? 'regular-package' : 'long-term-package'"
                >
                  <div class="text-2xl font-bold text">{{ packageInfo.packageName }}</div>
                  <div class="package-tag">
                    <a-tag :color="packageInfo.isLongTerm === '1' ? 'blue' : 'green'">
                      {{ packageInfo.isLongTerm === '1' ? '限时套餐' : '长期套餐' }}
                    </a-tag>
                  </div>
                </div>

                <a-descriptions bordered :column="1" size="small">
                  <a-descriptions-item label="套餐价格">
                    <span class="price">¥{{ packageInfo.price }}</span>
                  </a-descriptions-item>
                  <a-descriptions-item label="积分数量">
                    <span class="points">{{ packageInfo.points }} 积分</span>
                  </a-descriptions-item>
                  <a-descriptions-item label="赠送积分" v-if="packageInfo.pointsBonus > 0">
                    <span class="bonus-points">{{ packageInfo.pointsBonus }} 积分</span>
                  </a-descriptions-item>
                  <a-descriptions-item label="有效期">
                    <template v-if="packageInfo.isLongTerm === '0'"> 长期有效</template>
                    <template v-else-if="packageInfo.startTime && packageInfo.endTime">
                      {{ formatDateTimeByStr(packageInfo.startTime) }} 至
                      {{ formatDateTimeByStr(packageInfo.endTime) }}
                    </template>
                  </a-descriptions-item>
                </a-descriptions>
              </div>

              <div class="order-summary">
                <h3>订单摘要</h3>
                <div class="summary-item">
                  <span>套餐价格:</span>
                  <span>¥{{ packageInfo.price }}</span>
                </div>
                <div class="summary-item">
                  <span>优惠折扣:</span>
                  <span>-¥{{ discount.toFixed(2) }}</span>
                </div>
                <div class="summary-item total">
                  <span>应付金额:</span>
                  <span>¥{{ (packageInfo.price - discount).toFixed(2) }}</span>
                </div>
              </div>

              <div class="action-buttons">
                <a-button @click="goBack">返回</a-button>
                <a-button type="primary" @click="nextStep">确认订单</a-button>
              </div>
            </a-card>
          </div>

          <!-- 步骤2: 选择支付方式 -->
          <div v-if="currentStep === 1" class="payment-method">
            <a-card title="选择支付方式" class="payment-card">
              <a-radio-group v-model:value="paymentMethod" class="payment-options">
                <a-radio value="alipay" class="payment-option">
                  <div class="payment-option-content">
                    <div class="payment-icon alipay-icon">
                      <SvgIcon size="1.5em" name="aliPay" />
                    </div>
                    <div class="payment-label">
                      <div>支付宝</div>
                      <div class="payment-description">推荐使用支付宝快捷支付</div>
                    </div>
                  </div>
                </a-radio>
                <a-radio value="wechat" class="payment-option">
                  <div class="payment-option-content">
                    <div class="payment-icon wechat-icon">
                      <SvgIcon size="1.5em" name="weChat" />
                    </div>
                    <div class="payment-label">
                      <div>微信支付</div>
                      <div class="payment-description">使用微信扫码支付</div>
                    </div>
                  </div>
                </a-radio>
                <a-radio disabled value="creditcard" class="payment-option">
                  <div class="payment-option-content">
                    <div class="payment-icon creditcard-icon">
                      <SvgIcon size="1.5em" name="bankCard" />
                    </div>
                    <div class="payment-label">
                      <div>银行卡支付</div>
                      <div class="payment-description">支持各大银行借记卡/信用卡</div>
                    </div>
                  </div>
                </a-radio>
              </a-radio-group>

              <div v-if="paymentMethod === 'creditcard'" class="credit-card-form">
                <a-form :model="cardForm" layout="vertical">
                  <a-form-item
                    label="持卡人姓名"
                    name="cardHolder"
                    :rules="[{ required: true, message: '请输入持卡人姓名' }]"
                  >
                    <a-input v-model:value="cardForm.cardHolder" placeholder="请输入持卡人姓名" />
                  </a-form-item>
                  <a-form-item
                    label="卡号"
                    name="cardNumber"
                    :rules="[{ required: true, message: '请输入卡号' }]"
                  >
                    <a-input v-model:value="cardForm.cardNumber" placeholder="请输入卡号" />
                  </a-form-item>
                  <a-row :gutter="16">
                    <a-col :span="12">
                      <a-form-item
                        label="有效期"
                        name="expiry"
                        :rules="[{ required: true, message: '请输入有效期' }]"
                      >
                        <a-input v-model:value="cardForm.expiry" placeholder="MM/YY" />
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item
                        label="CVV"
                        name="cvv"
                        :rules="[{ required: true, message: '请输入CVV' }]"
                      >
                        <a-input v-model:value="cardForm.cvv" placeholder="CVV" />
                      </a-form-item>
                    </a-col>
                  </a-row>
                </a-form>
              </div>

              <div class="payment-summary">
                <div class="summary-item total">
                  <span>应付金额:</span>
                  <span>¥{{ (packageInfo.price - discount).toFixed(2) }}</span>
                </div>
              </div>

              <div class="action-buttons">
                <a-button @click="prevStep">上一步</a-button>
                <a-button type="primary" @click="nextStep" :disabled="!paymentMethod"
                  >下一步
                </a-button>
              </div>
            </a-card>
          </div>

          <!-- 步骤3: 支付处理 -->
          <div v-if="currentStep === 2" class="payment-processing">
            <a-card title="支付处理中" class="processing-card">
              <div class="payment-qrcode" v-if="paymentMethod === 'wechat'">
                <div class="qrcode-container">
                  <div class="qrcode-image">
                    <!-- 这里放二维码图片 -->
                    <div class="mock-qrcode"></div>
                  </div>
                  <p class="qrcode-tip">
                    <!--                    {{ paymentMethod === 'alipay' ? '请使用支付宝扫码支付' : '请使用微信扫码支付' }}-->
                    正在使用使用微信扫码支付，请勿关闭或刷新此页面
                  </p>
                </div>
                <div class="payment-amount">
                  <p>
                    支付金额:
                    <span class="amount">¥{{ (packageInfo.price - discount).toFixed(2) }}</span>
                  </p>
                </div>
                <div class="payment-timer">
                  <p>
                    二维码有效时间: <span class="timer">{{ formatTime(paymentTimer) }}</span>
                  </p>
                </div>
              </div>
              <div class="payment-qrcode" v-if="paymentMethod === 'alipay'">
                <div class="qrcode-container">
                  <div class="qrcode-image">
                    <!-- 这里放二维码图片 -->
                    <SvgIcon name="useAlipay" size="12em" />
                  </div>
                  <p class="qrcode-tip">
                    <!--                    {{ paymentMethod === 'alipay' ? '请使用支付宝扫码支付' : '请使用微信扫码支付' }}-->
                    正在使用支付宝支付，请勿关闭或刷新此页面
                  </p>
                </div>
                <div class="payment-amount">
                  <p>
                    支付金额:
                    <span class="amount">¥{{ (packageInfo.price - discount).toFixed(2) }}</span>
                  </p>
                </div>
                <div class="payment-timer">
                  <p>
                    支付有效时间: <span class="timer">{{ formatTime(paymentTimer) }}</span>
                  </p>
                </div>
              </div>

              <div class="bank-processing" v-if="paymentMethod === 'creditcard'">
                <div class="processing-animation">
                  <a-spin size="large" />
                </div>
                <p class="processing-text">正在处理您的支付请求，请稍候...</p>
                <p class="processing-tip">请不要关闭或刷新此页面</p>
              </div>

              <div class="action-buttons">
                <a-button @click="prevStep">取消支付</a-button>
                <a-button type="primary" @click="simulatePayment">
                  {{ paymentMethod === 'creditcard' ? '确认支付' : '我已完成支付' }}
                </a-button>
              </div>
            </a-card>
          </div>

          <!-- 步骤4: 支付结果 -->
          <div v-if="currentStep === 3" class="payment-result">
            <a-result
              :status="paymentSuccess ? 'success' : 'error'"
              :title="paymentSuccess ? '支付成功' : '支付失败'"
              :sub-title="
                paymentSuccess ? '您的积分已成功充值到账户' : '支付过程中遇到问题，请重试'
              "
            >
              <template #extra>
                <div class="result-details" v-if="paymentSuccess">
                  <div class="result-item">
                    <span>订单号:</span>
                    <span>{{ orderId }}</span>
                  </div>
                  <div class="result-item">
                    <span>支付金额:</span>
                    <span>¥{{ (packageInfo.price - discount).toFixed(2) }}</span>
                  </div>
                  <div class="result-item">
                    <span>获得积分:</span>
                    <span>{{ packageInfo.points }} 积分</span>
                  </div>
                  <div class="result-item" v-if="packageInfo.pointsBonus > 0">
                    <span>赠送积分:</span>
                    <span>{{ packageInfo.pointsBonus }} 积分</span>
                  </div>
                  <div class="result-item">
                    <span>支付时间:</span>
                    <span>{{ formatDateTimeByDate(new Date()) }}</span>
                  </div>
                </div>
                <div class="action-buttons">
                  <a-button v-if="!paymentSuccess" @click="anewPayment">重新支付</a-button>
                  <a-button type="primary" @click="finishPayment">
                    {{ paymentSuccess ? '完成' : '返回套餐列表' }}
                  </a-button>
                </div>
              </template>
            </a-result>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="loading-container">
      <a-spin size="large" />
      <p>正在加载套餐信息...</p>
    </div>
  </div>
</template>

<script setup lang="ts" name="PointsPayment">
import { onMounted, onUnmounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getPointsRechargePackageInfo } from '@/api/points/points.ts'
import type { PointsRechargePackageInfoVo } from '@/types/points/points.d.ts'
import SvgIcon from '@/components/SvgIcon.vue'
import { formatDateTimeByDate, formatDateTimeByStr, formatTime } from '@/utils/common.ts'
import { alipayWeb } from '@/api/points/pay.ts'
import type { PayRequest } from '@/types/points/pay'
import useUserStore from '@/stores/modules/user.ts'
import { storeToRefs } from 'pinia'
import { toNumber } from 'lodash-es'

const router = useRouter()
const route = useRoute()
const packageInfo = ref<PointsRechargePackageInfoVo>()

// 支付流程状态
const currentStep = ref(0)
const paymentMethod = ref('alipay')
const paymentSuccess = ref(false)
const paymentTimer = ref(300) // 5分钟倒计时
const timerInterval = ref(null)
const discount = ref(0) // 优惠金额
const orderId = ref('')

const userStore = useUserStore()
const { userId: userId } = storeToRefs(userStore)
//支付表单
const formState = ref<PayRequest>({
  packageId: route.query.packageId as string,
  userId: userId.value,
})

// 银行卡表单
const cardForm = ref({
  cardHolder: '',
  cardNumber: '',
  expiry: '',
  cvv: '',
})

// 方法
const navToPackages = () => {
  router.push('/points')
}

const goBack = () => {
  navToPackages()
}

// 支付宝支付
const alipay = () => {
  alipayWeb(formState.value).then((res) => {
    console.log(res)
    if (res.code != 200) {
      message.error('支付失败！！！')
    }
    if (!res.data?.success) {
      message.error('支付失败！！！')
    }
    message.success('支付成功后请回此页面点击完成支付')
    message.success("即将跳转支付宝支付页面...")
    //定时三秒后跳转
    setTimeout(() => {
      // 打开新窗口
      const newWindow = window.open('', '_blank')
      if (newWindow) {
        // 将 HTML 内容写入新窗口
        newWindow.document.write(res.data.html)
        newWindow.document.close() // 关闭写入流
      } else {
        console.error('无法打开新窗口')
      }
    }, 3000)
  })
}

const nextStep = () => {
  if (currentStep.value === 0) {
    // 从确认订单到选择支付方式
    currentStep.value = 1
  } else if (currentStep.value === 1) {
    // 从选择支付方式到支付处理
    if (!paymentMethod.value) {
      message.warning('请选择支付方式')
      return
    }

    if (paymentMethod.value === 'creditcard') {
      // 验证信用卡信息
      if (
        !cardForm.value.cardHolder ||
        !cardForm.value.cardNumber ||
        !cardForm.value.expiry ||
        !cardForm.value.cvv
      ) {
        message.warning('请填写完整的银行卡信息')
        return
      }
    }

    currentStep.value = 2
    // 支付
    //如果是支付宝支付
    if (paymentMethod.value === 'alipay') {
      alipay()
    }
    // 启动计时器
    startPaymentTimer()
  }
}

const prevStep = () => {
  if (currentStep.value > 0) {
    currentStep.value -= 1

    // 如果从支付处理返回，清除计时器
    if (currentStep.value === 1) {
      if (timerInterval.value !== null) {
        clearInterval(timerInterval.value)
      }
    }
  } else {
    navToPackages()
  }
}
//重新支付
const anewPayment = () => {
  //从选择支付开始
  currentStep.value = 1
  //清楚定时器
  if (timerInterval.value !== null) {
    clearInterval(timerInterval.value)
  }
}
const startPaymentTimer = () => {
  // 清除可能存在的旧计时器
  if (timerInterval.value) {
    clearInterval(timerInterval.value)
  }

  paymentTimer.value = 300 // 重置为5分钟

  timerInterval.value = setInterval(() => {
    if (paymentTimer.value > 0) {
      paymentTimer.value -= 1
    } else {
      // 时间到，支付失败
      if (timerInterval.value !== null) {
        clearInterval(timerInterval.value)
      }
      message.error('支付超时，请重新尝试')
      currentStep.value = 1 // 返回到支付方式选择
    }
  }, 1000)
}

const simulatePayment = () => {
  // 模拟支付过程
  message.loading('正在处理支付...', 2)

  // 清除计时器
  if (timerInterval.value !== null) {
    clearInterval(timerInterval.value)
  }

  // 生成随机订单号
  orderId.value = 'ORD' + Date.now().toString().slice(-8) + Math.floor(Math.random() * 1000)

  // 模拟80%的概率支付成功
  setTimeout(() => {
    paymentSuccess.value = false
    currentStep.value = 3

    if (paymentSuccess.value) {
      message.success('支付成功！')
    } else {
      message.error('支付失败，请重试')
    }
  }, 2000)
}

const finishPayment = () => {
  if (paymentSuccess.value) {
    // 支付成功，更新用户积分 (实际应用中会通过API处理)
    const totalPoints =
      (toNumber(packageInfo.value?.points) || 0) + (toNumber(packageInfo.value?.pointsBonus) || 0)

    // 显示积分到账通知
    setTimeout(() => {
      message.success(`${totalPoints} 积分已成功充值到您的账户`)
      // 支付成功后跳转到积分记录页面
      router.push('/points')
    }, 1000)
  } else {
    // 支付失败，返回套餐列表
    router.push('/points')
  }
}

onMounted(() => {
  const packageId = route.query.packageId
  if (packageId) {
    // 从 API 获取套餐信息
    getPointsRechargePackageInfo(typeof packageId === 'string' ? packageId : '').then((res) => {
      packageInfo.value = res.data
    })
  } else {
    message.error('套餐信息不存在')
    router.push('/points')
  }
})

// 生命周期钩子
onUnmounted(() => {
  // 清除计时器
  if (timerInterval.value) {
    clearInterval(timerInterval.value)
  }
})
</script>

<style lang="scss" scoped>
// 颜色变量
$primary-color: #1890ff;
$success-color: #52c41a;
$error-color: #f5222d;
$warning-color: #faad14;
$text-color: #333333;
$text-secondary: #8c8c8c;
$border-color: #f0f0f0;
$bg-light: #f5f5f5;
$bg-white: #fff;
$shadow-color: rgba(0, 0, 0, 0.05);

// 间距变量
$spacing-xs: 8px;
$spacing-sm: 2px;
$spacing-md: 24px;
$spacing-lg: 32px;
$spacing-xl: 48px;

// 边框圆角
$border-radius: 4px;

// 混合器
@mixin flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

@mixin flex-between {
  display: flex;
  justify-content: space-between;
}

@mixin flex-column {
  display: flex;
  flex-direction: column;
}

@mixin card-shadow {
  box-shadow: 0 1px 2px $shadow-color;
}

// 主容器样式
.payment {
  padding: $spacing-md;
  background-color: $bg-light;
  min-height: 80vh; // 确保容器占据整个视口高度
  // 内容区域
  .payment-content {
    margin: 0 auto;
    max-width: 800px;
    width: 100%; // 确保内容区域宽度自适应
    background-color: $bg-white;
    border-radius: $border-radius;
    @include card-shadow;
    padding: $spacing-md;
  }

  // 加载状态
  .loading-container {
    @include flex-column;
    align-items: center;
    justify-content: center;
    padding: $spacing-xl 0;

    p {
      margin-top: $spacing-sm;
      color: $text-secondary;
    }
  }
}

// 支付流程样式
.payment-flow {
  width: 100%;

  .payment-steps {
    margin-bottom: $spacing-lg;
  }

  .step-content {
    background-color: $bg-light;
    padding: $spacing-sm;
    border-radius: $border-radius;

    // 卡片样式
    .order-card,
    .payment-card,
    .processing-card {
      margin-bottom: $spacing-sm;
    }

    // 套餐头部
    .package-header {
      padding: $spacing-md;
      color: $bg-white;
      text-align: center;
      border-radius: $border-radius;
      margin-bottom: $spacing-sm;

      &.regular-package {
        background: linear-gradient(135deg, #3498db, hsl(204, 70%, 30%));
      }

      &.long-term-package {
        padding: 10px 0px;
        background: linear-gradient(135deg, $success-color);
      }

      .package-tag {
        margin-top: $spacing-xs;
        display: flex;
        justify-content: center;
        gap: $spacing-xs;
      }
    }

    // 订单摘要
    .order-summary,
    .payment-summary {
      margin-top: $spacing-md;
      padding-top: $spacing-sm;
      border-top: 1px solid $border-color;

      .summary-item {
        @include flex-between;
        margin-bottom: $spacing-xs;

        &.total {
          font-weight: bold;
          font-size: 16px;
          color: $error-color;
          margin-top: $spacing-xs;
          padding-top: $spacing-xs;
          border-top: 1px dashed $border-color;
        }
      }
    }

    // 按钮区域
    .action-buttons {
      margin-top: $spacing-md;
      @include flex-between;
    }

    // 支付方式选择
    .payment-options {
      @include flex-column;
      gap: $spacing-sm;
      width: 100%;

      .payment-option {
        width: 100%;
        height: 60px;
        margin-right: 0;

        .payment-option-content {
          display: flex;
          align-items: center;
          padding-left: $spacing-xs;

          .payment-icon {
            margin-right: $spacing-sm;
            padding: 0 10px;
            @include flex-center;
          }

          .payment-label {
            @include flex-column;

            .payment-description {
              font-size: 12px;
              color: $text-secondary;
            }
          }
        }
      }
    }

    // 信用卡表单
    .credit-card-form {
      margin-top: $spacing-md;
      padding: $spacing-sm;
      background-color: white;
      border-radius: $border-radius;
    }

    // 二维码支付
    .payment-qrcode {
      @include flex-column;
      align-items: center;
      padding: $spacing-md 0;

      .qrcode-container {
        text-align: center;
        margin-bottom: $spacing-md;

        .qrcode-image {
          margin-bottom: $spacing-sm;

          .mock-qrcode {
            width: 200px;
            height: 200px;
            background: linear-gradient(135deg, #3498db, hsl(204, 70%, 30%));
            margin: 0 auto;
            border: 1px solid white;
          }
        }

        .qrcode-tip {
          color: $text-secondary;
        }
      }

      .payment-amount {
        margin-bottom: $spacing-sm;

        .amount {
          font-size: 20px;
          font-weight: bold;
          color: $error-color;
        }
      }

      .payment-timer {
        color: $text-secondary;

        .timer {
          color: $error-color;
          font-weight: bold;
        }
      }
    }

    // 银行卡处理
    .bank-processing {
      @include flex-column;
      align-items: center;
      padding: $spacing-xl 0;

      .processing-animation {
        margin-bottom: $spacing-md;
      }

      .processing-text {
        font-size: 16px;
        margin-bottom: $spacing-xs;
      }

      .processing-tip {
        color: $error-color;
        font-size: 14px;
      }
    }

    // 结果详情
    .result-details {
      background-color: white;
      padding: $spacing-sm;
      border-radius: $border-radius;
      margin-bottom: $spacing-md;
      text-align: left;

      .result-item {
        @include flex-between;
        margin-bottom: $spacing-xs;
      }
    }
  }
}

// 文本样式
.price {
  font-weight: bold;
  color: $error-color;
}

.points {
  font-weight: bold;
  color: $primary-color;
}

.bonus-points {
  font-weight: bold;
  color: $success-color;
}
</style>
