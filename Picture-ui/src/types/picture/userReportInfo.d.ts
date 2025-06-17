/**
 * 用户举报信息添加请求参数
 * 对应后端的 UserReportInfoAdd DTO
 */
export interface UserReportInfoAdd {
  /** 目标编号 */
  targetId: string
  /** 举报类型 */
  reportType: string

  /** 目标类型（0图片 1用户 2空间） */
  targetType: string

  /** 举报原因 */
  reason: string
}
