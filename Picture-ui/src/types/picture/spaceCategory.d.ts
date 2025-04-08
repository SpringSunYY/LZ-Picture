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

  /** 更新时间（格式：yyyy-MM-dd） */
  updateTime?: string

  /** 删除标记（0否 1是） */
  isDelete?: string

  /** 请求参数（可选扩展参数） */
  params?: Record<string, any>
}
