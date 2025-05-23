import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type {
  UserPointsUsageLogInfoQuery,
  UserPointsUsageLogInfoVo,
} from '@/types/points/pointsUsageLogInfo'

//积分使用记录
export function listPointsUsageLogInfo(
  params: UserPointsUsageLogInfoQuery,
): Promise<API.ResponseInfo<UserPointsUsageLogInfoVo>> {
  return request({
    url: '/points/pointsUsageLogInfo/list',
    method: 'get',
    params: params,
  })
}
