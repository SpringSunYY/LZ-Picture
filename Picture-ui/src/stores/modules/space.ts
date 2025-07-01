import { getSpacePerm } from '@/api/picture/space'

let spacePerms: string[] = []
let permsLoaded = false
let loadingPromise: Promise<string[]> | null = null

export const spacePerm = {
  /**
   * 获取空间权限（同步）
   */
  getSpacePerms(): string[] {
    if (!permsLoaded) {
      throw new Error('权限未加载，请先调用 loadSpacePerms()')
    }
    return spacePerms
  },

  /**
   * 异步加载权限
   */
  async loadSpacePerms(): Promise<void> {
    if (permsLoaded) return

    if (!loadingPromise) {
      loadingPromise = getSpacePerm().then(res => {
        spacePerms = res.data || []
        permsLoaded = true
        loadingPromise = null
        return spacePerms
      })
    }

    await loadingPromise
  },

  /**
   * 手动刷新权限
   */
  async refreshSpacePerms(): Promise<void> {
    permsLoaded = false
    await this.loadSpacePerms()
  },
}
