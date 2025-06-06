<template>
  <div class="points-recharge-table">
    <a-card title="积分充值记录" :bordered="false">
      <a-form layout="inline" :model="queryParams">
        <a-form-item>
          <a-select
            v-model:value="queryParams.rechargeStatus"
            placeholder="充值状态"
            allow-clear
            @change="handleSearch"
            style="width: 200px"
          >
            <a-select-option
              v-for="dict in po_recharge_status"
              :key="dict.dictValue"
              :value="dict.dictValue"
            >
              {{ dict.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-select
            v-model:value="queryParams.paymentType"
            placeholder="支付方式"
            allow-clear
            @change="handleSearch"
            style="width: 200px"
          >
            <a-select-option
              v-for="dict in po_payment_type"
              :key="dict.dictValue"
              :value="dict.dictValue"
            >
              {{ dict.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-range-picker
            v-model:value="dateRange"
            format="YYYY-MM-DD"
            style="min-width: 200px"
            @change="handleSearch"
          />
        </a-form-item>
        <a-button type="primary" @click="resetSearch">重置</a-button>
      </a-form>
      <div style="margin-bottom: 20px"></div>
      <a-table
        :columns="columns"
        :data-source="rechargeList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="rechargeId"
        :scroll="{ x: 1000 }"
      >
        <template #bodyCell="{ column, text }">
          <template v-if="column.dataIndex === 'rechargeStatus'">
            <dict-tag :options="po_recharge_status" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'paymentType'">
            <dict-tag :options="po_payment_type" :value="text" />
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, onMounted, ref } from 'vue'
import dayjs from 'dayjs'
import DictTag from '@/components/DictTag.vue'
import { listRechargeInfo } from '@/api/points/pointsRechargeInfo'
import type {
  UserPointsRechargeInfoQuery,
  UserPointsRechargeInfoVo,
} from '@/types/points/pointsRecharge'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { po_recharge_status, po_payment_type } = proxy?.useDict(
  'po_recharge_status',
  'po_payment_type',
)
const queryParams = ref<UserPointsRechargeInfoQuery>({
  pageNum: 1,
  pageSize: 10,
  isAsc: 'desc',
  rechargeStatus: null,
  paymentType: null,
  params: {},
})

const dateRange = ref<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)
const loading = ref(false)
const rechargeList = ref<UserPointsRechargeInfoVo[]>([])

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total) => `共 ${total} 条记录`,
})

const columns = [
  { title: '充值编号', dataIndex: 'rechargeId', width: 150 },
  { title: '套餐名称', dataIndex: 'packageName', width: 150 },
  { title: '充值积分', dataIndex: 'pointsCount', width: 100 },
  { title: '赠送积分', dataIndex: 'bonusCount', width: 100 },
  { title: '实付金额', dataIndex: 'buyerPayAmount', width: 100 },
  { title: '支付方式', dataIndex: 'paymentType', width: 120 },
  { title: '充值状态', dataIndex: 'rechargeStatus', width: 100 },
  { title: '充值时间', dataIndex: 'createTime', width: 180, sorter: true },
  { title: '到账时间', dataIndex: 'arrivalTime', width: 180 },
]

const getRechargeList = async () => {
  loading.value = true
  queryParams.value.params = {}
  if (dateRange.value) {
    queryParams.value.params['beginCreateTime'] = dateRange.value[0]
      .format('YYYY-MM-DD')
      .concat(' 00:00:00')
    queryParams.value.params['endCreateTime'] = dateRange.value[1]
      .format('YYYY-MM-DD')
      .concat(' 23:59:59')
  }

  const res = await listRechargeInfo(queryParams.value)
  rechargeList.value = res?.rows || []
  pagination.value.total = res?.total || 0
  loading.value = false
}

const handleSearch = () => {
  pagination.value.current = 1
  queryParams.value.pageNum = 1
  getRechargeList()
}

const resetSearch = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    rechargeStatus: null,
    paymentType: null,
    params: {},
  }
  dateRange.value = null
  handleSearch()
}

const handleTableChange = (pag, filters, sorter) => {
  pagination.value.current = pag.current
  pagination.value.pageSize = pag.pageSize
  queryParams.value.pageNum = pag.current
  queryParams.value.pageSize = pag.pageSize
  //判断是升序还是降序
  queryParams.value.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc'
  getRechargeList()
}

onMounted(() => {
  getRechargeList()
})
</script>

<style scoped lang="scss"></style>
