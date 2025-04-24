import request from '@/utils/request'

// 查询文件日志列表
export function listFileLogInfo(query) {
  return request({
    url: '/config/fileLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询文件日志详细
export function getFileLogInfo(logId) {
  return request({
    url: '/config/fileLogInfo/' + logId,
    method: 'get'
  })
}

// 新增文件日志
export function addFileLogInfo(data) {
  return request({
    url: '/config/fileLogInfo',
    method: 'post',
    data: data
  })
}

// 修改文件日志
export function updateFileLogInfo(data) {
  return request({
    url: '/config/fileLogInfo',
    method: 'put',
    data: data
  })
}

// 删除文件日志
export function delFileLogInfo(logId) {
  return request({
    url: '/config/fileLogInfo/' + logId,
    method: 'delete'
  })
}
