<template>
  <div class="user-behavior-table">
    <a-card title="用户行为信息" :bordered="false">
      <!-- 搜索区域 -->
      <a-row :gutter="16" class="mb-4">
        <a-col :span="6">
          <a-input-search
            style="min-width: 200px"
            v-model:value="behaviorQuery.targetContent"
            placeholder="搜索目标内容"
            @search="handleSearch"
            enter-button
          />
        </a-col>
        <a-col :span="6">
          <a-select
            style="min-width: 200px; width: 100%"
            v-model:value="behaviorQuery.behaviorType"
            placeholder="选择行为类型"
            @change="handleSearch"
            allow-clear
          >
            <a-select-option value="VIEW">浏览</a-select-option>
            <a-select-option value="LIKE">点赞</a-select-option>
            <a-select-option value="COMMENT">评论</a-select-option>
            <a-select-option value="SHARE">分享</a-select-option>
            <a-select-option value="COLLECT">收藏</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="6">
          <a-range-picker
            v-model:value="dateRange"
            @change="handleSearch"
            format="YYYY-MM-DD"
            style="width: 100%; min-width: 200px"
          />
        </a-col>
        <a-col :span="6">
          <a-button type="primary" @click="resetSearch">重置</a-button>
        </a-col>
      </a-row>

      <!-- 表格 -->
      <a-table
        :columns="columns"
        :data-source="behaviorList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="behaviorId"
      >
        <!-- 行为类型列自定义渲染 -->
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'behaviorType'">
            <a-tag :color="getBehaviorTypeColor(text)">{{ getBehaviorTypeText(text) }}</a-tag>
          </template>

          <!-- 目标内容列自定义渲染 -->
          <template v-if="column.dataIndex === 'targetContent'">
            <a-tooltip :title="text">
              <span>{{ text && text.length > 20 ? text.substring(0, 20) + '...' : text }}</span>
            </a-tooltip>
          </template>

          <!-- 封面列自定义渲染 -->
          <template v-if="column.dataIndex === 'targetCover'">
            <a-image v-if="text" :width="50" :src="text" :preview="{ src: text }" alt="封面" />
            <span v-else>-</span>
          </template>

          <!-- 标签列自定义渲染 -->
          <template v-if="column.dataIndex === 'tags'">
            <template v-if="text">
              <a-tag v-for="tag in text.split(',')" :key="tag" color="blue">{{ tag }}</a-tag>
            </template>
            <span v-else>-</span>
          </template>

          <!-- 操作列 -->
          <template v-if="column.dataIndex === 'action'">
            <a-space>
              <a @click="viewDetail(record)">查看</a>
              <a-divider type="vertical" />
              <!--              <a-popconfirm-->
              <!--                title="确定要删除这条记录吗?"-->
              <!--                ok-text="是"-->
              <!--                cancel-text="否"-->
              <!--                @confirm="handleDelete(record)"-->
              <!--              >-->
              <!--                <a class="text-red-500">删除</a>-->
              <!--              </a-popconfirm>-->
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import type { UserBehaviorInfoQuery, UserBehaviorInfoVo } from '@/types/picture/userBehaviorInfo'
import { listUserBehaviorInfo } from '@/api/picture/userBehaviorInfo.ts'

const behaviorList = ref<UserBehaviorInfoVo[]>()
const behaviorQuery = ref<UserBehaviorInfoQuery>({
  pageNum: 1,
  pageSize: 10,
  isAsc: 'desc',
  behaviorType: '',
  targetContent: '',
  targetType: '',
})

// 分页配置
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total) => `共 ${total} 条记录`,
})
const dateRange = ref<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)
const loading = ref(false)
const getBehaviorList = () => {
  loading.value = true
  behaviorQuery.value.params = {}
  if (dateRange.value != null && dateRange.value != []) {
    console.log('dateRange', dateRange.value)
    behaviorQuery.value.params['beginCreateTime'] = dateRange.value[0].format('YYYY-MM-DD')
    behaviorQuery.value.params['endCreateTime'] = dateRange.value[1].format('YYYY-MM-DD')
  }
  listUserBehaviorInfo(behaviorQuery.value).then((res) => {
    behaviorList.value = res?.rows || []
    pagination.value.total = res?.total || 0
    loading.value = false
  })
}

getBehaviorList()
// 表格列定义
const columns = [
  {
    title: '封面',
    dataIndex: 'targetCover',
    width: 100,
  },
  {
    title: '目标内容',
    dataIndex: 'targetContent',
    width: 200,
  },
  {
    title: '目标类型',
    dataIndex: 'targetType',
    width: 100,
  },
  {
    title: '行为类型',
    dataIndex: 'behaviorType',
    width: 100,
  },
  {
    title: '分享链接',
    dataIndex: 'shareLink',
    width: 150,
    ellipsis: true,
  },
  {
    title: '标签',
    dataIndex: 'tags',
    width: 150,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
    sorter: true,
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 120,
  },
]


// 状态定义
const searchText = ref('')
const searchBehaviorType = ref(null)

// 处理表格变化（分页、排序、筛选）
const handleTableChange = (pag, filters, sorter) => {
  loading.value = true
  // 更新分页信息
  pagination.value.current = pag.current
  pagination.value.pageSize = pag.pageSize
  behaviorQuery.value.pageNum = pagination.value.current
  behaviorQuery.value.pageSize = pagination.value.pageSize
  getBehaviorList()
}

// 搜索处理
const handleSearch = () => {
  pagination.value.current = 1
  console.log(dateRange.value)
  getBehaviorList()
}

// 重置搜索
const resetSearch = () => {
  searchText.value = ''
  searchBehaviorType.value = null
  dateRange.value = null
  pagination.value.current = 1
  handleSearch()
}

// 查看详情
const viewDetail = (record) => {
  message.info(`查看行为ID: ${record.behaviorId} 的详情`)
  console.log('查看详情:', record)
}

// 获取行为类型颜色
const getBehaviorTypeColor = (type) => {
  const colorMap = {
    VIEW: 'blue',
    LIKE: 'red',
    COMMENT: 'green',
    SHARE: 'purple',
    COLLECT: 'orange',
  }
  return colorMap[type] || 'default'
}

// 获取行为类型文本
const getBehaviorTypeText = (type) => {
  const textMap = {
    VIEW: '浏览',
    LIKE: '点赞',
    COMMENT: '评论',
    SHARE: '分享',
    COLLECT: '收藏',
  }
  return textMap[type] || type
}

// 组件挂载时初始化
onMounted(() => {
  // 可以在这里加载真实数据
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 500)
})
</script>

<style scoped></style>
