import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type {
  UserPointsRechargeInfoQuery,
  UserPointsRechargeInfoVo,
} from '@/types/points/pointsRecharge'

//充值记录
export function listRechargeInfo(
  params: UserPointsRechargeInfoQuery,
): Promise<API.ResponseInfo<UserPointsRechargeInfoVo>> {
  return request({
    url: '/points/pointsRechargeInfo/list',
    method: 'get',
    params: params,
  })
}
