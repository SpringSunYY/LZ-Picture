/* eslint-disable */

export type Space = {
  /** 空间编号 */
  spaceId: string

  /** 空间名称 */
  spaceName: string

  /** 空间封面URL */
  spaceAvatar?: string

  /** 存储类型（0官方 阿里云） */
  ossType?: string

  /** 最大容量（字节） */
  maxSize?: number

  /** 最大文件数 */
  maxCount?: number

  /** 已用容量（字节） */
  totalSize?: number

  /** 文件总数 */
  totalCount?: number

  /** 所属用户 */
  userId?: string

  /** 空间描述 */
  spaceDesc?: string

  /** 空间状态 */
  spaceStatus?: string

  /** 空间类型（0个人 1团队 2官方） */
  spaceType?: string

  /** 成员上限 */
  memberLimit?: number

  /** 当前成员数 */
  currentMembers?: number

  /** 创建时间 */
  createTime?: string

  /** 最后上传时间 */
  lastUpdateTime?: string

  /** 最后更新时间 */
  updateTime?: string
}

export type SpaceInfo = {
  /** 空间编号 */
  spaceId?: string
  /** 空间名称 */
  spaceName: string

  /** 空间封面URL */
  spaceAvatar?: string

  /** 所属用户 */
  userId?: string

  /** 空间描述 */
  spaceDesc?: string

  /** 空间状态 */
  spaceStatus: string

  /** 空间类型（0个人 1团队 2官方） */
  spaceType: string
}

export type SpaceQuery = {
  /** 空间编号 */
  spaceId?: string

  /** 空间名称 */
  spaceName?: string

  /** 存储类型（0官方 阿里云） */
  ossType?: string

  /** 所属用户 */
  userId?: string

  /** 空间状态 */
  spaceStatus?: string

  /** 空间类型（0个人 1团队 2官方） */
  spaceType?: string

  /** 创建时间 */
  createTime?: string

  /** 最后上传时间 */
  lastUpdateTime?: string

  /** 最后更新时间 */
  updateTime?: string

  /** 请求参数 */
  params?: Record<string, any>
}

export enum PSpaceType {
  SPACE_TYPE_0 = '0',
  SPACE_TYPE_1 = '1',
  SPACE_TYPE_2 = '2',
}

export const PSpaceTypeLabels = {
  [PSpaceType.SPACE_TYPE_0]: '官方',
  [PSpaceType.SPACE_TYPE_1]: '团队',
  [PSpaceType.SPACE_TYPE_2]: '个人',
}

/**
 * 根据值获取对应的标签
 * @param value 枚举的值
 * @returns 对应的标签
 */
export function getPSpaceTypeLabel(value: string): string {
  return PSpaceTypeLabels[value as PSpaceType] || '未知类型'
}

export enum PSpaceStatus {
  SPACE_STATUS_0 = '0',
  SPACE_STATUS_1 = '1',
}

export const PSpaceStatusLabels = {
  [PSpaceStatus.SPACE_STATUS_0]: '公共',
  [PSpaceStatus.SPACE_STATUS_1]: '私有',
}

/**
 * 根据值获取对应的标签
 * @param value 枚举的值
 * @returns 对应的标签
 */
export function getPSpaceStatusLabel(value: string): string {
  return PSpaceStatusLabels[value as PSpaceStatus] || '未知状态'
}

export enum PSpaceRole {
  SPACE_ROLE_0 = '0',
  SPACE_ROLE_1 = '1',
  SPACE_ROLE_2 = '2',
  SPACE_ROLE_3 = '3',
}

export const PSpaceRoleLabels = {
  [PSpaceRole.SPACE_ROLE_0]: '创建者',
  [PSpaceRole.SPACE_ROLE_1]: '管理员',
  [PSpaceRole.SPACE_ROLE_2]: '编辑者',
  [PSpaceRole.SPACE_ROLE_3]: '预览者',
}

/**
 * 根据值获取对应的标签
 * @param value 枚举的值
 * @returns 对应的标签
 */
export function getPSpaceRoleLabel(value: string): string {
  return PSpaceRoleLabels[value as PSpaceRole] || '未知角色'
}

export enum PSpaceOssType {
  SPACE_OSS_TYPE_0 = '0',
  SPACE_OSS_TYPE_1 = '1',
}

export const PSpaceOssTypeLabels = {
  [PSpaceOssType.SPACE_OSS_TYPE_0]: '官方',
  [PSpaceOssType.SPACE_OSS_TYPE_1]: '阿里云',
}

/**
 * 根据值获取对应的标签
 * @param value 枚举的值
 * @returns 对应的标签
 */
export function getPSpaceOssTypeLabel(value: string): string {
  return PSpaceOssTypeLabels[value as PSpaceOssType] || '未知类型'
}
