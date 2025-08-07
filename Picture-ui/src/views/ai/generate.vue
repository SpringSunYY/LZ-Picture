<template>
  <div class="ai-generate flex flex-col md:flex-row w-full min-h-screen">
    <div class="left w-full md:w-1/3 p-4">
      <a-tabs v-model:activeKey="activeTab" class="tab-container" size="large" centered>
        <a-tab-pane key="1" tab="文生图">
          <h1 class="text-xl text-blue-4 font-bold">创意描述</h1>
          <a-textarea
            placeholder="请输入内容"
            :border="false"
            :auto-size="{ minRows: 5, maxRows: 10 }"
            :maxlength="512"
            :allowClear="true"
            show-count
            class="text-area mt-1"
          />
          <div class="tag-container mt-4">
            <a-space wrap>
              <h1 class="text-xl font-600">推荐</h1>
              <a-tag color="success">success</a-tag>
              <a-tag color="processing">processing</a-tag>
              <a-tag color="error">error</a-tag>
              <a-tag color="warning">warning</a-tag>
              <a-tag color="default">default</a-tag>
            </a-space>
          </div>
        </a-tab-pane>
        <a-tab-pane key="2" tab="参考生图"> </a-tab-pane>
        <a-tab-pane key="3" tab="风格转换">Content of Tab Pane 3</a-tab-pane>
      </a-tabs>
      <div class="model mt-4">
        <AiCheckModel />
      </div>
      <div class="generate">
        <a-tooltip placement="top">
          <template #title>
            <span>一共需要消耗积分</span>
          </template>
          <generate-button style="width: 60%" />
        </a-tooltip>
      </div>
    </div>
    <div class="right w-full md:w-2/3 p-4"></div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import AiCheckModel from '@/components/AiCheckModel.vue'
import GenerateButton from '@/components/button/GenerateButton.vue'

const activeTab = ref('1')
</script>

<style scoped lang="scss">
$bg-left-color: #2c2c2c;
$bg-left-border-color: rgba(218, 218, 218, 0.71);
$bg-right-color: #000000;
$text-color: #ffffff;

.ai-generate {
  min-height: 100vh;
}

.left {
  background-color: $bg-left-color;
  min-height: 100vh;
  display: flex;
  flex-direction: column;

  .tab-container {
    color: $text-color;

    .ant-tabs-tab {
      font-size: 32px;
    }
  }

  .generate {
    //底部居中
    margin-top: auto;
    display: flex;
    justify-content: center;
    padding: 20px 0;
  }

  /* 修改 textarea 背景色和文字色 */
  ::v-deep(.text-area .ant-input) {
    background-color: $bg-left-color;
    font-size: 1.5em;
    color: $text-color;
    border-color: $bg-left-border-color;
  }

  ::v-deep(.text-area .ant-input::placeholder) {
    color: #aaa;
    opacity: 1;
  }

  ::v-deep(.ant-input-textarea-show-count::after) {
    color: $text-color;
  }

  :deep(.ant-tabs-tab-active) {
    color: #ffffff;
    font-size: 1.5em;
  }

  .tab-container {
    width: 100%;
  }
}

.right {
  background-color: $bg-right-color;
  min-height: 100vh;
}
</style>
