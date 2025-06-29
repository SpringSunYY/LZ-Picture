<template>
  <div class="space-personal-table">
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
          <template v-if="column.dataIndex === 'action'">
            <a-space>
              <a @click="handleSpaceDilatation(record.spaceId)">扩容</a>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal v-model:open="openDilatation" destroy-on-close :footer="null">
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
        :model="formDilatation"
        :label-col="{ span: 5 }"
        :rules="rules"
        :wrapper-col="{ span: 17 }"
        @finish="handleSpaceDilatationSubmit"
        ref="formRef"
        layout="horizontal"
      >
        <a-form-item name="applyType">
          <template #label>
            <span style="display: inline-flex; align-items: center">
              扩容类型
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
                    1. 不同类型所需积分不同；<br />
                    2. 容量扩容：n积分/1GB；<br />
                    3. 数量扩容：n积分/1个。<br />
                    4. 人数扩容：n积分/1人，个人空间不可以扩容人数。<br />
                  </div>
                </template>
              </a-tooltip>
            </span>
          </template>
          <a-radio-group v-model:value="formDilatation.dilatationType" name="radioGroup">
            <a-radio
              @change="handleDilatationChange"
              v-for="dict in p_space_dilatation_type"
              :value="dict.dictValue"
              :key="dict.dictValue"
            >
              {{ dict.dictLabel }}
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item name="dilatationTotal">
          <template #label>
            <span style="display: inline-flex; align-items: center">
              扩容数量
              <a-tooltip title="根据不同的扩容类型，扩容单价有所不同，计算总数也不一样">
                <InfoCircleOutlined
                  style="
                    margin-left: 4px;
                    color: #999;
                    font-size: 14px;
                    position: relative;
                    top: 1px;
                  "
                />
              </a-tooltip>
            </span>
          </template>
          <a-input-number
            :min="1"
            style="width: 100%"
            @change="handleDilatationChange"
            v-model:value="formDilatation.dilatationTotal"
            placeholder="请输入扩容数量"
          />
        </a-form-item>
        <a-form-item name="pointsTotal" label="单价">
          <a-input-number
            :min="1"
            style="width: 100%"
            :disabled="true"
            v-model:value="formDilatation.dilatationUnit"
            placeholder="请输入所需积分"
          />
        </a-form-item>
        <a-form-item name="pointsTotal" label="所需积分">
          <a-input-number
            :min="0"
            style="width: 100%"
            :disabled="true"
            v-model:value="formDilatation.pointsTotal"
            placeholder="请输入所需积分"
          />
        </a-form-item>
        <div class="form-footer">
          <a-button @click="openDilatation = false">取消</a-button>
          <a-button type="primary" html-type="submit" :loading="dilatationLoading">提交</a-button>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, nextTick, onMounted, ref } from 'vue'
import DictTag from '@/components/DictTag.vue'
import { listUserPersonalSpaceInfo } from '@/api/picture/space'
import {
  PSpaceType,
  type SpaceInfoQuery,
  type UserPersonalSpaceInfoVo,
} from '@/types/picture/space.d.ts'
import { formatSize } from '@/utils/common.ts'
import {
  PSpaceDilatationTypeEnum,
  type SpaceDilatationInfoAdd,
} from '@/types/picture/pictureDilationInfo.d.ts'
import { InfoCircleOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue'
import { addSpaceDilationInfo, getSpaceDilationInfo } from '@/api/picture/pictureDilationInfo.ts'
import { usePasswordVerify } from '@/utils/auth.ts'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_space_status, p_space_type, p_space_oss_type, p_space_dilatation_type } = proxy?.useDict(
  'p_space_status',
  'p_space_type',
  'p_space_oss_type',
  'p_space_dilatation_type',
)
// region表格查询
const dateRange = ref<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)
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
  { title: '总容量', dataIndex: 'maxSize', width: 80 },
  { title: '已用容量', dataIndex: 'totalSize', width: 100, sorter: true },
  { title: '文件总数', dataIndex: 'maxCount', width: 100 },
  { title: '文件数', dataIndex: 'totalCount', width: 90, sorter: true },
  { title: 'OSS类型', dataIndex: 'ossType', width: 80 },
  { title: '创建时间', dataIndex: 'createTime', width: 140, sorter: true },
  { title: '更新时间', dataIndex: 'updateTime', width: 140 },
  { title: '最后上传时间', dataIndex: 'lastUpdateTime', width: 140, sorter: true },
  { title: '操作', dataIndex: 'action', width: 140 },
]

const getSpaceList = () => {
  loading.value = true
  queryParams.value.params = {}
  if (dateRange.value != null && Array.isArray(dateRange.value) && dateRange.value.length > 0) {
    queryParams.value.params['beginCreateTime'] = dateRange.value[0].format('YYYY-MM-DD').concat(' 00:00:00')
    queryParams.value.params['endCreateTime'] = dateRange.value[1].format('YYYY-MM-DD').concat(' 23:59:59')
  }
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
  getSpaceList()
}
//endregion
//region 扩容
const title = ref('空间扩容')
const formDilatation = ref<SpaceDilatationInfoAdd>({
  spaceId: '',
  dilatationType: '0',
  dilatationTotal: 1,
})
const openDilatation = ref(false)
const dilatationLoading = ref(false)
const { verify } = usePasswordVerify()
const handleSpaceDilatationSubmit = async () => {
  //如果是个人空间&&扩容人数，提示不可以
  if (
    spaceList.value.find((item) => item.spaceId === formDilatation.value.spaceId)?.spaceType ===
    PSpaceType.SPACE_TYPE_2 &&
    formDilatation.value.dilatationType === PSpaceDilatationTypeEnum.P_SPACE_DILATATION_TYPE_2
  ) {
    message.error('个人空间不允许扩容人数！！！')
    return
  }
  //校验密码
  const verified = await verify('空间扩容')
  if (!verified) return
  //密码校验通过
  dilatationLoading.value = true
  try {
    const res = await addSpaceDilationInfo({
      spaceId: formDilatation.value.spaceId,
      dilatationType: formDilatation.value.dilatationType,
      dilatationTotal: formDilatation.value.dilatationTotal,
    })
    if (res.code === 200) {
      message.success('扩容成功！！！')
      openDilatation.value = false
      getSpaceList()
    } else {
      message.error(res.msg || '未知错误！！！')
    }
  } finally {
    dilatationLoading.value = false
  }
}

const rules = {
  dilatationType: [{ required: true, message: '请输入申请类型', trigger: 'blur' }],
  dilatationTotal: [{ required: true, message: '请输入申请数量', trigger: 'blur' }],
}
const handleSpaceDilatation = (spaceId: string) => {
  formDilatation.value = {
    spaceId: '',
    dilatationType: '0',
    dilatationTotal: 1,
  }
  formDilatation.value.spaceId = spaceId
  handleDilatationChange()
  openDilatation.value = true
}
const handleDilatationChange = async () => {
  console.log('handleDilatationChange', formDilatation.value)
  await nextTick()
  console.log('handleDilatationChange', formDilatation.value)
  getSpaceDilationInfo({
    spaceId: formDilatation.value.spaceId,
    dilatationType: formDilatation.value.dilatationType,
    dilatationTotal: formDilatation.value.dilatationTotal,
  }).then((res) => {
    formDilatation.value.pointsTotal = res?.data?.pointsTotal
    formDilatation.value.dilatationUnit = res?.data?.dilatationUnit
  })
}
//endregion
onMounted(getSpaceList)
</script>
<style scoped lang="scss">
.space-personal-table {
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
