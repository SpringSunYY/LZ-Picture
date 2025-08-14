// 容量格式化
import type { PictureCategoryInfoVo } from '@/types/picture/pictureCategory'

//@ts-ignore
const dnsUrl = import.meta.env.VITE_DNS_URL
//获取文件名
export const getFileName = (url: string) => {
  if (!url) {
    return ''
  }
  //判断是否是有参数？
  if (url.includes('?')) {
    url = url.substring(0, url.indexOf('?'))
  }
  return url.substring(url.lastIndexOf('/') + 1)
}
/** 添加 DNS 前缀 */
export const formatDnsUrl = (url: string) => {
  if (!url) {
    return ''
  }
  if (url.startsWith('http')) {
    return url
  }
  return dnsUrl + url
}
export const formatSize = (bytes: number) => {
  const units = ['B', 'KB', 'MB', 'GB']
  let size = bytes
  let unitIndex = 0
  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024
    unitIndex++
  }
  //保留小数点后两位
  size = Math.round(size * 100) / 100
  return `${size}${units[unitIndex]}`
}

export const formatStrSize = (str: string, maxLength: number) => {
  //如果字符串长度大于最大长度，则截取并添加省略号
  if (str.length > maxLength) {
    str = str.substring(0, maxLength)
    // console.log('str', str)
    return str.substring(0, maxLength) + '...'
  } else {
    return str
  }
}

/**
 * 根据 categoryId 查找完整路径
 * @param data 分类列表
 * @param targetId 目标叶子节点 ID
 * @returns 路径数组，如 ['1', '1-1', '1-1-1']
 */
export const findPathById = (data: PictureCategoryInfoVo[], targetId: string): string[] | null => {
  for (const node of data) {
    const path = [node.categoryId]
    if (node.categoryId === targetId) {
      return path
    }
    if (node.children) {
      const childPath = findPathById(node.children, targetId)
      if (childPath) {
        return [...path, ...childPath]
      }
    }
  }
  return null
}

// 格式化函数
export const formatDate = (dateString: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

export const formatDateTimeByStr = (dateString: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
}

export const formatDateTimeByDate = (date: Date) => {
  date = new Date(date)
  return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
}

export const formatTime = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
}

/**
 * 转换时间为日期字符串
 * @param dateStr
 */
export const formatDateTime = (dateStr: string) => {
  //把时间转换为时间戳
  const date = new Date(dateStr)
  //当前时间
  const now = new Date()
  const diff = now.getDate() - date.getDate()
  if (date.getFullYear() === now.getFullYear() && date.getMonth() === now.getMonth()) {
    if (diff === 0) return '今天'
    if (diff === 1) return '昨天'
  }
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

/**
 * 初始化封面
 */
export const initCover = (cover: string) => {
  if (!cover) {
    console.log('cover', cover)
    return '/avatar.jpg'
  }
  return cover
}
