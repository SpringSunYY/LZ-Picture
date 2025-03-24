import request from '@/utils/request'

// 查询图片点赞记录列表
export function listPictureLikeInfo(query) {
  return request({
    url: '/picture/pictureLikeInfo/list',
    method: 'get',
    params: query
  })
}

// 查询图片点赞记录详细
export function getPictureLikeInfo(likeId) {
  return request({
    url: '/picture/pictureLikeInfo/' + likeId,
    method: 'get'
  })
}

// 新增图片点赞记录
export function addPictureLikeInfo(data) {
  return request({
    url: '/picture/pictureLikeInfo',
    method: 'post',
    data: data
  })
}

// 修改图片点赞记录
export function updatePictureLikeInfo(data) {
  return request({
    url: '/picture/pictureLikeInfo',
    method: 'put',
    data: data
  })
}

// 删除图片点赞记录
export function delPictureLikeInfo(likeId) {
  return request({
    url: '/picture/pictureLikeInfo/' + likeId,
    method: 'delete'
  })
}
