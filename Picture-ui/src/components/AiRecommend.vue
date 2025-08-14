<template>
  <div class="ai-recommend">
    <a-space wrap>
      <h1 class="text-blue-600 text-base">推荐</h1>
      <a v-for="item in promptList" :key="item.promptId" @click="handleClickItem(item)" class="content">{{
        item.name
      }}</a>
      <refresh-button style="margin-left: 10px" tip="换一批" :loading="loading" @click="handleClick" />
    </a-space>
  </div>
</template>
<script lang="ts" setup>
import RefreshButton from '@/components/button/RefreshButton.vue'
import { onMounted, ref } from 'vue'
import type { PromptInfoRequest, PromptInfoVo } from '@/types/ai/prompt'
import { listPrompt } from '@/api/ai/prompt.ts'

const promptList = ref<PromptInfoVo[]>([])
const query = ref<PromptInfoRequest>({
  pageNum: 1,
  pageSize: 5,
})

const getPromptList = async () => {
  try {
    loading.value = true
    const res = await listPrompt(query.value)
    if (res.code === 200) {
      promptList.value = res?.rows || []
      if (res?.rows?.length < query.value.pageSize) {
        query.value.pageNum = 0
      }
    }
  } finally {
    loading.value = false
  }
}

const emit = defineEmits(['update:modelValue'])
const handleClickItem = (item: PromptInfoVo) => {
  emit('update:modelValue', item.content)
}

const loading = ref(false)
const handleClick = () => {
  query.value.pageNum++
  getPromptList()
}
onMounted(() => {
  getPromptList()
})
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
