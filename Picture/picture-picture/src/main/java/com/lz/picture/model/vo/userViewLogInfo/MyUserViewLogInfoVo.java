package com.lz.picture.model.vo.userViewLogInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.UserViewLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户浏览记录Vo对象 用户自己的浏览记录
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
public class MyUserViewLogInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @Excel(name = "记录编号")
    private String viewId;

    /**
     * 目标类型 字典类型 p_view_log_target_type
     */
    @Excel(name = "目标类型")
    private String targetType;

    /**
     * 目标对象
     */
    @Excel(name = "目标对象")
    private String targetId;

    /**
     * 目标内容
     */
    @Excel(name = "目标内容")
    private String targetContent;

    /**
     * 图片标签
     */
    @Excel(name = "图片标签")
    private String tags;

    /**
     * 封面
     */
    @Excel(name = "封面")
    private String targetCover;

    /**
     * 查看时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 对象转封装类
     *
     * @param userViewLogInfo UserViewLogInfo实体对象
     * @return UserViewLogInfoVo
     */
    public static MyUserViewLogInfoVo objToVo(UserViewLogInfo userViewLogInfo) {
        if (userViewLogInfo == null) {
            return null;
        }
        MyUserViewLogInfoVo userViewLogInfoVo = new MyUserViewLogInfoVo();
        BeanUtils.copyProperties(userViewLogInfo, userViewLogInfoVo);
        return userViewLogInfoVo;
    }

    /**
     * 封装类转对象列表
     *
     * @param userViewLogInfos UserViewLogInfoVo封装类
     * @return UserViewLogInfo
     */
    public static List<MyUserViewLogInfoVo> objToVo(List<UserViewLogInfo> userViewLogInfos) {
        return userViewLogInfos.stream().map(MyUserViewLogInfoVo::objToVo).toList();
    }
}
