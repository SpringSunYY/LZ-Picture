import { pictureDownload } from '@/api/common/file.ts'

export async function downloadImage(pictureId: string, fileName?: string) {
  try {
    const response = await pictureDownload(pictureId)
    //@ts-ignore
    const blob = new Blob([response], { type: 'application/octet-stream' })

    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = fileName || `${pictureId}.jpg`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (error) {
    console.error('下载失败：', error)
    throw new Error('文件下载失败，请重试') // 建议添加错误抛出供上层处理
  }
}
