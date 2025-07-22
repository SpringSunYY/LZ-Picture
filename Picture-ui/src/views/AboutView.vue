<template>
  <div class="min-h-screen bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100">
    <!-- Hero Section - 恢复“关于我们”内容，但采用 picture-hot 的背景和动画风格 -->
    <section class="relative hero-section-about-us">
      <div class="relative z-10 max-w-4xl mx-auto px-4 text-center">
        <h1
          class="text-6xl font-extrabold tracking-tight sm:text-7xl md:text-8xl animate-fade-in-up"
        >
          关于我们
        </h1>
        <p
          class="mt-6 text-xl sm:text-2xl md:text-3xl max-w-3xl mx-auto leading-relaxed animate-fade-in-up animation-delay-200"
        >
          我们（荔枝软件开发工作室）致力于通过创新和卓越，为您的生活和工作带来积极的改变，致力于一览无余的无限进步。
        </p>
      </div>
      <!-- 背景装饰元素，来自 picture-hot 的灵感 -->
      <div class="hero-decoration">
        <div class="decoration-circle circle-1"></div>
        <div class="decoration-circle circle-2"></div>
        <div class="decoration-circle circle-3"></div>
        <div class="decoration-circle circle-4"></div>
        <div class="decoration-circle circle-5"></div>
        <div class="decoration-circle circle-6"></div>
      </div>
    </section>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 space-y-24">
      <!-- Our Story Section -->
      <section class="grid md:grid-cols-2 gap-16 items-center py-12 animate-fade-in-up">
        <div class="order-2 md:order-1 animate-fade-in-left">
          <h2 class="text-5xl font-bold tracking-tight mb-6 text-green-500 dark:text-green-300">
            我们的故事
          </h2>
          <div
            class="content-paragraph text-lg leading-relaxed space-y-4 text-gray-700 dark:text-gray-300"
          >
            <p>
              我们工作室成立于 2022 年，源于对软件开发的热爱，对技术的提升。
              从一个简单的大学生期末作业，从一个简单的想法开始，到开发出属于我们的产品。
              我们一步步发展壮大，始终秉持着为客户创造价值的核心理念。我们相信，通过持续的学习和不懈的努力，可以为世界带来积极的改变。
            </p>
            <p>
              在过去的几年里，我们克服了无数挑战，更经历了无数次失败，我们不仅积累了宝贵的经验，更坚定了我们的决心。
              我们每一次的进步都离不开团队的辛勤付出和用户的坚定支持，
              更为所取得的成就感到自豪，并期待着未来的更多可能。
            </p>
          </div>
        </div>
        <div
          class="relative w-full aspect-video rounded-xl overflow-hidden shadow-2xl order-1 md:order-2 transform hover:scale-105 transition-transform duration-500 animate-fade-in-right"
        >
          <img :src="profileImage" alt="Our Story" class="object-cover w-full h-full" />
        </div>
      </section>

      <!-- Our Vision Section -->
      <section class="grid md:grid-cols-2 gap-16 items-center py-12 animate-fade-in-up">
        <div
          class="relative w-full aspect-video rounded-xl overflow-hidden shadow-2xl transform hover:scale-105 transition-transform duration-500 animate-fade-in-left"
        >
          <img :src="visionImage" alt="Our Vision" class="object-cover w-full h-full" />
        </div>
        <div class="animate-fade-in-right">
          <h2 class="text-5xl font-bold tracking-tight mb-6 text-teal-600 dark:text-teal-300">
            我们的愿景
          </h2>
          <div
            class="content-paragraph text-lg leading-relaxed space-y-4 text-gray-700 dark:text-gray-300"
          >
            <p>
              我们的愿景是成为行业领导者，不断探索前沿技术，为全球客户提供最先进、最可靠的解决方案。我们致力于构建一个开放、协作和创新的生态系统，让每个人都能从我们的产品和服务中受益。
            </p>
            <p>
              我们坚信，通过技术的力量，可以连接世界，赋能个体，并共同创造一个更加美好的未来。我们将持续投入研发，倾听用户声音，不断超越自我，以实现这一宏伟愿景，为社会贡献我们的力量。
            </p>
          </div>
        </div>
      </section>

      <!-- Our Story Section -->
      <section class="grid md:grid-cols-2 gap-16 items-center py-12 animate-fade-in-up">
        <div class="order-2 md:order-1 animate-fade-in-left">
          <h2 class="text-5xl font-bold tracking-tight mb-6 text-purple-500 dark:text-green-300">
            联系我们
          </h2>
          <div
            class="content-paragraph text-lg leading-relaxed space-y-4 text-gray-700 dark:text-gray-300"
          >
            <p>这是我们的工作室第一个产品，我们不知道是否能够成功，但是也要勇于尝试。</p>
            <p>
              所以我们也需要展示我们主要的业务，帮助我们能够逐步壮大，完善自己。如果您有需要可以联系我们。
            </p>
          </div>
        </div>
        <div
          class="relative w-full aspect-video rounded-xl overflow-hidden shadow-2xl order-1 md:order-2 transform hover:scale-105 transition-transform duration-500 animate-fade-in-right"
        >
          <img :src="cardImage" alt="Our Story" class="object-cover w-full h-full" />
        </div>
      </section>

      <section class="py-12 text-center p-8 md:p-12">
        <h2 class="text-5xl font-bold tracking-tight mb-16 text-blue-500 dark:text-gray-200">
          赞助我们 / 联系我们
        </h2>
        <div
          class="content-paragraph text-lg leading-relaxed space-y-4 text-gray-700 dark:text-gray-300 mb-12"
        >
          <p>
            我们非常欢迎各界朋友与我们建立合作关系，无论是商业赞助、技术合作还是媒体采访，我们都期待与您携手共创价值。如果您对我们的项目感兴趣，或者有任何疑问和建议，请随时通过以下方式联系我们。
          </p>
          <p>
            您的支持是我们持续创新和发展的动力。我们相信，通过开放合作，可以共同实现更大的目标。期待您的来信！
          </p>
        </div>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
          <div class="flex flex-col items-center">
            <img
              :src="wxImage"
              alt="Sponsor Image 1"
              class="w-48 h-48 object-cover rounded-lg shadow-md mb-4"
            />
            <p class="text-xl font-semibold text-gray-900 dark:text-gray-100">商业合作</p>
            <p class="text-base text-gray-700 dark:text-gray-300 mt-2">探索潜在的商业合作机会</p>
          </div>
          <div class="flex flex-col items-center">
            <img
              :src="wxOfferImage"
              alt="Sponsor Image 2"
              class="w-48 h-48 object-cover rounded-lg shadow-md mb-4"
            />
            <p class="text-xl font-semibold text-gray-900 dark:text-gray-100">媒体与公关</p>
            <p class="text-base text-gray-700 dark:text-gray-300 mt-2">微信公众号</p>
          </div>
          <div class="flex flex-col items-center">
            <img
              :src="wxPayImage"
              alt="Sponsor Image 3"
              class="w-48 h-48 object-cover rounded-lg shadow-md mb-4"
            />
            <p class="text-xl font-semibold text-gray-900 dark:text-gray-100">微信</p>
            <p class="text-base text-gray-700 dark:text-gray-300 mt-2">微信支付</p>
          </div>
          <div class="flex flex-col items-center">
            <img
              :src="aliApyImage"
              alt="Sponsor Image 3"
              class="w-48 h-48 object-cover rounded-lg shadow-md mb-4"
            />
            <p class="text-xl font-semibold text-gray-900 dark:text-gray-100">支付宝</p>
            <p class="text-base text-gray-700 dark:text-gray-300 mt-2">支付宝支付</p>
          </div>
        </div>
      </section>
      <!-- Our Team Section -->
      <section class="content-paragraph py-12 text-center animate-fade-in-up">
        <h2 class="text-5xl font-bold tracking-tight mb-16 text-yellow-500 dark:text-gray-200">
          我们的团队
        </h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-12">
          <div
            class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-8 transform transition-transform duration-500 hover:scale-105 hover:shadow-2xl animate-fade-in-up"
          >
            <img
              :src="profileImage"
              alt="Jane Doe - CEO"
              class="w-40 h-40 rounded-full mx-auto mb-6 object-cover border-4 border-green-500 dark:border-green-600 shadow-md"
            />
            <h3 class="text-3xl font-semibold text-gray-900 dark:text-gray-100">YY</h3>
            <p class="text-xl text-gray-600 dark:text-gray-400 mt-2">YY</p>
            <p class="mt-4 text-base text-gray-700 dark:text-gray-300 content-paragraph">
              Jane 开发工程师，是公司的灵魂人物，带领团队不断突破创新。
            </p>
          </div>
          <div
            class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-8 transform transition-transform duration-500 hover:scale-105 hover:shadow-2xl animate-fade-in-up animation-delay-100"
          >
            <img
              :src="profileImage"
              alt="John Smith - CTO"
              class="w-40 h-40 rounded-full mx-auto mb-6 object-cover border-4 border-teal-500 dark:border-teal-600 shadow-md"
            />
            <h3 class="text-3xl font-semibold text-gray-900 dark:text-gray-100">YY</h3>
            <p class="text-xl text-gray-600 dark:text-gray-400 mt-2">负责人</p>
            <p class="mt-4 text-base text-gray-700 dark:text-gray-300 content-paragraph">
              负责公司的技术战略和创新，确保我们的产品始终走在行业前沿，为用户提供最先进的解决方案。
            </p>
          </div>
          <div
            class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-8 transform transition-transform duration-500 hover:scale-105 hover:shadow-2xl animate-fade-in-up animation-delay-200"
          >
            <img
              :src="profileImage"
              alt="Emily White - Marketing Director"
              class="w-40 h-40 rounded-full mx-auto mb-6 object-cover border-4 border-orange-500 dark:border-orange-600 shadow-md"
            />
            <h3 class="text-3xl font-semibold text-gray-900 dark:text-gray-100">YY</h3>
            <p class="text-xl text-gray-600 dark:text-gray-400 mt-2">市场总监</p>
            <p class="mt-4 text-base text-gray-700 dark:text-gray-300 content-paragraph">
              带领团队将我们的品牌推向市场，通过创新的营销策略，与用户建立深厚的情感连接。
            </p>
          </div>
        </div>
      </section>

      <!-- Call to Action Section -->
      <section
        class="relative py-20 md:py-24 text-center bg-gradient-to-br from-orange-500 to-red-600 dark:from-orange-800 dark:to-red-900 text-white rounded-xl shadow-2xl overflow-hidden animate-fade-in-up"
      >
        <!-- 背景抽象图案 -->
        <div
          class="absolute inset-0 opacity-10 dark:opacity-20 bg-[url('/placeholder.svg?height=600&width=900')] bg-cover bg-center"
        ></div>
        <div class="content-paragraph relative z-10 max-w-3xl mx-auto px-4">
          <h2 class="text-5xl font-bold tracking-tight sm:text-6xl mb-6">加入我们，共创未来！</h2>
          <p class="mt-4 text-xl sm:text-2xl leading-relaxed">
            我们一直在寻找有才华、有热情的人加入我们的团队。如果你对创新充满激情，并希望与一群志同道合的人一起工作，共同创造未来，请查看我们的招聘页面。
          </p>
          <a
            href="#"
            class="mt-10 inline-flex items-center px-12 py-5 border border-white text-xl font-medium rounded-full shadow-lg text-white bg-transparent hover:bg-white hover:text-orange-600 dark:hover:text-orange-800 transition duration-300 ease-in-out transform hover:-translate-y-1"
          >
            查看职位空缺
          </a>
        </div>
      </section>

      <!-- Legal & Help Sections Group -->
      <div class="grid lg:grid-cols-2 gap-16 py-12">
        <section
          class="bg-white dark:bg-gray-800 p-8 md:p-12 rounded-xl shadow-xl animate-fade-in-up transform transition-transform duration-500 hover:scale-105 hover:shadow-2xl animate-fade-in-up"
        >
          <h2
            class="text-4xl font-bold tracking-tight text-center mb-8 text-gray-800 dark:text-gray-200"
          >
            隐私条款
          </h2>
          <div
            class="content-paragraph text-base leading-relaxed space-y-4 text-gray-700 dark:text-gray-300"
          >
            <p>
              我们非常重视您的隐私。本隐私条款解释了我们如何收集、使用、披露和保护您的个人信息。通过使用我们的服务，即表示您同意本隐私条款的条款。
            </p>
            <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
              信息收集
            </h3>
            <p>
              我们可能会收集您直接提供给我们的信息，例如当您注册账户、填写表格或与我们联系时。这些信息可能包括您的姓名、电子邮件地址、电话号码等。我们还会自动收集某些信息，例如您的
              IP 地址、浏览器类型、访问时间以及您在我们平台上的活动。
            </p>
            <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
              信息使用
            </h3>
            <p>
              我们使用收集到的信息来提供、维护和改进我们的服务，处理您的交易，向您发送相关通知，以及用于营销和分析目的。这些用途包括个性化您的体验、提供客户支持、进行研究和分析以优化服务性能。
            </p>
            <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
              信息披露
            </h3>
            <p>
              我们不会出售您的个人信息。我们可能会与第三方服务提供商共享信息，以帮助我们运营业务，例如支付处理、数据分析、营销服务等。在法律要求的情况下，或为了保护我们的权利、财产或安全，我们可能会披露信息。
            </p>
          </div>
        </section>

        <section
          class="bg-white dark:bg-gray-800 p-8 md:p-12 rounded-xl shadow-xl animate-fade-in-up animation-delay-100 transform transition-transform duration-500 hover:scale-105 hover:shadow-2xl animate-fade-in-up"
        >
          <h2
            class="text-4xl font-bold tracking-tight text-center mb-8 text-gray-800 dark:text-gray-200"
          >
            服务条款
          </h2>
          <div
            class="content-paragraph text-base leading-relaxed space-y-4 text-gray-700 dark:text-gray-300"
          >
            <p>
              欢迎使用我们的服务。在使用我们的服务之前，请仔细阅读本服务条款。通过访问或使用我们的服务，即表示您同意受本条款的约束。
            </p>
            <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
              账户责任
            </h3>
            <p>
              您有责任维护您的账户信息的机密性，并对您账户下发生的所有活动负责。您同意在发现任何未经授权使用您账户的情况时立即通知我们。
            </p>
            <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
              禁止行为
            </h3>
            <p>
              您同意不从事任何非法、有害或侵犯他人权利的活动，包括但不限于发送垃圾邮件、上传恶意软件、侵犯知识产权、进行网络钓鱼或任何形式的骚扰。
            </p>
            <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
              免责声明
            </h3>
            <p>
              我们的服务按“原样”提供，不作任何明示或暗示的保证，包括但不限于适销性、特定用途适用性或不侵权的保证。我们不保证服务将不间断、无错误或安全。
            </p>
          </div>
        </section>
      </div>

      <section
        class="bg-white dark:bg-gray-800 p-8 md:p-12 rounded-xl shadow-xl animate-fade-in-up transform transition-transform duration-500 hover:scale-105 hover:shadow-2xl animate-fade-in-up"
      >
        <h2
          class="text-4xl font-bold tracking-tight text-center mb-8 text-gray-800 dark:text-gray-200"
        >
          版权声明
        </h2>
        <div
          class="content-paragraph text-base leading-relaxed space-y-4 text-gray-700 dark:text-gray-300"
        >
          <p>
            欢迎您使用本网站（以下简称“本平台”）提供的图片发布、浏览、下载等服务。为了保障原创作者的合法权益，防范和减少侵权行为，构建良性的内容创作和传播环境，特作出如下版权声明。本声明适用于所有在本平台上传、展示、下载、传播图片及相关内容的行为。请所有用户在使用本平台前仔细阅读并理解本声明，使用即视为您同意并接受本声明的全部内容。
          </p>

          <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
            一、图片内容的版权归属与授权说明
          </h3>
          <p>
            用户在本平台上传的所有图片及其相关信息（包括但不限于标题、标签、描述、EXIF
            元数据等）由上传者本人负责，其应确保对该内容享有合法、完整的著作权或已取得合法授权。
            上传图片的用户须明确承诺：所上传的图片为其原创，或者已取得原著作权人或合法代理人的明确授权；上传内容不侵犯任何第三方的著作权、肖像权、商标权、名誉权、隐私权或其他合法权益；不得上传来源于商业图库、影视截图、动漫角色、品牌
            logo、明星肖像、网络转载图片等可能涉及侵权的内容，除非具有合法授权。
            用户在上传图片的同时，自动授予本平台一项全球范围内的、非独占、可转授权、免许可费的永久性授权，用于：在平台内展示该图片；通过平台系统生成缩略图、水印图；用于平台内容推荐、页面宣传、搜索结果呈现等相关服务；用于平台自身推广、运营与相关功能的展示与维护（包括但不限于首页、排行榜、专题推荐等）。
          </p>
          <p>
            用户上传的图片如若侵犯您的权利，请立即联系我们，我们会及时处理并采取必要的措施。同时，本平台将采取必要措施，以保护您的合法权益。
          </p>
          <p>
            本平台仅作为图片管理平台，用户可以在平台上管理自己的图片资源，如果用户上传的图片（违规、侵权、版权虚假）被其他用户所下载，用户上传的图片如果版权虚假，造成的版权纠纷均由用户承担，并需要承担平台造成的损失。
          </p>

          <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
            二、使用限制
          </h3>
          <p>
            为保证平台正常运营，本平台将采取必要措施：
            <p>
              1、平台图片分为私有图片和公开图片：私有图片：仅供个人使用，不为任何其他用户提供任何操作权限，不向其他用户展示；公开图片：平台提供公开图片服务，其他用户可以查看到图片的基本信息。
            </p>
            <p>
              2、图片上传与展示：允许用户上传图片，但因为运营、技术原因，平台图片的展示可能会有延迟，请用户耐心等待。同时所有的图片平台都会压缩并未图片添加水印作为展示，降低平台的运营成本，平台也会为用户保留原图，在用户所需图片（本人下载、查看原图）时，提供原图链接。
            </p>
            <p>
              3、查看原图：用户可以在平台内查看图片的原图，但请务必遵守图片版权、肖像权、商标权、名誉权、隐私权或其他合法权益。
            </p>
          </p>

          <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
            三、侵权投诉与版权保护机制
          </h3>
          <p>
            本平台尊重并保护所有创作者与权利人的合法权益。若您认为平台某个图片或内容涉嫌侵犯了您的著作权、肖像权、商标权等合法权益，请通过以下方式进行投诉：发送邮件至：<a
            href="https://wx.mail.qq.com/"
            class="text-blue-600 dark:text-blue-400 hover:underline"
          >3217778348@qq.com</a
          >
            或通过平台提供的“版权投诉”通道于联系方式提交资料为加快处理效率，建议投诉时提供以下材料：被侵权作品的权属证明材料（如著作权登记证书、原始创作记录等）；涉嫌侵权内容在平台上的页面链接或截图；原始作品与涉嫌侵权图片的对比分析说明；投诉人真实身份信息与联系方式。本平台在收到有效投诉后，将按照国家相关法律法规和平台规则，在48小时内完成初步审核处理。如投诉属实，将立即下架侵权内容，并视情况对上传者作出如下处理：警告或提示；限制上传权限；封禁账户；向相关司法部门或权利人提供必要配合（如依法需提供上传者信息）。
          </p>

          <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
            四、免责声明
          </h3>
          <p>
            本平台作为中立的图片分享平台，主要提供用户内容展示与推荐的服务，本身并不主动上传或编辑任何用户图片内容，也无法对所有用户上传内容的合法性进行实质审查，因此无法保证其绝对的原创性与版权合规性。本平台不对用户上传内容的真实性、合法性、准确性承担法律责任，但承诺履行合理的注意义务，积极接受并处理权利人的侵权投诉。对于任何用户因下载或使用平台内容而产生的版权纠纷、法律责任或经济损失，平台概不承担赔偿责任，相关责任由上传者或使用者自行承担。
          </p>

          <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
            五、特别提示与风险警示
          </h3>
          <p>
            请勿上传任何未经授权的商业图片、知名影视/动漫角色图、明星肖像、品牌
            logo、他人摄影作品等内容；对于反复上传侵权内容、恶意传播盗版资源的用户，平台将永久封禁并保留依法追责的权利；本平台鼓励用户发布原创摄影、设计作品，平台将为原创用户提供更高曝光、积分激励与内容推荐机会；所有用户须自觉遵守国家版权法律法规及平台相关规定，共同维护健康、合法、可持续发展的内容社区。
          </p>
          <p class="mt-6 text-base text-gray-600 dark:text-gray-400">
            本声明自发布之日起生效，平台有权根据实际运营情况随时修订本声明内容，修订后的版本将通过平台公示方式发布，并自发布之日起生效。如您对本声明有任何疑问或异议，请及时与我们联系。
          </p>
        </div>
      </section>

      <section
        class="bg-white dark:bg-gray-800 p-8 md:p-12 rounded-xl shadow-xl animate-fade-in-up transform transition-transform duration-500 hover:scale-105 hover:shadow-2xl animate-fade-in-up"
      >
        <h2
          class="text-4xl font-bold tracking-tight text-center mb-8 text-gray-800 dark:text-gray-200"
        >
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
              您可以通过点击主页上的“注册”按钮，然后按照提示填写必要信息来注册用户。请确保提供准确的个人信息以便我们为您提供更好的服务。
            </p>
          </div>
          <div>
            <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
              如何重置密码？
            </h3>
            <p>
              在登录页面，点击“忘记密码”链接，然后输入您的注册手机号，我们将向您发送重置密码的验证码。请检查您的垃圾短信文件夹，以防短信被误判。
            </p>
            <h3>
              如何创建账户？
            </h3>
            <p>
              用户在充值积分后平台会自动为您创建账户，并自动充值积分。
            </p>
          </div>
          <div>
            <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 mt-6">
              联系支持
            </h3>
            <p>
              如果您有其他问题，请通过
              <a
                href="https://wx.mail.qq.com/"
                class="text-blue-600 dark:text-blue-400 hover:underline"
              >3217778348@qq.com</a
              >
              联系我们，或访问我们的
              <a href="#" class="text-blue-600 dark:text-blue-400 hover:underline">联系页面</a
              >。我们的支持团队将在 24 小时内回复您。
            </p>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts" name="AboutView">
import profileImage from '@/assets/images/profile.jpg'
import cardImage from '@/assets/images/card.png'
import visionImage from '@/assets/images/vision.jpeg'
import wxOfferImage from '@/assets/images/wxOffer.jpg'
import wxImage from '@/assets/images/wx.png'
import wxPayImage from '@/assets/images/wxPay.jpg'
import aliApyImage from '@/assets/images/aliPay.jpg'
</script>

<style scoped>
/* 为所有需要缩进的段落添加样式 */
.content-paragraph p {
  text-indent: 2em; /* 两字符缩进 */
}

/* 确保标题和列表等不受影响 */
.content-paragraph h3,
.content-paragraph ul,
.content-paragraph ol,
.content-paragraph div {
  text-indent: 0;
}

/* Hero Section Styles - 仅用于“关于我们”页面的顶部背景和装饰 */
.hero-section-about-us {
  position: relative;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); /* 使用你提供的渐变色 */
  //border-radius: 0 0 40px 40px; /* 底部圆角，顶部直角 */
  padding: 4rem 2rem;
  margin: 0; /* 移除外边距，让它全宽 */
  overflow: hidden;
  color: white;
  box-shadow: 0 20px 40px rgba(102, 126, 234, 0.3);
}

/* 装饰元素样式，来自 picture-hot 的灵感 */
.hero-decoration {
  position: absolute;
  top: 0;
  right: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15); /* 稍微亮一点 */
  box-shadow: 0 0 20px rgba(255, 255, 255, 0.1);
}

.circle-1 {
  width: 150px; /* 增大 */
  height: 150px;
  top: -75px;
  right: -75px;
  animation: float-1 8s ease-in-out infinite;
}

.circle-2 {
  width: 100px; /* 增大 */
  height: 100px;
  top: 60%;
  left: 10%; /* 调整位置 */
  animation: float-2 6s ease-in-out infinite reverse;
}

.circle-3 {
  width: 80px; /* 增大 */
  height: 80px;
  bottom: 20px;
  right: 20%; /* 调整位置 */
  animation: float-3 7s ease-in-out infinite;
}

.circle-4 {
  width: 60px;
  height: 60px;
  bottom: 10px;
  right: 10%;
  animation: float-4 5s ease-in-out infinite;
}

.circle-5 {
  width: 40px;
  height: 40px;
  top: 10px;
  left: 10%;
  animation: float-5 3s ease-in-out infinite;
}

.circle-6 {
  width: 20px;
  height: 20px;
  bottom: 30px;
  right: 40%;
  animation: float-6 2s ease-in-out infinite;
}

/* 浮动动画 */
@keyframes float-1 {
  0%,
  100% {
    transform: translateY(0px) translateX(0px);
  }
  50% {
    transform: translateY(-30px) translateX(15px);
  }
}

@keyframes float-2 {
  0%,
  100% {
    transform: translateY(0px) translateX(0px);
  }
  50% {
    transform: translateY(10px) translateX(-10px);
  }
}

@keyframes float-3 {
  0%,
  100% {
    transform: translateY(0px) translateX(0px);
  }
  50% {
    transform: translateY(-20px) translateX(-10px);
  }
}

@keyframes float-4 {
  0%,
  100% {
    transform: translateY(0px) translateX(0px);
  }
  50% {
    transform: translateY(50px) translateX(40px);
  }
}

@keyframes float-5 {
  0%,
  100% {
    transform: translateY(0px) translateX(0px);
  }
  50% {
    transform: translateY(20px) translateX(20px);
  }
}

@keyframes float-6 {
  0%,
  100% {
    transform: translateY(0px) translateX(0px);
  }
  50% {
    transform: translateY(10px) translateX(10px);
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .hero-section-about-us {
    padding: 3rem 1.5rem;
    border-radius: 0; /* 手机上移除圆角 */
  }

  /* 手机上 Hero 标题和段落的字体大小调整 */
  .hero-section-about-us h1 {
    font-size: 2.5rem;
  }

  .hero-section-about-us p {
    font-size: 1.25rem;
  }
}

/* Custom Animations for content sections */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  /* 增加位移 */
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.animate-fade-in {
  animation: fadeIn 1s ease-out forwards;
  opacity: 0; /* 初始隐藏 */
}

.animate-fade-in-up {
  animation: fadeInUp 1s ease-out forwards;
  opacity: 0; /* 初始隐藏 */
}

.animate-fade-in-left {
  animation: fadeInLeft 1s ease-out forwards;
  opacity: 0; /* 初始隐藏 */
}

.animate-fade-in-right {
  animation: fadeInRight 1s ease-out forwards;
  opacity: 0; /* 初始隐藏 */
}

/* Delay utilities for sequential animations */
.animation-delay-100 {
  animation-delay: 0.1s;
}

.animation-delay-200 {
  animation-delay: 0.2s;
}

.animation-delay-300 {
  animation-delay: 0.3s;
}

.animation-delay-400 {
  animation-delay: 0.4s;
}

.animation-delay-500 {
  animation-delay: 0.5s;
}

/* Add more as needed */
</style>
