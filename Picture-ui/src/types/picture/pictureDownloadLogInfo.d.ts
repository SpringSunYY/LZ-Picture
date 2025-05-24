/**
 * 是否免费下载枚举
 */
export enum PDownloadIsFreeEnum {
  DOWNLOAD_IS_FREE_0 = '0', // 是
  DOWNLOAD_IS_FREE_1 = '1', // 否
}

export function getDownloadIsFreeLabel(value: string): string | undefined {
  switch (value) {
    case PDownloadIsFreeEnum.DOWNLOAD_IS_FREE_0:
      return '是'
    case PDownloadIsFreeEnum.DOWNLOAD_IS_FREE_1:
      return '否'
    default:
      return undefined
  }
}

export function getDownloadIsFreeByValue(value: string): PDownloadIsFreeEnum | undefined {
  if (Object.values(PDownloadIsFreeEnum).includes(value as PDownloadIsFreeEnum)) {
    return value as PDownloadIsFreeEnum
  }
  return undefined
}

/**
 * 下载来源类型枚举
 */
export enum PDownloadReferSourceEnum {
  DOWNLOAD_REFER_SOURCE_0 = '0', // 其他
  DOWNLOAD_REFER_SOURCE_1 = '1', // 详情
  DOWNLOAD_REFER_SOURCE_2 = '2', // 分享
}

export function getDownloadReferSourceLabel(value: string): string | undefined {
  switch (value) {
    case PDownloadReferSourceEnum.DOWNLOAD_REFER_SOURCE_0:
      return '其他'
    case PDownloadReferSourceEnum.DOWNLOAD_REFER_SOURCE_1:
      return '详情'
    case PDownloadReferSourceEnum.DOWNLOAD_REFER_SOURCE_2:
      return '分享'
    default:
      return undefined
  }
}

export function getDownloadReferSourceByValue(value: string): PDownloadReferSourceEnum | undefined {
  if (Object.values(PDownloadReferSourceEnum).includes(value as PDownloadReferSourceEnum)) {
    return value as PDownloadReferSourceEnum
  }
  return undefined
}


