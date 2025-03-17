package com.lz.config.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.lz.common.exception.sql.SQLDuplicateKeyException;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.config.mapper.PermissionInfoMapper;
import com.lz.config.model.domain.PermissionInfo;
import com.lz.config.service.IPermissionInfoService;
import com.lz.config.model.dto.permissionInfo.PermissionInfoQuery;
import com.lz.config.model.vo.permissionInfo.PermissionInfoVo;

/**
 * 权限信息Service业务层处理
 *
 * @author YY
 * @date 2025-02-28
 */
@Slf4j
@Service
public class PermissionInfoServiceImpl extends ServiceImpl<PermissionInfoMapper, PermissionInfo> implements IPermissionInfoService {
    @Resource
    private PermissionInfoMapper permissionInfoMapper;

    //region mybatis代码

    /**
     * 查询权限信息
     *
     * @param permissionId 权限信息主键
     * @return 权限信息
     */
    @Override
    public PermissionInfo selectPermissionInfoByPermissionId(Long permissionId) {
        return permissionInfoMapper.selectPermissionInfoByPermissionId(permissionId);
    }

    /**
     * 查询权限信息列表
     *
     * @param permissionInfo 权限信息
     * @return 权限信息
     */
    @Override
    public List<PermissionInfo> selectPermissionInfoList(PermissionInfo permissionInfo) {
        return permissionInfoMapper.selectPermissionInfoList(permissionInfo);
    }

    /**
     * 新增权限信息
     *
     * @param permissionInfo 权限信息
     * @return 结果
     */
    @Override
    public int insertPermissionInfo(PermissionInfo permissionInfo) {
        permissionInfo.setCreateBy(SecurityUtils.getUsername());
        permissionInfo.setCreateTime(DateUtils.getNowDate());
        try {
            return permissionInfoMapper.insertPermissionInfo(permissionInfo);
        } catch (Exception e) {
            log.error("添加权限信息失败：{}", e.getMessage(), e.getCause());
            throw new SQLDuplicateKeyException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 修改权限信息
     *
     * @param permissionInfo 权限信息
     * @return 结果
     */
    @Override
    public int updatePermissionInfo(PermissionInfo permissionInfo) {
        permissionInfo.setUpdateBy(SecurityUtils.getUsername());
        permissionInfo.setUpdateTime(DateUtils.getNowDate());
        try {
            return permissionInfoMapper.updatePermissionInfo(permissionInfo);
        } catch (Exception e) {
            log.error("修改权限信息失败：{}", e.getMessage(), e.getCause());
            throw new SQLDuplicateKeyException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 批量删除权限信息
     *
     * @param permissionIds 需要删除的权限信息主键
     * @return 结果
     */
    @Override
    public int deletePermissionInfoByPermissionIds(Long[] permissionIds) {
        return permissionInfoMapper.deletePermissionInfoByPermissionIds(permissionIds);
    }

    /**
     * 删除权限信息信息
     *
     * @param permissionId 权限信息主键
     * @return 结果
     */
    @Override
    public int deletePermissionInfoByPermissionId(Long permissionId) {
        return permissionInfoMapper.deletePermissionInfoByPermissionId(permissionId);
    }

    //endregion
    @Override
    public QueryWrapper<PermissionInfo> getQueryWrapper(PermissionInfoQuery permissionInfoQuery) {
        QueryWrapper<PermissionInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = permissionInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String permissionName = permissionInfoQuery.getPermissionName();
        queryWrapper.like(StringUtils.isNotEmpty(permissionName), "permission_name", permissionName);

        String parentId = permissionInfoQuery.getParentId();
        queryWrapper.eq(StringUtils.isNotEmpty(parentId), "parent_id", parentId);

        String status = permissionInfoQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(status), "status", status);

        String createBy = permissionInfoQuery.getCreateBy();
        queryWrapper.like(StringUtils.isNotEmpty(createBy), "create_by", createBy);

        Date createTime = permissionInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String updateBy = permissionInfoQuery.getUpdateBy();
        queryWrapper.like(StringUtils.isNotEmpty(updateBy), "update_by", updateBy);

        Date updateTime = permissionInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<PermissionInfoVo> convertVoList(List<PermissionInfo> permissionInfoList) {
        if (StringUtils.isEmpty(permissionInfoList)) {
            return Collections.emptyList();
        }
        return permissionInfoList.stream().map(PermissionInfoVo::objToVo).collect(Collectors.toList());
    }

}
