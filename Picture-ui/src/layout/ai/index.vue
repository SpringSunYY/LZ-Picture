<template>
  <div class="aiLayout">
    <a-layout has-sider>
      <a-layout-sider :collapsed="true" class="navigate">
        <img class="logo" src="../../assets/logo.svg" alt="logo" />
        <div class="menu">
          <a-menu
            mode="inline"
            v-model:selectedKeys="current"
            :items="items"
            @click="doMenuClick"
          />
        </div>
        <div class="user">
          <div class="user-info" v-if="userName">
            <a-dropdown>
              <div class="user-avatar">
                <a-avatar
                  :size="40"
                  :src="avatar != '' ? formatDnsUrl(avatar) : '../assets/images/avatar.jpg'"
                />
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
            <div style="margin-top: 10px"></div>
            <!--            <div @click="clickInform">-->
            <!--              <SvgIcon name="inform" size="1.5em" />-->
            <!--            </div>-->
            <!--            <div @click="clickPoints">-->
            <!--              <SvgIcon name="pointsBlue" size="1.2em" />-->
            <!--            </div>-->
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-layout-sider>
      <a-layout :style="{ marginLeft: '70px' }">
        <a-layout-content :style="{ overflow: 'initial' }">
          <router-view v-slot="{ Component, route }">
            <keep-alive v-if="route?.meta?.isCache === true">
              <component :is="Component" :key="route.name" />
            </keep-alive>
            <component v-else :is="Component" :key="route.name" />
          </router-view>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </div>
</template>
<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue'
import useUserStore from '@/stores/modules/user.ts'
import { storeToRefs } from 'pinia'
import { type RouteRecordRaw, useRouter } from 'vue-router'
import usePermissionStore from '@/stores/modules/permission.ts'
import { generateMenu, toMenu } from '@/router/permisson.ts'
import { formatDnsUrl } from '@/utils/common.ts'
import { LogoutOutlined } from '@ant-design/icons-vue'
import { message, Modal } from 'ant-design-vue'

const userStore = useUserStore()
const { userName: userName, avatar: avatar, nickName: nickName } = storeToRefs(userStore) // 使用 storeToRefs 提取响应式状态
const router = useRouter()

//region 菜单
// 当前选中菜单
const current = ref<string[]>([])

const doMenuClick = (route: RouteRecordRaw) => {
  if (isMobile.value) {
    menuVisible.value = false
  }
  toMenu(route)
}
onMounted(() => {
  if (router.currentRoute.value.name === 'toAi') {
    router.push({ name: 'aiDiscover' })
    return
  }
  if (userStore.token) {
    userName.value = userStore.userName
    avatar.value = userStore.avatar
  }
})
// 监听路由变化，更新当前选中菜单
router.afterEach((to) => {
  current.value = [to.name as string]
})
// 动态生成菜单项
const items = computed(() => {
  // 生成有效路由结构
  return [...(generateMenu(permissionStore?.getRoutes(), '4') || [])]
})

const permissionStore = usePermissionStore()

const isMobile = ref(false)
const menuVisible = ref(false)
//endregion

//region 用户
// 消息
const clickInform = () => {
  router.push({
    name: 'inform',
  })
}
//积分
const clickPoints = () => {
  router.push({
    name: 'Points',
  })
}
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
//endregion
</script>
<style scoped lang="scss">
.navigate {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  width: 70px;
  background-color: white;
  display: flex;
  flex-direction: column;
  align-items: center; // 水平居中用
  justify-content: space-between;
  overflow: hidden; // 禁止溢出，确保子项不乱跑
  .logo {
    width: 100%;
    height: 100px;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 12px 0;
  }

  .menu {
    position: absolute;
    top: 40%;
    left: 0;
    transform: translateY(-50%);
    width: 100%;
    display: flex;
    justify-content: center;
    pointer-events: auto;
  }

  .user {
    position: absolute;
    bottom: 12px;
    left: 0;
    width: 100%;
    display: flex;
    justify-content: center;

    .user-info {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px; // 图标和头像间距可调
    }

    .user-avatar {
      cursor: pointer;
    }
  }
}
</style>
