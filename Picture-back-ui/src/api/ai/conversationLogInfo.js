import request from '@/utils/request'

// 查询AI对话明细记录列表
export function listConversationLogInfo(query) {
  return request({
    url: '/ai/conversationLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询AI对话明细记录详细
export function getConversationLogInfo(conversationId) {
  return request({
    url: '/ai/conversationLogInfo/' + conversationId,
    method: 'get'
  })
}

// 新增AI对话明细记录
export function addConversationLogInfo(data) {
  return request({
    url: '/ai/conversationLogInfo',
    method: 'post',
    data: data
  })
}

// 修改AI对话明细记录
export function updateConversationLogInfo(data) {
  return request({
    url: '/ai/conversationLogInfo',
    method: 'put',
    data: data
  })
}

// 删除AI对话明细记录
export function delConversationLogInfo(conversationId) {
  return request({
    url: '/ai/conversationLogInfo/' + conversationId,
    method: 'delete'
  })
}
