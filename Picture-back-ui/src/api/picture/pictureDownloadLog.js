import request from '@/utils/request'

// 查询图片下载记录列表
export function listPictureDownloadLog(query) {
  return request({
    url: '/picture/pictureDownloadLog/list',
    method: 'get',
    params: query
  })
}

// 查询图片下载记录详细
export function getPictureDownloadLog(downloadId) {
  return request({
    url: '/picture/pictureDownloadLog/' + downloadId,
    method: 'get'
  })
}

// 新增图片下载记录
export function addPictureDownloadLog(data) {
  return request({
    url: '/picture/pictureDownloadLog',
    method: 'post',
    data: data
  })
}

// 修改图片下载记录
export function updatePictureDownloadLog(data) {
  return request({
    url: '/picture/pictureDownloadLog',
    method: 'put',
    data: data
  })
}

// 删除图片下载记录
export function delPictureDownloadLog(downloadId) {
  return request({
    url: '/picture/pictureDownloadLog/' + downloadId,
    method: 'delete'
  })
}
