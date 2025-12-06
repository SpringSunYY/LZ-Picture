import { getDicts } from '@/api/common/common'
import { formatDateByDate } from '@/utils/common'

// 生成带日期的 key
const getDictKey = (key) => {
  if (!key) return null
  const now = formatDateByDate(new Date())
  return `${now}:${key}`
}

// 从本地存储加载字典数据
const loadDictFromStorage = () => {
  try {
    const data = uni.getStorageSync('dict_cache')
    return data ? JSON.parse(data) : {}
  } catch (e) {
    console.error('加载字典缓存失败:', e)
    return {}
  }
}

const dict = {
  state: {
    dict: loadDictFromStorage(),
  },

  mutations: {
    SET_DICT: (state, { key, value }) => {
      state.dict[key] = value
      // 同时保存到本地存储
      try {
        uni.setStorageSync('dict_cache', JSON.stringify(state.dict))
      } catch (e) {
        console.error('保存字典缓存失败:', e)
      }
    },
  },

  getters: {
    getDict: (state) => (key) => {
      if (!key) return null
      return state.dict[key] || null
    },
  },

  actions: {
    // 获取字典数据（先查缓存，没有就从接口获取）
    async getDict({ state, commit, getters }, dictType) {
      if (!dictType) return []

      // 生成带日期的 key
      const dictKey = getDictKey(dictType)
      
      // 先查缓存 - 使用 getter 访问，避免 Proxy 问题
      const cached = getters.getDict(dictKey)
      if (cached && Array.isArray(cached) && cached.length > 0) {
        return cached
      }

      // 没有缓存，从接口获取
      try {
        const resp = await getDicts(dictType)
        if (resp?.code === 200 && resp?.data) {
          commit('SET_DICT', { key: dictKey, value: resp.data })
          return resp.data
        } else {
          console.warn(`[字典Store] 获取 ${dictType} 失败:`, resp)
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

