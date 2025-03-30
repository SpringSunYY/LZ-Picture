package com.lz.config.model.dto.menuInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.MenuInfo;
/**
 * 菜单信息Query对象 c_menu_info
 *
 * @author YY
 * @date 2025-03-30
 */
@Data
public class MenuInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long menuId;

    /** 菜单名称 */
    private String menuName;

    /** 父菜单 */
    private String parentId;

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

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param menuInfoQuery 查询对象
     * @return MenuInfo
     */
    public static MenuInfo queryToObj(MenuInfoQuery menuInfoQuery) {
        if (menuInfoQuery == null) {
            return null;
        }
        MenuInfo menuInfo = new MenuInfo();
        BeanUtils.copyProperties(menuInfoQuery, menuInfo);
        return menuInfo;
    }
}
