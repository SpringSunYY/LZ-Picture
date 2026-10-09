import { getAccountPasswordVerify } from '@/api/points/account'
import type { Ref } from 'vue'

const TokenKey: string = 'App-Token'

export function getToken(): string {
  return uni.getStorageSync(TokenKey) || ''
}

export function setToken(token: string): void {
  uni.setStorageSync(TokenKey, token)
}

export function removeToken(): void {
  uni.removeStorageSync(TokenKey)
}

/** 密码验证组件实例暴露的最小协议 */
export interface PasswordVerifyComponent {
  show: (actionDesc: string, resolve: (ok: boolean) => void) => void
}

/**
 * 密码验证工具函数
 * @param passwordModalRef 密码弹窗组件的 ref，不传时 verify 永远返回 false
 */
export const usePasswordVerify = (
  passwordModalRef: Ref<PasswordVerifyComponent | null> | null = null
): { verify: (actionDesc?: string) => Promise<boolean> } => {
  const verify = async (actionDesc: string = '此操作'): Promise<boolean> => {
    // 首先校验用户最近是否输入且正确
    try {
      const state = await getAccountPasswordVerify()
      if (state === 1) {
        return true
      }
    } catch (error) {
      console.log('获取验证状态失败', error)
    }

    return new Promise<boolean>((resolve) => {
      console.log('开始验证密码')

      // 如果传入了组件 ref，使用组件（支持密码类型）
      if (passwordModalRef?.value) {
        passwordModalRef.value.show(actionDesc, resolve)
      } else {
        // 如果没有组件，返回 false
        resolve(false)
      }
    })
  }

  return { verify }
}
