// 登录方法
import { http as request } from '@/utils'

export function pictureUpload(file: any) {
  return request({
    url: '/picture/pictureInfo/upload',
    headers: {
      isToken: false,
      repeatSubmit: false,
    },
    method: 'post',
    data: file,
    timeout: 60000,
  })
}
