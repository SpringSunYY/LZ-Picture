import type { API } from '@/types/common'
import { http as request } from '@/utils'
import type {
  CreateOutPaintingTaskResponse,
  CreatePictureOutPaintingTaskRequest,
  GetOutPaintingTaskResponse,
} from '@/types/picture/ai'

export function createPictureOutPaintingTask(
  data: CreatePictureOutPaintingTaskRequest,
): Promise<API.ResponseInfo<CreateOutPaintingTaskResponse>> {
  return request({
    url: '/picture/ai/outPainting/createTask',
    method: 'post',
    data: data,
  })
}

export function getPictureOutPaintingTask(
  taskId: string,
): Promise<API.ResponseInfo<GetOutPaintingTaskResponse>> {
  return request({
    url: '/picture/ai/outPainting/getTask',
    method: 'get',
    params: {
      taskId: taskId,
    },
  })
}
