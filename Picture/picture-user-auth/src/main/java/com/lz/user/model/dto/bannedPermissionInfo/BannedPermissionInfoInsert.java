package com.lz.user.model.dto.bannedPermissionInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.user.model.domain.BannedPermissionInfo;
/**
 * 用户封禁权限Vo对象 u_banned_permission_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class BannedPermissionInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 封禁记录编号 */
    private String bannedId;

    /** 权限名称 */
    private String permissionName;

    /** 用户 */
    private String userId;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 状态（0=封禁中 1=结束） */
    private String status;

    /** 封禁原因 */
    private String cause;

    /**
     * 对象转封装类
     *
     * @param bannedPermissionInfoInsert 插入对象
     * @return BannedPermissionInfoInsert
     */
    public static BannedPermissionInfo insertToObj(BannedPermissionInfoInsert bannedPermissionInfoInsert) {
        if (bannedPermissionInfoInsert == null) {
            return null;
        }
        BannedPermissionInfo bannedPermissionInfo = new BannedPermissionInfo();
        BeanUtils.copyProperties(bannedPermissionInfoInsert, bannedPermissionInfo);
        return bannedPermissionInfo;
    }
}
