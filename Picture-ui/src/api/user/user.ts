import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { MyUserInfo, UserInfoUpdate } from '@/types/user/user'

// 获取用户详细信息
export function getMyUserInfoByUserName(username: string): Promise<API.ResponseInfo<MyUserInfo>> {
  return request({
    url: '/user/userInfo/my/' + username,
    method: 'get',
  })
}

// 用户更新基本信息
export function updateUserInfo(data: UserInfoUpdate): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/user/userInfo/update',
    method: 'put',
    data: data,
  })
}
