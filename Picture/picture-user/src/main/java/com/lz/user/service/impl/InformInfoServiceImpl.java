package com.lz.user.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lz.common.utils.StringUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.user.mapper.InformInfoMapper;
import com.lz.user.model.domain.InformInfo;
import com.lz.user.service.IInformInfoService;
import com.lz.user.model.dto.informInfo.InformInfoQuery;
import com.lz.user.model.vo.informInfo.InformInfoVo;

/**
 * 用户通知记录Service业务层处理
 *
 * @author YY
 * @date 2025-03-17
 */
@Service
public class InformInfoServiceImpl extends ServiceImpl<InformInfoMapper, InformInfo> implements IInformInfoService
{
    @Resource
    private InformInfoMapper informInfoMapper;

    //region mybatis代码
    /**
     * 查询用户通知记录
     *
     * @param recordId 用户通知记录主键
     * @return 用户通知记录
     */
    @Override
    public InformInfo selectInformInfoByRecordId(String recordId)
    {
        return informInfoMapper.selectInformInfoByRecordId(recordId);
    }

    /**
     * 查询用户通知记录列表
     *
     * @param informInfo 用户通知记录
     * @return 用户通知记录
     */
    @Override
    public List<InformInfo> selectInformInfoList(InformInfo informInfo)
    {
        return informInfoMapper.selectInformInfoList(informInfo);
    }

    /**
     * 新增用户通知记录
     *
     * @param informInfo 用户通知记录
     * @return 结果
     */
    @Override
    public int insertInformInfo(InformInfo informInfo)
    {
        return informInfoMapper.insertInformInfo(informInfo);
    }

    /**
     * 修改用户通知记录
     *
     * @param informInfo 用户通知记录
     * @return 结果
     */
    @Override
    public int updateInformInfo(InformInfo informInfo)
    {
        return informInfoMapper.updateInformInfo(informInfo);
    }

    /**
     * 批量删除用户通知记录
     *
     * @param recordIds 需要删除的用户通知记录主键
     * @return 结果
     */
    @Override
    public int deleteInformInfoByRecordIds(String[] recordIds)
    {
        return informInfoMapper.deleteInformInfoByRecordIds(recordIds);
    }

    /**
     * 删除用户通知记录信息
     *
     * @param recordId 用户通知记录主键
     * @return 结果
     */
    @Override
    public int deleteInformInfoByRecordId(String recordId)
    {
        return informInfoMapper.deleteInformInfoByRecordId(recordId);
    }
    //endregion
    @Override
    public QueryWrapper<InformInfo> getQueryWrapper(InformInfoQuery informInfoQuery){
        QueryWrapper<InformInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = informInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String recordId = informInfoQuery.getRecordId();
        queryWrapper.eq(StringUtils.isNotEmpty(recordId) ,"record_id",recordId);

    Long templateId = informInfoQuery.getTemplateId();
        queryWrapper.eq( StringUtils.isNotNull(templateId),"template_id",templateId);

    String userId = informInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String informType = informInfoQuery.getInformType();
        queryWrapper.eq(StringUtils.isNotEmpty(informType) ,"inform_type",informType);

    Integer status = informInfoQuery.getStatus();
        queryWrapper.eq( StringUtils.isNotNull(status),"status",status);

    Integer isRead = informInfoQuery.getIsRead();
        queryWrapper.eq( StringUtils.isNotNull(isRead),"is_read",isRead);

    Date readTime = informInfoQuery.getReadTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginReadTime"))&&StringUtils.isNotNull(params.get("endReadTime")),"read_time",params.get("beginReadTime"),params.get("endReadTime"));

    Date sendTime = informInfoQuery.getSendTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginSendTime"))&&StringUtils.isNotNull(params.get("endSendTime")),"send_time",params.get("beginSendTime"),params.get("endSendTime"));

    Integer isDeleted = informInfoQuery.getIsDeleted();
        queryWrapper.eq( StringUtils.isNotNull(isDeleted),"is_deleted",isDeleted);

        return queryWrapper;
    }

    @Override
    public List<InformInfoVo> convertVoList(List<InformInfo> informInfoList) {
        if (StringUtils.isEmpty(informInfoList)) {
            return Collections.emptyList();
        }
        return informInfoList.stream().map(InformInfoVo::objToVo).collect(Collectors.toList());
    }

}
