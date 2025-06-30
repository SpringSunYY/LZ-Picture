<template>
  <div class="space-invitation-table">
    <a-card title="空间邀请记录" :bordered="false">
      <a-form layout="inline" :model="queryParams">
        <a-form-item>
          <a-select
            v-model:value="queryParams.userType"
            placeholder="邀请/被邀请"
            allow-clear
            @change="handleSearch"
            style="width: 180px"
          >
            <a-select-option value="0">邀请</a-select-option>
            <a-select-option value="1">被邀请</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-select
            v-model:value="queryParams.roleType"
            placeholder="邀请角色"
            allow-clear
            @change="handleSearch"
            style="width: 180px"
          >
            <a-select-option
              v-for="item in p_space_role"
              :key="item.dictValue"
              :value="item.dictValue"
            >
              {{ item.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-select
            v-model:value="queryParams.invitationStatus"
            placeholder="邀请状态"
            allow-clear
            @change="handleSearch"
            style="width: 180px"
          >
            <a-select-option
              v-for="item in p_space_invitation_status"
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
            style="width: 100%; min-width: 200px"
          />
        </a-form-item>

        <a-button type="primary" @click="resetSearch">重置</a-button>
      </a-form>
      <div style="margin-bottom: 20px"></div>
      <a-table
        :columns="columns"
        :data-source="invitationList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="invitationId"
        :scroll="{ x: 1000 }"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'roleType'">
            <dict-tag :options="p_space_role" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'invitationStatus'">
            <dict-tag :options="p_space_invitation_status" :value="text" />
          </template>
          <!-- 空间封面URL 图片显示 -->
          <template v-if="column.dataIndex === 'spaceAvatar'">
            <a-image :src="text" width="60" />
          </template>
          <template v-if="column.dataIndex === 'invitation'">
            <a-tooltip :title="text">{{ text.substring(0, 10) }}</a-tooltip>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, onMounted, ref } from 'vue'
import DictTag from '@/components/DictTag.vue'
import { listSpaceInvitationInfo } from '@/api/picture/spaceInvitationInfo'
import type {
  SpaceInvitationInfoQuery,
  SpaceInvitationInfoVo,
} from '@/types/picture/spaceInvitationInfo.d.ts'
import dayjs from 'dayjs'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_space_role, p_space_invitation_status } = proxy?.useDict(
  'p_space_role',
  'p_space_invitation_status',
)

const queryParams = ref<SpaceInvitationInfoQuery>({
  pageNum: 1,
  pageSize: 10,
  userType: '1',
  roleType: undefined,
  invitationStatus: undefined,
})

const invitationList = ref<SpaceInvitationInfoVo[]>([])
const loading = ref(false)
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showTotal: (total: number) => `共 ${total} 条记录`,
  showSizeChanger: true,
  showQuickJumper: true,
})

const columns = [
  { title: '邀请编号', dataIndex: 'invitationId', width: 180 },
  { title: '空间名称', dataIndex: 'spaceName', width: 180 },
  { title: '空间封面', dataIndex: 'spaceAvatar', width: 100 },
  { title: '邀请角色', dataIndex: 'roleType', width: 180 },
  { title: '邀请状态', dataIndex: 'invitationStatus', width: 180 },
  { title: '邀请理由', dataIndex: 'invitation', width: 200 },
  { title: '创建时间', dataIndex: 'createTime', width: 150, sorter: true },
  { title: '过期时间', dataIndex: 'expireTime', width: 150, sorter: true },
]

const getSpaceInvitationInfo = () => {
  loading.value = true
  queryParams.value.params = {}
  queryParams.value.params = {}
  if (dateRange.value != null && Array.isArray(dateRange.value) && dateRange.value.length > 0) {
    queryParams.value.params['beginCreateTime'] = dateRange.value[0]
      .format('YYYY-MM-DD')
      .concat(' 00:00:00')
    queryParams.value.params['endCreateTime'] = dateRange.value[1]
      .format('YYYY-MM-DD')
      .concat(' 23:59:59')
  }
  // console.log('queryParams.value', queryParams.value)
  listSpaceInvitationInfo(queryParams.value).then((res) => {
    invitationList.value = res?.rows || []
    pagination.value.total = res?.total || 0
    loading.value = false
  })
}

const handleSearch = () => {
  pagination.value.current = 1
  queryParams.value.pageNum = 1
  getSpaceInvitationInfo()
}

const resetSearch = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    userType: '1',
    roleType: undefined,
    invitationStatus: undefined,
  }
  dateRange.value = null
  pagination.value.current = 1
  handleSearch()
}

const handleTableChange = (pag, filters, sorter) => {
  pagination.value.current = pag.current
  pagination.value.pageSize = pag.pageSize
  queryParams.value.pageNum = pag.current
  queryParams.value.pageSize = pag.pageSize
  queryParams.value.orderByColumn = sorter.field
  queryParams.value.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc'
  getSpaceInvitationInfo()
}

const dateRange = ref<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)

onMounted(getSpaceInvitationInfo)
</script>
<style scoped lang="scss">
.space-invitation-table {
  margin: 0 2em;
}
</style>
