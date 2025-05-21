/**
 * 用户信息 VO
 */
export interface MyUserInfo {
  /** 用户ID */
  userId: string

  /** 用户名 */
  userName: string

  /** 手机号 */
  phone: string

  /** 国家代码 */
  countryCode: string

  /** 昵称 */
  nickName: string

  /** 头像地址 */
  avatarUrl: string

  /** 状态（0=正常 1=异常 2=禁用） */
  status: string

  /** 性别（0=未知 1=男 2=女） */
  sex: string

  /** 生日（格式：yyyy-MM-dd HH:mm:ss） */
  birthday: date

  /** 职业 */
  occupation: string

  /** 个人简介 */
  introductory: string

  /** IP属地 */
  ipAddress: string

  /** 创建时间（格式：yyyy-MM-dd HH:mm:ss） */
  createTime: date

  /** 赚取总积分  */
  pointsEarned: number

  /** 使用总积分 */
  pointsUsed: number

  /** 剩余积分 */
  pointsBalance: number

  /** 充值金额 */
  rechargeAmount: number

  /** 登录日志列表 */
  loginLogInfoVos: MyLoginLogInfo[]
}

/**
 * 登录日志信息 VO
 */
export interface MyLoginLogInfo {
  /** 编号 */
  infoId: string

  /** 登录方式 */
  loginType: string

  /** 登录地点 */
  loginLocation: string

  /** IP地址 */
  ipaddr: string

  /** 登录时间（格式：yyyy-MM-dd HH:mm:ss） */
  loginTime: string
}

/**
 * 用户信息修改参数
 */
export interface UserInfoUpdate {
  /** 用户ID */
  userId: string

  /** 昵称 */
  nickName: string

  /** 性别（0=未知 1=男 2=女） */
  sex: string

  /** 生日（格式：yyyy-MM-dd） */
  birthday?: date

  /** 职业 */
  occupation?: string

  /** 个人简介 */
  introductory?: string
}

// 枚举定义 用户状态枚举
export enum UUserStatusEnum {
  USER_STATUS_0 = '0', // 正常
  USER_STATUS_1 = '1', // 异常
  USER_STATUS_2 = '2', // 禁用
}

// 获取标签的函数 用户状态枚举
export function getUserStatusLabel(value: string): string | undefined {
  switch (value) {
    case UUserStatusEnum.USER_STATUS_0:
      return '正常'
    case UUserStatusEnum.USER_STATUS_1:
      return '异常'
    case UUserStatusEnum.USER_STATUS_2:
      return '禁用'
    default:
      return undefined
  }
}

// 获取枚举值的函数（根据字符串值） 用户状态枚举
export function getUserStatusByValue(value: string): UUserStatusEnum | undefined {
  if (Object.values(UUserStatusEnum).includes(value as UUserStatusEnum)) {
    return value as UUserStatusEnum
  }
  return undefined
}

// 枚举定义 用户性别枚举
export enum UUserSexEnum {
  USER_SEX_0 = '0', // 未知
  USER_SEX_1 = '1', // 男
  USER_SEX_2 = '2', // 女
}

// 获取标签的函数 用户性别枚举
export function getUserSexLabel(value: string): string | undefined {
  switch (value) {
    case UUserSexEnum.USER_SEX_0:
      return '未知'
    case UUserSexEnum.USER_SEX_1:
      return '男'
    case UUserSexEnum.USER_SEX_2:
      return '女'
    default:
      return undefined
  }
}

// 获取枚举值的函数（根据字符串值） 用户性别枚举
export function getUserSexByValue(value: string): UUserSexEnum | undefined {
  if (Object.values(UUserSexEnum).includes(value as UUserSexEnum)) {
    return value as UUserSexEnum
  }
  return undefined
}
