<template>
  <div class="inform-detail">
    <a-card class="detail-card" :title="detail.informTitle">
      <template #extra>
        <DictTag :value="detail.templateType" :options="c_template_type" />
      </template>

      <div class="meta">
        <div>
          <span>类型：</span>
          <DictTag :value="detail.informType" :options="u_inform_type" />
        </div>
        <div>
          <span>状态：</span>
          <DictTag :value="detail.isRead" :options="u_inform_is_read" />
        </div>
        <div>
          <span>时间：</span>
          <span>{{ detail.sendTime }}</span>
        </div>
      </div>
      <div class="content">
        <MarkdownView :modelValue="detail.content" />
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { getInformInfo } from '@/api/user/inform.ts'
import DictTag from '@/components/DictTag.vue'
import MarkdownView from '@/components/MarkdownView.vue'
import type { InformInfoVo } from '@/types/user/informInfo'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { u_inform_type, u_inform_is_read, c_template_type } = proxy?.useDict(
  'u_inform_type',
  'u_inform_is_read',
  'c_template_type',
)
const route = useRoute()
const recordId = route.query.recordId as string


const detail = ref<InformInfoVo>({
  recordId: '',
  informTitle: '',
  templateType: '',
  content: '',
  informType: '',
  isRead: 1,
  sendTime: '',
})

if (recordId) {
  getInformInfo(recordId).then((res) => {
    detail.value = res.data
  })
}
</script>

<style scoped lang="scss">
.inform-detail {
  padding: 20px;
  max-width: 1280px;
  margin: 0 auto;
  .detail-card {
    .meta {
      display: flex;
      gap: 40px;
      margin-bottom: 16px;
      font-size: 14px;
      color: #555;

      > div {
        display: flex;
        gap: 4px;
        align-items: center;
      }
    }

    .content {
      padding-top: 12px;
      font-size: 15px;
      line-height: 1.8;
      color: #333;
      white-space: pre-wrap;
    }
  }
}
</style>
