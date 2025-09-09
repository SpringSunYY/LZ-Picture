import request from '@/utils/request'

// 查询统计信息列表
export function listUStatisticsInfo(query) {
  return request({
    url: '/user/uStatisticsInfo/list',
    method: 'get',
    params: query
  })
}

// 查询统计信息详细
export function getUStatisticsInfo(statisticsId) {
  return request({
    url: '/user/uStatisticsInfo/' + statisticsId,
    method: 'get'
  })
}

// 新增统计信息
export function addUStatisticsInfo(data) {
  return request({
    url: '/user/uStatisticsInfo',
    method: 'post',
    data: data
  })
}

// 修改统计信息
export function updateUStatisticsInfo(data) {
  return request({
    url: '/user/uStatisticsInfo',
    method: 'put',
    data: data
  })
}

// 删除统计信息
export function delUStatisticsInfo(statisticsId) {
  return request({
    url: '/user/uStatisticsInfo/' + statisticsId,
    method: 'delete'
  })
}
