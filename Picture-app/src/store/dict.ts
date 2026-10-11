import {getDicts} from '@/api/common/common'
import {formatDateByDate} from '@/utils/common'
import type {Module, ActionContext} from 'vuex'
import {toRaw} from 'vue'
import type {DictCache, DictCacheItem, DictList} from '@/types/dict.t'
import type {DictState, RootState} from './types'

/** 持久化键名（与旧实现保持一致，避免升级后历史缓存全部失效） */
const STORAGE_KEY = 'dict_cache'

/**
 * 生成带日期的 key：`YYYY-MM-DD:dictType`
 * web 端 useDictStore 同样做法，确保两端 key 形状对齐。
 */
const getDictKey = (key: string): string | null => {
    if (!key) return null
    const now = formatDateByDate(new Date())
    return `${now}:${key}`
}

/** 从本地存储加载字典数据（数组形式，向后兼容旧的 Record 形式） */
const loadDictFromStorage = (): DictCache => {
    try {
        const data = uni.getStorageSync(STORAGE_KEY)
        if (!data) return []
        const parsed = JSON.parse(data)
        // 兼容老版本 Record<string, DictList> 形态：转成数组
        if (parsed && typeof parsed === 'object' && !Array.isArray(parsed)) {
            const now = formatDateByDate(new Date())
            return Object.keys(parsed).map((k) => ({
                key: now + ':' + k,
                value: parsed[k] as DictList,
            }))
        }
        return Array.isArray(parsed) ? (parsed as DictCache) : []
    } catch (e) {
        console.error('加载字典缓存失败:', e)
        return []
    }
}

/** 同步落库（uni-app 用 setStorageSync，对标 web 端 persist 插件） */
const persistDictCache = (cache: DictCache): void => {
    try {
        // 用 toRaw 把 reactive/proxy 拆出来，避免 JSON.stringify 拿到奇怪对象
        uni.setStorageSync(STORAGE_KEY, JSON.stringify(toRaw(cache)))
    } catch (e) {
        console.error('保存字典缓存失败:', e)
    }
}

const dict: Module<DictState, RootState> = {
    namespaced: false,

    state: (): DictState => ({
        dict: loadDictFromStorage(),
    }),

    // 与 web 端一致:getDict 同步返回缓存(setDict/removeDict/cleanDict/initDict
    // /loadDict 是写操作,放在 actions)
    getters: {
        /**
         * 同步读字典缓存:命中即返回 DictList,未命中返回 null。
         * 与 web 端 useDictStore.getDict 的同步语义保持一致,
         * 这样 useDict 里可以 `store.getters.getDict(t)` 当作同步值用。
         */
        getDict:
            (state: DictState) =>
            (dictType: string): DictList | null => {
                if (!dictType) return null
                const dictKey = getDictKey(dictType)
                if (!dictKey) return null
                const item = state.dict.find(
                    (it: DictCacheItem) => it.key === dictKey,
                )
                if (item && Array.isArray(item.value) && item.value.length > 0) {
                    // 用 toRaw 拆掉 vuex 的 Proxy,避免下游 reactive 包装混乱
                    return toRaw(item.value)
                }
                return null
            },
    },

    actions: {
        /**
         * 写缓存：覆盖或追加。
         * 这里做成同步落库（与 web 端 setDict 一致），
         * 调用方通常是刚拿到接口数据后立即写入。
         */
        setDict(
            ctx: ActionContext<DictState, RootState>,
            payload: { key: string; value: DictList },
        ): void {
            const {key, value} = payload || {}
            if (!key) return
            const dictKey = getDictKey(key)
            if (!dictKey) return

            const idx = ctx.state.dict.findIndex((it: DictCacheItem) => it.key === dictKey)
            if (idx !== -1) {
                ctx.state.dict[idx].value = value
            } else {
                ctx.state.dict.push({key: dictKey, value})
            }
            persistDictCache(ctx.state.dict)
        },

        /**
         * 删除某条缓存（按当前日期 key 隔离，跨天不会误删）
         */
        removeDict(
            ctx: ActionContext<DictState, RootState>,
            dictType: string,
        ): boolean {
            if (!dictType) return false
            const dictKey = getDictKey(dictType)
            if (!dictKey) return false

            const idx = ctx.state.dict.findIndex((it: DictCacheItem) => it.key === dictKey)
            if (idx === -1) return false
            ctx.state.dict.splice(idx, 1)
            persistDictCache(ctx.state.dict)
            return true
        },

        /** 清空全部字典缓存（state + 落库） */
        cleanDict(ctx: ActionContext<DictState, RootState>): void {
            ctx.state.dict.splice(0, ctx.state.dict.length)
            persistDictCache(ctx.state.dict)
        },

        /**
         * 初始化钩子（与 web 端 useDictStore.initDict 对称）。
         * 当前只做"清空跨日期的旧缓存"，避免日期前缀失效后堆积垃圾。
         */
        initDict(ctx: ActionContext<DictState, RootState>): void {
            const today = formatDateByDate(new Date())
            const prefix = today + ':'
            const before = ctx.state.dict.length
            const filtered: DictCache = ctx.state.dict.filter((it: DictCacheItem) =>
                typeof it.key === 'string' ? it.key.startsWith(prefix) : false,
            )
            if (filtered.length !== before) {
                // 原地替换：保留同一引用，便于 vuex 变更追踪
                ctx.state.dict.splice(0, ctx.state.dict.length, ...filtered)
                persistDictCache(ctx.state.dict)
            }
        },

        /**
         * 取字典（带网络请求）：缓存命中直接 resolve；未命中则请求接口并写入缓存。
         * 这是 useDict 的主入口 —— 保留原异步签名，调用方 await。
         */
        async loadDict(
            ctx: ActionContext<DictState, RootState>,
            dictType: string,
        ): Promise<DictList> {
            if (!dictType) return []

            // 与 getter getDict 一致的命中逻辑:直接从 state 读 + toRaw
            const dictKey = getDictKey(dictType)
            let cached: DictList | null = null
            if (dictKey) {
                const item = ctx.state.dict.find(
                    (it: DictCacheItem) => it.key === dictKey,
                )
                if (item && Array.isArray(item.value) && item.value.length > 0) {
                    cached = toRaw(item.value)
                }
            }
            if (cached && cached.length > 0) return cached

            try {
                const list: DictList = await getDicts(dictType)
                if (list && Array.isArray(list) && list.length > 0) {
                    console.log(`[字典Store] 获取 ${dictType} 成功, 共 ${list.length} 条`)
                    ;(ctx.dispatch('setDict', {key: dictType, value: list}) as unknown) as void
                    return list
                }
                // 列表为空(0 条)—— 业务上算"没拿到",但接口本身是成功的,
                // 不再当作异常打日志,避免控制台噪音。
                return []
            } catch (error) {
                console.error(`[字典Store] 获取 ${dictType} 异常:`, error)
                return []
            }
        },
    },
}

export default dict
export type {DictCacheItem}