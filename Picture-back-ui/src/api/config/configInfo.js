import request from '@/utils/request'

// 查询配置信息列表
export function listConfigInfo(query) {
  return request({
    url: '/config/configInfo/list',
    method: 'get',
    params: query
  })
}

// 查询配置信息详细
export function getConfigInfo(configId) {
  return request({
    url: '/config/configInfo/' + configId,
    method: 'get'
  })
}

// 新增配置信息
export function addConfigInfo(data) {
  return request({
    url: '/config/configInfo',
    method: 'post',
    data: data
  })
}

// 修改配置信息
export function updateConfigInfo(data) {
  return request({
    url: '/config/configInfo',
    method: 'put',
    data: data
  })
}

// 删除配置信息
export function delConfigInfo(configId) {
  return request({
    url: '/config/configInfo/' + configId,
    method: 'delete'
  })
}
