package com.lz.picture.model.dto.spaceInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.SpaceInfo;
/**
 * 空间信息Vo对象 p_space_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SpaceInfoInsert implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    /** 空间编号 */
    private String spaceId;

    /** 空间名称 */
    private String spaceName;

    /** 空间封面URL */
    private String spaceAvatar;

    /** 存储类型（0官方 阿里云） */
    private String ossType;

    /** 存储配置 */
    private String ossConfig;

    /** 最大容量（字节） */
    private Long maxSize;

    /** 最大文件数 */
    private Long maxCount;

    /** 已用容量（字节） */
    private Long totalSize;

    /** 文件总数 */
    private Long totalCount;

    /** 所属用户 */
    private String userId;

    /** 空间描述 */
    private String spaceDesc;

    /** 空间状态 */
    private String spaceStatus;

    /** 空间类型（0个人 1团队 2官方） */
    private String spaceType;

    /** 成员上限 */
    private Long memberLimit;

    /** 当前成员数 */
    private Long currentMembers;

    /** 最后上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastUpdateTime;

    /** 删除（0否 1是） */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param spaceInfoInsert 插入对象
     * @return SpaceInfoInsert
     */
    public static SpaceInfo insertToObj(SpaceInfoInsert spaceInfoInsert) {
        if (spaceInfoInsert == null) {
            return null;
        }
        SpaceInfo spaceInfo = new SpaceInfo();
        BeanUtils.copyProperties(spaceInfoInsert, spaceInfo);
        return spaceInfo;
    }
}
