package com.lz.config.model.dto.permissionInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.PermissionInfo;
/**
 * 权限信息Query对象 c_permission_info
 *
 * @author ruoyi
 * @date 2025-02-28
 */
@Data
public class PermissionInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 权限名称 */
    private String permissionName;

    /** 父菜单 */
    private String parentId;

    /** 是否使用（0正常 1关闭） */
    private String status;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param permissionInfoQuery 查询对象
     * @return PermissionInfo
     */
    public static PermissionInfo queryToObj(PermissionInfoQuery permissionInfoQuery) {
        if (permissionInfoQuery == null) {
            return null;
        }
        PermissionInfo permissionInfo = new PermissionInfo();
        BeanUtils.copyProperties(permissionInfoQuery, permissionInfo);
        return permissionInfo;
    }
}
