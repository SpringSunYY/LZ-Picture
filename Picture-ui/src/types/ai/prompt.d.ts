/**
 * 提示词信息
 * ai_prompt_info
 */
export interface PromptInfoVo {
  /**
   * 编号
   */
  infoId: string

  /**
   * 名称
   */
  name: string

  /**
   * 提示内容
   */
  content: string
}

/**
 * 提示词信息
 * ai_prompt_info
 */
export interface PromptInfoRequest {
  /**
   * 页码
   */
  pageNum: number
  /**
   * 页数
   */
  pageSize: number

}
