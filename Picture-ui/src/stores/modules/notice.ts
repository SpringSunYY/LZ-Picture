import { defineStore } from 'pinia'
import { getNoticeInfoByExhibit } from '@/api/config/notice'
import type { NoticeInfoRequest, NoticeInfoVo } from '@/types/config/notice'

interface DailyNoticeCache {
  date: string
  noticeId: string
  hasPushedToday: boolean
}

const CACHE_KEY = 'DAILY_NOTICE_CACHE'

function getToday(): string {
  return new Date().toISOString().slice(0, 10)
}

function loadCache(): DailyNoticeCache | null {
  const str = localStorage.getItem(CACHE_KEY)
  if (!str) return null
  try {
    return JSON.parse(str) as DailyNoticeCache
  } catch {
    return null
  }
}

function saveCache(noticeId: string, hasPushedToday: boolean) {
  const cache: DailyNoticeCache = {
    date: getToday(),
    noticeId,
    hasPushedToday,
  }
  localStorage.setItem(CACHE_KEY, JSON.stringify(cache))
}

export const useNoticeStore = defineStore('noticeStore', {
  state: () => ({
    noticeLoaded: false,  // 初始化为 false，确保未加载时不会重复请求
    todayNotice: null as NoticeInfoVo | null,
    hasPushedToday: false, // 是否已推送公告
  }),

  actions: {
    /** 获取公告，用户是否已读，否则每次都会展示 */
    async ensureTodayNotice(params: NoticeInfoRequest): Promise<NoticeInfoVo | null> {
      const today = getToday()
      const cache = loadCache()

      // 如果缓存中有数据，且是今天的公告，并且已经推送过公告
      if (cache?.date === today && cache?.noticeId && cache.hasPushedToday) {
        this.todayNotice = null
        this.noticeLoaded = true
        this.hasPushedToday = true // 标记今天已经推送过公告
        return this.todayNotice
      }

      // 如果缓存无效或者今天没有推送过公告，则请求后端
      if (!this.noticeLoaded || !this.hasPushedToday) {
        const res = await getNoticeInfoByExhibit(params)

        if (res.code === 200 && res.data) {
          const notice = res.data

          // 更新缓存并推送公告
          this.todayNotice = notice
          this.hasPushedToday = false
          saveCache(notice.noticeId, this.hasPushedToday)
        } else {
          this.todayNotice = null
          this.hasPushedToday = true // 出错也不再重复提示
        }

        // 设置已加载状态
        this.noticeLoaded = true
      }

      return this.todayNotice
    },

    /** 标记已读 */
    markAsRead(notice: NoticeInfoVo) {
      saveCache(notice.noticeId, true)
      this.todayNotice = null
      this.hasPushedToday = true
      this.noticeLoaded = true
    },
  },
})
