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
        <a-row
          style="margin-top: 10px"
          :gutter="20"
          v-if="checkUser(folder.userId) || checkSpaceEditor(folder.spaceId)"
        >
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
      <a-col
        :xs="24"
        :sm="9"
        :md="6"
        :lg="3"
        class="folder-item add-folder"
        @click="handleBatchUpload"
      >
        <ToTopOutlined class="icon" />
        <div class="text">批量上传图片</div>
      </a-col>
    </a-row>

    <PictureInfoList
      ref="pictureInfoListRef"
      style="margin-top: 20px"
      :space-id="spaceId"
      :current-parent-id="currentParentId"
    ></PictureInfoList>

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
    <!--批量添加图片-->
    <a-modal v-model:open="openBatchUpload" :footer="null" :width="800" centered destroyOnClose>
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          {{ title }}
          <a-tooltip title="批量上传可能会因为空间容量不够导致上传失败，请自行清理空间">
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
      >
        <a-form-item label="" name="">
          <PictureBatchUpload
            @upload-success="handleUploadSuccess"
            @upload-accomplish="handleUploadAccomplish"
            :hasUpload="hasUpload"
            :maxCount="100"
            :maxSize="50"
          ></PictureBatchUpload>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts" name="PictureSpaceFolder">
import { onMounted, reactive, ref } from 'vue'
import {
  DeleteTwoTone,
  EditTwoTone,
  FolderAddTwoTone,
  FolderTwoTone,
  QuestionCircleOutlined,
  ToTopOutlined,
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
import PictureInfoList from '@/components/PictureInfoList.vue'
import PictureBatchUpload from '@/components/PictureBatchUpload.vue'
import type { PictureFileResponse } from '@/types/file'
import { addPictureInfo } from '@/api/picture/picture.ts'
import { PPictureStatus } from '@/types/picture/picture.d.ts'
import { checkSpaceEditor, checkUser } from '@/utils/permission.ts'
import { spacePerm } from '@/stores/modules/space.ts'

interface Folder {
  folderId: string
  folderName: string
  parentId: string
}

onMounted(async () => {
  await spacePerm.loadSpacePerms()
})

const pictureInfoListRef = ref<InstanceType<typeof PictureInfoList>>()

// 获取当前路由信息
const route = useRoute()
const router = useRouter()
const spaceId = ref(route.query.spaceId as string)

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

const folderQuery = ref<SpaceFolderInfoQuery>({
  spaceId: spaceId.value,
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
  // console.log(currentParentId.value)
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
const openBatchUpload = ref(false)
const hasUpload = ref<boolean>(false)
const handleBatchUpload = () => {
  openBatchUpload.value = true
  title.value = '批量上传图片'
}

const handleUploadSuccess = async (data: PictureFileResponse) => {
  try {
    const formData = {
      pictureUrl: data.url,
      dnsUrl: data.dnsUrl,
      name: data.name,
      picSize: data.picSize,
      picWidth: data.picWidth,
      picHeight: data.picHeight,
      picScale: data.picScale,
      picFormat: data.picFormat,
      thumbnailUrl: data.thumbnailUrl,
      spaceId: spaceId.value,
      folderId: currentParentId.value,
      pointsNeed: 10,
      pictureStatus: PPictureStatus.PICTURE_STATUS_1,
    }
    // console.log(hasUpload.value)
    const res = await addPictureInfo(formData)
    if (res.code === 200) {
      message.success('图片(' + data.name + ')添加成功，请等待所有图片上传完成')
    } else {
      message.error(res.msg)
      hasUpload.value = true
    }
  } catch (e) {
    console.log(e)
    hasUpload.value = true
  }
}
const handleUploadAccomplish = () => {
  console.log('上传完成')
  getFolderList()
  pictureInfoListRef.value?.refreshData()
}
getFolderList()
</script>

<style scoped lang="scss">
.form-footer {
  text-align: right;
  padding: 16px 0 0;
  //margin-top: 24px;
  border-top: 1px solid #f0f0f0;

  .ant-btn {
    margin-left: 10px;
  }
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
    //box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
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
      //color: #333;
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
}
</style>
