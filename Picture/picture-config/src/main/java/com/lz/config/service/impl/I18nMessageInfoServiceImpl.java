package com.lz.config.service.impl;

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
import com.lz.config.mapper.I18nMessageInfoMapper;
import com.lz.config.model.domain.I18nMessageInfo;
import com.lz.config.service.II18nMessageInfoService;
import com.lz.config.model.dto.i18nMessageInfo.I18nMessageInfoQuery;
import com.lz.config.model.vo.i18nMessageInfo.I18nMessageInfoVo;

/**
 * 国际化信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-02-28
 */
@Service
public class I18nMessageInfoServiceImpl extends ServiceImpl<I18nMessageInfoMapper, I18nMessageInfo> implements II18nMessageInfoService
{
    @Resource
    private I18nMessageInfoMapper i18nMessageInfoMapper;

    //region mybatis代码
    /**
     * 查询国际化信息
     *
     * @param messageId 国际化信息主键
     * @return 国际化信息
     */
    @Override
    public I18nMessageInfo selectI18nMessageInfoByMessageId(Long messageId)
    {
        return i18nMessageInfoMapper.selectI18nMessageInfoByMessageId(messageId);
    }

    /**
     * 查询国际化信息列表
     *
     * @param i18nMessageInfo 国际化信息
     * @return 国际化信息
     */
    @Override
    public List<I18nMessageInfo> selectI18nMessageInfoList(I18nMessageInfo i18nMessageInfo)
    {
        return i18nMessageInfoMapper.selectI18nMessageInfoList(i18nMessageInfo);
    }

    /**
     * 新增国际化信息
     *
     * @param i18nMessageInfo 国际化信息
     * @return 结果
     */
    @Override
    public int insertI18nMessageInfo(I18nMessageInfo i18nMessageInfo)
    {
                i18nMessageInfo.setCreateTime(DateUtils.getNowDate());
            return i18nMessageInfoMapper.insertI18nMessageInfo(i18nMessageInfo);
    }

    /**
     * 修改国际化信息
     *
     * @param i18nMessageInfo 国际化信息
     * @return 结果
     */
    @Override
    public int updateI18nMessageInfo(I18nMessageInfo i18nMessageInfo)
    {
                i18nMessageInfo.setUpdateTime(DateUtils.getNowDate());
        return i18nMessageInfoMapper.updateI18nMessageInfo(i18nMessageInfo);
    }

    /**
     * 批量删除国际化信息
     *
     * @param messageIds 需要删除的国际化信息主键
     * @return 结果
     */
    @Override
    public int deleteI18nMessageInfoByMessageIds(Long[] messageIds)
    {
        return i18nMessageInfoMapper.deleteI18nMessageInfoByMessageIds(messageIds);
    }

    /**
     * 删除国际化信息信息
     *
     * @param messageId 国际化信息主键
     * @return 结果
     */
    @Override
    public int deleteI18nMessageInfoByMessageId(Long messageId)
    {
        return i18nMessageInfoMapper.deleteI18nMessageInfoByMessageId(messageId);
    }
    //endregion
    @Override
    public QueryWrapper<I18nMessageInfo> getQueryWrapper(I18nMessageInfoQuery i18nMessageInfoQuery){
        QueryWrapper<I18nMessageInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = i18nMessageInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
                String messageKey = i18nMessageInfoQuery.getMessageKey();
                    queryWrapper.eq(StringUtils.isNotEmpty(messageKey) ,"message_key",messageKey);

                String locale = i18nMessageInfoQuery.getLocale();
                    queryWrapper.eq(StringUtils.isNotEmpty(locale) ,"locale",locale);

                String message = i18nMessageInfoQuery.getMessage();
                    queryWrapper.eq(StringUtils.isNotEmpty(message) ,"message",message);

                String createBy = i18nMessageInfoQuery.getCreateBy();
                    queryWrapper.like(StringUtils.isNotEmpty(createBy) ,"create_by",createBy);

                Date createTime = i18nMessageInfoQuery.getCreateTime();
                    queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

                String updateBy = i18nMessageInfoQuery.getUpdateBy();
                    queryWrapper.like(StringUtils.isNotEmpty(updateBy) ,"update_by",updateBy);

                Date updateTime = i18nMessageInfoQuery.getUpdateTime();
                    queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime"))&&StringUtils.isNotNull(params.get("endUpdateTime")),"update_time",params.get("beginUpdateTime"),params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<I18nMessageInfoVo> convertVoList(List<I18nMessageInfo> i18nMessageInfoList) {
        if (StringUtils.isEmpty(i18nMessageInfoList)) {
            return Collections.emptyList();
        }
        return i18nMessageInfoList.stream().map(I18nMessageInfoVo::objToVo).collect(Collectors.toList());
    }

}
