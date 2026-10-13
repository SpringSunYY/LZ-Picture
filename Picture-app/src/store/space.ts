import type {Module} from 'vuex'
import type {RootState, SpaceState} from './types'
import {getSpacePerm} from '@/api/picture/space'

const space: Module<SpaceState, RootState> = {
    namespaced: true,

    state: (): SpaceState => ({
        perms: [],
        permsLoaded: false,
    }),

    mutations: {
        SET_PERMS(state, perms: string[]) {
            state.perms = perms
            state.permsLoaded = true
        },
        RESET(state) {
            state.perms = []
            state.permsLoaded = false
        },
    },

    actions: {
        /**
         * 异步加载空间权限
         */
        async loadSpacePerms({state, commit}): Promise<void> {
            if (state.permsLoaded) return
            const res = await getSpacePerm()
            commit('SET_PERMS', res.data || [])
        },

        /**
         * 手动刷新权限
         */
        async refreshSpacePerms({commit}): Promise<void> {
            commit('RESET')
            const res = await getSpacePerm()
            commit('SET_PERMS', res.data || [])
        },
    },

    getters: {
        /** 获取所有空间权限（同步，未加载时返回空数组） */
        getSpacePerms: (state) => state.perms,
        /** 权限是否已加载 */
        isPermsLoaded: (state) => state.permsLoaded,
    },
}

export default space
