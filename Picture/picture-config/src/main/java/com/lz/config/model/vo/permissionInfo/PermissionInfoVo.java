package com.lz.config.model.vo.permissionInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.PermissionInfo;
/**
 * 权限信息Vo对象 c_permission_info
 *
 * @author YY
 * @date 2025-02-28
 */
@Data
public class PermissionInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Excel(name = "编号")
    private Long permissionId;

    /** 权限名称 */
    @Excel(name = "权限名称")
    private String permissionName;

    /** 父菜单 */
    @Excel(name = "父菜单")
    private String parentId;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /** 权限标识 */
    @Excel(name = "权限标识")
    private String permission;

    /** 是否使用（0正常 1关闭） */
    @Excel(name = "是否使用", readConverterExp = "0=正常,1=关闭")
    private String status;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    @Excel(name = "更新人")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;


     /**
     * 对象转封装类
     *
     * @param permissionInfo PermissionInfo实体对象
     * @return PermissionInfoVo
     */
    public static PermissionInfoVo objToVo(PermissionInfo permissionInfo) {
        if (permissionInfo == null) {
            return null;
        }
        PermissionInfoVo permissionInfoVo = new PermissionInfoVo();
        BeanUtils.copyProperties(permissionInfo, permissionInfoVo);
        return permissionInfoVo;
    }
}
