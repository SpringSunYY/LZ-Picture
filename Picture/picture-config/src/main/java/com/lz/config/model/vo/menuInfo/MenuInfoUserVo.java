package com.lz.config.model.vo.menuInfo;

import com.lz.config.model.domain.MenuInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * Project: Picture
 * Package: com.lz.config.model.vo.menuInfo
 * Author: YY
 * CreateTime: 2025-03-31  22:26
 * Description: MenuInfoUserVo
 * 用户返回vo
 * Version: 1.0
 */
@Data
public class MenuInfoUserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 编号
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单
     */
    private String parentId;

    /**
     * 显示顺序
     */
    private Long orderNum;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 路由名称
     */
    private String routeName;

    /**
     * 显示位置
     */
    private String menuAddress;

    /**
     * 是否外链
     */
    private String isFrame;

    /**
     * 是否缓存
     */
    private String isCache;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单类型
     */
    private String menuType;

    /**
     * 对象转封装类
     *
     * @param menuInfo MenuInfo实体对象
     * @return MenuInfoUserVo
     */
    public static MenuInfoUserVo objToVo(MenuInfo menuInfo) {
        if (menuInfo == null) {
            return null;
        }
        MenuInfoUserVo menuInfoVo = new MenuInfoUserVo();
        BeanUtils.copyProperties(menuInfo, menuInfoVo);
        return menuInfoVo;
    }
}
