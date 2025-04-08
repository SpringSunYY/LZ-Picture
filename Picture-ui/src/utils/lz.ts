/**
 * 参数处理
 * @param params 参数
 */
function tansParams({ params }: { params: any }): string {
  let result = ''
  for (const propName in params) {
    const value = params[propName]
    const part = encodeURIComponent(propName) + '='

    if (value !== null && value !== '' && typeof value !== 'undefined') {
      if (typeof value === 'object') {
        for (const key in value) {
          if (value[key] !== null && value[key] !== '' && typeof value[key] !== 'undefined') {
            const param = `${propName}[${key}]`
            result += encodeURIComponent(param) + '=' + encodeURIComponent(value[key]) + '&'
          }
        }
      } else {
        result += part + encodeURIComponent(value) + '&'
      }
    }
  }
  return result
}

/**
 * 构建树结构
 * @param data 原始数据数组
 * @param id 字段名（默认为 'menuId'）
 * @param parentId 父字段名（默认为 'parentId'）
 * @param children 子节点字段名（默认为 'children'）
 * @returns 构建后的树结构
 */
export function handleTree(
  data: any[],
  id: string = 'menuId',
  parentId: string = 'parentId',
  children: string = 'children',
) {
  // 配置映射
  const config = {
    id,
    parentId,
    childrenList: children,
  }

  // 子节点映射
  const childrenListMap: Record<string, any[]> = {}
  // 节点映射
  const nodeIds: Record<string, any> = {}
  // 自动推导树结构
  const tree = []

  // 遍历数据，填充 childrenListMap 和 nodeIds
  for (const d of data) {
    const parentIdValue = d[config.parentId]
    if (!childrenListMap[parentIdValue]) {
      childrenListMap[parentIdValue] = []
    }
    nodeIds[d[config.id]] = d
    childrenListMap[parentIdValue].push(d)
  }

  // 构建根节点
  for (const d of data) {
    const parentIdValue = d[config.parentId]
    if (!nodeIds[parentIdValue]) {
      tree.push(d)
    }
  }

  // 递归转换子节点
  for (const t of tree) {
    adaptToChildrenList(t)
  }

  /**
   * 递归适配子节点
   * @param o 当前节点
   */
  function adaptToChildrenList(o: any) {
    const childrenList = childrenListMap[o[config.id]]
    if (childrenList) {
      o[config.childrenList] = childrenList
      for (const c of childrenList) {
        adaptToChildrenList(c)
      }
    }
  }

  return tree
}

export default { tansParams }
