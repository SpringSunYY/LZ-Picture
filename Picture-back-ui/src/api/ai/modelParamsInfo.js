import request from '@/utils/request'

// 查询AI模型参数配置列表
export function listModelParamsInfo(query) {
  return request({
    url: '/ai/modelParamsInfo/list',
    method: 'get',
    params: query
  })
}

// 查询AI模型参数配置详细
export function getModelParamsInfo(modelId) {
  return request({
    url: '/ai/modelParamsInfo/' + modelId,
    method: 'get'
  })
}

// 新增AI模型参数配置
export function addModelParamsInfo(data) {
  return request({
    url: '/ai/modelParamsInfo',
    method: 'post',
    data: data
  })
}

// 修改AI模型参数配置
export function updateModelParamsInfo(data) {
  return request({
    url: '/ai/modelParamsInfo',
    method: 'put',
    data: data
  })
}

// 删除AI模型参数配置
export function delModelParamsInfo(modelId) {
  return request({
    url: '/ai/modelParamsInfo/' + modelId,
    method: 'delete'
  })
}
