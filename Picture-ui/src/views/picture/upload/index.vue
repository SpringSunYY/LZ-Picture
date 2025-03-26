<template></template>
<script setup lang="ts" name="PictureUpload">
import useUserStore from '@/stores/modules/user.ts'
import { ref } from 'vue'
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
</script>
<style scoped></style>
