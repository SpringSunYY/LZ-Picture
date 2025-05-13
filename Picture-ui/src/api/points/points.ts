import { http as request } from '@/utils'
import type {
  PointsRechargePackageInfoQuery,
  PointsRechargePackageInfoVo,
} from '@/types/points/points.ts'

//获取积分套餐列表
export function getPointsRechargePackageInfoList(
  params: PointsRechargePackageInfoQuery,
): Promise<API.ResponseInfo<PointsRechargePackageInfoVo>> {
  return request({
    url: '/points/pointsRechargePackageInfo/list',
    method: 'get',
    params: params,
  })
}

//获取积分套餐
export function getPointsRechargePackageInfo(
  packageId: string,
): Promise<API.ResponseInfo<PointsRechargePackageInfoVo>> {
  return request({
    url: '/points/pointsRechargePackageInfo/' + packageId,
    method: 'get',
  })
}
