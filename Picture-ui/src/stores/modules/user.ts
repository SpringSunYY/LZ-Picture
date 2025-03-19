import { getInfo, login, logout, smsLogin } from '@/api/userInfo/login.ts'
import { getToken, removeToken, setToken } from '@/utils/token'
import { isEmpty, isHttp } from '@/utils/validate'
import defAva from '@/assets/images/profile.jpg'
import { defineStore } from 'pinia'
import type { USER } from '@/types/user'
import router from '@/router'

const useUserStore = defineStore('user', {
  state: (): {
    permissions: any[]
    name: string
    id: string
    avatar: string
    token: string | null
  } => ({
    token: getToken(),
    id: '',
    name: '',
    avatar: '',
    permissions: [],
  }),

  actions: {
    // 登录操作
    async login(userInfo: USER.LoginParams): Promise<void> {
      try {
        const res = await login(userInfo)
        setToken(res.token)
        this.token = res.token
        router.push('/')
      } catch (error) {
        return Promise.reject(error)
      }
    },

    async smsLogin(userInfo: USER.SmsLoginParams): Promise<void> {
      try {
        const res = await smsLogin(userInfo)
        setToken(res.token)
        this.token = res.token
        router.push('/')
      } catch (error) {
        return Promise.reject(error)
      }
    },

    // 获取用户信息
    async getInfo(): Promise<USER.UserInfoResponse> {
      try {
        const res = (await getInfo()) as USER.UserInfoResponse
        const user = res.user

        // 处理头像路径
        let avatar = user.avatarUrl || ''
        if (!isHttp(avatar)) {
          avatar = isEmpty(avatar) ? defAva : `${import.meta.env.VITE_APP_BASE_API}${avatar}`
        }

        // 更新用户信息
        this.id = user.userId
        this.name = user.userName
        this.avatar = avatar

        return res
      } catch (error) {
        router.push('/user/login')
        return Promise.reject(error)
      }
    },

    // 退出登录
    logOut: async function (): Promise<void> {
      try {
        await logout()
        this.token = ''
        this.permissions = []
        removeToken()
      } catch (error) {
        return Promise.reject(error)
      }
    },
  },
})

export default useUserStore
