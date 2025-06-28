<template>
  <div class="inform-list">
    <a-page-header
      title="我的消息"
      sub-title="要关心自己的消息哦"
    />

    <a-form layout="inline" :model="queryParams">
      <a-form-item label="消息标题">
        <a-input-search
          v-model:value="queryParams.informTitle"
          placeholder="搜索消息标题"
          @search="handleSearch"
          enter-button
          style="min-width: 200px"
        />
      </a-form-item>
      <a-form-item label="读取状态">
        <a-select
          v-model:value="queryParams.isRead"
          placeholder="读取状态"
          allow-clear
          @change="handleSearch"
          style="width: 180px"
        >
          <a-select-option
            v-for="item in u_inform_is_read"
            :key="item.dictValue"
            :value="item.dictValue"
          >
            {{ item.dictLabel }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="消息类型">
        <a-select
          v-model:value="queryParams.informType"
          placeholder="消息类型"
          allow-clear
          @change="handleSearch"
          style="width: 180px"
        >
          <a-select-option
            v-for="item in u_inform_type"
            :key="item.dictValue"
            :value="item.dictValue"
          >
            {{ item.dictLabel }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-button type="primary" @click="resetSearch">重置</a-button>
    </a-form>
    <div style="margin-bottom: 10px"></div>

    <a-tabs default-active-key="all" v-model:activeKey="activeKey" @change="handleTabChange">
      <a-tab-pane key="all" tab="全部" />
      <a-tab-pane key="1" tab="短信" />
      <a-tab-pane key="2" tab="邮件" />
      <a-tab-pane key="3" tab="站内通知" />
      <a-tab-pane key="4" tab="APP推送" />
      <a-tab-pane key="5" tab="微信" />
      <a-tab-pane key="0" tab="其他" />
    </a-tabs>

    <div v-for="item in dataList" :key="item.recordId" class="message-card" @click="goDetail(item)">
      <div class="card-top">
        <span class="type">{{ getCTemplateTypeLabel(item.templateType) }}</span>
        <span class="title">{{ item.informTitle }}</span>
      </div>
      <div class="card-bottom">
        <div class="right-meta">
          <span>
            <DictTag :value="item.informType" :options="u_inform_type" />
          </span>
          <span>
            <DictTag :value="item.isRead" :options="u_inform_is_read" />
          </span>
          <span>{{ item.sendTime }}</span>
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
      :show-total="total => `共 ${total} 条`"
      show-less-items
    />
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  getCTemplateTypeLabel,
  type InformInfoQuery,
  type InformInfoVo,
} from '@/types/user/informInfo.d.ts'
import { listInformInfo } from '@/api/user/inform.ts'
import DictTag from '@/components/DictTag.vue'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { u_inform_type, u_inform_is_read } = proxy?.useDict('u_inform_type', 'u_inform_is_read')

const router = useRouter()
const dataList = ref<InformInfoVo[]>([])
const loading = ref(false)
const total = ref(0)
const queryParams = ref<InformInfoQuery>({
  pageNum: 1,
  pageSize: 8,
  usageType: null,
})

const getDataList = () => {
  loading.value = true
  listInformInfo(queryParams.value).then((res) => {
    dataList.value = res?.rows || []
    total.value = res?.total || 0
    loading.value = false
  })
}
getDataList()

const activeKey = ref('all')

const onPageChange = (page: number) => {
  queryParams.value.pageNum = page
  getDataList()
}

const handleTabChange = (key: string) => {
  queryParams.value.pageNum = 1
  if (key === 'all') {
    queryParams.value.templateType = null
  } else {
    queryParams.value.templateType = key
  }
  getDataList()
}
const handleSearch = () => {
  queryParams.value.pageNum = 1
  queryParams.value.current = 1
  getDataList()
}

const resetSearch = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 8,
    templateType: activeKey.value,
  }
  queryParams.value.current = 1
  handleTabChange(activeKey.value)
}
const goDetail = (item: any) => {
  router.push({ name: 'informDetail', query: { recordId: item.recordId } })
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
