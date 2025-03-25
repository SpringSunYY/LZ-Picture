import request from '@/utils/request'

// 查询积分使用记录列表
export function listPointsUsageLog(query) {
  return request({
    url: '/points/pointsUsageLog/list',
    method: 'get',
    params: query
  })
}

// 查询积分使用记录详细
export function getPointsUsageLog(logId) {
  return request({
    url: '/points/pointsUsageLog/' + logId,
    method: 'get'
  })
}

// 新增积分使用记录
export function addPointsUsageLog(data) {
  return request({
    url: '/points/pointsUsageLog',
    method: 'post',
    data: data
  })
}

// 修改积分使用记录
export function updatePointsUsageLog(data) {
  return request({
    url: '/points/pointsUsageLog',
    method: 'put',
    data: data
  })
}

// 删除积分使用记录
export function delPointsUsageLog(logId) {
  return request({
    url: '/points/pointsUsageLog/' + logId,
    method: 'delete'
  })
}
