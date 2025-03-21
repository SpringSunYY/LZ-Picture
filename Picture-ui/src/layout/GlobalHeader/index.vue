<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="200px">
        <RouterLink to="/">
          <div class="title-bar">
            <img class="logo" src="@/assets/logo.png" alt="logo" />
            <div class="title">荔枝云图库</div>
          </div>
        </RouterLink>
      </a-col>
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
        />
      </a-col>
      <a-col flex="120px">
        <div class="user-login-status">
          <div v-if="user?.userName">
            <a-dropdown>
              <ASpace>
                <a-avatar :src="user?.avatar" />
                {{ user?.userName ?? '未知' }}
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
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script setup lang="ts">
import { h, ref } from 'vue'
import { HomeOutlined, LogoutOutlined } from '@ant-design/icons-vue'
import { MenuProps } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import useUserStore from '@/stores/modules/user.js'
import type { UserInfo } from '@/types/common'

const userStore = useUserStore()
const user = ref<UserInfo>({
  userName: '',
  password: '',
})

function getUserInfo() {
  userStore.getInfo().then((res) => {
    user.value = res?.user
  })
}

getUserInfo()

// 用户注销
const doLogout = async () => {
  await userStore.logOut()
}
const router = useRouter()
// 当前选中菜单
const current = ref<string[]>([])
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  current.value = [to.path]
})

// 路由跳转事件
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  })
}

const items = ref<MenuProps['items']>([
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/about',
    label: '关于',
    title: '关于',
  },
  {
    key: 'others',
    label: h(
      'a',
      {
        href: 'https://github.com/SpringSunYY/LZ-Picture',
        target: '_blank',
      },
      '官方源码',
    ),
    title: '编程导航',
  },
])
</script>

<style scoped>
#globalHeader {
  max-width: 1440px;
  margin: 0 auto;

  .title-bar {
    display: flex;
    align-items: center;
  }

  .title {
    color: black;
    font-size: 18px;
    margin-left: 16px;
  }

  .logo {
    height: 48px;
  }
}
</style>
