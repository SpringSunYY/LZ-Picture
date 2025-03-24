import request from '@/utils/request'

// 查询图片标签关联列表
export function listPictureTagRelInfo(query) {
  return request({
    url: '/picture/pictureTagRelInfo/list',
    method: 'get',
    params: query
  })
}

// 查询图片标签关联详细
export function getPictureTagRelInfo(pictureId) {
  return request({
    url: '/picture/pictureTagRelInfo/' + pictureId,
    method: 'get'
  })
}

// 新增图片标签关联
export function addPictureTagRelInfo(data) {
  return request({
    url: '/picture/pictureTagRelInfo',
    method: 'post',
    data: data
  })
}

// 修改图片标签关联
export function updatePictureTagRelInfo(data) {
  return request({
    url: '/picture/pictureTagRelInfo',
    method: 'put',
    data: data
  })
}

// 删除图片标签关联
export function delPictureTagRelInfo(pictureId) {
  return request({
    url: '/picture/pictureTagRelInfo/' + pictureId,
    method: 'delete'
  })
}
