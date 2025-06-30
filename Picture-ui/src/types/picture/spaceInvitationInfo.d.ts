/**
 * 空间成员邀请记录
 * 对应后端：SpaceInvitationInfoAdd
 */
export interface SpaceInvitationInfoAdd {
  /** 空间编号 */
  spaceId: string

  /** 邀请角色（0创建者 1管理员 2编辑者 3浏览者） */
  roleType: string

  /** 邀请理由 */
  invitation: string

  /** 被邀请用户编号 */
  userName: string
}

/**
 * 空间成员邀请记录Vo对象
 * 对应后端：SpaceInvitationInfoVo
 */
export interface SpaceInvitationInfoVo {
  /** 邀请编号 */
  invitationId: string

  /** 空间编号 */
  spaceId: string

  /** 空间名称 */
  spaceName: string

  /** 空间封面URL */
  spaceAvatar: string

  /** 邀请角色（0创建者 1管理员 2编辑者 3浏览者） */
  roleType: string

  /** 邀请状态（0待同意 1同意 2拒绝 3过期） */
  invitationStatus: string


  /** 邀请理由 */
  invitation: string

  /** 过期时间 */
  expireTime: string // ISO string format, or Date if necessary

  /** 创建时间 */
  createTime: string // ISO string format, or Date if necessary

  /** 被邀请用户编号 */
  userId: string
}

/**
 * 空间成员邀请记录查询参数
 * 对应后端：UserSpaceInvitationInfoQuery
 */
export interface SpaceInvitationInfoQuery {
  /** 空间编号 */
  spaceId?: string;

  /** 空间名称 */
  spaceName?: string;

  /** 邀请角色（0创建者 1管理员 2编辑者 3浏览者） */
  roleType?: string;

  /** 邀请状态（0待同意 1同意 2拒绝 3过期） */
  invitationStatus?: string;


  /** 用户类型 区分是邀请者还是被邀请者 */
  userType?: string;

  /** 请求参数 Map */
  params?: Record<string, any>;

  /** 当前页码（分页） */
  pageNum?: number;

  /** 每页记录数（分页） */
  pageSize?: number;

  /** 排序列 */
  orderByColumn?: string;

  /** 排序方向（asc 或 desc） */
  isAsc?: string;
}

// 枚举定义
export enum PSpaceInvitationStatusEnum {
  P_SPACE_INVITATION_STATUS_0 = '0', // 待同意
  P_SPACE_INVITATION_STATUS_1 = '1', // 同意
  P_SPACE_INVITATION_STATUS_2 = '2', // 拒绝
  P_SPACE_INVITATION_STATUS_3 = '3', // 过期
  P_SPACE_INVITATION_STATUS_4 = '4', // 已取消
}

// 获取标签函数
export function getSpaceInvitationStatusLabel(
  value: PSpaceInvitationStatusEnum,
): string | undefined {
  switch (value) {
    case PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_0:
      return '待同意'
    case PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_1:
      return '同意'
    case PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_2:
      return '拒绝'
    case PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_3:
      return '过期'
    case PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_4:
      return '已取消'
    default:
      return undefined
  }
}

// 获取枚举值函数
export function getSpaceInvitationStatusByValue(
  value: string,
): PSpaceInvitationStatusEnum | undefined {
  if (Object.values(PSpaceInvitationStatusEnum).includes(value as PSpaceInvitationStatusEnum)) {
    return value as PSpaceInvitationStatusEnum
  }
  return undefined
}
