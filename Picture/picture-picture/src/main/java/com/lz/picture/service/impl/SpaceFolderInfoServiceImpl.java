package com.lz.picture.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.mapper.SpaceFolderInfoMapper;
import com.lz.picture.model.domain.SpaceFolderInfo;
import com.lz.picture.service.ISpaceFolderInfoService;
import com.lz.picture.model.dto.spaceFolderInfo.SpaceFolderInfoQuery;
import com.lz.picture.model.vo.spaceFolderInfo.SpaceFolderInfoVo;

/**
 * 空间文件夹Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class SpaceFolderInfoServiceImpl extends ServiceImpl<SpaceFolderInfoMapper, SpaceFolderInfo> implements ISpaceFolderInfoService
{
    @Resource
    private SpaceFolderInfoMapper spaceFolderInfoMapper;

    //region mybatis代码
    /**
     * 查询空间文件夹
     *
     * @param folderId 空间文件夹主键
     * @return 空间文件夹
     */
    @Override
    public SpaceFolderInfo selectSpaceFolderInfoByFolderId(String folderId)
    {
        return spaceFolderInfoMapper.selectSpaceFolderInfoByFolderId(folderId);
    }

    /**
     * 查询空间文件夹列表
     *
     * @param spaceFolderInfo 空间文件夹
     * @return 空间文件夹
     */
    @Override
    public List<SpaceFolderInfo> selectSpaceFolderInfoList(SpaceFolderInfo spaceFolderInfo)
    {
        return spaceFolderInfoMapper.selectSpaceFolderInfoList(spaceFolderInfo);
    }

    /**
     * 新增空间文件夹
     *
     * @param spaceFolderInfo 空间文件夹
     * @return 结果
     */
    @Override
    public int insertSpaceFolderInfo(SpaceFolderInfo spaceFolderInfo)
    {
        spaceFolderInfo.setCreateTime(DateUtils.getNowDate());
        return spaceFolderInfoMapper.insertSpaceFolderInfo(spaceFolderInfo);
    }

    /**
     * 修改空间文件夹
     *
     * @param spaceFolderInfo 空间文件夹
     * @return 结果
     */
    @Override
    public int updateSpaceFolderInfo(SpaceFolderInfo spaceFolderInfo)
    {
      spaceFolderInfo.setUpdateTime(DateUtils.getNowDate());
        return spaceFolderInfoMapper.updateSpaceFolderInfo(spaceFolderInfo);
    }

    /**
     * 批量删除空间文件夹
     *
     * @param folderIds 需要删除的空间文件夹主键
     * @return 结果
     */
    @Override
    public int deleteSpaceFolderInfoByFolderIds(String[] folderIds)
    {
        return spaceFolderInfoMapper.deleteSpaceFolderInfoByFolderIds(folderIds);
    }

    /**
     * 删除空间文件夹信息
     *
     * @param folderId 空间文件夹主键
     * @return 结果
     */
    @Override
    public int deleteSpaceFolderInfoByFolderId(String folderId)
    {
        return spaceFolderInfoMapper.deleteSpaceFolderInfoByFolderId(folderId);
    }
    //endregion
    @Override
    public QueryWrapper<SpaceFolderInfo> getQueryWrapper(SpaceFolderInfoQuery spaceFolderInfoQuery){
        QueryWrapper<SpaceFolderInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = spaceFolderInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String folderId = spaceFolderInfoQuery.getFolderId();
        queryWrapper.eq(StringUtils.isNotEmpty(folderId) ,"folder_id",folderId);

    String spaceId = spaceFolderInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId) ,"space_id",spaceId);

    String parentId = spaceFolderInfoQuery.getParentId();
        queryWrapper.eq(StringUtils.isNotEmpty(parentId) ,"parent_id",parentId);

    String folderName = spaceFolderInfoQuery.getFolderName();
        queryWrapper.like(StringUtils.isNotEmpty(folderName) ,"folder_name",folderName);

    String folderLevel = spaceFolderInfoQuery.getFolderLevel();
        queryWrapper.eq(StringUtils.isNotEmpty(folderLevel) ,"folder_level",folderLevel);

    String userId = spaceFolderInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    Date createTime = spaceFolderInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    Date updateTime = spaceFolderInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime"))&&StringUtils.isNotNull(params.get("endUpdateTime")),"update_time",params.get("beginUpdateTime"),params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<SpaceFolderInfoVo> convertVoList(List<SpaceFolderInfo> spaceFolderInfoList) {
        if (StringUtils.isEmpty(spaceFolderInfoList)) {
            return Collections.emptyList();
        }
        return spaceFolderInfoList.stream().map(SpaceFolderInfoVo::objToVo).collect(Collectors.toList());
    }

}
