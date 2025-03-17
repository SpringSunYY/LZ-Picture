package com.lz.user.service;

import java.util.List;
import com.lz.user.model.domain.BannedPermissionInfo;
import com.lz.user.model.vo.bannedPermissionInfo.BannedPermissionInfoVo;
import com.lz.user.model.dto.bannedPermissionInfo.BannedPermissionInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户封禁权限Service接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface IBannedPermissionInfoService extends IService<BannedPermissionInfo>
{
    //region mybatis代码
    /**
     * 查询用户封禁权限
     *
     * @param bannedId 用户封禁权限主键
     * @return 用户封禁权限
     */
    public BannedPermissionInfo selectBannedPermissionInfoByBannedId(String bannedId);

    /**
     * 查询用户封禁权限列表
     *
     * @param bannedPermissionInfo 用户封禁权限
     * @return 用户封禁权限集合
     */
    public List<BannedPermissionInfo> selectBannedPermissionInfoList(BannedPermissionInfo bannedPermissionInfo);

    /**
     * 新增用户封禁权限
     *
     * @param bannedPermissionInfo 用户封禁权限
     * @return 结果
     */
    public int insertBannedPermissionInfo(BannedPermissionInfo bannedPermissionInfo);

    /**
     * 修改用户封禁权限
     *
     * @param bannedPermissionInfo 用户封禁权限
     * @return 结果
     */
    public int updateBannedPermissionInfo(BannedPermissionInfo bannedPermissionInfo);

    /**
     * 批量删除用户封禁权限
     *
     * @param bannedIds 需要删除的用户封禁权限主键集合
     * @return 结果
     */
    public int deleteBannedPermissionInfoByBannedIds(String[] bannedIds);

    /**
     * 删除用户封禁权限信息
     *
     * @param bannedId 用户封禁权限主键
     * @return 结果
     */
    public int deleteBannedPermissionInfoByBannedId(String bannedId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param bannedPermissionInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<BannedPermissionInfo> getQueryWrapper(BannedPermissionInfoQuery bannedPermissionInfoQuery);

    /**
     * 转换vo
     *
     * @param bannedPermissionInfoList BannedPermissionInfo集合
     * @return BannedPermissionInfoVO集合
     */
    List<BannedPermissionInfoVo> convertVoList(List<BannedPermissionInfo> bannedPermissionInfoList);
}
