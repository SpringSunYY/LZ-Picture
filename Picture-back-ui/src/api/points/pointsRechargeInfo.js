import request from '@/utils/request'

// 查询积分充值记录列表
export function listPointsRechargeInfo(query) {
  return request({
    url: '/points/pointsRechargeInfo/list',
    method: 'get',
    params: query
  })
}

// 查询积分充值记录详细
export function getPointsRechargeInfo(rechargeId) {
  return request({
    url: '/points/pointsRechargeInfo/' + rechargeId,
    method: 'get'
  })
}

// 新增积分充值记录
export function addPointsRechargeInfo(data) {
  return request({
    url: '/points/pointsRechargeInfo',
    method: 'post',
    data: data
  })
}

// 修改积分充值记录
export function updatePointsRechargeInfo(data) {
  return request({
    url: '/points/pointsRechargeInfo',
    method: 'put',
    data: data
  })
}

// 删除积分充值记录
export function delPointsRechargeInfo(rechargeId) {
  return request({
    url: '/points/pointsRechargeInfo/' + rechargeId,
    method: 'delete'
  })
}
