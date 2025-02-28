package com.lz.config.model.dto.configInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.ConfigInfo;
/**
 * 配置信息Query对象 c_config_info
 *
 * @author YY
 * @date 2025-02-28
 */
@Data
public class ConfigInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 配置名称 */
    private String configName;

    /** 配置键名 */
    private String configKey;

    /** 配置键值 */
    private String configValue;

    /** 配置类型（1值 2文件） */
    private String configType;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param configInfoQuery 查询对象
     * @return ConfigInfo
     */
    public static ConfigInfo queryToObj(ConfigInfoQuery configInfoQuery) {
        if (configInfoQuery == null) {
            return null;
        }
        ConfigInfo configInfo = new ConfigInfo();
        BeanUtils.copyProperties(configInfoQuery, configInfo);
        return configInfo;
    }
}
