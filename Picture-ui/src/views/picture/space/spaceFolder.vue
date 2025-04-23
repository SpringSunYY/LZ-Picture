<template>
  <div class="space-folder">
    <a-page-header class="folder-header" :title="'空间文件夹管理'" @back="goBack">
      <template #breadcrumb>
        <a-breadcrumb>
          <a-breadcrumb-item @click="goToLevel(0)" style="cursor: pointer">
            根目录
          </a-breadcrumb-item>
          <a-breadcrumb-item
            v-for="(item, index) in folderPathStack"
            :key="item.folderId"
            @click="goToLevel(index + 1)"
            style="cursor: pointer"
          >
            {{ item.folderName }}
          </a-breadcrumb-item>
        </a-breadcrumb>
      </template>
    </a-page-header>

    <!-- 文件夹展示 -->
    <a-row :gutter="[24, 24]" class="folder-container">
      <a-col
        :xs="24"
        :sm="9"
        :md="6"
        :lg="3"
        v-for="folder in folderList"
        :key="folder.folderId"
        class="folder-item"
      >
        <FolderTwoTone class="icon" @click="enterFolder(folder)" />
        <div class="text" @click="enterFolder(folder)">{{ folder.folderName }}</div>
        <a-row style="margin-top: 10px" :gutter="20">
          <a-col :span="12">
            <a-tooltip title="删除文件夹">
              <DeleteTwoTone class="folder-footer" @click="handleDelete(folder.folderId)" />
            </a-tooltip>
          </a-col>
          <a-col :span="12">
            <a-tooltip title="修改">
              <EditTwoTone class="folder-footer" @click="handleUpdate(folder.folderId)" />
            </a-tooltip>
          </a-col>
        </a-row>
      </a-col>

      <a-col :xs="24" :sm="9" :md="6" :lg="3" class="folder-item add-folder" @click="handleAdd">
        <FolderAddTwoTone class="icon" />
        <div class="text">添加文件夹</div>
      </a-col>
    </a-row>

    <!-- 图片网格列表 -->
    <a-row :gutter="[24, 24]">
      <a-col
        v-for="picture in pictureList"
        :key="picture.pictureId"
        :xs="24"
        :sm="6"
        :md="6"
        :lg="6"
      >
        <div class="picture-card">
          <div class="cover-image" :style="coverStyle(picture?.thumbnailUrl)"></div>
          <div class="picture-info">
            <h3 class="title">{{ picture.name }}</h3>
            <div class="meta">
              <a-button
                v-if="picture.userId === userId && checkPermiSingle('picture:space:update')"
                style="float: right"
                type="primary"
                size="small"
              >
                修改
              </a-button>
            </div>
          </div>
        </div>
      </a-col>
    </a-row>

    <div style="text-align: center; margin-top: 20px; margin-bottom: 10px">
      <a-pagination
        v-model:current="current"
        :pageSize="pictureQuery.pageSize"
        show-quick-jumper
        :total="pictureTotal"
        :showTotal="(total) => `共 ${total} 条`"
        :showSizeChanger="true"
        @change="onChange"
        @showSizeChange="onShowSizeChange"
        :pageSizeOptions="['12', '24', '48', '96']"
      />
    </div>

    <!--添加空间-->
    <a-modal v-model:open="open" :footer="null" centered destroyOnClose>
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          {{ title }}
          <a-tooltip title="文件夹的层级最多7层">
            <question-circle-outlined class="title-tip-icon" />
          </a-tooltip>
        </div>
      </template>
      <a-form
        :model="formState"
        :rules="rules"
        @finish="handleSubmit"
        ref="formRef"
        labelAlign="left"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="文件夹名" name="folderName">
          <a-input
            v-model:value="formState.folderName"
            showCount
            :maxlength="32"
            placeholder="请输入文件夹名称"
          />
        </a-form-item>
        <a-form-item label="排序" name="sortOrder">
          <a-input-number
            style="width: 100%"
            :min="0"
            :max="10"
            v-model:value="formState.sortOrder"
            placeholder="请输入排序，升序排序哦"
          />
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-textarea
            v-model:value="formState.remark"
            placeholder="请输入备注"
            :rows="3"
            showCount
            :maxlength="512"
          />
        </a-form-item>
        <div class="form-footer">
          <a-button @click="open = false">取消</a-button>
          <a-button type="primary" html-type="submit" :loading="submitting"> 提交</a-button>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts" name="PictureSpaceFolder">
import { reactive, ref } from 'vue'
import {
  DeleteTwoTone,
  EditTwoTone,
  FolderAddTwoTone,
  FolderTwoTone,
  QuestionCircleOutlined,
} from '@ant-design/icons-vue'
import type {
  SpaceFolderInfo,
  SpaceFolderInfoQuery,
  SpaceFolderInfoVo,
} from '@/types/picture/spaceFolder'
import {
  addSpaceFolderInfo,
  deleteSpaceFolderInfo,
  getSpaceFolderInfo,
  listSpaceFolderInfo,
  updateSpaceFolderInfo,
} from '@/api/picture/spaceFolder.ts'
import { message, Modal } from 'ant-design-vue'
import { useRoute, useRouter } from 'vue-router'
import { checkPermiSingle } from '@/utils/permission.ts'
import { storeToRefs } from 'pinia'
import type { PictureInfoQuery, PictureInfoVo } from '@/types/picture/picture'
import useUserStore from '@/stores/modules/user.ts'
import { listPictureInfo } from '@/api/picture/picture.ts'

interface Folder {
  folderId: string
  folderName: string
  parentId: string
}

const userStore = useUserStore()
const { userId: userId } = storeToRefs(userStore)
const pictureList = ref<PictureInfoVo[]>([]) // 图片
const pictureQuery = ref<PictureInfoQuery>({
  pageNum: 1,
  pageSize: 12,
})
const current = ref(1)
const pictureTotal = ref(1)
// 当前所在的父文件夹 ID
const currentParentId = ref('0')
const open = ref(false)
const submitting = ref(false)
const title = ref('')
// 表单数据结构
const formState = reactive<SpaceFolderInfo>({
  folderId: '',
  spaceId: '',
  folderName: '',
  sortOrder: 0,
  remark: '',
  parentId: currentParentId.value,
})
// 获取当前路由信息
const route = useRoute()
const router = useRouter()
const folderQuery = ref<SpaceFolderInfoQuery>({
  spaceId: route.query.spaceId as string,
  parentId: currentParentId.value,
})
const folderList = ref<SpaceFolderInfoVo[]>([])
// 验证规则
const rules = {
  folderName: [
    { required: true, message: '名称不能为空' },
    { min: 1, max: 32, message: '长度需在2-32字符之间' },
  ],
  remark: [{ max: 512, message: '长度不能超过512字符' }],
  sortOrder: [{ required: true, message: '排序不能为空' }],
  parentId: [{ required: true, message: '父文件夹不能为空' }],
}

// 路径栈
const folderPathStack = reactive<Folder[]>([])

// 进入子文件夹
function enterFolder(folder: Folder) {
  folderPathStack.push(folder)
  currentParentId.value = folder.folderId
  console.log(currentParentId.value)
  folderQuery.value.parentId = folder.folderId
  getFolderList()
}

// 返回某一级（点击面包屑）
function goToLevel(index: number) {
  if (index === 0) {
    folderPathStack.splice(0)
    currentParentId.value = '0'
  } else {
    const target = folderPathStack[index - 1]
    folderPathStack.splice(index)
    currentParentId.value = target.folderId
  }
  getFolderList()
}

//删除文件夹
const handleDelete = (folderId: string) => {
  Modal.confirm({
    title: '确定删除该文件夹吗？',
    okText: '确定',
    cancelText: '取消',
    onOk() {
      deleteSpaceFolderInfo(folderId).then((res) => {
        if (res?.code === 200) {
          message.success('删除成功')
          getFolderList()
        }
      })
    },
  })
}
const handleUpdate = (folderId: string) => {
  resetForm()
  getSpaceFolderInfo(folderId).then((res) => {
    Object.assign(formState, res?.data)
    title.value = '修改文件夹'
    open.value = true
  })
}

// 添加文件夹（模拟）
function handleAdd() {
  resetForm()
  title.value = '创建文件夹'
  open.value = true
}

const handleSubmit = () => {
  if (formState.folderId !== '') {
    updateSpaceFolderInfo(formState).then((res) => {
      if (res?.code === 200) {
        message.success('修改成功')
        open.value = false
        getFolderList()
      }
    })
  } else {
    addSpaceFolderInfo(formState).then((res) => {
      if (res.code === 200) {
        message.success('添加成功')
        open.value = false
        getFolderList()
      }
    })
  }
}
const resetForm = () => {
  Object.assign(formState, {
    folderId: '',
    spaceId: route.query.spaceId as string,
    folderName: '',
    sortOrder: 0,
    remark: '',
    parentId: currentParentId.value,
  })
}

// 返回图库
function goBack() {
  // folderPathStack.pop()
  // currentParentId.value = folderPathStack[folderPathStack.length - 1]?.folderId || '0'
  // getFolderList()
  router.push({ path: '/picture/space' })
}

//获取文件夹
const getFolderList = () => {
  folderQuery.value.parentId = currentParentId.value
  // 获取文件夹列表
  listSpaceFolderInfo(folderQuery.value).then((res) => {
    folderList.value = res?.rows || []
  })
}

// 分页器每页条数变化回调
const onShowSizeChange = (currentPage: number, size: number) => {
  pictureQuery.value.pageSize = size
  pictureQuery.value.pageNum = 1 // 切换条数后重置到第一页
  current.value = 1
  getSpaceInfoList()
}

// 修改现有 onChange 方法保持页码同步
const onChange = (pageNumber: number) => {
  current.value = pageNumber
  pictureQuery.value.pageNum = pageNumber
  getSpaceInfoList()
}

const getSpaceInfoList = () => {
  pictureQuery.value.folderId = currentParentId.value
  pictureQuery.value.spaceId = route.query.spaceId as string
  listPictureInfo(pictureQuery.value).then((res) => {
    pictureList.value = res?.rows || []
    pictureTotal.value = res?.total || 0
    // console.log('pictureList', pictureList.value)
    // console.log('pictureTotal', pictureTotal.value)
  })
}

// 封面样式处理
const coverStyle = (url?: string) => ({
  backgroundImage: `url(${url || '/default-space-cover.jpg'})`,
})
getSpaceInfoList()
getFolderList()
</script>

<style scoped lang="scss">
.space-folder {
  max-width: 1440px;
  margin: 0 auto;

  .folder-header {
    background-color: #fff;
  }

  .folder-container {
    display: flex;
    flex-wrap: wrap;
    gap: 24px;
    padding: 10px;
    background-color: #fff;
    border-radius: 6px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  }

  .folder-item {
    width: 130px;
    border-radius: 8px;
    background-color: #f5f5f5;
    text-align: center;
    cursor: pointer;
    transition: background-color 0.3s;
    padding: 16px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    &:hover {
      background-color: #e6f7ff;
    }

    .icon {
      font-size: 50px;
      color: #1890ff;
    }

    .text {
      margin-top: 12px;
      font-size: 14px;
      color: #333;
      word-break: break-all;
    }

    .folder-footer {
      font-size: 18px;
      word-break: break-all;
    }
  }

  .add-folder {
    background-color: #fafafa;
    border: 2px dashed #d9d9d9;

    &:hover {
      border-color: #1890ff;
      background-color: #f0faff;
    }

    .icon {
      color: #52c41a;
    }
  }

  .picture-card {
    position: relative;
    border-radius: 8px;
    overflow: hidden;
    transition: all 0.3s;
    cursor: pointer;
    background: #fff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    .cover-image {
      height: 160px;
      background-size: cover;
      background-position: center;
    }

    .picture-info {
      padding: 16px;

      .title {
        margin: 0;
        font-size: 16px;
      }

      .meta {
        color: rgba(0, 0, 0, 0.45);
        font-size: 12px;
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
}
</style>
