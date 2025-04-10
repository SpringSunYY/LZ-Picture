import { defineStore } from 'pinia'

export const useConfigStore = defineStore('config', {
  state: () => ({
    configs: {} as Record<string, string>, // 使用对象来存储配置
  }),
  actions: {
    // 获取配置
    getConfig(key: string): string | null {
      if (!key) {
        return null
      }
      return this.configs[key] || null
    },

    // 设置配置
    setConfig(key: string, value: string): void {
      if (key) {
        this.configs[key] = value
        // console.log(`设置配置信息：${key} = ${value}`)
      }
    },

    // 删除配置
    removeConfig(key: string): boolean {
      if (this.configs[key]) {
        delete this.configs[key]
        return true
      }
      return false
    },
  },
})
