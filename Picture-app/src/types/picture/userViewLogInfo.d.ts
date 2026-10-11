/**
 * 用户浏览记录查询参数（用户自己查询）
 */
export interface UserViewLogInfoQuery {
  /** 当前页码 */
  pageNum?: number

  /** 每页条数 */
  pageSize?: number

  /** 用户编号 */
  userId?: string

  /** 目标类型（如：图片/空间/用户等） */
  targetType?: string | null

  /** 目标内容 */
  targetContent?: string

  /** 查看时间（格式：yyyy-MM-dd） */
  createTime?: string

  /** 其他请求参数 */
  params?: Record<string, any>
}

/**
 * 用户浏览记录响应对象
 */
export interface UserViewLogInfoVo {
  /** 记录编号 */
  viewId: string

  /** 目标类型（图片、空间、用户等） */
  targetType: string

  /** 目标对象ID */
  targetId: string

  /** 目标内容（通常为标题或简述） */
  targetContent: string

  /** 图片标签（逗号分隔） */
  tags: string

  /** 封面图 URL */
  targetCover: string

  /** 查看时间（格式：yyyy-MM-dd HH:mm:ss） */
  createTime: string
}

// 枚举定义 浏览记录目标类型枚举
export enum PViewLogTargetTypeEnum {
  VIEW_LOG_TARGET_TYPE_0 = "0", // 图片
  VIEW_LOG_TARGET_TYPE_1 = "1", // 空间
  VIEW_LOG_TARGET_TYPE_2 = "2", // 用户
}

// 获取标签的函数 浏览记录目标类型枚举
export function getViewLogTargetTypeLabel(value: string): string | undefined {
  switch (value) {
    case PViewLogTargetTypeEnum.VIEW_LOG_TARGET_TYPE_0:
      return "图片";
    case PViewLogTargetTypeEnum.VIEW_LOG_TARGET_TYPE_1:
      return "空间";
    case PViewLogTargetTypeEnum.VIEW_LOG_TARGET_TYPE_2:
      return "用户";
    default:
      return undefined;
  }
}

// 获取枚举值的函数（根据字符串值） 浏览记录目标类型枚举
export function getViewLogTargetTypeByValue(value: string): PViewLogTargetTypeEnum | undefined {
  if (Object.values(PViewLogTargetTypeEnum).includes(value as PViewLogTargetTypeEnum)) {
    return value as PViewLogTargetTypeEnum;
  }
  return undefined;
}

