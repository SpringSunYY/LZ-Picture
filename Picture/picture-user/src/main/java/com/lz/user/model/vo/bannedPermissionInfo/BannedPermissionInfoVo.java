package com.lz.user.model.vo.bannedPermissionInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.BannedPermissionInfo;
/**
 * 用户封禁权限Vo对象 u_banned_permission_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class BannedPermissionInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 封禁记录编号 */
    @Excel(name = "封禁记录编号")
    private String bannedId;

    /** 权限名称 */
    @Excel(name = "权限名称")
    private String permissionName;

    /** 用户 */
    @Excel(name = "用户")
    private String userId;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 状态（0=封禁中 1=结束） */
    @Excel(name = "状态", readConverterExp = "0==封禁中,1==结束")
    private String status;

    /** 封禁原因 */
    @Excel(name = "封禁原因")
    private String cause;


     /**
     * 对象转封装类
     *
     * @param bannedPermissionInfo BannedPermissionInfo实体对象
     * @return BannedPermissionInfoVo
     */
    public static BannedPermissionInfoVo objToVo(BannedPermissionInfo bannedPermissionInfo) {
        if (bannedPermissionInfo == null) {
            return null;
        }
        BannedPermissionInfoVo bannedPermissionInfoVo = new BannedPermissionInfoVo();
        BeanUtils.copyProperties(bannedPermissionInfo, bannedPermissionInfoVo);
        return bannedPermissionInfoVo;
    }
}
