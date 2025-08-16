<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col>
        <RouterLink to="/">
          <div class="title-bar">
            <img class="logo" src="../../../assets/logo.svg" alt="logo" />
            <div class="title" v-if="!isMobile">LZ-Picture</div>
          </div>
        </RouterLink>
      </a-col>
      <a-col flex="auto" class="menu">
        <!-- 桌面模式 -->
        <a-menu
          v-if="!isMobile"
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
        />

        <!-- 移动模式 -->
        <a-button v-else type="text" @click="menuVisible = true" class="mobile-menu-btn">
          <MenuOutlined />
        </a-button>

        <!-- 移动端抽屉菜单 -->
        <a-drawer
          width="60%"
          placement="left"
          v-model:open="menuVisible"
          :bodyStyle="{ padding: 0 }"
        >
          <a-menu
            v-model:selectedKeys="current"
            mode="inline"
            :items="items"
            @click="handleMobileMenuClick"
          />
        </a-drawer>
      </a-col>
      <a-col flex="5em">
        <div class="user-login-status">
          <div class="user-info" v-if="userName">
            <a-badge
              @click="clickInform"
              title="通知信息，点击查看"
              :count="unreadInformCount"
              :number-style="{
                backgroundColor: '#fff',
                color: '#00ff3d',
                boxShadow: '0 0 0 1px #d9d9d9 inset',
              }"
            >
              <notification-outlined class="header-icon" />
            </a-badge>

            <a-dropdown>
              <div class="user-avatar" @click="showDrawer">
                <a-avatar :size="36" :src="initCover(avatar)" />
              </div>
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
        width="35vh"
        v-model:open="open"
        class="custom-class"
        root-class-name="root-class-name"
        placement="right"
      >
        <template #title>
          <a-dropdown>
            <a-space align="center">
              <a-avatar :src="initCover(avatar)" size="large" />
              <div>
                <div class="nickname" style="font-size: 14px">{{ userName }}</div>
                <div class="nickname" style="font-size: 12px; color: #616161">{{ nickName }}</div>
              </div>
            </a-space>
            <template #overlay>
              <a-menu>
                <a-menu-item @click="doLogout">
                  <LogoutOutlined />
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </template>
        <SideRight @doMenuClick="() => (open = !open)" />
      </a-drawer>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { LogoutOutlined, MenuOutlined, NotificationOutlined } from '@ant-design/icons-vue'
import { message, Modal } from 'ant-design-vue'
import { type RouteRecordRaw, useRouter } from 'vue-router'
import useUserStore from '@/stores/modules/user.ts'
import { storeToRefs } from 'pinia'
import usePermissionStore from '@/stores/modules/permission.ts'
import SideRight from '@/layout/base/SideRight.vue'
import { getUnReadInformCount } from '@/api/user/inform.ts'
import { initCover } from '@/utils/common.ts'
import { generateMenu, toMenu } from '@/router/permisson.ts'

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
      open.value = false
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
  current.value = [to.name as string]
})

const doMenuClick = (route: RouteRecordRaw) => {
  if (isMobile.value) {
    menuVisible.value = false
  }
  toMenu(route)
}

const handleMobileMenuClick = (route: any) => {
  menuVisible.value = false
  toMenu(route)
}

const permissionStore = usePermissionStore()
const unreadInformCount = ref<number>(0)

const isMobile = ref(false)
const menuVisible = ref(false)

// 检测屏幕尺寸
const checkScreenSize = () => {
  isMobile.value = window.innerWidth < 768
}
// 初始化用户信息
onMounted(async () => {
  if (userStore.token) {
    userName.value = userStore.userName
    avatar.value = userStore.avatar
    getUnReadInformCount().then((res) => {
      if (res.code === 200) {
        unreadInformCount.value = res.data || 0
      }
    })
  }
  checkScreenSize()
  window.addEventListener('resize', checkScreenSize)
})

const clickInform = () => {
  router.push({
    name: 'inform',
  })
}
// 动态生成菜单项
const items = computed(() => {
  // 生成有效路由结构
  // 合并静态菜单项
  return [...(generateMenu(permissionStore?.getRoutes(), '2') || [])]
})

const open = ref<boolean>(false)

const showDrawer = () => {
  open.value = true
}
</script>

<style scoped>
#globalHeader {
  margin: 0 2em;

  .title-bar {
    display: flex;
    float: left;
    height: 100%;
    align-items: center;
  }

  .title {
    font-size: 18px;
    color: #096dd9;
    font-family: 'sans-serif';
  }

  .logo {
    height: 48px;
  }

  .menu {
    padding-left: 10px;
  }

  .user-login-status {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    height: 100%;

    .user-info {
      display: flex;
      align-items: center;
      gap: 16px;
      height: 64px;
    }

    .header-icon {
      font-size: 24px;
      cursor: pointer;
      display: inline-flex;
      align-items: center;
    }

    .user-avatar {
      cursor: pointer;
      display: inline-flex;
      align-items: center;
    }
  }

  .mobile-menu-btn {
    padding: 0 10px;
    display: flex;
    align-items: center;
    height: 100%;
  }

  /* 移动端菜单项样式 */

  .ant-menu-inline {
    border-right: none;
  }

  .ant-menu-item {
    margin: 4px 0;
  }
}
</style>
