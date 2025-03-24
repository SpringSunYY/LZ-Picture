import request from '@/utils/request'

// 查询图片评论列表
export function listPictureCommentInfo(query) {
  return request({
    url: '/picture/pictureCommentInfo/list',
    method: 'get',
    params: query
  })
}

// 查询图片评论详细
export function getPictureCommentInfo(commentId) {
  return request({
    url: '/picture/pictureCommentInfo/' + commentId,
    method: 'get'
  })
}

// 新增图片评论
export function addPictureCommentInfo(data) {
  return request({
    url: '/picture/pictureCommentInfo',
    method: 'post',
    data: data
  })
}

// 修改图片评论
export function updatePictureCommentInfo(data) {
  return request({
    url: '/picture/pictureCommentInfo',
    method: 'put',
    data: data
  })
}

// 删除图片评论
export function delPictureCommentInfo(commentId) {
  return request({
    url: '/picture/pictureCommentInfo/' + commentId,
    method: 'delete'
  })
}
