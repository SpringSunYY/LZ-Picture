/**
 * 图片文件返回对象
 * 用于封装上传或获取图片文件后的返回信息
 */
export interface PictureFileResponse {
  /**
   * 图片 URL
   */
  url: string

  /**
   * 图片域名
   */
  dnsUrl: string

  /**
   * 图片名称
   */
  name: string

  /**
   * 图片体积（字节）
   */
  picSize: number

  /**
   * 图片宽度
   */
  picWidth: number

  /**
   * 图片高度
   */
  picHeight: number

  /**
   * 宽高比例
   */
  picScale: number

  /**
   * 图片格式
   */
  picFormat: string

  /**
   * 缩略图 URL
   */
  thumbnailUrl: string
}

export interface UrlUploadRequest {
  url: string;
  pictureId: string;
}
