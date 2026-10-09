import { getDicts } from '@/api/common/common'
import { formatDateByDate } from '@/utils/common'
import type { Module, ActionContext } from 'vuex'
import type { DictCache, DictList } from '@/types/dict'
import type { DictState, RootState } from './types'

/** 生成带日期的 key */
const getDictKey = (key: string): string | null => {
  if (!key) return null
  const now = formatDateByDate(new Date())
  return `${now}:${key}`
}

/** 从本地存储加载字典数据 */
const loadDictFromStorage = (): DictCache => {
  try {
    const data = uni.getStorageSync('dict_cache')
    return data ? JSON.parse(data) : {}
  } catch (e) {
    console.error('加载字典缓存失败:', e)
    return {}
  }
}

interface DictGetters {
  getDict: (key: string) => DictList | null
}

const dict: Module<DictState, RootState> = {
  namespaced: false,

  state: (): DictState => ({
    dict: loadDictFromStorage(),
  }),

  mutations: {
    SET_DICT: (state, payload: { key: string; value: DictList }) => {
      state.dict[payload.key] = payload.value
      // 同时保存到本地存储
      try {
        uni.setStorageSync('dict_cache', JSON.stringify(state.dict))
      } catch (e) {
        console.error('保存字典缓存失败:', e)
      }
    },
  },

  getters: {
    getDict: (state: DictState) => (key: string): DictList | null => {
      if (!key) return null
      return state.dict[key] || null
    },
  },

  actions: {
    // 获取字典数据（先查缓存，没有就从接口获取）
    async getDict(
      ctx: ActionContext<DictState, RootState>,
      dictType: string
    ): Promise<DictList> {
      if (!dictType) return []
      const { state, commit, getters } = ctx

      // 生成带日期的 key
      const dictKey = getDictKey(dictType)
      if (!dictKey) return []

      // 先查缓存 - 使用 getter 访问，避免 Proxy 问题
      const g = getters as unknown as DictGetters
      const cached = g.getDict(dictKey)
      if (cached && Array.isArray(cached) && cached.length > 0) {
        return cached
      }
      void state

      // 没有缓存，从接口获取
      try {
        const list: DictList = await getDicts(dictType)
        if (list && Array.isArray(list) && list.length > 0) {
          commit('SET_DICT', { key: dictKey, value: list })
          return list
        } else {
          console.warn(`[字典Store] 获取 ${dictType} 失败:`, list)
          return []
        }
      } catch (error) {
        console.error(`[字典Store] 获取 ${dictType} 异常:`, error)
        return []
      }
    },
  },
}

export default dict
