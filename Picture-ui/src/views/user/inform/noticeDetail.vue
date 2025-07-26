<template>
  <div class="inform-detail">
    <a-card class="detail-card" :title="detail.noticeTitle">
      <template #extra>
        <DictTag :value="detail.noticeType" :options="u_notice_type" />
      </template>

      <div class="meta">
        <div>
          <span>时间：</span>
          <span>{{ detail.createTime }}</span>
        </div>
      </div>
      <div class="content">
        <MarkdownView :modelValue="detail.content" />
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref } from 'vue'
import { useRoute } from 'vue-router'
import DictTag from '@/components/DictTag.vue'
import MarkdownView from '@/components/MarkdownView.vue'
import { getNoticeInfo } from '@/api/config/notice.ts'
import type { NoticeInfoVo } from '@/types/config/notice'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { u_notice_type } = proxy?.useDict('u_notice_type')
const route = useRoute()
const noticeId = route.query.noticeId as string

const detail = ref<NoticeInfoVo>({
  noticeId: '',
  noticeTitle: '',
  noticeType: '',
  content: '',
  createTime: '',
})

if (noticeId) {
  getNoticeInfo(noticeId).then((res) => {
    detail.value = res.data || null
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
