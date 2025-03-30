export type SpaceAdd = {
  /** 空间名称 */
  spaceName: string

  /** 空间封面URL */
  spaceAvatar?: string

  /** 所属用户 */
  userId?: string

  /** 空间描述 */
  spaceDesc?: string

  /** 空间状态 */
  spaceStatus: string

  /** 空间类型（0个人 1团队 2官方） */
  spaceType: string
}
