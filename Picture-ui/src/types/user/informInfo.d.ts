// 是否已读枚举
export enum UInformIsReadEnum {
  IS_READ_0 = "0", // 未读
  IS_READ_1 = "1", // 已读
}

export function getInformIsReadLabel(value: string): string | undefined {
  switch (value) {
    case UInformIsReadEnum.IS_READ_0:
      return "未读";
    case UInformIsReadEnum.IS_READ_1:
      return "已读";
    default:
      return undefined;
  }
}

export function getInformIsReadByValue(value: string): UInformIsReadEnum | undefined {
  if (Object.values(UInformIsReadEnum).includes(value as UInformIsReadEnum)) {
    return value as UInformIsReadEnum;
  }
  return undefined;
}

// 通知状态枚举
export enum UInformStatusEnum {
  INFORM_STATUS_0 = "0", // 待发送
  INFORM_STATUS_1 = "1", // 已发送
  INFORM_STATUS_2 = "2", // 发送失败
  INFORM_STATUS_3 = "3", // 撤回
}

export function getInformStatusLabel(value: string): string | undefined {
  switch (value) {
    case UInformStatusEnum.INFORM_STATUS_0:
      return "待发送";
    case UInformStatusEnum.INFORM_STATUS_1:
      return "已发送";
    case UInformStatusEnum.INFORM_STATUS_2:
      return "发送失败";
    case UInformStatusEnum.INFORM_STATUS_3:
      return "撤回";
    default:
      return undefined;
  }
}

export function getInformStatusByValue(value: string): UInformStatusEnum | undefined {
  if (Object.values(UInformStatusEnum).includes(value as UInformStatusEnum)) {
    return value as UInformStatusEnum;
  }
  return undefined;
}

// 通知类型枚举
export enum UInformTypeEnum {
  INFORM_TYPE_0 = "0", // 通知
  INFORM_TYPE_1 = "1", // 公告
}

export function getInformTypeLabel(value: string): string | undefined {
  switch (value) {
    case UInformTypeEnum.INFORM_TYPE_0:
      return "通知";
    case UInformTypeEnum.INFORM_TYPE_1:
      return "公告";
    default:
      return undefined;
  }
}

export function getInformTypeByValue(value: string): UInformTypeEnum | undefined {
  if (Object.values(UInformTypeEnum).includes(value as UInformTypeEnum)) {
    return value as UInformTypeEnum;
  }
  return undefined;
}
