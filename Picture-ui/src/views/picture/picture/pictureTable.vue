<template>
  <div class="picture-table">
    <a-card title="我的图片" :bordered="false">
      <a-form layout="inline" :model="queryParams">
        <a-form-item>
          <a-input
            v-model:value="queryParams.name"
            placeholder="图片名称"
            allow-clear
            @pressEnter="handleSearch"
            style="width: 200px"
          />
        </a-form-item>
        <a-form-item>
          <a-input
            v-model:value="queryParams.categoryName"
            placeholder="分类名称"
            allow-clear
            @pressEnter="handleSearch"
            style="width: 200px"
          />
        </a-form-item>
        <a-button type="primary" @click="resetSearch">重置</a-button>
      </a-form>

      <a-table
        :columns="columns"
        :data-source="pictureList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="pictureId"
        size="middle"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'thumbnailUrl'">
            <a-image :src="record.thumbnailUrl" width="60" height="60" :preview="true" />
          </template>
          <template v-if="column.dataIndex === 'pictureStatus'">
            <dict-tag :value="record.pictureStatus" :options="p_picture_status" />
          </template>
          <template v-if="column.dataIndex === 'tags'">
            <a-space wrap>
              <a-tag v-for="tag in record.tags" :key="tag" color="blue">{{ tag }}</a-tag>
            </a-space>
          </template>
          <template v-if="column.dataIndex === 'picSize'">
            {{ formatSize(record.picSize) }}
          </template>
          <template v-if="column.dataIndex === 'picDimensions'">
            {{ record.picWidth }} × {{ record.picHeight }}
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, getCurrentInstance } from 'vue'
import { listMyTable } from '@/api/picture/picture'
import DictTag from '@/components/DictTag.vue'

const { proxy } = getCurrentInstance()!
const { p_picture_status } = proxy?.useDict('p_picture_status')

const pictureList = ref([])
const loading = ref(false)
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showTotal: (total: number) => `共 ${total} 条记录`,
  showSizeChanger: true,
  showQuickJumper: true,
})

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  isAsc: 'desc',
  name: '',
  categoryName: '',
})

const columns = [
  { title: '缩略图', dataIndex: 'thumbnailUrl', width: 80 },
  { title: '图片名称', dataIndex: 'name' },
  { title: '简介', dataIndex: 'introduction', ellipsis: true },
  { title: '分类', dataIndex: 'categoryName', width: 100 },
  { title: '体积', dataIndex: 'picSize', width: 100 },
  { title: '尺寸', dataIndex: 'picDimensions', width: 120 },
  { title: '比例', dataIndex: 'picScale', width: 80 },
  { title: '格式', dataIndex: 'picFormat', width: 80 },
  { title: '状态', dataIndex: 'pictureStatus', width: 100 },
  { title: '标签', dataIndex: 'tags' },
  { title: '创建时间', dataIndex: 'createTime', sorter: true, width: 180 },
]

const getList = () => {
  loading.value = true
  listMyTable(queryParams.value).then((res) => {
    pictureList.value = res?.rows || []
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
    name: '',
    categoryName: '',
  }
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

const formatSize = (bytes: number): string => {
  if (!bytes) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']
  let i = 0
  while (bytes >= 1024 && i < units.length - 1) {
    bytes /= 1024
    i++
  }
  return `${bytes.toFixed(1)} ${units[i]}`
}

onMounted(getList)
</script>
