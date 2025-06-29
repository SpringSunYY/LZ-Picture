<template>
  <div class="user-team-space-info">
    <a-card title="我的团队空间" :bordered="false">
      <a-form layout="inline" :model="queryParams">
        <a-form-item>
          <a-select
            v-model:value="queryParams.roleType"
            placeholder="角色"
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
        :data-source="teamSpaceList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="spaceId"
        :scroll="{ x: 1000 }"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'roleType'">
            <dict-tag :options="p_space_role" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'maxSize'">
            <span>{{ formatSize(text) }}</span>
          </template>
          <template v-if="column.dataIndex === 'spaceStatus'">
            <dict-tag :options="p_space_status" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'ossType'">
            <dict-tag :options="p_space_oss_type" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'spaceAvatar'">
            <a-image :src="text" width="60" />
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, getCurrentInstance } from 'vue'
import DictTag from '@/components/DictTag.vue'
import { listUserTeamSpaceInfo } from '@/api/picture/space'
import type { UserTeamSpaceInfoVo, UserTeamSpaceInfoQuery } from '@/types/picture/space.d.ts'
import { formatSize } from '@/utils/common.ts'
import dayjs from 'dayjs'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_space_status, p_space_role, p_space_oss_type } = proxy?.useDict(
  'p_space_status',
  'p_space_role',
  'p_space_oss_type',
)

// region 表格数据
const queryParams = ref<UserTeamSpaceInfoQuery>({
  pageNum: 1,
  pageSize: 10,
  isAsc: 'desc',
  roleType: undefined,
})
const dateRange = ref<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)
const teamSpaceList = ref<UserTeamSpaceInfoVo[]>([])
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
  { title: '空间名称', dataIndex: 'spaceName', width: 160 },
  { title: '空间封面', dataIndex: 'spaceAvatar', width: 100 },
  { title: '存储类型', dataIndex: 'ossType', width: 80 },
  { title: '最大容量', dataIndex: 'maxSize', width: 80 },
  { title: '最大文件数', dataIndex: 'maxCount', width: 80 },
  { title: '已用容量', dataIndex: 'totalSize', width: 80 },
  { title: '文件总数', dataIndex: 'totalCount', width: 80 },
  { title: '当前人数', dataIndex: 'currentMembers', width: 80 },
  { title: '成员上限', dataIndex: 'memberLimit', width: 80 },
  { title: '空间状态', dataIndex: 'spaceStatus', width: 80 },
  { title: '角色', dataIndex: 'roleType', width: 80 },
  { title: '最后操作时间', dataIndex: 'lastActiveTime', width: 150, sorter: true },
  { title: '加入时间', dataIndex: 'createTime', width: 150, sorter: true },
  { title: '最后更新时间', dataIndex: 'lastUpdateTime', width: 150 },
]

const getTeamSpaceList = () => {
  loading.value = true
  queryParams.value.params = {}
  if (dateRange.value != null && Array.isArray(dateRange.value) && dateRange.value.length > 0) {
    queryParams.value.params['beginCreateTime'] = dateRange.value[0].format('YYYY-MM-DD').concat(' 00:00:00')
    queryParams.value.params['endCreateTime'] = dateRange.value[1].format('YYYY-MM-DD').concat(' 23:59:59')
  }
  listUserTeamSpaceInfo(queryParams.value).then((res) => {
    teamSpaceList.value = res?.rows || []
    pagination.value.total = res?.total || 0
    loading.value = false
  })
}

const handleSearch = () => {
  pagination.value.current = 1
  queryParams.value.pageNum = 1
  getTeamSpaceList()
}

const resetSearch = () => {
  queryParams.value.roleType = undefined
  pagination.value.current = 1
  dateRange.value = null
  handleSearch()
}

const handleTableChange = (pag, _, sorter) => {
  pagination.value.current = pag.current
  pagination.value.pageSize = pag.pageSize
  queryParams.value.pageNum = pag.current
  queryParams.value.pageSize = pag.pageSize
  queryParams.value.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc'
  getTeamSpaceList()
}
//endregion
onMounted(getTeamSpaceList)
</script>
<style scoped lang="scss">
.space-team-table {
  margin: 0 2em;
}

.custom-modal-title {
  display: flex;
  align-items: center;
  font-size: 16px;

  .title-tip-icon {
    margin-left: 8px;
    color: rgba(57, 57, 57, 0.45);
    cursor: help;
    transition: color 0.3s;

    &:hover {
      color: #1890ff;
    }
  }
}

.form-footer {
  text-align: right;
  padding: 16px 0 0;
  margin-top: 24px;
  border-top: 1px solid #f0f0f0;

  .ant-btn {
    margin-left: 10px;
  }
}
</style>
