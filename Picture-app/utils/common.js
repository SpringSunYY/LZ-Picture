import config from "@/config.js";

const dnsUrl = config.dnsUrl

/**
 * 显示消息提示框
 * @param content 提示的标题
 */
export function toast(content) {
    uni.showToast({
        icon: 'none',
        title: content
    })
}

/**
 * 显示模态弹窗
 * @param content 提示的标题
 */
export function showConfirm(content) {
    return new Promise((resolve, reject) => {
        uni.showModal({
            title: '提示',
            content: content,
            cancelText: '取消',
            confirmText: '确定',
            success: function (res) {
                resolve(res)
            }
        })
    })
}

/**
 * 参数处理
 * @param params 参数
 */
export function tansParams(params) {
    let result = ''
    for (const propName of Object.keys(params)) {
        const value = params[propName]
        var part = encodeURIComponent(propName) + "="
        if (value !== null && value !== "" && typeof (value) !== "undefined") {
            if (typeof value === 'object') {
                for (const key of Object.keys(value)) {
                    if (value[key] !== null && value[key] !== "" && typeof (value[key]) !== 'undefined') {
                        let params = propName + '[' + key + ']'
                        var subPart = encodeURIComponent(params) + "="
                        result += subPart + encodeURIComponent(value[key]) + "&"
                    }
                }
            } else {
                result += part + encodeURIComponent(value) + "&"
            }
        }
    }
    return result
}

//格式化时间年月日
export const formatDateByDate = (date) => {
    date = new Date(date)
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}
export const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60)
    const secs = seconds % 60
    return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
}


/**
 * 初始化封面
 */
export const initCover = (url) => {
    if (!url) {
        // console.log('cover', url)
        return '/avatar.jpg'
    }
    if (url.startsWith('http')) {
        return url
    }
    return dnsUrl + url
}
