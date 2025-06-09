import request from '@/utils/request'

// 查询用户图片推荐模型列表
export function listPictureRecommendInfo(query) {
  return request({
    url: '/picture/pictureRecommendInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户图片推荐模型详细
export function getPictureRecommendInfo(recommendId) {
  return request({
    url: '/picture/pictureRecommendInfo/' + recommendId,
    method: 'get'
  })
}

// 新增用户图片推荐模型
export function addPictureRecommendInfo(data) {
  return request({
    url: '/picture/pictureRecommendInfo',
    method: 'post',
    data: data
  })
}

// 修改用户图片推荐模型
export function updatePictureRecommendInfo(data) {
  return request({
    url: '/picture/pictureRecommendInfo',
    method: 'put',
    data: data
  })
}

// 删除用户图片推荐模型
export function delPictureRecommendInfo(recommendId) {
  return request({
    url: '/picture/pictureRecommendInfo/' + recommendId,
    method: 'delete'
  })
}
