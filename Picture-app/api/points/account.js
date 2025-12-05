import request from "@/utils/request";
import {encryptBack} from "@/utils/jsencrypt.js";

/**
 * 获取账户信息
 */
export function getAccountInfo() {
    return request({
        url: '/points/accountInfo',
        method: 'get',
    })
}

//获取用户账户最近是否校验密码
export function getAccountPasswordVerify() {
    return request({
        url: '/points/accountInfo/verifyPassword',
        method: 'get',
    })
}

//校验密码
export function verifyPassword(password){
    return request({
        url: '/points/accountInfo/verifyPassword',
        method: 'post',
        data: { password: encryptBack(password) },
    })
}
