const getters = {
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  username: state => state.user.username,
  permissions: state => state.user.permissions
}
export default getters