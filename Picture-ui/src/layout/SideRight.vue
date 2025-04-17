<template>
  <a-menu
    v-model:openKeys="current"
    v-model:selectedKeys="current"
    mode="inline"
    :items="items"
    @onClick="doMenuClick"
  ></a-menu>
</template>
<script lang="ts" setup>
import { computed, h, ref } from 'vue'
import { type MenuProps } from 'ant-design-vue'
import { type RouteRecordRaw, useRouter } from 'vue-router'
import usePermissionStore from '@/stores/modules/permission.ts'
import SvgIcon from '@/components/SvgIcon.vue'

const router = useRouter()

// 当前选中菜单
const current = ref<string[]>([])

// 监听路由变化，更新当前选中菜单
router.afterEach((to) => {
  current.value = [to.path.replace(/\/$/, '')] // 移除末尾斜杠
})

// 路由跳转事件
const doMenuClick = (route: RouteRecordRaw) => {
  // console.log('点击', route)
  router.push({
    path: route.path,
  })
}
const permissionStore = usePermissionStore()

// 递归检查路由隐藏状态
const checkRouteHidden = (route: RouteRecordRaw | undefined): boolean => {
  if (!route) return false // 确保 route 为空时直接返回 false

  // console.log('checkRouteHidden', route)
  // 当前路由标记隐藏
  if (route?.meta?.isHidden) return true
  // console.log('checkRouteHidden', route.meta?.isCache)
  if (route?.meta?.menuAddress !== undefined && route?.meta?.menuAddress !== '3') {
    return true
  }
  // 查找父路由路径（确保不会找出空路径）
  const parentPath = route.path.split('/').slice(0, -1).join('/') || '/'
  if (parentPath === route.path) return false // 避免路径相同导致死循环

  const parentRoute = permissionStore.routes.find((r) => r.path === parentPath)

  // 递归检查父级隐藏状态
  return parentRoute ? checkRouteHidden(parentRoute) : false
}
const generateMenu = (routes: RouteRecordRaw[]): MenuProps['items'] => {
  // console.log('generateMenu', routes)
  return routes
    .filter((route) => {
      return !route.redirect && route?.meta?.title && !checkRouteHidden(route)
    })
    .map((route) => {
      // console.log('route', route)
      const menuItem: any = {
        key: route.path,
        label: route?.meta?.title,
        onClick: () => {
          if (route.meta?.menuType && route.meta.menuType !== 'C') return
          router.push({ path: route.path })
        },
        title: route?.meta?.title,
        icon: route?.meta?.icon ? renderIcon(route.meta.icon as string) : undefined,
      }

      // 只有在存在子节点时才添加 children
      if (route.children?.length) {
        const children = generateMenu(
          route.children.map((child) => ({
            ...child,
            path: `${child.path}`.replace(/\/+/g, '/'),
          })),
        )
        if (route.children.length > 0) {
          menuItem.children = children
        }
      }

      // console.log('menuItem', menuItem)
      return menuItem
    })
}
// 动态生成菜单项
const items = computed(() => {
  // 生成有效路由结构
  // 合并静态菜单项
  return [...(generateMenu(permissionStore?.routes) || [])]
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
