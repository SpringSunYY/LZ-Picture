import { http as request } from '@/utils'
import type {
  UserBehaviorInfo,
  UserBehaviorInfoAdd,
  UserBehaviorInfoQuery,
  UserBehaviorInfoVo,
} from '@/types/picture/userBehaviorInfo'
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

//查看行为记录
export function listUserBehaviorInfo(
  params: UserBehaviorInfoQuery,
): Promise<API.ResponseInfo<UserBehaviorInfoVo>> {
  return request({
    url: '/picture/userBehaviorInfo/list',
    method: 'get',
    params: params,
  })
}
