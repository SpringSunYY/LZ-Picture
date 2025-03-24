import request from '@/utils/request'

// 查询图片分类信息列表
export function listPictureCategoryInfo(query) {
  return request({
    url: '/picture/pictureCategoryInfo/list',
    method: 'get',
    params: query
  })
}

// 查询图片分类信息详细
export function getPictureCategoryInfo(categoryId) {
  return request({
    url: '/picture/pictureCategoryInfo/' + categoryId,
    method: 'get'
  })
}

// 新增图片分类信息
export function addPictureCategoryInfo(data) {
  return request({
    url: '/picture/pictureCategoryInfo',
    method: 'post',
    data: data
  })
}

// 修改图片分类信息
export function updatePictureCategoryInfo(data) {
  return request({
    url: '/picture/pictureCategoryInfo',
    method: 'put',
    data: data
  })
}

// 删除图片分类信息
export function delPictureCategoryInfo(categoryId) {
  return request({
    url: '/picture/pictureCategoryInfo/' + categoryId,
    method: 'delete'
  })
}
