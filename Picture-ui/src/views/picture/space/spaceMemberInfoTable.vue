<template>
  <div class="space-member-table">
    <a-card title="空间成员信息" :bordered="false">
      <a-form layout="inline" :model="queryParams">
        <a-form-item>
          <a-select
            v-model:value="queryParams.roleType"
            placeholder="角色类型"
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
            v-model:value="queryParams.joinType"
            placeholder="加入方式"
            allow-clear
            @change="handleSearch"
            style="width: 180px"
          >
            <a-select-option
              v-for="item in p_space_join_type"
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
            style="width: 100%; min-width: 240px"
          />
        </a-form-item>
        <a-button type="primary" @click="resetSearch">重置</a-button>
      </a-form>
      <div style="margin-bottom: 20px"></div>
      <a-table
        :columns="columns"
        :data-source="memberList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="memberId"
        :scroll="{ x: 1000 }"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'roleType'">
            <dict-tag :options="p_space_role" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'joinType'">
            <dict-tag :options="p_space_join_type" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'avatarUrl'">
            <a-image v-if="text" :src="text" width="60" />
            <span v-else>-</span>
          </template>
          <template v-if="column.dataIndex === 'action'">
            <a-popconfirm
              title="确定要删除吗，删除之后成员将被踢出团队空间?"
              ok-text="是"
              cancel-text="否"
              @confirm="handleDelete(record)"
              ><a style="color: red">删除</a>
            </a-popconfirm>
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
import { deleteSpaceMemberInfo, listSpaceMemberInfo } from '@/api/picture/spaceMemberInfo'
import type { SpaceMemberInfoQuery, SpaceMemberInfoVo } from '@/types/picture/spaceMemberInfo.d.ts'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_space_role, p_space_join_type } = proxy?.useDict('p_space_role', 'p_space_join_type')

// region空间成员列表
const route = useRoute()
const spaceId = ref<string>(route.query.spaceId as string)

const queryParams = ref<SpaceMemberInfoQuery>({
  pageNum: 1,
  spaceId: spaceId.value,
  pageSize: 10,
  roleType: undefined,
  joinType: undefined,
})
const dateRange = ref<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)

const memberList = ref<SpaceMemberInfoVo[]>([])
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
  { title: '成员编号', dataIndex: 'memberId', width: 160 },
  { title: '空间名称', dataIndex: 'spaceName', width: 180 },
  { title: '用户', dataIndex: 'userName', width: 150 },
  { title: '用户头像', dataIndex: 'avatarUrl', width: 100 },
  { title: '角色类型', dataIndex: 'roleType', width: 50 },
  { title: '邀请人', dataIndex: 'inviterUserName', width: 150 },
  { title: '加入方式', dataIndex: 'joinType', width: 50 },
  { title: '最后操作时间', dataIndex: 'lastActiveTime', width: 150, sorter: true },
  { title: '加入时间', dataIndex: 'createTime', width: 150, sorter: true },
  { title: '操作', dataIndex: 'action', width: 100 },
]

const getMemberList = () => {
  loading.value = true
  queryParams.value.params = {}
  if (dateRange.value) {
    queryParams.value.params.beginCreateTime = dateRange.value[0].format('YYYY-MM-DD') + ' 00:00:00'
    queryParams.value.params.endCreateTime = dateRange.value[1].format('YYYY-MM-DD') + ' 23:59:59'
  }
  listSpaceMemberInfo(queryParams.value).then((res) => {
    memberList.value = res?.rows || []
    pagination.value.total = res?.total || 0
    loading.value = false
  })
}

const handleSearch = () => {
  pagination.value.current = 1
  queryParams.value.pageNum = 1
  getMemberList()
}

const resetSearch = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    spaceId: spaceId.value,
    roleType: undefined,
    joinType: undefined,
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
  getMemberList()
}
//endregion
//region 删除
const handleDelete = (record) => {
  deleteSpaceMemberInfo(record.memberId).then(res => {
    message.success('删除成功！！！')
    getMemberList()
  })
}
//endregion
onMounted(getMemberList)
</script>
<style scoped lang="scss">
.space-member-table {
  margin: 0 2em;
}
</style>
