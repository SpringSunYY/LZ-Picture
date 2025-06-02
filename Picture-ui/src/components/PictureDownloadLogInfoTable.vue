<template>
  <div class="picture-download-log">
    <a-card title="我的下载记录" :bordered="false">
      <a-form layout="inline" :model="queryParams">
        <a-form-item>
          <a-input-search
            v-model:value="queryParams.pictureName"
            placeholder="图片名称"
            @search="handleSearch"
            enter-button
            style="min-width: 200px"
          />
        </a-form-item>
        <a-form-item>
          <a-select
            v-model:value="queryParams.downloadStatus"
            placeholder="下载状态"
            allow-clear
            @change="handleSearch"
            style="width: 150px"
          >
            <a-select-option
              v-for="item in p_download_status"
              :key="item.dictValue"
              :value="item.dictValue"
            >
              {{ item.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-select
            v-model:value="queryParams.downloadType"
            placeholder="下载方式"
            allow-clear
            @change="handleSearch"
            style="width: 150px"
          >
            <a-select-option
              v-for="item in p_download_type"
              :key="item.dictValue"
              :value="item.dictValue"
            >
              {{ item.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-select
            v-model:value="queryParams.referSource"
            placeholder="来源"
            allow-clear
            @change="handleSearch"
            style="width: 150px"
          >
            <a-select-option
              v-for="item in p_download_refer_source"
              :key="item.dictValue"
              :value="item.dictValue"
            >
              {{ item.dictLabel }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-range-picker
            v-model:value="dateRange"
            @change="handleSearch"
            format="YYYY-MM-DD"
            style="min-width: 250px"
          />
        </a-form-item>
        <a-button type="primary" @click="resetSearch">重置</a-button>
      </a-form>

      <a-table
        :columns="columns"
        :data-source="downloadList"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        row-key="downloadId"
        :scroll="{ x: 1000 }"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'thumbnailUrl'">
            <a-image v-if="text" :width="60" :src="text" :preview="{ src: text }" />
            <span v-else>-</span>
          </template>
          <template v-if="column.dataIndex === 'tags'">
            <tags
              :values="text?.split(';')"
              :colors="['pink', 'pink', 'orange', 'green', 'cyan', 'blue', 'purple']"
              v-if="text"
            />
            <span v-else>-</span>
          </template>
          <template v-if="column.dataIndex === 'downloadStatus'">
            <dict-tag :value="text" :options="p_download_status" />
          </template>
          <template v-if="column.dataIndex === 'downloadType'">
            <dict-tag :value="text" :options="p_download_type" />
          </template>
          <template v-if="column.dataIndex === 'referSource'">
            <dict-tag :value="text" :options="p_download_refer_source" />
          </template>
          <!-- 操作列 -->
          <template v-if="column.dataIndex === 'action'">
            <a-space>
              <a-button @click="viewDetail(record)">查看</a-button>
              <a-button :loading="downloadPictureLoading" @click="download(record)">下载</a-button>
              <a-divider type="vertical" />
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, onMounted, ref } from 'vue'
import { listPictureDownloadLogInfo } from '@/api/picture/pictureDownloadLogInfo'
import DictTag from '@/components/DictTag.vue'
import Tags from '@/components/Tags.vue'
import dayjs from 'dayjs'
import type {
  PictureDownloadLogInfoQuery,
  PictureDownloadLogInfoVo,
} from '@/types/picture/pictureDownloadLogInfo'
import { useRouter } from 'vue-router'
import { downloadImage, downloadImageByLog } from '@/utils/file.ts'
import { message } from 'ant-design-vue'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_download_status, p_download_type, p_download_refer_source } = proxy?.useDict(
  'p_download_status',
  'p_download_type',
  'p_download_refer_source',
)

const downloadList = ref<PictureDownloadLogInfoVo[]>([])
const loading = ref(false)
const dateRange = ref<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showTotal: (total: number) => `共 ${total} 条记录`,
  showSizeChanger: true,
  showQuickJumper: true,
})

const queryParams = ref<PictureDownloadLogInfoQuery>({
  pageNum: 1,
  pageSize: 10,
  isAsc: 'desc',
  pictureName: '',
  downloadStatus: null,
  downloadType: null,
  referSource: null,
  params: {},
})

const columns = [
  { title: '缩略图', dataIndex: 'thumbnailUrl' },
  { title: '图片名称', dataIndex: 'pictureName' },
  { title: '标签', dataIndex: 'tags' },
  { title: '消耗积分', dataIndex: 'pointsCost' },
  { title: '下载状态', dataIndex: 'downloadStatus' },
  { title: '下载方式', dataIndex: 'downloadType' },
  { title: '来源', dataIndex: 'referSource' },
  { title: '下载时间', dataIndex: 'createTime', width: 180, sorter: true },
  { title: '操作', dataIndex: 'action', fixed: 'right', width: 150 },
]

const getList = () => {
  loading.value = true
  queryParams.value.params = {}
  if (dateRange.value) {
    queryParams.value.params['beginCreateTime'] = dateRange.value[0]
      .format('YYYY-MM-DD')
      .concat(' 00:00:00')
    queryParams.value.params['endCreateTime'] = dateRange.value[1]
      .format('YYYY-MM-DD')
      .concat(' 23:59:59')
  }
  listPictureDownloadLogInfo(queryParams.value).then((res) => {
    downloadList.value = res?.rows || []
    pagination.value.total = res?.total || 0
    loading.value = false
  })
}

const handleSearch = () => {
  pagination.value.current = 1
  queryParams.value.pageNum = 1
  getList()
}

const resetSearch = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    isAsc: 'desc',
    pictureName: '',
    downloadStatus: null,
    downloadType: null,
    referSource: null,
    params: {},
  }
  dateRange.value = null
  pagination.value.current = 1
  getList()
}

const handleTableChange = (pag, _, sorter) => {
  pagination.value.current = pag.current
  pagination.value.pageSize = pag.pageSize
  queryParams.value.pageNum = pag.current
  queryParams.value.pageSize = pag.pageSize
  queryParams.value.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc'
  getList()
}

// 路由跳转
const router = useRouter()
// 查看详情
const viewDetail = (record: PictureDownloadLogInfoVo) => {
  //如果是图片
  const routeData = router.resolve({
    path: '/pictureDetail',
    query: { pictureId: record.pictureId },
  })
  window.open(routeData.href, '_blank')
}
const downloadPictureLoading = ref(false)
//下载
const download = async (record: PictureDownloadLogInfoVo) => {
  try {
    // console.log('下载', record)
    message.success('图片下载中...', 5)
    message.info('请不要刷新页面', 5)
    downloadPictureLoading.value = true
    const res = await downloadImageByLog(record.downloadId, record.pictureName)
  } catch (e) {
    console.log(e)
  } finally {
    downloadPictureLoading.value = false
  }
}
onMounted(getList)
</script>
