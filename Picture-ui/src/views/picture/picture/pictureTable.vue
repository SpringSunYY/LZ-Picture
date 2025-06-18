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
          <template v-if="column.dataIndex === 'name'">
            <div class="editable-cell">
              <template v-if="editingId === record.pictureId">
                <a-input v-model:value="editingName" size="small" style="width: 160px" />
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
        </template>
      </a-table>
    </a-card>
  </div>
</template>
<script setup lang="ts">
import { getCurrentInstance, onMounted, ref } from 'vue'
import { listMyTable, updatePictureName } from '@/api/picture/picture'
import DictTag from '@/components/DictTag.vue'
import { formatSize } from '@/utils/common.ts'
import { CheckOutlined, CloseOutlined, EditOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
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

const { proxy } = getCurrentInstance()!
const { p_picture_status } = proxy?.useDict('p_picture_status')

const pictureList = ref<any[]>([])
const loading = ref(false)

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showTotal: (total: number) => `共 ${total} 条记录`,
  showSizeChanger: true,
  showQuickJumper: true,
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
  { title: '简介', dataIndex: 'introduction', ellipsis: true },
  { title: '分类', dataIndex: 'categoryName', width: 100 },
  { title: '分类', dataIndex: 'spaceName', width: 100 },
  { title: '体积', dataIndex: 'picSize', width: 100, sorter: true },
  { title: '尺寸', dataIndex: 'picDimensions', width: 120 },
  { title: '比例', dataIndex: 'picScale', width: 80 },
  { title: '格式', dataIndex: 'picFormat', width: 80 },
  { title: '状态', dataIndex: 'pictureStatus', width: 100 },
  { title: '标签', dataIndex: 'tags' },
  { title: '创建时间', dataIndex: 'createTime', sorter: true, width: 180 },
]

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
getMySpaceList()
getPictureCategoryList()
onMounted(getList)
</script>

<style scoped lang="scss">
.picture-table {
  margin: 0 4em;
}

.editable-cell-icon {
  margin-left: 8px;
  color: #1890ff;
}
</style>
