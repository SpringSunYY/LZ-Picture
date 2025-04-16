// 登录方法
import { http as request } from '@/utils'

export function pictureUpload(file: any) {
  return request({
    url: '/picture/file/upload',
    headers: {
      repeatSubmit: false,
    },
    method: 'post',
    data: file,
    timeout: 60000,
  })
}

export function coverUploadFile(file: any) {
  return request({
    url: '/picture/file/upload/cover',
    headers: {
      repeatSubmit: false,
    },
    method: 'post',
    data: file,
    timeout: 60000,
  })
}

export function pictureDownload(pictureId: string) {
  return request({
    url: '/picture/file/download/' + pictureId,
    headers: {
      repeatSubmit: false,
    },
    responseType: 'blob',
    method: 'get',
    timeout: 60000,
  })
}
