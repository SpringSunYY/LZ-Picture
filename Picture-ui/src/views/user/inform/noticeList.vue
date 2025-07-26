<template>
  <div class="inform-list">
    <a-page-header title="通知公告" sub-title="平台的通知公告也要关心哦" />

    <a-form layout="inline" style="margin: 0 2em" :model="queryParams">
      <a-form-item label="通知标题">
        <a-input-search
          v-model:value="queryParams.noticeTitle"
          placeholder="搜索消息标题"
          @search="handleSearch"
          enter-button
          style="min-width: 200px"
        />
      </a-form-item>
      <a-space>
        <a-button type="primary" @click="resetSearch">重置</a-button>
      </a-space>
    </a-form>
    <div style="margin-bottom: 10px"></div>

    <a-tabs
      default-active-key="all"
      style="margin: 0 2em"
      v-model:activeKey="activeKey"
      @change="handleTabChange"
    >
      <a-tab-pane key="all" tab="全部" />
      <a-tab-pane v-for="item in u_notice_type" :key="item.dictValue" :tab="item.dictLabel" />
    </a-tabs>

    <div v-for="item in dataList" :key="item.noticeId" class="message-card" @click="goDetail(item)">
      <div class="card-top">
        <span class="title">{{ item.noticeTitle }}</span>
      </div>
      <div class="card-bottom">
        <div class="right-meta">
          <span>
            <DictTag :value="item.noticeType" :options="u_notice_type" />
          </span>
          <span>{{ item.createTime }}</span>
        </div>
      </div>
    </div>

    <a-pagination
      v-if="total > 0"
      class="mt-6 text-right"
      :page-size="queryParams.pageSize"
      :current="queryParams.pageNum"
      :total="total"
      @change="onPageChange"
      :show-total="(total) => `共 ${total} 条`"
      :page-size-options="['8', '10', '20', '50']"
      show-less-items
    />
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  CNoticePlatformEnum,
  type NoticeInfoRequest,
  type NoticeInfoVo,
} from '@/types/config/notice.d.ts'
import { listNoticeInfo } from '@/api/config/notice.ts'
import DictTag from '@/components/DictTag.vue'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { u_notice_type } = proxy?.useDict('u_notice_type')
const router = useRouter()
const dataList = ref<NoticeInfoVo[]>([])
const loading = ref(false)
const total = ref(0)
const queryParams = ref<NoticeInfoRequest>({
  pageNum: 1,
  pageSize: 8,
  locale: 'zh-CN',
  platform: CNoticePlatformEnum.NOTICE_PLATFORM_1,
  noticeType: null,
  noticeTitle: '',
})

const getDataList = () => {
  loading.value = true
  listNoticeInfo(queryParams.value).then((res) => {
    dataList.value = res?.rows || []
    total.value = res?.total || 0
    loading.value = false
  })
}
getDataList()

const activeKey = ref('all')
const onPageChange = (pag, filters, sorter) => {
  console.log(pag, filters, sorter)
  queryParams.value.pageNum = pag
  queryParams.value.pageSize = filters
  getDataList()
}

const handleTabChange = (key: string) => {
  queryParams.value.pageNum = 1
  if (key === 'all') {
    queryParams.value.noticeType = null
  } else {
    queryParams.value.noticeType = key
  }
  getDataList()
}
const handleSearch = () => {
  queryParams.value.pageNum = 1
  queryParams.value.pageSize = 1
  getDataList()
}

const resetSearch = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 8,
    noticeType: activeKey.value,
    platform: CNoticePlatformEnum.NOTICE_PLATFORM_1,
    locale: 'zh-CN',
  }
  queryParams.value.pageNum = 1
  handleTabChange(activeKey.value)
}
const goDetail = (item: any) => {
  router.push({ name: 'NoticeDetail', query: { noticeId: item.noticeId } })
}
</script>

<style scoped lang="scss">
.inform-list {
  margin: 0 3em;

  .message-card {
    background: rgba(211, 211, 211, 0.28);
    padding: 2px 24px;
    margin-bottom: 15px;
    border-radius: 12px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
    cursor: pointer;
    transition:
      transform 0.2s ease,
      box-shadow 0.2s ease;

    &:hover {
      transform: scale(1.03);
      box-shadow: 0 6px 20px rgba(28, 186, 24, 0.46);
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
}
</style>
