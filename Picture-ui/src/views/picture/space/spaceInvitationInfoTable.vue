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
          <template v-if="column.dataIndex === 'action'">
            <a-space-compact>
              <a-dropdown placement="bottomRight" :trigger="['click']">
                <a-button> 操作</a-button>
                <template #overlay>
                  <a-menu>
                    <a-menu-item
                      v-if="
                        queryParams.userType === '1' &&
                        record.invitationStatus ===
                          PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_0 &&
                        checkPermiSingle('space:invitation')
                      "
                    >
                      <a-popconfirm
                        title="您确认同意吗?"
                        ok-text="Yes"
                        cancel-text="No"
                        @confirm="
                          confirmAction(
                            PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_1,
                            record,
                          )
                        "
                        ><a style="color: #3ad83a"> 同意</a>
                      </a-popconfirm>
                    </a-menu-item>
                    <a-menu-item
                      v-if="
                        queryParams.userType === '1' &&
                        record.invitationStatus ===
                          PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_0 &&
                        checkPermiSingle('space:invitation')
                      "
                    >
                      <a-popconfirm
                        title="您确认拒绝吗?"
                        ok-text="Yes"
                        cancel-text="No"
                        @confirm="
                          confirmAction(
                            PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_2,
                            record,
                          )
                        "
                        ><a style="color: #b1b141">拒绝</a>
                      </a-popconfirm>
                    </a-menu-item>
                    <a-menu-item
                      v-if="
                        queryParams.userType === '0' &&
                        record.invitationStatus ===
                          PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_0 &&
                        checkPermiSingle('space:invitation')
                      "
                    >
                      <a-popconfirm
                        title="您确认取消吗"
                        ok-text="Yes"
                        cancel-text="No"
                        @confirm="
                          confirmAction(
                            PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_4,
                            record,
                          )
                        "
                        ><a style="color: red">取消</a>
                      </a-popconfirm>
                    </a-menu-item>
                    <a-menu-item>
                      <a-popconfirm
                        title="确定要删除这条记录吗?"
                        ok-text="是"
                        cancel-text="否"
                        @confirm="handleDelete(record)"
                        v-if="checkPermiSingle('space:invitation')"
                        ><a style="color: red">删除</a>
                      </a-popconfirm>
                    </a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </a-space-compact>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, onMounted, ref } from 'vue'
import DictTag from '@/components/DictTag.vue'
import {
  cancelSpaceInvitationInfo,
  listSpaceInvitationInfo,
  userActionSpaceInvitationInfo,
} from '@/api/picture/spaceInvitationInfo'
import {
  PSpaceInvitationStatusEnum,
  type SpaceInvitationInfoQuery,
  type SpaceInvitationInfoVo,
} from '@/types/picture/spaceInvitationInfo.d.ts'
import dayjs from 'dayjs'
import { checkPermiSingle } from '@/utils/permission.ts'
import { message } from 'ant-design-vue'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_space_role, p_space_invitation_status } = proxy?.useDict(
  'p_space_role',
  'p_space_invitation_status',
)
//region 列表
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
  { title: '操作', dataIndex: 'action', width: 50 },
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
  const userType = queryParams.value.userType
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    userType: userType,
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
//endregion
//region 操作列
const confirmAction = (action: string, record: SpaceInvitationInfoVo) => {
  if (
    action === PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_1 ||
    action === PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_2
  ) {
    userActionSpaceInvitationInfo({
      invitationId: record.invitationId,
      invitationStatus: action,
    }).then((res) => {
      if (res.code === 200) {
        message.success('操作成功')
        getSpaceInvitationInfo()
      } else {
        message.error(res.msg || '操作失败')
      }
    })
  } else if (action === PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_4) {
    cancelSpaceInvitationInfo({
      invitationId: record.invitationId,
      invitationStatus: action,
    }).then((res) => {
      if (res.code === 200) {
        message.success('操作成功')
      } else {
        message.error(res.msg || '操作失败')
      }
      getSpaceInvitationInfo()
    })
  }
}
//endregion
const handleDelete = (record: SpaceInvitationInfoVo) => {
  console.log('删除', record)
}
onMounted(getSpaceInvitationInfo)
</script>
<style scoped lang="scss">
.space-invitation-table {
  margin: 0 2em;
}
</style>
