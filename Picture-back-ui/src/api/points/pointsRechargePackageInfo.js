import request from '@/utils/request'

// 查询充值积分套餐列表
export function listPointsRechargePackageInfo(query) {
  return request({
    url: '/points/pointsRechargePackageInfo/list',
    method: 'get',
    params: query
  })
}

// 查询充值积分套餐详细
export function getPointsRechargePackageInfo(packageId) {
  return request({
    url: '/points/pointsRechargePackageInfo/' + packageId,
    method: 'get'
  })
}

// 新增充值积分套餐
export function addPointsRechargePackageInfo(data) {
  return request({
    url: '/points/pointsRechargePackageInfo',
    method: 'post',
    data: data
  })
}

// 修改充值积分套餐
export function updatePointsRechargePackageInfo(data) {
  return request({
    url: '/points/pointsRechargePackageInfo',
    method: 'put',
    data: data
  })
}

// 删除充值积分套餐
export function delPointsRechargePackageInfo(packageId) {
  return request({
    url: '/points/pointsRechargePackageInfo/' + packageId,
    method: 'delete'
  })
}
