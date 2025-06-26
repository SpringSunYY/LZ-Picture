package com.lz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.config.model.domain.InformTemplateInfo;
import com.lz.config.model.enmus.CTemplateStatusEnum;
import com.lz.config.service.IInformTemplateInfoService;
import com.lz.user.mapper.InformInfoMapper;
import com.lz.user.model.domain.InformInfo;
import com.lz.user.model.dto.informInfo.InformInfoQuery;
import com.lz.user.model.dto.informInfo.UserInformInfoQuery;
import com.lz.user.model.enums.UInformIsReadEnum;
import com.lz.user.model.enums.UInformStatusEnum;
import com.lz.user.model.vo.informInfo.InformInfoVo;
import com.lz.user.service.IInformInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.config.LocaleConstants.DEFAULT_LOCALE;
import static com.lz.common.constant.redis.UserRedisConstants.*;

/**
 * 用户通知记录Service业务层处理
 *
 * @author YY
 * @date 2025-05-27
 */
@Service
public class InformInfoServiceImpl extends ServiceImpl<InformInfoMapper, InformInfo> implements IInformInfoService {
    @Resource
    private InformInfoMapper informInfoMapper;

    @Resource
    private IInformTemplateInfoService informTemplateInfoService;

    @Resource
    private RedisCache redisCache;

    //region mybatis代码

    /**
     * 查询用户通知记录
     *
     * @param recordId 用户通知记录主键
     * @return 用户通知记录
     */
    @Override
    public InformInfo selectInformInfoByRecordId(String recordId) {
        return informInfoMapper.selectInformInfoByRecordId(recordId);
    }

    /**
     * 查询用户通知记录列表
     *
     * @param informInfo 用户通知记录
     * @return 用户通知记录
     */
    @Override
    public List<InformInfo> selectInformInfoList(InformInfo informInfo) {
        return informInfoMapper.selectInformInfoList(informInfo);
    }

    /**
     * 新增用户通知记录
     *
     * @param informInfo 用户通知记录
     * @return 结果
     */
    @Override
    public int insertInformInfo(InformInfo informInfo) {
        return informInfoMapper.insertInformInfo(informInfo);
    }

    /**
     * 修改用户通知记录
     *
     * @param informInfo 用户通知记录
     * @return 结果
     */
    @Override
    public int updateInformInfo(InformInfo informInfo) {
        return informInfoMapper.updateInformInfo(informInfo);
    }

    /**
     * 批量删除用户通知记录
     *
     * @param recordIds 需要删除的用户通知记录主键
     * @return 结果
     */
    @Override
    public int deleteInformInfoByRecordIds(String[] recordIds) {
        return informInfoMapper.deleteInformInfoByRecordIds(recordIds);
    }

    /**
     * 删除用户通知记录信息
     *
     * @param recordId 用户通知记录主键
     * @return 结果
     */
    @Override
    public int deleteInformInfoByRecordId(String recordId) {
        return informInfoMapper.deleteInformInfoByRecordId(recordId);
    }

    //endregion
    @Override
    public QueryWrapper<InformInfo> getQueryWrapper(InformInfoQuery informInfoQuery) {
        QueryWrapper<InformInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = informInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String recordId = informInfoQuery.getRecordId();
        queryWrapper.eq(StringUtils.isNotEmpty(recordId), "record_id", recordId);

        String templateKey = informInfoQuery.getTemplateKey();
        queryWrapper.like(StringUtils.isNotNull(templateKey), "template_key", templateKey);

        String templateType = informInfoQuery.getTemplateType();
        queryWrapper.eq(StringUtils.isNotEmpty(templateType), "template_type", templateType);

        String locale = informInfoQuery.getLocale();
        queryWrapper.eq(StringUtils.isNotEmpty(locale), "locale", locale);

        String informTitle = informInfoQuery.getInformTitle();
        queryWrapper.like(StringUtils.isNotEmpty(informTitle), "inform_title", informTitle);

        String userId = informInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String informType = informInfoQuery.getInformType();
        queryWrapper.eq(StringUtils.isNotEmpty(informType), "inform_type", informType);

        String status = informInfoQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotNull(status), "status", status);

        String isRead = informInfoQuery.getIsRead();
        queryWrapper.eq(StringUtils.isNotNull(isRead), "is_read", isRead);

        Date readTime = informInfoQuery.getReadTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginReadTime")) && StringUtils.isNotNull(params.get("endReadTime")), "read_time", params.get("beginReadTime"), params.get("endReadTime"));

        Long retryCount = informInfoQuery.getRetryCount();
        queryWrapper.eq(StringUtils.isNotNull(retryCount), "retry_count", retryCount);

        Date sendTime = informInfoQuery.getSendTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginSendTime")) && StringUtils.isNotNull(params.get("endSendTime")), "send_time", params.get("beginSendTime"), params.get("endSendTime"));

        String isDelete = informInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotNull(isDelete), "is_delete", isDelete);

        return queryWrapper;
    }

    @Override
    public List<InformInfoVo> convertVoList(List<InformInfo> informInfoList) {
        if (StringUtils.isEmpty(informInfoList)) {
            return Collections.emptyList();
        }
        return informInfoList.stream().map(InformInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public int sendInform(String userId, String templateKey, String local, String templateType, String informType, Map<String, String> params) {
        //查询到对应的模板
        InformTemplateInfo informTemplateInfoByKeyLocaleType = informTemplateInfoService.getInformTemplateInfoByKeyLocaleType(templateKey, local, templateType);
        if (StringUtils.isNull(informTemplateInfoByKeyLocaleType)
                || !informTemplateInfoByKeyLocaleType.getStatus().equals(CTemplateStatusEnum.TEMPLATE_STATUS_0.getValue())) {
            //再次查询默认语言，如果没有则返回
            informTemplateInfoByKeyLocaleType = informTemplateInfoService.getInformTemplateInfoByKeyLocaleType(templateKey, DEFAULT_LOCALE, templateType);
            if (StringUtils.isNull(informTemplateInfoByKeyLocaleType)
                    || !informTemplateInfoByKeyLocaleType.getStatus().equals(CTemplateStatusEnum.TEMPLATE_STATUS_0.getValue())) {
                return 0;
            }
        }
        InformInfo informInfo = new InformInfo();
        informInfo.setInformType(informType);
        informInfo.setStatus(UInformStatusEnum.INFORM_STATUS_1.getValue());
        informInfo.setIsRead(UInformIsReadEnum.INFORM_IS_READ_0.getValue());
        informInfo.setRetryCount(0L);
        informInfo.setSendTime(DateUtils.getNowDate());
        informInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        informInfo.setTemplateKey(informTemplateInfoByKeyLocaleType.getTemplateKey());
        informInfo.setTemplateType(informTemplateInfoByKeyLocaleType.getTemplateType());
        informInfo.setLocale(informTemplateInfoByKeyLocaleType.getLocale());
        informInfo.setInformTitle(informTemplateInfoByKeyLocaleType.getInformTitle());
        informInfo.setUserId(userId);
        informInfo.setContent(StringUtils.parseTemplate(informTemplateInfoByKeyLocaleType.getContent(), params));
        String key = USER_INFORM_UNREAD_COUNT + userId;
        redisCache.increment(key, 1);
        return this.save(informInfo) ? 1 : 0;
    }

    @Override
    public Page<InformInfo> selectUserInformInfoList(UserInformInfoQuery userInformInfoQuery) {
        // 提取基础参数
        Integer pageNum = userInformInfoQuery.getPageNum();
        Integer pageSize = userInformInfoQuery.getPageSize();

        return this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<InformInfo>()
                        .eq(StringUtils.isNotEmpty(userInformInfoQuery.getUserId()), InformInfo::getUserId, userInformInfoQuery.getUserId())
                        .like(StringUtils.isNotEmpty(userInformInfoQuery.getInformTitle()), InformInfo::getInformTitle, userInformInfoQuery.getInformTitle())
                        .eq(StringUtils.isNotEmpty(userInformInfoQuery.getInformType()), InformInfo::getInformType, userInformInfoQuery.getInformType())
                        .eq(StringUtils.isNotEmpty(userInformInfoQuery.getIsRead()), InformInfo::getIsRead, userInformInfoQuery.getIsRead())
                        .eq(StringUtils.isNotEmpty(userInformInfoQuery.getTemplateType()), InformInfo::getTemplateType, userInformInfoQuery.getTemplateType())
                        .eq(InformInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                        .eq(InformInfo::getStatus, UInformStatusEnum.INFORM_STATUS_1.getValue())
                        .orderBy(true, false, InformInfo::getSendTime));
    }

    @Override
    public Integer getUnReadInformCount(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return 0;
        }
        String key = USER_INFORM_UNREAD_COUNT + userId;
        Integer cacheObject = redisCache.getCacheObject(key);
        if (StringUtils.isNotNull(cacheObject)) {
            return cacheObject;
        }
        long count = this.count(new LambdaQueryWrapper<InformInfo>()
                .eq(InformInfo::getUserId, userId)
                .eq(InformInfo::getIsRead, UInformIsReadEnum.INFORM_IS_READ_0.getValue()));
        int intExact = Math.toIntExact(count);
        redisCache.setCacheObject(key, intExact, USER_INFORM_UNREAD_COUNT_EXPIRE_TIME, TimeUnit.SECONDS);
        return intExact;
    }

    @Override
    public InformInfo selectInformInfoByRecordIdAndUserId(String recordId, String userId) {
        String key = USER_INFORM_DETAIL + userId + COMMON_SEPARATOR_CACHE + recordId;
        InformInfo informInfo = redisCache.getCacheObject(key);
        if (StringUtils.isNotNull(informInfo)) {
            return informInfo;
        }
        informInfo = this.getOne(new LambdaQueryWrapper<InformInfo>()
                .eq(InformInfo::getRecordId, recordId)
                .eq(InformInfo::getUserId, userId));
        if (StringUtils.isNotNull(informInfo) && informInfo.getIsRead().equals(UInformIsReadEnum.INFORM_IS_READ_0.getValue())) {
            //查看就为已读
            informInfo.setIsRead(UInformIsReadEnum.INFORM_IS_READ_1.getValue());
            informInfo.setReadTime(DateUtils.getNowDate());
            this.updateById(informInfo);
            //删除缓存未读数
            redisCache.deleteObject(USER_INFORM_UNREAD_COUNT + userId);
        }
        redisCache.setCacheObject(key, informInfo, USER_INFORM_DETAIL_EXPIRE_TIME, TimeUnit.SECONDS);
        return informInfo;
    }

}
