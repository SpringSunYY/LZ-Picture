/**
 * 用户通知记录查询参数对象
 */
export interface InformInfoQuery {
  /** 模版类型（1=短信 2=邮件 3=站内通知 4=APP推送 5=微信模板） */
  templateType?: string | null;

  /** 通知标题 */
  informTitle?: string;

  /** 用户编号 */
  userId?: string;

  /** 通知类型 */
  informType?: string | null;

  /** 是否已读（0=未读 1=已读） */
  isRead?: string | null;

  /** 页码 */
  pageNum?: number;

  /** 每页数量 */
  pageSize?: number;

  /** 额外参数 */
  [key: string]: any;
}

/**
 * 用户通知记录VO对象
 */
export interface InformInfoVo {
  /** 通知记录编号 */
  recordId: string;

  /** 通知标题 */
  informTitle: string;

  /** 模版类型（1=短信 2=邮件 3=站内通知 4=APP推送 5=微信模板） */
  templateType: string;

  /** 实际发送内容 */
  content: string;

  /** 通知类型（1=公共, 0=通知） */
  informType: string;

  /** 是否已读（0=未读, 1=已读） */
  isRead: number;

  /** 发送时间 */
  sendTime: string;
}

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


// 枚举定义 消息模板类型枚举
export enum CTemplateTypeEnum {
  TEMPLATE_TYPE_0 = "0", // 其他
  TEMPLATE_TYPE_1 = "1", // 短信
  TEMPLATE_TYPE_2 = "2", // 邮件
  TEMPLATE_TYPE_3 = "3", // 站内通知
  TEMPLATE_TYPE_4 = "4", // APP推送
  TEMPLATE_TYPE_5 = "5", // 微信
}

// 获取标签的函数 消息模板类型枚举
export function getCTemplateTypeLabel(value: string): string | undefined {
  switch (value) {
    case CTemplateTypeEnum.TEMPLATE_TYPE_0: return "其他";
    case CTemplateTypeEnum.TEMPLATE_TYPE_1: return "短信";
    case CTemplateTypeEnum.TEMPLATE_TYPE_2: return "邮件";
    case CTemplateTypeEnum.TEMPLATE_TYPE_3: return "站内通知";
    case CTemplateTypeEnum.TEMPLATE_TYPE_4: return "APP推送";
    case CTemplateTypeEnum.TEMPLATE_TYPE_5: return "微信";
    default: return undefined;
  }
}

// 获取枚举值的函数 消息模板类型枚举
export function getCTemplateTypeByValue(value: string): CTemplateTypeEnum | undefined {
  if (Object.values(CTemplateTypeEnum).includes(value as CTemplateTypeEnum)) {
    return value as CTemplateTypeEnum;
  }
  return undefined;
}
