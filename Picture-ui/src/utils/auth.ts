import { Input, message, Modal } from 'ant-design-vue'
import { h } from 'vue'
import { getAccountPasswordVerify, verifyPassword } from '@/api/points/account.ts' // 引入 h 函数

export const usePasswordVerify = () => {
  const verify = async (actionDesc = '此操作') => {
    let password = ''
    //首先校验用户最近是否输入且正确
    const res = await getAccountPasswordVerify()
    if (res.code === 200 && res.data === 1) {
      return true;
    }
    return new Promise<boolean>((resolve) => {

      console.log('开始验证密码')
      Modal.confirm({
        title: '安全验证',
        content: h('div', [
          h('p', `请输入密码以${actionDesc}`),
          h(Input.Password, {
            placeholder: '请输入账户密码',
            onChange: (e: Event) => {
              password = (e.target as HTMLInputElement).value
            },
          }),
        ]),
        centered: true,
        okText: '确认',
        cancelText: '取消',
        onOk: async () => {
          if (!password) {
            message.warning('请输入密码')
            return Promise.reject()
          }
          try {
            const res = await verifyPassword(password)
            if (res.code === 200 && res.data === 1) {
              message.success('验证通过')
              resolve(true)
            } else {
              message.error('密码错误，请重新输入')
              return Promise.reject() // 阻止弹窗关闭
            }
          } catch (error) {
            console.log('验证密码失败', error)
            resolve(false)
          }
        },
        onCancel: () => resolve(false),
      })
    })
  }
  return { verify }
}
