package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.picture.mapper.SpaceFolderInfoMapper;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.SpaceFolderInfo;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.spaceFolderInfo.SpaceFolderInfoQuery;
import com.lz.picture.model.enums.PSpaceTypeEnum;
import com.lz.picture.model.vo.spaceFolderInfo.SpaceFolderInfoVo;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.ISpaceFolderInfoService;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.picture.utils.TreeUtils;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 空间文件夹Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class SpaceFolderInfoServiceImpl extends ServiceImpl<SpaceFolderInfoMapper, SpaceFolderInfo> implements ISpaceFolderInfoService {
    @Resource
    private SpaceFolderInfoMapper spaceFolderInfoMapper;

    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    @Lazy
    private IPictureInfoService pictureInfoService;

    //region mybatis代码

    /**
     * 查询空间文件夹
     *
     * @param folderId 空间文件夹主键
     * @return 空间文件夹
     */
    @Override
    public SpaceFolderInfo selectSpaceFolderInfoByFolderId(String folderId) {
        return spaceFolderInfoMapper.selectSpaceFolderInfoByFolderId(folderId);
    }

    /**
     * 查询空间文件夹列表
     *
     * @param spaceFolderInfo 空间文件夹
     * @return 空间文件夹
     */
    @Override
    public List<SpaceFolderInfo> selectSpaceFolderInfoList(SpaceFolderInfo spaceFolderInfo) {
        return spaceFolderInfoMapper.selectSpaceFolderInfoList(spaceFolderInfo);
    }

    /**
     * 新增空间文件夹
     *
     * @param spaceFolderInfo 空间文件夹
     * @return 结果
     */
    @Override
    public int insertSpaceFolderInfo(SpaceFolderInfo spaceFolderInfo) {
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
    public int updateSpaceFolderInfo(SpaceFolderInfo spaceFolderInfo) {
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
    public int deleteSpaceFolderInfoByFolderIds(String[] folderIds) {
        return spaceFolderInfoMapper.deleteSpaceFolderInfoByFolderIds(folderIds);
    }

    /**
     * 删除空间文件夹信息
     *
     * @param folderId 空间文件夹主键
     * @return 结果
     */
    @Override
    public int deleteSpaceFolderInfoByFolderId(String folderId) {
        //查询是否有下级
        SpaceFolderInfo spaceFolderInfo = new SpaceFolderInfo();
        spaceFolderInfo.setParentId(folderId);
        List<SpaceFolderInfo> spaceFolderInfos = spaceFolderInfoMapper.selectSpaceFolderInfoList(spaceFolderInfo);
        if (StringUtils.isNotEmpty(spaceFolderInfos)) {
            throw new ServiceException("存在下级文件夹，不允许删除");
        }
        //查询是否有图片
        List<PictureInfo> list = pictureInfoService.list(new LambdaQueryWrapper<PictureInfo>().eq(PictureInfo::getFolderId, folderId));
        if (StringUtils.isNotEmpty(list)) {
            throw new ServiceException("存在图片，不允许删除");
        }
        return spaceFolderInfoMapper.deleteSpaceFolderInfoByFolderId(folderId);
    }

    //endregion
    @Override
    public QueryWrapper<SpaceFolderInfo> getQueryWrapper(SpaceFolderInfoQuery spaceFolderInfoQuery) {
        QueryWrapper<SpaceFolderInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = spaceFolderInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String folderId = spaceFolderInfoQuery.getFolderId();
        queryWrapper.eq(StringUtils.isNotEmpty(folderId), "folder_id", folderId);

        String spaceId = spaceFolderInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId), "space_id", spaceId);

        String parentId = spaceFolderInfoQuery.getParentId();
        queryWrapper.eq(StringUtils.isNotEmpty(parentId), "parent_id", parentId);

        String folderName = spaceFolderInfoQuery.getFolderName();
        queryWrapper.like(StringUtils.isNotEmpty(folderName), "folder_name", folderName);

        String folderLevel = spaceFolderInfoQuery.getFolderLevel();
        queryWrapper.eq(StringUtils.isNotEmpty(folderLevel), "folder_level", folderLevel);

        String userId = spaceFolderInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        Date createTime = spaceFolderInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = spaceFolderInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<SpaceFolderInfoVo> convertVoList(List<SpaceFolderInfo> spaceFolderInfoList) {
        if (StringUtils.isEmpty(spaceFolderInfoList)) {
            return Collections.emptyList();
        }
        return spaceFolderInfoList.stream().map(SpaceFolderInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public int userInsertSpaceFolderInfo(SpaceFolderInfo spaceFolderInfo) {
        //查询空间是否存在
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(spaceFolderInfo.getSpaceId());
        if (StringUtils.isNull(spaceInfo)
                || spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_0.getValue())
                || !spaceInfo.getIsDelete().equals(CommonDeleteEnum.NORMAL.getValue())
                || !spaceInfo.getUserId().equals(spaceFolderInfo.getUserId())) {
            throw new ServiceException("空间为官方空间、不存在、或者已被删除，不可创建文件夹");
        }
        //查询空间+父级目录+文件夹名称是否已存在
        SpaceFolderInfo old = this.getOne(new LambdaQueryWrapper<SpaceFolderInfo>()
                .eq(SpaceFolderInfo::getSpaceId, spaceFolderInfo.getSpaceId())
                .eq(SpaceFolderInfo::getParentId, spaceFolderInfo.getParentId())
                .eq(SpaceFolderInfo::getFolderName, spaceFolderInfo.getFolderName()));
        if (StringUtils.isNotNull(old)) {
            throw new ServiceException("该文件夹已存在");
        }
        initSpaceFolderInfo(spaceFolderInfo);
        return this.save(spaceFolderInfo) ? 1 : 0;
    }

    private void initSpaceFolderInfo(SpaceFolderInfo spaceFolderInfo) {
        if (spaceFolderInfo.getParentId().equals("0")) {
            spaceFolderInfo.setFolderLevel("1");
        } else {
            //查询父级目录
            SpaceFolderInfo parentFolder = getSpaceFolderInfoByParentId(spaceFolderInfo.getParentId());
            //如果父级等级为7
            if (parentFolder.getFolderLevel().equals("7")) {
                throw new ServiceException("该文件夹已超过最大层级");
            }
            long level = (Long.parseLong(parentFolder.getFolderLevel())) + 1;
            spaceFolderInfo.setFolderLevel(String.valueOf(level));
        }
        //获取祖级列表
        StringBuilder ancestors = TreeUtils.getAncestors(spaceFolderInfo.getParentId(), new StringBuilder(), spaceFolderInfoMapper::selectSpaceFolderInfoByFolderId, SpaceFolderInfo::getFolderId, SpaceFolderInfo::getParentId, "0", ",");
        spaceFolderInfo.setAncestors(ancestors.toString());
        //获取路径快照
        StringBuilder fullPath = TreeUtils.getAncestors(spaceFolderInfo.getParentId(), new StringBuilder(spaceFolderInfo.getFolderName()), spaceFolderInfoMapper::selectSpaceFolderInfoByFolderId, SpaceFolderInfo::getFolderName, SpaceFolderInfo::getParentId, "根目录", "/");
        spaceFolderInfo.setFullPath(fullPath.toString());
        spaceFolderInfo.setCreateTime(DateUtils.getNowDate());
    }

    @Override
    public int userUpdateSpaceFolderInfo(SpaceFolderInfo spaceFolderInfo) {
        //查询文件夹是否已存在
        SpaceFolderInfo folderInfo = this.selectSpaceFolderInfoByFolderId(spaceFolderInfo.getFolderId());
        if (StringUtils.isNull(folderInfo)) {
            throw new ServiceException("该文件夹不存在");
        }
        //查询空间+父级目录+文件夹名称是否已存在
        SpaceFolderInfo old = this.getOne(new LambdaQueryWrapper<SpaceFolderInfo>()
                .eq(SpaceFolderInfo::getSpaceId, spaceFolderInfo.getSpaceId())
                .eq(SpaceFolderInfo::getParentId, spaceFolderInfo.getParentId())
                .eq(SpaceFolderInfo::getFolderName, spaceFolderInfo.getFolderName()));
        if (StringUtils.isNotNull(old) && !old.getFolderId().equals(spaceFolderInfo.getFolderId())) {
            throw new ServiceException("该文件夹已存在");
        }
        initSpaceFolderInfo(spaceFolderInfo);
        spaceFolderInfo.setUpdateTime(DateUtils.getNowDate());
        return spaceFolderInfoMapper.updateSpaceFolderInfo(spaceFolderInfo);
    }


    private SpaceFolderInfo getSpaceFolderInfoByParentId(String parentId) {
        return this.getOne(new LambdaQueryWrapper<SpaceFolderInfo>().eq(SpaceFolderInfo::getFolderId, parentId));
    }

}
