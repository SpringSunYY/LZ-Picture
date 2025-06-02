import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { MyUserInfo, UserInfoUpdate, UserInfoUpdateAvatar, UserPasswordUploadRequest } from '@/types/user/user'
import { encryptBack } from '@/utils/jsencrypt.ts'

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
// 用户更新基本信息头像
export function updateUserInfoAvatar(data: UserInfoUpdateAvatar): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/user/userInfo/update/avatar',
    method: 'put',
    data: data
  })
}

// 用户更新密码
export function updateUserInfoPassword(data: UserPasswordUploadRequest): Promise<API.ResponseInfo<number>> {

  return request({
    url: '/user/userInfo/password',
    method: 'put',
    data: {
      password: encryptBack(data.password),
      confirmPassword: encryptBack(data.confirmPassword),
      oldPassword: encryptBack(data.oldPassword),
      userId: encryptBack(data.userId),
    },
  })
}
