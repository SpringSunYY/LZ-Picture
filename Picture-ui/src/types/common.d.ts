// 通用的用户类型
export type LoginUser = {
  username: string
  password: string
}

export type UserInfo = {
  userName: string
  password?: string
  avatar?: string
}
/* eslint-disable */
declare namespace API {
  interface ResponseInfo<T =any> {
    rows?: Array<T>
    data?: T
    msg?: string
    code: number
  }
}

export type Dict = {
  /** 字典编码 */
  dictCode?: number;


  /** 字典标签 */
  dictLabel: string;

  /** 字典键值 */
  dictValue: string;

  /** 字典类型 */
  dictType?: string;

  /** 样式属性（其他样式扩展） */
  cssClass?: string;

  /** 表格字典样式 */
  listClass?: string;
};
