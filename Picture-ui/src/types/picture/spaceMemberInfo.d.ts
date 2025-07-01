/**
 * 空间成员更新参数
 * 对应后端：SpaceMemberInfoUpdate
 */
export interface SpaceMemberInfoUpdate {
  /** 成员编号 */
  memberId: string;

  /** 角色（0创建者 1管理员 2编辑者 3浏览者） */
  roleType: string;
}


/**
 * 空间成员信息 Vo 对象（p_space_member_info）
 * @date 2025-03-24
 */
export interface SpaceMemberInfoVo {
  /** 成员编号 */
  memberId?: string

  /** 空间编号 */
  spaceId?: string

  /** 空间名称 */
  spaceName?: string

  /** 用户编号 */
  userName?: string

  /** 用户头像 */
  avatarUrl?: string

  /** 角色类型（0创建者 1管理员 2编辑者 3浏览者），数据字典：p_space_role */
  roleType?: string

  /** 最后操作时间（格式：yyyy-MM-dd HH:mm:ss） */
  lastActiveTime?: string

  /** 加入时间（格式：yyyy-MM-dd HH:mm:ss） */
  createTime?: string

  /** 邀请人编号 */
  inviterUserName?: string

  /** 加入方式（0邀请），数据字典：p_space_join_type */
  joinType?: string
}

/**
 * 空间成员查询参数
 * 对应后端：UserSpaceMemberInfoQuery
 */
export interface SpaceMemberInfoQuery {
  /** 空间编号 */
  spaceId: string

  /** 角色（0创建者 1管理员 2编辑者 3浏览者） */
  roleType?: string | null

  /** 用户编号 */
  userId?: string

  /** 最后操作时间（yyyy-MM-dd） */
  lastActiveTime?: string

  /** 加入时间（yyyy-MM-dd） */
  createTime?: string

  /** 加入方式（0邀请） */
  joinType?: string | null

  /** 请求参数 Map */
  params?: Record<string, any>

  /** 当前页码（分页） */
  pageNum?: number

  /** 每页记录数（分页） */
  pageSize?: number

  /** 排序列 */
  orderByColumn?: string

  /** 排序方向（asc 或 desc） */
  isAsc?: string
}
