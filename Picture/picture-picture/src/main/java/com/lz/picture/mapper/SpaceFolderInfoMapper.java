package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.SpaceFolderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 空间文件夹Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface SpaceFolderInfoMapper extends BaseMapper<SpaceFolderInfo>
{
    /**
     * 查询空间文件夹
     *
     * @param folderId 空间文件夹主键
     * @return 空间文件夹
     */
    public SpaceFolderInfo selectSpaceFolderInfoByFolderId(String folderId);

    /**
     * 查询空间文件夹列表
     *
     * @param spaceFolderInfo 空间文件夹
     * @return 空间文件夹集合
     */
    public List<SpaceFolderInfo> selectSpaceFolderInfoList(SpaceFolderInfo spaceFolderInfo);

    /**
     * 新增空间文件夹
     *
     * @param spaceFolderInfo 空间文件夹
     * @return 结果
     */
    public int insertSpaceFolderInfo(SpaceFolderInfo spaceFolderInfo);

    /**
     * 修改空间文件夹
     *
     * @param spaceFolderInfo 空间文件夹
     * @return 结果
     */
    public int updateSpaceFolderInfo(SpaceFolderInfo spaceFolderInfo);

    /**
     * 删除空间文件夹
     *
     * @param folderId 空间文件夹主键
     * @return 结果
     */
    public int deleteSpaceFolderInfoByFolderId(String folderId);

    /**
     * 批量删除空间文件夹
     *
     * @param folderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpaceFolderInfoByFolderIds(String[] folderIds);
}
