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
import {getAccountPasswordVerify} from '@/api/points/account'

export const usePasswordVerify = (passwordModalRef = null) => {
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

            // 如果传入了组件 ref，使用组件（支持密码类型）
            if (passwordModalRef?.value) {
                passwordModalRef.value.show(actionDesc, resolve)
            } else {
                // 如果没有组件，返回 false
                resolve(false)
            }
        })
    }

    return {verify}
}
