<template>
  <div class="picture-space">
    <!-- 空状态 -->
    <div v-if="spaceList.length == 0" class="empty-state">
      <div class="add-card" style="width: 25%; margin: 0 auto" @click="handleAdd">
        <div class="add-content">
          <plus-outlined class="add-icon" />
          <div class="add-text">新建空间</div>
        </div>
      </div>
    </div>
    <!-- 空间网格列表 -->
    <a-row v-else :gutter="[24, 24]">
      <a-col v-for="space in spaceList" :key="space.spaceId" :xs="24" :sm="12" :md="8" :lg="6">
        <div class="space-card">
          <div
            class="cover-image"
            @click="goDetail(space.spaceId)"
            :style="coverStyle(space)"
          ></div>
          <div class="space-info">
            <h3 class="title">{{ space.spaceName }}</h3>
            <div class="meta">
              <span>{{ formatSize(space.totalSize) }}</span>
              <a-divider type="vertical" />
              <span>{{ space.totalCount }}个文件</span>
              <a-divider type="vertical" />
              <Tags :values="[getPSpaceStatusLabel(space.spaceStatus)]" :colors="['#1890ff']" />
              <Tags :values="[getPSpaceTypeLabel(space.spaceType)]" :colors="['#00ff0d']" />
              <a-button
                v-if="space.userId === userId && checkPermiSingle('picture:space:update')"
                style="float: right"
                type="primary"
                @click="handleUpdate(space.spaceId)"
                size="small"
              >
                修改
              </a-button>
            </div>
          </div>
        </div>
      </a-col>
      <a-col :xs="24" :sm="12" :md="8" :lg="6">
        <div class="add-card" @click="handleAdd">
          <div class="add-content">
            <plus-outlined class="add-icon" />
            <div class="add-text">新建空间</div>
          </div>
        </div>
      </a-col>
    </a-row>
    <u-divider border-style="dashed"></u-divider>
    <PictureInfoList style="margin-top: 20px"></PictureInfoList>
    <!--添加空间-->
    <a-modal v-model:open="open" :footer="null" centered destroyOnClose>
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="custom-modal-title">
          <span style="color: #1890ff; margin-right: 8px">🚀</span>
          {{ title }}
          <a-tooltip
            title="个人公共/私有空间每种状态最多创建10个且不可以转换状态，团队空间最多创建10个"
          >
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
        <a-form-item label="空间名称" name="spaceName">
          <a-input v-model:value="formState.spaceName" showCount :maxlength="32" allowClear />
        </a-form-item>
        <a-form-item label="空间封面" name="spaceAvatar">
          <CoverUpload
            v-model:value="formState.spaceAvatar"
            :acceptTypes="['image/jpeg', 'image/png']"
            :maxSize="10"
            :maxCount="1"
          />
        </a-form-item>
        <a-row :gutter="[24, 24]">
          <a-col :xs="24" :sm="12" :md="12" :lg="12">
            <a-form-item :label-col="{ span: 10 }" label="空间状态" name="spaceStatus">
              <a-radio-group
                :disabled="formState.spaceId !== ''"
                v-model:value="formState.spaceStatus"
                name="radioGroup"
              >
                <a-radio
                  v-for="dict in p_space_status"
                  :value="dict.dictValue"
                  :key="dict.dictValue"
                >
                  {{ dict.dictLabel }}
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12" :md="12" :lg="12">
            <a-form-item label="空间类型" :label-col="{ span: 10 }" name="spaceType">
              <a-radio-group
                :disabled="formState.spaceId !== ''"
                v-model:value="formState.spaceType"
                name="radioGroup"
              >
                <a-radio value="2">个人</a-radio>
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
          <a-button type="primary" html-type="submit" :loading="submitting">提交</a-button>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts" name="PictureSpace">
import { PlusOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue'
import { getCurrentInstance, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getPSpaceStatusLabel, getPSpaceTypeLabel } from '@/types/picture/space.d.ts'
import type { Space, SpaceInfo, SpaceQuery } from '@/types/picture/space.d.ts'
import { addSpaceInfo, getSpaceInfo, mySpaceInfo, updateSpaceInfo } from '@/api/picture/space.ts'
import { message } from 'ant-design-vue'
import Tags from '@/components/Tags.vue'
import { formatSize } from '@/utils/common.ts'
import useUserStore from '@/stores/modules/user.ts'
import { storeToRefs } from 'pinia'
import { checkPermiSingle } from '@/utils/permission.ts'
import CoverUpload from '@/components/CoverUpload.vue'
import PictureInfoList from '@/components/PictureInfoList.vue'

const instance = getCurrentInstance()
const proxy = instance?.proxy

const { p_space_status } = proxy?.useDict('p_space_status')
const userStore = useUserStore()
const { userId: userId } = storeToRefs(userStore)
// 新增状态管理
const open = ref(false)
const submitting = ref(false)

const spaceList = ref<Space[]>([])
const spaceQuery = reactive<SpaceQuery>({})
const title = ref('')
// 表单数据结构
const formState = reactive<SpaceInfo>({
  spaceId: '',
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

// 封面样式处理
const coverStyle = (space: Space) => ({
  backgroundImage: `url(${space.spaceAvatar || '/default-space-cover.jpg'})`,
})

// 路由跳转
const router = useRouter()
const goDetail = (id: string) =>
  router.push({
    path: '/picture/space/spaceFolder',
    query: { spaceId: id },
  })
// 添加空间
const handleAdd = () => {
  resetForm()
  open.value = true
  title.value = '创建空间'
}

const resetForm = () => {
  Object.assign(formState, {
    spaceId: '',
    spaceName: '',
    spaceAvatar: '',
    spaceDesc: '',
    spaceType: '0',
    spaceStatus: '0',
  })
}

const handleSubmit = () => {
  if (formState.spaceId !== '') {
    updateSpaceInfo(formState).then((res) => {
      if (res?.code === 200) {
        message.success('修改空间成功')
        getMySpaceList()
        open.value = false
      }
    })
  } else {
    addSpaceInfo(formState).then((res) => {
      if (res?.code === 200) {
        message.success('创建空间成功')
        getMySpaceList()
        open.value = false
      }
    })
  }
}
const handleUpdate = (spaceId: string) => {
  // console.log(spaceId)
  resetForm()
  title.value = '修改空间'
  getSpaceInfo(spaceId).then((res) => {
    Object.assign(formState, res.data)
    open.value = true
  })
}
const getMySpaceList = () => {
  // 获取我的空间列表
  mySpaceInfo(spaceQuery).then((res) => {
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
