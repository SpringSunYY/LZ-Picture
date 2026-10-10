import config from '@/config'

const dnsUrl: string = config.dnsUrl

/**
 * 参数处理
 * @param params 参数
 */
export function tansParams(params: Record<string, unknown>): string {
    let result = ''
    for (const propName of Object.keys(params)) {
        const value = params[propName]
        const part = encodeURIComponent(propName) + '='
        if (value !== null && value !== '' && typeof value !== 'undefined') {
            if (typeof value === 'object') {
                for (const key of Object.keys(value as Record<string, unknown>)) {
                    const objVal = (value as Record<string, unknown>)[key]
                    if (objVal !== null && objVal !== '' && typeof objVal !== 'undefined') {
                        const params = propName + '[' + key + ']'
                        const subPart = encodeURIComponent(params) + '='
                        result += subPart + encodeURIComponent(String(objVal)) + '&'
                    }
                }
            } else {
                result += part + encodeURIComponent(String(value)) + '&'
            }
        }
    }
    return result
}

/** 格式化 yyyy-MM-dd */
export const formatDateByDate = (date: string | number | Date): string => {
    const d = new Date(date)
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

/** 格式化 mm:ss（秒转分秒） */
export const formatTime = (seconds: number): string => {
    const mins = Math.floor(seconds / 60)
    const secs = seconds % 60
    return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
}

/**
 * 初始化封面：空 → static/profile.jpg；完整 http → 原样；相对路径 → 拼 dnsUrl
 *
 * 关键：兜底路径不能写 '/static/profile.jpg' 这种带前导斜杠的绝对路径
 * 在 微信小程序 / App 里会解析失败(小程序没有"站点根"概念,绝对路径指不到产物文件)
 * 不带前导斜杠,uni-app 编译器会拦截并改写为三端都能识别的资源路径
 */
export const initCover = (url: string | null | undefined): string => {
    if (!url || url === '') {
        return '/static/profile.jpg'
    }
    if (url.startsWith('http')) {
        return url
    }
    return dnsUrl + url
}

/** 格式化文件大小 */
export const formatSize = (bytes: number) => {
    const units = ['B', 'KB', 'MB', 'GB', 'TB']
    let size = bytes
    let unitIndex = 0
    while (size >= 1024 && unitIndex < units.length - 1) {
        size /= 1024
        unitIndex++
    }
    //保留小数点后两位
    size = Math.round(size * 100) / 100
    return `${size}${units[unitIndex]}`
}