<template>
  <div></div>
</template>
<script lang="ts" setup>
import { Button, notification } from 'ant-design-vue'
import { h, onMounted, ref } from 'vue'
import { useNoticeStore } from '@/stores/modules/notice.ts'
import {
  CNoticePlatformEnum,
  getCNoticeTypeLabel,
  type NoticeInfoVo,
} from '@/types/config/notice.d.ts'
import { useRouter } from 'vue-router'

const close = () => {
  noticeStore.markAsRead(noticeInfo.value)
}
const openNotification = () => {
  const key = `open${Date.now()}`
  notification.open({
    message: getCNoticeTypeLabel(noticeInfo.value?.noticeType),
    description: noticeInfo.value?.noticeTitle,
    top: 80,
    btn: () => [
      h(
        Button,
        {
          type: 'primary',
          size: 'small',
          onClick: () => {
            notification.close(key)
            toNoticeInfo() // 点击后跳转到详情页
          },
        },
        { default: () => '立即查看' },
      ),
    ],
    key,
    onClose: close,
  })
}
const noticeStore = useNoticeStore()
const noticeInfo = ref<NoticeInfoVo>()
const router = useRouter()
const toNoticeInfo = () => {
  router.push({ name: 'NoticeDetail', query: { noticeId: noticeInfo.value?.noticeId } })
}

onMounted(async () => {
  const notice = await noticeStore.ensureTodayNotice({
    locale: 'zh-CN',
    platform: CNoticePlatformEnum.NOTICE_PLATFORM_1,
  })
  if (!noticeStore.hasPushedToday) {
    console.log('今日已推送过公告',notice)
    noticeInfo.value = notice
    // 今日首次展示公告
    openNotification()
  } else {
    // 今日已推送或无新公告
    console.log('不推送今日公共')
  }
})
</script>
