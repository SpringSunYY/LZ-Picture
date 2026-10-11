import { reactive, toRefs, type Ref, type ToRefs } from 'vue'
import { useStore } from 'vuex'
import { getDicts } from '@/api/common/common'
import type { RootState } from '@/store/types'
import {Dict} from "@/types/dict.t";

/**
 * 获取字典数据(对齐 web 端 useDict 风格)
 * 行为:
 * 1) 先从 vuex dict module 查缓存(state.dict, 数组 + 日期:key 隔离)
 * 2) 命中即同步返回;未命中调 getDicts(已剥外层包,直接拿数组)
 * 3) 拿到数组后写回缓存(供下次同步返回)
 *
 * 注意: getDicts 现在返回的是真正的 Dict[], 不是 {code,msg,data},
 * 不要再写 resp.data,那会拿到 undefined。
 */
export function useDict(...args: string[]): ToRefs<Record<string, Dict[]>> {
  const store = useStore<RootState>()
  const res = reactive<Record<string, Dict[]>>({})

  // getter 是参数化函数(getDict(dictType)),这里拿到的是工厂函数
  // (state) => (dictType) => DictList|null 的第二层
  type GetDictGetter = (dictType: string) => Dict[] | null
  const getDictGetter = store.getters.getDict as GetDictGetter

  args.forEach((dictType) => {
    res[dictType] = []

    // 1) 缓存命中(同步,与 web 端 dictStore.getDict 行为一致)
    const cached = getDictGetter(dictType)
    if (cached && Array.isArray(cached) && cached.length > 0) {
      res[dictType] = cached
      return
    }

    // 2) 缓存未命中,走接口
    getDicts(dictType)
      .then((data: Dict[]) => {
        if (Array.isArray(data) && data.length > 0) {
          res[dictType] = data
          // 写回 vuex 字典缓存(日期:key 隔离由 setDict 内部处理)
          store.dispatch('setDict', { key: dictType, value: data })
        }
      })
      .catch((err: unknown) => {
        console.error(`[useDict] 获取 ${dictType} 失败:`, err)
      })
  })

  return toRefs(res) as ToRefs<Record<string, Dict[]>>
}

/** 重导出,方便调用方单独声明类型 */
export type DictRefs = Record<string, Ref<Dict[]>>