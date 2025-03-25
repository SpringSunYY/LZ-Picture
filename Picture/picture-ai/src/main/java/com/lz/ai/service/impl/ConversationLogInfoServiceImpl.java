package com.lz.ai.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lz.common.utils.StringUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.ai.mapper.ConversationLogInfoMapper;
import com.lz.ai.model.domain.ConversationLogInfo;
import com.lz.ai.service.IConversationLogInfoService;
import com.lz.ai.model.dto.conversationLogInfo.ConversationLogInfoQuery;
import com.lz.ai.model.vo.conversationLogInfo.ConversationLogInfoVo;

/**
 * AI对话明细记录Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class ConversationLogInfoServiceImpl extends ServiceImpl<ConversationLogInfoMapper, ConversationLogInfo> implements IConversationLogInfoService
{
    @Resource
    private ConversationLogInfoMapper conversationLogInfoMapper;

    //region mybatis代码
    /**
     * 查询AI对话明细记录
     *
     * @param conversationId AI对话明细记录主键
     * @return AI对话明细记录
     */
    @Override
    public ConversationLogInfo selectConversationLogInfoByConversationId(String conversationId)
    {
        return conversationLogInfoMapper.selectConversationLogInfoByConversationId(conversationId);
    }

    /**
     * 查询AI对话明细记录列表
     *
     * @param conversationLogInfo AI对话明细记录
     * @return AI对话明细记录
     */
    @Override
    public List<ConversationLogInfo> selectConversationLogInfoList(ConversationLogInfo conversationLogInfo)
    {
        return conversationLogInfoMapper.selectConversationLogInfoList(conversationLogInfo);
    }

    /**
     * 新增AI对话明细记录
     *
     * @param conversationLogInfo AI对话明细记录
     * @return 结果
     */
    @Override
    public int insertConversationLogInfo(ConversationLogInfo conversationLogInfo)
    {
        conversationLogInfo.setCreateTime(DateUtils.getNowDate());
        return conversationLogInfoMapper.insertConversationLogInfo(conversationLogInfo);
    }

    /**
     * 修改AI对话明细记录
     *
     * @param conversationLogInfo AI对话明细记录
     * @return 结果
     */
    @Override
    public int updateConversationLogInfo(ConversationLogInfo conversationLogInfo)
    {
        return conversationLogInfoMapper.updateConversationLogInfo(conversationLogInfo);
    }

    /**
     * 批量删除AI对话明细记录
     *
     * @param conversationIds 需要删除的AI对话明细记录主键
     * @return 结果
     */
    @Override
    public int deleteConversationLogInfoByConversationIds(String[] conversationIds)
    {
        return conversationLogInfoMapper.deleteConversationLogInfoByConversationIds(conversationIds);
    }

    /**
     * 删除AI对话明细记录信息
     *
     * @param conversationId AI对话明细记录主键
     * @return 结果
     */
    @Override
    public int deleteConversationLogInfoByConversationId(String conversationId)
    {
        return conversationLogInfoMapper.deleteConversationLogInfoByConversationId(conversationId);
    }
    //endregion
    @Override
    public QueryWrapper<ConversationLogInfo> getQueryWrapper(ConversationLogInfoQuery conversationLogInfoQuery){
        QueryWrapper<ConversationLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = conversationLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String conversationId = conversationLogInfoQuery.getConversationId();
        queryWrapper.eq(StringUtils.isNotEmpty(conversationId) ,"conversation_id",conversationId);

    String sessionId = conversationLogInfoQuery.getSessionId();
        queryWrapper.eq(StringUtils.isNotEmpty(sessionId) ,"session_id",sessionId);

    String userId = conversationLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    Date requestTime = conversationLogInfoQuery.getRequestTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginRequestTime"))&&StringUtils.isNotNull(params.get("endRequestTime")),"request_time",params.get("beginRequestTime"),params.get("endRequestTime"));

    Date responseTime = conversationLogInfoQuery.getResponseTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginResponseTime"))&&StringUtils.isNotNull(params.get("endResponseTime")),"response_time",params.get("beginResponseTime"),params.get("endResponseTime"));

    String conversationStatus = conversationLogInfoQuery.getConversationStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(conversationStatus) ,"conversation_status",conversationStatus);

    String aiStatusCode = conversationLogInfoQuery.getAiStatusCode();
        queryWrapper.eq(StringUtils.isNotEmpty(aiStatusCode) ,"ai_status_code",aiStatusCode);

    String conversationType = conversationLogInfoQuery.getConversationType();
        queryWrapper.eq(StringUtils.isNotEmpty(conversationType) ,"conversation_type",conversationType);

    Date createTime = conversationLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<ConversationLogInfoVo> convertVoList(List<ConversationLogInfo> conversationLogInfoList) {
        if (StringUtils.isEmpty(conversationLogInfoList)) {
            return Collections.emptyList();
        }
        return conversationLogInfoList.stream().map(ConversationLogInfoVo::objToVo).collect(Collectors.toList());
    }

}
