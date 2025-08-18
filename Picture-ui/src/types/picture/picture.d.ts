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

  /** 图片状态 */
  pictureStatus?: string | null

  /** 额外查询参数 */
  params?: Record<string, any>

  /** 上传类型*/
  uploadType?: string
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
  publishTime?: string

  /** 图片标签 */
  pictureTags?: string[]

  /** 查看次数  */
  lookCount?: number

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
  /** 更多信息 */
  moreInfo?: MoreInfo
}

export interface MoreInfo {
  /** 所需积分 */
  pointsNeed?: number
  /** 所需金额 */
  priceNeed?: number
  /** 申请类型 */
  applyType?: string
  /** 模型信息*/
  modelType?: ModelType
  modelName?: string
  modelKey?: string
  modelPoints?: number
}

/**
 * 图片信息对象，用于展示用户自己的图片列表
 */
export interface MyPictureInfoVo {
  /**
   * 图片编号
   */
  pictureId: string

  /**
   * 图片名称
   */
  name: string

  /**
   * 简介
   */
  introduction: string

  /**
   * 分类编号
   */
  categoryId: string

  /** 空间编号 */
  spaceId: string

  /**
   * 图片体积
   */
  picSize: number

  /**
   * 图片宽度
   */
  picWidth: number

  /**
   * 图片高度
   */
  picHeight: number

  /**
   * 宽高比例
   */
  picScale: number

  /**
   * 图片格式
   */
  picFormat: string

  /**
   * 上传用户编号
   */
  userId: string

  /**
   * 创建时间，格式：yyyy-MM-dd HH:mm:ss
   */
  createTime: string

  /**
   * 图片状态（0公共 1私有）
   */
  pictureStatus: string

  /**
   * 缩略图URL
   */
  thumbnailUrl: string
}

/**
 * 图片搜索推荐结果项
 */
export interface PictureInfoSearchRecommendVo {
  pictureId: string
  /** 图片名称 */
  name: string

  /** 图片简介 */
  introduction: string

  /** 缩略图 URL */
  thumbnailUrl: string
}

/**
 * 图片下载信息返回信息
 */
export interface PictureDownloadVo {
  /** 图片编号 */
  pictureId: string

  /** 图片URL */
  pictureUrl: string

  /** 所需积分 */
  pointsNeed: number

  /** 所需金额 */
  priceNeed: BigDecimal
}

/**
 * 图片URL上传请求参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-13  11:17
 * @Version: 1.0
 */
export interface PictureAiUpload {
  /**
   * 图片名称
   * 不能为空
   */
  name: string

  /**
   * 简介
   */
  introduction?: string

  /**
   * 分类编号
   * 不能为空，且必须是AI下分类
   */
  categoryId: string

  /**
   * 所属空间编号
   * 不能为空
   */
  spaceId: string

  /**
   * 所属文件夹编号
   */
  folderId?: string

  /**
   * 图片标签
   */
  tags?: string[]

  /**
   * 图片状态
   * 不能为空
   */
  pictureStatus: string

  /**
   * 消耗积分
   */
  pointsNeed?: number

  /**
   * 用户编号
   */
  userId?: string

  /**
   * 生成记录编号
   * 不能为空
   */
  logId: string
}

/**
 * 用户推荐的图片信息 VO（前端对应类）
 * 对应后端的 UserRecommendPictureInfoVo
 */
export interface UserRecommendPictureInfoVo {
  /** 图片编号 */
  pictureId: string

  /** 图片名称 */
  name: string

  /** 分类编号 */
  categoryId: string

  /** 图片宽度 */
  picWidth: number

  /** 图片高度 */
  picHeight: number

  /** 宽高比例 */
  picScale: number

  /** 缩略图 URL */
  thumbnailUrl: string
}

/**
 * 用户推荐请求参数 DTO
 * 对应后端的 PictureRecommendRequest
 */
export interface PictureRecommendRequest {
  /** 用户编号 */
  userId?: string

  /** 图片编号 */
  pictureId?: string

  /** 当前记录起始页（从0开始） */
  currentPage?: number

  /** 每页显示记录数（1-50） */
  pageSize: number

  /** 页码*/
  pageNum?: number

  /** 偏移量（可选） */
  offset?: number

  /** 图片名称 */
  name?: string
}

/**
 * 图片搜索建议项
 */
export interface PictureInfoSearchSuggestionVo {
  /** 图片编号 */
  pictureId: string

  /** 图片名称 */
  name: string
}

/**
 * 图片推荐请求参数
 */
export interface PictureInfoRecommendRequest {
  /** 图片编号 */
  pictureId: string

  /** 当前记录起始索引，从0开始 */
  currentPage: number

  /** 每页显示记录数（1~50） */
  pageSize: number
}

/**
 * 图片 URL 上传请求参数
 * 对应后端的 PictureUrlUpload DTO
 */
export interface PictureUrlUpload {
  /** 图片文件地址 */
  url: string

  /** 图片名称 */
  name: string

  /** 简介（可选） */
  introduction?: string

  /** 分类编号 */
  categoryId?: string | null

  /** 所属空间编号 */
  spaceId: string

  /** 所属文件夹编号（可选） */
  folderId?: string | null

  /** 图片标签（可选） */
  tags?: string[]
}

/**
 * 热门图片请求参数
 * 对应后端：PictureInfoHotRequest
 */
export interface PictureInfoHotRequest {
  /** 类型（如：下载量、点赞数等） */
  type?: string

  /** 当前页码（分页） */
  pageNum?: number

  /** 每页记录数（分页） */
  pageSize?: number

  /** 排序列 */
  orderByColumn?: string

  /** 排序方向（asc 或 desc） */
  isAsc?: string
}

/**
 * 图片信息Vo对象 p_picture_info
 */
export interface PictureInfoAiVo {
  /** 图片编号 */
  pictureId: string

  /** 图片URL */
  pictureUrl: string

  /** 图片名称 */
  name: string

  /** 简介 */
  introduction: string

  /** 创建时间 */
  createTime: string // yyyy-MM-dd HH:mm:ss

  /** 发布时间 */
  publishTime: string // yyyy-MM-dd HH:mm:ss

  /** 缩略图URL */
  thumbnailUrl: string

  /** 查看次数 */
  lookCount: number

  /** 收藏次数 */
  collectCount: number

  /** 点赞次数 */
  likeCount: number

  /** 分享次数 */
  shareCount: number

  /** 下载次数 */
  downloadCount: number

  /** 宽度 */
  width: number

  /** 高度 */
  height: number
}

/**
 * 用户图片信息查询对象
 */
export interface PictureInfoAiQuery {
  /** 图片编号 */
  pictureId?: string

  /** 图片名称 */
  name?: string

  /** 上传用户编号 */
  userId?: string

  /** 创建时间 */
  createTime?: string // yyyy-MM-dd

  /** 图片状态 */
  pictureStatus?: string

  /** 用户名*/
  username?: string

  /** 当前页码（分页） */
  pageNum?: number

  /** 每页记录数（分页） */
  pageSize?: number
}

// 图片状态 枚举
export enum PPictureStatus {
  PICTURE_STATUS_0 = '0', // 公共
  PICTURE_STATUS_1 = '1', // 私有
}

// 图片状态的标签映射
export const PPictureStatusLabel: { [key in PPictureStatus]: string } = {
  [PPictureStatus.PICTURE_STATUS_0]: '公共',
  [PPictureStatus.PICTURE_STATUS_1]: '私有',
}

// 根据值获取标签
export function getPictureStatusLabel(value: string): string | undefined {
  return PPictureStatusLabel[value]
}

// 根据字符串值获取枚举项
export function getPictureStatusByValue(value: string): PPictureStatus | undefined {
  if (Object.values(PPictureStatus).includes(value as PPictureStatus)) {
    return value as PPictureStatus
  }
  return undefined
}

export interface PictureInfoUpdate {
  /** 图片编号 */
  pictureId: string
  /** 图片URL */
  pictureUrl: string

  /** 图片名称 */
  name: string

  /** 简介 */
  introduction?: string

  /** 分类编号 */
  categoryId?: string

  /** 所属空间编号 */
  spaceId: string

  /** 所属文件夹编号 */
  folderId?: string

  /** 图片标签 */
  tags?: string[]
}

/**
 * 用户图片信息展示表格数据（对应后端 PictureInfoTableVo）
 */
export interface PictureInfoTableVo {
  /** 图片编号 */
  pictureId: string

  /** 图片名称 */
  name: string

  /** 简介 */
  introduction: string

  /** 分类编号 */
  categoryId: string

  /** 分类名称 */
  categoryName: string

  /** 图片体积（单位：字节） */
  picSize: number

  /** 图片宽度 */
  picWidth: number

  /** 图片高度 */
  picHeight: number

  /** 宽高比例 */
  picScale: number

  /** 图片格式 */
  picFormat: string

  /** 上传用户编号 */
  userId: string

  /** 创建时间 */
  createTime: string // 前端一般接收为 ISO 字符串，可用 dayjs 格式化

  /** 图片状态（0公共 1私有） */
  pictureStatus: string

  /** 缩略图 URL */
  thumbnailUrl: string

  /** 标签集合 */
  tags: string[]
}

/**
 * 用户更新图片名称请求参数
 * 对应后端类：UserPictureInfoUpdateName
 */
export interface PictureInfoUpdateName {
  /** 图片编号 */
  pictureId: string

  /** 图片名称 */
  name: string
}
