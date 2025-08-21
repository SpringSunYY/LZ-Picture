import { defineStore } from 'pinia'
import type { Dict } from '@/types/common'
import { toRaw } from 'vue'
import { formatDateByDate } from '@/utils/common.ts'

interface DictItem {
  key: string
  value: Dict[]
}

export const useDictStore = defineStore('dict', {
  state: () => ({
    dict: [] as DictItem[],
  }),

  actions: {
    getDict(key: string): Dict[] | null {
      if (!key) return null
      const now = formatDateByDate(new Date())
      // console.log('当前时间：', now)
      key = now + ':' + key
      const item = this.dict.find((item) => item.key === key)
      return item ? toRaw(item.value) : null
    },

    setDict(key: string, value: Dict[]): void {
      if (!key) return
      const now = formatDateByDate(new Date())
      // console.log('当前时间：', now)
      key = now + ':' + key
      const existingIndex = this.dict.findIndex((item) => item.key === key)
      if (existingIndex !== -1) {
        this.dict[existingIndex].value = value // 覆盖
      } else {
        this.dict.push({ key: key, value })
      }
    },

    removeDict(key: string): boolean {
      const now = formatDateByDate(new Date())
      // console.log('当前时间：', now)
      key = now + ':' + key
      const index = this.dict.findIndex((item) => item.key === key)
      if (index !== -1) {
        this.dict.splice(index, 1)
        return true
      }
      return false
    },

    cleanDict(): void {
      this.dict = []
    },

    initDict(): void {
      // 可加入接口初始化逻辑
    },
  },

  persist: {
    storage: sessionStorage, // or localStorage
    serializer: {
      serialize: (state) => {
        return JSON.stringify(toRaw(state.dict))
      },
      deserialize: (str) => {
        try {
          const data = JSON.parse(str)
          return { dict: data as DictItem[] }
        } catch (e) {
          return { dict: [] }
        }
      },
    },
  },
})
