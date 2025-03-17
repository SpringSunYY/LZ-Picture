package com.lz.config.service;

import java.util.List;
import com.lz.config.model.domain.PermissionInfo;
import com.lz.config.model.vo.permissionInfo.PermissionInfoVo;
import com.lz.config.model.dto.permissionInfo.PermissionInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 权限信息Service接口
 *
 * @author YY
 * @date 2025-02-28
 */
public interface IPermissionInfoService extends IService<PermissionInfo>
{
    //region mybatis代码
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
     * 批量删除权限信息
     *
     * @param permissionIds 需要删除的权限信息主键集合
     * @return 结果
     */
    public int deletePermissionInfoByPermissionIds(Long[] permissionIds);

    /**
     * 删除权限信息信息
     *
     * @param permissionId 权限信息主键
     * @return 结果
     */
    public int deletePermissionInfoByPermissionId(Long permissionId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param permissionInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PermissionInfo> getQueryWrapper(PermissionInfoQuery permissionInfoQuery);

    /**
     * 转换vo
     *
     * @param permissionInfoList PermissionInfo集合
     * @return PermissionInfoVO集合
     */
    List<PermissionInfoVo> convertVoList(List<PermissionInfo> permissionInfoList);
}
