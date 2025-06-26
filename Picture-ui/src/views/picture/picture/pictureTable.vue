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
        <!-- 分类选择 -->
        <a-form-item label="图片分类" name="categoryId" style="width: 250px">
          <a-cascader
            v-model:value="queryParams.categoryId"
            :options="pictureCategoryList"
            @change="handleSearch"
            expand-trigger="hover"
            placeholder="请选择图片分类"
            :fieldNames="{
              label: 'name',
              value: 'categoryId',
              children: 'children',
            }"
          />
        </a-form-item>
        <a-form-item label="图片空间" name="spaceId" style="width: 250px">
          <a-select
            show-search
            v-model:value="queryParams.spaceId"
            :options="spaceList"
            :filter-option="false"
            :fieldNames="{
              label: 'spaceName',
              value: 'spaceId',
            }"
            @search="handleSearchSpace"
            @select="handleSelectSpace"
            placeholder="请选择图片空间"
            :not-found-content="spaceLoading"
          />
        </a-form-item>
        <a-form-item label="图片文件夹" style="width: 250px">
          <a-cascader
            v-model:value="queryParams.folderId"
            :options="folderList"
            @change="handleSearch"
            placeholder="请选择图片文件夹"
            change-on-select
            :fieldNames="{
              label: 'folderName',
              value: 'folderId',
              children: 'children',
            }"
          />
        </a-form-item>
        <a-button type="primary" @click="resetSearch">重置</a-button>
      </a-form>

      <div style="margin-bottom: 20px"></div>
      <a-space style="margin-bottom: 16px">
        <a-button danger :disabled="!selectedRowKeys.length" @click="handleBatchDelete">
          批量删除
        </a-button>
      </a-space>
      <a-table
        :columns="columns"
        :data-source="pictureList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="pictureId"
        size="middle"
        :row-selection="rowSelection"
        :scroll="{ x: 'max-content' }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'name'">
            <div class="editable-cell">
              <template v-if="editingId === record.pictureId">
                <a-input v-model:value="editingName" size="small" style="width: 80px" />
                <check-outlined
                  class="editable-cell-icon confirm"
                  @click="save(record)"
                  title="保存"
                />
                <close-outlined
                  class="editable-cell-icon cancel"
                  @click="cancelEdit"
                  title="取消"
                />
              </template>
              <template v-else>
                <span @click="edit(record)" style="cursor: pointer">
                  {{ record.name || '未命名' }}
                  <edit-outlined class="editable-cell-icon" />
                </span>
              </template>
            </div>
          </template>

          <!-- 缩略图 -->
          <template v-if="column.dataIndex === 'thumbnailUrl'">
            <a-image
              :alt="record.name"
              :src="record.thumbnailUrl"
              width="60"
              height="60"
              :preview="true"
            />
          </template>

          <!-- 状态标签 -->
          <template v-if="column.dataIndex === 'pictureStatus'">
            <dict-tag :value="record.pictureStatus" :options="p_picture_status" />
          </template>

          <!-- 标签列表 -->
          <template v-if="column.dataIndex === 'tags'">
            <a-space wrap>
              <a-tag v-for="tag in record.tags" :key="tag" color="blue">{{ tag }}</a-tag>
            </a-space>
          </template>

          <!-- 文件大小 -->
          <template v-if="column.dataIndex === 'picSize'">
            {{ formatSize(record.picSize) }}
          </template>

          <!-- 图片尺寸 -->
          <template v-if="column.dataIndex === 'picDimensions'">
            {{ record.picWidth }} × {{ record.picHeight }}
          </template>

          <!-- 操作列 -->
          <template v-if="column.dataIndex === 'action'">
            <a-space>
              <a @click="handleOpenApply(record.pictureId)">公开</a>
              <a @click="handleUpdate(record.pictureId)" v-if="checkPermiSingle('picture:upload')"
                >修改</a
              >
              <a @click="viewDetail(record)" v-if="checkPermiSingle('picture:upload:detail')"
                >查看</a
              >
              <a-popconfirm
                title="确定要删除这条记录吗?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record)"
              >
                <a class="text-red-500">删除</a>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal v-model:open="openApply" destroy-on-close :footer="null">
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          {{ title }}
          <a-tooltip
            title="您可以申请您的图片公开权限，请填写申请理由、您的联系方式，如果您有版权可以设置图片的积分或者金额，原创作品可以设置金额。"
          >
            <question-circle-outlined class="title-tip-icon" />
          </a-tooltip>
        </div>
      </template>
      <a-form
        :model="formApply"
        :label-col="{ span: 5 }"
        :rules="rules"
        :wrapper-col="{ span: 17 }"
        @finish="handleApplySubmit"
        ref="formRef"
        layout="horizontal"
      >
        <a-form-item name="applyType">
          <template #label>
            <span style="display: inline-flex; align-items: center">
              申请类型
              <a-tooltip
                title="注意：原创类型仅支持原创图片，请勿上传非原创图片，原创类型可以设置金额，如果不设置则表示免费，如果版权虚假，造成的版权纠纷均由用户承担。
                            转载资源、无版权资源可以设置积分，不可设置金额，但请注意版权，如用户版权不归您，造成版权纠纷均由用户承担，平台图片仅仅作为展示。"
              >
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
          <a-radio-group v-model:value="formApply.applyType" name="radioGroup">
            <a-radio
              v-for="dict in p_picture_apply_type"
              :value="dict.dictValue"
              :key="dict.dictValue"
            >
              {{ dict.dictLabel }}
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="申请理由" name="applyReason">
          <a-textarea
            v-model:value="formApply.applyReason"
            placeholder="请输入申请理由"
            :rows="3"
          />
        </a-form-item>

        <a-form-item label="证明图片" name="applyImage">
          <CoverUpload
            v-model:value="formApply.applyImage"
            :maxCount="9"
            type="4"
            fileDir="apply"
          />
        </a-form-item>

        <a-form-item label="证明文件" name="applyFile">
          <FileUpload v-model:value="formApply.applyFile" placeholder="请输入文件链接或上传路径" />
        </a-form-item>

        <a-form-item name="contact">
          <template #label>
            <span style="display: inline-flex; align-items: center">
              联系方式
              <a-tooltip
                title="请输入您的联系方式，手机号码、微信（推荐）等信息，例：微信：123456789，便于我们联系您处理举报信息。"
              >
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
          <a-input v-model:value="formApply.contact" placeholder="请输入联系方式" />
        </a-form-item>

        <a-form-item
          label="所需积分"
          name="pointsNeed"
          v-if="formApply.applyType !== '' && formApply.applyType !== '0'"
        >
          <a-input-number
            v-model:value="formApply.pointsNeed"
            :min="0"
            :step="10"
            style="width: 100%"
            placeholder="请输入所需积分"
          />
        </a-form-item>

        <a-form-item label="所需金额" name="priceNeed" v-if="formApply.applyType === '0'">
          <a-input-number
            v-model:value="formApply.priceNeed"
            :min="0"
            :precision="2"
            style="width: 100%"
            placeholder="请输入所需金额"
          />
        </a-form-item>
        <div class="form-footer">
          <a-button @click="openApply = false">取消</a-button>
          <a-button type="primary" html-type="submit" :loading="applyLoading">提交</a-button>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { computed, getCurrentInstance, onMounted, ref } from 'vue'
import { deletePictureByPictureIds, listMyTable, updatePictureName } from '@/api/picture/picture'
import DictTag from '@/components/DictTag.vue'
import { formatSize } from '@/utils/common.ts'
import {
  CheckOutlined,
  CloseOutlined,
  EditOutlined,
  InfoCircleOutlined,
  QuestionCircleOutlined,
} from '@ant-design/icons-vue'
import { message, Modal } from 'ant-design-vue'
import type { PictureInfoQuery } from '@/types/picture/picture'
import { listPictureCategoryInfo } from '@/api/picture/pictureCategory.ts'
import { handleTree } from '@/utils/lz.ts'
import type {
  PictureCategoryInfoQuery,
  PictureCategoryInfoVo,
} from '@/types/picture/pictureCategory'
import { mySpaceInfo } from '@/api/picture/space.ts'
import { listSpaceFolderInfo } from '@/api/picture/spaceFolder.ts'
import type { Space, SpaceQuery } from '@/types/picture/space'
import type { SpaceFolderInfoQuery, SpaceFolderInfoVo } from '@/types/picture/spaceFolder'
import { debounce } from 'lodash-es'
import { useRouter } from 'vue-router'
import { checkPermiSingle } from '@/utils/permission.ts'
import type { PictureApplyInfoAdd } from '@/types/picture/pictureApplyInfo.d.ts'
import CoverUpload from '@/components/CoverUpload.vue'
import FileUpload from '@/components/FileUpload.vue'
import { addPictureApplyInfo } from '@/api/picture/pictureApplyInfo.ts'

const { proxy } = getCurrentInstance()!
const { p_picture_status, p_picture_apply_type } = proxy?.useDict(
  'p_picture_status',
  'p_picture_apply_type',
)

const pictureList = ref<any[]>([])
const loading = ref(false)

//region 图片公开申请
const title = ref('图片公开申请')
const formApply = ref<PictureApplyInfoAdd>({
  pictureId: '',
  applyType: '',
  applyReason: '',
  applyImage: '',
  applyFile: '',
  contact: '',
  pointsNeed: 0,
  priceNeed: 0,
})
const openApply = ref(false)
const applyLoading = ref(false)
const handleApplySubmit = () => {
  applyLoading.value = true
  addPictureApplyInfo(formApply.value).then((res) => {
    if (res.code === 200) {
      message.success('申请成功')
      openApply.value = false
    } else {
      message.error(res.msg)
    }
    applyLoading.value = false
  })
}

const handleOpenApply = (pictureId: string) => {
  formApply.value = {
    pictureId: '',
    applyType: '',
    applyReason: '',
    applyImage: '',
    applyFile: '',
    contact: '',
    pointsNeed: 0,
    priceNeed: 0,
  }
  formApply.value.pictureId = pictureId
  openApply.value = true
}
const rules = {
  applyId: [{ required: true, message: '请输入申请编号', trigger: 'blur' }],
  applyType: [{ required: true, message: '请输入申请类型', trigger: 'blur' }],
  applyReason: [{ required: true, message: '请输入申请理由', trigger: 'blur' }],
  contact: [{ required: true, message: '请输入联系方式', trigger: 'blur' }],
  pointsNeed: [{ required: true, message: '请输入所需积分', trigger: 'blur' }],
}
//endregion

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showTotal: (total: number) => `共 ${total} 条记录`,
  showSizeChanger: true,
  showQuickJumper: true,
  pageSizeOptions: ['10', '20', '30', '50'],
})

const queryParams = ref(<PictureInfoQuery>{
  pageNum: 1,
  pageSize: 10,
  isAsc: 'desc',
  name: '',
  categoryId: '',
})

const columns = [
  { title: '缩略图', dataIndex: 'thumbnailUrl', width: 80 },
  { title: '图片名称', dataIndex: 'name', ellipsis: true },
  { title: '分类', dataIndex: 'categoryName', width: 100 },
  { title: '分类', dataIndex: 'spaceName', width: 100 },
  { title: '体积', dataIndex: 'picSize', width: 100, sorter: true },
  { title: '尺寸', dataIndex: 'picDimensions', width: 120 },
  { title: '比例', dataIndex: 'picScale', width: 80 },
  { title: '格式', dataIndex: 'picFormat', width: 80 },
  { title: '状态', dataIndex: 'pictureStatus', width: 100 },
  { title: '标签', dataIndex: 'tags' },
  { title: '创建时间', dataIndex: 'createTime', sorter: true, width: 180 },
  { title: '操作', dataIndex: 'action', width: 180 },
]

// 多选相关
const selectedRowKeys = ref<string[]>([])
const selectedRows = ref<any[]>([])

const rowSelection = computed(() => ({
  selectedRowKeys: selectedRowKeys.value,
  onChange: (keys: string[], rows: any[]) => {
    selectedRowKeys.value = keys
    selectedRows.value = rows
  },
}))

const handleBatchDelete = () => {
  if (!selectedRowKeys.value.length) return

  Modal.confirm({
    title: '确定要删除吗？',
    content: '删除后数据将无法恢复，请谨慎操作！',
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      // 执行删除逻辑
      deletePictureByPictureIds(selectedRowKeys.value).then(() => {
        message.success('删除成功')
        selectedRows.value = []
        selectedRowKeys.value = []
        getList()
      })
    },
    onCancel: () => {
      message.info('已取消删除')
    },
  })
}

const handleDelete = (record: any) => {
  deletePictureByPictureIds([record.pictureId]).then(() => {
    message.success('删除成功')
    getList()
  })
}

const getList = () => {
  loading.value = true
  let categoryId = ''
  if (queryParams.value.categoryId && Array.isArray(queryParams.value.categoryId)) {
    categoryId = queryParams.value.categoryId[queryParams.value.categoryId.length - 1]
  }
  let folderId = ''
  if (queryParams.value.folderId && Array.isArray(queryParams.value.folderId)) {
    folderId = queryParams.value.folderId[queryParams.value.folderId.length - 1]
  }
  listMyTable({
    name: queryParams.value.name,
    categoryId: categoryId,
    spaceId: queryParams.value.spaceId,
    folderId: folderId,
    pageNum: queryParams.value.pageNum,
    pageSize: queryParams.value.pageSize,
    orderByColumn: queryParams.value.orderByColumn,
    isAsc: queryParams.value.isAsc,
  }).then((res) => {
    pictureList.value = (res?.rows || []).map((item) => ({
      ...item,
      editing: false,
    }))
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
    categoryId: '',
  }
  pagination.value.current = 1
  getList()
}

const handleTableChange = (pag, _, sorter) => {
  console.log(pag, _, sorter)
  pagination.value.current = pag.current
  pagination.value.pageSize = pag.pageSize
  queryParams.value.pageNum = pag.current
  queryParams.value.pageSize = pag.pageSize
  queryParams.value.orderByColumn = sorter.field
  queryParams.value.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc'
  getList()
}

const editingId = ref<string | null>(null)
const editingName = ref<string>('')

const edit = (record: any) => {
  editingId.value = record.pictureId
  editingName.value = record.name
}

const cancelEdit = () => {
  editingId.value = null
  editingName.value = ''
}

const save = (record: any) => {
  if (!editingName.value.trim()) {
    return message.warning('图片名称不能为空')
  }
  if (editingName.value.length > 32) {
    return message.warning('图片名称不能超过32个字符')
  }

  updatePictureName({ pictureId: record.pictureId, name: editingName.value }).then(() => {
    message.success('修改成功')
    editingId.value = null
    getList()
  })
}

//分类
const pictureCategoryList = ref<PictureCategoryInfoVo[]>([])
const pictureCategoryQuery = ref<PictureCategoryInfoQuery>({})
const getPictureCategoryList = async () => {
  listPictureCategoryInfo(pictureCategoryQuery.value).then((res) => {
    pictureCategoryList.value = handleTree(
      JSON.parse(JSON.stringify(res?.rows || [])),
      'categoryId',
      'parentId',
      'children',
    )
    // console.log('pictureCategoryList', pictureCategoryList.value)
  })
}

//空间
const spaceList = ref<Space[]>([])
const spaceQuery = ref<SpaceQuery>({})
const spaceLoading = ref(false)

//文件夹
const folderQuery = ref<SpaceFolderInfoQuery>({
  spaceId: '',
})
const folderList = ref<SpaceFolderInfoVo[]>([])
const getMySpaceList = () => {
  spaceLoading.value = true
  // 获取我的空间列表
  mySpaceInfo(spaceQuery.value).then((res) => {
    if (res.code === 200) {
      spaceList.value = res?.rows || []
    } else {
      message.error('获取空间列表失败')
    }
    spaceLoading.value = false
  })
}
const handleSearchSpace = debounce((value: string) => {
  spaceQuery.value.spaceName = value
  getMySpaceList()
}, 300)
const getFolderList = () => {
  // 获取文件夹列表
  listSpaceFolderInfo(folderQuery.value).then((res) => {
    folderList.value = handleTree(
      JSON.parse(JSON.stringify(res?.rows || [])),
      'folderId',
      'parentId',
      'children',
    )
  })
}
const handleSelectSpace = () => {
  queryParams.value.folderId = ''
  folderQuery.value.spaceId = queryParams.value.spaceId
  handleSearch()
  getFolderList()
}
// 路由跳转
const router = useRouter()
// 查看详情
const viewDetail = (record) => {
  const routeData = router.resolve({
    path: '/pictureDetail',
    query: { pictureId: record.pictureId },
  })
  window.open(routeData.href, '_blank')
}

const handleUpdate = (id: string) => {
  const routeData = router.resolve({
    path: '/picture/pictureEdit',
    query: { pictureId: id },
  })
  window.open(routeData.href, '_blank')
}

getMySpaceList()
getPictureCategoryList()
onMounted(getList)
</script>

<style scoped lang="scss">
.picture-table {
  margin: 0 2em;
}

.editable-cell-icon {
  margin-left: 8px;
  color: #1890ff;
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
