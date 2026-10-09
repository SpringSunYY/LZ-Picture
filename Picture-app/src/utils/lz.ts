/**
 * 参数处理
 */
export function tansParams(params: Record<string, unknown>): string {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName]
    const part = encodeURIComponent(propName) + '='
    if (value !== null && value !== '' && typeof value !== 'undefined') {
      if (typeof value === 'object') {
        for (const key of Object.keys(value as Record<string, unknown>)) {
          const objVal = (value as Record<string, unknown>)[key]
          if (objVal !== null && objVal !== '' && typeof objVal !== 'undefined') {
            const param = `${propName}[${key}]`
            result += encodeURIComponent(param) + '=' + encodeURIComponent(String(objVal)) + '&'
          }
        }
      } else {
        result += part + encodeURIComponent(String(value)) + '&'
      }
    }
  }
  return result
}

/**
 * 树节点（递归结构，子节点字段名由入参决定）
 */
export interface TreeNode<T> {
  [key: string]: unknown
  children?: TreeNode<T>[]
}

/**
 * 构建树结构
 * @param data 原始数据数组
 * @param id 字段名（默认为 'menuId'）
 * @param parentId 父字段名（默认为 'parentId'）
 * @param children 子节点字段名（默认为 'children'）
 * @returns 构建后的树结构
 */
export function handleTree<T extends Record<string, unknown>>(
  data: T[],
  id: keyof T | string = 'menuId',
  parentId: keyof T | string = 'parentId',
  children: keyof T | string = 'children'
): TreeNode<T>[] {
  // 默认参数值
  const idKey = (id || 'menuId') as keyof T
  const parentIdKey = (parentId || 'parentId') as keyof T
  const childrenKey = (children || 'children') as keyof T

  // 子节点映射
  const childrenListMap: Record<string, TreeNode<T>[]> = {}
  // 节点映射
  const nodeIds: Record<string, TreeNode<T>> = {}

  // 遍历数据，填充 childrenListMap 和 nodeIds
  for (const d of data) {
    const parentIdValue = String(d[parentIdKey] ?? '')
    if (!childrenListMap[parentIdValue]) {
      childrenListMap[parentIdValue] = []
    }
    nodeIds[String(d[idKey])] = d as unknown as TreeNode<T>
    childrenListMap[parentIdValue].push(d as unknown as TreeNode<T>)
  }

  // 构建根节点
  const tree: TreeNode<T>[] = []
  for (const d of data) {
    const parentIdValue = String(d[parentIdKey] ?? '')
    if (!nodeIds[parentIdValue]) {
      tree.push(d as unknown as TreeNode<T>)
    }
  }

  // 递归转换子节点
  for (const t of tree) {
    adaptToChildrenList(t)
  }

  /**
   * 递归适配子节点
   */
  function adaptToChildrenList(o: TreeNode<T>): void {
    const childrenList = childrenListMap[String((o as T)[idKey] ?? '')]
    if (childrenList) {
      ;(o as Record<string, unknown>)[childrenKey as string] = childrenList
      for (const c of childrenList) {
        adaptToChildrenList(c)
      }
    }
  }

  return tree
}

export default { tansParams }
