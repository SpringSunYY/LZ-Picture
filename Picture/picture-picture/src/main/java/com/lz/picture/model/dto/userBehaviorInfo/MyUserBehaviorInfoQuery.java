package com.lz.picture.model.dto.userBehaviorInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.core.page.PageDomain;
import com.lz.picture.model.domain.UserBehaviorInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户查询自己行为信息查询参数
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
public class MyUserBehaviorInfoQuery extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 行为类型
     */
    private String behaviorType;


    /**
     * 目标类型
     */
    private String targetType;


    /**
     * 目标内容
     */
    private String targetContent;


    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param userBehaviorInfoQuery 查询对象
     * @return UserBehaviorInfo
     */
    public static UserBehaviorInfo queryToObj(MyUserBehaviorInfoQuery userBehaviorInfoQuery) {
        if (userBehaviorInfoQuery == null) {
            return null;
        }
        UserBehaviorInfo userBehaviorInfo = new UserBehaviorInfo();
        BeanUtils.copyProperties(userBehaviorInfoQuery, userBehaviorInfo);
        return userBehaviorInfo;
    }
}
