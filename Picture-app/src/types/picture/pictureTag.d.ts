/**
 * 图片标签查询参数
 */
export interface PictureTagInfoQuery {
  /** 标签编号 */
  tagId?: string

  /** 标签名称 */
  name?: string

  /** 标签状态 */
  tagsStatus?: string

  /** 所属用户 */
  userId?: string

  /** 创建时间（格式：yyyy-MM-dd） */
  createTime?: string

  /** 更新时间（格式：yyyy-MM-dd） */
  updateTime?: string

  pageNum?: number
  pageSize?: number

  /** 请求参数（可选扩展参数） */
  params?: Record<string, any>
}

/**
 * 图片标签信息视图对象
 */
export interface PictureTagInfoVo {
  /** 标签编号 */
  tagId?: string

  /** 标签名称 */
  name?: string

  /** 标签状态 */
  tagsStatus?: string

  /** 标签描述 */
  tagDesc?: string

  /** 使用次数 */
  usageCount?: number

  /** 查看次数 */
  lookCount?: number

  /** 下载次数 */
  downloadCount?: number

  /** 所属用户 */
  userId?: string

  /** 创建时间（格式：yyyy-MM-dd HH:mm:ss） */
  createTime?: string

  /** 更新时间（格式：yyyy-MM-dd HH:mm:ss） */
  updateTime?: string
}
