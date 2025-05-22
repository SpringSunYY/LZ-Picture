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
