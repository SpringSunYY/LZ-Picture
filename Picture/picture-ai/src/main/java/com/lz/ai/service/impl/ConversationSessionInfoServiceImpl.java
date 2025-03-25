package com.lz.ai.service.impl;

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
import com.lz.ai.mapper.ConversationSessionInfoMapper;
import com.lz.ai.model.domain.ConversationSessionInfo;
import com.lz.ai.service.IConversationSessionInfoService;
import com.lz.ai.model.dto.conversationSessionInfo.ConversationSessionInfoQuery;
import com.lz.ai.model.vo.conversationSessionInfo.ConversationSessionInfoVo;

/**
 * AI会话管理Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class ConversationSessionInfoServiceImpl extends ServiceImpl<ConversationSessionInfoMapper, ConversationSessionInfo> implements IConversationSessionInfoService
{
    @Resource
    private ConversationSessionInfoMapper conversationSessionInfoMapper;

    //region mybatis代码
    /**
     * 查询AI会话管理
     *
     * @param sessionId AI会话管理主键
     * @return AI会话管理
     */
    @Override
    public ConversationSessionInfo selectConversationSessionInfoBySessionId(String sessionId)
    {
        return conversationSessionInfoMapper.selectConversationSessionInfoBySessionId(sessionId);
    }

    /**
     * 查询AI会话管理列表
     *
     * @param conversationSessionInfo AI会话管理
     * @return AI会话管理
     */
    @Override
    public List<ConversationSessionInfo> selectConversationSessionInfoList(ConversationSessionInfo conversationSessionInfo)
    {
        return conversationSessionInfoMapper.selectConversationSessionInfoList(conversationSessionInfo);
    }

    /**
     * 新增AI会话管理
     *
     * @param conversationSessionInfo AI会话管理
     * @return 结果
     */
    @Override
    public int insertConversationSessionInfo(ConversationSessionInfo conversationSessionInfo)
    {
        conversationSessionInfo.setCreateTime(DateUtils.getNowDate());
        return conversationSessionInfoMapper.insertConversationSessionInfo(conversationSessionInfo);
    }

    /**
     * 修改AI会话管理
     *
     * @param conversationSessionInfo AI会话管理
     * @return 结果
     */
    @Override
    public int updateConversationSessionInfo(ConversationSessionInfo conversationSessionInfo)
    {
      conversationSessionInfo.setUpdateTime(DateUtils.getNowDate());
        return conversationSessionInfoMapper.updateConversationSessionInfo(conversationSessionInfo);
    }

    /**
     * 批量删除AI会话管理
     *
     * @param sessionIds 需要删除的AI会话管理主键
     * @return 结果
     */
    @Override
    public int deleteConversationSessionInfoBySessionIds(String[] sessionIds)
    {
        return conversationSessionInfoMapper.deleteConversationSessionInfoBySessionIds(sessionIds);
    }

    /**
     * 删除AI会话管理信息
     *
     * @param sessionId AI会话管理主键
     * @return 结果
     */
    @Override
    public int deleteConversationSessionInfoBySessionId(String sessionId)
    {
        return conversationSessionInfoMapper.deleteConversationSessionInfoBySessionId(sessionId);
    }
    //endregion
    @Override
    public QueryWrapper<ConversationSessionInfo> getQueryWrapper(ConversationSessionInfoQuery conversationSessionInfoQuery){
        QueryWrapper<ConversationSessionInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = conversationSessionInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String sessionId = conversationSessionInfoQuery.getSessionId();
        queryWrapper.eq(StringUtils.isNotEmpty(sessionId) ,"session_id",sessionId);

    String userId = conversationSessionInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String conversationId = conversationSessionInfoQuery.getConversationId();
        queryWrapper.eq(StringUtils.isNotEmpty(conversationId) ,"conversation_id",conversationId);

    String sessionName = conversationSessionInfoQuery.getSessionName();
        queryWrapper.like(StringUtils.isNotEmpty(sessionName) ,"session_name",sessionName);

    String ipAddr = conversationSessionInfoQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr) ,"ip_addr",ipAddr);

    String deviceId = conversationSessionInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId) ,"device_id",deviceId);

    String browser = conversationSessionInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser) ,"browser",browser);

    String os = conversationSessionInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os) ,"os",os);

    String platform = conversationSessionInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform) ,"platform",platform);

    Date createTime = conversationSessionInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    Date updateTime = conversationSessionInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime"))&&StringUtils.isNotNull(params.get("endUpdateTime")),"update_time",params.get("beginUpdateTime"),params.get("endUpdateTime"));

    String isDelete = conversationSessionInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete) ,"is_delete",isDelete);

        return queryWrapper;
    }

    @Override
    public List<ConversationSessionInfoVo> convertVoList(List<ConversationSessionInfo> conversationSessionInfoList) {
        if (StringUtils.isEmpty(conversationSessionInfoList)) {
            return Collections.emptyList();
        }
        return conversationSessionInfoList.stream().map(ConversationSessionInfoVo::objToVo).collect(Collectors.toList());
    }

}
