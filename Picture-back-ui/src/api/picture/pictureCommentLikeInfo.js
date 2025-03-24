import request from '@/utils/request'

// 查询评论点赞记录列表
export function listPictureCommentLikeInfo(query) {
  return request({
    url: '/picture/pictureCommentLikeInfo/list',
    method: 'get',
    params: query
  })
}

// 查询评论点赞记录详细
export function getPictureCommentLikeInfo(likeId) {
  return request({
    url: '/picture/pictureCommentLikeInfo/' + likeId,
    method: 'get'
  })
}

// 新增评论点赞记录
export function addPictureCommentLikeInfo(data) {
  return request({
    url: '/picture/pictureCommentLikeInfo',
    method: 'post',
    data: data
  })
}

// 修改评论点赞记录
export function updatePictureCommentLikeInfo(data) {
  return request({
    url: '/picture/pictureCommentLikeInfo',
    method: 'put',
    data: data
  })
}

// 删除评论点赞记录
export function delPictureCommentLikeInfo(likeId) {
  return request({
    url: '/picture/pictureCommentLikeInfo/' + likeId,
    method: 'delete'
  })
}
