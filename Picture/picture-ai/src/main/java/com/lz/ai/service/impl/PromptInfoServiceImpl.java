package com.lz.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.ai.mapper.PromptInfoMapper;
import com.lz.ai.model.domain.PromptInfo;
import com.lz.ai.model.dto.promptInfo.PromptInfoQuery;
import com.lz.ai.model.vo.promptInfo.PromptInfoVo;
import com.lz.ai.service.IPromptInfoService;
import com.lz.common.annotation.CustomSort;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 提示词信息Service业务层处理
 *
 * @author YY
 * @date 2025-08-14
 */
@Service
public class PromptInfoServiceImpl extends ServiceImpl<PromptInfoMapper, PromptInfo> implements IPromptInfoService {
    @Resource
    private PromptInfoMapper promptInfoMapper;

    //region mybatis代码

    /**
     * 查询提示词信息
     *
     * @param infoId 提示词信息主键
     * @return 提示词信息
     */
    @Override
    public PromptInfo selectPromptInfoByInfoId(String infoId) {
        return promptInfoMapper.selectPromptInfoByInfoId(infoId);
    }

    /**
     * 查询提示词信息列表
     *
     * @param promptInfo 提示词信息
     * @return 提示词信息
     */
    @CustomSort(sortFields = {"orderNum","name","createTime","updateTime"},
    sortMappingFields = {"order_num","name","create_time","update_time"})
    @Override
    public List<PromptInfo> selectPromptInfoList(PromptInfo promptInfo) {
        return promptInfoMapper.selectPromptInfoList(promptInfo);
    }

    /**
     * 新增提示词信息
     *
     * @param promptInfo 提示词信息
     * @return 结果
     */
    @Override
    public int insertPromptInfo(PromptInfo promptInfo) {
        promptInfo.setInfoId(IdUtils.fastSimpleUUID());
        promptInfo.setCreateBy(SecurityUtils.getUsername());
        promptInfo.setCreateTime(DateUtils.getNowDate());
        return promptInfoMapper.insertPromptInfo(promptInfo);
    }

    /**
     * 修改提示词信息
     *
     * @param promptInfo 提示词信息
     * @return 结果
     */
    @Override
    public int updatePromptInfo(PromptInfo promptInfo) {
        promptInfo.setUpdateBy(SecurityUtils.getUsername());
        promptInfo.setUpdateTime(DateUtils.getNowDate());
        return promptInfoMapper.updatePromptInfo(promptInfo);
    }

    /**
     * 批量删除提示词信息
     *
     * @param infoIds 需要删除的提示词信息主键
     * @return 结果
     */
    @Override
    public int deletePromptInfoByInfoIds(String[] infoIds) {
        return promptInfoMapper.deletePromptInfoByInfoIds(infoIds);
    }

    /**
     * 删除提示词信息信息
     *
     * @param infoId 提示词信息主键
     * @return 结果
     */
    @Override
    public int deletePromptInfoByInfoId(String infoId) {
        return promptInfoMapper.deletePromptInfoByInfoId(infoId);
    }

    //endregion
    @Override
    public QueryWrapper<PromptInfo> getQueryWrapper(PromptInfoQuery promptInfoQuery) {
        QueryWrapper<PromptInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = promptInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String infoId = promptInfoQuery.getInfoId();
        queryWrapper.eq(StringUtils.isNotEmpty(infoId), "info_id", infoId);

        String name = promptInfoQuery.getName();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);

        String content = promptInfoQuery.getContent();
        queryWrapper.like(StringUtils.isNotEmpty(content), "content", content);

        String promptStatus = promptInfoQuery.getPromptStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(promptStatus), "prompt_status", promptStatus);

        String createBy = promptInfoQuery.getCreateBy();
        queryWrapper.like(StringUtils.isNotEmpty(createBy), "create_by", createBy);

        Date createTime = promptInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String updateBy = promptInfoQuery.getUpdateBy();
        queryWrapper.like(StringUtils.isNotEmpty(updateBy), "update_by", updateBy);

        Date updateTime = promptInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<PromptInfoVo> convertVoList(List<PromptInfo> promptInfoList) {
        if (StringUtils.isEmpty(promptInfoList)) {
            return Collections.emptyList();
        }
        return promptInfoList.stream().map(PromptInfoVo::objToVo).collect(Collectors.toList());
    }

}
