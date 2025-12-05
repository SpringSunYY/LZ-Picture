import { createStore } from 'vuex'
import user from '@/store/user'
import dict from '@/store/dict'
import getters from './getters'

const store = createStore({
  modules: {
    user,
    dict
  },
  getters
})

export default store