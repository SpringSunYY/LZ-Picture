import type { GetterTree } from 'vuex'
import type { RootState } from './types'

const getters: GetterTree<RootState, RootState> = {
  token: (state: RootState) => state.user.token,
  avatar: (state: RootState) => state.user.avatar,
  username: (state: RootState) => state.user.userName,
  userId: (state: RootState) => state.user.userId,
  nickName: (state: RootState) => state.user.nickName,
  permissions: (state: RootState) => state.user.permissions,
}

export default getters
