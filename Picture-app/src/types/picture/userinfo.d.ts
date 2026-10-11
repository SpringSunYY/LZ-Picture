/**
 * 用户信息返回
 */
export interface UserInfoVo {
  /** 用户名 */
  userName: string

  /** 昵称 */
  nickName: string

  /** 头像地址 */
  avatarUrl: string

  /** 性别（0=未知 1=男 2=女） */
  sex: string

  /** 生日 */
  birthday: string // yyyy-MM-dd

  /** 职业 */
  occupation: string

  /** 个人简介 */
  introductory: string

  /** IP属地 */
  ipAddress: string

  /** 图片总数 */
  pictureCount: number

  /** 喜欢总数 */
  likeCount: number

  /** 收藏总数 */
  collectCount: number

  /** 分享 */
  shareCount: number
}
