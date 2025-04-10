import type { PageDomain } from '@/types/common'

/**
 * 用户上传图片添加对象
 */
export interface PictureInfo {
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
