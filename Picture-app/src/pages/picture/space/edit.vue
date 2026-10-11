<template>
  <view class="space-edit-page" :style="rootStyleVar">
    <!-- 背景装饰光斑 —— 与 space.vue / system.vue / password.vue 同一套语言 -->
    <view class="bg-blur bg-blur--primary"/>
    <view class="bg-blur bg-blur--secondary"/>

    <main class="se-main">
      <!-- ============================================================
           1. 顶部引导头 —— 图标 + 标题 + 说明
           ============================================================ -->
      <view class="se-header">
        <view class="se-header-icon">
          <SvgIcon :icon="isEdit ? 'fork' : 'release'" width="1.6em" class="se-header-icon-svg"/>
        </view>
        <text class="se-header-title">{{ isEdit ? '修改工作空间' : '创建工作空间' }}</text>
        <text class="se-header-sub">
          {{ isEdit ? '可调整名称、封面与描述;空间状态与类型创建后不可修改.' : '给空间起个好名字,方便以后按用途检索.' }}
        </text>
      </view>

      <!-- ============================================================
           2. 表单卡片
           ============================================================ -->
      <view class="form-card">
        <up-form
            :model="formModel"
            :rules="formRules"
            errorType="message"
            labelPosition="top"
            ref="spaceFormRef"
        >
          <!-- 空间名称 -->
          <up-form-item prop="spaceName" label="空间名称" borderBottom="false">
            <up-input
                v-model="formModel.spaceName"
                shape="circle"
                type="text"
                :maxlength="32"
                placeholder="请输入空间名称"
                clearable
            >
              <template #prefix>
                <SvgIcon icon="space" width="1.2em"/>
              </template>
            </up-input>
          </up-form-item>

          <!-- 空间封面 —— CoverUpload:通过 :uploader 注入 coverUploadFile
               (api/common/file.ts → /picture/file/upload/cover),
               返回的 url 字段自动写回 formModel.spaceAvatar -->
          <up-form-item prop="spaceAvatar" label="空间封面" borderBottom="false">
            <view class="se-form-cover">
              <!--
                CoverUpload 内部已自带编辑器(H5=cropperjs,MP=wx.cropImage),
                不需要在父组件注入 #editor slot —— 那样会被 uni-app slot 渲染时序坑
              -->
              <CoverUpload
                  v-model="formModel.spaceAvatar"
                  empty-text="点击上传空间封面"
                  :uploader="onUploadCover"
              />
            </view>
          </up-form-item>

          <!-- 空间状态 -->
          <up-form-item prop="spaceStatus" label="空间状态" borderBottom="false">
            <up-radio-group
                v-model="formModel.spaceStatus"
                placement="row"
                shape="square"
                :active-color="primaryColor"
                :disabled="isEdit"
                gap="40rpx"
            >
              <up-radio
                  v-for="opt in statusOptions"
                  :key="opt.value"
                  :name="opt.value"
                  :label="opt.label"
              />
            </up-radio-group>
          </up-form-item>

          <!-- 空间类型 -->
          <up-form-item prop="spaceType" label="空间类型" borderBottom="false">
            <up-radio-group
                v-model="formModel.spaceType"
                placement="row"
                shape="square"
                :active-color="primaryColor"
                :disabled="isEdit"
                gap="40rpx"
            >
              <up-radio
                  v-for="opt in typeOptions"
                  :key="opt.value"
                  :name="opt.value"
                  :label="opt.label"
              />
            </up-radio-group>
          </up-form-item>

          <!-- 空间描述 -->
          <up-form-item prop="spaceDesc" label="空间描述" borderBottom="false">
            <up-textarea
                v-model="formModel.spaceDesc"
                :maxlength="512"
                :count="true"
                :height="140"
                placeholder="简单描述这个空间的用途,方便以后检索"
                border="none"
            />
          </up-form-item>
        </up-form>

        <TipCard text="个人公共/私有空间每种状态最多创建 10 个且不可转换状态,团队空间最多创建 10 个."/>
      </view>

      <!-- ============================================================
           3. 提交按钮 —— 固定底部,跟随 safe-area
           ============================================================ -->
      <view
          class="primary-btn"
          :class="{ 'primary-btn--disabled': submitting }"
          @tap="onSubmit"
      >
        <text class="primary-btn-text">{{ submitting ? '提交中...' : (isEdit ? '保存修改' : '创建空间') }}</text>
      </view>
    </main>
  </view>
</template>

<script setup>
import {computed, reactive, ref} from 'vue'
import {onLoad} from '@dcloudio/uni-app'
import {useStore} from 'vuex'
import SvgIcon from '@/components/SvgIcon.vue'
import TipCard from '@/components/TipCard.vue'
import CoverUpload from '@/components/common/CoverUpload.vue'
import {addSpaceInfo, getSpaceInfo, updateSpaceInfo} from '@/api/picture/space'
import {toast, toastHide, toastLoading, toastWarn} from '@/utils/toast'
import {useDict} from '@/utils/useDict'
import {themePresets} from '@/constants/theme'

defineOptions({
  name: 'PictureSpaceEdit',
})

/**
 * 顶部 tab 标题 —— 实际文案在 onLoad 里按是否有 spaceId 改成「创建空间 / 修改空间」
 */
definePage({
  style: {
    navigationBarTitleText: '创建空间',
  },
})

const store = useStore()

/** 当前主题主色 —— 与 AppTabbar.vue / space.vue 同款取法,给 up-radio 的激活色用 */
const primaryColor = computed(() => {
  const key = store.state.preferences?.theme?.key
  return themePresets[key]?.primary || '#4f46e5'
})

// ===================== 空间类型 / 状态字典 =====================
// 与 Web 端 useDict('p_space_status', 'p_space_type') 同源,
// 字典未加载时退回静态文案,避免出现空选项。
const {p_space_status, p_space_type} = useDict('p_space_status', 'p_space_type')

const FALLBACK_STATUS = [
  {value: '0', label: '公共'},
  {value: '1', label: '私有'},
]
const FALLBACK_TYPE = [
  {value: '0', label: '官方'},
  {value: '1', label: '团队'},
  {value: '2', label: '个人'},
]

const statusOptions = computed(() =>
    p_space_status.value && p_space_status.value.length
        ? p_space_status.value.map((d) => ({value: d.dictValue, label: d.dictLabel}))
        : FALLBACK_STATUS,
)

const typeOptions = computed(() =>
    p_space_type.value && p_space_type.value.length
        ? p_space_type.value.map((d) => ({value: d.dictValue, label: d.dictLabel}))
        : FALLBACK_TYPE,
)

// ===================== 表单状态 =====================
/** URL 上带的 spaceId —— 为空是新建,不为空是修改 */
const spaceId = ref('')
const isEdit = computed(() => !!spaceId.value)

const submitting = ref(false)
const spaceFormRef = ref(null)

const DEFAULT_FORM = {
  spaceId: '',
  spaceName: '',
  spaceAvatar: '',
  spaceDesc: '',
  spaceStatus: '0',
  spaceType: '0',
}
const formModel = reactive({...DEFAULT_FORM})

/** 表单校验 —— 与 Web 端 rules 保持同一套约束 */
const formRules = {
  spaceName: [
    {
      validator: (rule, value) => {
        const v = String(value ?? '').trim()
        if (!v) return Promise.reject(new Error('空间名称不能为空'))
        if (v.length < 2 || v.length > 32) return Promise.reject(new Error('名称长度需在 2-32 字符之间'))
        return Promise.resolve()
      },
      trigger: ['blur', 'change'],
    },
  ],
  spaceDesc: [
    {
      validator: (rule, value) => {
        if (String(value ?? '').length > 512) return Promise.reject(new Error('描述不能超过 512 字符'))
        return Promise.resolve()
      },
      trigger: ['blur', 'change'],
    },
  ],
}

// ===================== 页面初始化 =====================
onLoad(async (options) => {
  const id = options?.spaceId || ''
  uni.setNavigationBarTitle({title: id ? '修改空间' : '创建空间'})

  // 新建:默认值就是 DEFAULT_FORM,无需再拉详情
  if (!id) return

  spaceId.value = id
  formModel.spaceId = id
  try {
    const res = await getSpaceInfo(id)
    if (res?.data) Object.assign(formModel, res.data)
  } catch (e) {
    // request.ts 已统一 toast,这里兜一句让用户知道是编辑详情没拉到
    toastWarn('获取空间信息失败,请稍后重试')
  }
})

// ===================== 封面上传(注入给 CoverUpload) =====================
/**
 * 适配 CoverUpload 的 uploader 协议
 * 接收:
 *   - H5  : { blob: Blob, name: string, type: string }  裁剪后输出
 *   - MP  : { tempPath: string, name: string }         wx.cropImage 输出
 *
 * 必须走 uni.uploadFile(走 multipart/form-data),
 * 因为 coverUploadFile/api/common/file.ts 内部用的是 request.ts 的 uni.request + JSON,
 * 后端 /picture/file/upload/cover 大概率不接受 JSON 传 file
 */
/**
 * H5 端: cover upload 走 picture 域(不是 user 域)
 * 小程序/App: 走完整域名,加 /picture 路径
 */
const getUploadBaseUrl = () => {
    // #ifdef H5
    // H5 dev 由 vite proxy 把 /api/picture/* 反代到 https://www.springsun.online/prod-api/picture/*
    // H5 生产由 nginx / 部署服务器反代
    return '/api/picture'
    // #endif
    // #ifndef H5
    const envBase = import.meta.env.VITE_APP_BASE_API || ''
    return envBase ? envBase.replace(/\/user\/?$/, '') + '/picture' : ''
    // #endif
}

const onUploadCover = (payload) => {
    return new Promise((resolve, reject) => {
        const token = uni.getStorageSync('App-Token') || ''
        const formData = { type: '2', fileDir: 'cover' }
        const name = payload?.name || 'cover.jpg'

        // #ifdef H5
        // H5 端:直接用 fetch + FormData,绕开 uni.uploadFile 在 H5 端的多坑
        // (uni.uploadFile 在 H5 走 XHR,对 Blob/dataURL 编码不一致,经常导致后端拿不到文件)
        const blob = payload?.blob
        if (!blob) {
            reject(new Error('H5 上传需要 blob'))
            return
        }
        const fd = new FormData()
        // 关键: 第三个参数给文件名,后端 multipart 解析时才能拿到正确的 filename
        fd.append('file', blob, name)
        Object.entries(formData).forEach(([k, v]) => fd.append(k, v))

        const uploadUrl = getUploadBaseUrl() + '/picture/file/upload/cover'
        const headers = token ? { Authorization: 'UserBearer ' + token } : {}
        console.log('[edit:H5] upload start', {
            url: uploadUrl,
            hasToken: !!token,
            tokenPrefix: token ? token.slice(0, 10) + '...' : '(none)',
            blobSize: blob.size,
            blobType: blob.type,
            formData,
        })
        // 不要手动设 Content-Type,让浏览器自动加 boundary
        fetch(uploadUrl, {
            method: 'POST',
            headers,
            body: fd,
        })
            .then(async (res) => {
                const text = await res.text()
                console.log('[edit:H5] upload response', {
                    ok: res.ok,
                    status: res.status,
                    textPreview: text.slice(0, 800),
                })
                let body
                try {
                    body = JSON.parse(text)
                } catch (e) {
                    console.error('[edit:H5] response not JSON', e)
                    toast('上传失败:响应不是 JSON')
                    reject(new Error('响应不是 JSON'))
                    return
                }
                if (
                    res.ok &&
                    (body?.code === 200 || body?.code === 0) &&
                    (body?.data?.url || body?.data?.thumbnailUrl)
                ) {
                    console.log('[edit:H5] upload success', body.data)
                    resolve(body.data)
                } else {
                    const msg = body?.msg || `上传失败(code=${body?.code ?? res.status})`
                    console.error('[edit:H5] upload fail', msg, body)
                    toast(msg)
                    reject(new Error(msg))
                }
            })
            .catch((err) => {
                console.error('[edit:H5] fetch error', err)
                const msg = err?.message || '上传失败,请检查网络'
                toast(msg)
                reject(new Error(msg))
            })
        // #endif

        // #ifndef H5
        if (!payload?.tempPath) {
            reject(new Error('上传需要 tempPath'))
            return
        }
        uni.uploadFile({
            url: getUploadBaseUrl() + '/picture/file/upload/cover',
            filePath: payload.tempPath,
            name: 'file',
            header: token ? { Authorization: 'UserBearer ' + token } : {},
            formData,
            success: (res) => handleUploadResponse(res, resolve, reject),
            fail: (err) => {
                const msg = err?.errMsg || '上传失败,请检查网络'
                toast(msg)
                reject(new Error(msg))
            },
        })
        // #endif
    })
}

const handleUploadResponse = async (res, resolve, reject) => {
    console.log('[edit] upload raw response', {
        statusCode: res?.statusCode,
        dataType: typeof res?.data,
        dataPreview: typeof res?.data === 'string' ? res.data.slice(0, 500) : res?.data,
    })
    let body
    try {
        body = typeof res.data === 'string' ? JSON.parse(res.data) : res.data
    } catch (e) {
        console.error('[edit] JSON.parse failed', e)
        toast('上传失败:响应解析错误')
        reject(new Error('响应解析错误'))
        return
    }
    console.log('[edit] upload parsed body', body)
    // 兼容多种后端响应形态:
    // 1) { code, data: { url } }
    // 2) { code, data: { thumbnailUrl } }  (用户给的 PictureFileResponse)
    // 3) { code, data: 'http://...' }       (data 直接是 url 字符串)
    // 4) { code, data: { name: 'xxx.jpg' } } 现实:后端只返 name 没返 url,要根据 name 拼路径
    const data = body?.data
    let url =
        (data && typeof data === 'object' && (data.url || data.thumbnailUrl)) ||
        (typeof data === 'string' ? data : null)

    // 兜底: 后端只返 name 时,按 PictureFileResponse 设计意图拼成完整 url
    // 规则: name 一般是 "hash-random.jpg" 或 "hash-random-compressed.jpg" 形式
    //      -> 拼成 "https://<dnsUrl>/picture/cover/YYYY/MM/DD/<name>"
    if (!url && data && typeof data === 'object' && data.name) {
        const d = new Date()
        const yyyy = d.getFullYear()
        const mm = String(d.getMonth() + 1).padStart(2, '0')
        const dd = String(d.getDate()).padStart(2, '0')
        // 取 config.dnsUrl(避免硬编码域名)
        let base = ''
        try {
            // 动态读 config,避免循环依赖
            const cfg = (await import('@/config')).default
            base = cfg?.dnsUrl || ''
        } catch (e) {
            base = ''
        }
        url = base
            ? `${base}/picture/cover/${yyyy}/${mm}/${dd}/${data.name}`
            : `/picture/cover/${yyyy}/${mm}/${dd}/${data.name}`
        console.warn('[edit] 后端没返 url/thumbnailUrl,根据 name 兜底拼接:', url)
    }

    if ((body?.code === 200 || body?.code === 0) && url) {
        // 统一成 { url } 形态再 resolve,CoverUpload 期望 data.url
        console.log('[edit] upload success, url=', url)
        resolve({ ...(data || {}), url })
    } else {
        const msg = body?.msg || `上传失败(code=${body?.code ?? '?'})`
        console.error('[edit] upload fail', msg, { data, url })
        toast(msg)
        reject(new Error(msg))
    }
}

// ===================== 提交 =====================
const onSubmit = () => {
  if (submitting.value) return
  spaceFormRef.value?.setRules(formRules)
  spaceFormRef.value?.validate()
      .then(() => doSubmit())
      .catch((error) => {
        const first = Array.isArray(error) ? error[0] : error
        toastWarn(first?.message || '请检查输入')
      })
}

const doSubmit = async () => {
  submitting.value = true
  toastLoading(isEdit.value ? '保存中...' : '创建中...')
  try {
    // CoverUpload 删除时 emit 的是空串,这里统一兜成空串,
    // 否则会把 null / "null" 提交给后端,更新时覆盖掉已有封面
    const payload = {
      spaceId: formModel.spaceId,
      spaceName: String(formModel.spaceName ?? '').trim(),
      spaceAvatar: formModel.spaceAvatar ?? '',
      spaceDesc: formModel.spaceDesc ?? '',
      spaceStatus: formModel.spaceStatus,
      spaceType: formModel.spaceType,
    }
    if (isEdit.value) {
      await updateSpaceInfo(payload)
      toastHide()
      toast('空间修改成功')
    } else {
      await addSpaceInfo(payload)
      toastHide()
      toast('空间创建成功')
    }
    // 给 toast 一帧时间渲染再回退;列表页 onShow 会自动重新拉数据
    setTimeout(() => {
      uni.navigateBack({delta: 1})
    }, 600)
  } catch (e) {
    toastHide()
    toastWarn(isEdit.value ? '空间修改失败' : '空间创建失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style lang="scss" scoped>
/* ============================================================
 * 页面 token —— 与 space.vue / system.vue / password.vue 完全同一套
 * 主题色由 rootStyleVar 注入的 --app-* 解析,业务统一用 $app-* 变量
 * ============================================================ */
$surface-lowest: #ffffff;
$surface: #faf8ff;
$surface-container: #e2e7ff;
$surface-container-high: #dae2fd;
$on-surface: #131b2e;
$on-surface-variant: #464555;
$outline: #777587;
$primary-container: #4f46e5;
$se-radius-lg: 32rpx;
$se-radius-md: 24rpx;
$se-radius-xl: 40rpx;

button {
  background-color: transparent;
  padding: 0;
  margin: 0;
  font-size: 28rpx;
  line-height: normal;

  &::after {
    border: none;
  }
}

/* ========== 页面结构 ========== */
.space-edit-page {
  position: relative;
  width: 100%;
  min-height: 100vh;
  background-color: $surface;
  /* 底部留出提交按钮 + safe-area */
  padding: 24rpx 32rpx 180rpx;
  box-sizing: border-box;
  overflow: hidden;
}

.bg-blur {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  z-index: 0;

  &--primary {
    top: -160rpx;
    right: -120rpx;
    width: 480rpx;
    height: 480rpx;
    background-color: rgba($primary-container, 0.18);
    filter: blur(120rpx);
  }

  &--secondary {
    top: 420rpx;
    left: -140rpx;
    width: 380rpx;
    height: 380rpx;
    background-color: rgba($surface-container, 0.7);
    filter: blur(80rpx);
  }
}

/* 内容浮在 bg-blur 之上 */
.se-main,
.se-header,
.form-card {
  position: relative;
  z-index: 1;
}

.se-main {
  display: flex;
  flex-direction: column;
}

/* ========== 引导头 ========== */
.se-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16rpx 0 8rpx;
  text-align: center;
}

.se-header-icon {
  width: 112rpx;
  height: 112rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: $app-primary-light;
}

.se-header-icon-svg {
  color: $app-primary;
  line-height: 1;
}

.se-header-title {
  margin-top: 24rpx;
  font-size: 38rpx;
  font-weight: 700;
  color: $on-surface;
  letter-spacing: -1rpx;
  line-height: 1.2;
}

.se-header-sub {
  margin-top: 10rpx;
  max-width: 560rpx;
  font-size: 22rpx;
  color: $outline;
  letter-spacing: 0.2rpx;
  line-height: 1.5;
}

/* ========== 表单卡片 ========== */
.form-card {
  width: 100%;
  margin-top: 32rpx;
  padding: 36rpx 32rpx 28rpx;
  border-radius: $se-radius-xl;
  background-color: $surface-lowest;
  box-shadow: 0 4rpx 12rpx rgba(19, 27, 46, 0.05);
  box-sizing: border-box;
}

.se-form-cover {
  width: 100%;
}

/* ========== 提交按钮 ========== */
.primary-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 92rpx;
  margin-top: 32rpx;
  border-radius: $se-radius-md;
  background-color: $app-primary;
  box-shadow: 0 6rpx 16rpx rgba(79, 70, 229, 0.3);
  transition: transform 0.15s, opacity 0.15s;

  &:active {
    transform: scale(0.99);
  }

  &--disabled {
    opacity: 0.6;
  }
}

.primary-btn-text {
  font-size: 30rpx;
  font-weight: 600;
  color: #ffffff;
  line-height: 1;
}
</style>