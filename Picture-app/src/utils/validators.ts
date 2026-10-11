/**
 * 全局校验工具（单一来源）
 *
 * 为什么要集中在这里：
 * - predicate 统一返回 true/false，提示语由 rule 侧决定
 * - 与 uview-plus 的 validator 协议（Promise）解耦，页面只需 makeValidator 包装一次
 */

// ============================================================
// 基础正则（唯一的正则来源，其他地方一律 import）
// ============================================================

/** 手机号：1 开头，第二位 3-9，共 11 位（大陆号码） */
export const PHONE_RE: RegExp = /^1[3-9]\d{9}$/

/** 图形验证码：字母/数字，长度 1-6（与后端 /captchaImage 长度对齐） */
export const CAPTCHA_RE: RegExp = /^[A-Za-z0-9]{1,6}$/

/** 短信验证码：纯数字 6 位 */
export const SMS_CODE_RE: RegExp = /^\d{6}$/


/** 用户名：字母开头，允许字母数字下划线，4-20 位 */
export const USERNAME_RE: RegExp = /^[A-Za-z]\w{3,19}$/

/** 邮箱 */
export const EMAIL_RE: RegExp = /^[\w.+-]+@[\w-]+(\.[\w-]+)+$/

/** 表情符号（密码禁止项） */
const EMOJI_RE: RegExp = /[\u{1F300}-\u{1FAFF}\u2600-\u27BF]/u

// ============================================================
// 密码校验（动态长度 + 字母数字必含 + 表情检测）
// ============================================================

// 表情校验正则
const emojiRegex =
    /[\u{1F600}-\u{1F64F}\u{1F300}-\u{1F5FF}\u{1F680}-\u{1F6FF}\u{2600}-\u{26FF}\u{2700}-\u{27BF}]/u

// 密码校验函数（自定义校验器）
export const validatePassword = (
    password: string,
    minLength: number,
    maxLength: number,
): Promise<void> => {
    if (!password || password.length < minLength || password.length > maxLength) {
        return Promise.reject('密码长度为' + minLength + '~' + maxLength + '位')
    }
    if (emojiRegex.test(password)) {
        return Promise.reject('密码不能包含表情符号')
    }
    return Promise.resolve()
}

// 验证规则
export const validateConfirmPassword = (
    password: string,
    confirmPassword: string,
): Promise<void> => {
    if (password !== confirmPassword) {
        return Promise.reject(new Error('两次密码不一致'))
    }
    return Promise.resolve()
}

// ============================================================
// 通用 predicate（页面里的 computed / canXxx 直接用这些）
// ============================================================

export type Predicate = (v: unknown) => boolean

export const isEmpty = (v: unknown): boolean =>
    v === null || v === undefined || String(v).trim() === ''

/** 手机号是否合法（纯正则判断，不依赖任何页面上下文） */
export const isPhone: Predicate = (v: unknown): boolean => !isEmpty(v) && PHONE_RE.test(String(v).trim())

/** 图形验证码是否合法 */
export const isCaptcha: Predicate = (v: unknown): boolean =>
    !isEmpty(v) && CAPTCHA_RE.test(String(v).trim())

/** 短信验证码是否合法（纯数字 6 位） */
export const isSmsCode: Predicate = (v: unknown): boolean =>
    !isEmpty(v) && SMS_CODE_RE.test(String(v).trim())

/** 密码是否合法：8-20 位且不含表情 */
export const isPassword: Predicate = (v: unknown): boolean => {
    if (isEmpty(v)) return false
    const s = String(v)
    if (s.length < 8 || s.length > 20) return false
    if (EMOJI_RE.test(s)) return false
    return true
}

/** 用户名是否合法 */
export const isUsername: Predicate = (v: unknown): boolean =>
    !isEmpty(v) && USERNAME_RE.test(String(v).trim())

/** 邮箱是否合法 */
export const isEmail: Predicate = (v: unknown): boolean =>
    !isEmpty(v) && EMAIL_RE.test(String(v).trim())

// ============================================================
// uview-plus validator 适配器
// uview-plus 的 validator 协议是：返回 Promise，失败 reject(new Error(msg))
// 页面只需要 makeValidator(isPhone, '请输入正确的手机号') 即可
// ============================================================

export interface MakeValidatorOptions {
    required?: boolean
    emptyMsg?: string
}

/** uview-plus 规则对象，validator 只用 value，这里只声明用到的那部分 */
export interface URuleLike {
    required?: boolean

    [key: string]: unknown
}

/**
 * 把 predicate 包装成 uview-plus validator
 * uview-plus 会以 (rule, value) 调用 validator，所以这里保持同样的签名
 */
export const makeValidator = (
    predicate: Predicate,
    errMsg: string,
    opts: MakeValidatorOptions = {}
): ((rule: URuleLike, value: unknown) => Promise<void>) => {
    const {required = true, emptyMsg = '该项不能为空'} = opts
    return (_rule: URuleLike, value: unknown): Promise<void> =>
        new Promise((resolve, reject) => {
            if (required && isEmpty(value)) {
                reject(new Error(emptyMsg))
                return
            }
            if (!predicate(value)) {
                reject(new Error(errMsg))
                return
            }
            resolve()
        })
}
