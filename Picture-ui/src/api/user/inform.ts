import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type { InformInfoQuery, InformInfoVo } from '@/types/user/informInfo'

//查看用户通知信息
export function listInformInfo(params: InformInfoQuery): Promise<API.ResponseInfo<InformInfoVo>> {
  return request({
    url: '/user/informInfo/list',
    method: 'get',
    params: params,
  })
}

//查看用户通知信息
export function getInformInfo(recordId: string): Promise<API.ResponseInfo<InformInfoVo>> {
  return request({
    url: '/user/informInfo/' + recordId,
    method: 'get',
  })
}
