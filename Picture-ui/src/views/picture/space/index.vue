<template>
  <div class="picture-space">
    <!-- 空状态 -->
    <div v-if="spaces.length === 0" class="empty-state">
      <div class="add-card" style="width: 25%; margin: 0 auto" @click="handleCreate">
        <div class="add-content">
          <plus-outlined class="add-icon" />
          <div class="add-text">新建空间</div>
        </div>
      </div>
    </div>

    <!-- 空间网格列表 -->
    <a-row v-else :gutter="[24, 24]">
      <a-col v-for="space in spaces" :key="space.space_id" :xs="24" :sm="12" :md="8" :lg="6">
        <div class="space-card" @click="goDetail(space.space_id)">
          <div class="cover-image" :style="coverStyle(space)"></div>
          <div class="space-info">
            <h3 class="title">{{ space.space_name }}</h3>
            <div class="meta">
              <span>{{ formatSize(space.total_size) }}</span>
              <a-divider type="vertical" />
              <span>{{ space.total_count }}个文件</span>
            </div>
          </div>
        </div>
      </a-col>
      <a-col :xs="24" :sm="12" :md="8" :lg="6">
        <div class="add-card" @click="handleCreate">
          <div class="add-content">
            <plus-outlined class="add-icon" />
            <div class="add-text">新建空间</div>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts" name="PictureSpace">
import { PlusOutlined } from '@ant-design/icons-vue'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

// 假数据生成
const mockSpaces = Array.from({ length: 0 }, (_, i) => ({
  space_id: `space_${i + 1}`,
  space_name: `项目空间 ${i + 1}`,
  space_avatar: i % 2 ? 'https://picsum.photos/300/200' : null,
  total_size: Math.random() * 1073741824,
  total_count: Math.floor(Math.random() * 500),
  space_type: i % 3,
  current_members: i % 2 ? 3 : 0,
}))

const spaces = ref([...mockSpaces])
console.log(spaces.value)
// 封面样式处理
const coverStyle = (space) => ({
  backgroundImage: `url(${space.space_avatar || '/default-space-cover.jpg'})`,
})

// 容量格式化
const formatSize = (bytes) => {
  const units = ['B', 'KB', 'MB', 'GB']
  let size = bytes
  let unitIndex = 0
  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024
    unitIndex++
  }
  return `${size.toFixed(1)}${units[unitIndex]}`
}

// 路由跳转
const router = useRouter()
const goDetail = (id) => router.push(`/space/${id}`)
const handleCreate = () => router.push('/space/create')
</script>

<style lang="scss" scoped>
.picture-space {
  padding: 24px;
  max-width: 1440px;
  margin: 0 auto;

  .empty-state {
    display: flex;
    justify-content: center; // 水平居中
    align-items: center; // 垂直居中
    height: 70vh; // 占据整个视口高度
    width: 100%; // 占据整个宽度
    :deep(.ant-empty-image) {
      height: 200px;
    }

    .create-tip {
      color: rgba(0, 0, 0, 0.45);
      margin-top: 12px;
    }
  }

  .add-card {
    border: 2px dashed #d9d9d9;
    height: 224px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 8px;

    &:hover {
      border-color: #1890ff;
    }

    .add-content {
      text-align: center;

      .add-icon {
        font-size: 32px;
        color: #1890ff;
      }

      .add-text {
        margin-top: 8px;
        color: rgba(0, 0, 0, 0.85);
      }
    }
  }

  .space-card {
    position: relative;
    border-radius: 8px;
    overflow: hidden;
    transition: all 0.3s;
    cursor: pointer;
    background: #fff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    .cover-image {
      height: 160px;
      background-size: cover;
      background-position: center;
    }

    .space-info {
      padding: 16px;

      .title {
        margin: 0;
        font-size: 16px;
      }

      .meta {
        color: rgba(0, 0, 0, 0.45);
        font-size: 12px;
      }
    }
  }
}
</style>
