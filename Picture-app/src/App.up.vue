<!--
  uview-plus 3.x Root 根组件
  Vite 插件 UniUpRoot 会在构建时自动改写此文件,把 <up-toast ref="upGlobalToastRef"> 注入进来
  默认文件已留好,无需手写组件,保持最小化即可
-->
<template>
    <UpRootView />
</template>

<script>
export default {
    onLaunch() {
        console.log('App.up Launch')
    }
}
</script>