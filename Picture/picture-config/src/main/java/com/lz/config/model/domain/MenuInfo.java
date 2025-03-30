package com.lz.config.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 菜单信息对象 c_menu_info
 *
 * @author YY
 * @date 2025-03-30
 */
@TableName("c_menu_info")
@Data
public class MenuInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @Excel(name = "编号")
    @TableId(value = "menu_id", type = IdType.ASSIGN_ID)
    private Long menuId;

    /**
     * 菜单名称
     */
    @Excel(name = "菜单名称")
    private String menuName;

    /**
     * 父菜单
     */
    @Excel(name = "父菜单")
    private String parentId;

    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /**
     * 路由地址
     */
    @Excel(name = "路由地址")
    private String path;

    /**
     * 组件路径
     */
    @Excel(name = "组件路径")
    private String component;

    /**
     * 路由参数
     */
    @Excel(name = "路由参数")
    private String query;

    /**
     * 路由名称
     */
    @Excel(name = "路由名称")
    private String routeName;

    /**
     * 显示位置
     */
    @Excel(name = "显示位置")
    private String menuAddress;

    /**
     * 是否外链
     */
    @Excel(name = "是否外链")
    private String isFrame;

    /**
     * 是否缓存
     */
    @Excel(name = "是否缓存")
    private String isCache;

    /**
     * 菜单类型
     */
    @Excel(name = "菜单类型")
    private String menuType;

    /**
     * 是否显示
     */
    @Excel(name = "是否显示")
    private String visible;

    /**
     * 菜单状态
     */
    @Excel(name = "菜单状态")
    private String status;

    /**
     * 权限标识
     */
    @Excel(name = "权限标识")
    private String perms;

    /**
     * 菜单图标
     */
    @Excel(name = "菜单图标")
    private String icon;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    @Excel(name = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
