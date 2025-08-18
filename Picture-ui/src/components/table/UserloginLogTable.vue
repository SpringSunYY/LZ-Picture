<template>
  <div class="my-login-log">
    <a-card title="我的登录日志" :bordered="false">
      <a-table
        :columns="columns"
        :data-source="logList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="infoId"
      >
        <template #bodyCell="{ column, text }">
          <template v-if="column.dataIndex === 'loginType'">
            <dict-tag :value="text" :options="u_login_type" />
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, getCurrentInstance } from 'vue'
import { listLoginLog } from '@/api/user/login.ts'
import DictTag from '@/components/DictTag.vue'
import dayjs from 'dayjs'
import type { LoginLogInfoQuery } from '@/types/user/loginLog'

const { proxy } = getCurrentInstance()!
const { u_login_type } = proxy?.useDict('u_login_type')

const logList = ref([])
const loading = ref(false)
const dateRange = ref<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showTotal: (total: number) => `共 ${total} 条记录`,
  showSizeChanger: true,
  showQuickJumper: true,
  pageSizeOptions: ['10', '20', '30', '50']
})

const queryParams = ref<LoginLogInfoQuery>({
  pageNum: 1,
  pageSize: 10,
  isAsc: 'desc',
  loginType: null,
  loginLocation: '',
  params: {},
})

const columns = [
  { title: '登录方式', dataIndex: 'loginType' },
  { title: '登录地点', dataIndex: 'loginLocation' },
  { title: '登录IP', dataIndex: 'ipaddr' },
  { title: '登录时间', dataIndex: 'loginTime', sorter: true, width: 180 },
]

const getList = () => {
  loading.value = true
  queryParams.value.params = {}
  if (dateRange.value) {
    queryParams.value.params['beginLoginTime'] = dateRange.value[0]
      .format('YYYY-MM-DD')
      .concat(' 00:00:00')
    queryParams.value.params['endLoginTime'] = dateRange.value[1]
      .format('YYYY-MM-DD')
      .concat(' 23:59:59')
  }
  listLoginLog(queryParams.value).then((res) => {
    logList.value = res?.rows || []
    pagination.value.total = res?.total || 0
    loading.value = false
  })
}

const handleSearch = () => {
  queryParams.value.pageNum = 1
  pagination.value.current = 1
  getList()
}

const resetSearch = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    isAsc: 'desc',
    loginType: null,
    loginLocation: '',
    params: {},
  }
  dateRange.value = null
  pagination.value.current = 1
  getList()
}

const handleTableChange = (pag, _, sorter) => {
  pagination.value.current = pag.current
  pagination.value.pageSize = pag.pageSize
  queryParams.value.pageNum = pag.current
  queryParams.value.pageSize = pag.pageSize
  queryParams.value.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc'
  getList()
}

onMounted(getList)
</script>
