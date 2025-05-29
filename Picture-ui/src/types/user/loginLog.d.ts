/**
 * 我的登录日志VO对象
 */
export interface LoginLogInfoVo {
  /** 编号 */
  infoId: string;

  /** 登录方式（字典：u_login_type） */
  loginType: string;

  /** 登录地点 */
  loginLocation: string;

  /** 登录IP地址 */
  ipaddr: string;

  /** 登录时间（格式：yyyy-MM-dd HH:mm:ss） */
  loginTime: string;
}

/**
 * 用户登录日志查询对象
 */
export interface LoginLogInfoQuery {
  /** 用户ID */
  userId?: string;

  /** 额外参数（可用于扩展条件） */
  params?: Record<string, any>;

  /** 当前页码 */
  pageNum?: number;

  /** 每页条数 */
  pageSize?: number;


  /** 排序字段 */
  orderByColumn?: string

  /** 排序方式（asc 或 desc） */
  isAsc?: string
}

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
