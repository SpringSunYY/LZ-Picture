package com.lz.config.model.dto.configInfo;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.config.model.domain.ConfigInfo;
/**
 * 配置信息Vo对象 c_config_info
 *
 * @author YY
 * @date 2025-02-28
 */
@Data
public class ConfigInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long configId;

    /** 配置名称 */
    @NotBlank(message = "配置名称不能为空")
    private String configName;

    /** 配置键名 */
    @NotBlank(message = "配置键名不能为空")
    private String configKey;

    /** 配置键值 */
    @NotBlank(message = "配置键值不能为空")
    private String configValue;

    /** 配置类型（1值 2文件） */
    @NotBlank(message = "配置类型不能为空")
    private String configType;

    /** 配置排序 */
    private Long orderNum;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param configInfoInsert 插入对象
     * @return ConfigInfoInsert
     */
    public static ConfigInfo insertToObj(ConfigInfoInsert configInfoInsert) {
        if (configInfoInsert == null) {
            return null;
        }
        ConfigInfo configInfo = new ConfigInfo();
        BeanUtils.copyProperties(configInfoInsert, configInfo);
        return configInfo;
    }
}
