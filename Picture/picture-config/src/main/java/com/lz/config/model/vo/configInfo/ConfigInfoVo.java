package com.lz.config.model.vo.configInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.ConfigInfo;
/**
 * 配置信息Vo对象 c_config_info
 *
 * @author YY
 * @date 2025-02-28
 */
@Data
public class ConfigInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Excel(name = "编号")
    private Long configId;

    /** 配置名称 */
    @Excel(name = "配置名称")
    private String configName;

    /** 配置键名 */
    @Excel(name = "配置键名")
    private String configKey;

    /** 配置键值 */
    @Excel(name = "配置键值")
    private String configValue;

    /** 配置类型（1值 2文件） */
    @Excel(name = "配置类型", readConverterExp = "1=值,2=文件")
    private String configType;

    /** 配置排序 */
    @Excel(name = "配置排序")
    private Long orderNum;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /** 更新人 */
    @Excel(name = "更新人")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;


     /**
     * 对象转封装类
     *
     * @param configInfo ConfigInfo实体对象
     * @return ConfigInfoVo
     */
    public static ConfigInfoVo objToVo(ConfigInfo configInfo) {
        if (configInfo == null) {
            return null;
        }
        ConfigInfoVo configInfoVo = new ConfigInfoVo();
        BeanUtils.copyProperties(configInfo, configInfoVo);
        return configInfoVo;
    }
}
