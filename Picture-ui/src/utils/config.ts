import { useConfigStore } from '@/stores/modules/config.ts'
import { getConfig } from '@/api/common/common.ts'

/**
 * 获取字典数据
 */
export async function useConfig(key: string) {
  const configStore = useConfigStore()
  let res = ''
  const config = configStore.getConfig(key)
  if (config) {
    return config
  } else {
    const resp = await getConfig(key)
    if (resp?.data) {
      res = resp?.data?.configValue
      configStore.setConfig(key, res)
      return res
    } else {
      return ''
    }
  }
}
