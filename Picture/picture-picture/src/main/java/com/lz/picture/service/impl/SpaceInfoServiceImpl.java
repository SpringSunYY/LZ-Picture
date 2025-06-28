package com.lz.picture.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.config.OssConfig;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.core.redis.RedisCache;
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
import com.lz.picture.model.dto.spaceInfo.UserSpaceInfoQuery;
import com.lz.picture.model.enums.PSpaceOssTypeEnum;
import com.lz.picture.model.enums.PSpaceTypeEnum;
import com.lz.picture.model.vo.spaceInfo.SpaceInfoVo;
import com.lz.picture.model.vo.spaceInfo.UserPersonalSpaceInfoVo;
import com.lz.picture.service.ISpaceInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.config.UserConfigKeyConstants.*;
import static com.lz.common.constant.redis.PictureRedisConstants.*;

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

    @Resource
    private RedisCache redisCache;

    @Resource
    private OssConfig ossConfig;

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
        if (save) {
            //删除表格缓存
            deleteSpaceTableCacheByUserId(spaceInfo.getUserId());
        }
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
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.updateSpaceCoverFileInfoLog(old, spaceInfo));
        if (b) {
            //删除表格缓存
            deleteSpaceTableCacheByUserId(spaceInfo.getUserId());
        }
        return b ? 1 : 0;
    }

    @Override
    public TableDataInfo listSpaceInfoTable(UserSpaceInfoQuery userSpaceInfoQuery) {
        String jsonStr = JSON.toJSONString(userSpaceInfoQuery);
        //查询缓存是否存在
        String keyData = PICTURE_SPACE_TABLE_DATE + userSpaceInfoQuery.getUserId() + COMMON_SEPARATOR_CACHE + jsonStr;
        List<UserPersonalSpaceInfoVo> vos = new ArrayList<>();
        if (redisCache.hasKey(keyData)) {
            vos = redisCache.getCacheObject(keyData);
        }
        String keyTotal = PICTURE_SPACE_TABLE_TOTAL + userSpaceInfoQuery.getUserId() + COMMON_SEPARATOR_CACHE + jsonStr;
        Long total = 0L;
        if (redisCache.hasKey(keyTotal)) {
            total = redisCache.getCacheObject(keyTotal);
        }
        //如果都存在直接返回
        if (StringUtils.isNotEmpty(vos) && StringUtils.isNotNull(total)) {
            return new TableDataInfo(vos, Math.toIntExact(total));
        }
        //构造查询条件
        Page<SpaceInfo> spaceInfoPage = new Page<>();
        spaceInfoPage.setCurrent(userSpaceInfoQuery.getPageNum());
        spaceInfoPage.setSize(userSpaceInfoQuery.getPageSize());
        //获取时间范围
        Map<String, Object> params = userSpaceInfoQuery.getParams();
        // 提取 beginCreateTime 和 endCreateTime（安全获取）
        String beginCreateTime = Optional.ofNullable(params)
                .map(p -> p.get("beginCreateTime"))
                .map(Object::toString)
                .filter(StringUtils::isNotEmpty)
                .orElse(null);

        String endCreateTime = Optional.ofNullable(params)
                .map(p -> p.get("endCreateTime"))
                .map(Object::toString)
                .filter(StringUtils::isNotEmpty)
                .orElse(null);
        //构造查询条件
        LambdaQueryWrapper<SpaceInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getIsDelete()), SpaceInfo::getIsDelete, userSpaceInfoQuery.getIsDelete())
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getUserId()), SpaceInfo::getUserId, userSpaceInfoQuery.getUserId())
                .like(StringUtils.isNotEmpty(userSpaceInfoQuery.getSpaceName()), SpaceInfo::getSpaceName, userSpaceInfoQuery.getSpaceName())
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getSpaceId()), SpaceInfo::getSpaceId, userSpaceInfoQuery.getSpaceId())
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getOssType()), SpaceInfo::getOssType, userSpaceInfoQuery.getOssType())
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getSpaceStatus()), SpaceInfo::getSpaceStatus, userSpaceInfoQuery.getSpaceStatus())
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getSpaceType()), SpaceInfo::getSpaceType, userSpaceInfoQuery.getSpaceType())
                .apply(
                        StringUtils.isNotEmpty(beginCreateTime) && StringUtils.isNotEmpty(endCreateTime),
                        "create_time between {0} and {1}",
                        beginCreateTime,
                        endCreateTime
                );
        //构建排序
        if (StringUtils.isNotEmpty(userSpaceInfoQuery.getIsAsc()) && StringUtils.isNotEmpty(userSpaceInfoQuery.getOrderByColumn())
                && Arrays.asList("createTime", "totalSize", "totalCount", "lastUpdateTime").contains(userSpaceInfoQuery.getOrderByColumn())) {
            if (userSpaceInfoQuery.getOrderByColumn().equals("totalSize")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInfoQuery.getIsAsc().equals("asc"), SpaceInfo::getTotalSize);
            }
            if (userSpaceInfoQuery.getOrderByColumn().equals("createTime")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInfoQuery.getIsAsc().equals("asc"), SpaceInfo::getCreateTime);
            }
            if (userSpaceInfoQuery.getOrderByColumn().equals("totalSize")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInfoQuery.getIsAsc().equals("asc"), SpaceInfo::getTotalSize);
            }
            if (userSpaceInfoQuery.getOrderByColumn().equals("totalCount")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInfoQuery.getIsAsc().equals("asc"), SpaceInfo::getTotalCount);
            }
            if (userSpaceInfoQuery.getOrderByColumn().equals("lastUpdateTime")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInfoQuery.getIsAsc().equals("asc"), SpaceInfo::getLastUpdateTime);
            }
        } else {
            lambdaQueryWrapper
                    .orderBy(true, false, SpaceInfo::getCreateTime);
        }
        Page<SpaceInfo> page = this.page(spaceInfoPage, lambdaQueryWrapper);
        //如果为空直接返回
        List<SpaceInfo> records = page.getRecords();
        if (StringUtils.isEmpty(records)) {
            redisCache.setCacheObject(keyData, vos, PICTURE_PICTURE_TABLE_DATE_EXPIRE_TIME, TimeUnit.SECONDS);
            redisCache.setCacheObject(keyTotal, total, PICTURE_PICTURE_TABLE_TOTAL_EXPIRE_TIME, TimeUnit.SECONDS);
            return new TableDataInfo(vos, Math.toIntExact(total));
        }
        //压缩图片
        String inCache = configInfoService.getConfigInfoInCache(PICTURE_SPACE_AVATAR_P);
        //转换为vo并且转换地址
        List<UserPersonalSpaceInfoVo> personalSpaceInfoVos = page.getRecords().stream()
                .map(spaceInfo -> {
                    spaceInfo.setSpaceAvatar(ossConfig.builderUrl(spaceInfo.getSpaceAvatar()) + "?x-oss-process=image/resize,p_" + inCache);
                    return UserPersonalSpaceInfoVo.objToVo(spaceInfo);
                }).toList();
        //存入缓存信息并返回
        redisCache.setCacheObject(keyData, personalSpaceInfoVos, PICTURE_PICTURE_TABLE_DATE_EXPIRE_TIME, TimeUnit.SECONDS);
        redisCache.setCacheObject(keyTotal, page.getTotal(), PICTURE_PICTURE_TABLE_TOTAL_EXPIRE_TIME, TimeUnit.SECONDS);
        return new TableDataInfo(personalSpaceInfoVos, Math.toIntExact(page.getTotal()));
    }

    /**
     * 删除空间表格缓存
     *
     * @param userId 用户编号
     * @return void
     * @author: YY
     * @method: deleteSpaceTableCacheByUserId
     * @date: 2025/6/28 17:28
     **/
    public void deleteSpaceTableCacheByUserId(String userId) {
        redisCache.deleteObjectsByPattern(PICTURE_SPACE_TABLE_DATE + userId + "*");
        redisCache.deleteObjectsByPattern(PICTURE_SPACE_TABLE_TOTAL + userId + "*");
    }

}
