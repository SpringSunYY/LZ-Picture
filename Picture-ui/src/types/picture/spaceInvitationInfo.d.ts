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


// 枚举定义
export enum PSpaceInvitationStatusEnum {
  P_SPACE_INVITATION_STATUS_0 = "0", // 待同意
  P_SPACE_INVITATION_STATUS_1 = "1", // 同意
  P_SPACE_INVITATION_STATUS_2 = "2", // 拒绝
  P_SPACE_INVITATION_STATUS_3 = "3", // 过期
  P_SPACE_INVITATION_STATUS_4 = "4", // 已取消
}

// 获取标签函数
export function getSpaceInvitationStatusLabel(value: PSpaceInvitationStatusEnum): string | undefined {
  switch (value) {
    case PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_0:
      return "待同意";
    case PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_1:
      return "同意";
    case PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_2:
      return "拒绝";
    case PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_3:
      return "过期";
    case PSpaceInvitationStatusEnum.P_SPACE_INVITATION_STATUS_4:
      return "已取消";
    default:
      return undefined;
  }
}

// 获取枚举值函数
export function getSpaceInvitationStatusByValue(value: string): PSpaceInvitationStatusEnum | undefined {
  if (Object.values(PSpaceInvitationStatusEnum).includes(value as PSpaceInvitationStatusEnum)) {
    return value as PSpaceInvitationStatusEnum;
  }
  return undefined;
}
