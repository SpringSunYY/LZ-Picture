import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { MyUserInfo } from '@/types/user/user'

// 获取用户详细信息
export function getMyUserInfoByUserName(username: string): Promise<API.ResponseInfo<MyUserInfo>> {
  return request({
    url: '/user/userInfo/my/' + username,
    method: 'get',
  })
}
