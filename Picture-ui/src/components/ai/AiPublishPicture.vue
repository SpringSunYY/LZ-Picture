<template>
  <a-modal v-model:open="openPublic" :width="700" destroy-on-close :footer="null">
    <template #title>
      <div class="custom-modal-title">
        <span style="color: #1890ff; margin-right: 8px">🚀</span>
        发布图片
        <a-tooltip
          title="您可以设置图片为公开，这样其他用户也可以查看到您的图片哦，如果设置为私有就只可以自己查看哦，同时也可以设置积分哦。"
        >
          <question-circle-outlined class="title-tip-icon" />
        </a-tooltip>
      </div>
    </template>
    <a-form
      :model="pictureInfo"
      @finish="handleSubmitPicture"
      :label-col="{ span: 6 }"
      :rules="rules"
      :wrapper-col="{ span: 16 }"
    >
      <a-row justify="center">
        <a-col :span="24">
          <a-form-item label="图片名称" name="name">
            <a-input
              v-model:value="pictureInfo.name"
              showCount
              :maxlength="32"
              allowClear
              placeholder="请输入图片名称"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="图片分类" name="categoryId">
            <a-cascader
              v-model:value="pictureInfo.categoryId"
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
              v-model:value="pictureInfo.spaceId"
              :options="spaceList"
              :filter-option="false"
              :fieldNames="{
                label: 'spaceName',
                value: 'spaceId',
              }"
              @search="handleSearchSpace"
              @select="handleSelectSpace"
              placeholder="请选择图片空间"
              :not-found-content="spaceLoading ? '加载中...' : '无匹配项'"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="图片文件夹">
            <a-cascader
              v-model:value="pictureInfo.folderId"
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
              v-model:value="pictureInfo.tags"
              :options="tagList"
              placeholder="请输入图片标签"
              :filter-option="false"
              :fieldNames="{
                label: 'name',
                value: 'name',
              }"
              @search="handleSearchTag"
              @select="handleSelectTag"
              :not-found-content="tagLoading ? '加载中...' : '无匹配项'"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="图片简介" name="introduction">
            <a-textarea
              v-model:value="pictureInfo.introduction"
              :rows="4"
              show-count
              :maxlength="512"
              placeholder="请输入图片简介"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item :label-col="{ span: 10 }" name="pictureStatus">
            <template #label>
              <span style="display: inline-flex; align-items: center">
                图片状态
                <a-tooltip
                  title="公共：所有的用户可见，别的用户使用您的图片生成，或者使用您的图片简介作为参考，查看您的原图，您将获得积分奖励。&#10;私有：仅自己或者团队可见"
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
            <a-radio-group v-model:value="pictureInfo.pictureStatus" name="radioGroup">
              <a-radio
                v-for="dict in p_picture_status"
                :value="dict.dictValue"
                :key="dict.dictValue"
              >
                {{ dict.dictLabel }}
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item name="pointsNeed">
            <template #label>
              <span style="display: inline-flex; align-items: center">
                积分
                <a-tooltip
                  title="如果您是公开可以设置积分，可以为0表示查看您原图的时候无需积分，反之为需要积分，积分必须为0或10的倍数"
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
            <a-input-number
              v-model:value="pictureInfo.pointsNeed"
              :min="0"
              :step="10"
              style="width: 90%"
              placeholder="请输入所需积分"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <div class="form-footer">
        <a-button @click="openPublic = false">取消</a-button>
        <a-button type="primary" html-type="submit" :loading="pictureLoading">提交</a-button>
      </div>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { InfoCircleOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue'
import { mySpaceInfo } from '@/api/picture/space.ts'
import { listPictureTagInfo } from '@/api/picture/pictureTag.ts'
import { listSpaceFolderInfo } from '@/api/picture/spaceFolder.ts'
import { handleTree } from '@/utils/lz.ts'
import { debounce } from 'lodash-es'
import { listPictureCategoryInfo } from '@/api/picture/pictureCategory'
import { addPictureInfoByAi } from '@/api/picture/picture.ts'
import type { PictureAiUpload } from '@/types/picture/picture.d.ts'
import type { PictureTagInfoQuery, PictureTagInfoVo } from '@/types/picture/pictureTag.d.ts'
import type { SpaceFolderInfoQuery, SpaceFolderInfoVo } from '@/types/picture/spaceFolder.d.ts'
import type { Space, SpaceQuery } from '@/types/picture/space.d.ts'
import {
  PCategoryStatusEnum,
  PCategoryTypeEnum,
  type PictureCategoryInfoQuery,
  type PictureCategoryInfoVo,
} from '@/types/picture/pictureCategory.d.ts'

const { proxy } = getCurrentInstance()!
const { p_picture_status } = proxy?.useDict('p_picture_status')

const props = defineProps<{
  initialItem: {
    prompt: string
    modelName: string
    logId: string
    createTime: string
  } | null
}>()

const emit = defineEmits(['success'])

watch(
  () => props.initialItem,
  (newItem) => {
    if (newItem) {
      pictureInfo.value = {
        introduction: newItem.prompt,
        tags: [newItem.modelName],
        pictureStatus: '0',
        logId: newItem.logId,
        pointsNeed: 0,
        spaceId: '',
        folderId: [],
        categoryId: [],
        name: '',
      }
      getMySpaceList()
      getTagList()
      getPictureCategoryList()
    }
  },
  { immediate: true },
)

const openPublic = ref(false)
const pictureInfo = ref<PictureAiUpload>({
  name: '',
  introduction: '',
  categoryId: '',
  spaceId: '',
  folderId: '',
  tags: [],
  pictureStatus: '0',
  logId: '',
})
const rules = {
  pictureUrl: [
    {
      required: true,
      message: '请选择图片',
      trigger: 'change',
    },
  ],
  name: [
    {
      required: true,
      message: '请输入图片名称',
      trigger: 'blur',
    },
  ],
  categoryId: [
    {
      required: true,
      message: '请选择图片分类',
      trigger: 'change',
    },
  ],
  spaceId: [
    {
      required: true,
      message: '请选择图片空间',
      trigger: 'change',
    },
  ],
  pictureStatus: [{ required: true, message: '图片状态不能为空' }],
}
//标签
const tagList = ref<PictureTagInfoVo[]>([])
const tagQuery = ref<PictureTagInfoQuery>({})
const tagLoading = ref(false)
const getTagList = () => {
  tagLoading.value = true
  listPictureTagInfo(tagQuery.value).then((res) => {
    tagList.value = res?.rows || []
    tagLoading.value = false
  })
}
const handleSearchSpace = debounce((value: string) => {
  spaceQuery.value.spaceName = value
  getMySpaceList()
}, 300)
const handleSelectTag = (value: string) => {
  //如果标签字符长度大于9个
  if (value.length > 9) {
    //删除选择
    pictureInfo.value.tags = pictureInfo.value.tags?.filter((item) => item !== value)
    message.error('标签名称不能超过9个字符')
    return
  }
  if ((pictureInfo.value.tags?.length ?? 0) > 5) {
    message.error('最多只能选择5个标签')
    pictureInfo.value.tags = pictureInfo.value.tags?.slice(0, 5)
    return
  }
}
//文件夹
const folderList = ref<SpaceFolderInfoVo[]>([])
const folderQuery = ref<SpaceFolderInfoQuery>({
  spaceId: '',
})
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
//空间
const spaceList = ref<Space[]>([])
const spaceQuery = ref<SpaceQuery>({})
const spaceLoading = ref(false)
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
const handleSelectSpace = () => {
  pictureInfo.value.folderId = ''
  folderQuery.value.spaceId = pictureInfo.value.spaceId
  getFolderList()
}
const handleSearchTag = debounce((value: string) => {
  tagQuery.value.name = value
  getTagList()
}, 300)
const pictureCategoryQuery = ref<PictureCategoryInfoQuery>({
  categoryStatus: PCategoryStatusEnum.P_CATEGORY_STATUS_0,
  categoryType: PCategoryTypeEnum.CATEGORY_TYPE_1,
})
//分类
const pictureCategoryList = ref<PictureCategoryInfoVo[]>([])
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
const pictureLoading = ref(false)

const handleSubmitPicture = async () => {
  //积分校验
  if (pictureInfo.value.pointsNeed && pictureInfo.value.pointsNeed > 0) {
    //必须为0或者10的倍数
    if (pictureInfo.value.pointsNeed % 10 !== 0) {
      message.error('消耗积分必须为0或者10的倍数')
      return
    }
  }
  let categoryId = null
  if (pictureInfo.value.categoryId && Array.isArray(pictureInfo.value.categoryId)) {
    categoryId = pictureInfo.value.categoryId[pictureInfo.value.categoryId.length - 1]
  }
  let folderId = null
  if (pictureInfo.value.folderId && Array.isArray(pictureInfo.value.folderId)) {
    folderId = pictureInfo.value.folderId[pictureInfo.value.folderId.length - 1]
  }
  pictureInfo.value = {
    ...pictureInfo.value,
    categoryId,
    folderId,
  }
  pictureLoading.value = true
  try {
    console.log('pictureInfo.value', pictureInfo.value)
    const res = await addPictureInfoByAi(pictureInfo.value)
    if (res.code === 200) {
      message.success('发布成功，如果是公开请等待审核')
      openPublic.value = false
      emit('success')
    }
  } catch (e) {
    message.error('发布失败')
  } finally {
    pictureLoading.value = false
  }
}

defineExpose({
  openModal: () => {
    openPublic.value = true
  },
})
</script>
