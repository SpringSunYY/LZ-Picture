package com.lz.picture.model.dto.spaceFolderInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.SpaceFolderInfo;
/**
 * 空间文件夹Query对象 p_space_folder_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SpaceFolderInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 文件夹编号 */
    private String folderId;

    /** 空间编号 */
    private String spaceId;

    /** 父文件夹编号 */
    private String parentId;

    /** 文件夹名称 */
    private String folderName;

    /** 层级 */
    private String folderLevel;

    /** 创建人 */
    private String userId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param spaceFolderInfoQuery 查询对象
     * @return SpaceFolderInfo
     */
    public static SpaceFolderInfo queryToObj(SpaceFolderInfoQuery spaceFolderInfoQuery) {
        if (spaceFolderInfoQuery == null) {
            return null;
        }
        SpaceFolderInfo spaceFolderInfo = new SpaceFolderInfo();
        BeanUtils.copyProperties(spaceFolderInfoQuery, spaceFolderInfo);
        return spaceFolderInfo;
    }
}
