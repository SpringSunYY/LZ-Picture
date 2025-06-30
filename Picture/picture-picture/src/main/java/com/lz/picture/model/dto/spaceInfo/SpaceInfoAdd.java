package com.lz.picture.model.dto.spaceInfo;

import com.lz.picture.model.domain.SpaceInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * Project: Picture
 * Package: com.lz.picture.model.dto.spaceInfo
 * Author: YY
 * CreateTime: 2025-03-30  21:45
 * Description: SpaceInfoAdd
 * 空间新增
 * Version: 1.0
 */
@Data
public class SpaceInfoAdd implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 空间名称
     */
    @NotEmpty(message = "空间名称不能为空")
    @Size(max = 32, message = "空间名称不能超过32个字符")
    private String spaceName;

    /**
     * 空间封面URL
     */
    private String spaceAvatar;


    /**
     * 所属用户
     */
    private String userId;

    /**
     * 空间描述
     */
    @Size(max = 512, message = "空间描述不能超过512个字符")
    private String spaceDesc;

    /**
     * 空间状态
     */
    @NotEmpty(message = "请选择空间状态")
    private String spaceStatus;

    /**
     * 空间类型（0个人 1团队 2官方）
     */
    @NotEmpty(message = "请选择空间类型")
    private String spaceType;


    /**
     * 对象转封装类
     *
     * @param spaceInfoEdit 编辑对象
     * @return SpaceInfo
     */
    public static SpaceInfo addToObj(SpaceInfoAdd spaceInfoEdit) {
        if (spaceInfoEdit == null) {
            return null;
        }
        SpaceInfo spaceInfo = new SpaceInfo();
        BeanUtils.copyProperties(spaceInfoEdit, spaceInfo);
        return spaceInfo;
    }
}
