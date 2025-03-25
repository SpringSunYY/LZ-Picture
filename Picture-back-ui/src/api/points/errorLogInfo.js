import request from '@/utils/request'

// 查询异常捕获列表
export function listErrorLogInfo(query) {
  return request({
    url: '/points/errorLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询异常捕获详细
export function getErrorLogInfo(errorId) {
  return request({
    url: '/points/errorLogInfo/' + errorId,
    method: 'get'
  })
}

// 新增异常捕获
export function addErrorLogInfo(data) {
  return request({
    url: '/points/errorLogInfo',
    method: 'post',
    data: data
  })
}

// 修改异常捕获
export function updateErrorLogInfo(data) {
  return request({
    url: '/points/errorLogInfo',
    method: 'put',
    data: data
  })
}

// 删除异常捕获
export function delErrorLogInfo(errorId) {
  return request({
    url: '/points/errorLogInfo/' + errorId,
    method: 'delete'
  })
}
