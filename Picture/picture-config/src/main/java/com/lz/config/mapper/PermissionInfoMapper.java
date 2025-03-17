package com.lz.config.mapper;

import java.util.List;
import com.lz.config.model.domain.PermissionInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 权限信息Mapper接口
 *
 * @author YY
 * @date 2025-02-28
 */
public interface PermissionInfoMapper extends BaseMapper<PermissionInfo>
{
    /**
     * 查询权限信息
     *
     * @param permissionId 权限信息主键
     * @return 权限信息
     */
    public PermissionInfo selectPermissionInfoByPermissionId(Long permissionId);

    /**
     * 查询权限信息列表
     *
     * @param permissionInfo 权限信息
     * @return 权限信息集合
     */
    public List<PermissionInfo> selectPermissionInfoList(PermissionInfo permissionInfo);

    /**
     * 新增权限信息
     *
     * @param permissionInfo 权限信息
     * @return 结果
     */
    public int insertPermissionInfo(PermissionInfo permissionInfo);

    /**
     * 修改权限信息
     *
     * @param permissionInfo 权限信息
     * @return 结果
     */
    public int updatePermissionInfo(PermissionInfo permissionInfo);

    /**
     * 删除权限信息
     *
     * @param permissionId 权限信息主键
     * @return 结果
     */
    public int deletePermissionInfoByPermissionId(Long permissionId);

    /**
     * 批量删除权限信息
     *
     * @param permissionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePermissionInfoByPermissionIds(Long[] permissionIds);
}
