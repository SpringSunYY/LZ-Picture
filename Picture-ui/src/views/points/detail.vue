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
        <a-tag :color="packageInfo.packageStatus === '0' ? 'success' : 'error'">
          {{ packageInfo.packageStatus === '0' ? '正常' : '失效' }}
        </a-tag>
      </div>
    </div>

    <div class="details-content">
      <a-descriptions bordered :column="{ xxl: 2, xl: 2, lg: 2, md: 1, sm: 1, xs: 1 }">
        <a-descriptions-item label="套餐价格" :span="1">
          <span class="price">¥{{ packageInfo.price }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="有效期" :span="1">
          <template v-if="packageInfo.isLongTerm === '0'"> 长期有效 </template>
          <template v-else-if="packageInfo.startTime && packageInfo.endTime">
            {{ formatDate(packageInfo.startTime) }} 至 {{ formatDate(packageInfo.endTime) }}
          </template>
          <template v-else> 未设置 </template>
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
          <a-button type="primary" size="large" :disabled="packageInfo.packageStatus === '1'">
            立即购买
          </a-button>
          <a-button size="large" @click="handleClose"> 返回列表 </a-button>
        </a-space>
      </div>

      <div class="package-benefits" v-if="packageInfo.packageStatus === '0'">
        <h3>套餐权益</h3>
        <a-card>
          <a-row :gutter="[16, 16]">
            <a-col :span="8">
              <div class="benefit-item">
                <div class="benefit-icon">
                  <span class="anticon">
                    <svg
                      viewBox="64 64 896 896"
                      data-icon="dollar"
                      width="1em"
                      height="1em"
                      fill="currentColor"
                      aria-hidden="true"
                    >
                      <path
                        d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372zm47.7-395.2l-25.4-5.9V348.6c38 5.2 61.5 29 65.5 58.2.5 4 3.9 6.9 7.9 6.9h44.9c4.7 0 8.4-4.1 8-8.8-6.1-62.3-57.4-102.3-125.9-109.2V263c0-4.4-3.6-8-8-8h-28.1c-4.4 0-8 3.6-8 8v33c-70.8 6.9-126.2 46-126.2 119 0 67.6 49.8 100.2 102.1 112.7l24.7 6.3v142.7c-44.2-5.9-69-29.5-74.1-61.3-.6-3.8-4-6.6-7.9-6.6H363c-4.7 0-8.4 4-8 8.7 4.5 55 46.2 105.6 135.2 112.1V761c0 4.4 3.6 8 8 8h28.4c4.4 0 8-3.6 8-8.1l-.2-31.7c78.3-6.9 134.3-48.8 134.3-124-.1-69.4-44.2-100.4-109-116.4zm-68.6-16.2c-5.6-1.6-10.3-3.1-15-5-33.8-12.2-49.5-31.9-49.5-57.3 0-36.3 27.5-57 64.5-61.7v124zM534.3 677V543.3c3.1.9 5.9 1.6 8.8 2.2 47.3 14.4 63.2 34.4 63.2 65.1 0 39.1-29.4 62.6-72 66.4z"
                      ></path>
                    </svg>
                  </span>
                </div>
                <div class="benefit-title">积分抵扣</div>
                <div class="benefit-desc">可用于商品抵扣</div>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="benefit-item">
                <div class="benefit-icon">
                  <span class="anticon">
                    <svg
                      viewBox="64 64 896 896"
                      data-icon="gift"
                      width="1em"
                      height="1em"
                      fill="currentColor"
                      aria-hidden="true"
                    >
                      <path
                        d="M880 310H732.4c13.6-21.4 21.6-46.8 21.6-74 0-76.1-61.9-138-138-138-41.4 0-78.7 18.4-104 47.4-25.3-29-62.6-47.4-104-47.4-76.1 0-138 61.9-138 138 0 27.2 7.9 52.6 21.6 74H144c-17.7 0-32 14.3-32 32v200c0 4.4 3.6 8 8 8h40v344c0 17.7 14.3 32 32 32h640c17.7 0 32-14.3 32-32V550h40c4.4 0 8-3.6 8-8V342c0-17.7-14.3-32-32-32zm-334-74c0-38.6 31.4-70 70-70s70 31.4 70 70-31.4 70-70 70h-70v-70zm-138-70c38.6 0 70 31.4 70 70v70h-70c-38.6 0-70-31.4-70-70s31.4-70 70-70zM160 342h264v198H160V342zm0 198v344h264V540H160zm540 344H436V540h264v344zm0-344V342h264v198H700z"
                      ></path>
                    </svg>
                  </span>
                </div>
                <div class="benefit-title">积分兑换</div>
                <div class="benefit-desc">可兑换优惠券礼品</div>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="benefit-item">
                <div class="benefit-icon">
                  <span class="anticon">
                    <svg
                      viewBox="64 64 896 896"
                      data-icon="crown"
                      width="1em"
                      height="1em"
                      fill="currentColor"
                      aria-hidden="true"
                    >
                      <path
                        d="M899.6 276.5L705 396.4 518.4 147.5a8.06 8.06 0 00-12.9 0L319 396.4 124.3 276.5c-5.7-3.5-13.1 1.2-12.2 7.9L188.5 865c1.1 7.9 7.9 14 16 14h615.1c8 0 14.9-6 15.9-14l76.4-580.6c.8-6.7-6.5-11.4-12.3-7.9zm-126 534.1H250.3l-53.8-409.4 139.8 86.1L512 252.9l175.7 234.4 139.8-86.1-53.9 409.4zM512 509c-62.1 0-112.6 50.5-112.6 112.6S449.9 734.2 512 734.2s112.6-50.5 112.6-112.6S574.1 509 512 509zm0 160.9c-26.6 0-48.2-21.6-48.2-48.3 0-26.6 21.6-48.3 48.2-48.3s48.2 21.6 48.2 48.3c0 26.6-21.6 48.3-48.2 48.3z"
                      ></path>
                    </svg>
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
import { defineProps } from 'vue'

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
