/**
 * 用户图片分类信息 VO
 */
export interface PictureCategoryInfoVo {
  /** 分类编号 */
  categoryId: string

  /** 父级分类编号 */
  parentId: string

  /** 封面图 URL */
  coverUrl: string

  /** 图片图标 */
  categoryIcon: string

  /** 分类名称 */
  name: string

  /** 分类描述 */
  categoryDesc: string

  /** 子集 */
  children?: PictureCategoryInfoVo[]
}

/**
 * 图片分类查询参数
 */
export interface PictureCategoryInfoQuery {
  /** 分类编号 */
  categoryId?: string

  /** 父级分类编号 */
  parentId?: string

  /** 分类名称 */
  name?: string

  /** 分类状态（0正常 1关闭） */
  categoryStatus?: string

  /** 分类类型 */
  categoryType?: string

  /** 查询状态（0是 1否） */
  queryStatus?: string

  /** 创建时间（格式：yyyy-MM-dd） */
  createTime?: string
}

// 枚举定义
export enum PCategoryQueryStatusEnum {
  P_CATEGORY_QUERY_STATUS_0 = '0', // 是
  P_CATEGORY_QUERY_STATUS_1 = '1', // 否
}

// 获取标签函数
export function getPCategoryQueryStatusLabel(value: PCategoryQueryStatusEnum): string | undefined {
  switch (value) {
    case PCategoryQueryStatusEnum.P_CATEGORY_QUERY_STATUS_0:
      return '是'
    case PCategoryQueryStatusEnum.P_CATEGORY_QUERY_STATUS_1:
      return '否'
    default:
      return undefined
  }
}

// 获取枚举值函数
export function getPCategoryQueryStatusByValue(
  value: string,
): PCategoryQueryStatusEnum | undefined {
  if (Object.values(PCategoryQueryStatusEnum).includes(value as PCategoryQueryStatusEnum)) {
    return value as PCategoryQueryStatusEnum
  }
  return undefined
}

// 枚举定义
export enum PCategoryStatusEnum {
  P_CATEGORY_STATUS_0 = '0', // 正常
  P_CATEGORY_STATUS_1 = '1', // 关闭
}

// 获取标签函数
export function getPCategoryStatusLabel(value: PCategoryStatusEnum): string | undefined {
  switch (value) {
    case PCategoryStatusEnum.P_CATEGORY_STATUS_0:
      return '正常'
    case PCategoryStatusEnum.P_CATEGORY_STATUS_1:
      return '关闭'
    default:
      return undefined
  }
}

// 获取枚举值函数
export function getPCategoryStatusByValue(value: string): PCategoryStatusEnum | undefined {
  if (Object.values(PCategoryStatusEnum).includes(value as PCategoryStatusEnum)) {
    return value as PCategoryStatusEnum
  }
  return undefined
}
