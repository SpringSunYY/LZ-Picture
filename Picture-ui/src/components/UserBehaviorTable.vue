<template>
  <div class="user-behavior-table">
    <a-card title="用户行为信息" :bordered="false">
      <a-form layout="inline" :model="behaviorQuery">
        <!-- 搜索区域 -->
        <a-form-item>
          <a-input-search
            style="min-width: 200px"
            v-model:value="behaviorQuery.targetContent"
            placeholder="搜索目标内容"
            @search="handleSearch"
            enter-button
          />
        </a-form-item>
        <a-form-item>
          <a-select
            style="width: 200px"
            v-model:value="behaviorQuery.behaviorType"
            placeholder="选择行为类型"
            @change="handleSearch"
            allow-clear
          >
            <a-select-option
              v-for="dict in p_user_behavior_type"
              :key="dict.dictValue"
              :value="dict.dictValue || ''"
            >
              {{ dict.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-select
            style="width: 200px"
            v-model:value="behaviorQuery.targetType"
            placeholder="选择目标类型"
            @change="handleSearch"
            allow-clear
          >
            <a-select-option
              v-for="dict in p_user_behavior_target_type"
              :key="dict.dictValue"
              :value="dict.dictValue || ''"
            >
              {{ dict.dictLabel }}
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
      <!-- 表格 -->
      <a-table
        :columns="columns"
        :data-source="behaviorList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="behaviorId"
        :scroll="{ x: 1000 }"
      >
        <!-- 行为类型列自定义渲染 -->
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'behaviorType'">
            <dict-tag :options="p_user_behavior_type" :value="text" />
          </template>

          <template v-if="column.dataIndex === 'targetType'">
            <dict-tag :options="p_user_behavior_target_type" :value="text" />
          </template>

          <!-- 目标内容列自定义渲染 -->
          <template v-if="column.dataIndex === 'targetContent'">
            <a-tooltip :title="text">
              <span>{{ text && text.length > 20 ? text.substring(0, 20) + '...' : text }}</span>
            </a-tooltip>
          </template>

          <!-- 封面列自定义渲染 -->
          <template v-if="column.dataIndex === 'targetCover'">
            <a-image v-if="text" :width="50" :src="text" :preview="{ src: text }" alt="封面失效" />
            <span v-else>-</span>
          </template>

          <!-- 标签列自定义渲染 -->
          <template v-if="column.dataIndex === 'tags'">
            <template v-if="text">
              <tags
                :values="text.split(';')"
                :colors="['pink', 'pink', 'orange', 'green', 'cyan', 'blue', 'purple']"
              />
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
import { getCurrentInstance, onMounted, ref } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import {
  PUserBehaviorTargetTypeEnum,
  type UserBehaviorInfoQuery,
  type UserBehaviorInfoVo,
} from '@/types/picture/userBehaviorInfo.d.ts'
import { listUserBehaviorInfo } from '@/api/picture/userBehaviorInfo.ts'
import DictTag from '@/components/DictTag.vue'
import Tags from '@/components/Tags.vue'
import { useRouter } from 'vue-router'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_user_behavior_type, p_user_behavior_target_type } = proxy?.useDict(
  'p_user_behavior_type',
  'p_user_behavior_target_type',
)

const behaviorList = ref<UserBehaviorInfoVo[]>()
const behaviorQuery = ref<UserBehaviorInfoQuery>({
  pageNum: 1,
  pageSize: 10,
  isAsc: 'desc',
  behaviorType: null,
  targetContent: '',
  targetType: null,
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
  if (dateRange.value != null && Array.isArray(dateRange.value) && dateRange.value.length > 0) {
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

// 处理表格变化（分页、排序、筛选）
const handleTableChange = (pag, filters, sorter) => {
  // 更新分页信息
  pagination.value.current = pag.current
  pagination.value.pageSize = pag.pageSize
  behaviorQuery.value.pageNum = pagination.value.current
  behaviorQuery.value.pageSize = pagination.value.pageSize
  //判断是升序还是降序
  behaviorQuery.value.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc'
  getBehaviorList()
}

// 搜索处理
const handleSearch = () => {
  pagination.value.current = 1
  getBehaviorList()
}

// 重置搜索
const resetSearch = () => {
  behaviorQuery.value = {
    pageNum: 1,
    pageSize: 10,
    isAsc: 'desc',
    behaviorType: null,
    targetContent: '',
    targetType: null,
  }
  dateRange.value = null
  pagination.value.current = 1
  handleSearch()
}

// 路由跳转
const router = useRouter()
// 查看详情
const viewDetail = (record) => {
  //如果是图片
  if (record.targetType === PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0) {
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

// 组件挂载时初始化
onMounted(() => {
  // 可以在这里加载真实数据
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 500)
})
</script>

<style scoped lang="scss"></style>
