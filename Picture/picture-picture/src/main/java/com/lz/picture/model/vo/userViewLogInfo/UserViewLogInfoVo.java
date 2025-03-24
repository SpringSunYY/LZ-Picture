package com.lz.picture.model.vo.userViewLogInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.UserViewLogInfo;
/**
 * 用户浏览记录Vo对象 p_user_view_log_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class UserViewLogInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 浏览记录编号 */
    @Excel(name = "浏览记录编号")
    private String viewId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private String userId;

    /** 目标类型（0图片 1用户 2空间） */
    @Excel(name = "目标类型", readConverterExp = "0=图片,1=用户,2=空间")
    private String targetType;

    /** 目标对象编号 */
    @Excel(name = "目标对象编号")
    private String targetId;

    /** 图片分类 */
    @Excel(name = "图片分类")
    private String categoryId;

    /** 图片标签 */
    @Excel(name = "图片标签")
    private String tags;

    /** 封面URL */
    @Excel(name = "封面URL")
    private String targetCover;

    /** 查看时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "查看时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


     /**
     * 对象转封装类
     *
     * @param userViewLogInfo UserViewLogInfo实体对象
     * @return UserViewLogInfoVo
     */
    public static UserViewLogInfoVo objToVo(UserViewLogInfo userViewLogInfo) {
        if (userViewLogInfo == null) {
            return null;
        }
        UserViewLogInfoVo userViewLogInfoVo = new UserViewLogInfoVo();
        BeanUtils.copyProperties(userViewLogInfo, userViewLogInfoVo);
        return userViewLogInfoVo;
    }
}
