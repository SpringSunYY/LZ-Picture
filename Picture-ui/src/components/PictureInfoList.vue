<template>
  <div class="picture-info-list">
    <!-- 图片网格列表 -->
    <a-row :gutter="[24, 24]">
      <a-col
        v-for="picture in pictureList"
        :key="picture.pictureId"
        :xs="24"
        :sm="6"
        :md="6"
        :lg="6"
      >
        <div class="picture-card">
          <div
            class="cover-image"
            :style="coverStyle(picture?.thumbnailUrl)"
            @click="goDetail(picture.pictureId)"
          ></div>
          <div class="picture-info">
            <h3 class="title">{{ picture.name }}</h3>
            <a-space class="meta" :wrap="true" direction="horizontal">
              <span>{{ formatSize(picture.picSize) }}</span>
              <a-divider type="vertical" />
              <Tags
                :values="[getPictureStatusLabel(picture.pictureStatus)]"
                :colors="['#0084ff']"
              />
              <Tags
                :values="[getPictureReviewStatusLabel(picture.reviewStatus)]"
                :colors="['#7300ff']"
              />

              <a-button
                v-if="checkUser(picture.userId) && checkPermiSingle('picture:upload')"
                style="float: right"
                type="primary"
                size="small"
                @click="handleUpdate(picture.pictureId)"
              >
                修改
              </a-button>
            </a-space>
          </div>
        </div>
      </a-col>
    </a-row>

    <div v-if="pictureTotal > 12" style="text-align: center; margin-top: 20px; margin-bottom: 10px">
      <a-pagination
        v-model:current="current"
        :pageSize="pictureQuery.pageSize"
        show-quick-jumper
        :total="pictureTotal"
        :showTotal="
          (total: number) => {
            return `共 ${total} 条`
          }
        "
        :showSizeChanger="true"
        @change="onChange"
        @showSizeChange="onShowSizeChange"
        :pageSizeOptions="['12', '24', '48', '96']"
      />
    </div>
  </div>
</template>
<script setup lang="ts">
import { checkPermiSingle, checkUser } from '@/utils/permission.ts'
import { ref, watchEffect } from 'vue'
import {
  getPictureReviewStatusLabel,
  getPictureStatusLabel,
  type MyPictureInfoVo,
  type PictureInfoQuery,
} from '@/types/picture/picture.d.ts'
import { listMyPictureInfo } from '@/api/picture/picture.ts'
import { formatSize } from '@/utils/common.ts'
import Tags from '@/components/Tags.vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  currentParentId: {
    type: String,
    default: '',
  },
  spaceId: {
    type: String,
    default: '',
  },
})

const pictureList = ref<MyPictureInfoVo[]>([]) // 图片
const pictureQuery = ref<PictureInfoQuery>({
  pageNum: 1,
  pageSize: 12,
})
const current = ref(1)
const pictureTotal = ref(1)
const pictureId = ref('')
// 路由跳转
const router = useRouter()
const goDetail = (pictureId: string) => {
  const routeData = router.resolve({
    path: '/pictureDetail',
    query: { pictureId },
  })
  window.open(routeData.href, '_blank')
}

// 分页器每页条数变化回调
const onShowSizeChange = (currentPage: number, size: number) => {
  pictureQuery.value.pageSize = size
  pictureQuery.value.pageNum = 1 // 切换条数后重置到第一页
  current.value = 1
  getSpaceInfoList()
}

// 修改现有 onChange 方法保持页码同步
const onChange = (pageNumber: number) => {
  current.value = pageNumber
  pictureQuery.value.pageNum = pageNumber
  getSpaceInfoList()
}

const getSpaceInfoList = () => {
  if (props.currentParentId !== '') {
    pictureQuery.value.folderId = props.currentParentId
  }
  if (props.spaceId !== '') {
    pictureQuery.value.spaceId = props.spaceId
  }
  listMyPictureInfo(pictureQuery.value).then((res) => {
    pictureList.value = res?.rows || []
    pictureTotal.value = res?.total || 0
    // console.log('pictureList', pictureList.value)
    // console.log('pictureTotal', pictureTotal.value)
  })
}

const handleUpdate = (id: string) => {
  pictureId.value = id
  const routeData = router.resolve({
    path: '/picture/pictureEdit',
    query: { pictureId: id },
  })
  console.log('routeData', routeData)
  window.open(routeData.href, '_blank')
}

watchEffect(() => {
  if (props.currentParentId) {
    getSpaceInfoList()
  }
  if (props.spaceId) {
    getSpaceInfoList()
  }
})
// 封面样式处理
const coverStyle = (url?: string) => ({
  backgroundImage: `url(${url || '/default-space-cover.jpg'})`,
})

getSpaceInfoList()
</script>

<style scoped lang="scss">
.picture-info-list {
  .picture-card {
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

    .picture-info {
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
