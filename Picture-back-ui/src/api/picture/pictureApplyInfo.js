import request from '@/utils/request'

// 查询图片申请信息列表
export function listPictureApplyInfo(query) {
  return request({
    url: '/picture/pictureApplyInfo/list',
    method: 'get',
    params: query
  })
}

// 查询图片申请信息详细
export function getPictureApplyInfo(applyId) {
  return request({
    url: '/picture/pictureApplyInfo/' + applyId,
    method: 'get'
  })
}

// 新增图片申请信息
export function addPictureApplyInfo(data) {
  return request({
    url: '/picture/pictureApplyInfo',
    method: 'post',
    data: data
  })
}

// 修改图片申请信息
export function updatePictureApplyInfo(data) {
  return request({
    url: '/picture/pictureApplyInfo',
    method: 'put',
    data: data
  })
}

// 删除图片申请信息
export function delPictureApplyInfo(applyId) {
  return request({
    url: '/picture/pictureApplyInfo/' + applyId,
    method: 'delete'
  })
}
