import request from '@/utils/request'

// 查询菜单信息列表
export function listMenuInfo(query) {
    return request({
        url: '/config/menuInfo/list',
        method: 'get',
        params: query
    })
}

// 查询菜单信息详细
export function getMenuInfo(menuId) {
    return request({
        url: '/config/menuInfo/' + menuId,
        method: 'get'
    })
}

// 初始化菜单信息缓存
export function initMenuInfoCache() {
    return request({
        url: '/config/menuInfo/reset',
        method: 'delete',
    })
}

// 新增菜单信息
export function addMenuInfo(data) {
    return request({
        url: '/config/menuInfo',
        method: 'post',
        data: data
    })
}

// 修改菜单信息
export function updateMenuInfo(data) {
    return request({
        url: '/config/menuInfo',
        method: 'put',
        data: data
    })
}

// 删除菜单信息
export function delMenuInfo(menuId) {
    return request({
        url: '/config/menuInfo/' + menuId,
        method: 'delete'
    })
}
