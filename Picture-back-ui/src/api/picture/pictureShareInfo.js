import request from '@/utils/request'

// 查询图片转发记录列表
export function listPictureShareInfo(query) {
  return request({
    url: '/picture/pictureShareInfo/list',
    method: 'get',
    params: query
  })
}

// 查询图片转发记录详细
export function getPictureShareInfo(shareId) {
  return request({
    url: '/picture/pictureShareInfo/' + shareId,
    method: 'get'
  })
}

// 新增图片转发记录
export function addPictureShareInfo(data) {
  return request({
    url: '/picture/pictureShareInfo',
    method: 'post',
    data: data
  })
}

// 修改图片转发记录
export function updatePictureShareInfo(data) {
  return request({
    url: '/picture/pictureShareInfo',
    method: 'put',
    data: data
  })
}

// 删除图片转发记录
export function delPictureShareInfo(shareId) {
  return request({
    url: '/picture/pictureShareInfo/' + shareId,
    method: 'delete'
  })
}
