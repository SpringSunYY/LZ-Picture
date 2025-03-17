package com.lz.user.model.dto.bannedPermissionInfo;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.BannedPermissionInfo;
/**
 * 用户封禁权限Query对象 u_banned_permission_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class BannedPermissionInfoQuery implements Serializable
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

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param bannedPermissionInfoQuery 查询对象
     * @return BannedPermissionInfo
     */
    public static BannedPermissionInfo queryToObj(BannedPermissionInfoQuery bannedPermissionInfoQuery) {
        if (bannedPermissionInfoQuery == null) {
            return null;
        }
        BannedPermissionInfo bannedPermissionInfo = new BannedPermissionInfo();
        BeanUtils.copyProperties(bannedPermissionInfoQuery, bannedPermissionInfo);
        return bannedPermissionInfo;
    }
}
