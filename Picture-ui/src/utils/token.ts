// 封装存取方法
const TOKEN_KEY = 'token_key'

function setToken(token: string) {
    return localStorage.setItem(TOKEN_KEY, token)
}

function getToken() {
    return localStorage.getItem(TOKEN_KEY)
}

function removeToken() {
    return localStorage.removeItem(TOKEN_KEY)
}

export {
    setToken,
    getToken,
    removeToken
}
