package com.lz.picture.model.dto.userViewLogInfo;

import lombok.Data;

/**
 * 用户浏览记录目标信息 用于日志记录实体
 */
@Data
public class UserViewLogTargetInfoDto {
    private String targetId;
    private String targetContent;
    private String categoryId;
    private String targetCover;
    private String spaceId;
    private String tags;
}
