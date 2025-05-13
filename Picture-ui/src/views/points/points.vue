<template>
  <div class="points">
    <a-page-header title="积分充值套餐" sub-title="选择适合您的积分充值方案" />

    <div class="filter-section">
      <a-space align="center" :wrap="true">
        <a-input-search
          v-model:value="searchText"
          placeholder="搜索套餐名称"
          style="width: 250px"
          @search="handleSearch"
        />
        <!--        <a-select-->
        <!--          v-model:value="statusFilter"-->
        <!--          placeholder="套餐状态"-->
        <!--          style="width: 150px"-->
        <!--          @change="handleSearch"-->
        <!--        >-->
        <!--          <a-select-option value="">全部</a-select-option>-->
        <!--          <a-select-option value="0">正常</a-select-option>-->
        <!--          <a-select-option value="1">失效</a-select-option>-->
        <!--        </a-select>-->
        <a-select
          v-model:value="termFilter"
          placeholder="套餐类型"
          style="width: 150px"
          @change="handleSearch"
        >
          <a-select-option value="">全部</a-select-option>
          <a-select-option value="0">长期套餐</a-select-option>
          <a-select-option value="1">限时套餐</a-select-option>
        </a-select>
      </a-space>
    </div>

    <div class="package-list">
      <a-row :gutter="[24, 24]">
        <a-col
          xs="24" :sm="24" :md="24" :lg="12" :xl="6" :xxl="6"
          v-for="pkg in filteredPackages"
          :key="pkg.packageId"
        >
          <a-card
            hoverable
            class="package-card"
            :class="{ 'inactive-package': pkg.packageStatus === '0' }"
            @click="showPackageDetails(pkg)"
          >
            <template #cover>
              <div
                class="package-header"
                :class="pkg.isLongTerm === '1' ? 'regular-package' : 'long-term-package'"
              >
                <div class="text-2xl font-bold text">{{ pkg.packageName }}</div>
                <div class="package-tag">
                  <a-tag :color="pkg.isLongTerm === '1' ? 'blue' : 'green'">
                    {{ pkg.isLongTerm === '1' ? '限时套餐' : '长期套餐' }}
                  </a-tag>
                  <a-tag :color="pkg.packageStatus === '1' ? 'success' : 'warning'">
                    {{ getPackageStatusLabel(pkg.packageStatus) }}
                  </a-tag>
                </div>
              </div>
            </template>
            <div class="price-section">
              <span class="currency">¥</span>
              <span class="price">{{ pkg.price }}</span>
            </div>
            <div class="points-section">
              <div class="points-item">
                <span class="points-label">积分</span>
                <span class="points-value">{{ pkg.points }}</span>
              </div>
              <div class="points-item">
                <span class="points-label">赠送</span>
                <span class="points-value bonus">+{{ pkg.pointsBonus || 0 }}</span>
              </div>
            </div>
            <div class="package-footer">
              <p v-if="pkg.isLongTerm === '0'" class="validity">长期有效</p>
              <p v-else-if="pkg.startTime && pkg.endTime" class="validity">
                {{ formatDate(pkg.startTime) }} 至 {{ formatDate(pkg.endTime) }}
              </p>
              <a-button
                type="primary"
                block

                :disabled="pkg.packageStatus !== '1'"
              >
                立即购买
              </a-button>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 套餐详情弹窗 -->
    <a-modal v-model:open="detailsVisible" :title="'购买套餐'" width="600px" :footer="null">
      <Detail
        v-if="pointsRechargePackageInfo"
        @close="detailsVisible = !detailsVisible"
        :packageInfo="pointsRechargePackageInfo"
      />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import Detail from './detail.vue'
import {
  getPointsRechargePackageInfo,
  getPointsRechargePackageInfoList,
} from '@/api/points/points.ts'
import { getPackageStatusLabel } from '@/types/points/points.ts'
import type { PointsRechargePackageInfoVo } from '@/types/points/points.ts'

// 模拟数据
const packages = ref<PointsRechargePackageInfoVo>([])
const pointsRechargePackageInfo = ref<PointsRechargePackageInfoVo>({
  packageId: '',
  packageName: '',
  price: 9.9,
  points: 100,
  pointsBonus: 10,
  description: '',
  isLongTerm: '',
  startTime: '',
  endTime: '',
  packageStatus: '0',
  create_time: '',
  update_time: null,
})
// 状态变量
const searchText = ref('')
const statusFilter = ref('')
const termFilter = ref('')
const detailsVisible = ref(false)

// 过滤后的套餐列表
const filteredPackages = computed(() => {
  return packages.value?.filter((pkg) => {
    const matchesSearch =
      !searchText.value || pkg.packageName.toLowerCase().includes(searchText.value.toLowerCase())
    const matchesStatus = !statusFilter.value || pkg.packageStatus === statusFilter.value
    const matchesTerm = !termFilter.value || pkg.isLongTerm === termFilter.value

    return matchesSearch && matchesStatus && matchesTerm
  })
})

// 方法
const handleSearch = () => {
  // 搜索逻辑已通过计算属性实现
  if (filteredPackages.value.length === 0) {
    message.info('没有找到匹配的套餐')
  }
}

const showPackageDetails = (pkg) => {
  getPointsRechargePackageInfo(pkg.packageId).then((res) => {
    pointsRechargePackageInfo.value = res.data
    detailsVisible.value = true
  })
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}
const getPointsRechargePackageList = () => {
  getPointsRechargePackageInfoList({}).then((res) => {
    packages.value = res.rows
    message.success('套餐数据加载成功')
  })
}
// 生命周期钩子
onMounted(() => {
  // 在实际应用中，这里会从API获取套餐数据
  getPointsRechargePackageList()
})
</script>

<style scoped lang="scss">
.points {
  //padding: 24px;
  //background-color: #f5f5f5;
  //min-height: 100vh;
  margin: 0 auto;
  max-width: 90%;

  .filter-section {
    margin-bottom: 24px;
    padding: 16px;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  }

  .package-card {
    height: 100%;
    transition: all 0.3s;
    overflow: hidden;
  }

  .package-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .inactive-package {
    opacity: 0.6;
  }

  .package-header {
    padding: 16px;
    color: #fff;
    text-align: center;
  }

  .regular-package {
    background: linear-gradient(135deg, #1890ff, #096dd9);
  }

  .long-term-package {
    background: linear-gradient(135deg, #52c41a, #389e0d);
  }

  .package-tag {
    margin-top: 8px;
    display: flex;
    justify-content: center;
    gap: 8px;
  }

  .price-section {
    text-align: center;
    margin: 16px 0;
  }

  .currency {
    font-size: 16px;
    font-weight: bold;
    vertical-align: top;
  }

  .price {
    font-size: 32px;
    font-weight: bold;
    color: #f5222d;
  }

  .points-section {
    margin: 16px 0;
  }

  .points-item {
    display: flex;
    justify-content: space-between;
    margin: 0 20px;
  }

  .points-label {
    color: #8c8c8c;
  }

  .points-value {
    font-weight: bold;
    color: #096dd9;
  }

  .bonus {
    color: #52c41a;
  }

  .package-footer {
    margin-top: 16px;
  }

  .validity {
    text-align: center;
    color: #8c8c8c;
    font-size: 12px;
    margin-bottom: 12px;
  }
}
</style>
