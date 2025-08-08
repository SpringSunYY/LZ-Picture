package com.lz.ai.service;

import java.util.List;
import com.lz.ai.model.domain.PromptInfo;
import com.lz.ai.model.vo.promptInfo.PromptInfoVo;
import com.lz.ai.model.dto.promptInfo.PromptInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 提示词信息Service接口
 *
 * @author YY
 * @date 2025-08-08
 */
public interface IPromptInfoService extends IService<PromptInfo>
{
    //region mybatis代码
    /**
     * 查询提示词信息
     *
     * @param infoId 提示词信息主键
     * @return 提示词信息
     */
    public PromptInfo selectPromptInfoByInfoId(String infoId);

    /**
     * 查询提示词信息列表
     *
     * @param promptInfo 提示词信息
     * @return 提示词信息集合
     */
    public List<PromptInfo> selectPromptInfoList(PromptInfo promptInfo);

    /**
     * 新增提示词信息
     *
     * @param promptInfo 提示词信息
     * @return 结果
     */
    public int insertPromptInfo(PromptInfo promptInfo);

    /**
     * 修改提示词信息
     *
     * @param promptInfo 提示词信息
     * @return 结果
     */
    public int updatePromptInfo(PromptInfo promptInfo);

    /**
     * 批量删除提示词信息
     *
     * @param infoIds 需要删除的提示词信息主键集合
     * @return 结果
     */
    public int deletePromptInfoByInfoIds(String[] infoIds);

    /**
     * 删除提示词信息信息
     *
     * @param infoId 提示词信息主键
     * @return 结果
     */
    public int deletePromptInfoByInfoId(String infoId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param promptInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PromptInfo> getQueryWrapper(PromptInfoQuery promptInfoQuery);

    /**
     * 转换vo
     *
     * @param promptInfoList PromptInfo集合
     * @return PromptInfoVO集合
     */
    List<PromptInfoVo> convertVoList(List<PromptInfo> promptInfoList);
}
