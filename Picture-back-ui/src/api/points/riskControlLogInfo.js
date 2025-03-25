import request from '@/utils/request'

// 查询风控日志列表
export function listRiskControlLogInfo(query) {
  return request({
    url: '/points/riskControlLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询风控日志详细
export function getRiskControlLogInfo(logId) {
  return request({
    url: '/points/riskControlLogInfo/' + logId,
    method: 'get'
  })
}

// 新增风控日志
export function addRiskControlLogInfo(data) {
  return request({
    url: '/points/riskControlLogInfo',
    method: 'post',
    data: data
  })
}

// 修改风控日志
export function updateRiskControlLogInfo(data) {
  return request({
    url: '/points/riskControlLogInfo',
    method: 'put',
    data: data
  })
}

// 删除风控日志
export function delRiskControlLogInfo(logId) {
  return request({
    url: '/points/riskControlLogInfo/' + logId,
    method: 'delete'
  })
}
