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
          />
        </a-form-item>
        <a-form-item label="标签">
          <a-select
            v-model:value="formState.tags"
            mode="tags"
            placeholder="请输入图片标签"
            :options="[]"
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
import { type CascaderProps, message } from 'ant-design-vue'
import PictureUpload from '@/components/PictureUpload/index.vue'
import type { PictureCategoryInfoQuery, PictureCategoryInfoVo } from '@/types/picture/spaceCategory'
import { listPictureCategoryInfo } from '@/api/picture/spaceCategory.ts'
import { handleTree } from '@/utils/lz.ts'
import type { Space, SpaceQuery } from '@/types/picture/space'
import { mySpaceInfo } from '@/api/picture/space.ts'
import { debounce } from 'lodash-es'
import type { SpaceFolderInfoQuery, SpaceFolderInfoVo } from '@/types/picture/spaceFolder'
import { listSpaceFolderInfo } from '@/api/picture/spaceFolder.ts'
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

const rules = {}
const fileList = ref([])
const submitting = ref(false)
const formState = reactive({
  name: '',
  introduction: '',
  pictureUrl: '',
  categoryId: '',
  pointsNeed: 10,
  pictureStatus: 0,
})
const handleSuccess = (modelValue) => {
  // console.log('modelValue', modelValue)
  // 提交到后端或处理数据
  formState.name = modelValue.name
  formState.picWidth = modelValue.width
  formState.picHeight = modelValue.height
  formState.pictureUrl = modelValue.pictureUrl
  formState.picScale = modelValue.picScale
  // formState.pictureUrl = modelValue.meta.url
}

// 提交处理
const handleSubmit = async () => {
  submitting.value = true
  try {
    // 构造提交数据
    const submitData = {
      ...formState,
      ...fileInfo.value,
      userId: '当前用户ID', // 从登录状态获取
    }

    // 这里添加实际的API调用
    // await api.uploadImage(submitData);

    message.success('图片上传成功')
    // 重置表单
    formState.name = ''
    formState.introduction = ''
    formState.categoryId = undefined
    fileList.value = []
  } catch (error) {
    message.error('上传失败')
  } finally {
    submitting.value = false
  }
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
