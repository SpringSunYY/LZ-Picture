import request from '@/utils/request'

// 查询提示词信息列表
export function listPromptInfo(query) {
  return request({
    url: '/ai/promptInfo/list',
    method: 'get',
    params: query
  })
}

// 查询提示词信息详细
export function getPromptInfo(infoId) {
  return request({
    url: '/ai/promptInfo/' + infoId,
    method: 'get'
  })
}

// 新增提示词信息
export function addPromptInfo(data) {
  return request({
    url: '/ai/promptInfo',
    method: 'post',
    data: data
  })
}

// 修改提示词信息
export function updatePromptInfo(data) {
  return request({
    url: '/ai/promptInfo',
    method: 'put',
    data: data
  })
}

// 删除提示词信息
export function delPromptInfo(infoId) {
  return request({
    url: '/ai/promptInfo/' + infoId,
    method: 'delete'
  })
}
