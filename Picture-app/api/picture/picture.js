import request from '@/utils/request'


/**
 * 查询图片信息列表
 * @param params
 */
export function getPictureInfoRecommend(params) {
    return request({
        url: '/picture/pictureInfo/recommend',
        params: params,
    })
}
