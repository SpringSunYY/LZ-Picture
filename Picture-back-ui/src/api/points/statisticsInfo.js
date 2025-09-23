import request from '@/utils/request'

// 查询统计信息列表
export function listStatisticsInfo(query) {
  return request({
    url: '/points/statisticsInfo/list',
    method: 'get',
    params: query
  })
}

// 查询统计信息详细
export function getStatisticsInfo(statisticsId) {
  return request({
    url: '/points/statisticsInfo/' + statisticsId,
    method: 'get'
  })
}

// 新增统计信息
export function addStatisticsInfo(data) {
  return request({
    url: '/points/statisticsInfo',
    method: 'post',
    data: data
  })
}

// 修改统计信息
export function updateStatisticsInfo(data) {
  return request({
    url: '/points/statisticsInfo',
    method: 'put',
    data: data
  })
}

// 删除统计信息
export function delStatisticsInfo(statisticsId) {
  return request({
    url: '/points/statisticsInfo/' + statisticsId,
    method: 'delete'
  })
}

//积分消耗类型统计
export function pointsUsageTypeStatistics(query){
  return request({
    url: '/points/statisticsInfo/points/usage/type',
    method: 'get',
    params: query
  })
}

//用户充值排行
export function pointsOrderRankStatistics(query){
  return request({
    url: '/points/statisticsInfo/points/order/rank',
    method: 'get',
    params: query
  })
}
