package com.lz.config.model.dto.permissionInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.config.model.domain.PermissionInfo;
/**
 * 权限信息Vo对象 c_permission_info
 *
 * @author ruoyi
 * @date 2025-02-28
 */
@Data
public class PermissionInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long permissionId;

    /** 权限名称 */
    private String permissionName;

    /** 父菜单 */
    private String parentId;

    /** 显示顺序 */
    private Long orderNum;

    /** 权限标识 */
    private String permission;

    /** 是否使用（0正常 1关闭） */
    private String status;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param permissionInfoEdit 编辑对象
     * @return PermissionInfo
     */
    public static PermissionInfo editToObj(PermissionInfoEdit permissionInfoEdit) {
        if (permissionInfoEdit == null) {
            return null;
        }
        PermissionInfo permissionInfo = new PermissionInfo();
        BeanUtils.copyProperties(permissionInfoEdit, permissionInfo);
        return permissionInfo;
    }
}
