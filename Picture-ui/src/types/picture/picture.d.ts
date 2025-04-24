import type { PageDomain } from '@/types/common'
import type { UserVo } from '@/types/user'

/**
 * 用户上传图片添加对象
 */
export interface PictureInfo {
  /** 图片编号 */
  pictureId?: string
  /** 图片URL */
  pictureUrl: string
  /** 域名URL */
  dnsUrl?: string

  /** 图片名称 */
  name: string

  /** 简介 */
  introduction?: string

  /** 分类编号 */
  categoryId?: string

  /** 图片体积（字节） */
  picSize?: number

  /** 图片宽度 */
  picWidth?: number

  /** 图片高度 */
  picHeight?: number

  /** 图片比例 */
  picScale?: number
  /** 图片格式 */
  picFormat?: string

  /** 所需积分 */
  pointsNeed?: number

  /** 图片状态（0公共 1私有） */
  pictureStatus?: string

  /** 缩略图URL */
  thumbnailUrl?: string

  /** 所属空间编号 */
  spaceId: string

  /** 图片标签 */
  tags?: string[]

  /** 所属文件夹编号 */
  folderId?: string

  /** 图片主色调（十六进制代码） */
  picColor?: string
}

/**
 * 用户图片信息查询对象
 */
export interface PictureInfoQuery extends PageDomain {
  /** 图片编号 */
  pictureId?: string

  /** 图片名称 */
  name?: string

  /** 分类编号 */
  categoryId?: string

  /** 图片体积 */
  picSize?: number

  /** 图片宽度 */
  picWidth?: number

  /** 图片高度 */
  picHeight?: number

  /** 宽高比例 */
  picScale?: number

  /** 图片格式 */
  picFormat?: string

  /** 上传用户编号 */
  userId?: string

  /** 创建时间（yyyy-MM-dd） */
  createTime?: string

  /** 所属空间编号 */
  spaceId?: string

  /** 所属文件夹编号 */
  folderId?: string

  /** 额外查询参数 */
  params?: Record<string, any>
}

/**
 * 用户图片信息展示对象
 */
export interface PictureInfoVo {
  /** 图片编号 */
  pictureId?: string

  /** 图片名称 */
  name?: string

  /** 分类编号 */
  categoryId?: string

  /** 图片宽度 */
  picWidth?: number

  /** 图片高度 */
  picHeight?: number

  /** 图片比例 */
  picScale?: float

  /** 上传用户编号 */
  userId?: string

  /** 缩略图URL */
  thumbnailUrl?: string

  /** 所属空间编号 */
  spaceId?: string

  /** 所属文件夹编号 */
  folderId?: string
}

/**
 * 用户图片信息详情对象
 */
export interface PictureDetailInfoVo {
  /** 图片编号 */
  pictureId: string

  /** 图片名称 */
  name?: string

  /** 图片简介 */
  introduction?: string

  /** 分类编号 */
  categoryId?: string

  /** 分类名称 */
  categoryName?: string

  /** 图片宽度 */
  picWidth?: number

  /** 图片高度 */
  picHeight?: number

  /** 图片体积（字节） */
  picSize?: number

  /** 宽高比例 */
  picScale?: number

  /** 图片格式 */
  picFormat?: string

  /** 所需积分 */
  pointsNeed?: number

  /** 上传用户编号 */
  userId?: string

  /** 上传用户名称 */
  userName?: string

  /** 用户信息 */
  userInfoVo?: UserVo

  /** 缩略图URL */
  thumbnailUrl?: string

  /** 所属空间编号 */
  spaceId?: string

  /** 所属空间名称 */
  spaceName?: string

  /** 所属文件夹编号 */
  folderId?: string

  /**创建时间 */
  createTime?: string

  /** 更多信息 */
  moreInfo?: Record<string, string>

  /** 图片标签 */
  pictureTags?: string[]

  /** 喜欢总数 */
  likeCount?: number
  /** 是否喜欢 */
  isLike?: boolean
  /** 分享总数 */
  shareCount?: number
  /**收藏总数 */
  collectCount?: number
  /** 是否收藏 */
  isCollect?: boolean
  /** 下载总数 */
  downloadCount?: number
  /** 评论总数 */
  commentCount?: number
}

/**
 * 图片信息对象，用于展示用户自己的图片列表
 */
export interface MyPictureInfoVo {
  /**
   * 图片编号
   */
  pictureId: string;

  /**
   * 图片名称
   */
  name: string;

  /**
   * 简介
   */
  introduction: string;

  /**
   * 分类编号
   */
  categoryId: string;

  /**
   * 图片体积
   */
  picSize: number;

  /**
   * 图片宽度
   */
  picWidth: number;

  /**
   * 图片高度
   */
  picHeight: number;

  /**
   * 宽高比例
   */
  picScale: number;

  /**
   * 图片格式
   */
  picFormat: string;

  /**
   * 上传用户编号
   */
  userId: string;

  /**
   * 创建时间，格式：yyyy-MM-dd HH:mm:ss
   */
  createTime: string;

  /**
   * 图片状态（0公共 1私有）
   */
  pictureStatus: string;

  /**
   * 审核状态（0待审核 1通过 2拒绝）
   */
  reviewStatus: string;

  /**
   * 缩略图URL
   */
  thumbnailUrl: string;
}

// 图片状态 枚举
export enum PPictureStatus {
  PICTURE_STATUS_0 = "0", // 公共
  PICTURE_STATUS_1 = "1", // 私有
}

// 图片状态的标签映射
export const PPictureStatusLabel: { [key in PPictureStatus]: string } = {
  [PPictureStatus.PICTURE_STATUS_0]: "公共",
  [PPictureStatus.PICTURE_STATUS_1]: "私有",
};

// 根据值获取标签
export function getPictureStatusLabel(value: string): string | undefined {
  return PPictureStatusLabel[value];
}

// 根据字符串值获取枚举项
export function getPictureStatusByValue(value: string): PPictureStatus | undefined {
  if (Object.values(PPictureStatus).includes(value as PPictureStatus)) {
    return value as PPictureStatus;
  }
  return undefined;
}

// 图片审核状态 枚举
export enum PPictureReviewStatus {
  PICTURE_REVIEW_STATUS_0 = "0", // 待审核
  PICTURE_REVIEW_STATUS_1 = "1", // 同意
  PICTURE_REVIEW_STATUS_2 = "2", // 拒绝
}

// 审核状态的标签映射
export const PPictureReviewStatusLabel: { [key in PPictureReviewStatus]: string } = {
  [PPictureReviewStatus.PICTURE_REVIEW_STATUS_0]: "待审核",
  [PPictureReviewStatus.PICTURE_REVIEW_STATUS_1]: "同意",
  [PPictureReviewStatus.PICTURE_REVIEW_STATUS_2]: "拒绝",
};

// 获取状态标签
export function getPictureReviewStatusLabel(value: string): string | undefined {
  return PPictureReviewStatusLabel[value];
}

// 通过字符串获取枚举值
export function getPictureReviewStatusByValue(value: string): PPictureReviewStatus | undefined {
  if (Object.values(PPictureReviewStatus).includes(value as PPictureReviewStatus)) {
    return value as PPictureReviewStatus;
  }
  return undefined;
}
