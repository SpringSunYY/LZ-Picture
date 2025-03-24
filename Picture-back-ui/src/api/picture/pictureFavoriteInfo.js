import request from '@/utils/request'

// 查询用户图片收藏列表
export function listPictureFavoriteInfo(query) {
  return request({
    url: '/picture/pictureFavoriteInfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户图片收藏详细
export function getPictureFavoriteInfo(favoriteId) {
  return request({
    url: '/picture/pictureFavoriteInfo/' + favoriteId,
    method: 'get'
  })
}

// 新增用户图片收藏
export function addPictureFavoriteInfo(data) {
  return request({
    url: '/picture/pictureFavoriteInfo',
    method: 'post',
    data: data
  })
}

// 修改用户图片收藏
export function updatePictureFavoriteInfo(data) {
  return request({
    url: '/picture/pictureFavoriteInfo',
    method: 'put',
    data: data
  })
}

// 删除用户图片收藏
export function delPictureFavoriteInfo(favoriteId) {
  return request({
    url: '/picture/pictureFavoriteInfo/' + favoriteId,
    method: 'delete'
  })
}
