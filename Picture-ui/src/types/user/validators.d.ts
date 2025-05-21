// 密码校验正则
export const passwordPattern = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$/

// 密码校验提示语
export const passwordPatternMessage = '密码长度8~20包含字母和数字，可使用符号但不能使用表情'

// 表情校验正则
const emojiRegex =
  /[\u{1F600}-\u{1F64F}\u{1F300}-\u{1F5FF}\u{1F680}-\u{1F6FF}\u{2600}-\u{26FF}\u{2700}-\u{27BF}]/gu

// 密码校验函数（自定义校验器）
export const validatePassword = (_: any, value: string): Promise<void> => {
  if (!value || value.length < 8) {
    return Promise.reject('密码长度至少为8位')
  }
  if (emojiRegex.test(value)) {
    return Promise.reject('密码不能包含表情符号')
  }
  return Promise.resolve()
}

// 验证规则
export const validateConfirmPassword =
  (password: string, confirmPassword: string): Promise<void> => {
  if (password !== confirmPassword) {
    return Promise.reject(new Error('两次密码不一致'));
  }
  return Promise.resolve();
}

