import request from '@/utils/request'
import {getToken} from '@/utils/auth'
import type {PictureFileResponse, UrlUploadRequest} from '@/types/file'
import type {API} from '@/types/common'
import type {PictureDownloadVo} from '@/types/picture/picture'

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

/**
 * 封面上传:MP/App 用 uni.uploadFile,H5 用 fetch + FormData。
 * - H5 端 uni.uploadFile 的 filePath 只收字符串,无法传 Blob
 * - MP 端 uni.request 传 {tempPath} 不是合法 multipart,后端报 500
 * - H5 把 Blob 包成 File 再 append,避免 cross-realm Blob instanceof 失败
 * 后端契约(/picture/file/upload/cover):
 *   multipart: file
 *   form:     type (默认 1), fileDir (默认 cover)
 */
export function coverUploadFile(
  file: any,
  options: {type?: string; fileDir?: string} = {},
  url = '/picture/file/upload/cover',
): Promise<API.ResponseInfo<PictureFileResponse>> {
  const {type = '1', fileDir = 'cover'} = options
  const token = getToken() || ''
  // #ifdef H5
  return new Promise((resolve, reject) => {
    const blob: Blob | undefined = file?.blob
    const name: string = file?.name || 'cover.jpg'
    if (!blob) {
      reject(new Error('上传数据为空'))
      return
    }
    const fd = new FormData()
    // 用 File 包装,绕开 cross-realm Blob instanceof 失败
    fd.append('file', new File([blob], name, {type: blob.type || 'image/jpeg'}))
    fd.append('type', type)
    fd.append('fileDir', fileDir)
    fetch('/api' + url, {
      method: 'POST',
      headers: token ? {Authorization: 'UserBearer ' + token} : {},
      body: fd,
    })
      .then((res) => {
        if (!res.ok) throw new Error('上传失败 HTTP ' + res.status)
        return res.json()
      })
      .then((body) => {
        if (body?.code !== 200 && body?.code !== 0) throw new Error(body?.msg || '上传失败')
        resolve(body as API.ResponseInfo<PictureFileResponse>)
      })
      .catch((err) => reject(err instanceof Error ? err : new Error(String(err))))
  })
  // #endif
  // #ifndef H5
  return new Promise((resolve, reject) => {
    const filePath: string = file?.tempPath || file?.filePath || ''
    if (!filePath) {
      reject(new Error('上传数据为空'))
      return
    }
    const baseUrl =
      (import.meta.env.VITE_APP_BASE_API as string | undefined) || 'http://localhost:8080'
    uni.uploadFile({
      url: baseUrl + url,
      filePath,
      name: 'file',
      header: token ? {Authorization: 'UserBearer ' + token} : {},
      formData: {type, fileDir},
      success: (res) => handleUploadSuccess(res, resolve, reject),
      fail: (err) => reject(new Error(err?.errMsg || '上传请求失败')),
    })
  })
  // #endif
}

function handleUploadSuccess(
  res: UniApp.UploadFileSuccessCallbackResult,
  resolve: (value: API.ResponseInfo<PictureFileResponse>) => void,
  reject: (reason: Error) => void,
) {
  if (!res.data) {
    reject(new Error('上传响应为空'))
    return
  }
  let body: API.ResponseInfo<PictureFileResponse>
  try {
    // H5 下 res.data 可能是对象,MP 下是字符串
    body = typeof res.data === 'string' ? JSON.parse(res.data) : res.data
  } catch {
    reject(new Error('响应解析失败'))
    return
  }
  if (body?.code === 200 || body?.code === 0) resolve(body)
  else reject(new Error(body?.msg || '上传失败'))
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

export function pictureDownloadByMy(pictureId: string): Promise<API.ResponseInfo<Blob>> {
  return request({
    url: '/picture/file/download/my/' + pictureId,
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

export function getPictureOriginalLogInfo(
  pictureId: string,
): Promise<API.ResponseInfo<PictureDownloadVo>> {
  return request({
    url: '/picture/file/original/' + pictureId,
    method: 'get',
  })
}

export function getPictureOriginalLogInfoByLog(
  downloadId: string,
): Promise<API.ResponseInfo<PictureDownloadVo>> {
  return request({
    url: '/picture/file/log/original/' + downloadId,
    method: 'get',
  })
}

export function getPictureOriginByMy(
  pictureId: string,
): Promise<API.ResponseInfo<PictureDownloadVo>> {
  return request({
    url: '/picture/file/original/my/' + pictureId,
    method: 'get',
  })
}
