import request from '@/utils/request'
import type {UserInfo, UserPasswordUploadRequest} from '@/types/user'
import {encryptBack} from "@/utils/jsencrypt";
import {API} from "@/types/common";

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
