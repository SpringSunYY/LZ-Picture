import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { addUserBehaviorInfo } from '@/api/picture/userBehaviorInfo'

export function useUserBehavior(picture: any) {
  const openShare = ref(false)
  const shareLink = ref('')

  const addUserBehavior = (behaviorType: string) => {
    const targetType = '0'
    let msg = '操作成功'
    if (behaviorType === '2') {
      shareLink.value = window.location.href
    }

    addUserBehaviorInfo({
      behaviorType: behaviorType,
      targetType: targetType,
      targetId: picture.value.pictureId,
      shareLink: shareLink.value,
    }).then((res: any) => {
      if (res.code === 200 && res.data != undefined && res.data) {
        switch (behaviorType) {
          case '0':
            msg = '点赞成功'
            picture.value.likeCount = Number(picture.value?.likeCount || 0) + 1
            picture.value.isLike = !picture.value.isLike
            break
          case '1':
            msg = '收藏成功'
            picture.value.collectCount = Number(picture.value?.collectCount || 0) + 1
            picture.value.isCollect = !picture.value.isCollect
            break
          case '2':
            msg = '分享成功'
            picture.value.shareCount = Number(picture.value?.shareCount || 0) + 1
            handleShare()
            break
        }
      } else {
        switch (behaviorType) {
          case '0':
            msg = '取消点赞成功'
            picture.value.likeCount = Number(picture.value?.likeCount || 0) - 1
            picture.value.isLike = !picture.value.isLike
            break
          case '1':
            msg = '取消收藏成功'
            picture.value.collectCount = Number(picture.value?.collectCount || 0) - 1
            picture.value.isCollect = !picture.value.isCollect
            break
          case '2':
            msg = '分享成功'
            handleShare()
            break
        }
      }
      message.success(msg)
    })
  }

  const handleShare = () => {
    openShare.value = true
  }

  return {
    openShare,
    shareLink,
    addUserBehavior,
    handleShare,
  }
}
