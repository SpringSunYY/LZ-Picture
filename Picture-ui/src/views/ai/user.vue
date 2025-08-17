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
              <span class="count">{{ userInfo?.workCount || 0 }}</span>
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
        <TextView :text="userInfo?.introduction || '这个用户很懒，还没有介绍自己'" :max-lines="8" />
      </p>
      <div class="actions">
        <FollowButton class="follow-btn" />
        <ShareButton class="share-btn" />
      </div>
    </aside>

    <main class="main-content" ref="mainContentRef">
      <nav class="tabs" v-if="isSelf">
        <a href="#" class="tab-item active">已发布</a>
        <a href="#" class="tab-item">灵感</a>
        <a href="#" class="tab-item">AI图片</a>
      </nav>

      <AiVerticalFallLayout
        ref="aiVerticalFallLayoutRef"
        :loading="loading"
        @load-more="loadMore"
        :no-more="noMore"
        :picture-list="pictureList"
      />
      <BackToUp />
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import FollowButton from '@/components/button/FollowButton.vue'
import ShareButton from '@/components/button/ShareButton.vue'
import BackToUp from '@/components/BackToUp.vue'
import AiVerticalFallLayout from '@/components/AiVerticalFallLayout.vue'
import type { PictureInfoAiQuery, PictureInfoAiVo } from '@/types/picture/picture'
import { message } from 'ant-design-vue'
import { listMyAiPictureInfo } from '@/api/picture/picture.ts'
import useUserStore from '@/stores/modules/user.ts'
import { storeToRefs } from 'pinia'
import { useRoute } from 'vue-router'
import { getMyUserInfoByUserName } from '@/api/user/user.ts'
import type { MyUserInfo } from '@/types/user/user'
import { initCover } from '@/utils/common.ts'
import TextView from '@/components/TextView.vue'

// 用户信息相关逻辑
const route = useRoute()
const toUserName = ref((route.query.toUserName as string) || '')
const userStore = useUserStore()
const { userName: loginUserName } = storeToRefs(userStore)

// 判断当前页面是否为用户自己的主页
const isSelf = computed(() => {
  return loginUserName.value === toUserName.value || !toUserName.value
})

const userInfo = ref<MyUserInfo>()
const username = ref('')

/**
 * 根据路由参数获取用户信息
 */
const getUserInfo = async () => {
  username.value = isSelf.value ? loginUserName.value : toUserName.value
  const res = await getMyUserInfoByUserName(username.value)
  if (res.code === 200) {
    userInfo.value = res.data
  }
}
getUserInfo()

// 图片列表相关逻辑
const pictureQuery = ref<PictureInfoAiQuery>({
  pageNum: 1,
  pageSize: 35,
  name: '',
  pictureStatus: '',
})

const aiVerticalFallLayoutRef = ref()
const loading = ref(false)
const noMore = ref(false)
const pictureList = ref<PictureInfoAiVo[]>([])

/**
 * 加载更多图片，用于瀑布流组件的触底加载
 */
async function loadMore() {
  if (loading.value || noMore.value) return
  loading.value = true
  message.loading('正在为您获取图片推荐...', 1)
  try {
    const res = await listMyAiPictureInfo(pictureQuery.value)
    if (res?.rows) {
      pictureList.value = [...pictureList.value, ...res.rows]
      if (res.rows.length < pictureQuery.value.pageSize) {
        message.success('已为您获取全部图片推荐')
        noMore.value = true
      } else {
        pictureQuery.value.pageNum++
        message.success(`已为您推荐${res.rows.length}张图片`)
      }
    }
  } catch (error) {
    message.error('获取图片失败，请重试')
    console.error('Failed to load pictures:', error)
  } finally {
    loading.value = false
  }
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
  // 页面加载时立即加载第一批图片
  loadMore()
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
  padding: 20vh 20px;
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
