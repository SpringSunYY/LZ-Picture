// 格式化日期为 YYYY-MM-DD
const formatDateByDate = (date) => {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

// 从存储中加载字典数据
const loadDictFromStorage = () => {
  try {
    const storageKey = 'dict_storage'
    const data = uni.getStorageSync(storageKey)
    if (data) {
      return JSON.parse(data)
    }
  } catch (e) {
    console.error('加载字典数据失败', e)
  }
  return []
}

// 保存字典数据到存储
const saveDictToStorage = (dict) => {
  try {
    const storageKey = 'dict_storage'
    uni.setStorageSync(storageKey, JSON.stringify(dict))
  } catch (e) {
    console.error('保存字典数据失败', e)
  }
}

const dict = {
  state: {
    dict: loadDictFromStorage(),
  },

  mutations: {
    SET_DICT: (state, { key, value }) => {
      const existingIndex = state.dict.findIndex((item) => item.key === key)
      if (existingIndex !== -1) {
        state.dict[existingIndex].value = value
      } else {
        state.dict.push({ key: key, value })
      }
      saveDictToStorage(state.dict)
    },

    REMOVE_DICT: (state, key) => {
      const index = state.dict.findIndex((item) => item.key === key)
      if (index !== -1) {
        state.dict.splice(index, 1)
        saveDictToStorage(state.dict)
      }
    },

    CLEAN_DICT: (state) => {
      state.dict = []
      try {
        uni.removeStorageSync('dict_storage')
      } catch (e) {
        console.error('清除字典存储失败', e)
      }
    },
  },

  getters: {
    getDict: (state) => (key) => {
      if (!key) return null
      const now = formatDateByDate(new Date())
      key = now + ':' + key
      const item = state.dict.find((item) => item.key === key)
      return item ? item.value : null
    },
  },

  actions: {
    setDict({ commit }, { key, value }) {
      if (!key) return
      const now = formatDateByDate(new Date())
      key = now + ':' + key
      commit('SET_DICT', { key, value })
    },

    removeDict({ commit }, key) {
      const now = formatDateByDate(new Date())
      key = now + ':' + key
      commit('REMOVE_DICT', key)
    },

    cleanDict({ commit }) {
      commit('CLEAN_DICT')
    },

    initDict() {
      // 可加入接口初始化逻辑
    },
  },
}

export default dict

