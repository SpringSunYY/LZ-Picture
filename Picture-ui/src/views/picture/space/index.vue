<template>
  <div class="picture-space">
    <!-- 空状态 -->
    <div v-if="spaceList.length == 0" class="empty-state">
      <div class="add-card" style="width: 25%; margin: 0 auto" @click="handleCreate">
        <div class="add-content">
          <plus-outlined class="add-icon" />
          <div class="add-text">新建空间</div>
        </div>
      </div>
    </div>

    <!-- 空间网格列表 -->
    <a-row v-else :gutter="[24, 24]">
      <a-col v-for="space in spaceList" :key="space.spaceId" :xs="24" :sm="12" :md="8" :lg="6">
        <div class="space-card" @click="goDetail(space.spaceId)">
          <div class="cover-image" :style="coverStyle(space)"></div>
          <div class="space-info">
            <h3 class="title">{{ space.spaceName }}</h3>
            <div class="meta">
              <span>{{ formatSize(space.totalSize) }}</span>
              <a-divider type="vertical" />
              <span>{{ space.totalCount }}个文件</span>
              <a-divider type="vertical" />
              <span>{{ space.spaceType }}</span>
            </div>
          </div>
        </div>
      </a-col>
      <a-col :xs="24" :sm="12" :md="8" :lg="6">
        <div class="add-card" @click="handleCreate">
          <div class="add-content">
            <plus-outlined class="add-icon" />
            <div class="add-text">新建空间</div>
          </div>
        </div>
      </a-col>
    </a-row>

    <!--添加空间-->
    <a-modal v-model:open="open" title="创建新空间" :footer="null" centered destroyOnClose>
      <a-form
        :model="formState"
        :rules="rules"
        @finish="handleSubmit"
        ref="formRef"
        labelAlign="left"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="空间名称" name="spaceName">
          <a-input v-model:value="formState.spaceName" showCount :maxlength="32" allowClear />
        </a-form-item>
        <a-form-item label="空间封面" name="spaceAvatar">
          <PictureUpload
            :modelValue="formState.spaceAvatar"
            v-model:value="formState.spaceAvatar"
            :allowedFormats="['image/jpeg', 'image/png']"
            :maxSizeMB="10"
            :maxCount="1"
            @upload-success="uploadSuccess"
          />
        </a-form-item>
        <a-row :gutter="[24, 24]">
          <a-col :xs="24" :sm="12" :md="12" :lg="12">
            <a-form-item :label-col="{ span: 10 }" label="空间状态" name="spaceStatus">
              <a-radio-group v-model:value="formState.spaceStatus" name="radioGroup">
                <a-radio value="0">公开</a-radio>
                <a-radio value="1">私有</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12" :md="12" :lg="12">
            <a-form-item label="空间类型" :label-col="{ span: 10 }" name="spaceType">
              <a-radio-group v-model:value="formState.spaceType" name="radioGroup">
                <a-radio value="0">个人</a-radio>
                <a-radio value="1">团队</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="空间描述" name="spaceDesc">
          <a-textarea v-model:value="formState.spaceDesc" :rows="3" showCount :maxlength="512" />
        </a-form-item>

        <div class="form-footer">
          <a-button @click="open = false">取消</a-button>
          <a-button type="primary" html-type="submit" :loading="submitting"> 立即创建</a-button>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts" name="PictureSpace">
import { PlusOutlined } from '@ant-design/icons-vue'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import PictureUpload from '@/components/PictureUpload/index.vue'
import type { Space, SpaceAdd, SpaceQuery } from '@/types/picture/space'
import { addSpace, mySpace } from '@/api/picture/space.ts'
import { message } from 'ant-design-vue'

// 新增状态管理
const open = ref(false)
const submitting = ref(false)
const formRef = ref()
const spaceList = ref<Space[]>([])
const spaceQuery = reactive<SpaceQuery>({})
// 表单数据结构
const formState = reactive<SpaceAdd>({
  spaceName: '',
  spaceAvatar: '',
  spaceDesc: '',
  spaceType: '0',
  spaceStatus: '0',
})

// 验证规则
const rules = {
  spaceName: [
    { required: true, message: '名称不能为空' },
    { min: 2, max: 32, message: '长度需在2-32字符之间' },
  ],
  spaceType: [{ required: true, message: '空间类型不能为空' }],
  spaceDesc: [{ max: 512, message: '长度不能超过512字符' }],
  spaceStatus: [{ required: true, message: '空间状态不能为空' }],
}

// 假数据生成
const mockSpaces = Array.from({ length: 5 }, (_, i) => ({
  space_id: `space_${i + 1}`,
  space_name: `项目空间 ${i + 1}`,
  space_avatar: i % 2 ? 'https://picsum.photos/300/200' : null,
  total_size: Math.random() * 1073741824,
  total_count: Math.floor(Math.random() * 500),
  space_type: i % 3,
  current_members: i % 2 ? 3 : 0,
}))

const spaces = ref([...mockSpaces])
console.log(spaces.value)
// 封面样式处理
const coverStyle = (space) => ({
  backgroundImage: `url(${space.spaceAvatar || '/default-space-cover.jpg'})`,
})

// 容量格式化
const formatSize = (bytes) => {
  const units = ['B', 'KB', 'MB', 'GB']
  let size = bytes
  let unitIndex = 0
  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024
    unitIndex++
  }
  return `${size}${units[unitIndex]}`
}

// 路由跳转
const router = useRouter()
const goDetail = (id) => router.push(`/space/${id}`)
// 添加空间
const handleCreate = () => {
  resetForm()
  open.value = true
  formRef.value?.resetFields()
}

const resetForm = () => {
  Object.assign(formState, {
    spaceName: '',
    spaceAvatar: '',
    spaceDesc: '',
    spaceType: '0',
    spaceStatus: '0',
  })
}

const uploadSuccess = (modelValue: any) => {
  console.log('modelValue', modelValue)
  formState.spaceAvatar = modelValue.pictureUrl
}
const handleSubmit = () => {
  addSpace(formState).then((res) => {
    if (res.code === 200) {
      message.success('创建成功')
      open.value = false
      getMySpaceList()
    } else {
      message.error('创建失败')
    }
  })
}
const getMySpaceList = () => {
  // 获取我的空间列表
  mySpace(spaceQuery).then((res) => {
    if (res.code === 200) {
      spaceList.value = res?.rows || []
    } else {
      message.error('获取空间列表失败')
    }
  })
}
getMySpaceList()
</script>

<style lang="scss" scoped>
.form-footer {
  text-align: right;
  padding: 16px 0 0;
  margin-top: 24px;
  border-top: 1px solid #f0f0f0;

  .ant-btn {
    margin-left: 10px;
  }
}

.picture-space {
  padding: 24px;
  max-width: 1440px;
  margin: 0 auto;

  .empty-state {
    display: flex;
    justify-content: center; // 水平居中
    align-items: center; // 垂直居中
    height: 70vh; // 占据整个视口高度
    width: 100%; // 占据整个宽度
    :deep(.ant-empty-image) {
      height: 200px;
    }

    .create-tip {
      color: rgba(0, 0, 0, 0.45);
      margin-top: 12px;
    }
  }

  .add-card {
    border: 2px dashed #d9d9d9;
    height: 224px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 8px;

    &:hover {
      border-color: #1890ff;
    }

    .add-content {
      text-align: center;

      .add-icon {
        font-size: 32px;
        color: #1890ff;
      }

      .add-text {
        margin-top: 8px;
        color: rgba(0, 0, 0, 0.85);
      }
    }
  }

  .space-card {
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

    .space-info {
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
}
</style>
