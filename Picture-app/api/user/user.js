// 获取用户详细信息
import request from '@/utils/request'

export function getMyUserInfoByUserName(username) {
    return request({
        url: '/user/userInfo/my/' + username,
        method: 'get',
    })
}
