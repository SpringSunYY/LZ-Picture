<template>
  <view class="space-page" :style="rootStyleVar">
<!--    <view class="bg-blur bg-blur-primary"/>-->
<!--    <view class="bg-blur bg-blur-secondary"/>-->

    <main class="sp-main">
      <!-- ============================================================
           1. 云存储配额 (复用 components/CloudStorageBar.vue)
           ============================================================ -->
      <CloudStorageBar
          :used="summary.usedBytes"
          :total="summary.totalBytes"
          :subtitle="storageSubtitle"
          title="空间云存储"
          @expand="onOpenUpgrade"
          @clean="onOpenClean"
      />

      <!-- ============================================================
           2. 工作空间列表区 (Spaces Workspace Feed)
           ============================================================ -->
      <view class="sp-section">
        <view class="sp-section-head">
          <text class="sp-section-title">工作空间目录</text>
          <view class="sp-section-count">
            <text class="sp-section-count-text">{{ spaceList.length }}</text>
          </view>
          <text class="sp-section-hint">{{ sortTipText }}</text>
          <!-- 快捷工具入口:排序(与提示同处标题行) -->
          <view class="sp-btn-ghost" @tap="onOpenSort">
            <SvgIcon icon="sort" width="0.9em"/>
            <text class="sp-btn-ghost-text">排序</text>
          </view>
        </view>

        <!-- 加载中骨架 -->
        <view v-if="loading" class="sp-loading">
          <view v-for="i in 3" :key="i" class="sp-skeleton">
            <view class="sp-skeleton-body">
              <view class="sp-skeleton-line sp-skeleton-line-title"/>
              <view class="sp-skeleton-line sp-skeleton-line-meta"/>
            </view>
          </view>
        </view>

        <!-- 空状态 -->
        <view v-else-if="spaceList.length === 0" class="sp-empty" @tap="onCreate">
          <view class="sp-empty-icon-wrap">
            <SvgIcon icon="space" width="2em" class="sp-empty-icon"/>
          </view>
          <text class="sp-empty-title">还没有工作空间</text>
          <text class="sp-empty-sub">创建一个空间,把照片和素材整齐地收进来</text>
          <view class="sp-empty-btn">
            <SvgIcon icon="release" width="0.9em" class="sp-empty-btn-icon"/>
            <text class="sp-empty-btn-text">新建空间</text>
          </view>
        </view>

        <!-- 空间卡片列表 -->
        <view v-else class="sp-list">
          <view
              v-for="space in spaceList"
              :key="space.spaceId"
              class="sp-card"
              @tap="onEnterSpace(space)"
          >
            <!-- 封面作为整卡背景,信息浮于其上 -->
            <image
                v-if="space.spaceAvatar"
                class="sp-card-bg"
                :src="initCover(space.spaceAvatar)"
                mode="aspectFill"
            />
            <view v-else class="sp-card-bg sp-card-bg--empty">
              <SvgIcon icon="picture" width="2.4em" class="sp-card-bg-empty-icon"/>
            </view>

            <!-- 蒙版:整体压暗 + 底部加深 -->
            <view v-if="space.spaceAvatar" class="sp-card-scrim"/>
            <view v-if="space.spaceAvatar" class="sp-card-scrim-bottom"/>

            <!-- 顶部徽标 —— 状态/类型走后端字典(p_space_status / p_space_type) -->
            <view class="sp-card-badges">
              <DictTag dict-type="p_space_status" :value="space.spaceStatus"/>
              <DictTag dict-type="p_space_type" :value="space.spaceType"/>
            </view>

            <!-- 底部:标题 / 描述 / 元数据 -->
            <view class="sp-card-content">
              <text class="sp-card-title">
                {{ space.spaceName }}
              </text>
              <text class="sp-card-sub">{{ space.spaceDesc || defaultDesc(space) }}</text>

              <view class="sp-card-foot">
                <view class="sp-card-foot-left">
                  <text class="sp-card-foot-text">{{ space.totalCount || 0 }} 个文件</text>
                  <text class="sp-card-foot-dot">·</text>
                  <text class="sp-card-foot-text">{{ formatSize(space.totalSize || 0) }}</text>
                  <text class="sp-card-foot-dot">·</text>
                  <text class="sp-card-foot-text">{{ updateTimeText(space) }}</text>
                </view>
                <view class="sp-card-edit-mark" @tap.stop="onEdit(space)" v-if="canEdit(space)">
                  <text class="sp-card-enter-text">编辑</text>
                </view>
                <view class="sp-card-enter" @tap.stop="onEnterSpace(space)">
                  <text class="sp-card-enter-text">进入图库</text>
                  <SvgIcon icon="right" width="0.7em" class="sp-card-enter-arrow"/>
                </view>
              </view>
            </view>
          </view>
        </view>

        <!-- ============================================================
             3. 新建自定义工作空间引导条
             ============================================================ -->
        <button v-if="spaceList.length > 0" class="sp-create-bar" @tap="onCreate">
          <view class="sp-create-bar-inner">
            <SvgIcon icon="release" width="1.15em" class="sp-create-bar-icon"/>
            <text class="sp-create-bar-text">新建自定义工作空间</text>
          </view>
        </button>
      </view>
    </main>

    <!-- 排序方式选择 (ActionSheet) -->
    <up-action-sheet
        :show="sortSheetOpen"
        :actions="sortActions"
        title="排序方式"
        description="按你习惯的顺序浏览工作空间"
        cancel-text="取消"
        @select="onSelectSort"
        @close="sortSheetOpen = false"
    />

    <AppTabbar/>
  </view>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue'
import {onShow} from '@dcloudio/uni-app'
import {useStore} from 'vuex'
import AppTabbar from '@/components/AppTabbar.vue'
import CloudStorageBar from '@/components/CloudStorageBar.vue'
import DictTag from '@/components/DictTag.vue'
import SvgIcon from '@/components/SvgIcon.vue'
import {mySpaceInfo} from '@/api/picture/space'
import {formatSize, initCover} from '@/utils/common'
import {toast} from '@/utils/toast'
import {checkPermiSingle, checkUser} from '@/utils/permission'

defineOptions({
  name: "PictureSpace",
})


const store = useStore()


// ===================== 列表数据 =====================
const spaceList = ref([])
const loading = ref(false)
// 是否已经成功拉过至少一次 —— 决定再次进入时是显示骨架还是直接展示旧数据
const loaded = ref(false)

// userId 匹配 + 后端给了 picture:space:update 才显示修改入口
const canEdit = (space) =>
    !!space && checkUser(space.userId ?? "") && checkPermiSingle("picture:space:update")


// 描述为空时回退空串(让卡片副标题自然不显示),跟 web 端 defaultDesc 语义一致
const defaultDesc = (space) => space?.spaceDesc || ''

const updateTimeText = (space) => {
  const t = space?.lastUpdateTime || space?.updateTime || space?.createTime
  if (!t) return "刚刚创建"
  return String(t).slice(0, 10).replace(/-/g, ".")
}

// 云存储汇总 —— 容量按各空间 maxSize 汇总,文件数按 totalCount 汇总
const summary = computed(() => {
  let totalCount = 0
  let totalSize = 0
  let maxSize = 0
  let maxCount = 0
  spaceList.value.forEach((s) => {
    totalCount += Number(s.totalCount || 0)
    totalSize += Number(s.totalSize || 0)
    maxSize += Number(s.maxSize || 0)
    maxCount += Number(s.maxCount || 0)
  })
  return {
    totalCount,
    usedBytes: totalSize,
    // 后端没给配额时给 0,CloudStorageBar 会显示 0% 而不是 NaN
    totalBytes: maxSize,
    countLimitText: maxCount > 0 ? `配额 ${totalCount}/${maxCount} 个文件` : "未设置文件数配额",
  }
})

// CloudStorageBar 副标题 —— 左描述右操作,跟组件契约对齐
const storageSubtitle = computed(() => summary.value.countLimitText)

// ===================== 排序 =====================
const SORT_MODES = [
  {value: "recent", label: "最近更新"},
  {value: "count", label: "文件最多"},
  {value: "size", label: "容量最大"},
]
const sortMode = ref("recent")
const sortSheetOpen = ref(false)
const sortActions = SORT_MODES.map((m) => ({name: m.label}))


const sortTipText = computed(() => {
  if (sortMode.value === "count") return "按图片数量降序排列"
  if (sortMode.value === "size") return "按占用容量降序排列"
  return "按最后上传时间排列"
})

const onOpenSort = () => {
  sortSheetOpen.value = true
}

const onSelectSort = (item) => {
  const mode = SORT_MODES.find((m) => m.label === item?.name)
  if (!mode) return
  sortMode.value = mode.value
  sortSheetOpen.value = false
  applySort()
}

// 排序统一走这里,新增排序维度时只改这一个函数
const applySort = () => {
  const list = [...spaceList.value]
  const sortKey = {
    recent: (s) => String(s.lastUpdateTime || s.updateTime || s.createTime || ""),
    count: (s) => Number(s.totalCount || 0),
    size: (s) => Number(s.totalSize || 0),
  }[sortMode.value]
  list.sort((a, b) => (sortKey(a) < sortKey(b) ? 1 : sortKey(a) > sortKey(b) ? -1 : 0))
  spaceList.value = list
}

// ===================== 数据拉取 =====================
const fetchSpaceList = async () => {
  // 首次进入显示骨架;之后静默刷新,避免切 tab 回来闪一下空白
  if (!loaded.value) loading.value = true
  try {
    const res = await mySpaceInfo({})
    spaceList.value = res?.rows || []
    applySort()
    loaded.value = true
  } catch (e) {
    if (!loaded.value) spaceList.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchSpaceList()
})

// tab 页每次露出都同步一次列表
onShow(() => {
  if (loaded.value) fetchSpaceList()
})

// ===================== 进入图库 =====================
const onEnterSpace = (space) => {
  // TODO: spaceFolder(图库页)还没落地
  toast(`正在进入「${space.spaceName}」资产空间...`)
}

// ===================== 创建 / 修改 =====================
// 独立页面承载表单(edit.vue),本页只负责跳转
const SPACE_EDIT_URL = '/pages/picture/space/edit'

const onCreate = () => {
  uni.navigateTo({url: SPACE_EDIT_URL})
}

const onEdit = (space) => {
  if (!space?.spaceId) return
  uni.navigateTo({url: `${SPACE_EDIT_URL}?spaceId=${space.spaceId}`})
}

// ===================== 存储条操作 =====================
const onOpenUpgrade = () => toast("空间扩容套餐开发中,敬请期待")
const onOpenClean = () => toast("智能清理:扫描大文件与重复内容")
</script>

<style lang="scss" scoped>
/* ============================================================
 * 页面 token —— 与 system.vue / profile.vue / preferences.vue 完全同一套
 * 主颜色由 rootStyleVar 注入的 --app-* 解析,业务统一用 $app-* 变量
 * ============================================================ */
$surface-lowest: #ffffff;
$surface-low: #f2f3ff;
$surface: #faf8ff;
$surface-container: #eaedff;
$surface-container-high: #e2e7ff;
$on-surface: #131b2e;
$on-surface-variant: #464555;
$outline: #777587;
$primary-container: #4f46e5;
$sp-radius-lg: 32rpx;
$sp-radius-md: 24rpx;

button {
  background-color: transparent;
  padding: 0;
  margin: 0;
  font-size: 28rpx;
  line-height: normal;

  &::after {
    border: none;
  }
}

/* ========== 页面结构 ========== */
.space-page {
  position: relative;
  min-height: 100vh;
  padding: 24rpx 32rpx 220rpx;
  background-color: $surface;
  box-sizing: border-box;
  overflow: hidden;
}

/* ========== 背景装饰光斑 ========== */
.bg-blur {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  z-index: 0;

  &-primary {
    top: -160rpx;
    right: -120rpx;
    width: 480rpx;
    height: 480rpx;
    background-color: rgba($primary-container, 0.18);
    filter: blur(120rpx);
  }

  &-secondary {
    top: 420rpx;
    left: -140rpx;
    width: 380rpx;
    height: 380rpx;
    background-color: rgba($surface-container, 0.7);
    filter: blur(80rpx);
  }
}

/* 公共层级:内容都浮在 bg-blur 之上 */
.sp-main,
.sp-section,
.sp-create-bar {
  position: relative;
  z-index: 1;
}

.sp-main {
  display: flex;
  flex-direction: column;
}

/* ============================================================
   1. 快捷操作按钮 —— 排序胶囊(已并入工作空间目录标题行)
   ============================================================ */
.sp-btn-ghost {
  display: flex;
  align-items: center;
  margin-left: 16rpx;
  padding: 12rpx 24rpx;
  border-radius: 999rpx;
  background-color: $surface-container-high;
  transition: background-color 0.15s, transform 0.15s;

  &:active {
    transform: scale(0.96);
    background-color: $surface-container;
  }
}

.sp-btn-ghost-text {
  font-size: 24rpx;
  color: $on-surface;
  padding-left: 5rpx
}

/* ============================================================
   2. 空间列表区
   ============================================================ */
.sp-section {
  margin-top: 32rpx;
}

.sp-section-head {
  display: flex;
  align-items: center;
  padding: 0 8rpx;
}

.sp-section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: $on-surface;
  letter-spacing: -0.5rpx;
}

.sp-section-count {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-left: 12rpx;
  min-width: 36rpx;
  height: 36rpx;
  padding: 0 10rpx;
  border-radius: 999rpx;
  background-color: $surface-container;
}

.sp-section-count-text {
  font-size: 20rpx;
  font-weight: 700;
  color: $app-primary;
  font-family: "JetBrains Mono", monospace;
}

.sp-section-hint {
  margin-left: auto;
  font-size: 22rpx;
  color: $on-surface-variant;
}

.sp-list {
  display: flex;
  flex-direction: column;
  margin-top: 16rpx;
}

/* --- 空间卡片 --- 封面铺满,信息浮其上 --- */
.sp-card {
  position: relative;
  height: 320rpx;
  border-radius: $sp-radius-lg;
  background-color: $surface-container;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.04);
  box-sizing: border-box;
  overflow: hidden;
  transition: transform 0.15s, box-shadow 0.2s;

  &:active {
    transform: scale(0.99);
  }

  & + & {
    margin-top: 24rpx;
  }
}

.sp-card-bg {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
}

.sp-card-bg--empty {
  display: flex;
  align-items: center;
  justify-content: center;
}

.sp-card-bg-empty-icon {
  color: $outline;
}

.sp-card-scrim {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(19, 27, 46, 0.28);
}

.sp-card-scrim-bottom {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 75%;
  background: linear-gradient(to top, rgba(15, 21, 36, 0.88), rgba(15, 21, 36, 0));
}

.sp-card-badges {
  position: absolute;
  left: $sp-radius-md;
  top: $sp-radius-md;
  right: $sp-radius-md;
  display: flex;
  align-items: center;
  z-index: 2;
  gap: 12rpx;
}

.sp-card-content {
  position: absolute;
  left: $sp-radius-md;
  right: $sp-radius-md;
  bottom: $sp-radius-md;
  z-index: 2;
}

.sp-card-title {
  font-size: 32rpx;
  font-weight: 700;
  color: #ffffff;
  text-shadow: 0 1rpx 4rpx rgba(0, 0, 0, 0.35);
  letter-spacing: -0.5rpx;
  line-height: 1.3;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 标题末尾的「修改」内联小标签 —— 替代独立 button 元素 */
.sp-card-edit-mark {
  margin-left: 12rpx;
  padding: 2rpx 12rpx;
  border-radius: 999rpx;
  background-color: rgba(255, 255, 255, 0.22);
  border: 1rpx solid rgba(255, 255, 255, 0.4);
  font-size: 20rpx;
  font-weight: 500;
}

.sp-card-sub {
  display: block;
  margin-top: 6rpx;
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.78);
  text-shadow: 0 1rpx 4rpx rgba(0, 0, 0, 0.3);
  letter-spacing: 0.2rpx;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 字典徽标在深色封面上需要的不换行 */
.dict-tag-container {
  flex-wrap: nowrap;
  gap: 8rpx;
}

.sp-card-members {
  margin-left: 4rpx;
  font-size: 18rpx;
  color: rgba(255, 255, 255, 0.9);
  font-family: "JetBrains Mono", monospace;
}

.sp-card-foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 18rpx;
  padding-top: 16rpx;
  border-top: 1rpx solid rgba(255, 255, 255, 0.22);
}

.sp-card-foot-left {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.sp-card-foot-text {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.88);
  font-family: "JetBrains Mono", monospace;
  letter-spacing: -0.2rpx;
  line-height: 1.2;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sp-card-foot-dot {
  margin: 0 10rpx;
  font-size: 20rpx;
  color: rgba(255, 255, 255, 0.6);
}

.sp-card-enter {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  margin-left: 16rpx;
}

.sp-card-enter-text {
  font-size: 24rpx;
  font-weight: 500;
  color: #ffffff;
}

.sp-card-enter-arrow {
  margin-left: 4rpx;
  color: #ffffff;
}

/* --- 骨架屏 --- */
.sp-loading {
  display: flex;
  flex-direction: column;
  margin-top: 16rpx;
}

.sp-skeleton {
  position: relative;
  height: 320rpx;
  border-radius: $sp-radius-lg;
  background-color: $surface-container;
  overflow: hidden;

  & + & {
    margin-top: 24rpx;
  }
}

.sp-skeleton-body {
  position: absolute;
  left: $sp-radius-md;
  right: $sp-radius-md;
  bottom: $sp-radius-md;
  display: flex;
  flex-direction: column;
}

.sp-skeleton-line {
  height: 24rpx;
  border-radius: 8rpx;
  background-color: rgba(255, 255, 255, 0.5);

  &-title {
    width: 60%;
  }

  &-meta {
    width: 40%;
    margin-top: 14rpx;
  }
}

/* --- 空状态 --- */
.sp-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 16rpx;
  padding: 64rpx 32rpx;
  border-radius: $sp-radius-lg;
  background-color: $surface-lowest;
  border: 4rpx dashed $surface-container-high;
  box-sizing: border-box;
}

.sp-empty-icon-wrap {
  width: 112rpx;
  height: 112rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: $app-primary-light;
}

.sp-empty-icon {
  color: $app-primary;
}

.sp-empty-title {
  margin-top: 24rpx;
  font-size: 30rpx;
  font-weight: 600;
  color: $on-surface;
}

.sp-empty-sub {
  margin-top: 10rpx;
  font-size: 24rpx;
  color: $outline;
  line-height: 1.5;
  text-align: center;
}

.sp-empty-btn {
  display: flex;
  align-items: center;
  margin-top: 32rpx;
  padding: 14rpx 40rpx;
  border-radius: 999rpx;
  background-color: $primary-container;
  box-shadow: 0 6rpx 16rpx rgba(79, 70, 229, 0.26);
}

.sp-empty-btn-icon {
  margin-right: 8rpx;
}

.sp-empty-btn-text {
  font-size: 26rpx;
  font-weight: 600;
  color: #ffffff;
}

/* ============================================================
   3. 底部新建引导条
   ============================================================ */
.sp-create-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 32rpx;
  padding: 28rpx 32rpx;
  border-radius: $sp-radius-lg;
  background-color: $surface-lowest;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.04);
  transition: background-color 0.15s, transform 0.15s;

  &:active {
    transform: scale(0.99);
    background-color: $surface-low;
  }
}

.sp-create-bar-inner {
  display: flex;
  align-items: center;
  justify-content: center;
}

.sp-create-bar-icon {
  margin-right: 12rpx;
  color: $app-primary;
}

.sp-create-bar-text {
  font-size: 28rpx;
  font-weight: 500;
  color: $app-primary;
}
</style>