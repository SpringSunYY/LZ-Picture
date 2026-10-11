import { createStore, type Store } from 'vuex'
import type { InjectionKey } from 'vue'
import user from '@/store/user'
import dict from '@/store/dict'
import preferences from '@/store/preferences'
import getters from './getters'
import type { RootState } from './types'

const store = createStore<RootState>({
    modules: {
    user,
    dict,
    preferences,
  },
  getters,
})

/** 注入到 Vue 实例时使用的 key，配合 useStore() 时 TS 推导更准 */
export const storeKey: InjectionKey<Store<RootState>> = Symbol('store')

export default store
