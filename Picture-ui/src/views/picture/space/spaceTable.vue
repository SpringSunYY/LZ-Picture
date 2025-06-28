<template>
  <div class="space-table">
    <a-card title="我的空间" :bordered="false">
      <a-form layout="inline" :model="queryParams">
        <a-form-item>
          <a-input
            v-model:value="queryParams.spaceName"
            placeholder="空间名称"
            allow-clear
            @pressEnter="handleSearch"
            style="width: 200px"
          />
        </a-form-item>
        <a-form-item>
          <a-select
            v-model:value="queryParams.spaceType"
            placeholder="空间类型"
            allow-clear
            @change="handleSearch"
            style="width: 180px"
          >
            <a-select-option
              v-for="item in p_space_type"
              :key="item.dictValue"
              :value="item.dictValue"
            >
              {{ item.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-select
            v-model:value="queryParams.spaceStatus"
            placeholder="空间状态"
            allow-clear
            @change="handleSearch"
            style="width: 180px"
          >
            <a-select-option
              v-for="item in p_space_status"
              :key="item.dictValue"
              :value="item.dictValue"
            >
              {{ item.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-select
            v-model:value="queryParams.ossType"
            placeholder="存储类型"
            allow-clear
            @change="handleSearch"
            style="width: 180px"
          >
            <a-select-option
              v-for="item in p_space_oss_type"
              :key="item.dictValue"
              :value="item.dictValue"
            >
              {{ item.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-button type="primary" @click="resetSearch">重置</a-button>
      </a-form>
      <div style="margin-bottom: 20px"></div>
      <a-table
        :columns="columns"
        :data-source="spaceList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="spaceId"
        :scroll="{ x: 1000 }"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'spaceType'">
            <dict-tag :options="p_space_type" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'spaceStatus'">
            <dict-tag :options="p_space_status" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'maxSize'">
            <span>{{ formatSize(text) }}</span>
          </template>
          <template v-if="column.dataIndex === 'totalSize'">
            <span>{{ formatSize(text) }}</span>
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
import { listUserPersonalSpaceInfo } from '@/api/picture/space'
import type { SpaceInfoQuery, UserPersonalSpaceInfoVo } from '@/types/picture/space.d.ts'
import { formatSize } from '@/utils/common.ts'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_space_status, p_space_type, p_space_oss_type } = proxy?.useDict(
  'p_space_status',
  'p_space_type',
  'p_space_oss_type',
)

const queryParams = ref<SpaceInfoQuery>({
  pageNum: 1,
  pageSize: 10,
  isAsc: 'desc',
  spaceStatus: undefined,
  spaceType: undefined,
})

const spaceList = ref<UserPersonalSpaceInfoVo[]>([])
const loading = ref(false)
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showTotal: (total: number) => `共 ${total} 条记录`,
  showSizeChanger: true,
  showQuickJumper: true,
  pageSizeOptions: ['10', '20', '30'],
})

const columns = [
  { title: '封面', dataIndex: 'spaceAvatar', width: 100 },
  { title: '空间名称', dataIndex: 'spaceName', width: 120 },
  { title: '空间类型', dataIndex: 'spaceType', width: 80 },
  { title: '状态', dataIndex: 'spaceStatus', width: 80 },
  { title: '容量', dataIndex: 'maxSize', width: 120 },
  { title: '已用容量', dataIndex: 'totalSize', width: 120, sorter: true },
  { title: '文件总数', dataIndex: 'maxCount', width: 100 },
  { title: '文件数', dataIndex: 'totalCount', width: 100, sorter: true },
  { title: 'OSS类型', dataIndex: 'ossType', width: 120 },
  { title: '创建时间', dataIndex: 'createTime', width: 180, sorter: true },
  { title: '更新时间', dataIndex: 'updateTime', width: 180 },
  { title: '最后上传时间', dataIndex: 'lastUpdateTime', width: 180, sorter: true },
]

const getSpaceList = () => {
  loading.value = true
  listUserPersonalSpaceInfo(queryParams.value).then((res) => {
    spaceList.value = res?.rows || []
    pagination.value.total = res?.total || 0
    loading.value = false
  })
}

const handleSearch = () => {
  pagination.value.current = 1
  queryParams.value.pageNum = 1
  getSpaceList()
}

const resetSearch = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    isAsc: 'desc',
    spaceStatus: undefined,
    spaceType: undefined,
  }
  pagination.value.current = 1
  handleSearch()
}

const handleTableChange = (pag, _, sorter) => {
  pagination.value.current = pag.current
  pagination.value.pageSize = pag.pageSize
  queryParams.value.pageNum = pag.current
  queryParams.value.pageSize = pag.pageSize
  queryParams.value.orderByColumn = sorter.field
  queryParams.value.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc'
  getSpaceList()
}

onMounted(getSpaceList)
</script>
<style scoped lang="scss">
.space-table {
  margin: 0 2em;
}
</style>
