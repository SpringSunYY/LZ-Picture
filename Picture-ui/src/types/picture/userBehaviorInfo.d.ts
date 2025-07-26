/**
 * 用户行为信息添加对象
 */
export interface UserBehaviorInfoAdd {
  /** 行为类型（不能为空） */
  behaviorType: string

  /** 目标类型（不能为空） */
  targetType: string

  /** 目标对象ID（不能为空） */
  targetId: string

  /** 分享链接 */
  shareLink?: string
}

/**
 * 用户行为信息对象
 */
export interface UserBehaviorInfo {
  /** 行为编号 */
  behaviorId?: string

  /** 行为类型 */
  behaviorType?: string

  /** 用户编号 */
  userId?: string

  /** 目标类型 */
  targetType?: string

  /** 目标对象ID */
  targetId?: string

  /** 目标内容 */
  targetContent?: string

  /** 分数 */
  score?: number

  /** 分享链接 */
  shareLink?: string

  /** 图片分类ID */
  categoryId?: string

  /** 空间ID */
  spaceId?: string

  /** 图片标签（多个标签以字符串形式保存） */
  tags?: string

  /** 封面图URL */
  targetCover?: string

  /** 创建时间 */
  createTime?: string // ISO 日期字符串，例如 '2025-04-11T10:00:00'

  /** 设备唯一标识 */
  deviceId?: string

  /** 浏览器类型 */
  browser?: string

  /** 操作系统 */
  os?: string

  /** 平台 */
  platform?: string

  /** IP属地 */
  ipAddress?: string
}

/**
 * 用户行为Vo对象（p_user_behavior_info）
 * @date 2025-04-12
 */
export interface UserBehaviorInfoVo {
  /** 行为编号 */
  behaviorId?: string

  /** 行为类型 */
  behaviorType?: string

  /** 目标类型 */
  targetType?: string

  /** 目标对象 */
  targetId?: string

  /** 目标内容 */
  targetContent?: string

  /** 空间 */
  spaceId?: string

  /** 图片标签 */
  tags?: string

  /** 封面 */
  targetCover?: string

  /** 创建时间（格式：yyyy-MM-dd HH:mm:ss） */
  createTime?: string
}

/**
 * 用户行为查询参数
 */
export interface UserBehaviorInfoQuery {
  /** 用户编号 */
  userId?: string

  /** 行为类型 */
  behaviorType?: string | null

  /** 目标类型 */
  targetType?: string | null

  /** 目标内容 */
  targetContent?: string

  /** 请求附加参数（非数据库字段） */
  params?: Record<string, any>

  /** 分页参数：页码 */
  pageNum?: number

  /** 分页参数：每页数量 */
  pageSize?: number

  /** 排序字段 */
  orderByColumn?: string

  /** 排序方式（asc 或 desc） */
  isAsc?: string
}

// 用户行为类型枚举
enum PUserBehaviorType {
  USER_BEHAVIOR_TYPE_0 = '0', // 点赞
  USER_BEHAVIOR_TYPE_1 = '1', // 收藏
  USER_BEHAVIOR_TYPE_2 = '2', // 转发
}

// 获取枚举标签的函数
function getUserBehaviorLabel(value: PUserBehaviorType): string | undefined {
  switch (value) {
    case PUserBehaviorType.USER_BEHAVIOR_TYPE_0:
      return '点赞'
    case PUserBehaviorType.USER_BEHAVIOR_TYPE_1:
      return '收藏'
    case PUserBehaviorType.USER_BEHAVIOR_TYPE_2:
      return '转发'
    default:
      return undefined
  }
}

// 获取枚举值的函数
function getUserBehaviorByValue(value: string): PUserBehaviorType | undefined {
  if (Object.values(PUserBehaviorType).includes(value as PUserBehaviorType)) {
    return value as PUserBehaviorType
  }
  return undefined
}

export { PUserBehaviorType, getUserBehaviorLabel, getUserBehaviorByValue }

// 枚举定义 用户行为目标类型枚举
export enum PUserBehaviorTargetTypeEnum {
  USER_BEHAVIOR_TARGET_TYPE_0 = '0', // 图片
  USER_BEHAVIOR_TARGET_TYPE_1 = '1', // 空间
  USER_BEHAVIOR_TARGET_TYPE_2 = '2', // 用户
}

// 获取标签的函数 用户行为目标类型枚举
export function getUserBehaviorTargetTypeLabel(value: string): string | undefined {
  switch (value) {
    case PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0:
      return '图片'
    case PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_1:
      return '空间'
    case PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_2:
      return '用户'
    default:
      return undefined
  }
}

// 获取枚举值的函数（根据字符串值） 用户行为目标类型枚举
export function getUserBehaviorTargetTypeByValue(
  value: string,
): PUserBehaviorTargetTypeEnum | undefined {
  if (Object.values(PUserBehaviorTargetTypeEnum).includes(value as PUserBehaviorTargetTypeEnum)) {
    return value as PUserBehaviorTargetTypeEnum
  }
  return undefined
}
