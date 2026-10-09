import config from '@/config'
import storage from '@/utils/storage'
import constant from '@/utils/constant'
import {getInfo, login, smsLogin} from '@/api/login'
import {getToken, removeToken, setToken} from '@/utils/auth'
import type {GetInfoResponse, LoginParams, LoginTokenResponse, SmsLoginParams, UserInfo} from '@/types/user'
import type {ActionContext, Commit, Module} from 'vuex'
import type {RootState, UserState} from './types'

const baseUrl: string = config.baseUrl

interface UserCommitCtx {
  commit: Commit
  state: UserState
}

/**
 * 登录成功后统一处理 token 落库 + "记住我"账号同步。
 */
function commitLoginSuccess(
  { commit, state }: UserCommitCtx,
  token: string,
  account: string
): void {
  setToken(token)
  commit('SET_TOKEN', token)
  // 勾选"记住我"才保存账号（密码/短信码一律不存），未勾选则清空
  if (state.rememberMe && account) {
    commit('SET_REMEMBER_ACCOUNT', account)
  } else {
    commit('SET_REMEMBER_ACCOUNT', '')
  }
}

const user: Module<UserState, RootState> = {
  namespaced: false,

  state: (): UserState => ({
    // 从本地恢复登录态和用户信息，避免每次热更新都要重新登录
    token: getToken(),
    userId: (storage.get<string | number>(constant.userId) as UserState['userId']) || '',
    userName: storage.get(constant.userName) || '',
    nickName: storage.get(constant.nickName) || '',
    avatar: storage.get(constant.avatar) || '',
    permissions: (storage.get<string[]>(constant.permissions) as string[]) || [],
    // UI 偏好：记住我（默认 false, 必须由用户主动勾选才开启）
    rememberMe:
      storage.get<boolean>(constant.rememberMe) ||
      storage.get<boolean>(constant.rememberMe) === ('true' as unknown as boolean),
    rememberAccount: storage.get(constant.rememberAccount) || '',
  }),

  mutations: {
    SET_TOKEN: (state: UserState, token: string) => {
      state.token = token
    },
    SET_USER_ID: (state: UserState, userId: string | number) => {
      state.userId = userId
      storage.set(constant.userId, userId)
    },
    SET_USER_NAME: (state: UserState, userName: string) => {
      state.userName = userName
      storage.set(constant.userName, userName)
    },
    SET_NICK_NAME: (state: UserState, nickName: string) => {
      state.nickName = nickName
      storage.set(constant.nickName, nickName)
    },
    SET_AVATAR: (state: UserState, avatar: string) => {
      state.avatar = avatar
      storage.set(constant.avatar, avatar)
    },
    SET_PERMISSIONS: (state: UserState, permissions: string[]) => {
      state.permissions = permissions
      storage.set(constant.permissions, permissions)
    },
    SET_REMEMBER_ME: (state: UserState, val: boolean) => {
      state.rememberMe = val
      storage.set(constant.rememberMe, val)
    },
    SET_REMEMBER_ACCOUNT: (state: UserState, account: string) => {
      state.rememberAccount = account || ''
      if (account) {
        storage.set(constant.rememberAccount, account)
      } else {
        storage.remove(constant.rememberAccount)
      }
    },
  },

  actions: {
    // 切换"记住我"开关
    ToggleRememberMe(ctx: ActionContext<UserState, RootState>, val: boolean): void {
      ctx.commit('SET_REMEMBER_ME', val)
      if (!val) {
        ctx.commit('SET_REMEMBER_ACCOUNT', '')
      }
    },

    // 强制从本地 storage 重新拉取"记住我/记住账号"
    RefreshRemember(ctx: ActionContext<UserState, RootState>): void {
      const rememberMe =
        storage.get<boolean>(constant.rememberMe) ||
        storage.get<boolean>(constant.rememberMe) === ('true' as unknown as boolean)
      const rememberAccount = storage.get(constant.rememberAccount) || ''
      ctx.commit('SET_REMEMBER_ME', rememberMe)
      ctx.commit('SET_REMEMBER_ACCOUNT', rememberAccount)
    },

    // 账号密码登录
    Login(
      ctx: ActionContext<UserState, RootState>,
      userInfo: LoginParams
    ): Promise<void> {
      const { commit, state } = ctx
      // 兼容 username / phone 两种入参；undefined 时兜底为空串，避免 .trim() 抛错
      const username = (userInfo.username || '').trim()
      const password = userInfo.password || ''
      const phone = (userInfo.phone || '').trim()
      const countryCode = userInfo.countryCode || '+86'
      return new Promise<void>((resolve, reject) => {
        login(username, password, phone, countryCode)
          .then((res: LoginTokenResponse) => {
            commitLoginSuccess({ commit, state }, res.token, username || phone)
            resolve()
          })
          .catch((error: unknown) => {
            reject(error)
          })
      })
    },

    /**
     * 短信验证码登录：与 Login 对称，token 落库/记住我副作用都走 commitLoginSuccess。
     */
    SmsLogin(
      ctx: ActionContext<UserState, RootState>,
      payload: SmsLoginParams
    ): Promise<LoginTokenResponse> {
      const { commit, state } = ctx
      const data: SmsLoginParams = {
        countryCode: payload.countryCode || '+86',
        phone: (payload.phone || '').trim(),
        smsCode: payload.smsCode || '',
        code: payload.code || '',
        uuid: payload.uuid || '',
      }
      return new Promise<LoginTokenResponse>((resolve, reject) => {
        smsLogin(data)
          .then((res: LoginTokenResponse) => {
            commitLoginSuccess({ commit, state }, res.token, data.phone || '')
            resolve(res)
          })
          .catch((error: unknown) => {
            reject(error)
          })
      })
    },

    // 获取用户信息
    GetInfo(
      ctx: ActionContext<UserState, RootState>
    ): Promise<GetInfoResponse> {
      const { commit } = ctx
      void ctx.state
      return new Promise<GetInfoResponse>((resolve, reject) => {
        getInfo()
          .then((res: GetInfoResponse) => {
            const u: UserInfo | null = res.user
            const avatar =
              u == null || u.avatarUrl === '' || u.avatarUrl == null
                ? '@/static/images/profile.jpg'
                : baseUrl + u.avatarUrl
            const userName =
              u == null || u.userName === '' || u.userName == null ? '' : u.userName
            commit('SET_USER_ID', u?.userId ?? '')
            commit('SET_USER_NAME', userName)
            commit('SET_AVATAR', avatar)
            commit('SET_PERMISSIONS', u?.permissions ?? [])
            commit('SET_NICK_NAME', u?.nickName ?? '')
            resolve(res)
          })
          .catch((error: unknown) => {
            reject(error)
          })
      })
    },

    // 退出系统
    LogOut(ctx: ActionContext<UserState, RootState>): Promise<void> {
      const { commit, state } = ctx
      void state
      // 注意: 不在这里调后端 logout() 接口。
      // 原因: 401 时 token 已过期, 后端 /logout 也会 401, 会导致整个 Promise reject,
      // 从而阻断 .then(uni.reLaunch) 的执行, 出现"点了确认但页面不跳转"的问题。
      // 这里只清本地登录态, 后端 /logout 留给上层显式调用的场景(用户主动退出按钮)。
      try {
        commit('SET_TOKEN', '')
        commit('SET_USER_ID', '')
        commit('SET_USER_NAME', '')
        commit('SET_AVATAR', '')
        removeToken()
        // 注意: 不要 storage.clean(), 否则会一并清掉 "记住我" / "记住账号" 等 UI 偏好
        // 这里只清掉身份相关字段, UI 偏好保留以便下次进入时仍能自动回填手机号
        commit('SET_NICK_NAME', '')
        commit('SET_PERMISSIONS', [])
        return Promise.resolve()
      } catch (e) {
        return Promise.reject(e)
      }
    },
  },
}

export default user
