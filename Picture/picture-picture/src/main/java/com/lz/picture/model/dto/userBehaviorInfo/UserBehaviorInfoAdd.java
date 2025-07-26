package com.lz.picture.model.dto.userBehaviorInfo;

import com.lz.picture.model.domain.UserBehaviorInfo;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户行为Vo对象 p_user_behavior_info
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
public class UserBehaviorInfoAdd implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 行为类型
     */
    @NotEmpty(message = "行为类型不能为空")
    private String behaviorType;


    /**
     * 目标类型
     */
    @NotEmpty(message = "目标类型不能为空")
    private String targetType;

    /**
     * 目标对象
     */
    @NotEmpty(message = "目标对象不能为空")
    private String targetId;

    private String shareLink;


    /**
     * 对象转封装类
     *
     * @param userBehaviorInfoInsert 插入对象
     * @return UserBehaviorInfoInsert
     */
    public static UserBehaviorInfo addToObj(UserBehaviorInfoAdd userBehaviorInfoInsert) {
        if (userBehaviorInfoInsert == null) {
            return null;
        }
        UserBehaviorInfo userBehaviorInfo = new UserBehaviorInfo();
        BeanUtils.copyProperties(userBehaviorInfoInsert, userBehaviorInfo);
        return userBehaviorInfo;
    }
}
