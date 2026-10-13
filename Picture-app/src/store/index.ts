import { createStore, type Store } from 'vuex'
import type { InjectionKey } from 'vue'
import user, {installUserInfoSync} from '@/store/user'
import dict from '@/store/dict'
import preferences from '@/store/preferences'
import space from '@/store/space'
import getters from './getters'
import type { RootState } from './types'

const store = createStore<RootState>({
    modules: {
    user,
    dict,
    preferences,
    space,
  },
  getters,
})

// 跨页面用户信息同步副作用(由 user 模块自带,详见 store/user.ts)
installUserInfoSync(store)

/** 注入到 Vue 实例时使用的 key，配合 useStore() 时 TS 推导更准 */
export const storeKey: InjectionKey<Store<RootState>> = Symbol('store')

export default store
