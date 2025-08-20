<template>
  <div class="user-profile-page">
    <aside class="user-info-section">
      <div class="user-info-container">
        <div class="avatar">
          <img :src="initCover(userInfo?.avatarUrl || '')" alt="avatar" />
        </div>
        <div class="user-details">
          <h2 class="username">{{ userInfo?.nickName || '' }}</h2>
          <h2 class="username text-gray-400 text-xl">{{ userInfo?.userName || '' }}</h2>
          <div class="stats">
            <div class="stat-item">
              <span class="label">{{ userInfo?.ipAddress || '未知' }}</span>
            </div>
            <div class="stat-item">
              <span class="label">{{ userInfo?.occupation || '用户不愿透露哦' }}</span>
            </div>
            <div class="stat-item">
              <span class="label">{{ userInfo?.birthday || '2000-01-01' }}</span>
            </div>
            <div class="stat-item">
              <span class="label"
                >{{
                  userInfo?.sex
                    ? u_user_sex.find((item) => item.dictValue === userInfo?.sex).dictLabel
                    : '未知'
                }}
              </span>
            </div>
          </div>
          <div class="stats">
            <div class="stat-item">
              <span class="count">{{ userInfo?.pictureCount || 0 }}</span>
              <span class="label">作品</span>
            </div>
            <div class="stat-item">
              <span class="count">{{ userInfo?.likeCount || 0 }}</span>
              <span class="label">喜欢</span>
            </div>
            <div class="stat-item">
              <span class="count">{{ userInfo?.collectCount || 0 }}</span>
              <span class="label">收藏</span>
            </div>
            <div class="stat-item">
              <span class="count">{{ userInfo?.shareCount || 0 }}</span>
              <span class="label">分享</span>
            </div>
          </div>
        </div>
      </div>
      <p class="bio">
        <TextView
          :text="userInfo?.introductory || '这个用户很懒，还没有介绍自己哦'"
          :max-lines="8"
        />
      </p>
      <div class="actions">
        <FollowButton class="follow-btn" />
        <ShareButton @click="handleShareUser('3')" class="share-btn" />
      </div>
    </aside>

    <main class="main-content" ref="mainContentRef">
      <nav class="tabs" v-if="isSelf">
        <a
          v-for="dict in p_picture_status"
          :key="dict.dictValue"
          href="#"
          @click.prevent="handleTabClick(dict.dictValue)"
          :class="['tab-item', { active: dict.dictValue === pictureQuery.pictureStatus }]"
          >{{ dict.dictLabel }}</a
        >
      </nav>

      <VerticalFallLayout
        v-show="pictureQuery.pictureStatus === '0'"
        ref="aiVerticalFallLayoutRef0"
        :loading="tabData['0'].loading"
        @load-more="loadMore"
        :no-more="tabData['0'].noMore"
        :picture-list="tabData['0'].pictureList"
        @handle-picture="handlePicture"
        :min-width="300"
      />
      <VerticalFallLayout
        v-show="pictureQuery.pictureStatus === '1'"
        ref="aiVerticalFallLayoutRef1"
        :loading="tabData['1'].loading"
        @load-more="loadMore"
        :no-more="tabData['1'].noMore"
        :picture-list="tabData['1'].pictureList"
        @handle-picture="handlePicture"
        :min-width="300"
      />
    </main>

    <a-modal v-model:open="openShare" title="分享主页" @ok="openShare = !openShare">
      <QRCode :value="shareLink" />
      <QuickCopy :content="shareLink" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { computed, getCurrentInstance, onMounted, onUnmounted, ref } from 'vue'
import FollowButton from '@/components/button/FollowButton.vue'
import ShareButton from '@/components/button/ShareButton.vue'
import type { PictureInfoAiQuery, PictureInfoAiVo, PictureInfoVo } from '@/types/picture/picture'
import { message } from 'ant-design-vue'
import { listAiPictureInfo } from '@/api/picture/picture.ts'
import useUserStore from '@/stores/modules/user.ts'
import { storeToRefs } from 'pinia'
import { useRoute, useRouter } from 'vue-router'
import { initCover } from '@/utils/common.ts'
import TextView from '@/components/TextView.vue'
import { getPictureUserInfoByUsername } from '@/api/picture/userinfo.ts'
import type { UserInfoVo } from '@/types/picture/userinfo'
import VerticalFallLayout from '@/components/VerticalFallLayout.vue'
import { useUserBehavior } from '@/utils/useUserBehavior.ts'
import QuickCopy from '@/components/QuickCopy.vue'
import QRCode from '@/components/QRCode.vue'

const { proxy } = getCurrentInstance()!
const { p_picture_status, u_user_sex } = proxy?.useDict('p_picture_status', 'u_user_sex')

//region 用户信息相关逻辑
const route = useRoute()
const toUserName = ref((route.query.username as string) || '')
const userStore = useUserStore()
const { userName: loginUserName } = storeToRefs(userStore)

// 判断当前页面是否为用户自己的主页
const isSelf = computed(() => {
  return loginUserName.value === toUserName.value || !toUserName.value
})

const userInfo = ref<UserInfoVo>()
const username = ref('')

/**
 * 根据路由参数获取用户信息
 */
const getUserInfo = async () => {
  username.value = isSelf.value ? loginUserName.value : toUserName.value
  const res = await getPictureUserInfoByUsername(username.value)
  if (res.code === 200) {
    userInfo.value = res.data
  }
}
getUserInfo()
//endregion
// region图片列表相关逻辑
const pictureQuery = ref<PictureInfoAiQuery>({
  pageNum: 1,
  pageSize: 35,
  name: '',
  pictureStatus: '0',
})

// 为每个状态维护独立的数据
const tabData = ref({
  '0': {
    pictureList: [] as PictureInfoAiVo[],
    loading: false,
    noMore: false,
    pictureQuery: {
      pageNum: 1,
      pageSize: 35,
      name: '',
      pictureStatus: '0',
      username: username.value,
    },
  },
  '1': {
    pictureList: [] as PictureInfoAiVo[],
    loading: false,
    noMore: false,
    pictureQuery: {
      pageNum: 1,
      pageSize: 35,
      name: '',
      pictureStatus: '1',
      username: username.value,
    },
  },
})

const aiVerticalFallLayoutRef0 = ref()
const aiVerticalFallLayoutRef1 = ref()

const handleTabClick = (status: string) => {
  // 如果点击的是当前已选中的状态，不执行任何操作
  if (pictureQuery.value.pictureStatus === status) {
    return
  }

  // 更新当前选中的状态
  pictureQuery.value.pictureStatus = status

  // 初始化该tab的数据（如果需要）
  initTabData(status)
}

/**
 * 初始化指定tab的数据
 */
async function initTabData(status: string) {
  const targetTab = tabData.value[status]

  // 如果该tab已经有数据、正在加载或已经没有更多数据，则不执行任何操作
  if (targetTab.pictureList.length > 0 || targetTab.loading || targetTab.noMore) {
    return
  }

  // 设置为加载状态
  targetTab.loading = true
  message.loading('正在为您获取图片推荐...', 1)

  try {
    // 重置查询参数
    targetTab.pictureQuery.pageNum = 1
    targetTab.pictureQuery.pictureStatus = status

    const res = await listAiPictureInfo(targetTab.pictureQuery)
    if (res?.rows) {
      targetTab.pictureList = res.rows

      if (res.rows.length < targetTab.pictureQuery.pageSize) {
        targetTab.noMore = true
      } else {
        targetTab.pictureQuery.pageNum++
      }

      message.success(`已为您推荐${res.rows.length}张图片`)
    }
  } catch (error) {
    message.error('获取图片失败，请重试')
    console.error('Failed to load pictures:', error)
  } finally {
    targetTab.loading = false
  }
}

/**
 * 加载更多图片，用于瀑布流组件的触底加载
 */
async function loadMore() {
  const currentStatus = pictureQuery.value.pictureStatus
  const currentTab = tabData.value[currentStatus]

  if (currentTab.loading || currentTab.noMore) return

  currentTab.loading = true

  try {
    const res = await listAiPictureInfo(currentTab.pictureQuery)
    if (res?.rows) {
      // 确保第一页时替换数据，后续页时追加数据
      if (currentTab.pictureQuery.pageNum === 1) {
        currentTab.pictureList = res.rows
      } else {
        currentTab.pictureList = [...currentTab.pictureList, ...res.rows]
      }

      if (res.rows.length < currentTab.pictureQuery.pageSize) {
        message.success('已为您获取全部图片推荐')
        currentTab.noMore = true
      } else {
        currentTab.pictureQuery.pageNum++
        message.success(`已为您推荐${res.rows.length}张图片`)
      }
    }
  } catch (error) {
    message.error('获取图片失败，请重试')
    console.error('Failed to load pictures:', error)
  } finally {
    currentTab.loading = false
  }
}

//endregion
const router = useRouter()
const handlePicture = (item: PictureInfoVo) => {
  console.log('handleToPicture', item.pictureId)
  router.push({
    name: 'aiDetail',
    query: {
      pictureId: item.pictureId,
    },
  })
}
//分享主页
const shareLink = ref('')
const openShare = ref(false)
const handleShareUser = () => {
  shareLink.value = window.location.href
  openShare.value = true
}
// 移动端检测逻辑（不再用于显示/隐藏元素，仅用于样式判断）
const isMobile = ref(window.innerWidth <= 768)

/**
 * 监听窗口大小变化，更新移动端状态
 */
const handleResize = () => {
  isMobile.value = window.innerWidth <= 768
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
  // 页面加载时初始化默认tab的数据
  initTabData('0')
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style lang="scss" scoped>
.user-profile-page {
  display: flex;
  background-color: #1a1a1a;
  color: #f0f0f0;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans',
    sans-serif;
  height: 100vh;
  overflow: hidden;
  justify-content: center;

  // 桌面端布局：侧边栏 + 主内容
  flex-direction: row;
}

// 统一的用户信息部分
.user-info-section {
  flex: 0 0 320px;
  padding: 15vh 20px;
  box-sizing: border-box;
  height: 100vh;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  padding-top: 20px;

  .tabs {
    background-color: #1a1a1a;
    z-index: 10;
    padding: 20px;
    display: flex;
    justify-content: center;
    gap: 24px;
    border-bottom: 1px solid #333;
    flex-shrink: 0;
  }
}

.user-info-container {
  width: 100%;
  margin-bottom: 5px;
  text-align: center;

  .avatar {
    width: 200px;
    height: 200px;
    border-radius: 50%;
    overflow: hidden;
    margin: 0 auto 16px auto;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .username {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 8px;
  }

  .stats {
    display: flex;
    justify-content: center;
    gap: 24px;
    margin-bottom: 16px;

    .stat-item {
      display: flex;
      flex-direction: column;
      align-items: center;

      .count {
        font-size: 18px;
        font-weight: bold;
      }

      .label {
        font-size: 12px;
        color: #a0a0a0;
      }
    }
  }

  .bio {
    font-size: 14px;
    color: #a0a0a0;
    margin-top: 20px;
    text-align: center;
    padding: 0 10px;
  }
}

.actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.follow-btn,
.share-btn {
  min-width: 100px;
  padding: 8px 16px;
  border: none;
  cursor: pointer;

  &:last-child {
    background-color: #333;
    color: #f0f0f0;
  }
}

.tab-item {
  font-size: 18px;
  color: #a0a0a0;
  text-decoration: none;
  padding-bottom: 8px;
  position: relative;

  &.active {
    color: #f0f0f0;
    font-weight: bold;

    &::after {
      content: '';
      position: absolute;
      left: 0;
      bottom: 0;
      width: 100%;
      height: 2px;
      background-color: #f0f0f0;
    }
  }
}

// 手机端样式调整
@media (max-width: 768px) {
  .user-profile-page {
    flex-direction: column;
    height: auto;
    overflow: visible;
  }

  .user-info-section {
    width: 100%;
    padding: 20px;
    flex-direction: column;
    align-items: center;
    text-align: center;
    height: auto;
    overflow-y: visible; // 移除滚动
  }

  .user-info-container {
    .avatar {
      width: 80px;
      height: 80px;
    }

    .username {
      font-size: 20px;
    }

    .stats {
      gap: 16px;
      margin-bottom: 10px;
    }

    .stat-item {
      .count {
        font-size: 16px;
      }

      .label {
        font-size: 11px;
      }
    }

    .bio {
      margin-top: 10px;
      padding: 0 20px;
      text-align: center;
    }
  }

  .actions {
    width: 100%;
    justify-content: center;
    margin-top: 10px;
  }

  .main-content {
    margin: 0 4px;
    margin-left: 0;
    overflow-y: visible;
    padding-top: 0;
  }
}
</style>
