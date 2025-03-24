package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.SpaceFolderInfo;
import com.lz.picture.model.vo.spaceFolderInfo.SpaceFolderInfoVo;
import com.lz.picture.model.dto.spaceFolderInfo.SpaceFolderInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 空间文件夹Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface ISpaceFolderInfoService extends IService<SpaceFolderInfo>
{
    //region mybatis代码
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
     * 批量删除空间文件夹
     *
     * @param folderIds 需要删除的空间文件夹主键集合
     * @return 结果
     */
    public int deleteSpaceFolderInfoByFolderIds(String[] folderIds);

    /**
     * 删除空间文件夹信息
     *
     * @param folderId 空间文件夹主键
     * @return 结果
     */
    public int deleteSpaceFolderInfoByFolderId(String folderId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param spaceFolderInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<SpaceFolderInfo> getQueryWrapper(SpaceFolderInfoQuery spaceFolderInfoQuery);

    /**
     * 转换vo
     *
     * @param spaceFolderInfoList SpaceFolderInfo集合
     * @return SpaceFolderInfoVO集合
     */
    List<SpaceFolderInfoVo> convertVoList(List<SpaceFolderInfo> spaceFolderInfoList);
}
