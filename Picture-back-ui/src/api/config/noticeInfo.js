import request from '@/utils/request'

// 查询用户公告列表
export function listNoticeInfo(query) {
  return request({
    url: '/config/noticeInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户公告详细
export function getNoticeInfo(noticeId) {
  return request({
    url: '/config/noticeInfo/' + noticeId,
    method: 'get'
  })
}

// 新增用户公告
export function addNoticeInfo(data) {
  return request({
    url: '/config/noticeInfo',
    method: 'post',
    data: data
  })
}

// 修改用户公告
export function updateNoticeInfo(data) {
  return request({
    url: '/config/noticeInfo',
    method: 'put',
    data: data
  })
}

// 删除用户公告
export function delNoticeInfo(noticeId) {
  return request({
    url: '/config/noticeInfo/' + noticeId,
    method: 'delete'
  })
}
