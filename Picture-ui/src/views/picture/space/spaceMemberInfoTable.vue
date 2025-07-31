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
            <a-image v-if="text" :src="text" :alt="record.userName" width="60" />
            <span v-else>-</span>
          </template>
          <template v-if="column.dataIndex === 'action'">
            <a-space>
              <a-popconfirm
                v-if="
                  (checkPermiSingle('space:invitation') &&
                    record.roleType !== PSpaceRole.SPACE_ROLE_0 &&
                    checkSpaceCreator(record.spaceId)) ||
                  (checkUser(record.userId) && record.roleType !== PSpaceRole.SPACE_ROLE_0)
                "
                title="确定要退出吗，删除之后成员将被踢出团队空间?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record)"
                ><a style="color: red">退出</a>
              </a-popconfirm>
              <a
                v-if="
                  checkPermiSingle('space:invitation') &&
                  record.roleType !== PSpaceRole.SPACE_ROLE_0 &&
                  checkSpaceCreator(record.spaceId)
                "
                @click="handleMember(record)"
                >角色</a
              >
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal v-model:open="openMember" destroy-on-close :footer="null">
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          {{ title }}
          <a-tooltip title="您可以根据不同需求扩容空间的信息">
            <question-circle-outlined class="title-tip-icon" />
          </a-tooltip>
        </div>
      </template>
      <a-form
        :model="formMember"
        :label-col="{ span: 5 }"
        :rules="rules"
        :wrapper-col="{ span: 17 }"
        @finish="handleMemberSubmit"
        ref="formRef"
        layout="horizontal"
      >
        <a-form-item name="roleType">
          <template #label>
            <span style="display: inline-flex; align-items: center">
              成员角色
              <a-tooltip>
                <InfoCircleOutlined
                  style="
                    margin-left: 4px;
                    color: #999;
                    font-size: 14px;
                    position: relative;
                    top: 1px;
                  "
                />
                <template #title>
                  <div style="max-width: 350px; padding: 8px; font-size: 14px; line-height: 1.6">
                    注意事项：<br />
                    1. 创建者不可选；<br />
                    2. 根据不同的角色，邀请成功的用户会获得不同的角色权限；<br />
                    3. 当成员超过上限时，可以扩容成员角色。<br />
                  </div>
                </template>
              </a-tooltip>
            </span>
          </template>
          <a-radio-group v-model:value="formMember.roleType" name="radioGroup">
            <a-radio v-for="dict in p_space_role" :value="dict.dictValue" :key="dict.dictValue">
              {{ dict.dictLabel }}
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <div class="form-footer">
          <a-button @click="openMember = false">取消</a-button>
          <a-button type="primary" html-type="submit" :loading="memberLoading">提交</a-button>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, onMounted, ref } from 'vue'
import dayjs from 'dayjs'
import DictTag from '@/components/DictTag.vue'
import {
  deleteSpaceMemberInfo,
  listSpaceMemberInfo,
  updateSpaceMemberInfo,
} from '@/api/picture/spaceMemberInfo'
import type {
  SpaceMemberInfoQuery,
  SpaceMemberInfoUpdate,
  SpaceMemberInfoVo,
} from '@/types/picture/spaceMemberInfo.d.ts'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { InfoCircleOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue'
import {
  buildSpacePermByUser,
  checkPermiSingle,
  checkSpaceCreator,
  checkSpaceEditor,
  checkSpacePermsAny,
  checkUser,
} from '@/utils/permission.ts'
import { PSpaceRole } from '@/types/picture/space.d.ts'
import { spacePerm } from '@/stores/modules/space.ts'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_space_role, p_space_join_type } = proxy?.useDict('p_space_role', 'p_space_join_type')
onMounted(async () => {
  await spacePerm.loadSpacePerms()
})
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
  deleteSpaceMemberInfo(record.memberId).then((res) => {
    message.success('删除成功！！！')
    getMemberList()
  })
}
//endregion
//region 更改角色
const title = ref<string>('修改用户角色')
const formMember = ref<SpaceMemberInfoUpdate>({
  memberId: '',
  roleType: '',
})
const openMember = ref(false)
const memberLoading = ref(false)
const rules = {
  roleType: [{ required: true, message: '请选择角色', trigger: 'change' }],
}
const handleMember = (record: SpaceMemberInfoVo) => {
  formMember.value.memberId = record.memberId
  formMember.value.roleType = record.roleType
  openMember.value = true
}
const handleMemberSubmit = () => {
  if (formMember.value.roleType === PSpaceRole.SPACE_ROLE_0) {
    message.warning('不可给用户改为创建者')
    return
  }
  memberLoading.value = true
  updateSpaceMemberInfo(formMember.value)
    .then((res) => {
      if (res.code === 200) {
        message.success('更新成功')
        openMember.value = false
        spacePerm.refreshSpacePerms()
        getMemberList()
      } else {
        message.error('更新失败')
      }
    })
    .finally(() => {
      memberLoading.value = false
    })
}
//endregion
onMounted(getMemberList)
</script>
<style scoped lang="scss">
.space-member-table {
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
