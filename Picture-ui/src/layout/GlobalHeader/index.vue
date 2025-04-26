<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="200px">
        <RouterLink to="/">
          <div class="title-bar">
            <img class="logo" src="@/assets/logo.png" alt="logo" />
            <div class="title">荔智云图</div>
          </div>
        </RouterLink>
      </a-col>
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @onClick="doMenuClick"
        >
        </a-menu>
      </a-col>
      <a-col flex="120px">
        <div class="user-login-status">
          <div v-if="userName">
            <a-dropdown>
              <ASpace @click="showDrawer">
                <a-avatar :src="avatar" />
                {{ nickName ?? '未知' }}
              </ASpace>
              <template #overlay>
                <a-menu>
                  <a-menu-item @click="doLogout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录/注册</a-button>
          </div>
        </div>
      </a-col>
      <a-drawer
        width="36vh"
        v-model:open="open"
        class="custom-class"
        root-class-name="root-class-name"
        placement="right"
        @after-open-change="afterOpenChange"
      >
        <template #title>
          <a-space align="center">
            <a-avatar :src="avatar" size="large" />
            <div>
              <div class="nickname" style="font-size: 14px">{{ userName }}</div>
              <div class="nickname" style="font-size: 12px; color: #616161">{{ nickName }}</div>
            </div>
          </a-space>
        </template>
        <SideRight />
      </a-drawer>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref } from 'vue'
import { LogoutOutlined } from '@ant-design/icons-vue'
import { MenuProps, message, Modal } from 'ant-design-vue'
import { type RouteRecordRaw, useRouter } from 'vue-router'
import useUserStore from '@/stores/modules/user.js'
import { storeToRefs } from 'pinia'
import SvgIcon from '@/components/SvgIcon.vue'
import usePermissionStore from '@/stores/modules/permission.ts'
import SideRight from '@/layout/SideRight.vue'

const userStore = useUserStore()
const { userName: userName, avatar: avatar, nickName: nickName } = storeToRefs(userStore) // 使用 storeToRefs 提取响应式状态
const router = useRouter()
// 用户注销
const doLogout = async () => {
  Modal.confirm({
    title: '确认退出登录',
    content: '您确定要退出登录吗？',
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      await userStore.logOut()
      router.push('/')
      message.success('退出登录成功')
    },
    onCancel() {
      console.log('取消退出登录')
    },
  })
}

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
// 初始化用户信息
onMounted(async () => {
  if (userStore.token) {
    userName.value = userStore.userName
    avatar.value = userStore.avatar
  }
})
// 递归检查路由隐藏状态
const checkRouteHidden = (route: RouteRecordRaw | undefined): boolean => {
  if (!route) return false // 确保 route 为空时直接返回 false

  // console.log('checkRouteHidden', route)
  // 当前路由标记隐藏
  if (route?.meta?.isHidden) return true
  // console.log('checkRouteHidden', route.meta?.isCache)
  if (route?.meta?.menuAddress !== undefined && route?.meta?.menuAddress !== '2') {
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
      console.log('checkRouteHidden', route)
      return (
        !route.redirect && route?.meta?.title && !route.meta?.isHidden && !checkRouteHidden(route)
      )
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
        if (children && children.length > 0) {
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
  return [
    ...(generateMenu(permissionStore?.routes) || []),
    {
      key: 'others',
      label: h(
        'a',
        {
          href: 'https://github.com/SpringSunYY/LZ-Picture',
          target: '_blank',
          click: (e: Event) => {
            e.preventDefault() // 阻止默认跳转
            const target = e.currentTarget as HTMLAnchorElement // 类型断言
            if (target) {
              window.open(target.href, '_blank') // 手动跳转
            }
          },
        },
        '官方源码',
      ),
      icon: renderIcon('space'),
      title: '编程导航',
    },
  ]
})
// 图标渲染器
const renderIcon = (iconName: string) => {
  return () =>
    h(SvgIcon, {
      name: iconName,
      className: 'menu-icon',
    })
}

const open = ref<boolean>(false)

const afterOpenChange = (bool: boolean) => {
  console.log('open', bool)
}

const showDrawer = () => {
  open.value = true
}
</script>

<style scoped>
#globalHeader {
  margin: 0 4vh;

  .title-bar {
    display: flex;
    align-items: center;
  }

  .title {
    color: black;
    font-size: 18px;
    margin-left: 16px;
    font-family: 'sans-serif';
  }

  .logo {
    height: 48px;
  }
}
</style>
