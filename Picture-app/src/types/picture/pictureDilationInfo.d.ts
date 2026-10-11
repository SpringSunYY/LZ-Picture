/**
 * 用户添加空间扩容参数
 * 对应后端：SpaceDilatationInfoAdd
 */
export interface SpaceDilatationInfoAdd {
  /** 空间编号 */
  spaceId: string

  /** 扩容类型 */
  dilatationType: string

  /** 扩容总数 */
  dilatationTotal?: number

  /** 积分消耗 */
  pointsTotal?: number,
  /** 单价 */
  dilatationUnit?: number
}

/**
 * 空间扩容计算结果返回对象
 * 对应后端：SpaceDilatationInfoCalculationVo
 */
export interface SpaceDilatationInfoCalculationVo {
  /** 扩容类型 */
  dilatationType: string;

  /** 扩容单价（积分） */
  dilatationUnit: number;

  /** 扩容总数 */
  dilatationTotal: number;

  /** 消耗积分 */
  pointsTotal: number;
}


// 枚举定义
export enum PSpaceDilatationTypeEnum {
  P_SPACE_DILATATION_TYPE_0 = '0', // 容量扩容
  P_SPACE_DILATATION_TYPE_1 = '1', // 数量扩容
  P_SPACE_DILATATION_TYPE_2 = '2', // 人数扩容
}

// 获取标签函数
export function getSpaceDilatationTypeLabel(value: PSpaceDilatationTypeEnum): string | undefined {
  switch (value) {
    case PSpaceDilatationTypeEnum.P_SPACE_DILATATION_TYPE_0:
      return '容量扩容'
    case PSpaceDilatationTypeEnum.P_SPACE_DILATATION_TYPE_1:
      return '数量扩容'
    case PSpaceDilatationTypeEnum.P_SPACE_DILATATION_TYPE_2:
      return '人数扩容'
    default:
      return undefined
  }
}

// 获取枚举值函数
export function getSpaceDilatationTypeByValue(value: string): PSpaceDilatationTypeEnum | undefined {
  if (Object.values(PSpaceDilatationTypeEnum).includes(value as PSpaceDilatationTypeEnum)) {
    return value as PSpaceDilatationTypeEnum
  }
  return undefined
}
