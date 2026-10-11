/**
 * 图片申请信息添加请求参数
 * 对应后端类：PictureApplyInfoAdd
 */
export interface PictureApplyInfoAdd {
  /** 图片编号 */
  pictureId: string

  /** 申请类型 */
  applyType: string

  /** 申请理由 */
  applyReason: string

  /** 证明图片 */
  applyImage: string

  /** 证明文件 */
  applyFile: string

  /** 联系方式 */
  contact: string

  /** 所需积分 */
  pointsNeed: number

  /** 所需金额 */
  priceNeed: number
}

// 枚举定义
export enum PictureApplyStatusEnum {
  PICTURE_APPLY_STATUS_0 = '0', // 待审核
  PICTURE_APPLY_STATUS_1 = '1', // 同意
  PICTURE_APPLY_STATUS_2 = '2', // 拒绝
}

// 获取标签的函数
export function getPictureApplyStatusLabel(value: PictureApplyStatusEnum): string | undefined {
  switch (value) {
    case PictureApplyStatusEnum.PICTURE_APPLY_STATUS_0:
      return '待审核'
    case PictureApplyStatusEnum.PICTURE_APPLY_STATUS_1:
      return '同意'
    case PictureApplyStatusEnum.PICTURE_APPLY_STATUS_2:
      return '拒绝'
    default:
      return undefined
  }
}

// 获取枚举值的函数（根据字符串值）
export function getPictureApplyStatusByValue(value: string): PictureApplyStatusEnum | undefined {
  if (Object.values(PictureApplyStatusEnum).includes(value as PictureApplyStatusEnum)) {
    return value as PictureApplyStatusEnum
  }
  return undefined
}

// 枚举定义
export enum PictureApplyTypeEnum {
  PICTURE_APPLY_TYPE_0 = '0', // 原创作品
  PICTURE_APPLY_TYPE_1 = '1', // 转载资源
  PICTURE_APPLY_TYPE_2 = '2', // 无版权资源
}

// 获取标签的函数
export function getPictureApplyTypeLabel(value: PictureApplyTypeEnum): string | undefined {
  switch (value) {
    case PictureApplyTypeEnum.PICTURE_APPLY_TYPE_0:
      return '原创作品'
    case PictureApplyTypeEnum.PICTURE_APPLY_TYPE_1:
      return '转载资源'
    case PictureApplyTypeEnum.PICTURE_APPLY_TYPE_2:
      return '无版权资源'
    default:
      return undefined
  }
}

// 获取枚举值的函数（根据字符串值）
export function getPictureApplyTypeByValue(value: string): PictureApplyTypeEnum | undefined {
  if (Object.values(PictureApplyTypeEnum).includes(value as PictureApplyTypeEnum)) {
    return value as PictureApplyTypeEnum
  }
  return undefined
}
