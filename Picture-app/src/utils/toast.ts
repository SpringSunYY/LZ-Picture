const DEFAULT_DURATION = 3000
/**
 * 显示消息提示框
 * @param content 提示的标题
 * @param duration 提示的时长
 *
 */
export function toast(content: string, duration: number = DEFAULT_DURATION): void {
    // 走 uview-plus 的全局 Root Toast(Root 由 App.up.vue → UniUpRoot 插件注入)
    // 见 https://uview-plus.jiangruyi.com/guide/root.html
    console.log('toast', content)
    uni.$u.rootToast({message: content, duration: duration})
}

/**
 * 消息警告框
 * @param content 警告的标题
 * @param duration 提示的时长
 */
export function toastWarn(content: string, duration: number = DEFAULT_DURATION): void {
    uni.$u.rootToast({message: content, type: 'warning', duration: duration})
}

/**
 * 显示全局加载提示(uview-plus 风格,带遮罩)
 * 底层走 Root Toast 的 type: 'loading'(duration: -1 不自动关闭)
 * 关闭必须配套调用 toastHide(),否则会一直转
 */
export function toastLoading(content: string = '加载中...'): void {
    uni.$u.rootToast({message: content, type: 'loading', duration: -1})
}

/**
 * 关闭全局加载/提示
 * 用一个 duration 极短的空 toast 把 loading 挤掉
 * (uview-plus 没有官方 hideLoading API,这是社区通用做法)
 */
export function toastHide(): void {
    uni.$u.rootToast({message: '', type: 'default', duration: 10})
}

export interface ShowConfirmResult {
    confirm: boolean
    cancel: boolean
    content?: string
}

/**
 * 显示模态弹窗
 * @param content 提示的标题
 */
export function showConfirm(content: string): Promise<ShowConfirmResult> {
    return new Promise((resolve) => {
        uni.showModal({
            title: '提示',
            content,
            cancelText: '取消',
            confirmText: '确定',
            success: (res) => {
                resolve(res as ShowConfirmResult)
            },
        })
    })
}
