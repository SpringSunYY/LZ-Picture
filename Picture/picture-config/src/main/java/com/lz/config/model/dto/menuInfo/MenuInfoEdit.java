package com.lz.config.model.dto.menuInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.config.model.domain.MenuInfo;
/**
 * 菜单信息Vo对象 c_menu_info
 *
 * @author YY
 * @date 2025-03-30
 */
@Data
public class MenuInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long menuId;

    /** 菜单名称 */
    private String menuName;

    /** 父菜单 */
    private String parentId;

    /** 显示顺序 */
    private Long orderNum;

    /** 路由地址 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 路由参数 */
    private String query;

    /** 路由名称 */
    private String routeName;

    /** 显示位置 */
    private String menuAddress;

    /** 是否外链 */
    private String isFrame;

    /** 是否缓存 */
    private String isCache;

    /** 菜单类型 */
    private String menuType;

    /** 是否显示 */
    private String visible;

    /** 菜单状态 */
    private String status;

    /** 权限标识 */
    private String perms;

    /** 菜单图标 */
    private String icon;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param menuInfoEdit 编辑对象
     * @return MenuInfo
     */
    public static MenuInfo editToObj(MenuInfoEdit menuInfoEdit) {
        if (menuInfoEdit == null) {
            return null;
        }
        MenuInfo menuInfo = new MenuInfo();
        BeanUtils.copyProperties(menuInfoEdit, menuInfo);
        return menuInfo;
    }
}
