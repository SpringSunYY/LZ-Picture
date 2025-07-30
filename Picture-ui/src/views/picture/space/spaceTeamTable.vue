<template>
  <div class="team-space-table">
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
        :scroll="{ x: 1200 }"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'spaceName'">
            <router-link
              :to="`/spaceManage/member?spaceId=${record.spaceId}`"
              :style="{ textDecoration: 'underline', color: 'blue' }"
              >{{ text }}
            </router-link>
          </template>
          <template v-if="column.dataIndex === 'roleType'">
            <dict-tag :options="p_space_role" :value="text" />
          </template>
          <template v-if="column.dataIndex === 'maxSize'">
            <span>{{ formatSize(text) }}</span>
          </template>
          <template v-if="column.dataIndex === 'totalSize'">
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
          <template v-if="column.dataIndex === 'action'">
            <a-space>
              <a
                @click="handleInvitation(record.spaceId)"
                v-if="
                  checkPermiSingle('space:invitation') &&
                  checkSpacePerm(buildSpacePermByUser(record.spaceId, PSpaceRole.SPACE_ROLE_0))
                "
                >邀请</a
              >
              <router-link
                :to="`/spaceManage/pictureTeamTable?spaceId=${record.spaceId}&spaceName=${record.spaceName}`"
                >查看
              </router-link>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal v-model:open="openInvitation" destroy-on-close :footer="null">
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
        :model="formInvitation"
        :label-col="{ span: 5 }"
        :rules="rules"
        :wrapper-col="{ span: 17 }"
        @finish="handleInvitationSubmit"
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
          <a-radio-group v-model:value="formInvitation.roleType" name="radioGroup">
            <a-radio v-for="dict in p_space_role" :value="dict.dictValue" :key="dict.dictValue">
              {{ dict.dictLabel }}
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item name="userName" label="用户账号">
          <a-input v-model:value="formInvitation.userName" placeholder="请输入用户账号" />
        </a-form-item>
        <a-form-item name="invitation" label="邀请理由">
          <a-textarea
            :maxLength="512"
            :showCount="true"
            v-model:value="formInvitation.invitation"
            placeholder="请输入邀请理由"
          />
        </a-form-item>
        <div class="form-footer">
          <a-button @click="openInvitation = false">取消</a-button>
          <a-button type="primary" html-type="submit" :loading="invitationLoading">提交</a-button>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, onMounted, ref } from 'vue'
import DictTag from '@/components/DictTag.vue'
import { listUserTeamSpaceInfo } from '@/api/picture/space'
import {
  PSpaceRole,
  type UserTeamSpaceInfoQuery,
  type UserTeamSpaceInfoVo,
} from '@/types/picture/space.d.ts'
import { formatSize } from '@/utils/common.ts'
import dayjs from 'dayjs'
import { InfoCircleOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue'
import type { SpaceInvitationInfoAdd } from '@/types/picture/spaceInvitationInfo'
import { message } from 'ant-design-vue'
import { addSpaceInvitationInfo } from '@/api/picture/spaceInvitationInfo.ts'
import { buildSpacePermByUser, checkPermiSingle, checkSpacePerm } from '@/utils/permission.ts'
import { spacePerm } from '@/stores/modules/space.ts'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_space_status, p_space_role, p_space_oss_type } = proxy?.useDict(
  'p_space_status',
  'p_space_role',
  'p_space_oss_type',
)
onMounted(async () => {
  await spacePerm.loadSpacePerms()
})

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
  { title: '空间名称', dataIndex: 'spaceName', width: 120 },
  { title: '空间封面', dataIndex: 'spaceAvatar', width: 100 },
  { title: '存储类型', dataIndex: 'ossType', width: 80 },
  { title: '容量限制', dataIndex: 'maxSize', width: 80 },
  { title: '文件限制', dataIndex: 'maxCount', width: 80 },
  { title: '已用容量', dataIndex: 'totalSize', width: 80 },
  { title: '文件总数', dataIndex: 'totalCount', width: 80 },
  { title: '当前人数', dataIndex: 'currentMembers', width: 80 },
  { title: '成员上限', dataIndex: 'memberLimit', width: 80 },
  { title: '状态', dataIndex: 'spaceStatus', width: 70 },
  { title: '角色', dataIndex: 'roleType', width: 70 },
  { title: '最后操作时间', dataIndex: 'lastActiveTime', width: 150, sorter: true },
  { title: '加入时间', dataIndex: 'createTime', width: 150, sorter: true },
  { title: '最后更新时间', dataIndex: 'lastUpdateTime', width: 150 },
  { title: '操作', dataIndex: 'action', width: 100, fixed: 'right' },
]

const getTeamSpaceList = () => {
  loading.value = true
  queryParams.value.params = {}
  if (dateRange.value != null && Array.isArray(dateRange.value) && dateRange.value.length > 0) {
    queryParams.value.params['beginCreateTime'] = dateRange.value[0]
      .format('YYYY-MM-DD')
      .concat(' 00:00:00')
    queryParams.value.params['endCreateTime'] = dateRange.value[1]
      .format('YYYY-MM-DD')
      .concat(' 23:59:59')
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
  queryParams.value.orderByColumn = sorter.field
  queryParams.value.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc'
  getTeamSpaceList()
}
//endregion
// region 邀请
const openInvitation = ref(false)
const title = ref('邀请空间成员')
const invitationLoading = ref(false)
const formInvitation = ref<SpaceInvitationInfoAdd>({
  spaceId: '',
  roleType: '1',
  invitation: '',
  userName: '',
})
const handleInvitation = (spaceId: string) => {
  formInvitation.value = {
    spaceId: '',
    roleType: '1',
    invitation: '',
    userName: '',
  }
  formInvitation.value.spaceId = spaceId
  openInvitation.value = true
}
const rules = {
  roleType: [{ required: true, message: '请选择角色', trigger: 'change' }],
  userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  invitation: [{ required: true, message: '请输入邀请理由', trigger: 'blur' }],
}
const handleInvitationSubmit = () => {
  if (formInvitation.value.roleType === PSpaceRole.SPACE_ROLE_0) {
    message.error('创建者不能邀请')
    return
  }
  invitationLoading.value = true
  addSpaceInvitationInfo(formInvitation.value)
    .then(() => {
      message.success('邀请成功')
      getTeamSpaceList()
      openInvitation.value = false
    })
    .catch(() => {
      message.error('邀请失败')
    })
    .finally(() => {
      invitationLoading.value = false
    })
}
//endregion
onMounted(getTeamSpaceList)
</script>
<style scoped lang="scss">
.team-space-table {
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
