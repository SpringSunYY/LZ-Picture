package com.lz.picture.model.dto.spaceFolderInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.SpaceFolderInfo;
/**
 * 空间文件夹Vo对象 p_space_folder_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SpaceFolderInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 文件夹编号 */
    private String folderId;

    /** 空间编号 */
    private String spaceId;

    /** 父文件夹编号 */
    private String parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 文件夹名称 */
    private String folderName;

    /** 完整路径（格式：/文件夹名1/文件夹名2/） */
    private String fullPath;

    /** 层级 */
    private String folderLevel;

    /** 创建人 */
    private String userId;

    /** 排序权重 */
    private Integer sortOrder;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param spaceFolderInfoEdit 编辑对象
     * @return SpaceFolderInfo
     */
    public static SpaceFolderInfo editToObj(SpaceFolderInfoEdit spaceFolderInfoEdit) {
        if (spaceFolderInfoEdit == null) {
            return null;
        }
        SpaceFolderInfo spaceFolderInfo = new SpaceFolderInfo();
        BeanUtils.copyProperties(spaceFolderInfoEdit, spaceFolderInfo);
        return spaceFolderInfo;
    }
}
