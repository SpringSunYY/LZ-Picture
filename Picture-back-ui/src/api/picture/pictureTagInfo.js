import request from '@/utils/request'

// 查询图片标签信息列表
export function listPictureTagInfo(query) {
  return request({
    url: '/picture/pictureTagInfo/list',
    method: 'get',
    params: query
  })
}

// 查询图片标签信息详细
export function getPictureTagInfo(tagId) {
  return request({
    url: '/picture/pictureTagInfo/' + tagId,
    method: 'get'
  })
}

// 新增图片标签信息
export function addPictureTagInfo(data) {
  return request({
    url: '/picture/pictureTagInfo',
    method: 'post',
    data: data
  })
}

// 修改图片标签信息
export function updatePictureTagInfo(data) {
  return request({
    url: '/picture/pictureTagInfo',
    method: 'put',
    data: data
  })
}

// 删除图片标签信息
export function delPictureTagInfo(tagId) {
  return request({
    url: '/picture/pictureTagInfo/' + tagId,
    method: 'delete'
  })
}
