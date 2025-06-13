import { useConfigStore } from '@/stores/modules/config'
import { getConfig } from '@/api/common/common'

export async function useConfig(key: string): Promise<string> {
  const configStore = useConfigStore()
  const cached = configStore.getConfig(key)
  if (cached !== undefined && cached !== null) {
    return cached
  }
  console.log('useConfig', key)
  const resp = await getConfig(key)
  if (resp?.data?.configValue) {
    configStore.setConfig(key, resp.data.configValue)
    return resp.data.configValue
  }
  return ''
}
