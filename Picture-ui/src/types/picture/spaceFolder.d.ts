export interface SpaceFolderInfo {
  /** 空间编号（不能为空） */
  spaceId: string

  /** 父文件夹编号（不能为空） */
  parentId: string

  /** 文件夹名称（不能为空，最大长度32） */
  folderName: string

  /** 排序权重（不能为空，范围 0-10） */
  sortOrder: number

  /** 备注（可选） */
  remark?: string
}

/**
 * 空间文件夹信息 VO
 */
export interface SpaceFolderInfoVo {
  /** 文件夹编号 */
  folderId: string

  /** 空间编号 */
  spaceId: string

  /** 父文件夹编号 */
  parentId: string

  /** 祖级列表（例如：0,1,2） */
  ancestors: string

  /** 文件夹名称 */
  folderName: string

  /** 完整路径（格式：/文件夹名1/文件夹名2/） */
  fullPath: string

  /** 层级（一般为数字字符串） */
  folderLevel: string

  /** 创建人ID */
  userId: string

  /** 排序权重 */
  sortOrder: number

  /** 创建时间 */
  createTime: string // ISO 格式时间字符串，如需可转为 Date 类型

  /** 更新时间 */
  updateTime: string // ISO 格式时间字符串

  /** 备注 */
  remark?: string
}

export interface SpaceFolderInfoQuery {
  /** 空间编号（不能为空） */
  spaceId: string

  /** 父文件夹编号（不能为空） */
  parentId: string

  /** 文件夹名称（不能为空，最大长度32） */
  folderName?: string

  /** 排序权重（不能为空，范围 0-10） */
  sortOrder?: number

  /** 备注（可选） */
  remark?: string
}
