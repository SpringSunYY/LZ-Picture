<template>
  <a-menu
    v-model:openKeys="current"
    v-model:selectedKeys="current"
    mode="inline"
    :items="items"
    @click="doMenuClick"
  ></a-menu>
</template>
<script lang="ts" setup>
import { computed, h, ref } from 'vue'
import { type RouteRecordRaw, useRouter } from 'vue-router'
import usePermissionStore from '@/stores/modules/permission.ts'
import SvgIcon from '@/components/SvgIcon.vue'
import { generateMenu } from '@/router/permisson.ts'

const router = useRouter()

// 当前选中菜单
const current = ref<string[]>([])

// 监听路由变化，更新当前选中菜单
router.afterEach((to) => {
  current.value = [to.path.replace(/\/$/, '')] // 移除末尾斜杠
})

// 路由跳转事件
const doMenuClick = (route: RouteRecordRaw) => {
  if (route.item?.isFrame && route.item.isFrame) {
    // console.log('跳转外部链接')
    const url = route.item.path
    window.open(url, '_blank')
    return
  }
  router.push({
    path: route.key,
  })
}
const permissionStore = usePermissionStore()
// 动态生成菜单项
const items = computed(() => {
  // 生成有效路由结构
  // 合并静态菜单项
  return [...(generateMenu(permissionStore?.routes, '3') || [])]
})
// 图标渲染器
const renderIcon = (iconName: string) => {
  return () =>
    h(SvgIcon, {
      name: iconName,
      className: 'menu-icon',
    })
}
</script>
