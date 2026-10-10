/**
 * 用户账户更改密码请求
 * Version: 1.0
 */
export interface AccountPasswordUploadRequest {
  /** 用户ID（不能为空） */
  userId: string

  /** 旧密码（不能为空，长度 8~20 个字符） */
  oldPassword: string

  /** 新密码（不能为空，长度 8~20 个字符） */
  password: string

  /** 确认密码（不能为空，长度 8~20 个字符） */
  confirmPassword: string
}

/**
 * 用户积分账户VO对象
 */
export interface AccountInfoVo {
  /** 赚取总积分 */
  pointsEarned: number

  /** 使用总积分 */
  pointsUsed: number

  /** 充值总金额（单位：元） */
  rechargeAmount: number

  /** 账户状态（0=正常, 1=异常, 2=禁用） */
  accountStatus: string

  /** 积分余额 */
  pointsBalance: number
}

/**
 * 用户重置账户密码验证码对象
 */
export interface ResetAccountPasswordCode {
  /** 国家代码（如 +86） */
  countryCode: string

  /** 手机号码 */
  phone: string

  /** 验证码（可选） */
  code?: string

  /** 是否启用验证码 */
  captchaEnabled?: boolean

  /** 唯一标识（用于验证码验证） */
  uuid?: string

  userId: string
}

/**
 * 忘记密码（重置账户密码）请求参数
 */
export interface ResetAccountPasswordBody {
  /** 国家代码（如 +86） */
  countryCode: string

  /** 手机号码 */
  phone: string

  /** 短信验证码 */
  smsCode: string

  /** 图形验证码（可选） */
  code?: string

  /** 是否启用验证码 */
  captchaEnabled?: boolean

  /** 图形验证码唯一标识 */
  uuid?: string

  /** 新密码 */
  password: string

  /** 确认密码 */
  confirmPassword: string

  userId: string
}
