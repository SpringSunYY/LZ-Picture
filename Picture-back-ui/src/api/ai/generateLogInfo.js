import request from '@/utils/request'

// 查询用户生成记录列表
export function listGenerateLogInfo(query) {
  return request({
    url: '/ai/generateLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户生成记录详细
export function getGenerateLogInfo(logId) {
  return request({
    url: '/ai/generateLogInfo/' + logId,
    method: 'get'
  })
}

// 新增用户生成记录
export function addGenerateLogInfo(data) {
  return request({
    url: '/ai/generateLogInfo',
    method: 'post',
    data: data
  })
}

// 修改用户生成记录
export function updateGenerateLogInfo(data) {
  return request({
    url: '/ai/generateLogInfo',
    method: 'put',
    data: data
  })
}

// 删除用户生成记录
export function delGenerateLogInfo(logId) {
  return request({
    url: '/ai/generateLogInfo/' + logId,
    method: 'delete'
  })
}
