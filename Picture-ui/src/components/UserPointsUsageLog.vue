<template>
  <div class="user-points-usage-log">
    <a-card title="积分使用记录" :bordered="false">
      <a-form layout="inline" :model="queryParams">
        <a-form-item>
          <a-select
            v-model:value="queryParams.logType"
            placeholder="日志类型"
            allow-clear
            @change="handleSearch"
            style="width: 180px"
          >
            <a-select-option
              v-for="item in po_points_usage_log_type"
              :key="item.dictValue"
              :value="item.dictValue"
            >
              {{ item.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-select
            v-model:value="queryParams.usageType"
            placeholder="使用类型"
            allow-clear
            @change="handleSearch"
            style="width: 180px"
          >
            <a-select-option
              v-for="item in po_points_usage_type"
              :key="item.dictValue"
              :value="item.dictValue"
            >
              {{ item.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-range-picker
            v-model:value="dateRange"
            @change="handleSearch"
            format="YYYY-MM-DD"
            style="min-width: 240px"
          />
        </a-form-item>
        <a-button type="primary" @click="resetSearch">重置</a-button>
      </a-form>

      <a-table
        :columns="columns"
        :data-source="dataList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="targetId"
        :scroll="{ x: 1000 }"
      >
        <template #bodyCell="{ column, text }">
          <template v-if="column.dataIndex === 'logType'">
            <dict-tag :options="po_points_usage_log_type" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'usageType'">
            <dict-tag :options="po_points_usage_type" :value="text" />
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, onMounted, ref } from 'vue'
import DictTag from '@/components/DictTag.vue'
import dayjs from 'dayjs'
import { listPointsUsageLogInfo } from '@/api/points/pointsUsageLogInfo'
import type {
  UserPointsUsageLogInfoVo,
  UserPointsUsageLogQuery,
} from '@/types/points/pointsUsageLogInfo.d.ts'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { po_points_usage_log_type, po_points_usage_type } = proxy?.useDict(
  'po_points_usage_log_type',
  'po_points_usage_type',
)

const dataList = ref<UserPointsUsageLogInfoVo[]>([])
const loading = ref(false)
const dateRange = ref<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showTotal: (total: number) => `共 ${total} 条记录`,
  showSizeChanger: true,
  showQuickJumper: true,
})

const queryParams = ref<UserPointsUsageLogQuery>({
  pageNum: 1,
  pageSize: 10,
  isAsc: 'desc',
  usageType: null,
  logType: null,
})

const columns = [
  { title: '日志类型', dataIndex: 'logType' },
  { title: '使用类型', dataIndex: 'usageType' },
  { title: '目标编号', dataIndex: 'targetId' },
  { title: '使用前积分', dataIndex: 'pointsBefore' },
  { title: '消费积分', dataIndex: 'pointsUsed' },
  { title: '使用后积分', dataIndex: 'pointsAfter' },
  { title: '使用时间', dataIndex: 'createTime', sorter: true, width: 180 },
]

const getDataList = () => {
  loading.value = true
  queryParams.value.params = {}
  if (dateRange.value) {
    queryParams.value.params['beginCreateTime'] = dateRange.value[0].format('YYYY-MM-DD')
    queryParams.value.params['endCreateTime'] = dateRange.value[1].format('YYYY-MM-DD')
  }
  listPointsUsageLogInfo(queryParams.value).then((res) => {
    dataList.value = res?.rows || []
    pagination.value.total = res?.total || 0
    loading.value = false
  })
}

const handleSearch = () => {
  queryParams.value.pageNum = 1
  pagination.value.current = 1
  getDataList()
}

const resetSearch = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    isAsc: 'desc',
    usageType: null,
    logType: null,
  }
  dateRange.value = null
  pagination.value.current = 1
  handleSearch()
}

const handleTableChange = (pag, _, sorter) => {
  pagination.value.current = pag.current
  pagination.value.pageSize = pag.pageSize
  queryParams.value.pageNum = pag.current
  queryParams.value.pageSize = pag.pageSize
  queryParams.value.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc'
  getDataList()
}

onMounted(getDataList)
</script>
