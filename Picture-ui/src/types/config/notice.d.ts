/**
 * 用户公告查询请求对象
 */
export interface NoticeInfoRequest extends PageDomain {
  /** 语言，默认 zh-CN */
  locale?: string

  /** 通知平台 */
  platform?: string

  /** 公告类型 */
  noticeType?: string | null

  /** 公告标题 */
  noticeTitle?: string

  /**分页 */
  pageNum?: number
  pageSize?: number
}

/**
 * 用户公告信息对象
 */
export interface NoticeInfoVo {
  /** 公告编号 */
  noticeId: string

  /** 公告类型 */
  noticeType: string

  /** 公告标题 */
  noticeTitle: string

  /** 公告内容 */
  content: string

  /** 创建时间，格式为 yyyy-MM-dd HH:mm:ss */
  createTime: string
}

// 枚举定义
export enum CNoticeTypeEnum {
  NOTICE_TYPE_0 = '0', // 其他
  NOTICE_TYPE_1 = '1', // 平台推送
  NOTICE_TYPE_2 = '2', // 用户须知
}


const CNoticeTypeLabels: Record<string, string> = {
  "0": "其他",
  "1": "平台推送",
  "2": "用户须知",
};

export function getCNoticeTypeLabel(value: string): string | undefined {
  return CNoticeTypeLabels[value];
}


// 获取枚举值函数
export function getCNoticeTypeByValue(value: string): CNoticeTypeEnum | undefined {
  if (Object.values(CNoticeTypeEnum).includes(value as CNoticeTypeEnum)) {
    return value as CNoticeTypeEnum
  }
  return undefined
}

// 枚举定义
export enum CNoticePlatformEnum {
  NOTICE_PLATFORM_0 = '0', // 全部
  NOTICE_PLATFORM_1 = '1', // Web
  NOTICE_PLATFORM_2 = '2', // 小程序
}

// 获取标签函数
export function getCNoticePlatformLabel(value: CNoticePlatformEnum): string | undefined {
  switch (value) {
    case CNoticePlatformEnum.NOTICE_PLATFORM_0:
      return '全部'
    case CNoticePlatformEnum.NOTICE_PLATFORM_1:
      return 'Web'
    case CNoticePlatformEnum.NOTICE_PLATFORM_2:
      return '小程序'
    default:
      return undefined
  }
}

// 获取枚举值函数
export function getCNoticePlatformByValue(value: string): CNoticePlatformEnum | undefined {
  if (Object.values(CNoticePlatformEnum).includes(value as CNoticePlatformEnum)) {
    return value as CNoticePlatformEnum
  }
  return undefined
}


