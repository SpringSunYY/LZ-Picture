import request from '@/utils/request'
import type { UserInfo } from '@/types/user'

export interface MyUserInfoResponse {
  user: UserInfo
  [key: string]: unknown
}

/**
 * 获取指定用户名的用户信息
 */
export function getMyUserInfoByUserName(username: string): Promise<MyUserInfoResponse> {
  return request<MyUserInfoResponse>({
    url: '/user/userInfo/my/' + username,
    method: 'get',
  })
}
