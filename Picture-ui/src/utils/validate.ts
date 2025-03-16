/**
 * 路径匹配器
 * @param {string} pattern 匹配模式
 * @param {string} path 路径
 * @returns {boolean}
 */
export function isPathMatch(pattern: string, path: string): boolean {
  const regexPattern = pattern.replace(/\//g, '\\/').replace(/\*\*/g, '.*').replace(/\*/g, '[^\\/]*');
  const regex = new RegExp(`^${regexPattern}$`);
  return regex.test(path);
}

/**
 * 判断value是否为空
 * @param {unknown} value 要判断的值
 * @returns {boolean}
 */
export function isEmpty(value: unknown): boolean {
  return value == null || value === "" || value === undefined || value === "undefined";
}

/**
 * 判断url是否是http或https
 * @param {string} url URL地址
 * @returns {boolean}
 */
export function isHttp(url: string): boolean {
  return url.includes('http://') || url.includes('https://');
}

/**
 * 判断path是否为外链
 * @param {string} path 路径
 * @returns {boolean}
 */
export function isExternal(path: string): boolean {
  return /^(https?:|mailto:|tel:)/.test(path);
}

/**
 * 校验用户名
 * @param {string} str 用户名
 * @returns {boolean}
 */
export function validUsername(str: string): boolean {
  const valid_map = ['admin', 'editor'];
  return valid_map.includes(str.trim());
}

/**
 * 校验URL格式
 * @param {string} url URL地址
 * @returns {boolean}
 */
export function validURL(url: string): boolean {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
  return reg.test(url);
}

/**
 * 校验小写字母
 * @param {string} str 字符串
 * @returns {boolean}
 */
export function validLowerCase(str: string): boolean {
  const reg = /^[a-z]+$/;
  return reg.test(str);
}

/**
 * 校验大写字母
 * @param {string} str 字符串
 * @returns {boolean}
 */
export function validUpperCase(str: string): boolean {
  const reg = /^[A-Z]+$/;
  return reg.test(str);
}

/**
 * 校验字母
 * @param {string} str 字符串
 * @returns {boolean}
 */
export function validAlphabets(str: string): boolean {
  const reg = /^[A-Za-z]+$/;
  return reg.test(str);
}

/**
 * 校验邮箱格式
 * @param {string} email 邮箱地址
 * @returns {boolean}
 */
export function validEmail(email: string): boolean {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email);
}

/**
 * 判断是否为字符串
 * @param {unknown} str 要判断的值
 * @returns {str is string}
 */
export function isString(str: unknown): str is string {
  return typeof str === 'string' || str instanceof String;
}

/**
 * 判断是否为数组
 * @param {unknown} arg 要判断的值
 * @returns {arg is any[]}
 */
export function isArray(arg: unknown): arg is any[] {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]';
  }
  return Array.isArray(arg);
}
