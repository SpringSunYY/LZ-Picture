package com.lz.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.picture.model.domain.SpaceFolderInfo;
import com.lz.picture.model.dto.spaceFolderInfo.SpaceFolderInfoQuery;
import com.lz.picture.model.vo.spaceFolderInfo.SpaceFolderInfoVo;

import java.util.List;

/**
 * 空间文件夹Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface ISpaceFolderInfoService extends IService<SpaceFolderInfo> {
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

    /**
     * description: 用户新增文件夹
     * author: YY
     * method: userInsertSpaceFolderInfo
     * date: 2025/4/7 22:04
     * param:
     * param: spaceFolderInfo
     * return: int
     **/
    int userInsertSpaceFolderInfo(SpaceFolderInfo spaceFolderInfo);

    /**
     * description: 用户更新空间
     * author: YY
     * method: userUpdateSpaceFolderInfo
     * date: 2025/4/7 23:53
     * param:
     * param: spaceFolderInfo
     * return: int
     **/
    int userUpdateSpaceFolderInfo(SpaceFolderInfo spaceFolderInfo);

    /**
     * 用户查询文件夹
     *
     * @param folderId
     * @param userId
     * @return
     */
    SpaceFolderInfo selectUserSpaceFolderInfoByFolderId(String folderId, String userId);

    /**
     * 用户删除文件夹
     *
     * @param folderId 文件夹编号
     * @param userId   用户编号
     * @return
     */
    int deleteUserSpaceFolderInfoByFolderId(String folderId, String userId);
}
