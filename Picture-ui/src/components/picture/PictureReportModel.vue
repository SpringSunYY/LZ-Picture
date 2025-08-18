<template>
  <a-modal v-model:open="openReport" :footer="null" centered destroyOnClose>
    <template #title>
      <div class="custom-modal-title">
        <span style="color: #1890ff; margin-right: 8px">🚀</span>
        {{ title }}
        <a-tooltip :title="titleDesc">
          <question-circle-outlined class="title-tip-icon" />
        </a-tooltip>
      </div>
    </template>
    <a-form
      :model="formReport"
      :rules="rulesReport"
      @finish="handleSubmitReport"
      ref="formRef"
      labelAlign="left"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 18 }"
    >
      <a-form-item label="举报类型" name="reportType">
        <a-radio-group v-model:value="formReport.reportType" name="radioGroup">
          <a-radio v-for="dict in p_report_type" :value="dict.dictValue" :key="dict.dictValue">
            {{ dict.dictLabel }}
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item name="reason">
        <template #label>
          <span style="display: inline-flex; align-items: center">
            举报原因
            <a-tooltip
              title="请描述您详细的举报原因，对您造成的影响，例：图片侵权，请列举您的版权信息"
            >
              <InfoCircleOutlined
                style="margin-left: 4px; color: #999; font-size: 14px; position: relative; top: 1px"
              />
            </a-tooltip>
          </span>
        </template>
        <a-textarea
          :showCount="true"
          placeholder="请输入内容"
          :auto-size="{ minRows: 5 }"
          v-model:value="formReport.reason"
        />
      </a-form-item>
      <a-form-item name="contact">
        <template #label>
          <span style="display: inline-flex; align-items: center">
            联系方式
            <a-tooltip
              title="请输入您的联系方式，手机号码、微信（推荐）等信息，例：微信：123456789，便于我们联系您处理举报信息。"
            >
              <InfoCircleOutlined
                style="margin-left: 4px; color: #999; font-size: 14px; position: relative; top: 1px"
              />
            </a-tooltip>
          </span>
        </template>
        <a-textarea
          placeholder="请输入联系方式"
          :auto-size="{ minRows: 2 }"
          :showCount="true"
          :max-length="512"
          v-model:value="formReport.contact"
        />
      </a-form-item>
      <div class="form-footer">
        <a-button @click="openReport = false">取消</a-button>
        <a-button type="primary" html-type="submit" :loading="submittingReport">提交</a-button>
      </div>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref, watch } from 'vue'
import { addUserReportInfo } from '@/api/picture/userReportInfo.ts'
import { message } from 'ant-design-vue'
import { useConfig } from '@/utils/config.ts'
import { InfoCircleOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue'

const { proxy } = getCurrentInstance()!
const { p_report_type } = proxy?.useDict('p_report_type') as any

const props = defineProps<{
  targetId: string
}>()

const emit = defineEmits(['success'])

const openReport = ref(false)
const title = ref('举报图片')
const titleDesc = ref('请选择举报图片类型')
const formRef = ref<any>(null)

const formReport = ref({
  targetType: '0',
  targetId: props.targetId,
  reportType: '0',
  reason: '',
  contact: '',
})

watch(
  () => props.targetId,
  (newId) => {
    formReport.value.targetId = newId
  },
)

const rulesReport = ref({
  reason: [
    { required: true, message: '请输入举报内容', trigger: 'blur' },
    { min: 16, message: '请输入16个字符以上的内容', trigger: 'blur' },
  ],
  reportType: [{ required: true, message: '请选择举报类型', trigger: 'change' }],
  contact: [
    { required: true, message: '请输入联系方式', trigger: 'blur' },
    { min: 16, message: '请输入16个字符以上的内容', trigger: 'blur' },
  ],
})

const submittingReport = ref(false)

const handleOpen = async () => {
  titleDesc.value = await useConfig('picture:report:content')
  openReport.value = true
  title.value = '举报图片'
  if (formRef.value) {
    formRef.value.resetFields()
  }
  formReport.value = {
    ...formReport.value,
    reportType: '0',
    reason: '',
    contact: '',
  }
}

const handleSubmitReport = () => {
  submittingReport.value = true
  addUserReportInfo(formReport.value)
    .then((res: any) => {
      if (res.code === 200) {
        message.success('举报成功')
        openReport.value = false
        emit('success')
      } else {
        message.error('举报失败')
      }
    })
    .finally(() => {
      submittingReport.value = false
    })
}

defineExpose({
  handleOpen,
})
</script>
