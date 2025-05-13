<template>
  <div class="points">
    <a-page-header title="积分充值套餐" sub-title="选择适合您的积分充值方案" />

    <div class="filter-section">
      <a-space>
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
      <a-row :gutter="[16, 16]">
        <a-col
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
          v-for="pkg in filteredPackages"
          :key="pkg.package_id"
        >
          <a-card
            hoverable
            class="package-card"
            :class="{ 'inactive-package': pkg.packageStatus === '1' }"
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
                  <a-tag :color="pkg.packageStatus === '0' ? 'success' : 'error'">
                    {{ pkg.packageStatus === '0' ? '正常' : '失效' }}
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
                <span class="points-label">积分：</span>
                <span class="points-value">{{ pkg.points }}</span>
              </div>
              <div class="points-item" v-if="pkg.pointsBonus > 0">
                <span class="points-label">赠送：</span>
                <span class="points-value bonus">+{{ pkg.pointsBonus }}</span>
              </div>
            </div>
            <div class="package-footer">
              <p v-if="pkg.isLongTerm === '0'" class="validity">长期有效</p>
              <p v-else-if="pkg.startTime && pkg.endTime" class="validity">
                {{ formatDate(pkg.startTime) }} 至 {{ formatDate(pkg.endTime) }}
              </p>
              <a-button type="primary" block :disabled="pkg.packageStatus === '1'">
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
        v-if="selectedPackage"
        @close="detailsVisible = !detailsVisible"
        :packageInfo="selectedPackage"
      />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import Detail from './detail.vue'

// 模拟数据
const packages = ref([
  {
    package_id: 'PKG001',
    packageName: '新手入门包',
    price: 9.9,
    points: 100,
    pointsBonus: 10,
    description: '适合新用户的入门级积分套餐，额外赠送10积分',
    isLongTerm: '1',
    startTime: '2023-01-01 00:00:00',
    endTime: '2023-12-31 23:59:59',
    packageStatus: '0',
    create_time: '2023-01-01 10:00:00',
    update_time: null,
    remark: '限时优惠',
  },
  {
    package_id: 'PKG002',
    packageName: '标准套餐',
    price: 29.9,
    points: 300,
    pointsBonus: 30,
    description: '性价比最高的标准套餐，额外赠送30积分',
    isLongTerm: '0',
    startTime: null,
    endTime: null,
    packageStatus: '0',
    create_time: '2023-01-01 10:00:00',
    update_time: null,
    remark: '热门选择',
  },
  {
    package_id: 'PKG003',
    packageName: '高级套餐',
    price: 99.9,
    points: 1000,
    pointsBonus: 200,
    description: '高级用户首选，赠送200积分，物超所值',
    isLongTerm: '0',
    startTime: null,
    endTime: null,
    packageStatus: '0',
    create_time: '2023-01-01 10:00:00',
    update_time: null,
    remark: '高级会员推荐',
  },
  {
    package_id: 'PKG004',
    packageName: '限时特惠包',
    price: 49.9,
    points: 600,
    pointsBonus: 100,
    description: '限时特惠，抢购即赠100积分',
    isLongTerm: '1',
    startTime: '2023-06-01 00:00:00',
    endTime: '2023-06-30 23:59:59',
    packageStatus: '1',
    create_time: '2023-05-15 10:00:00',
    update_time: null,
    remark: '已结束',
  },
  {
    package_id: 'PKG005',
    packageName: '尊享VIP套餐',
    price: 199.9,
    points: 2500,
    pointsBonus: 500,
    description: 'VIP专享套餐，赠送500积分，享受最高优惠',
    isLongTerm: '0',
    startTime: null,
    endTime: null,
    packageStatus: '0',
    create_time: '2023-01-01 10:00:00',
    update_time: null,
    remark: 'VIP专享',
  },
])

// 状态变量
const searchText = ref('')
const statusFilter = ref('')
const termFilter = ref('')
const detailsVisible = ref(false)
const selectedPackage = ref(null)

// 过滤后的套餐列表
const filteredPackages = computed(() => {
  return packages.value.filter((pkg) => {
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
  selectedPackage.value = pkg
  detailsVisible.value = true
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

// 生命周期钩子
onMounted(() => {
  // 在实际应用中，这里会从API获取套餐数据
  // fetchPackages();
})

// 实际应用中的API调用
const fetchPackages = async () => {
  try {
    // const response = await api.getPackages();
    // packages.value = response.data;
    message.success('套餐数据加载成功')
  } catch (error) {
    message.error('获取套餐数据失败')
    console.error(error)
  }
}
</script>

<style scoped lang="scss">
.points {
  //padding: 24px;
  //background-color: #f5f5f5;
  min-height: 100vh;

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
    margin-bottom: 8px;
  }

  .points-label {
    color: #8c8c8c;
  }

  .points-value {
    font-weight: bold;
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
