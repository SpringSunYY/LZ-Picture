import request from "@/utils/request";

export function getDicts(dictType){
    return request({
        url: '/system/dict/data/type/' + dictType,
        method: 'get',
    })
}

export function getConfig(key){
    return request({
        url: '/config/configInfo/key/' + key,
        method: 'get',
    })
}
