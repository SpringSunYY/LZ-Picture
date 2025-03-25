import request from '@/utils/request'

// 查询AI会话管理列表
export function listConversationSessionInfo(query) {
  return request({
    url: '/ai/conversationSessionInfo/list',
    method: 'get',
    params: query
  })
}

// 查询AI会话管理详细
export function getConversationSessionInfo(sessionId) {
  return request({
    url: '/ai/conversationSessionInfo/' + sessionId,
    method: 'get'
  })
}

// 新增AI会话管理
export function addConversationSessionInfo(data) {
  return request({
    url: '/ai/conversationSessionInfo',
    method: 'post',
    data: data
  })
}

// 修改AI会话管理
export function updateConversationSessionInfo(data) {
  return request({
    url: '/ai/conversationSessionInfo',
    method: 'put',
    data: data
  })
}

// 删除AI会话管理
export function delConversationSessionInfo(sessionId) {
  return request({
    url: '/ai/conversationSessionInfo/' + sessionId,
    method: 'delete'
  })
}
