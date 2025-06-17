import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { UserReportInfoAdd } from '@/types/picture/userReportInfo'

//新增举报信息
export function addUserReportInfo(data: UserReportInfoAdd): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/userReportInfo',
    method: 'post',
    data: data,
  })
}
