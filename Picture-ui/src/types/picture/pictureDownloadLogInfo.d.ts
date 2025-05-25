/**
 * 用户图片下载记录查询参数
 */
export interface PictureDownloadLogInfoQuery {
  /** 用户编号 */
  userId?: string;

  /** 图片名称 */
  pictureName?: string;

  /** 下载状态（1=失败，0=成功） */
  downloadStatus?: string;

  /** 下载方式（0=手动，1=API，2=批量） */
  downloadType?: string;

  /** 来源（0=其他，1=详情页，2=分享页） */
  referSource?: string;

  /** 额外查询参数 */
  params?: Record<string, any>;

  /** 当前页码 */
  pageNum?: number;

  /** 每页条数 */
  pageSize?: number;
}

/**
 * 用户图片下载记录视图对象
 */
export interface PictureDownloadLogInfoVo {
  /** 图片编号 */
  pictureId: string;

  /** 图片名称 */
  pictureName: string;

  /** 缩略图URL */
  thumbnailUrl: string;

  /** 图片标签（格式："标签1","标签2"） */
  tags: string;

  /** 消耗积分 */
  pointsCost: number;

  /** 下载状态（1=失败，0=成功） */
  downloadStatus: string;

  /** 下载方式（0=手动，1=API，2=批量） */
  downloadType: string;

  /** 来源（0=其他，1=详情，2=分享） */
  referSource: string;

  /** 下载时间 */
  createTime: string; // 建议作为字符串处理，方便格式化展示
}
