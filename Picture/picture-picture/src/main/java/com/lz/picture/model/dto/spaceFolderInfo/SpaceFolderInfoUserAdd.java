package com.lz.picture.model.dto.spaceFolderInfo;

import com.lz.picture.model.domain.SpaceFolderInfo;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 空间文件夹Vo对象 p_space_folder_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SpaceFolderInfoUserAdd implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 空间编号
     */
    @NotEmpty(message = "空间编号不能为空")
    private String spaceId;

    /**
     * 父文件夹编号
     */
    @NotEmpty(message = "父文件夹编号不能为空")
    private String parentId;


    /**
     * 文件夹名称
     */
    @NotEmpty(message = "文件夹名称不能为空")
    @Size(max = 32, message = "文件夹名称不能超过32个字符")
    private String folderName;


    /**
     * 排序权重
     */
    @NotNull(message = "排序权重不能为空")
    @Max(value = 10, message = "排序权重不能超过10")
    @Min(value = 0, message = "排序权重不能小于0")
    private Integer sortOrder;

    /**
     * 备注
     */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param spaceFolderInfoInsert 插入对象
     * @return SpaceFolderInfoInsert
     */
    public static SpaceFolderInfo addToObj(SpaceFolderInfoUserAdd spaceFolderInfoInsert) {
        if (spaceFolderInfoInsert == null) {
            return null;
        }
        SpaceFolderInfo spaceFolderInfo = new SpaceFolderInfo();
        BeanUtils.copyProperties(spaceFolderInfoInsert, spaceFolderInfo);
        return spaceFolderInfo;
    }
}
