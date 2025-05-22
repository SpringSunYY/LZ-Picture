package com.lz.picture.model.vo.userBehaviorInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.UserBehaviorInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户行为Vo对象 p_user_behavior_info
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
public class MyUserBehaviorInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 行为编号
     */
    private String behaviorId;

    /**
     * 行为类型
     */
    private String behaviorType;

    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 目标对象
     */
    private String targetId;

    /**
     * 目标内容
     */
    private String targetContent;


    /**
     * 空间
     */
    private String spaceId;

    /**
     * 图片标签
     */
    private String tags;

    /**
     * 封面
     */
    private String targetCover;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 对象转封装类
     *
     * @param userBehaviorInfo UserBehaviorInfo实体对象
     * @return UserBehaviorInfoVo
     */
    public static MyUserBehaviorInfoVo objToVo(UserBehaviorInfo userBehaviorInfo) {
        if (userBehaviorInfo == null) {
            return null;
        }
        MyUserBehaviorInfoVo userBehaviorInfoVo = new MyUserBehaviorInfoVo();
        BeanUtils.copyProperties(userBehaviorInfo, userBehaviorInfoVo);
        return userBehaviorInfoVo;
    }

    /**
     * 对象列表转vo列表
     *
     * @param userBehaviorInfos 实体列表
     * @return List<MyUserBehaviorInfoVo>
     * @author: YY
     * @method: objToVo
     * @date: 2025/5/22 16:59
     **/
    public static List<MyUserBehaviorInfoVo> objToVo(List<UserBehaviorInfo> userBehaviorInfos) {
        return userBehaviorInfos.stream().map(MyUserBehaviorInfoVo::objToVo).collect(Collectors.toList());
    }
}
