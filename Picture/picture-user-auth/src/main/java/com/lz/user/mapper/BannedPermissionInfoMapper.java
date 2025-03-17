package com.lz.user.mapper;

import java.util.List;
import com.lz.user.model.domain.BannedPermissionInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户封禁权限Mapper接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface BannedPermissionInfoMapper extends BaseMapper<BannedPermissionInfo>
{
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
     * 删除用户封禁权限
     *
     * @param bannedId 用户封禁权限主键
     * @return 结果
     */
    public int deleteBannedPermissionInfoByBannedId(String bannedId);

    /**
     * 批量删除用户封禁权限
     *
     * @param bannedIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBannedPermissionInfoByBannedIds(String[] bannedIds);
}
