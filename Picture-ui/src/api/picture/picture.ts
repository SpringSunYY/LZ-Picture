import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type {
  MyPictureInfoVo,
  PictureDetailInfoVo,
  PictureInfo,
  PictureInfoQuery,
  PictureInfoRecommendRequest,
  PictureInfoSearchRecommendVo,
  PictureInfoSearchSuggestionVo,
  PictureInfoUpdate,
  PictureInfoVo,
  PictureRecommendRequest,
  UserRecommendPictureInfoVo,
  PictureUrlUpload,
  PictureInfoTableVo,
} from '@/types/picture/picture'

/**
 * 新增图片信息
 * @param data
 */
export function addPictureInfo(data: PictureInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/pictureInfo',
    method: 'post',
    data: data,
  })
}

/**
 * 新增图片信息 图片搜索url
 * @param data
 */
export function addPictureInfoUrl(data: PictureUrlUpload): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/pictureInfo/upload/url',
    method: 'post',
    data: data,
  })
}

/**
 * 修改图片信息
 * @param data
 */
export function updatePictureInfo(
  data: PictureInfoUpdate,
): Promise<API.ResponseInfo<PictureDetailInfoVo>> {
  return request({
    url: '/picture/pictureInfo/update',
    method: 'put',
    data: data,
  })
}

/**
 * 查询图片信息列表
 * @param params
 */
export function listPictureInfo(
  params: PictureRecommendRequest,
): Promise<API.ResponseInfo<UserRecommendPictureInfoVo>> {
  return request({
    url: '/picture/pictureInfo/list',
    params: params,
  })
}

/**
 * 查询我的图片信息列表
 * @param params
 */
export function listMyPictureInfo(
  params: PictureInfoQuery,
): Promise<API.ResponseInfo<MyPictureInfoVo>> {
  return request({
    url: '/picture/pictureInfo/list/my',
    params: params,
  })
}

/**
 * 删除图片信息
 * @param data
 */
export function deletePictureInfo(data: PictureInfo): Promise<API.ResponseInfo<number>> {
  return request({
    url: '/picture/pictureInfo',
    method: 'delete',
    data: data,
  })
}

/**
 * 获取图片信息
 * @param pictureId
 */
export function getPictureDetailInfo(
  pictureId: string,
): Promise<API.ResponseInfo<PictureDetailInfoVo>> {
  return request({
    url: '/picture/pictureInfo/' + pictureId,
    method: 'get',
  })
}

/**
 * 获取我的图片信息
 * @param pictureId
 */
export function getMyPictureDetailInfo(
  pictureId: string,
): Promise<API.ResponseInfo<PictureDetailInfoVo>> {
  return request({
    url: '/picture/pictureInfo/my/' + pictureId,
    method: 'get',
  })
}

/**
 * 获取图片信息搜索建议
 */
export function getSearchRecommend(): Promise<API.ResponseInfo<PictureInfoSearchRecommendVo>> {
  return request({
    url: '/picture/pictureInfo/search/recommend',
    method: 'get',
  })
}

/**
 *  获取图片信息搜索建议
 * @param name
 */
export function getSearchSuggest(
  name: string,
): Promise<API.ResponseInfo<PictureInfoSearchSuggestionVo>> {
  return request({
    url: '/picture/pictureInfo/search/suggestion',
    method: 'get',
    params: {
      name: name,
    },
  })
}

//图片详情的推荐
export function getPictureInfoDetailRecommend(
  params: PictureInfoRecommendRequest,
): Promise<API.ResponseInfo<PictureInfoVo>> {
  return request({
    url: '/picture/pictureInfo/detail/recommend',
    method: 'get',
    params: params,
  })
}

//我的图片表格
export function listMyTable(
  params: PictureInfoQuery,
): Promise<API.ResponseInfo<PictureInfoTableVo>> {
  return request({
    url: '/picture/pictureInfo/list/my/table',
    method: 'get',
    params: params,
  })
}
