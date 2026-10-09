import { reactive, toRefs, type Ref, type ToRefs } from 'vue'
import { useStore } from 'vuex'
import type { RootState } from '@/store/types'
import type { DictList } from '@/types/dict'

export type DictRefs = Record<string, Ref<DictList>>

/**
 * 获取字典数据
 * @param args 字典类型，可以传入多个
 * @returns 响应式字典 refs 集合
 */
export function useDict(...args: string[]): ToRefs<Record<string, DictList>> {
  const store = useStore<RootState>()
  const res = reactive<Record<string, DictList>>({})

  args.forEach((dictType) => {
    res[dictType] = []

    // 直接调用 action，action 内部会处理缓存逻辑
    void store.dispatch('getDict', dictType).then((data: unknown) => {
      if (data && Array.isArray(data)) {
        res[dictType] = data as DictList
      }
    })
  })

  return toRefs(res) as ToRefs<Record<string, DictList>>
}
