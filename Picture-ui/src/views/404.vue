<template>
  <div class="not-found">
    <h1>404</h1>
    <p>页面不存在</p>
  </div>
</template>

<style scoped>
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: var(--bg-color);
  color: var(--text-color);

  h1 {
    font-size: 8rem;
    margin-bottom: 20px;
  }

  p {
    font-size: 2rem;
    margin-bottom: 40px;
  }
}
</style>
