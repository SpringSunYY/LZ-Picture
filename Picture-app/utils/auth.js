const TokenKey = 'App-Token'

export function getToken() {
  return uni.getStorageSync(TokenKey)
}

export function setToken(token) {
  return uni.setStorageSync(TokenKey, token)
}

export function removeToken() {
  return uni.removeStorageSync(TokenKey)
}

/**
 * 密码验证工具函数
 */
import { getAccountPasswordVerify, verifyPassword } from '@/api/points/account'
import { toast } from '@/utils/common'

// 全局密码验证状态
let passwordVerifyInstance = null

export const usePasswordVerify = () => {
  const verify = async (actionDesc = '此操作') => {
    // 首先校验用户最近是否输入且正确
    try {
      const res = await getAccountPasswordVerify()
      if (res.code === 200 && res.data === 1) {
        return true
      }
    } catch (error) {
      console.log('获取验证状态失败', error)
    }

    return new Promise((resolve) => {
      console.log('开始验证密码')

      // 使用 uni.showModal 的 prompt 方式（H5支持）
      // 小程序等平台需要使用自定义组件
      const systemInfo = uni.getSystemInfoSync()
      const platform = systemInfo.platform

      // H5 平台可以使用 prompt
      if (platform === 'devtools' || platform === 'web') {
        uni.showModal({
          title: '安全验证',
          content: `请输入密码以${actionDesc}`,
          editable: true,
          placeholderText: '请输入账户密码',
          confirmText: '确认',
          cancelText: '取消',
          success: async (res) => {
            if (res.confirm) {
              const password = res.content || ''

              if (!password) {
                toast('请输入密码')
                resolve(false)
                return
              }

              try {
                const verifyRes = await verifyPassword(password)
                if (verifyRes.code === 200 && verifyRes.data === 1) {
                  toast('验证通过')
                  resolve(true)
                } else {
                  toast('密码错误，请重新输入')
                  // 递归调用，允许重新输入
                  const retryResult = await verify(actionDesc)
                  resolve(retryResult)
                }
              } catch (error) {
                console.log('验证密码失败', error)
                toast('验证失败，请重试')
                resolve(false)
              }
            } else {
              resolve(false)
            }
          }
        })
      } else {
        // 小程序等平台，使用自定义弹窗
        // 这里需要触发全局的密码验证组件
        if (passwordVerifyInstance) {
          passwordVerifyInstance.show(actionDesc, resolve)
        } else {
          // 如果没有注册组件，使用简单的提示
          uni.showModal({
            title: '安全验证',
            content: `需要密码验证以${actionDesc}，请使用密码验证组件`,
            showCancel: false,
            confirmText: '确定',
            success: () => {
              resolve(false)
            }
          })
        }
      }
    })
  }

  return { verify }
}

// 注册密码验证组件实例（供组件调用）
export const setPasswordVerifyInstance = (instance) => {
  passwordVerifyInstance = instance
}
