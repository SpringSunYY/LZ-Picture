<template>
  <div class="ai-recommend">
    <a-space wrap>
      <h1 class="text-gray-200">推荐</h1>
      <a
        v-for="item in recommendList"
        :key="item.id"
        @click="handleClickItem(item)"
        class="content"
        >{{ item.name }}</a
      >
      <refresh-button style="margin-left: 10px" :loading="loading" @click="handleClick" />
    </a-space>
  </div>
</template>
<script lang="ts" setup>
import RefreshButton from '@/components/button/RefreshButton.vue'
import { ref } from 'vue'

interface recommend {
  id: number
  name: string
  prompt: string
}

const recommendList = ref<recommend[]>([
  {
    id: 1,
    name: '个人头像',
    prompt:
      '一个named的named头像，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是',
  },
  {
    id: 1,
    name: '个人头像',
    prompt:
      '一个named的named头像，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是',
  },
  {
    id: 1,
    name: '个人头像',
    prompt:
      '一个named的named头像，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是',
  },
  {
    id: 1,
    name: '个人头像',
    prompt:
      '一个named的named头像，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是',
  },
  {
    id: 1,
    name: '个人头像',
    prompt:
      '一个named的named头像，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是中文名，named是',
  },
])

const emit = defineEmits(['update:modelValue'])
const handleClickItem = (item: recommend) => {
  console.log(item)
  emit('update:modelValue', item.prompt)
}

const loading = ref(false)
const handleClick = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 2000)
}
</script>
<style lang="scss" scoped>
.ai-recommend {
  .content {
    font-size: 14px;
    padding: 6px 6px;
    background-color: rgba(106, 106, 106, 0.6);
    border-radius: 2px;
  }
}
</style>
