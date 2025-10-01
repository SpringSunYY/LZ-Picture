const emojiRegex = /[\u{1F300}-\u{1FAFF}\u2600-\u27BF]/u
// 密码校验函数保持不变
export const validatePassword = (
    password,
    minLength,
    maxLength,
) => {
    if (!password || password.length < minLength || password.length > maxLength) {
        return '密码长度为' + minLength + '~' + maxLength + '位'
    }
    if (emojiRegex.test(password)) {
        return '密码不能包含表情符号'
    }
    return null
}