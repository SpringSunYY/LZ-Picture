import { computed, watch, type ComputedRef } from 'vue'
import { useStore } from 'vuex'
import type { RootState } from '@/store/types'
import type { LoginParams, SmsLoginParams } from '@/types/user'

/** 登录页共用 model（账号 / 手机号字段） */
export interface UseUserModels {
  account: { account: string; [k: string]: unknown }
  phone: { regPhone: string; [k: string]: unknown }
}

export interface BindRememberOptions {
  /** true 时取消勾选会清空输入 */
  syncEmpty?: boolean
}

export interface UseUserReturn {
  rememberMe: ComputedRef<boolean>
  rememberAccount: ComputedRef<string>
  initialAccount: ComputedRef<string>
  bindRememberTo: (models: UseUserModels, opts?: BindRememberOptions) => void
  bindSharedPhone: (models: UseUserModels) => void
  setRememberMe: (val: boolean) => Promise<unknown>
  refreshRemember: () => Promise<unknown>
  login: (payload: LoginParams) => Promise<unknown>
  smsLogin: (payload: SmsLoginParams) => Promise<unknown>
  getInfo: () => Promise<unknown>
  logout: () => Promise<unknown>
}

/**
 * user 模块的页面侧封装。
 *
 * 设计原则（与 useDict 保持一致）：
 * - 页面不直接接触 store.dispatch / store.state，统一通过这里的方法和 computed
 * - 副作用（token 落库、记住我同步 storage）全部收敛在 store action 内
 * - 页面只负责表单校验与 UI 反馈
 */
export function useUser(): UseUserReturn {
  const store = useStore<RootState>()

  // ---------------- 读取（只读 computed） ----------------
  /** 是否勾选"记住我" */
  const rememberMe = computed<boolean>(() => store.state.user.rememberMe)
  /** 已记住的账号（未勾选时为空串） */
  const rememberAccount = computed<string>(() => store.state.user.rememberAccount)

  /**
   * 登录页初始账号：勾选了记住我才回填，否则空串
   * store 初始化早于 storage 写入的场景由 refreshRemember() 兜底
   */
  const initialAccount = computed<string>(() =>
    rememberMe.value ? rememberAccount.value || '' : ''
  )

  /**
   * 监听"记住我 / 记住账号"变化并同步到页面 model。
   */
  function bindRememberTo(
    models: UseUserModels,
    opts: BindRememberOptions = {}
  ): void {
    const { syncEmpty = true } = opts
    watch(
      () => [rememberMe.value, rememberAccount.value] as const,
      ([me, account]) => {
        if (me && account && !models.account.account) {
          // 勾选 + 有账号 + 输入仍空 → 回填
          models.account.account = account
          if (models.phone.regPhone !== account) models.phone.regPhone = account
        } else if (!me && syncEmpty) {
          // 取消勾选 → 清空输入，避免脏数据
          if (!rememberAccount.value) {
            if (models.account.account) models.account.account = ''
            if (models.phone.regPhone) models.phone.regPhone = ''
          }
        }
      },
      { immediate: true }
    )
  }

  /**
   * 两个 tab 共用同一手机号时的双向同步。
   * 只传其中一个 model 时退化为单向外同步。
   */
  function bindSharedPhone(models: UseUserModels): void {
    watch(
      () => models.account.account,
      (val) => {
        if (models.phone.regPhone !== val) models.phone.regPhone = val || ''
      }
    )
    watch(
      () => models.phone.regPhone,
      (val) => {
        if (models.account.account !== val) models.account.account = val || ''
      }
    )
  }

  // ---------------- 写入（action 转发） ----------------
  /** 切换"记住我"；取消勾选时 store 会自动清空已存账号 */
  function setRememberMe(val: boolean): Promise<unknown> {
    return store.dispatch('ToggleRememberMe', Boolean(val)) as Promise<unknown>
  }

  /** 从 storage 重新拉取"记住我/记住账号"，onShow 时调用 */
  function refreshRemember(): Promise<unknown> {
    return store.dispatch('RefreshRemember') as Promise<unknown>
  }

  /** 账号密码登录，成功后 token 由 store 落库 */
  function login(payload: LoginParams): Promise<unknown> {
    return store.dispatch('Login', payload) as Promise<unknown>
  }

  /** 短信验证码登录，成功后 token 由 store 落库 */
  function smsLogin(payload: SmsLoginParams): Promise<unknown> {
    return store.dispatch('SmsLogin', payload) as Promise<unknown>
  }

  /** 拉取用户信息并写入 store */
  function getInfo(): Promise<unknown> {
    return store.dispatch('GetInfo') as Promise<unknown>
  }

  /** 退出登录:统一走 store LogOut(确认 / 后端 / 清本地 / toast / reLaunch 全在里面) */
  function logout(): Promise<unknown> {
    return store.dispatch('LogOut') as Promise<unknown>
  }

  return {
    rememberMe,
    rememberAccount,
    initialAccount,
    bindRememberTo,
    bindSharedPhone,
    setRememberMe,
    refreshRemember,
    login,
    smsLogin,
    getInfo,
    logout,
  }
}

// end of file
