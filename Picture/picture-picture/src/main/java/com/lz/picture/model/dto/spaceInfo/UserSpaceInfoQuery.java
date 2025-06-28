
package com.lz.picture.model.dto.spaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.core.page.PageDomain;
import com.lz.picture.model.domain.SpaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 空间信息Query对象 p_space_info
 * 用户空间查询
 *
 * @author YY
 * @date 2025-03-24
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserSpaceInfoQuery extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 空间编号
     */
    private String spaceId;

    /**
     * 空间名称
     */
    private String spaceName;

    /**
     * 存储类型（0官方 阿里云）
     */
    private String ossType;

    /**
     * 所属用户
     */
    private String userId;

    /**
     * 空间状态
     */
    private String spaceStatus;

    /**
     * 空间类型（0个人 1团队 2官方）
     */
    private String spaceType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 最后上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastUpdateTime;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /**
     * 删除（0否 1是）
     */
    private String isDelete;


    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param spaceInfoQuery 查询对象
     * @return SpaceInfo
     */
    public static SpaceInfo queryToObj(UserSpaceInfoQuery spaceInfoQuery) {
        if (spaceInfoQuery == null) {
            return null;
        }
        SpaceInfo spaceInfo = new SpaceInfo();
        BeanUtils.copyProperties(spaceInfoQuery, spaceInfo);
        return spaceInfo;
    }
}
