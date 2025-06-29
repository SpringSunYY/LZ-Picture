package com.lz.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.common.core.page.TableDataInfo;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoQuery;
import com.lz.picture.model.dto.spaceInfo.UserSpaceInfoQuery;
import com.lz.picture.model.dto.spaceInfo.UserTeamSpaceInfoQuery;
import com.lz.picture.model.vo.spaceInfo.SpaceInfoVo;

import java.util.List;

/**
 * 空间信息Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface ISpaceInfoService extends IService<SpaceInfo> {
    //region mybatis代码

    /**
     * 查询空间信息
     *
     * @param spaceId 空间信息主键
     * @return 空间信息
     */
    public SpaceInfo selectSpaceInfoBySpaceId(String spaceId);

    /**
     * 查询空间信息列表
     *
     * @param spaceInfo 空间信息
     * @return 空间信息集合
     */
    public List<SpaceInfo> selectSpaceInfoList(SpaceInfo spaceInfo);

    /**
     * 新增空间信息
     *
     * @param spaceInfo 空间信息
     * @return 结果
     */
    public int insertSpaceInfo(SpaceInfo spaceInfo);

    /**
     * 修改空间信息
     *
     * @param spaceInfo 空间信息
     * @return 结果
     */
    public int updateSpaceInfo(SpaceInfo spaceInfo);

    /**
     * 批量删除空间信息
     *
     * @param spaceIds 需要删除的空间信息主键集合
     * @return 结果
     */
    public int deleteSpaceInfoBySpaceIds(String[] spaceIds);

    /**
     * 删除空间信息信息
     *
     * @param spaceId 空间信息主键
     * @return 结果
     */
    public int deleteSpaceInfoBySpaceId(String spaceId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param spaceInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<SpaceInfo> getQueryWrapper(SpaceInfoQuery spaceInfoQuery);

    /**
     * 转换vo
     *
     * @param spaceInfoList SpaceInfo集合
     * @return SpaceInfoVO集合
     */
    List<SpaceInfoVo> convertVoList(List<SpaceInfo> spaceInfoList);

    /**
     * 获取正常的空间未删除信息
     *
     * @param spaceId
     * @return SpaceInfo
     * @author: YY
     * @method: selectNormalSpaceInfoByUserId
     * @date: 2025/6/28 18:59
     **/
    SpaceInfo selectNormalSpaceInfoByUserId(String spaceId);

    /**
     * description: 创建空间信息
     * author: YY
     * method: userInsertSpaceInfo
     * date: 2025/3/30 21:53
     * param:
     * param: spaceInfo
     * return: int
     **/
    int userInsertSpaceInfo(SpaceInfo spaceInfo);

    /**
     * description: 用户更新空间信息
     * author: YY
     * method: userUpdateSpaceInfo
     * date: 2025/4/4 22:48
     * param:
     * param: spaceInfo
     * return: int
     **/
    int userUpdateSpaceInfo(SpaceInfo spaceInfo);

    /**
     * 获取用户空间信息列表
     *
     * @param userSpaceInfoQuery
     * @return TableDataInfo
     * @author: YY
     * @method: listSpaceInfoTable
     * @date: 2025/6/28 16:59
     **/
    TableDataInfo listSpaceInfoTable(UserSpaceInfoQuery userSpaceInfoQuery);

    /**
     * 删除空间表格缓存
     *
     * @param userId 用户编号
     * @return void
     * @author: YY
     * @method: deleteSpaceTableCacheByUserId
     * @date: 2025/6/28 17:28
     **/
    void deleteSpacePersonalTableCacheByUserId(String userId);

    /**
     * 获取团队空间表格缓存
     *
     * @param userTeamSpaceInfoQuery
     * @return TableDataInfo
     * @author: YY
     * @method: listTeamSpaceInfoTable
     * @date: 2025/6/29 16:51
     **/
    TableDataInfo listTeamSpaceInfoTable(UserTeamSpaceInfoQuery userTeamSpaceInfoQuery);

    /**
     * 删除团队空间表格缓存
     *
     * @param userId
     * @return void
     * @author: YY
     * @method: deleteSpaceTeamTableCacheByUserId
     * @date: 2025/6/29 18:59
     **/
    void deleteSpaceTeamTableCacheByUserId(String userId);
}
