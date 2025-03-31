// 获取用户详细信息
import { http as request } from '@/utils'
import type { MenuResponseInfo } from '@/types/router'

export function getMenuInfo(): Promise<MenuResponseInfo> {
  return request({
    url: '/user/userInfo/menu',
    method: 'get',
  })
}
