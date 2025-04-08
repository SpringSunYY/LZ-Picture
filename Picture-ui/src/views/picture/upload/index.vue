<template>
  <div class="picture-update">
    <a-card title="图片上传" :bordered="false">
      <a-form
        :model="formState"
        @finish="handleSubmit"
        :label-col="{ span: 4 }"
        :rules="rules"
        :wrapper-col="{ span: 18 }"
      >
        <!-- 图片上传 -->
        <a-form-item label="图片文件" name="pictureUrl">
          <PictureUpload
            :modelValue="formState.pictureUrl"
            v-model:value="formState.pictureUrl"
            :allowedFormats="['image/jpeg', 'image/png']"
            :maxSizeMB="10"
            :maxCount="1"
            @upload-success="handleSuccess"
          />
        </a-form-item>

        <!-- 图片名称 -->
        <a-form-item label="图片名称" name="name">
          <a-input v-model:value="formState.name" />
        </a-form-item>
        <a-form-item label="图片空间">
          <a-select
            show-search
            v-model:value="formState.spaceId"
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
        <a-form-item label="图片文件夹">
          <a-cascader
            v-model:value="formState.folderId"
            :options="folderList"
            placeholder="请选择图片文件夹"
            change-on-select
            :fieldNames="{
              label: 'folderName',
              value: 'folderId',
              children: 'children',
            }"
            @change="handleFolderChange"
          />
        </a-form-item>
        <!-- 分类选择 -->
        <a-form-item label="图片分类" name="categoryId">
          <a-cascader
            v-model:value="formState.categoryId"
            :options="pictureCategoryList"
            expand-trigger="hover"
            placeholder="请选择图片分类"
            :fieldNames="{
              label: 'name',
              value: 'categoryId',
              children: 'children',
            }"
            @change="handleCategoryChange"
          />
        </a-form-item>
        <a-form-item label="标签">
          <a-select
            mode="tags"
            v-model:value="formState.tags"
            :options="tagList"
            placeholder="请输入图片标签"
            :filter-option="false"
            :fieldNames="{
              label: 'name',
              value: 'name',
            }"
            @search="handleSearchTag"
            @select="handleSelectTag"
            :not-found-content="tagLoading"
          />
        </a-form-item>

        <!-- 图片描述 -->
        <a-form-item label="图片简介" name="introduction">
          <a-textarea
            v-model:value="formState.introduction"
            :rows="4"
            placeholder="请输入图片简介"
          />
        </a-form-item>

        <!-- 所需积分 -->
        <a-form-item label="所需积分" name="pointsNeed">
          <a-input-number v-model:value="formState.pointsNeed" :min="0" :precision="0" />
        </a-form-item>

        <!-- 图片状态 -->
        <a-form-item label="图片状态" name="pictureStatus">
          <a-switch
            v-model:checked="formState.pictureStatus"
            checked-children="公开"
            un-checked-children="私有"
            :checked-value="0"
            :unchecked-value="1"
          />
        </a-form-item>

        <!-- 提交按钮 -->
        <a-form-item :wrapper-col="{ offset: 4 }">
          <a-button type="primary" html-type="submit" :loading="submitting"> 提交</a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts" name="PictureUpdate">
import { reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import PictureUpload from '@/components/PictureUpload/index.vue'
import type {
  PictureCategoryInfoQuery,
  PictureCategoryInfoVo,
} from '@/types/picture/pictureCategory'
import { listPictureCategoryInfo } from '@/api/picture/pictureCategory.ts'
import { handleTree } from '@/utils/lz.ts'
import type { Space, SpaceQuery } from '@/types/picture/space'
import { mySpaceInfo } from '@/api/picture/space.ts'
import { debounce } from 'lodash-es'
import type { SpaceFolderInfoQuery, SpaceFolderInfoVo } from '@/types/picture/spaceFolder'
import { listSpaceFolderInfo } from '@/api/picture/spaceFolder.ts'
import type { PictureTagInfoQuery, PictureTagInfoVo } from '@/types/picture/pictureTag'
import { listPictureTagInfo } from '@/api/picture/pictureTag.ts'
import type { PictureInfo } from '@/types/picture/picture'
import { addPictureInfo } from '@/api/picture/picture.ts'
//空间
const spaceList = ref<Space[]>([])
const spaceQuery = ref<SpaceQuery>({})
const spaceLoading = ref(false)
//分类
const pictureCategoryList = ref<PictureCategoryInfoVo[]>([])
const pictureCategoryQuery = ref<PictureCategoryInfoQuery>({})
//文件夹
const folderQuery = ref<SpaceFolderInfoQuery>({
  spaceId: '',
})
const folderList = ref<SpaceFolderInfoVo[]>([])
//标签
const tagList = ref<PictureTagInfoVo[]>([])
const tagQuery = ref<PictureTagInfoQuery>({})
const tagLoading = ref(false)

const rules = {}
const fileList = ref([])
const submitting = ref(false)
const formState = reactive<PictureInfo>({
  pictureUrl: '',
  name: '',
  introduction: '',
  categoryId: '',
  spaceId: '',
  folderId: '',
  tags: [],
  picSize: 0,
  picHeight: 0,
  picWidth: 0,
  pointsNeed: 0,
  pictureStatus: '0',
  picFormat: '',
  picColor: '',
  picScale: 0,
})
const handleSuccess = (modelValue) => {
  console.log('modelValue', modelValue)
  // 提交到后端或处理数据
  formState.name = modelValue.name
  formState.thumbnailUrl = modelValue.thumbnailUrl
  formState.pictureUrl = modelValue.pictureUrl
  formState.picWidth = modelValue.meta.width
  formState.picHeight = modelValue.meta.height
  formState.picScale = modelValue.meta.ratio
  formState.picFormat = modelValue.meta.format
  formState.picSize = modelValue.meta.size
}

// 提交处理
const handleSubmit = async () => {
  submitting.value = true
  addPictureInfo(formState).then((res) => {
    if (res.code === 200) {
      message.success('添加成功')
    }
  })
  submitting.value = false
}

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
const handleCategoryChange = (value: string[]) => {
  formState.categoryId = value[value.length - 1]
}

const handleSelectSpace = () => {
  formState.folderId = ''
  folderQuery.value.spaceId = formState.spaceId
  getFolderList()
}
const handleSearchSpace = debounce((value: string) => {
  spaceQuery.value.spaceName = value
  getMySpaceList()
}, 300)
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
const handleFolderChange = (value: string[]) => {
  formState.folderId = value[value.length - 1]
}
const handleSearchTag = debounce((value: string) => {
  tagQuery.value.name = value
  getTagList()
}, 300)
const handleSelectTag = (value: string) => {
  if (formState?.tags?.length > 5) {
    message.error('最多只能选择5个标签')
    formState.tags = formState.tags.slice(0, 5)
    return
  }
}
const getTagList = () => {
  tagLoading.value = true
  listPictureTagInfo(tagQuery.value).then((res) => {
    tagList.value = res?.rows || []
    tagLoading.value = false
  })
}
getTagList()
getMySpaceList()
getPictureCategoryList()
</script>

<style lang="scss" scoped>
.picture-update {
  width: 60%;
  margin: 0 auto;

  .ant-upload-select-picture-card i {
    font-size: 32px;
    color: #616161;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }
}
</style>
