import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { UserInfoVo } from '@/types/picture/userinfo'

/**
 * 根据用户名获取用户信息，返回用户作品等信息
 * @param username
 */
export function getPictureUserInfoByUsername(
  username: string,
): Promise<API.ResponseInfo<UserInfoVo>> {
  return request({
    url: '/picture/userinfo/' + username,
    method: 'get',
  })
}
