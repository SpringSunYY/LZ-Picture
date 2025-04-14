package com.lz.picture.model.vo.userBehaviorInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.UserBehaviorInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户行为Vo对象 p_user_behavior_info
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
public class UserBehaviorInfoStaticVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 行为类型
     */
    private String behaviorType;

    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 目标类型行为总数
     */
    private Long targetTypeCount;
}
