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
          <div v-if="userName">
            <a-dropdown>
              <ASpace>
                <a-avatar :src="avatar" />
                {{ userName ?? '未知' }}
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
import { h, ref, onMounted } from 'vue'
import { HomeOutlined, LogoutOutlined, PictureOutlined } from '@ant-design/icons-vue'
import { MenuProps, message, Modal } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import useUserStore from '@/stores/modules/user.js'
import { storeToRefs } from 'pinia'
import SvgIcon from '@/components/SvgIcon/index.vue'

const userStore = useUserStore()
const { name: userName, avatar: avatar } = storeToRefs(userStore) // 使用 storeToRefs 提取响应式状态

// 用户注销
const doLogout = async () => {
  Modal.confirm({
    title: '确认退出登录',
    content: '您确定要退出登录吗？',
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      await userStore.logOut()
      message.success('退出登录成功')
    },
    onCancel() {
      console.log('取消退出登录')
    },
  })
}

const router = useRouter()

// 当前选中菜单
const current = ref<string[]>([])

// 监听路由变化，更新当前选中菜单
router.afterEach((to) => {
  current.value = [to.path]
})

// 路由跳转事件
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  })
}

// 初始化用户信息
onMounted(async () => {
  if (userStore.token) {
    await userStore.getInfo() // 确保用户信息已加载
  }
})

const items = ref<MenuProps['items']>([
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/picture',
    label: '图库',
    title: '图库',
    icon: () => h(PictureOutlined),
    children: [
      {
        key: '/picture/space',
        icon: () =>
          h(SvgIcon, {
            name: 'space', // 对应 space.svg 文件名
            className: 'menu-icon', // 可选自定义类名
          }),
        label: '我的空间',
        title: '我的空间',
      },
      {
        key: '/picture/upload',
        icon: () => h(PictureOutlined),
        label: '上传图片',
        title: '上传图片',
      },
    ],
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
