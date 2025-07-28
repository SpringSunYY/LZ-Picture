package com.lz.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomCacheEvict;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.annotation.CustomSort;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.mapper.NoticeInfoMapper;
import com.lz.config.model.domain.NoticeInfo;
import com.lz.config.model.dto.noticeInfo.NoticeInfoQuery;
import com.lz.config.model.dto.noticeInfo.NoticeInfoRequest;
import com.lz.config.model.enmus.CNoticeIsExhibitEnum;
import com.lz.config.model.enmus.CNoticePlatformEnum;
import com.lz.config.model.enmus.CNoticeStatusEnum;
import com.lz.config.model.vo.noticeInfo.NoticeInfoVo;
import com.lz.config.model.vo.noticeInfo.UserNoticeInfoVo;
import com.lz.config.service.INoticeInfoService;
import com.lz.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.redis.UserConfigRedisConstants.CONFIG_NOTICE_INFO;
import static com.lz.common.constant.redis.UserConfigRedisConstants.CONFIG_NOTICE_INFO_EXPIRE_TIME;

/**
 * 用户公告Service业务层处理
 *
 * @author YY
 * @date 2025-07-26
 */
@Service
public class NoticeInfoServiceImpl extends ServiceImpl<NoticeInfoMapper, NoticeInfo> implements INoticeInfoService {
    @Resource
    private NoticeInfoMapper noticeInfoMapper;

    @Resource
    private ISysUserService sysUserService;

    //region mybatis代码

    /**
     * 查询用户公告
     *
     * @param noticeId 用户公告主键
     * @return 用户公告
     */
    @Override
    public NoticeInfo selectNoticeInfoByNoticeId(String noticeId) {
        return noticeInfoMapper.selectNoticeInfoByNoticeId(noticeId);
    }

    /**
     * 查询用户公告列表
     *
     * @param noticeInfo 用户公告
     * @return 用户公告
     */
    @CustomSort(sortFields = {"createTime", "updateTime", "orderNum"}, sortMappingFields = {"create_time", "update_time", "order_num"})
    @Override
    public List<NoticeInfo> selectNoticeInfoList(NoticeInfo noticeInfo) {
        List<NoticeInfo> noticeInfos = noticeInfoMapper.selectNoticeInfoList(noticeInfo);
        if (StringUtils.isNotEmpty(noticeInfos)) {
            List<Long> userIds = noticeInfos.stream().map(NoticeInfo::getUserId).toList();
            List<SysUser> users = sysUserService.selectUserByIds(userIds);
            //map key-userId value-username
            Map<Long, String> userMap = users.stream()
                    .collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
            noticeInfos.forEach(info -> info.setUserName(userMap.get(info.getUserId())));
        }
        return noticeInfos;
    }

    /**
     * 新增用户公告
     *
     * @param noticeInfo 用户公告
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = {CONFIG_NOTICE_INFO})
    @Override
    public int insertNoticeInfo(NoticeInfo noticeInfo) {
        noticeInfo.setNoticeId(IdUtils.snowflakeId().toString());
        noticeInfo.setCreateTime(DateUtils.getNowDate());
        updateIsExhibit(noticeInfo);
        return noticeInfoMapper.insertNoticeInfo(noticeInfo);
    }

    private void updateIsExhibit(NoticeInfo noticeInfo) {
        //查询如果此公告需要展示但是已经有展示了的更新他为不展示
        List<NoticeInfo> noticeInfoList = this.list(new LambdaQueryWrapper<NoticeInfo>()
                .eq(NoticeInfo::getIsExhibit, CNoticeIsExhibitEnum.NOTICE_IS_EXHIBIT_1.getValue())
                .eq(NoticeInfo::getNoticeStatus, CNoticeStatusEnum.NOTICE_STATUS_1.getValue())
                .eq(NoticeInfo::getPlatform, noticeInfo.getPlatform()));
        noticeInfoList.forEach(notice -> notice.setIsExhibit(CNoticeIsExhibitEnum.NOTICE_IS_EXHIBIT_0.getValue()));
        this.updateBatchById(noticeInfoList);
    }

    /**
     * 修改用户公告
     *
     * @param noticeInfo 用户公告
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = {CONFIG_NOTICE_INFO})
    @Override
    public int updateNoticeInfo(NoticeInfo noticeInfo) {
        updateIsExhibit(noticeInfo);
        noticeInfo.setUpdateTime(DateUtils.getNowDate());
        return noticeInfoMapper.updateNoticeInfo(noticeInfo);
    }

    /**
     * 批量删除用户公告
     *
     * @param noticeIds 需要删除的用户公告主键
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = {CONFIG_NOTICE_INFO})
    @Override
    public int deleteNoticeInfoByNoticeIds(String[] noticeIds) {
        return noticeInfoMapper.deleteNoticeInfoByNoticeIds(noticeIds);
    }

    /**
     * 删除用户公告信息
     *
     * @param noticeId 用户公告主键
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = {CONFIG_NOTICE_INFO})
    @Override
    public int deleteNoticeInfoByNoticeId(String noticeId) {
        return noticeInfoMapper.deleteNoticeInfoByNoticeId(noticeId);
    }

    //endregion
    @Override
    public QueryWrapper<NoticeInfo> getQueryWrapper(NoticeInfoQuery noticeInfoQuery) {
        QueryWrapper<NoticeInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = noticeInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String noticeId = noticeInfoQuery.getNoticeId();
        queryWrapper.eq(StringUtils.isNotEmpty(noticeId), "notice_id", noticeId);

        String locale = noticeInfoQuery.getLocale();
        queryWrapper.eq(StringUtils.isNotEmpty(locale), "locale", locale);

        String platform = noticeInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform), "platform", platform);

        String noticeType = noticeInfoQuery.getNoticeType();
        queryWrapper.eq(StringUtils.isNotEmpty(noticeType), "notice_type", noticeType);

        String isExhibit = noticeInfoQuery.getIsExhibit();
        queryWrapper.eq(StringUtils.isNotEmpty(isExhibit), "is_exhibit", isExhibit);

        String noticeTitle = noticeInfoQuery.getNoticeTitle();
        queryWrapper.like(StringUtils.isNotEmpty(noticeTitle), "notice_title", noticeTitle);

        String noticeStatus = noticeInfoQuery.getNoticeStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(noticeStatus), "notice_status", noticeStatus);

        Long userId = noticeInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotNull(userId), "user_id", userId);

        Date createTime = noticeInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = noticeInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<NoticeInfoVo> convertVoList(List<NoticeInfo> noticeInfoList) {
        if (StringUtils.isEmpty(noticeInfoList)) {
            return Collections.emptyList();
        }
        return noticeInfoList.stream().map(NoticeInfoVo::objToVo).collect(Collectors.toList());
    }

    @CustomCacheable(keyPrefix = CONFIG_NOTICE_INFO, expireTime = CONFIG_NOTICE_INFO_EXPIRE_TIME, useQueryParamsAsKey = true)
    @Override
    public TableDataInfo selectUserNoticeInfoList(NoticeInfoRequest request) {
        Page<NoticeInfo> page = this.page(new Page<>(request.getPageNum(), request.getPageSize()), new LambdaQueryWrapper<NoticeInfo>()
                .eq(NoticeInfo::getNoticeStatus, CNoticeStatusEnum.NOTICE_STATUS_1.getValue())
                .like(StringUtils.isNotEmpty(request.getNoticeTitle()), NoticeInfo::getNoticeTitle, request.getNoticeTitle())
                .eq(StringUtils.isNotEmpty(request.getNoticeType()), NoticeInfo::getNoticeType, request.getNoticeType())
                .and(query ->
                        query.eq(StringUtils.isNotEmpty(request.getPlatform()), NoticeInfo::getPlatform, request.getPlatform())
                                .or()
                                .eq(NoticeInfo::getPlatform, CNoticePlatformEnum.NOTICE_PLATFORM_0.getValue()))
                .eq(StringUtils.isNotEmpty(request.getLocale()), NoticeInfo::getLocale, request.getLocale())
                .orderByAsc(true, NoticeInfo::getOrderNum)
                .orderByDesc(NoticeInfo::getCreateTime)
        );
        List<UserNoticeInfoVo> list = page.getRecords().stream().map(UserNoticeInfoVo::objToVo).toList();
        return new TableDataInfo(list, Math.toIntExact(page.getTotal()));
    }

    @CustomCacheable(keyPrefix = CONFIG_NOTICE_INFO, expireTime = CONFIG_NOTICE_INFO_EXPIRE_TIME, keyField = "noticeId")
    @Override
    public UserNoticeInfoVo selectUserNoticeInfoByNoticeId(String noticeId) {
        NoticeInfo byId = this.getById(noticeId);
        return UserNoticeInfoVo.objToVo(byId);
    }

    @CustomCacheable(keyPrefix = CONFIG_NOTICE_INFO, expireTime = CONFIG_NOTICE_INFO_EXPIRE_TIME, keyField = "request.platform")
    @Override
    public UserNoticeInfoVo selectUserNoticeInfoByExhibit(NoticeInfoRequest request) {
        //首先判断平台是否为空
        if (StringUtils.isEmpty(request.getPlatform())) {
            NoticeInfo noticeInfo = this.getOne(new LambdaQueryWrapper<NoticeInfo>()
                    .eq(NoticeInfo::getIsExhibit, CNoticeIsExhibitEnum.NOTICE_IS_EXHIBIT_1.getValue())
                    .orderByDesc(NoticeInfo::getUpdateTime).last("limit 1"));
            return UserNoticeInfoVo.objToVo(noticeInfo);
        }
        //不为空的时候，查询指定平台下的最新一条数据
        NoticeInfo noticeInfo = this.getOne(new LambdaQueryWrapper<NoticeInfo>()
                .eq(NoticeInfo::getPlatform, request.getPlatform())
                .eq(NoticeInfo::getIsExhibit, CNoticeIsExhibitEnum.NOTICE_IS_EXHIBIT_1.getValue())
                .orderByDesc(NoticeInfo::getUpdateTime).last("limit 1"));
        if (StringUtils.isNull(noticeInfo)) {
            NoticeInfo noticeInfoByBase = this.getOne(new LambdaQueryWrapper<NoticeInfo>()
                    .eq(NoticeInfo::getIsExhibit, CNoticeIsExhibitEnum.NOTICE_IS_EXHIBIT_1.getValue())
                    .orderByDesc(NoticeInfo::getUpdateTime).last("limit 1"));
            return UserNoticeInfoVo.objToVo(noticeInfoByBase);
        }
        return UserNoticeInfoVo.objToVo(noticeInfo);
    }

}
