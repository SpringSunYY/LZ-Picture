package com.lz.config.model.vo.configInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.config.model.domain.ConfigInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 配置信息Vo对象 c_config_info
 *
 * @author YY
 * @date 2025-02-28
 */
@Data
public class UserConfigInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置键值
     */
    private String configValue;

    /**
     * 对象转封装类
     *
     * @param configInfo ConfigInfo实体对象
     * @return ConfigInfoVo
     */
    public static UserConfigInfoVo objToVo(ConfigInfo configInfo) {
        if (configInfo == null) {
            return null;
        }
        UserConfigInfoVo configInfoVo = new UserConfigInfoVo();
        BeanUtils.copyProperties(configInfo, configInfoVo);
        return configInfoVo;
    }
}
