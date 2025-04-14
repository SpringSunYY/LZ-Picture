import { http as request } from '@/utils'
import type { UserBehaviorInfo, UserBehaviorInfoAdd } from '@/types/picture/userBehaviorInfo'
import type { API } from '@/types/common'

//新增行为
export function addUserBehaviorInfo(
  data: UserBehaviorInfoAdd,
): Promise<API.ResponseInfo<UserBehaviorInfo>> {
  return request({
    url: '/picture/userBehaviorInfo',
    method: 'post',
    data: data,
  })
}
