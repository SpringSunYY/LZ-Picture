import request from '@/utils/request'

// 查询空间文件夹列表
export function listSpaceFolderInfo(query) {
  return request({
    url: '/picture/spaceFolderInfo/list',
    method: 'get',
    params: query
  })
}

// 查询空间文件夹详细
export function getSpaceFolderInfo(folderId) {
  return request({
    url: '/picture/spaceFolderInfo/' + folderId,
    method: 'get'
  })
}

// 新增空间文件夹
export function addSpaceFolderInfo(data) {
  return request({
    url: '/picture/spaceFolderInfo',
    method: 'post',
    data: data
  })
}

// 修改空间文件夹
export function updateSpaceFolderInfo(data) {
  return request({
    url: '/picture/spaceFolderInfo',
    method: 'put',
    data: data
  })
}

// 删除空间文件夹
export function delSpaceFolderInfo(folderId) {
  return request({
    url: '/picture/spaceFolderInfo/' + folderId,
    method: 'delete'
  })
}
