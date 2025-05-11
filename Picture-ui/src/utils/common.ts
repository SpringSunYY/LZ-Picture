// 容量格式化
import type { PictureCategoryInfoVo } from '@/types/picture/pictureCategory'

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
    console.log('str', str)
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
