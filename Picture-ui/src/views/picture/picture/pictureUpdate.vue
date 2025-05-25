<template>
  <div class="picture-update">
    <a-card :bordered="false">
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          {{ title }}
          <a-tooltip
            title="您可以上传图片到官方空间、自己的空间以及加入的团队空间中，并设置图片信息，如名称、简介、分类、标签等，如果积分为0则表示图片免费。"
          >
            <question-circle-outlined class="title-tip-icon" />
          </a-tooltip>
        </div>
      </template>
      <a-form
        :model="formState"
        @finish="handleSubmit"
        :label-col="{ span: 4 }"
        :rules="rules"
        :wrapper-col="{ span: 18 }"
      >
        <a-row justify="center">
          <a-col :span="24">
            <!-- 图片上传 -->
            <a-form-item label="图片文件" name="pictureUrl">
              <PictureUpload
                :modelValue="formState.pictureUrl"
                v-model:value="formState.pictureUrl"
                :allowedFormats="['image/jpeg', 'image/png']"
                :maxSizeMB="50"
                :maxCount="1"
                @upload-success="handleSuccess"
              />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <!-- 图片名称 -->
            <a-form-item label="图片名称" name="name">
              <a-input
                v-model:value="formState.name"
                showCount
                :maxlength="32"
                allowClear
                placeholder="请输入图片名称"
              />
            </a-form-item>
          </a-col>
          <a-col :span="24">
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
          </a-col>
          <a-col :span="24">
            <a-form-item label="图片空间" name="spaceId">
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
          </a-col>
          <a-col :span="24">
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
          </a-col>
          <a-col :span="24">
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
          </a-col>
          <a-col :span="24">
            <!-- 图片描述 -->
            <a-form-item label="图片简介" name="introduction">
              <a-textarea
                v-model:value="formState.introduction"
                :rows="4"
                placeholder="请输入图片简介"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <!-- 所需积分 -->
            <a-form-item label="所需积分" :label-col="{ span: 8 }" name="pointsNeed">
              <a-input-number
                style="width: 100%"
                v-model:value="formState.pointsNeed"
                :step="10"
                :min="0"
                :precision="0"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <!-- 图片状态 -->
            <a-form-item label="图片状态" :label-col="{ span: 8 }" name="pictureStatus">
              <a-switch
                style="width: 20%"
                v-model:checked="formState.pictureStatus"
                checked-children="公开"
                un-checked-children="私有"
                :checked-value="0"
                :unchecked-value="1"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <!-- 提交按钮 -->
        <a-form-item :wrapper-col="{ offset: 4 }">
          <a-button
            type="primary"
            html-type="submit"
            style="padding: 0 40px; margin: 0 auto"
            :loading="submitting"
          >
            提交
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts" name="PictureUpdate">
import { reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import PictureUpload from '@/components/PictureUpload.vue'
import type {
  PictureCategoryInfoQuery,
  PictureCategoryInfoVo
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
import { QuestionCircleOutlined } from '@ant-design/icons-vue'
import { findPathById } from '@/utils/common.ts'
import { useRouter } from 'vue-router'

const title = ref('图片上传')
//空间
const spaceList = ref<Space[]>([])
const spaceQuery = ref<SpaceQuery>({})
const spaceLoading = ref(false)
//分类
const pictureCategoryList = ref<PictureCategoryInfoVo[]>([])
const pictureCategoryQuery = ref<PictureCategoryInfoQuery>({})
//文件夹
const folderQuery = ref<SpaceFolderInfoQuery>({
  spaceId: ''
})
const folderList = ref<SpaceFolderInfoVo[]>([])
//标签
const tagList = ref<PictureTagInfoVo[]>([])
const tagQuery = ref<PictureTagInfoQuery>({})
const tagLoading = ref(false)

const rules = {
  pictureUrl: [
    {
      required: true,
      message: '请选择图片',
      trigger: 'change'
    }
  ],
  name: [
    {
      required: true,
      message: '请输入图片名称',
      trigger: 'blur'
    }
  ],
  categoryId: [
    {
      required: true,
      message: '请选择图片分类',
      trigger: 'change'
    }
  ],
  spaceId: [
    {
      required: true,
      message: '请选择图片空间',
      trigger: 'change'
    }
  ],
  pointsNeed: [
    {
      required: true,
      message: '请输入所需积分',
      trigger: 'blur'
    },
    //必须是0或者10的倍数
    {
      validator: (rule: any, value: number) => {
        return value % 10 === 0
          ? Promise.resolve()
          : Promise.reject(new Error('请输入0或者10的倍数'))
      },
      trigger: 'blur'
    }
  ],
  pictureStatus: [
    {
      required: true,
      message: '请选择图片状态',
      trigger: 'change'
    }
  ]
}
const submitting = ref(false)
const formState = reactive<PictureInfo>({
  pictureUrl: '',
  dnsUrl: '',
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
  picScale: 0
})
const handleSuccess = (modelValue: any) => {
  // 提交到后端或处理数据
  formState.name = modelValue.name
  formState.thumbnailUrl = modelValue.thumbnailUrl
  formState.pictureUrl = modelValue.pictureUrl
  formState.dnsUrl = modelValue.dnsUrl
  formState.picWidth = modelValue.meta.width
  formState.picHeight = modelValue.meta.height
  formState.picScale = modelValue.meta.ratio
  formState.picFormat = modelValue.meta.format
  formState.picSize = modelValue.meta.size
}

const router = useRouter()
// 提交处理
const handleSubmit = async () => {
  submitting.value = true
  //如果分类或者文件夹存在，取数组最后一个
  if (formState.categoryId && Array.isArray(formState.categoryId)) {
    formState.categoryId = formState.categoryId[formState.categoryId.length - 1]
  }
  if (formState.folderId && Array.isArray(formState.folderId)) {
    formState.folderId = formState.folderId[formState.folderId.length - 1]
  }
  addPictureInfo(formState).then((res) => {
    if (res.code === 200) {
      message.success('添加成功,即将回到我的空间')
      setTimeout(() => {
        //路由至我的空间
        router.push({
          path: '/picture/space'
        })
        //重置表单
        Object.assign(formState, {
          thumbnailUrl: '',
          pictureUrl: '',
          dnsUrl: '',
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
          picScale: 0
        })
      }, 3000)
    }
  })
  // 处理分类路径
  if (formState.categoryId) {
    const selectedPath = findPathById(pictureCategoryList.value, formState.categoryId)
    if (selectedPath) {
      formState.categoryId = selectedPath // 设置为数组形式 ['1', '1-1', '1-1-1']
    } else {
      console.warn('未找到匹配的分类路径')
    }
  }
  submitting.value = false
}

const getPictureCategoryList = async () => {
  listPictureCategoryInfo(pictureCategoryQuery.value).then((res) => {
    pictureCategoryList.value = handleTree(
      JSON.parse(JSON.stringify(res?.rows || [])),
      'categoryId',
      'parentId',
      'children'
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
      'children'
    )
  })
}
const handleSearchTag = debounce((value: string) => {
  tagQuery.value.name = value
  getTagList()
}, 300)
const handleSelectTag = (value: string) => {
  //如果标签字符长度大于9个
  if (value.length > 9) {
    //删除选择
    formState.tags = formState.tags?.filter((item) => item !== value)
    message.error('标签名称不能超过9个字符')
    return
  }
  if ((formState.tags?.length ?? 0) > 5) {
    message.error('最多只能选择5个标签')
    formState.tags = formState.tags?.slice(0, 5)
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

  .custom-modal-title {
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
