/* eslint-disable */

export type Space = {
  /** 空间编号 */
  spaceId: string;

  /** 空间名称 */
  spaceName: string;

  /** 空间封面URL */
  spaceAvatar?: string;

  /** 存储类型（0官方 阿里云） */
  ossType: string;

  /** 最大容量（字节） */
  maxSize: number;

  /** 最大文件数 */
  maxCount: number;

  /** 已用容量（字节） */
  totalSize: number;

  /** 文件总数 */
  totalCount: number;

  /** 所属用户 */
  userId: string;

  /** 空间描述 */
  spaceDesc?: string;

  /** 空间状态 */
  spaceStatus: string;

  /** 空间类型（0个人 1团队 2官方） */
  spaceType: string;

  /** 成员上限 */
  memberLimit: number;

  /** 当前成员数 */
  currentMembers: number;

  /** 创建时间 */
  createTime: string;

  /** 最后上传时间 */
  lastUpdateTime: string;

  /** 最后更新时间 */
  updateTime: string;
};

export type SpaceAdd = {
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
  spaceId?: string;

  /** 空间名称 */
  spaceName?: string;

  /** 存储类型（0官方 阿里云） */
  ossType?: string;

  /** 所属用户 */
  userId?: string;

  /** 空间状态 */
  spaceStatus?: string;

  /** 空间类型（0个人 1团队 2官方） */
  spaceType?: string;

  /** 创建时间 */
  createTime?: string;

  /** 最后上传时间 */
  lastUpdateTime?: string;

  /** 最后更新时间 */
  updateTime?: string;


  /** 请求参数 */
  params?: Record<string, any>;
};
