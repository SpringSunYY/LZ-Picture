import { defineStore } from 'pinia'
import { toRaw } from 'vue'
import { formatDateByDate, formatDateTimeByDate } from '@/utils/common.ts' // 引入 toRaw 来访问原始对象

interface ConfigStoreState {
  configs: Map<string, string>
}

export const useConfigStore = defineStore('config', {
  state: (): ConfigStoreState => ({
    configs: new Map(),
  }),

  actions: {
    getConfig(key: string): string | null {
      // console.group('[ConfigStore] getConfig调试')
      // console.log('传入的key:', key)
      if (!key) {
        // console.log('key为空，返回null')
        // console.groupEnd()
        return null
      }
      const now = formatDateByDate(new Date())
      // console.log('当前时间：', now)
      key = now + ':' + key
      // 使用 toRaw 获取原始 Map，避免响应式干扰
      const rawMap = toRaw(this.configs)
      const value = rawMap.get(key)

      // console.log(`获取的值[${key}]:`, value)
      // console.groupEnd()
      return value ?? null
    },

    setConfig(key: string, value: string): void {
      // console.group('[ConfigStore] setConfig调试')
      // console.log('设置前 configs 内容:', [...toRaw(this.configs).entries()])
      const now = formatDateByDate(new Date())
      // console.log('当前时间：', now)
      key = now + ':' + key
      if (key) {
        this.configs.set(key, value)
        // console.log('设置后 configs 内容:', [...toRaw(this.configs).entries()])
      }
      console.groupEnd()
    },
  },

  persist: {
    storage: sessionStorage, // 或 localStorage，根据你的需求选择
    serializer: {
      serialize: (state) => {
        // console.log('[持久化] 序列化数据:', [...state.configs.entries()])
        return JSON.stringify([...state.configs])
      },
      deserialize: (str) => {
        // console.log('[持久化] 反序列化数据:', str)
        try {
          const data = JSON.parse(str)
          return { configs: new Map(data) }
        } catch (e) {
          // console.error('反序列化失败:', e)
          return { configs: new Map() }
        }
      },
    },
  },
})
