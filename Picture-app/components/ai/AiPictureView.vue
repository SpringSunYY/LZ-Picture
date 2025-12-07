<template>
  <view class="root-container">
    <view class="image-preview">
      <!-- H5 平台使用原生 img -->
      <!-- #ifdef H5 -->
      <img
          :src="imageUrl"
          alt="图片"
          class="preview-image"
          @click.stop="openFullscreen(imageUrl)"
          @error="handleImageError"
          @load="handleImageLoad"
      />
      <!-- #endif -->
      <!-- 非 H5 平台使用 uni-app image -->
      <!-- #ifndef H5 -->
      <image
          :src="imageUrl"
          alt="图片"
          mode="aspectFit"
          class="preview-image"
          @tap.stop="openFullscreen(imageUrl)"
          @error="handleImageError"
          @load="handleImageLoad"
      />
      <!-- #endif -->
    </view>
    <!-- 全屏预览 -->
    <view v-if="fullscreenImage" class="fullscreen-modal" @tap="closeFullscreen">
      <view class="modal-content" @tap="closeFullscreen">
        <view class="image-wrapper">
          <!-- H5 平台使用原生 img -->
          <!-- #ifdef H5 -->
          <img
              :src="fullscreenImage"
              class="fullscreen-image"
              alt="加载中"
              @click="handleImageClick"
              @contextmenu.prevent="handleLongPress"
              @error="handleImageError"
              @load="handleImageLoad"
          />
          <!-- #endif -->
          <!-- 非 H5 平台使用 uni-app image -->
          <!-- #ifndef H5 -->
          <image
              :src="fullscreenImage"
              class="fullscreen-image"
              alt="加载中"
              mode="aspectFit"
              @tap="handleImageClick"
              @longpress="handleLongPress"
              @error="handleImageError"
              @load="handleImageLoad"
          />
          <!-- #endif -->
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import {ref, watch} from 'vue'

const props = defineProps({
  imageUrl: {
    default: '',
    type: String,
  },
})

// 调试：监听 imageUrl 变化
watch(() => props.imageUrl, (newVal) => {
}, {immediate: true})

const fullscreenImage = ref(null)
const imageLoaded = ref(false)

const openFullscreen = (imageSrc) => {
  // console.log('打开全屏预览，图片路径:', imageSrc)
  // 确保图片路径有效
  if (!imageSrc) {
    console.warn('图片路径为空')
    return
  }

  // 直接设置图片路径
  fullscreenImage.value = imageSrc
  // console.log('设置全屏图片:', fullscreenImage.value)
  // 重置加载状态
  imageLoaded.value = false
}

const closeFullscreen = (e) => {
  // console.log('尝试关闭全屏预览', e)
  fullscreenImage.value = null
  imageLoaded.value = false
}

// 处理图片点击事件（点击图片也关闭）
const handleImageClick = (e) => {
  e.stopPropagation()
  closeFullscreen()
}

// 处理图片加载错误
const handleImageError = (e) => {
/*  console.error('图片加载失败:', e)
  console.error('图片URL:', props.imageUrl)
  console.error('URL类型:', typeof props.imageUrl)
  console.error('URL是否为空:', !props.imageUrl)*/
  uni.showToast({
    title: '图片加载失败',
    icon: 'none',
  })
}

// 处理图片加载成功
const handleImageLoad = (e) => {
  // console.log('图片加载成功:', props.imageUrl)
  // 标记图片已加载
  imageLoaded.value = true
}

// 将 base64 转换为临时文件路径（小程序平台）
const convertBase64ToTempFile = (base64) => {
  return new Promise((resolve, reject) => {
    // #ifdef MP-WEIXIN
    try {
      const fs = uni.getFileSystemManager()
      const base64Data = base64.split(',')[1] || base64
      const fileName = `temp_${Date.now()}.jpg`
      // 使用微信小程序的临时文件目录
      const filePath = `${wx.env.USER_DATA_PATH}/${fileName}`

      fs.writeFile({
        filePath: filePath,
        data: base64Data,
        encoding: 'base64',
        success: () => {
          // console.log('base64 转换为临时文件成功:', filePath)
          resolve(filePath)
        },
        fail: (err) => {
          // console.error('base64 转换为临时文件失败:', err)
          // 如果失败，尝试使用临时目录
          const tempPath = `wxfile://tmp_${Date.now()}.jpg`
          fs.writeFile({
            filePath: tempPath,
            data: base64Data,
            encoding: 'base64',
            success: () => resolve(tempPath),
            fail: () => reject(err),
          })
        },
      })
    } catch (error) {
      console.error('base64 转换异常:', error)
      reject(error)
    }
    // #endif

    // #ifndef MP-WEIXIN
    // 非微信小程序平台，尝试使用通用方式
    try {
      const fs = uni.getFileSystemManager()
      const base64Data = base64.split(',')[1] || base64
      const fileName = `temp_${Date.now()}.jpg`
      // 使用临时目录
      const filePath = `${uni.env.USER_DATA_PATH || ''}/${fileName}`

      if (filePath) {
        fs.writeFile({
          filePath: filePath,
          data: base64Data,
          encoding: 'base64',
          success: () => resolve(filePath),
          fail: reject,
        })
      } else {
        reject(new Error('无法获取临时目录'))
      }
    } catch (error) {
      reject(error)
    }
    // #endif
  })
}

// 下载网络图片到本地（小程序平台）
const downloadImage = (url) => {
  return new Promise((resolve, reject) => {
    // #ifdef MP-WEIXIN || MP-ALIPAY || MP-BAIDU || MP-TOUTIAO
    uni.downloadFile({
      url: url,
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.tempFilePath)
        } else {
          reject(new Error('下载失败'))
        }
      },
      fail: reject,
    })
    // #endif

    // #ifndef MP-WEIXIN && MP-ALIPAY && MP-BAIDU && MP-TOUTIAO
    resolve(url)
    // #endif
  })
}

// 保存图片到相册
const saveImageToAlbum = async (imagePath) => {
  try {
    let filePath = imagePath

    // #ifdef MP-WEIXIN || MP-ALIPAY || MP-BAIDU || MP-TOUTIAO
    // 小程序平台：如果是 base64，先转换为临时文件
    if (imagePath.startsWith('data:image')) {
      filePath = await convertBase64ToTempFile(imagePath)
    } else if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
      // 如果是网络图片，先下载到本地
      filePath = await downloadImage(imagePath)
    }
    // #endif

    // #ifdef H5
    // H5 平台：如果是 base64，创建下载链接
    if (imagePath.startsWith('data:image')) {
      const link = document.createElement('a')
      link.href = imagePath
      link.download = `image_${Date.now()}.jpg`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      uni.showToast({
        title: '保存成功',
        icon: 'success',
      })
      return
    }
    // #endif

    // 保存图片到相册（小程序和 APP 平台）
    // #ifndef H5
    uni.saveImageToPhotosAlbum({
      filePath: filePath,
      success: () => {
        uni.showToast({
          title: '保存成功',
          icon: 'success',
        })
      },
      fail: (err) => {
        console.error('保存图片失败:', err)
        uni.showToast({
          title: err.errMsg || '保存失败',
          icon: 'none',
        })
      },
    })
    // #endif
  } catch (error) {
    // console.error('保存图片异常:', error)
    uni.showToast({
      title: error.message || '保存失败',
      icon: 'none',
    })
  }
}

// 处理长按事件（长按不关闭，显示操作菜单）
const handleLongPress = (e) => {
  e.stopPropagation()
  // 可以添加保存图片等功能
  uni.showActionSheet({
    itemList: ['保存图片'],
    success: (res) => {
      if (res.tapIndex === 0 && fullscreenImage.value) {
        saveImageToAlbum(fullscreenImage.value)
      }
    },
  })
}
</script>

<style scoped lang="scss">
$color-bg: #18181b;
$color-bg-primary: #18181b;
$color-bg-secondary: #27272a;
$color-bg-tertiary: #3f3f46;
$color-text-primary: #f4f4f5;
$color-text-secondary: #a1a1aa;
$color-border: #3f3f46;
$color-accent: #6366f1;
$color-accent-hover: #7c7ff4;
$color-hover: #2e2e32;
$color-shadow: rgba(0, 0, 0, 0.4);


image {
  will-change: transform;
}

.root-container {
  width: 100%;
  height: 100%;
  position: relative;
  /* #ifdef MP-WEIXIN || MP-ALIPAY || MP-BAIDU || MP-TOUTIAO */
  // 小程序平台：确保能继承父容器高度
  min-height: 0;
  /* #endif */
}

.image-preview {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  cursor: pointer;
  overflow: hidden;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;

  .preview-image {
    width: 100%;
    height: 100%;
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
    border-radius: 2%;
    transition: all 0.15s ease-in-out;
    opacity: 1 !important;
    visibility: visible !important;
    background-color: transparent;
    /* #ifndef H5 */
    // 小程序平台确保图片填满容器
    display: block;
    /* #endif */
    /* #ifdef MP-WEIXIN || MP-ALIPAY || MP-BAIDU || MP-TOUTIAO */
    // 小程序平台：确保图片有最小尺寸
    min-width: 200rpx;
    min-height: 200rpx;
    /* #endif */
  }
}

.fullscreen-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  background-color: $color-bg;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  /* #ifdef H5 */
  backdrop-filter: blur(16rpx);
  /* #endif */

  .modal-content {
    position: relative;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: visible;
    pointer-events: auto;
  }

  .image-wrapper {
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    z-index: 10;
  }

  .fullscreen-image {
    width: 100%;
    height: 100%;
    max-width: 100vw;
    max-height: 100vh;
    /* #ifdef H5 */
    box-shadow: 0 16rpx 64rpx rgba(0, 0, 0, 0.9);
    cursor: pointer;
    /* #endif */
    display: block !important;
    object-fit: contain;
    position: relative;
    z-index: 1;
    opacity: 1 !important;
    visibility: visible !important;
    background-color: transparent;
  }
}
</style>

