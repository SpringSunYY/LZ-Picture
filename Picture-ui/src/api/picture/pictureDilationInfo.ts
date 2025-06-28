import { http as request } from '@/utils'
import type { API } from '@/types/common'
import type {
  SpaceDilatationInfoAdd,
  SpaceDilatationInfoCalculationVo
} from '@/types/picture/pictureDilationInfo'

//添加扩容信息
export function addSpaceDilationInfo(data: SpaceDilatationInfoAdd): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/spaceDilatationInfo',
    method: 'post',
    data: data,
  })
}

//获取扩容计算信息
export function getSpaceDilationInfo(params: SpaceDilatationInfoAdd): Promise<API.ResponseInfo<SpaceDilatationInfoCalculationVo>> {
  return request({
    url: '/picture/spaceDilatationInfo/calculate',
    method: 'get',
    params: params,
  })
}
