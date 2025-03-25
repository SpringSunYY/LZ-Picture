import request from '@/utils/request'

// 查询官方AI操作日志列表
export function listOfficialUsageLogInfo(query) {
  return request({
    url: '/ai/officialUsageLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询官方AI操作日志详细
export function getOfficialUsageLogInfo(logId) {
  return request({
    url: '/ai/officialUsageLogInfo/' + logId,
    method: 'get'
  })
}

// 新增官方AI操作日志
export function addOfficialUsageLogInfo(data) {
  return request({
    url: '/ai/officialUsageLogInfo',
    method: 'post',
    data: data
  })
}

// 修改官方AI操作日志
export function updateOfficialUsageLogInfo(data) {
  return request({
    url: '/ai/officialUsageLogInfo',
    method: 'put',
    data: data
  })
}

// 删除官方AI操作日志
export function delOfficialUsageLogInfo(logId) {
  return request({
    url: '/ai/officialUsageLogInfo/' + logId,
    method: 'delete'
  })
}
