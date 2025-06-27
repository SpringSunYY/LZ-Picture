package com.lz.picture.model.dto.spaceDilatationInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.SpaceDilatationInfo;
/**
 * 空间扩容信息Query对象 p_space_dilatation_info
 *
 * @author YY
 * @date 2025-06-28
 */
@Data
public class SpaceDilatationInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 申请编号 */
    private String dilatationId;

    /** 扩容KEY */
    private String dilatationKey;

    /** 空间编号 */
    private String spaceId;

    /** 空间名称 */
    private String spaceName;

    /** 扩容类型 */
    private String dilatationType;

    /** 消耗积分 */
    private Long pointsTotal;

    /** 用户 */
    private String userId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param spaceDilatationInfoQuery 查询对象
     * @return SpaceDilatationInfo
     */
    public static SpaceDilatationInfo queryToObj(SpaceDilatationInfoQuery spaceDilatationInfoQuery) {
        if (spaceDilatationInfoQuery == null) {
            return null;
        }
        SpaceDilatationInfo spaceDilatationInfo = new SpaceDilatationInfo();
        BeanUtils.copyProperties(spaceDilatationInfoQuery, spaceDilatationInfo);
        return spaceDilatationInfo;
    }
}
