<template>
    <up-tabbar
        :value="current"
        @change="onChange"
        :fixed="true"
        :placeholder="true"
        :safeAreaInsetBottom="true"
        inactive-color="#999999"
        style-type="convex"
        animation-type="scale"
    >
        <up-tabbar-item text="首页" icon="home" active-icon="home-fill" />
        <up-tabbar-item text="发现" icon="photo" active-icon="photo-fill" />
        <up-tabbar-item
            text=""
            icon="plus-circle"
            active-icon="plus-circle-fill"
            mode="midButton"
        />
        <up-tabbar-item text="消息" icon="chat" active-icon="chat-fill" />
        <up-tabbar-item text="我的" icon="account" active-icon="account-fill" />
    </up-tabbar>
</template>

<script>
const TABBAR_PAGES = [
    'pages/index',
    'pages/ai/discover',
    'pages/generate/index',
    'pages/message/index',
    'pages/user/profile',
]

export default {
    data() {
        return {
            currentPath: '',
        }
    },
    computed: {
        current() {
            const idx = TABBAR_PAGES.indexOf(this.currentPath)
            return idx < 0 ? 0 : idx
        },
    },
    mounted() {
        this.updateCurrentPath()
        // uni-app 跳页时触发这个
        // #ifdef H5
        this._onRouteChange = () => this.updateCurrentPath()
        uni.$on?.('__uni__page__change', this._onRouteChange)
        // #endif
    },
    beforeUnmount() {
        // #ifdef H5
        uni.$off?.('__uni__page__change', this._onRouteChange)
        // #endif
    },
    methods: {
        updateCurrentPath() {
            try {
                const pages =
                    typeof getCurrentPages === 'function' ? getCurrentPages() : []
                if (pages && pages.length) {
                    const cur = pages[pages.length - 1]
                    this.currentPath = cur?.route || cur?.$page?.route || ''
                }
            } catch (e) {
                this.currentPath = ''
            }
        },
        onChange(name) {
            const idx = Number(name)
            const target = TABBAR_PAGES[idx]
            if (!target) return
            uni.switchTab({ url: '/' + target, fail: () => {
                // 非 tabBar 页面时 fallback
                uni.reLaunch({ url: '/' + target })
            }})
        },
    },
}
</script>
