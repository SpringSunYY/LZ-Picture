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
export function getPictureTagRelInfo(relId) {
  return request({
    url: '/picture/pictureTagRelInfo/' + relId,
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
export function delPictureTagRelInfo(relId) {
  return request({
    url: '/picture/pictureTagRelInfo/' + relId,
    method: 'delete'
  })
}
