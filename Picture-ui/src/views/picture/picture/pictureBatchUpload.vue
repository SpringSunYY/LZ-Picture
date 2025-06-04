<template>
  <div class="picture-batch-upload">
    <PictureBatchUpload @upload-success="handleUploadSuccess" />
  </div>
</template>
<script setup lang="ts" name="pictureBatchUpload">
import PictureBatchUpload from '@/components/PictureBatchUpload.vue'
import type { PictureFileResponse } from '@/types/file'

const handleUploadSuccess = (data:PictureFileResponse) => {
  console.log(data)
}
</script>
<style scoped lang="scss">
.picture-batch-upload {
  width: 100%;
  margin: 0 auto;
}
</style>
