<template>
  <div class="picture-info-list">
    <!-- 图片网格列表 -->
    <a-row :gutter="[24, 24]">
      <a-col
        v-for="picture in pictureList"
        :key="picture.pictureId"
        :xs="24"
        :sm="12"
        :md="12"
        :lg="6"
      >
        <MasonryImage
          :src="picture?.thumbnailUrl"
          @click="goDetail(picture.pictureId)"
          :alt="picture.name"
          class="picture-card"
        >
          <div class="picture-info">
            <div>
              <a-space class="meta" :wrap="true" direction="horizontal">
                <h3 class="title">{{ picture.name }}</h3>
                <h3 class="title">{{ picture.picFormat }}</h3>
              </a-space>
            </div>
            <div>
              <a-space class="meta" :wrap="true" direction="horizontal">
                <span>{{ formatSize(picture.picSize) }}</span>
                <span>{{ picture.picWidth }} * {{ picture.picHeight }}</span>
                <a-divider type="vertical" />
                <dict-tag :options="p_picture_status" :value="picture.pictureStatus" />
                <a-button
                  v-if="
                    (checkUser(picture.userId) && checkPermiSingle('picture:upload')) ||
                    (checkSpaceEditor(picture.spaceId) && checkPermiSingle('picture:upload'))
                  "
                  style="pointer-events: auto"
                  type="primary"
                  size="small"
                  @click.stop="handleUpdate(picture.pictureId)"
                >
                  修改
                </a-button>
              </a-space>
            </div>
          </div>
        </MasonryImage>
      </a-col>
    </a-row>

    <div v-if="pictureTotal > 12" style="text-align: center; margin-top: 20px; margin-bottom: 10px">
      <a-pagination
        :current="pictureQuery.pageNum"
        :pageSize="pictureQuery.pageSize"
        show-quick-jumper
        :total="pictureTotal"
        :showTotal="
          (total: number) => {
            return `共 ${total} 条`
          }
        "
        :showSizeChanger="true"
        @change="onPageChange"
        :pageSizeOptions="['12', '24', '48', '96']"
      />
    </div>
  </div>
</template>
<script setup lang="ts">
import { checkPermiSingle, checkSpaceEditor, checkUser } from '@/utils/permission.ts'
import { getCurrentInstance, ref, watch, watchEffect } from 'vue'
import type { MyPictureInfoVo, PictureInfoQuery } from '@/types/picture/picture.d.ts'
import { listMyPictureInfo } from '@/api/picture/picture.ts'
import { formatSize } from '@/utils/common.ts'
import { useRouter } from 'vue-router'
import MasonryImage from '@/components/MasonryImage.vue'
import DictTag from '@/components/DictTag.vue'

const instance = getCurrentInstance()
const proxy = instance?.proxy
const { p_picture_status } = proxy?.useDict('p_picture_status')
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
const pictureTotal = ref(1)
// 路由跳转
const router = useRouter()
const goDetail = (pictureId: string) => {
  const routeData = router.resolve({
    path: '/pictureDetail',
    query: { pictureId },
  })
  window.open(routeData.href, '_blank')
}

const onPageChange = (page: number, pageSize: number) => {
  console.log('change 页码:', page, pageSize)
  pictureQuery.value.pageNum = page
  if (pageSize !== pictureQuery.value.pageSize) {
    pictureQuery.value.pageNum = 1
  }
  pictureQuery.value.pageSize = pageSize
  getPictureInfoList()
}

const getPictureInfoList = () => {
  if (props.currentParentId !== '') {
    pictureQuery.value.folderId = props.currentParentId
  }
  //如果当前为根目录
  if (props.currentParentId === '0') {
    pictureQuery.value.folderId = undefined
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
  const routeData = router.resolve({
    path: '/picture/pictureEdit',
    query: { pictureId: id },
  })
  window.open(routeData.href, '_blank')
}

watch(
  [() => props.currentParentId, () => props.spaceId],
  ([newCurrentParentId, newSpaceId], [oldCurrentParentId, oldSpaceId]) => {
    console.log('Prop changed:', { newCurrentParentId, newSpaceId, oldCurrentParentId, oldSpaceId })
    pictureQuery.value.pageNum = 1
    getPictureInfoList()
  },
)

getPictureInfoList()

// getPictureInfoList()
defineExpose({
  refreshData: getPictureInfoList,
})
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

    .picture-image {
      width: 100%;
      height: 160px;
      object-fit: cover;
    }

    .picture-info {
      padding: 16px;
      color: white;

      .title {
        margin: 0;
        font-size: 16px;
      }

      .meta {
        color: white;
        font-size: 12px;
      }
    }
  }
}
</style>
