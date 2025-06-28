import { defineStore } from 'pinia'
import type { Dict } from '@/types/common'
import { toRaw } from 'vue'

interface DictItem {
  key: string
  value: Dict[]
}

export const useDictStore = defineStore('dict', {
  state: () => ({
    dict: [] as DictItem[],
  }),

  actions: {
    getDict(_key: string): Dict[] | null {
      if (!_key) return null
      const item = this.dict.find((item) => item.key === _key)
      return item ? toRaw(item.value) : null
    },

    setDict(_key: string, value: Dict[]): void {
      if (!_key) return
      const existingIndex = this.dict.findIndex((item) => item.key === _key)
      if (existingIndex !== -1) {
        this.dict[existingIndex].value = value // 覆盖
      } else {
        this.dict.push({ key: _key, value })
      }
    },

    removeDict(_key: string): boolean {
      const index = this.dict.findIndex((item) => item.key === _key)
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
