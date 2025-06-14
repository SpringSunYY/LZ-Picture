package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureFileLogAsyncFactory;
import com.lz.picture.mapper.SpaceInfoMapper;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoQuery;
import com.lz.picture.model.enums.PSpaceOssTypeEnum;
import com.lz.picture.model.enums.PSpaceTypeEnum;
import com.lz.picture.model.vo.spaceInfo.SpaceInfoVo;
import com.lz.picture.service.ISpaceInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.config.UserConfigKeyConstants.*;

/**
 * 空间信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class SpaceInfoServiceImpl extends ServiceImpl<SpaceInfoMapper, SpaceInfo> implements ISpaceInfoService {
    @Resource
    private SpaceInfoMapper spaceInfoMapper;

    @Resource
    private IConfigInfoService configInfoService;

    //region mybatis代码

    /**
     * 查询空间信息
     *
     * @param spaceId 空间信息主键
     * @return 空间信息
     */
    @Override
    public SpaceInfo selectSpaceInfoBySpaceId(String spaceId) {
        return spaceInfoMapper.selectSpaceInfoBySpaceId(spaceId);
    }

    /**
     * 查询空间信息列表
     *
     * @param spaceInfo 空间信息
     * @return 空间信息
     */
    @Override
    public List<SpaceInfo> selectSpaceInfoList(SpaceInfo spaceInfo) {
        return spaceInfoMapper.selectSpaceInfoList(spaceInfo);
    }

    /**
     * 新增空间信息
     *
     * @param spaceInfo 空间信息
     * @return 结果
     */
    @Override
    public int insertSpaceInfo(SpaceInfo spaceInfo) {
        spaceInfo.setSpaceId(IdUtils.snowflakeId().toString());
        spaceInfo.setCreateTime(DateUtils.getNowDate());
        return spaceInfoMapper.insertSpaceInfo(spaceInfo);
    }

    /**
     * 修改空间信息
     *
     * @param spaceInfo 空间信息
     * @return 结果
     */
    @Override
    public int updateSpaceInfo(SpaceInfo spaceInfo) {
        spaceInfo.setUpdateTime(DateUtils.getNowDate());
        return spaceInfoMapper.updateSpaceInfo(spaceInfo);
    }

    /**
     * 批量删除空间信息
     *
     * @param spaceIds 需要删除的空间信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceInfoBySpaceIds(String[] spaceIds) {
        return spaceInfoMapper.deleteSpaceInfoBySpaceIds(spaceIds);
    }

    /**
     * 删除空间信息信息
     *
     * @param spaceId 空间信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceInfoBySpaceId(String spaceId) {
        return spaceInfoMapper.deleteSpaceInfoBySpaceId(spaceId);
    }

    //endregion
    @Override
    public QueryWrapper<SpaceInfo> getQueryWrapper(SpaceInfoQuery spaceInfoQuery) {
        QueryWrapper<SpaceInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = spaceInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String spaceId = spaceInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId), "space_id", spaceId);

        String spaceName = spaceInfoQuery.getSpaceName();
        queryWrapper.like(StringUtils.isNotEmpty(spaceName), "space_name", spaceName);

        String ossType = spaceInfoQuery.getOssType();
        queryWrapper.eq(StringUtils.isNotEmpty(ossType), "oss_type", ossType);

        String userId = spaceInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String spaceStatus = spaceInfoQuery.getSpaceStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceStatus), "space_status", spaceStatus);

        String spaceType = spaceInfoQuery.getSpaceType();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceType), "space_type", spaceType);

        Date createTime = spaceInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date lastUpdateTime = spaceInfoQuery.getLastUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginLastUpdateTime")) && StringUtils.isNotNull(params.get("endLastUpdateTime")), "last_update_time", params.get("beginLastUpdateTime"), params.get("endLastUpdateTime"));

        Date updateTime = spaceInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String isDelete = spaceInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        Date deletedTime = spaceInfoQuery.getDeletedTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginDeletedTime")) && StringUtils.isNotNull(params.get("endDeletedTime")), "deleted_time", params.get("beginDeletedTime"), params.get("endDeletedTime"));

        return queryWrapper;
    }

    @Override
    public List<SpaceInfoVo> convertVoList(List<SpaceInfo> spaceInfoList) {
        if (StringUtils.isEmpty(spaceInfoList)) {
            return Collections.emptyList();
        }
        return spaceInfoList.stream().map(SpaceInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public int userInsertSpaceInfo(SpaceInfo spaceInfo) {
        //根据用户查询是否存在此空间
        SpaceInfo old = this.getOne(new LambdaQueryWrapper<SpaceInfo>().eq(SpaceInfo::getUserId, spaceInfo.getUserId()).eq(SpaceInfo::getSpaceName, spaceInfo.getSpaceName()));
        if (StringUtils.isNotNull(old)) {
            throw new ServiceException("空间名称已经存在！！！");
        }
        //判断用户可以创建的类型、个人、团队
        ArrayList<String> spaceTypeList = new ArrayList<>();
        spaceTypeList.add(PSpaceTypeEnum.SPACE_TYPE_1.getValue());
        spaceTypeList.add(PSpaceTypeEnum.SPACE_TYPE_2.getValue());
        if (!spaceTypeList.contains(spaceInfo.getSpaceType())) {
            throw new ServiceException("空间类型错误！！！");
        }
        //获取用户最大空间数量
        String maxSpaceCount = "";
        if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_1.getValue())) {
            maxSpaceCount = configInfoService.getConfigInfoInCache(PICTURE_SPACE_MAX_1);
        } else if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_2.getValue())) {
            maxSpaceCount = configInfoService.getConfigInfoInCache(PICTURE_SPACE_MAX_2);
        }
        if (StringUtils.isEmpty(maxSpaceCount)) {
            maxSpaceCount = "10";
        }
        //查询用户此类型空间创建了多少个
        long count = this.count(new LambdaQueryWrapper<SpaceInfo>().eq(SpaceInfo::getUserId, spaceInfo.getUserId()).eq(SpaceInfo::getSpaceType, spaceInfo.getSpaceType()));
        if (count >= Long.parseLong(maxSpaceCount)) {
            throw new ServiceException("此类型空间创建了10个，不能再创建了！！！");
        }
        spaceInfo.setCreateTime(DateUtils.getNowDate());
        spaceInfo.setUpdateTime(DateUtils.getNowDate());

        String maxCount = configInfoService.getConfigInfoInCache(PICTURE_SPACE_MAX_COUNT);
        try {
            spaceInfo.setMaxCount(Long.parseLong(maxCount));
        } catch (NumberFormatException e) {
            log.error("获取最大空间文件数量配置信息出错", e);
            //如果转换异常则默认100
            maxCount = "100L";
        }
        String maxSize = null;
        try {
            maxSize = configInfoService.getConfigInfoInCache(PICTURE_SPACE_MAX_SIZE);
        } catch (Exception e) {
            log.error("获取最大空间文件数量配置信息出错", e);
            //如果转换异常则默认300M
            maxSize = "314572800";
        }
        spaceInfo.setMaxSize(Long.parseLong(maxSize));
        spaceInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        spaceInfo.setOssType(PSpaceOssTypeEnum.SPACE_OSS_TYPE_0.getValue());

        boolean save = this.save(spaceInfo);
        //异步更新封面文件日志
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.recordSpaceCoverFileInfoLog(spaceInfo));
        return save ? 1 : 0;
    }

    @Override
    public int userUpdateSpaceInfo(SpaceInfo spaceInfo) {
        //查询空间
        SpaceInfo old = this.getOne(new LambdaQueryWrapper<SpaceInfo>().eq(SpaceInfo::getSpaceId, spaceInfo.getSpaceId()));
        if (StringUtils.isNull(old)) {
            throw new ServiceException("空间不存在！！！");
        }
        if (!old.getUserId().equals(spaceInfo.getUserId())) {
            throw new ServiceException("空间不存在！！！");
        }
        spaceInfo.setUpdateTime(DateUtils.getNowDate());
        boolean b = this.updateById(spaceInfo);
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.updateSpaceCoverFileInfoLog(old,spaceInfo));
        return b ? 1 : 0;
    }

}
