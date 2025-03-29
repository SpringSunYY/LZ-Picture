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

        <!-- 分类选择 -->
        <a-form-item label="图片分类" name="categoryId">
          <a-select
            v-model:value="formState.categoryId"
            :options="categories"
            placeholder="请选择分类"
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

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import PictureUpload from '@/components/PictureUpload/index.vue'

const formState = reactive({
  name: '',
  introduction: '',
  pictureUrl: '',
  categoryId: '',
  pointsNeed: 10,
  pictureStatus: 0,
})

const rules = {}

const fileList = ref([])
const categories = ref([])
const submitting = ref(false)

const handleSuccess = (modelValue) => {
  console.log('modelValue',modelValue)
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

// 初始化分类数据
onMounted(async () => {
  try {
    // 这里添加获取分类的API调用
    // categories.value = await api.getCategories();
    categories.value = [
      { label: '自然风景', value: '1' },
      { label: '城市建筑', value: '2' },
    ]
  } catch (error) {
    message.error('分类加载失败')
  }
})
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
