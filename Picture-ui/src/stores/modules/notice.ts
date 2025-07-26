import { defineStore } from 'pinia'
import { getNoticeInfoByExhibit } from '@/api/config/notice'
import type { NoticeInfoRequest, NoticeInfoVo } from '@/types/config/notice'

interface DailyNoticeCache {
  date: string
  noticeId: string
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

function saveCache(noticeId: string) {
  const cache: DailyNoticeCache = {
    date: getToday(),
    noticeId,
  }
  localStorage.setItem(CACHE_KEY, JSON.stringify(cache))
}

export const useNoticeStore = defineStore('noticeStore', {
  state: () => ({
    noticeLoaded: false,
    todayNotice: null as NoticeInfoVo | null,
    hasPushedToday: false, // 表示是否已经“手动确认过”
  }),

  actions: {
    /** 获取公告，用户是否已读，否则每次都会展示 */
    async ensureTodayNotice(params: NoticeInfoRequest): Promise<NoticeInfoVo | null> {
      if (this.noticeLoaded) {
        return this.todayNotice
      }

      const today = getToday()
      const cache = loadCache()

      // 请求后端
      const res = await getNoticeInfoByExhibit(params)

      if (res.code === 200 && res.data) {
        const notice = res.data

        // 判断是否今天已标记已读（根据 noticeId 和日期）
        if (cache?.date === today && cache.noticeId === notice.noticeId) {
          this.todayNotice = null
          this.hasPushedToday = true
        } else {
          this.todayNotice = notice
          this.hasPushedToday = false
        }
      } else {
        this.todayNotice = null
        this.hasPushedToday = true // 出错也不再重复提示
      }

      this.noticeLoaded = true
      return this.todayNotice
    },

    /** 标记已读 */
    markAsRead(notice: NoticeInfoVo) {
      saveCache(notice.noticeId)
      this.todayNotice = null
      this.hasPushedToday = true
      this.noticeLoaded = true
    },
  },
})
