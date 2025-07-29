<template>
  <div class="points-details">
    <div
      class="package-header"
      :class="packageInfo.isLongTerm === '1' ? 'regular-package' : 'long-term-package'"
    >
      <h1>{{ packageInfo.packageName }}</h1>
      <div class="package-status">
        <a-tag :color="packageInfo.isLongTerm === '1' ? 'blue' : 'green'">
          {{ packageInfo.isLongTerm === '1' ? '限时套餐' : '长期套餐' }}
        </a-tag>
        <a-tag :color="packageInfo.packageStatus === '1' ? 'success' : 'error'">
          {{ getPackageStatusLabel(packageInfo.packageStatus) }}
        </a-tag>
      </div>
    </div>

    <div class="details-content">
      <a-descriptions bordered :column="{ xxl: 2, xl: 2, lg: 2, md: 1, sm: 1, xs: 1 }">
        <a-descriptions-item label="套餐价格" :span="1">
          <span class="price">¥{{ packageInfo.price }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="有效期" :span="1">
          <template v-if="packageInfo.isLongTerm === '0'"> 长期有效</template>
          <template v-else-if="packageInfo.startTime && packageInfo.endTime">
            {{ formatDate(packageInfo.startTime) }} 至 {{ formatDate(packageInfo.endTime) }}
          </template>
          <template v-else> 未设置</template>
        </a-descriptions-item>
        <a-descriptions-item label="积分数量" :span="1">
          <span class="points">{{ packageInfo.points }}</span>
        </a-descriptions-item>
        <a-descriptions-item label="赠送积分" :span="1">
          <span class="bonus-points">{{ packageInfo.pointsBonus || 0 }}</span>
        </a-descriptions-item>
        <a-descriptions-item label="套餐描述" :span="2">
          {{ packageInfo.description || '暂无描述' }}
        </a-descriptions-item>
      </a-descriptions>

      <div class="action-section">
        <a-space>
          <a-button
            type="primary"
            size="large"
            v-if="checkPermiSingle('points:payment')"
            :disabled="packageInfo.packageStatus !== '1'"
            @click="handlePurchase"
          >
            立即购买
          </a-button>
          <a-button size="large" @click="handleClose"> 返回列表</a-button>
        </a-space>
      </div>
      <div class="package-benefits">
        <a-card>
          <a-row :gutter="[16, 16]">
            <a-col :span="8">
              <div class="benefit-item">
                <div class="benefit-icon">
                  <span class="anticon">
                    <SvgIcon size="1em" name="deduction" />
                  </span>
                </div>
                <div class="benefit-title">积分抵扣</div>
                <div class="benefit-desc">可用于图片查看原图</div>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="benefit-item">
                <div class="benefit-icon">
                  <span class="anticon">
                    <SvgIcon size="1em" name="conversion" />
                  </span>
                </div>
                <div class="benefit-title">积分兑换</div>
                <div class="benefit-desc">可兑换AI使用</div>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="benefit-item">
                <div class="benefit-icon">
                  <span class="anticon">
                    <SvgIcon size="1em" name="privilege" />
                  </span>
                </div>
                <div class="benefit-title">积分特权</div>
                <div class="benefit-desc">享受积分专属权益</div>
              </div>
            </a-col>
          </a-row>
        </a-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="PointsDetail">
import { getPackageStatusLabel } from '@/types/points/points.d.ts'
import { message } from 'ant-design-vue'
import { checkPermiSingle } from '@/utils/permission.ts'
import { useRouter } from 'vue-router'

const props = defineProps({
  packageInfo: {
    type: Object,
    required: true,
  },
})
const emit = defineEmits(['close'])

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

const handleClose = () => {
  emit('close')
}
const router = useRouter()
const handlePurchase = () => {
  if (props.packageInfo.packageStatus !== '1') {
    message.error('该套餐尚未开始，请选择其他套餐！')
    return
  }

  // 关闭详情弹窗
  emit('close')

  // 导航到支付页面
  const packageId = props.packageInfo.packageId
  const routeData = router.resolve({
    path: '/points/payment',
    query: { packageId: packageId },
  })
  // console.log(routeData) // 调试路由解析结果
  window.open(routeData.href, '_blank')
}
</script>

<style scoped lang="scss">
.points-details {
  display: flex;
  flex-direction: column;

  .package-header {
    padding: 24px;
    color: #fff;
    font-size: 32px;
    border-radius: 4px;
    margin-bottom: 24px;
    text-align: center;
  }

  .regular-package {
    background: linear-gradient(135deg, #1890ff, #096dd9);
  }

  .long-term-package {
    background: linear-gradient(135deg, #52c41a, #389e0d);
  }

  .package-status {
    margin-top: 12px;
    display: flex;
    justify-content: center;
    gap: 8px;
  }

  .details-content {
    padding: 0 16px;
  }

  .price {
    font-size: 18px;
    font-weight: bold;
    color: #f5222d;
  }

  .points {
    font-weight: bold;
    color: #1890ff;
  }

  .bonus-points {
    font-weight: bold;
    color: #52c41a;
  }

  .action-section {
    margin: 24px 0;
    display: flex;
    justify-content: center;
  }

  .package-benefits {
    margin-top: 24px;
  }

  .package-benefits h3 {
    margin-bottom: 16px;
    font-weight: 500;
  }

  .benefit-item {
    text-align: center;
    padding: 16px;
  }

  .benefit-icon {
    font-size: 32px;
    color: #1890ff;
    margin-bottom: 12px;
  }

  .benefit-title {
    font-weight: 500;
    margin-bottom: 8px;
  }

  .benefit-desc {
    color: #8c8c8c;
    font-size: 12px;
  }
}
</style>
