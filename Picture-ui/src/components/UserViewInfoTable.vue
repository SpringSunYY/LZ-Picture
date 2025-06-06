<template>
  <div class="user-view-log">
    <a-card title="我的浏览记录" :bordered="false">
      <a-form layout="inline" :model="queryParams">
        <a-form-item>
          <a-input-search
            v-model:value="queryParams.targetContent"
            placeholder="搜索目标内容"
            @search="handleSearch"
            enter-button
            style="min-width: 200px"
          />
        </a-form-item>
        <a-form-item>
          <a-select
            v-model:value="queryParams.targetType"
            placeholder="目标类型"
            allow-clear
            @change="handleSearch"
            style="width: 200px"
          >
            <a-select-option
              v-for="item in p_view_log_target_type"
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
        :data-source="viewLogList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="viewId"
        :scroll="{ x: 1000 }"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'targetType'">
            <dict-tag :options="p_view_log_target_type" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'targetContent'">
            <a-tooltip :title="text">
              <span>{{ text?.length > 20 ? text.slice(0, 20) + '...' : text }}</span>
            </a-tooltip>
          </template>
          <!-- 标签列自定义渲染 -->
          <template v-if="column.dataIndex === 'tags'">
            <template v-if="text">
              <tags
                :values="text.split(COMMON_SEPARATION)"
                :colors="['pink', 'pink', 'orange', 'green', 'cyan', 'blue', 'purple']"
              />
            </template>
            <span v-else>-</span>
          </template>
          <template v-if="column.dataIndex === 'targetCover'">
            <a-image v-if="text" :width="60" :src="text" :preview="{ src: text }" />
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
import { getCurrentInstance, onMounted, ref } from 'vue'
import DictTag from '@/components/DictTag.vue'
import Tags from '@/components/Tags.vue'
import { listUserViewLogInfo } from '@/api/picture/userViewLogInfo.ts'
import {
  PViewLogTargetTypeEnum,
  type UserViewLogInfoVo,
  type UserViewLogQuery,
} from '@/types/picture/userViewLogInfo.d.ts'
import dayjs from 'dayjs'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { COMMON_SEPARATION } from '@/types/common.d.ts'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_view_log_target_type } = proxy?.useDict('p_view_log_target_type')

const viewLogList = ref<UserViewLogInfoVo[]>([])
const loading = ref(false)
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showTotal: (total: number) => `共 ${total} 条记录`,
  showSizeChanger: true,
  showQuickJumper: true,
})
const dateRange = ref<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)
const queryParams = ref<UserViewLogQuery>({
  pageNum: 1,
  pageSize: 10,
  isAsc: 'desc',
  targetContent: '',
  targetType: null,
})

const columns = [
  { title: '封面', dataIndex: 'targetCover' },
  { title: '目标内容', dataIndex: 'targetContent' },
  { title: '目标类型', dataIndex: 'targetType' },
  { title: '标签', dataIndex: 'tags' },
  { title: '查看时间', dataIndex: 'createTime', width: 180, sorter: true },
  { title: '操作', dataIndex: 'action', fixed: 'right', width: 120 },
]

const getUserViewLogInfo = () => {
  loading.value = true
  queryParams.value.params = {}
  if (dateRange.value != null && Array.isArray(dateRange.value) && dateRange.value.length > 0) {
    console.log('dateRange', dateRange.value)
    queryParams.value.params['beginCreateTime'] = dateRange.value[0].format('YYYY-MM-DD').concat(' 00:00:00')
    queryParams.value.params['endCreateTime'] = dateRange.value[1].format('YYYY-MM-DD').concat(' 23:59:59')
  }
  listUserViewLogInfo(queryParams.value).then((res) => {
    viewLogList.value = res?.rows || []
    pagination.value.total = res?.total || 0
    loading.value = false
  })
}

const handleSearch = () => {
  pagination.value.current = 1
  queryParams.value.pageNum = 1
  getUserViewLogInfo()
}

const resetSearch = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    targetContent: '',
    isAsc: 'desc',
    targetType: null,
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
  //判断是升序还是降序
  queryParams.value.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc'
  getUserViewLogInfo()
}

// 路由跳转
const router = useRouter()
// 查看详情
const viewDetail = (record) => {
  //如果是图片
  if (record.targetType === PViewLogTargetTypeEnum.VIEW_LOG_TARGET_TYPE_0) {
    const routeData = router.resolve({
      path: '/pictureDetail',
      query: { pictureId: record.targetId },
    })
    window.open(routeData.href, '_blank')
  } else {
    message.info(`查看行为ID: ${record.behaviorId} 的详情`)
    console.log('查看详情:', record)
  }
}

onMounted(getUserViewLogInfo)
</script>
