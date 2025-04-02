import { ref, reactive, toRefs } from 'vue'
import { useDictStore } from '@/stores/modules/dict'
import { getDicts } from '@/api/common/common.ts'
import type { Dict } from '@/types/common'

/**
 * 获取字典数据
 */
export function useDict(...args: string[]) {
  const dictStore = useDictStore()
  const res = reactive<Record<string, Dict[]>>({})

  args.forEach((dictType) => {
    res[dictType] = []
    const dicts = dictStore.getDict(dictType)
    if (dicts) {
      res[dictType] = dicts
    } else {
      getDicts(dictType).then((resp) => {
        if (resp?.data) {
          res[dictType] = resp.data
          dictStore.setDict(dictType, res[dictType])
        }
      })
    }
  })

  return toRefs(res)
}
