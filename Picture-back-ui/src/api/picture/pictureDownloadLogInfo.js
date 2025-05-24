import request from '@/utils/request'

// 查询图片下载记录列表
export function listPictureDownloadLogInfo(query) {
  return request({
    url: '/picture/pictureDownloadLogInfo/list',
    method: 'get',
    params: query
  })
}

// 查询图片下载记录详细
export function getPictureDownloadLogInfo(downloadId) {
  return request({
    url: '/picture/pictureDownloadLogInfo/' + downloadId,
    method: 'get'
  })
}

// 新增图片下载记录
export function addPictureDownloadLogInfo(data) {
  return request({
    url: '/picture/pictureDownloadLogInfo',
    method: 'post',
    data: data
  })
}

// 修改图片下载记录
export function updatePictureDownloadLogInfo(data) {
  return request({
    url: '/picture/pictureDownloadLogInfo',
    method: 'put',
    data: data
  })
}

// 删除图片下载记录
export function delPictureDownloadLogInfo(downloadId) {
  return request({
    url: '/picture/pictureDownloadLogInfo/' + downloadId,
    method: 'delete'
  })
}
