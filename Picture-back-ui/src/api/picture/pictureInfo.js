import request from '@/utils/request'

// 查询图片信息列表
export function listPictureInfo(query) {
  return request({
    url: '/picture/pictureInfo/list',
    method: 'get',
    params: query
  })
}

// 查询图片信息详细
export function getPictureInfo(pictureId) {
  return request({
    url: '/picture/pictureInfo/' + pictureId,
    method: 'get'
  })
}

// 新增图片信息
export function addPictureInfo(data) {
  return request({
    url: '/picture/pictureInfo',
    method: 'post',
    data: data
  })
}

// 修改图片信息
export function updatePictureInfo(data) {
  return request({
    url: '/picture/pictureInfo',
    method: 'put',
    data: data
  })
}

// 删除图片信息
export function delPictureInfo(pictureId) {
  return request({
    url: '/picture/pictureInfo/' + pictureId,
    method: 'delete'
  })
}
