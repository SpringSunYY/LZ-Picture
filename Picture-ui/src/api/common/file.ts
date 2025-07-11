import { http as request } from '@/utils'
import type { PictureFileResponse, UrlUploadRequest } from '@/types/file'
import type { API } from '@/types/common'

export function pictureUpload(file: any): Promise<API.ResponseInfo<PictureFileResponse>> {
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

export function coverUploadFile(file: any): Promise<API.ResponseInfo<PictureFileResponse>> {
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

export function uploadFile(file: any): Promise<API.ResponseInfo<PictureFileResponse>> {
  return request({
    url: '/picture/file/upload/file',
    headers: {
      repeatSubmit: false,
    },
    method: 'post',
    data: file,
    timeout: 60000,
  })
}

export function urlUploadFile(
  data: UrlUploadRequest,
): Promise<API.ResponseInfo<PictureFileResponse>> {
  return request({
    url: '/picture/file/upload/url',
    headers: {
      repeatSubmit: false,
    },
    method: 'post',
    data: data,
    timeout: 60000,
  })
}

export function pictureDownload(pictureId: string): Promise<API.ResponseInfo<Blob>> {
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

export function pictureDownloadByLog(downloadId: string): Promise<API.ResponseInfo<Blob>> {
  return request({
    url: '/picture/file/download/log/' + downloadId,
    headers: {
      repeatSubmit: false,
    },
    responseType: 'blob',
    method: 'get',
    timeout: 60000,
  })
}
