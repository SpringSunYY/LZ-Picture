import request from '@/utils/request'

// 查询积分使用记录列表
export function listPointsUsageLogInfo(query) {
  return request({
    url: '/points/pointsUsageLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询积分使用记录详细
export function getPointsUsageLogInfo(logId) {
  return request({
    url: '/points/pointsUsageLogInfo/' + logId,
    method: 'get'
  })
}

// 新增积分使用记录
export function addPointsUsageLogInfo(data) {
  return request({
    url: '/points/pointsUsageLogInfo',
    method: 'post',
    data: data
  })
}

// 修改积分使用记录
export function updatePointsUsageLogInfo(data) {
  return request({
    url: '/points/pointsUsageLogInfo',
    method: 'put',
    data: data
  })
}

// 删除积分使用记录
export function delPointsUsageLogInfo(logId) {
  return request({
    url: '/points/pointsUsageLogInfo/' + logId,
    method: 'delete'
  })
}
