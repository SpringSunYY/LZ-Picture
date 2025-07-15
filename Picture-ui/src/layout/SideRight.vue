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
import { computed, ref } from 'vue'
import { type RouteRecordRaw, useRouter } from 'vue-router'
import usePermissionStore from '@/stores/modules/permission.ts'
import { generateMenu, toMenu } from '@/router/permisson.ts'

const router = useRouter()
// 定义事件
const emit = defineEmits(['doMenuClick'])
// 当前选中菜单
const current = ref<string[]>([])

// 监听路由变化，更新当前选中菜单
router.afterEach((to) => {
  current.value = [to.name as string]
})

// 路由跳转事件
const doMenuClick = (route: RouteRecordRaw) => {
  toMenu(route)
  emit('doMenuClick')
}
const permissionStore = usePermissionStore()
// 动态生成菜单项
const items = computed(() => {
  // 生成有效路由结构
  // 合并静态菜单项
  return [...(generateMenu(permissionStore?.routes, '3') || [])]
})
</script>
