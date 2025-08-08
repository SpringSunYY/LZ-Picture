package com.lz.ai.mapper;

import java.util.List;
import com.lz.ai.model.domain.PromptInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 提示词信息Mapper接口
 *
 * @author YY
 * @date 2025-08-08
 */
public interface PromptInfoMapper extends BaseMapper<PromptInfo>
{
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
     * 删除提示词信息
     *
     * @param infoId 提示词信息主键
     * @return 结果
     */
    public int deletePromptInfoByInfoId(String infoId);

    /**
     * 批量删除提示词信息
     *
     * @param infoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePromptInfoByInfoIds(String[] infoIds);
}
