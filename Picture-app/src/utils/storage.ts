import constant from './constant'

// 存储变量名
const storageKey: string = 'storage_data'

// 存储节点变量名（必须与 constant 保持同步；列表外的 key 不会被持久化）
const storageNodeKeys: string[] = [
  constant.avatar,
  constant.name,
  constant.roles,
  constant.permissions,
  constant.userId,
  constant.userName,
  constant.nickName,
  // UI 偏好
  constant.rememberMe,
  constant.rememberAccount,
]

type StorageValue = string | number | boolean | string[] | null

interface Storage {
  set: (key: string, value: StorageValue) => void
  get: <T = string>(key: string) => T
  remove: (key: string) => void
  clean: () => void
}

const storage: Storage = {
  set: function (key: string, value: StorageValue): void {
    if (storageNodeKeys.indexOf(key) !== -1) {
      let tmp: Record<string, StorageValue> = uni.getStorageSync(storageKey)
      tmp = tmp ? tmp : {}
      tmp[key] = value
      uni.setStorageSync(storageKey, tmp)
    }
  },
  get: function <T = string>(key: string): T {
    const storageData: Record<string, StorageValue> = uni.getStorageSync(storageKey) || {}
    return (storageData[key] ?? '') as T
  },
  remove: function (key: string): void {
    const storageData: Record<string, StorageValue> = uni.getStorageSync(storageKey) || {}
    delete storageData[key]
    uni.setStorageSync(storageKey, storageData)
  },
  clean: function (): void {
    uni.removeStorageSync(storageKey)
  },
}

export default storage
