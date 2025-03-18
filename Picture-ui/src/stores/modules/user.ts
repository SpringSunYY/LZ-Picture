import { getInfo, login, logout } from '@/api/userInfo/login.ts'
import { getToken, removeToken, setToken } from '@/utils/token'
import { isEmpty, isHttp } from '@/utils/validate'
import defAva from '@/assets/images/profile.jpg'
import { defineStore } from 'pinia'

// 定义状态类型
interface IUserState {
  token: string
  id: string
  name: string
  avatar: string
  roles: string[]
  permissions: string[]
}

// 定义用户登录参数类型
interface ILoginParams {
  username: string
  password: string
  code: string
  uuid: string
}

// 定义用户信息响应类型
interface IUserInfoResponse {
  user: {
    userId: string
    userName: string
    avatarUrl?: string
  }
  permissions?: string[]
}

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
    async login(userInfo: ILoginParams): Promise<void> {
      const { username, password, code, uuid } = userInfo
      try {
        const res = await login(username, password, code, uuid)
        setToken(res.token)
        this.token = res.token
      } catch (error) {
        return Promise.reject(error)
      }
    },

    // 获取用户信息
    async getInfo(): Promise<IUserInfoResponse> {
      try {
        const res = (await getInfo()) as IUserInfoResponse
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
