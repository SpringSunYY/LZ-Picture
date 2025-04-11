// 容量格式化
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
    console.log('str',str)
    return str.substring(0, maxLength) + '...'
  } else {
    return str
  }
}
