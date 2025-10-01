import config from '@/config'
import storage from '@/utils/storage'
import constant from '@/utils/constant'
import {getInfo, login, logout} from '@/api/login'
import {getToken, removeToken, setToken} from '@/utils/auth'

const baseUrl = config.baseUrl

const user = {
    state: {
        token: getToken(),
        userId: '',
        userName: '',
        nickName: '',
        avatar: '',
        permissions: [],
    },

    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token
        },
        SET_USER_ID: (state, userId) => {
            state.userId = userId
            storage.set(constant.userId, userId)
        },
        SET_USER_NAME: (state, userName) => {
            state.userName = userName
            storage.set(constant.userName, userName)
        },
        SET_AVATAR: (state, avatar) => {
            state.avatar = avatar
            storage.set(constant.avatar, avatar)
        },
        SET_PERMISSIONS: (state, permissions) => {
            state.permissions = permissions
            storage.set(constant.permissions, permissions)
        }
    },

    actions: {
        // 登录
        Login({commit}, userInfo) {
            const username = userInfo.username.trim()
            const password = userInfo.password
            const phone = userInfo.phone
            const countryCode = userInfo.countryCode
            // const code = userInfo.code
            // const uuid = userInfo.uuid
            return new Promise((resolve, reject) => {
                login(username, password, phone, countryCode).then(res => {
                    setToken(res.token)
                    console.log('res', res)
                    commit('SET_TOKEN', res.token)
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },

        // 获取用户信息
        GetInfo({commit, state}) {
            return new Promise((resolve, reject) => {
                getInfo().then(res => {
                    const user = res.userInfo
                    console.log('user', user)
                    const avatar = (user == null || user.userInfoProfile === "" || user.userInfoProfile == null) ? "@/static/images/profile.jpg" : baseUrl + user.userInfoProfile
                    const username = (user == null || user.userInfoName === "" || user.userInfoName == null) ? "" : user.userInfoName
                    commit('SET_USER_ID', user.userId)
                    commit('SET_USER_NAME', username)
                    commit('SET_AVATAR', avatar)
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        // 退出系统
        LogOut({commit, state}) {
            return new Promise((resolve, reject) => {
                logout(state.token).then(() => {
                    commit('SET_TOKEN', '')
                    commit('SET_PERMISSIONS', [])
                    commit('SET_USER_ID', '')
                    commit('SET_USER_NAME', '')
                    commit('SET_AVATAR', '')
                    removeToken()
                    storage.clean()
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        }
    }
}

export default user
