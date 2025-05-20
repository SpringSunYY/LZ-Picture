// 获取用户详细信息
import { http as request } from '@/utils'
import type { MenuInfo } from '@/types/router'
import type { API } from '@/types/common'

export function getMenuInfo(): Promise<API.ResponseInfo<MenuInfo>> {
  return request({
    url: '/user/userInfo/menu',
    method: 'get'
  })
}
