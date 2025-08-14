package com.lz.ai.model.vo.promptInfo;

import com.lz.ai.model.domain.PromptInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 提示词信息Vo对象 ai_prompt_info
 *
 * @author YY
 * @date 2025-08-14
 */
@Data
public class UserPromptInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private String infoId;
    /**
     * 名称
     */
    private String name;

    /**
     * 提示内容
     */
    private String content;


    /**
     * 对象转封装类
     *
     * @param promptInfo PromptInfo实体对象
     * @return PromptInfoVo
     */
    public static UserPromptInfoVo objToVo(PromptInfo promptInfo) {
        if (promptInfo == null) {
            return null;
        }
        UserPromptInfoVo promptInfoVo = new UserPromptInfoVo();
        BeanUtils.copyProperties(promptInfo, promptInfoVo);
        return promptInfoVo;
    }

    /**
     * 封装类转对象
     *
     * @param promptInfoList PromptInfoVo封装类
     * @return PromptInfo
     */
    public static List<UserPromptInfoVo> objToVo(List<PromptInfo> promptInfoList) {
        return promptInfoList.stream().map(UserPromptInfoVo::objToVo).toList();
    }
}
