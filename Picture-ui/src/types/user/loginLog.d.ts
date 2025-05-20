// 枚举定义 登录类型枚举
export enum ULoginTypeEnum {
  LOGIN_TYPE_0 = "0", // 账密
  LOGIN_TYPE_1 = "1", // 短信
  LOGIN_TYPE_2 = "2", // 微信
}

// 获取标签的函数 登录类型枚举
export function getLoginTypeLabel(value: string): string | undefined {
  switch (value) {
    case ULoginTypeEnum.LOGIN_TYPE_0:
      return "账密";
    case ULoginTypeEnum.LOGIN_TYPE_1:
      return "短信";
    case ULoginTypeEnum.LOGIN_TYPE_2:
      return "微信";
    default:
      return undefined;
  }
}

// 获取枚举值的函数（根据字符串值） 登录类型枚举
export function getLoginTypeByValue(value: string): ULoginTypeEnum | undefined {
  if (Object.values(ULoginTypeEnum).includes(value as ULoginTypeEnum)) {
    return value as ULoginTypeEnum;
  }
  return undefined;
}


// 枚举定义 登录状态枚举
export enum ULoginStatusEnum {
  LOGIN_STATUS_0 = "0", // 成功
  LOGIN_STATUS_1 = "1", // 失败
}

// 获取标签的函数 登录状态枚举
export function getLoginStatusLabel(value: string): string | undefined {
  switch (value) {
    case ULoginStatusEnum.LOGIN_STATUS_0:
      return "成功";
    case ULoginStatusEnum.LOGIN_STATUS_1:
      return "失败";
    default:
      return undefined;
  }
}

// 获取枚举值的函数（根据字符串值） 登录状态枚举
export function getLoginStatusByValue(value: string): ULoginStatusEnum | undefined {
  if (Object.values(ULoginStatusEnum).includes(value as ULoginStatusEnum)) {
    return value as ULoginStatusEnum;
  }
  return undefined;
}
