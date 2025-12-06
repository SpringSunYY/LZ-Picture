import { reactive, toRefs } from 'vue'
import { useStore } from 'vuex'

/**
 * 获取字典数据
 * @param {...string} args 字典类型，可以传入多个
 * @returns {object} 返回响应式的字典数据对象
 */
export function useDict(...args) {
  const store = useStore()
  const res = reactive({})

  args.forEach((dictType) => {
    res[dictType] = []
    
    // 直接调用 action，action 内部会处理缓存逻辑
    store.dispatch('getDict', dictType).then((data) => {
      if (data && Array.isArray(data)) {
        res[dictType] = data
      }
    })
  })

  return toRefs(res)
}
