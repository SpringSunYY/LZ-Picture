<template>
  <section
    class="bg-white dark:bg-gray-800 p-8 md:p-12 rounded-xl shadow-xl animate-fade-in-up transform transition-transform duration-500 hover:scale-105 hover:shadow-2xl"
  >
    <h2 class="text-4xl font-bold tracking-tight text-center mb-8 text-gray-800 dark:text-gray-200">
      帮助中心
    </h2>
    <div
      class="content-paragraph text-base leading-relaxed space-y-4 text-gray-700 dark:text-gray-300"
    >
      <p>
        在这里您可以找到常见问题的答案和有用的资源。如果您找不到所需的信息，请随时联系我们的支持团队。
      </p>

      <div>
        <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
          如何注册用户？
        </h3>
        <p>
          您可以通过点击主页上的"注册"按钮，然后按照提示填写必要信息来注册用户。请确保提供准确的个人信息以便我们为您提供更好的服务。
        </p>
      </div>

      <div>
        <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
          如何重置密码？
        </h3>
        <p>
          在登录页面，点击"忘记密码"链接，然后输入您的注册手机号，我们将向您发送重置密码的验证码。请检查您的垃圾短信文件夹，以防短信被误判。
        </p>
        <h3>如何创建账户？</h3>
        <p>用户在充值积分后平台会自动为您创建账户，并自动充值积分。</p>
      </div>

      <div>
        <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">联系支持</h3>
        <p>
          如果您有其他问题，请通过
          <a href="https://wx.mail.qq.com/" class="text-blue-600 dark:text-blue-400 hover:underline"
            >3217778348@qq.com</a
          >
          联系我们，或访问我们的
          <a href="#" class="text-blue-600 dark:text-blue-400 hover:underline">联系页面</a>
          。我们的支持团队将在 24 小时内回复您。
        </p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts" name="HelpCenterSection">
import './index.css'
</script>

<style scoped></style>
