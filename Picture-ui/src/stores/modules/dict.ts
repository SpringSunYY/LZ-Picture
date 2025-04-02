import { defineStore } from 'pinia'
import type { Dict } from '@/types/common'

interface DictItem {
  key: string
  value: Dict[]
}

export const useDictStore = defineStore('dict', {
  state: () => ({
    dict: [] as DictItem[],
  }),
  actions: {
    // 获取字典
    getDict(_key: string): Dict[] | null {
      if (!_key) {
        return null
      }
      return this.dict.find((item) => item.key === _key)?.value ?? null
    },

    // 设置字典
    setDict(_key: string, value: Dict[]): void {
      if (_key) {
        this.dict.push({ key: _key, value })
      }
    },

    // 删除字典
    removeDict(_key: string): boolean {
      const index = this.dict.findIndex((item) => item.key === _key)
      if (index !== -1) {
        this.dict.splice(index, 1)
        return true
      }
      return false
    },

    // 清空字典
    cleanDict(): void {
      this.dict = []
    },

    // 初始字典
    initDict(): void {
      // 这里可以加初始化逻辑
    },
  },
})
