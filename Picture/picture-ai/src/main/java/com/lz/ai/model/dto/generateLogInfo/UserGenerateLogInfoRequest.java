package com.lz.ai.model.dto.generateLogInfo;

import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.common.core.page.PageDomain;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户生成记录
 * 理想是前行的舟船，现在就是小舟
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class UserGenerateLogInfoRequest extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String userId;


    /**
     * 对象转封装类
     *
     * @param generateLogInfoInsert 插入对象
     * @return GenerateLogInfoInsert
     */
    public static GenerateLogInfo insertToObj(UserGenerateLogInfoRequest generateLogInfoInsert) {
        if (generateLogInfoInsert == null) {
            return null;
        }
        GenerateLogInfo generateLogInfo = new GenerateLogInfo();
        BeanUtils.copyProperties(generateLogInfoInsert, generateLogInfo);
        return generateLogInfo;
    }
}
