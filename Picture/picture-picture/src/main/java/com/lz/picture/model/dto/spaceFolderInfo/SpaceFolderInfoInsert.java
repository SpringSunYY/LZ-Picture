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
public class SpaceFolderInfoInsert implements Serializable
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
    private String sortOrder;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param spaceFolderInfoInsert 插入对象
     * @return SpaceFolderInfoInsert
     */
    public static SpaceFolderInfo insertToObj(SpaceFolderInfoInsert spaceFolderInfoInsert) {
        if (spaceFolderInfoInsert == null) {
            return null;
        }
        SpaceFolderInfo spaceFolderInfo = new SpaceFolderInfo();
        BeanUtils.copyProperties(spaceFolderInfoInsert, spaceFolderInfo);
        return spaceFolderInfo;
    }
}
