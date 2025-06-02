import { pictureDownload, pictureDownloadByLog } from '@/api/common/file.ts'
import { message } from 'ant-design-vue'

export async function downloadImage(pictureId: string, fileName?: string) {
  try {
    await pictureDownload(pictureId).then((blob) => {
      // console.log('blob', blob)
      // 如果是 JSON 类型，说明是错误信息
      //@ts-ignore
      if (blob.type !== 'application/octet-stream') {
        const reader = new FileReader()
        return new Promise((resolve, reject) => {
          reader.onload = () => {
            try {
              const json = JSON.parse(reader.result as string)
              message.error(json.msg)
              resolve({ success: false, message: json.msg, data: null })
            } catch (e) {
              reject(new Error('无法解析错误响应'))
            }
          }
          reader.onerror = () => reject(reader.error)
          //@ts-ignore
          reader.readAsText(blob)
          throw new Error('文件下载失败，请重试')
        })
      } else {
        // 处理正常的文件下载
        //@ts-ignore
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = fileName || `${pictureId}.jpg`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        return { success: true, message: '下载成功', data: null }
      }
    })
  } catch (error) {
    console.error('下载失败：', error)
    throw new Error('文件下载失败，请重试') // 建议添加错误抛出供上层处理
  }
}

export async function downloadImageByLog(downloadId: string, pictureName: string) {
  try {
    await pictureDownloadByLog(downloadId).then((blob) => {
      console.log('blob', blob)
      // 如果是 JSON 类型，说明是错误信息
      //@ts-ignore
      if (blob.type !== 'application/octet-stream') {
        const reader = new FileReader()
        return new Promise((resolve, reject) => {
          reader.onload = () => {
            try {
              const json = JSON.parse(reader.result as string)
              message.error(json.msg)
              resolve({ success: false, message: json.msg, data: null })
            } catch (e) {
              reject(new Error('无法解析错误响应'))
            }
          }
          reader.onerror = () => reject(reader.error)
          //@ts-ignore
          // reader.readAsText(blob)
          throw new Error('文件下载失败，请重试')
        })
      } else {
        console.log('blob', blob)
        // 处理正常的文件下载
        //@ts-ignore
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = pictureName || `${downloadId}.jpg`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        return { success: true, message: '下载成功', data: null }
      }
    })
  } catch (error) {
    console.error('下载失败：', error)
    throw new Error('文件下载失败，请重试') // 建议添加错误抛出供上层处理
  }
}
