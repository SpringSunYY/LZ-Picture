<template>
  <div class="inform-page max-w-7xl mx-auto p-4">
    <a-tabs default-active-key="all" v-model:activeKey="activeKey">
      <a-tab-pane key="all" tab="全部" />
      <a-tab-pane key="1" tab="短信" />
      <a-tab-pane key="2" tab="邮件" />
      <a-tab-pane key="3" tab="站内通知" />
      <a-tab-pane key="4" tab="APP推送" />
      <a-tab-pane key="5" tab="微信模板" />
    </a-tabs>

    <div v-for="item in pagedData" :key="item.recordId" class="message-card" @click="goDetail(item)">
      <div class="card-top">
        <span class="type">{{ templateTypeMap[item.templateType] }}</span>
        <span class="title">{{ item.informTitle }}</span>
      </div>
      <div class="card-bottom">
        <div class="right-meta">
          <span>{{ informTypeMap[item.informType] }}</span>
          <span :class="{ unread: item.isRead === 0 }">
            {{ item.isRead === 0 ? '未读' : '已读' }}
          </span>
          <span>{{ item.sendTime }}</span>
        </div>
      </div>
    </div>

    <a-pagination
      class="mt-6 text-right"
      :page-size="pageSize"
      :current="currentPage"
      :total="filteredData.length"
      @change="onPageChange"
      show-less-items
    />
  </div>
</template>

<script setup lang="ts">

import { ref, computed ,watch} from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const templateTypeMap: Record<string, string> = {
  '1': '短信',
  '2': '邮件',
  '3': '站内通知',
  '4': 'APP推送',
  '5': '微信模板',
}
const informTypeMap: Record<string, string> = {
  '0': '通知',
  '1': '公共',
}

const activeKey = ref('all')
const currentPage = ref(1)
const pageSize = 8

const generateMockData = (count = 50) => {
  const titles = [
    '欢迎注册',
    '积分到账通知',
    '活动优惠提醒',
    '安全提醒',
    '任务完成奖励',
    '服务升级通知',
    '功能上线通知',
    '节假日祝福',
    '订单提醒',
    '实名认证通知',
  ]
  const contents = [
    '感谢您注册平台账号！',
    '您已获得100积分奖励',
    '平台满减活动即将开始',
    '您的账号存在异常登录行为',
    '完成每日签到获得10积分',
    '平台将于周末进行维护升级',
    '新功能已上线，欢迎体验',
    '祝您假期愉快！',
    '您的订单已发货',
    '您的实名认证已通过审核',
  ]
  const now = new Date()
  return Array.from({ length: count }, (_, i) => {
    const offset = i * 3600 * 1000
    return {
      recordId: String(i + 1),
      informTitle: titles[i % titles.length],
      templateType: String((i % 5) + 1),
      content: contents[i % contents.length],
      informType: String(i % 2),
      isRead: i % 3 === 0 ? 0 : 1,
      sendTime: new Date(now.getTime() - offset)
        .toISOString()
        .slice(0, 19)
        .replace('T', ' '),
    }
  })
}

const data = generateMockData(50)

const filteredData = computed(() => {
  if (activeKey.value === 'all') return data
  return data.filter((item) => item.templateType === activeKey.value)
})

const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredData.value.slice(start, start + pageSize)
})

const onPageChange = (page: number) => {
  currentPage.value = page
}

// 每次切换 Tab，重置页码
watch(activeKey, () => {
  currentPage.value = 1
})

const goDetail = (item: any) => {
  router.push({ name: 'UserInformDetail', query: { id: item.recordId } })
}
</script>

<style scoped lang="scss">
.message-card {
  background: #fff;
  padding: 2px 24px;
  margin-bottom: 8px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;

  &:hover {
    transform: scale(1.06);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  }

  .card-top {
    display: flex;
    gap: 12px;
    align-items: center;
    font-size: 17px;
    font-weight: 500;
    color: #1d1d1f;

    .type {
      color: #2f54eb;
    }

    .title {
      flex: 1;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }

  .card-bottom {
    display: flex;
    justify-content: flex-end;
    margin-top: 10px;

    .right-meta {
      font-size: 13px;
      color: #999;
      display: flex;
      gap: 16px;

      .unread {
        color: #fa541c;
        font-weight: 500;
      }
    }
  }
}
</style>
