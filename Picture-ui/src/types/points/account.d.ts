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
  pointsEarned: number;

  /** 使用总积分 */
  pointsUsed: number;

  /** 充值总金额（单位：元） */
  rechargeAmount: number;

  /** 账户状态（0=正常, 1=异常, 2=禁用） */
  accountStatus: string;

  /** 积分余额 */
  pointsBalance: number;
}
