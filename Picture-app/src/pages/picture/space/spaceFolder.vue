<template>
  <view class="folder-page" :style="rootStyleVar">
    <main class="folder-main">
      <!-- ============================================================
           1. 空间核心名牌看板 (Space Header)
           ============================================================ -->
      <view class="space-hero">
        <view class="space-hero-top">
          <view class="space-hero-info">
            <view class="space-hero-tags">
              <DictTag dict-type="p_space_status" :value="spaceInfo?.spaceStatus"/>
              <DictTag dict-type="p_space_type" :value="spaceInfo?.spaceType"/>
              <view class="space-hero-tag space-hero-tag--meta">
                <text class="space-hero-tag-text">
                  {{ spaceInfo?.totalCount || 0 }} 项 · {{ formatSize(spaceInfo?.totalSize || 0) }}
                </text>
              </view>
            </view>
            <text class="space-hero-name">{{ spaceInfo?.spaceName || '我的工作空间' }}</text>
          </view>
          <view class="space-hero-actions">
            <button v-if="isTeamSpace&&checkSpaceCreator(spaceInfo?.userId)" class="space-hero-action-btn"
                  @tap="onCollab">
              <SvgIcon icon="addUser" width="0.9em"/>
            </button>
            <view class="space-hero-action-btn" v-if="checkSpaceCreator(spaceInfo?.userId)" @tap="onSpaceSettings">
              <SvgIcon icon="edit" width="0.9em"/>
            </view>
          </view>
        </view>

        <text class="space-hero-desc">{{ spaceDescription }}</text>

        <view v-if="spaceEditor" class="space-hero-buttons">
          <PrimaryLightButton class="space-hero-btn space-hero-btn--primary" @tap="onBatchUpload" text="批量上传" icon="release" width="1.5em"/>
          <button class="space-hero-btn space-hero-btn--secondary" @tap="onUpload">
            <SvgIcon icon="upload" width="1.5em" class="space-hero-btn-icon space-hero-btn-icon--primary"/>
            <text class="space-hero-btn-text">上传</text>
          </button>
        </view>
      </view>

      <!-- ============================================================
           3. 空间文件夹 (Space Folders) —— 横向卡片列表
           ============================================================ -->
      <view class="space-folders">
        <view class="space-folders-head">
          <view class="space-folders-head-left">
            <SvgIcon icon="folder" width="1.5em"/>
            <text class="space-folders-head-title">空间文件夹</text>
            <up-badge :value="folderList.length" type="primary" :absolute="false"/>
          </view>
          <button v-if="spaceEditor" class="space-folders-add" @tap="onCreateFolder">
            <SvgIcon icon="addFolder" width="1.2em" class="space-folders-add-icon"/>
            <text class="space-folders-add-text">新建文件夹</text>
          </button>
        </view>

        <!-- 面包屑（当处于子文件夹时显示） -->
        <scroll-view v-if="breadcrumbList.length > 0" class="folder-breadcrumb-scroll" scroll-x>
          <view class="folder-breadcrumb-inner">
            <view class="folder-breadcrumb-item" @tap="onBreadcrumbClick(-1)">
              <text class="folder-breadcrumb-text folder-breadcrumb-text--root">根目录</text>
            </view>
            <view
                v-for="(item, index) in breadcrumbList"
                :key="item.folderId"
                class="folder-breadcrumb-item"
                @tap="onBreadcrumbClick(index)"
            >
              <text class="folder-breadcrumb-sep">/</text>
              <text
                  class="folder-breadcrumb-text"
                  :class="{'folder-breadcrumb-text--active': index === breadcrumbList.length - 1}"
              >
                {{ item.folderName }}
              </text>
            </view>
          </view>
        </scroll-view>

        <!-- 加载中 -->
        <view v-if="loading" class="folder-loading">
          <view v-for="i in 3" :key="i" class="folder-skeleton-card"/>
        </view>

        <!-- 空状态 -->
        <view v-else-if="folderList.length === 0" class="folder-empty">
          <view class="folder-empty-icon-wrap">
            <SvgIcon icon="folder" width="2em"/>
          </view>
          <text class="folder-empty-title">暂无文件夹</text>
          <text class="folder-empty-sub">创建文件夹来分类整理你的图片素材</text>
          <button v-if="spaceEditor" class="folder-empty-btn" @tap="onCreateFolder">
            <SvgIcon icon="release" width="0.9em" class="folder-empty-btn-icon"/>
            <text class="folder-empty-btn-text">新建文件夹</text>
          </button>
        </view>

        <!-- 文件夹网格列表：一行两列，居中 -->
        <view v-else class="folder-grid">
          <view
              v-for="(folder, index) in folderList"
              :key="folder.folderId"
              class="folder-grid-item"
              :class="folderCardClass(index)"
              @tap="onEnterFolder(folder)"
          >
            <view class="folder-grid-item-top">
              <view class="folder-grid-item-icon">
                <SvgIcon icon="folder" width="3em"/>
              </view>
              <up-badge
                  v-if="folder.sortOrder"
                  :value="'P' + folder.sortOrder"
                  type="primary"
                  :offset="[-6, -6]"
              />
            </view>
            <view class="folder-grid-item-info">
              <text class="folder-grid-item-name">{{ folder.folderName }}</text>
              <text class="folder-grid-item-meta">
                {{ folder.totalCount || 0 }} 项 · {{ formatSize(folder.totalSize || 0) }}
              </text>
            </view>


            <!-- 可编辑可见 -->
            <view v-if="spaceEditor" class="folder-grid-item-actions" @tap.stop>
              <view class="folder-grid-item-action" @tap.stop="onEditFolder(folder)">
                <SvgIcon icon="edit" width="1em"/>
              </view>
              <view class="folder-grid-item-action folder-grid-item-action--delete"
                    @tap.stop="onDeleteFolder(folder)">
                <SvgIcon icon="delete" width="1em"/>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- ============================================================
           5. 当前文件夹下的图片(按日期分组的瀑布流)
           组件内部自带"空间图片"标题 + 主题色斜杠 + 分页/刷新
           父组件只需监听 clickPicture 与调用 refreshData()
           ============================================================ -->
      <view class="space-pictures">
        <DateHorizontalFallLayout
            ref="datePictureRef"
            :space-id="spaceId"
            :current-parent-id="currentParentId"
            @click-picture="onPictureClick"
        />
      </view>
    </main>

    <!-- ============================================================
         4. 新建/编辑文件夹表单弹窗
         ============================================================ -->
    <up-popup
        :show="formSheetOpen"
        mode="bottom"
        round="24rpx"
        :closeOnClickOverlay="true"
        @close="onFormClose"
    >
      <view class="sheet-title">{{ formTitle }}</view>
      <up-form
          class="form-section"
          labelWidth="100"
      >
        <!-- 文件夹名称 -->
        <up-form-item label="文件夹名称" borderBottom="false" labelPosition="top">
          <up-input
              v-model="formState.folderName"
              shape="circle"
              :maxlength="32"
              placeholder="请输入文件夹名称"
              clearable
          >
            <template #prefix>
              <SvgIcon icon="folder" width="2em"/>
            </template>
          </up-input>
        </up-form-item>

        <!-- 排序权重 -->
        <up-form-item label="排序权重" borderBottom="false" labelPosition="top">
          <up-input
              v-model="formState.sortOrder"
              shape="circle"
              type="number"
              placeholder="0-10，数字越小越靠前"
              :min="0"
              :max="10"
          >
            <template #prefix>
              <SvgIcon icon="sort" width="1.5em"/>
            </template>
          </up-input>
        </up-form-item>

        <!-- 备注 -->
        <up-form-item label="备注" borderBottom="false" labelPosition="top">
          <up-textarea
              v-model="formState.remark"
              :maxlength="512"
              placeholder="请输入备注（可选）"
              count
          />
        </up-form-item>

        <button class="primary-btn" @tap="onFormSubmit" :disabled="formLoading">
          <text class="primary-btn-text">{{ formLoading ? '提交中...' : '确定' }}</text>
        </button>
      </up-form>
    </up-popup>
  </view>
</template>

<script setup lang="ts">
import {computed, onMounted, reactive, ref} from 'vue'
import {onShow} from '@dcloudio/uni-app'
import DictTag from '@/components/DictTag.vue'
import SvgIcon from '@/components/SvgIcon.vue'
import {getSpaceInfo} from '@/api/picture/space'
import {
  addSpaceFolderInfo,
  deleteSpaceFolderInfo,
  getSpaceFolderInfo,
  listSpaceFolderInfo,
  updateSpaceFolderInfo,
} from '@/api/picture/spaceFolder'
import {showConfirm, toast, toastHide, toastLoading, toastWarn} from '@/utils/toast'
import {checkSpaceCreator, checkSpaceEditor, ensureSpacePermsLoaded} from '@/utils/permission'
import {formatSize} from '@/utils/common'
import type {Space} from '@/types/picture/space.d.ts'
import type {SpaceFolderInfo, SpaceFolderInfoQuery, SpaceFolderInfoVo} from '@/types/picture/spaceFolder'
import PrimaryLightButton from '@/components/button/PrimaryLightButton.vue'
import DateHorizontalFallLayout from '@/components/DateHorizontalFallLayout.vue'
import type {MyPictureInfoVo} from '@/types/picture/picture.d'

defineOptions({
  name: "PictureSpaceFolder",
})


/**
 * 顶部 tab 标题
 */
definePage({
  style: {
    navigationBarTitleText: '图片空间',
  },
})

// uni-app H5 端会把 route.query 注入为 page 组件的 fallthrough attribute，
// 未在 props 中声明时会触发：
//   "Extraneous non-props attributes (spaceId) were passed to component
//    but could not be automatically inherited because component renders
//    fragment or text root nodes."
// 这里显式声明 props 让 Vue 把它当作 prop 消费掉，警告自然消失。
// 真正的取值仍走下面的 getCurrentPages().options（与 prop 兼容，prop 仅作"占位声明"）。
defineProps<{ spaceId?: string | number }>()
// ===================== 路由参数 =====================
const spaceId = ref('')
const currentParentId = ref('0')

// 空间权限（模板用）
const spaceEditor = ref(false)

onMounted(async () => {
  // 从页面参数获取 spaceId
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1] as any
  const options = currentPage?.options || {}
  spaceId.value = options.spaceId || ''
  if (spaceId.value) {
    await ensureSpacePermsLoaded()
    spaceEditor.value = await checkSpaceEditor(spaceId.value)
    await loadSpaceInfo()
    await fetchFolderList()
  }
})

// ===================== 空间核心信息 (Space Header) =====================
const spaceInfo = ref<Space | null>(null)
const spaceDescription = computed(() => {
  return spaceInfo.value?.spaceDesc || '本空间用于收藏和整理您的图片素材。'
})

// 0个人 1团队 2官方
const isTeamSpace = computed(() => {
  return spaceInfo.value?.spaceType === '1'
})

const loadSpaceInfo = async () => {
  if (!spaceId.value) return
  try {
    const res = await getSpaceInfo(spaceId.value)
    spaceInfo.value = res?.data || null
  } catch (e) {
    console.error('加载空间信息失败', e)
  }
}

// ===================== 面包屑导航 =====================
interface BreadcrumbItem {
  folderId: string
  folderName: string
}

const breadcrumbList = ref<BreadcrumbItem[]>([])

// 点击面包屑跳转
const onBreadcrumbClick = (index: number) => {
  if (index === -1) {
    breadcrumbList.value = []
    currentParentId.value = '0'
  } else {
    breadcrumbList.value = breadcrumbList.value.slice(0, index + 1)
    currentParentId.value = breadcrumbList.value[index].folderId
  }
  fetchFolderList()
}


// ===================== 文件夹列表 =====================
const folderList = ref<SpaceFolderInfoVo[]>([])
const loading = ref(false)

// 获取文件夹列表
const fetchFolderList = async () => {
  if (!spaceId.value) return
  loading.value = true
  try {
    const params: SpaceFolderInfoQuery = {
      spaceId: spaceId.value,
      parentId: currentParentId.value,
    }
    const res = await listSpaceFolderInfo(params)
    folderList.value = res?.rows || []
  } catch (e) {
    console.error('获取文件夹列表失败', e)
    toast('获取文件夹列表失败')
  } finally {
    loading.value = false
  }
}


// 进入子文件夹
const onEnterFolder = (folder: SpaceFolderInfoVo) => {
  breadcrumbList.value.push({
    folderId: folder.folderId,
    folderName: folder.folderName,
  })
  currentParentId.value = folder.folderId
  fetchFolderList()
}

// ===================== 空间图片(DateHorizontalFallLayout) =====================
/**
 * 日期分组瀑布流组件引用 —— 组件内部自带数据加载/分页/刷新
 * 父组件只需要在以下场景主动调用 refreshData():
 *   - 新建/删除文件夹后
 *   - 上传完图片后
 *   - 其他可能影响当前文件夹图片列表的操作后
 */
const datePictureRef = ref<InstanceType<typeof DateHorizontalFallLayout> | null>(null)

/**
 * 点击图片:由父组件决定如何跳转(uni-app 没有 detail 页时,可在此处做 preview 或 toast)
 */
const onPictureClick = (item: MyPictureInfoVo) => {
  console.log('[spaceFolder] 点击图片:', item)
  // 暂未配置详情页,这里只给个轻提示
  toast(`图片: ${item.name}`)
}

// ===================== 新建/编辑文件夹弹窗 =====================
const formSheetOpen = ref(false)
const formTitle = ref('')
const editingFolderId = ref('')
const formLoading = ref(false)

const formState = reactive<SpaceFolderInfo>({
  folderId: '',
  spaceId: '',
  parentId: '0',
  folderName: '',
  // 初始 0 是合法值；用户清空 input 后 v-model 会被 v-model 同步成 ''，submit 时校验拒绝
  sortOrder: 0,
  remark: '',
})

const resetForm = () => {
  formState.folderId = ''
  formState.spaceId = spaceId.value
  formState.parentId = currentParentId.value
  formState.folderName = ''
  formState.sortOrder = 0
  formState.remark = ''
}

const onFormClose = () => {
  formSheetOpen.value = false
  resetForm()
}

const onCreateFolder = () => {
  resetForm()
  formTitle.value = '新建文件夹'
  editingFolderId.value = ''
  formSheetOpen.value = true
}

const onEditFolder = (folder: SpaceFolderInfoVo) => {
  editingFolderId.value = folder.folderId
  toastLoading('加载中...')
  getSpaceFolderInfo(folder.folderId)
      .then((res) => {
        if (res?.data) {
          Object.assign(formState, res.data)
          formTitle.value = '编辑文件夹'
          formSheetOpen.value = true
        }
      })
      .catch(() => {
        toast('加载文件夹信息失败')
      })
      .finally(() => {
        toastHide()
      })
}

/**
 * 手动校验表单（绕开 up-form validate 的 model 缓存问题：
 * up-form 在 init 时拍 model 字段快照，后续 reset 不重拍，
 * 会导致"必须重新输入一次才校验"。这里直接读 formState 实时值）。
 *
 * @returns { ok: true } | { ok: false, message: string }
 */
const validateForm = (): { ok: true } | { ok: false; message: string } => {
  const name = (formState.folderName || '').trim()
  if (!name) return {ok: false, message: '请输入文件夹名称'}
  if (name.length > 32) return {ok: false, message: '文件夹名称不能超过32个字符'}

  const raw = formState.sortOrder
  // 用户清空 number input 后，v-model 会被写成空串 ''；null/undefined 也视为未填
  if (raw === null || raw === undefined || raw === '') {
    return {ok: false, message: '请输入排序权重'}
  }
  const n = typeof raw === 'number' ? raw : Number(String(raw).trim())
  if (Number.isNaN(n) || !Number.isInteger(n) || n < 0 || n > 10) {
    return {ok: false, message: '排序权重必须为 0-10 之间的整数'}
  }
  // 同步写回 trim 后的名称 + number 化排序
  formState.folderName = name
  formState.sortOrder = n
  return {ok: true}
}

const onFormSubmit = () => {
  if (formLoading.value) return
  const result = validateForm()
  if (!result.ok) {
    toastWarn(result.message)
    return
  }
  doFormSubmit()
}

const doFormSubmit = async () => {
  formLoading.value = true
  formState.spaceId = spaceId.value
  formState.parentId = currentParentId.value
  // 提交前把字符串 sortOrder 强转成 number（validator 已保证合法）
  if (formState.sortOrder !== null && formState.sortOrder !== undefined && formState.sortOrder !== '') {
    formState.sortOrder = Number(String(formState.sortOrder).trim())
  }
  toastLoading('提交中...')
  try {
    if (editingFolderId.value) {
      const res = await updateSpaceFolderInfo(formState)
      toastHide()
      if (res?.code === 200) {
        toast('修改成功')
        onFormClose()
        fetchFolderList()
      } else {
        toast(res?.msg || '修改失败')
      }
    } else {
      const res = await addSpaceFolderInfo(formState)
      toastHide()
      if (res?.code === 200) {
        toast('创建成功')
        onFormClose()
        fetchFolderList()
      } else {
        toast(res?.msg || '创建失败')
      }
    }
  } catch (e) {
    toastHide()
    toast('操作失败')
    console.error(e)
  } finally {
    formLoading.value = false
  }
}

const onDeleteFolder = async (folder: SpaceFolderInfoVo) => {
  const res = await showConfirm(`确定要删除文件夹「${folder.folderName}」吗？`)
  if (!res.confirm) return

  try {
    const deleteRes = await deleteSpaceFolderInfo(folder.folderId)
    if (deleteRes?.code === 200) {
      toast('删除成功')
      fetchFolderList()
    } else {
      toast(deleteRes?.msg || '删除失败')
    }
  } catch (e) {
    toast('删除失败')
    console.error(e)
  }
}

// 顶部分类标签已移除 —— 后端暂无标签字典

// ===================== 空间 Hero 操作按钮 =====================
const SPACE_EDIT_URL = '/pages/picture/space/edit'

const onCollab = () => toast('协作者管理功能开发中')
const onSpaceSettings = () => {
  if (!spaceId.value) {
    toast('空间信息不完整')
    return
  }
  uni.navigateTo({url: `${SPACE_EDIT_URL}?spaceId=${spaceId.value}`})
}
const onBatchUpload = () => toast('批量上传功能开发中')
const onUpload = () => toast('上传功能开发中')

// ===================== 文件夹卡片样式辅助 =====================

const folderCardClass = (index: number) => {
  return `folder-grid-item--${index % 5}`
}

// ===================== 页面可见时刷新 =====================
onShow(() => {
  if (spaceId.value) {
    loadSpaceInfo()
    fetchFolderList()
    // 同步刷新图片瀑布流 —— 父组件只需调用,具体加载逻辑由组件自己处理
    datePictureRef.value?.refreshData()
  }
})
</script>

<style lang="scss" scoped>
/* ============================================================
 * 页面 token —— 与 space.vue 同一套
 * ============================================================ */
$surface-lowest: #ffffff;
$surface-low: #f2f3ff;
$surface: #faf8ff;
$surface-container: #eaedff;
$surface-container-high: #e2e7ff;
$surface-variant: #dae2fd;
$on-surface: #131b2e;
$on-surface-variant: #464555;
$outline: #777587;
$primary-container: #4f46e5;
$app-primary: var(--app-primary, $primary-container);
$app-primary-light: var(--app-primary-light, rgba($primary-container, 0.15));
$radius-lg: 32rpx;
$radius-md: 24rpx;
$radius-sm: 16rpx;

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
.folder-page {
  min-height: 100vh;
  padding-bottom: 200rpx;
  background-color: $surface;
  box-sizing: border-box;
}

.folder-main {
  padding: 24rpx;
  display: flex;
  flex-direction: column;
}

/* ========== 空间 Hero ========== */
.space-hero {
  padding: 32rpx 24rpx;
  border-radius: $radius-md;
  background: linear-gradient(135deg, $surface-container-high 0%, $surface-container 50%, $surface-variant 100%);
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.04);
  margin-bottom: 24rpx;
}

.space-hero-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16rpx;
  margin-bottom: 16rpx;
}

.space-hero-info {
  flex: 1;
  min-width: 0;
}

.space-hero-tags {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-bottom: 12rpx;
  flex-wrap: wrap;
}

.space-hero-tag {
  display: inline-flex;
  align-items: center;
  gap: 4rpx;
  padding: 4rpx 12rpx;
  border-radius: 999rpx;
  box-shadow: 0 2rpx 6rpx rgba(19, 27, 46, 0.04);
}

.space-hero-tag--meta {
  background-color: $surface-lowest;
}

.space-hero-tag--meta .space-hero-tag-text {
  color: $on-surface-variant;
}

.space-hero-tag-text {
  font-size: 20rpx;
  font-weight: 700;
  color: #ffffff;
  letter-spacing: 0.2rpx;
}

.space-hero-name {
  font-size: 32rpx;
  font-weight: 700;
  color: $on-surface;
  letter-spacing: -0.5rpx;
  line-height: 1.2;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.space-hero-actions {
  display: flex;
  align-items: center;
  gap: 12rpx;
  flex-shrink: 0;
}

.space-hero-action-btn {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background-color: $surface-lowest;
  color: $on-surface-variant;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2rpx 6rpx rgba(19, 27, 46, 0.06);
  transition: transform 0.15s;

  &:active {
    transform: scale(0.92);
  }
}

.space-hero-desc {
  display: block;
  font-size: 24rpx;
  color: $on-surface-variant;
  line-height: 1.5;
  margin-bottom: 24rpx;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.space-hero-buttons {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.space-hero-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  height: 72rpx;
  padding: 0 24rpx;
  border-radius: $radius-sm;
  font-size: 26rpx;
  font-weight: 500;
  transition: transform 0.15s;

  &:active {
    transform: scale(0.97);
  }

  &--primary {
    box-shadow: 0 2rpx 6rpx rgba(19, 27, 46, 0.04);
    flex: 1;
  }

  &--secondary {
    background-color: $surface-lowest;
    color: $on-surface;
    box-shadow: 0 2rpx 6rpx rgba(19, 27, 46, 0.04);
  }
}

/* ========== 空间文件夹区 ========== */
.space-folders {
  margin-bottom: 24rpx;
}

.space-folders-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8rpx 4rpx;
  margin-bottom: 16rpx;
}

.space-folders-head-left {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.space-folders-head-title {
  font-size: 30rpx;
  font-weight: 600;
  color: $on-surface;
  letter-spacing: -0.3rpx;
}

/* ========== 空间图片区(DateHorizontalFallLayout) ========== */
.space-pictures {
  margin-top: 8rpx;
}

.space-folders-add {
  display: inline-flex;
  align-items: center;
  gap: 6rpx;
  padding: 10rpx 20rpx;
  border-radius: 999rpx;
  background-color: $surface-container-high;
  transition: transform 0.15s, background-color 0.15s;

  &:active {
    transform: scale(0.95);
    background-color: $surface-container;
  }
}

.space-folders-add-text {
  font-size: 22rpx;
  font-weight: 600;
  color: $app-primary;
  letter-spacing: 0.2rpx;
}

/* ========== 面包屑 ========== */
.folder-breadcrumb-scroll {
  width: 100%;
  white-space: nowrap;
  margin-bottom: 16rpx;
}

.folder-breadcrumb-inner {
  display: inline-flex;
  align-items: center;
  padding: 4rpx 0;
}

.folder-breadcrumb-item {
  display: inline-flex;
  align-items: center;
  flex-shrink: 0;
}

.folder-breadcrumb-sep {
  margin: 0 8rpx;
  font-size: 22rpx;
  color: $outline;
}

.folder-breadcrumb-text {
  font-size: 24rpx;
  color: $on-surface-variant;
  max-width: 200rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;

  &--root {
    font-weight: 500;
    color: $app-primary;
  }

  &--active {
    font-weight: 600;
    color: $on-surface;
  }
}

/* ========== 骨架屏 ========== */
.folder-loading {
  display: flex;
  gap: 16rpx;
  padding: 8rpx 0;
}

.folder-skeleton-card {
  flex-shrink: 0;
  width: 320rpx;
  height: 200rpx;
  border-radius: $radius-md;
  background-color: $surface-container;
}

/* ========== 空状态 ========== */
.folder-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 64rpx 32rpx;
  border-radius: $radius-md;
  background-color: $surface-lowest;
  border: 2rpx dashed $surface-container-high;
  box-sizing: border-box;
}

.folder-empty-icon-wrap {
  width: 96rpx;
  height: 96rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: $app-primary-light;
}

.folder-empty-title {
  margin-top: 20rpx;
  font-size: 28rpx;
  font-weight: 600;
  color: $on-surface;
}

.folder-empty-sub {
  margin-top: 8rpx;
  font-size: 22rpx;
  color: $outline;
  text-align: center;
  line-height: 1.5;
}

.folder-empty-btn {
  display: flex;
  align-items: center;
  margin-top: 24rpx;
  padding: 12rpx 32rpx;
  border-radius: 999rpx;
  background-color: $primary-container;
  box-shadow: 0 4rpx 12rpx rgba(79, 70, 229, 0.26);
  transition: transform 0.15s;

  &:active {
    transform: scale(0.96);
  }
}

.folder-empty-btn-icon {
  margin-right: 6rpx;
  color: #ffffff;
}

.folder-empty-btn-text {
  font-size: 24rpx;
  font-weight: 600;
  color: #ffffff;
}

/* ========== 文件夹两列网格 ========== */
.folder-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16rpx;
  padding: 4rpx 0 8rpx;
}

.folder-grid-item {
  position: relative;
  width: 100%;
  padding: 20rpx;
  border-radius: $radius-md;
  background-color: $surface-lowest;
  box-shadow: 0 2rpx 8rpx rgba(19, 27, 46, 0.04);
  border: 2rpx solid $surface-container;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-height: 220rpx;
  transition: transform 0.15s, box-shadow 0.2s;

  &:active {
    transform: scale(0.97);
    box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.08);
  }
}

.folder-grid-item-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 16rpx;
}

.folder-grid-item-icon {
  width: 90rpx;
  height: 90rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: $app-primary-light;
  position: relative;
}

.folder-grid-item-info {
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}

.folder-grid-item-name {
  font-size: 28rpx;
  font-weight: 600;
  color: $on-surface;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.folder-grid-item-meta {
  font-size: 20rpx;
  color: $on-surface-variant;
  letter-spacing: -0.1rpx;
}

.folder-grid-item-actions {
  position: absolute;
  bottom: 16rpx;
  right: 12rpx;
  display: flex;
  align-items: center;
  gap: 8rpx;
  opacity: 1;
  transition: opacity 0.2s;
}

.folder-grid-item-action {
  width: 44rpx;
  height: 44rpx;
  border-radius: 50%;
  background-color: $surface-container;
  color: $on-surface-variant;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.15s;

  &:active {
    transform: scale(0.9);
  }

  &--delete {
    background-color: rgba(186, 26, 26, 0.1);
    color: #ba1a1a;
  }
}

/* ========== 表单弹窗 ========== */
.sheet-title {
  padding: 32rpx 32rpx 8rpx;
  font-size: 32rpx;
  font-weight: 700;
  color: $on-surface;
  text-align: center;
}

.form-section {
  padding: 16rpx 32rpx calc(32rpx + env(safe-area-inset-bottom, 0px));
  display: flex;
  flex-direction: column;
}

.primary-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 92rpx;
  margin-top: 32rpx;
  border-radius: $radius-md;
  background-color: $app-primary;
  box-shadow: 0 6rpx 16rpx rgba(79, 70, 229, 0.3);
  border: none;

  &::after {
    border: none;
  }

  &:active {
    transform: scale(0.99);
  }

  &[disabled] {
    opacity: 0.6;
  }
}

.primary-btn-text {
  font-size: 30rpx;
  font-weight: 600;
  color: #ffffff;
  line-height: 1;
}
</style>
