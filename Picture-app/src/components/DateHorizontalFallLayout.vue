<template>
  <view class="date-horizontal-fall-layout">
    <!-- 顶部区:图标 + 标题"空间图片" + 主题色长斜杠 (可通过 showHeader 关闭) -->
    <view v-if="showHeader" class="dhf-header">
      <view class="dhf-header-left">
        <SvgIcon icon="picture" width="1.5em" class="dhf-header-icon"/>
        <text class="dhf-header-title">{{ headerTitle }}</text>
      </view>
      <!-- 长斜杠:用主题色渐变(左实右虚),增加层次感 -->
      <view class="dhf-header-divider"/>
    </view>

    <!-- 加载中:首次加载时显示骨架占位 -->
    <view v-if="loading && pictureGroups.length === 0" class="initial-loading">
      <LoadingData/>
    </view>

    <!-- 空数据 -->
    <view v-else-if="pictureGroups.length <= 0" class="empty-text">
      <NoMoreData/>
    </view>

    <!-- 按日期分组的图片列表 -->
    <template v-else>
      <view
          v-for="group in pictureGroups"
          :key="group.date"
          class="date-group"
      >
        <!-- 日期标题(类似相册的时间标志) -->
        <view class="date-header">
          <view class="date-header-line"/>
          <text class="date-header-text">{{ group.dateLabel }}</text>
          <view class="date-header-line"/>
          <text class="date-header-count">{{ group.items.length }} 张</text>
        </view>

        <!-- 当前日期分组下的瀑布流 -->
        <view class="horizontal-masonry">
          <view
              class="masonry-row"
              v-for="(row, rowIndex) in group.rows"
              :key="`${group.date}-${rowIndex}`"
          >
            <view
                v-for="item in row"
                :key="`${item.pictureId}-${group.date}-${rowIndex}`"
                class="masonry-item"
                :style="{ width: `${item.displayWidth}px`, height: `${item.displayHeight}px` }"
                @click="handlePicture(item)"
            >
              <MasonryImage :src="item.thumbnailUrl" :alt="item.name">
                <view class="masonry-item-content">
                  <view class="masonry-item-title">
                    {{ item.name }}
                  </view>
                </view>
              </MasonryImage>
            </view>
          </view>
        </view>
      </view>
    </template>

    <!-- 触底加载更多 -->
    <view ref="loadMoreTrigger" class="load-more-trigger">
      <LoadingData v-if="loading && pictureGroups.length > 0"/>
      <NoMoreData v-else-if="noMore && pictureGroups.length > 0"/>
    </view>
  </view>
</template>

<script setup lang="ts">
import {nextTick, onActivated, onBeforeUnmount, onMounted, ref, watch} from 'vue'
import MasonryImage from '@/components/MasonryImage.vue'
import NoMoreData from '@/components/NoMoreData.vue'
import LoadingData from '@/components/LoadingData.vue'
import SvgIcon from '@/components/SvgIcon.vue'
import {listMyPictureInfo} from '@/api/picture/picture'
import type {MyPictureInfoVo, PictureInfoQuery} from '@/types/picture/picture.d'
import {formatDateByDate} from '@/utils/common'
import type {API} from '@/types/common'

/**
 * 单条瀑布流行内的图片(在原始 VO 上扩展显示宽高)
 */
interface DisplayPicture extends MyPictureInfoVo {
  /** 渲染到瀑布流上使用的宽度(单位 px) */
  displayWidth: number
  /** 渲染到瀑布流上使用的高度(单位 px) */
  displayHeight: number
}

/**
 * 按日期分组后的瀑布流数据
 */
interface PictureDateGroup {
  /** 日期 yyyy-MM-dd */
  date: string
  /** 友好的日期标签(今天/昨天/具体日期) */
  dateLabel: string
  /** 当前分组下的所有图片(原始顺序) */
  items: DisplayPicture[]
  /** 当前分组下按瀑布流分行后的渲染数据 */
  rows: DisplayPicture[][]
}

/**
 * 组件 props —— 与 PC 端 PictureInfoList 保持语义一致
 */
const props = withDefaults(
    defineProps<{
      /** 空间编号;留空时组件内部自动取 "我的空间" 的第一个作为默认 */
      spaceId?: string
      /** 父文件夹 ID('0' 表示根目录);留空时默认根目录 */
      currentParentId?: string
      /** 每页大小 */
      pageSize?: number
      /** 瀑布流基准高度(像素),与原组件语义保持一致 */
      itemHeight?: number
      /** 是否显示顶部"空间图片"标题(带主题色长斜杠) */
      showHeader?: boolean
      /** 顶部标题文案 */
      headerTitle?: string
    }>(),
    {
      spaceId: '',
      currentParentId: '',
      pageSize: 24,
      itemHeight: 250,
      showHeader: true,
      headerTitle: '空间图片',
    },
)

/**
 * 事件 —— 点击图片交给父组件处理(父组件决定跳转行为)
 * 父组件只需要监听 clickPicture 即可,无需感知加载/分页细节
 */
const emit = defineEmits<{
  (e: 'clickPicture', item: MyPictureInfoVo): void
}>()

// ===================== 数据状态 =====================
/** 累计加载到的全部图片(扁平) */
const allPictureList = ref<MyPictureInfoVo[]>([])
/** 按日期分组后的渲染数据(每组内部已经排好瀑布流行) */
const pictureGroups = ref<PictureDateGroup[]>([])

/** 加载状态 */
const loading = ref(false)
/** 是否已加载完所有数据 */
const noMore = ref(false)
/** 当前页码 */
const pageNum = ref(1)
/** 触底加载的 DOM 引用 */
const loadMoreTrigger = ref<HTMLElement | null>(null)

/** IntersectionObserver 实例 */
let observer: IntersectionObserver | null = null

// ===================== 工具:获取容器宽度(跨 H5/小程序) =====================
/**
 * 取当前瀑布流容器的可视宽度(像素)
 * H5 走 DOM clientWidth;小程序/App 走系统信息 windowWidth
 */
const getContainerWidth = (): number => {
  const baseHeight = props.itemHeight

  if (typeof document !== 'undefined') {
    const container = document.querySelector('.date-horizontal-fall-layout .horizontal-masonry') as HTMLElement | null
    if (!container) return baseHeight * 2
    return container.clientWidth
  }

  if (typeof uni !== 'undefined') {
    try {
      // #ifdef MP-WEIXIN
      // 微信小程序优先用 getWindowInfo,避免 getSystemInfoSync 弃用警告
      // 这里通过 any 规避 wx 全局未声明类型的问题
      const wxGlobal = (globalThis as unknown as { wx?: { getWindowInfo?: () => { windowWidth: number } } }).wx
      if (wxGlobal && typeof wxGlobal.getWindowInfo === 'function') {
        const info = wxGlobal.getWindowInfo()
        return info.windowWidth
      }
      // #endif
      if (typeof uni.getSystemInfoSync === 'function') {
        const sys = uni.getSystemInfoSync()
        return sys.windowWidth || baseHeight * 2
      }
    } catch (e) {
      // 取不到系统信息时给一个保底值
    }
  }
  return baseHeight * 2
}

// ===================== 工具:日期相关 =====================
/**
 * 将 yyyy-MM-dd HH:mm:ss 这类字符串安全转成 Date
 * 兼容 'YYYY-MM-DD HH:mm:ss' 和 ISO 'YYYY-MM-DDTHH:mm:ss'
 */
const toDate = (raw: string | undefined | null): Date | null => {
  if (!raw) return null
  const normalized = raw.replace(/-/g, '/').replace('T', ' ').replace(/\..*$/, '')
  const d = new Date(normalized)
  return isNaN(d.getTime()) ? null : d
}

/**
 * 与传入日期对比,返回 "今天" / "昨天" / "YYYY-MM-DD" / "YYYY-MM-DD (周X)"
 */
const formatDateLabel = (dateStr: string): string => {
  const d = toDate(dateStr)
  if (!d) return dateStr

  const today = new Date()
  const sameDay = (a: Date, b: Date) =>
      a.getFullYear() === b.getFullYear() &&
      a.getMonth() === b.getMonth() &&
      a.getDate() === b.getDate()

  if (sameDay(d, today)) return '今天'

  const yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000)
  if (sameDay(d, yesterday)) return '昨天'

  // 当年只显示 MM-DD,跨年显示 YYYY-MM-DD
  if (d.getFullYear() === today.getFullYear()) {
    const mm = String(d.getMonth() + 1).padStart(2, '0')
    const dd = String(d.getDate()).padStart(2, '0')
    const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
    return `${mm}-${dd} ${weekdays[d.getDay()]}`
  }
  return formatDateByDate(d)
}

/**
 * 取出 yyyy-MM-dd 用于分组 key
 */
const getDateKey = (dateStr: string): string => {
  const t = toDate(dateStr)
  return t ? formatDateByDate(t) : dateStr
}

// ===================== 瀑布流分行算法 =====================
/**
 * 在固定的容器宽度内,把一组图片按等比缩放分行 —— 直接返回二维数组
 * - 同一行内图片按比例平摊到容器宽度
 * - 当加入新图会导致行高 < baseHeight 时,落定该行并开启新行
 * - 不足一行的尾部,使用 baseHeight 作为行高
 *
 * 关键:每次落定一行/收尾一行都 push 一个新 row,返回结构是 [row1, row2, ...]
 */
const splitIntoRows = (items: MyPictureInfoVo[], containerWidth: number): DisplayPicture[][] => {
  const baseHeight = props.itemHeight
  const spacing = 6
  const minWidth = 150

  const rows: DisplayPicture[][] = []
  let tempRow: MyPictureInfoVo[] = []
  let totalRatio = 0

  // 收尾/落定一行的辅助函数
  const flushRow = (rowItems: MyPictureInfoVo[], height: number) => {
    const row: DisplayPicture[] = rowItems.map((p) => {
      const r = p.picWidth / p.picHeight
      return {
        ...p,
        displayWidth: r * height,
        displayHeight: height,
      }
    })
    rows.push(row)
  }

  for (const pic of items) {
    if (!pic.picWidth || !pic.picHeight) continue
    const r = pic.picWidth / pic.picHeight
    const estimatedWidth = r * baseHeight
    if (estimatedWidth < minWidth) continue

    tempRow.push(pic)
    totalRatio += r

    const totalSpacing = spacing * (tempRow.length - 1)
    const targetWidth = containerWidth - totalSpacing
    const currentHeight = totalRatio > 0 ? targetWidth / totalRatio : baseHeight

    if (currentHeight < baseHeight) {
      // 落定这一行(用 currentHeight 作为该行高度)
      flushRow(tempRow, currentHeight)
      tempRow = []
      totalRatio = 0
    }
  }

  // 收尾的不足一行,直接按 baseHeight 平铺
  if (tempRow.length > 0) {
    flushRow(tempRow, baseHeight)
  }

  return rows
}

// ===================== 按日期分组 =====================
/**
 * 把扁平图片按 createTime 日期分组
 * 每个日期组内部直接由 splitIntoRows 排好瀑布流(已经是二维行结构)
 *
 * 注意:只保留有图(rows.length > 0)的分组,过滤掉当天全被尺寸过滤掉的空组
 */
const buildGroups = (allItems: MyPictureInfoVo[], containerWidth: number): PictureDateGroup[] => {
  const map = new Map<string, MyPictureInfoVo[]>()
  for (const pic of allItems) {
    const key = getDateKey(pic.createTime)
    if (!map.has(key)) map.set(key, [])
    map.get(key)!.push(pic)
  }

  // 按日期倒序(新的在上,符合相册习惯)
  const sortedKeys = Array.from(map.keys()).sort((a, b) => {
    const ta = toDate(a)?.getTime() ?? 0
    const tb = toDate(b)?.getTime() ?? 0
    return tb - ta
  })

  const groups: PictureDateGroup[] = []
  for (const key of sortedKeys) {
    const items = map.get(key)!
    const rows = splitIntoRows(items, containerWidth)
    // 过滤掉"全部图都被尺寸过滤掉"的空日期组
    if (rows.length === 0) continue

    // 把所有图片拍平用于"X 张"计数显示
    const flatItems: DisplayPicture[] = rows.flat()

    groups.push({
      date: key,
      dateLabel: formatDateLabel(key),
      items: flatItems,
      rows,
    })
  }
  return groups
}

// ===================== 数据加载 =====================
/**
 * 拉取当前页数据,追加到 allPictureList
 * 当 total 已加载完时不再请求
 *
 * 父组件不传 spaceId / folderId 时直接传 undefined,由后端按登录态过滤
 */
const fetchPageData = async (): Promise<void> => {
  if (loading.value || noMore.value) return
  loading.value = true
  try {
    const params: PictureInfoQuery = {
      pageNum: pageNum.value,
      pageSize: props.pageSize,
      spaceId: props.spaceId || undefined,
      folderId: props.currentParentId === '0' ? undefined : props.currentParentId,
    }
    const res: API.ResponseInfo<MyPictureInfoVo> = await listMyPictureInfo(params)
    const rows = res?.rows || []
    if (rows.length > 0) {
      allPictureList.value = [...allPictureList.value, ...rows]
      pageNum.value += 1
    }
    // total 兜底:有些接口只返回 rows 没 total,按 rows.length < pageSize 判定触底
    const total = res?.total
    if (typeof total === 'number') {
      noMore.value = allPictureList.value.length >= total
    } else {
      noMore.value = rows.length < props.pageSize
    }
  } catch (e) {
    console.error('[DateHorizontalFallLayout] 加载图片失败:', e)
  } finally {
    loading.value = false
    renderGroups()
  }
}

/**
 * 触底触发的加载 —— 行为由 IntersectionObserver 内部驱动
 */
const handleLoadMore = () => {
  if (loading.value || noMore.value) return
  fetchPageData()
}

/**
 * 重新计算分组并刷新视图(在数据变化或窗口尺寸变化时调用)
 */
const renderGroups = () => {
  const containerWidth = getContainerWidth()
  pictureGroups.value = buildGroups(allPictureList.value, containerWidth)
}

// ===================== 重置与刷新 =====================
/**
 * 清空当前数据 + 重置分页
 * 用于 spaceId / folderId 切换时,丢弃上一页的数据
 */
const resetAndReload = async (): Promise<void> => {
  allPictureList.value = []
  pictureGroups.value = []
  pageNum.value = 1
  noMore.value = false
  loading.value = false
  // 等一帧 DOM 更新再加载,避免老数据闪现
  await Promise.resolve()
  await fetchPageData()
}

/**
 * 暴露给父组件的刷新方法 —— 父组件无需关心分页/数据,直接调用即可
 * 用法:
 *   pictureDateRef.value?.refreshData()
 */
const refreshData = async (): Promise<void> => {
  await resetAndReload()
}

defineExpose({
  refreshData,
  clearData: () => {
    allPictureList.value = []
    pictureGroups.value = []
    pageNum.value = 1
    noMore.value = false
  },
})

// ===================== 事件 =====================
const handlePicture = (item: MyPictureInfoVo) => {
  emit('clickPicture', item)
}

// ===================== IntersectionObserver 触底加载 =====================
/**
 * 校验传入节点是否为可被 IntersectionObserver 接受的 Element
 * (uni-app H5/小程序 模板 ref 在 setup 阶段拿到的可能是组件实例,不是 DOM 节点)
 */
const isElement = (node: unknown): node is Element => {
  return !!node && typeof node === 'object' && 'nodeType' in (node as object) && (node as { nodeType: number }).nodeType === 1
}

const setupObserver = () => {
  if (observer && observer.disconnect) observer.disconnect()
  if (typeof IntersectionObserver === 'undefined') return

  // ref 还没拿到,或拿到的不是 Element(可能是组件实例/文本节点),跳过本次
  if (!isElement(loadMoreTrigger.value)) return

  observer = new IntersectionObserver(
      (entries) => {
        const entry = entries[0]
        if (entry && entry.isIntersecting) {
          handleLoadMore()
        }
      },
      {rootMargin: '10px'},
  )
  observer.observe(loadMoreTrigger.value)
}

// 窗口尺寸变化时重新布局(横竖屏切换、分屏等)
const handleResize = () => {
  renderGroups()
}

/**
 * scroll 兜底方案:当滚动到距离底部 < 100px 时触发加载
 *
 * 为什么需要兜底:
 *   1. uni-app H5 端 <view> 编译为 <div>,IntersectionObserver 通常工作
 *   2. 但小程序/App 端模板 ref 可能拿到组件实例而非 Element,observer 不触发
 *   3. 首屏数据少时,如果用户没滚动到触发元素位置,observer 也不会触发
 *   4. scroll 事件是各端通用 API,作为兜底更可靠
 */
const SCROLL_BOTTOM_THRESHOLD = 100 // 距底 100px 视为触底

const handleScroll = () => {
  if (loading.value || noMore.value) return

  const scrollTop
      = window.pageYOffset
        || document.documentElement.scrollTop
        || document.body.scrollTop
        || 0
  const windowHeight = window.innerHeight || document.documentElement.clientHeight || 0
  const documentHeight
      = document.documentElement.scrollHeight
        || document.body.scrollHeight
        || 0

  if (scrollTop + windowHeight >= documentHeight - SCROLL_BOTTOM_THRESHOLD) {
    handleLoadMore()
  }
}

// ===================== 生命周期 =====================
// 1. 监听 loadMoreTrigger ref —— 一旦 ref 拿到真实 DOM,立即挂载 observer
//    (uni-app H5 同步 onMounted 通常已可拿到,但小程序编译产物下 <view> 可能是组件实例,
//     用 watch + flush: 'post' 可以确保 ref 真正可用后再挂 observer)
watch(
    loadMoreTrigger,
    (el) => {
      if (isElement(el)) {
        setupObserver()
      }
    },
    {flush: 'post'},
)

onMounted(() => {
  if (typeof window !== 'undefined') {
    window.addEventListener('resize', handleResize)
    // 兜底:scroll 事件监听触底(H5/小程序都能用)
    // IntersectionObserver 在某些情况下不触发,scroll 是更可靠的备选
    window.addEventListener('scroll', handleScroll, {passive: true})
  }
  // 首次拉数据(observer 会在 ref 真正可用时通过上面 watch 自动挂载)
  fetchPageData()
  // 兜底:如果 watch 没触发(极少见),用 nextTick 再试一次
  nextTick(() => {
    if (!observer) setupObserver()
  })
})

// uni-app KeepAlive 缓存激活时重挂 observer
onActivated(() => {
  nextTick(() => {
    setupObserver()
  })
})

// 监听 spaceId / currentParentId 变化,自动重置并重新加载 —— 类似 PC 端的 watch
watch(
    () => [props.spaceId, props.currentParentId],
    async ([newSpaceId, newFolderId], [oldSpaceId, oldFolderId]) => {
      // 首次挂载时 onMounted 已经拉过,这里跳过
      if (newSpaceId === oldSpaceId && newFolderId === oldFolderId) return
      await resetAndReload()
    },
)

onBeforeUnmount(() => {
  if (observer && observer.disconnect) observer.disconnect()
  if (typeof window !== 'undefined') {
    window.removeEventListener('resize', handleResize)
    window.removeEventListener('scroll', handleScroll)
  }
})
</script>

<style scoped lang="scss">
.date-horizontal-fall-layout {
  padding: 8px;
  margin: 0 auto;

  // 顶部"空间图片"标题区 —— 图标 + 标题 + 主题色长斜杠
  .dhf-header {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px 4px 10px;

    .dhf-header-left {
      display: flex;
      align-items: center;
      gap: 6px;
      flex-shrink: 0;
    }

    .dhf-header-icon {
      // 图标颜色跟随主题色(读 CSS 变量,未注入时用 #4f46e5 兜底)
      color: var(--app-primary, #4f46e5);
    }

    .dhf-header-title {
      font-size: 30rpx;
      font-weight: 600;
      color: var(--app-on-surface, #131b2e);
      letter-spacing: -0.5rpx;
      white-space: nowrap;
    }

    // 长斜杠:从主题色渐变到透明,营造"指向更多"的视觉
    .dhf-header-divider {
      flex: 1;
      height: 2px;
      border-radius: 2px;
      background: linear-gradient(
          to right,
          var(--app-primary, #4f46e5) 0%,
          var(--app-primary, #4f46e5) 35%,
          rgba(79, 70, 229, 0.35) 70%,
          transparent 100%
      );
    }
  }

  .initial-loading {
    width: 100%;
    text-align: center;
    padding: 32px 0;
    color: #888;
  }

  .empty-text {
    width: 100%;
    text-align: center;
    padding: 32px 0;
  }

  .date-group {
    margin-bottom: 16px;

    // 相册式日期标题:左右短横线 + 日期 + 张数
    .date-header {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 12px 4px 8px;

      .date-header-line {
        flex: 1;
        height: 1px;
        background: linear-gradient(to right, transparent, #d6d6e3, transparent);
      }

      .date-header-text {
        font-size: 14px;
        font-weight: 600;
        color: #464555;
        letter-spacing: 0.5px;
        white-space: nowrap;
      }

      .date-header-count {
        font-size: 12px;
        color: #888;
        white-space: nowrap;
        margin-left: 4px;
      }
    }
  }

  .horizontal-masonry {
    width: 100%;
  }

  .masonry-row {
    display: flex;
    gap: 6px;
    margin-bottom: 6px;
  }

  .masonry-item {
    border-radius: 12px;
    overflow: hidden;
    transition: all 0.3s;

    .masonry-item-content {
      width: 100%;
      height: 100%;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: flex-end;

      .masonry-item-title {
        font-size: 18px;
        margin-bottom: 0;
      }
    }
  }

  .load-more-trigger {
    width: 100%;
    text-align: center;
    padding: 16px 0;
    color: #888;
  }
}
</style>