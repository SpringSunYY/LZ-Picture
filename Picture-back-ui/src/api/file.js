import request from '@/utils/request'

/**
 * 获取地图数据
 * @param area 比如国家就直接国家名称，比如中国就直接china，省级就是china/province，市级：china/citys，区县级：china/county
 * @param name 名称，这个是省、市、县名称，比如贵州省
 * @returns {*}
 */
export function getGeoJson(area, name) {
    return request({
        url: '/common/geo',
        method: 'get',
        params: {
            area: area,
            name: name
        }
    })
}
