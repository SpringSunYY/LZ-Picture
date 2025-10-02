const getters = {
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  username: state => state.user.username,
  userId: state => state.user.userId,
  nickName: state => state.user.nickName,
  permissions: state => state.user.permissions
}
export default getters
