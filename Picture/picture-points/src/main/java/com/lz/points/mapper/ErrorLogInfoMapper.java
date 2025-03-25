package com.lz.points.mapper;

import java.util.List;
import com.lz.points.model.domain.ErrorLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 异常捕获Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface ErrorLogInfoMapper extends BaseMapper<ErrorLogInfo>
{
    /**
     * 查询异常捕获
     *
     * @param errorId 异常捕获主键
     * @return 异常捕获
     */
    public ErrorLogInfo selectErrorLogInfoByErrorId(String errorId);

    /**
     * 查询异常捕获列表
     *
     * @param errorLogInfo 异常捕获
     * @return 异常捕获集合
     */
    public List<ErrorLogInfo> selectErrorLogInfoList(ErrorLogInfo errorLogInfo);

    /**
     * 新增异常捕获
     *
     * @param errorLogInfo 异常捕获
     * @return 结果
     */
    public int insertErrorLogInfo(ErrorLogInfo errorLogInfo);

    /**
     * 修改异常捕获
     *
     * @param errorLogInfo 异常捕获
     * @return 结果
     */
    public int updateErrorLogInfo(ErrorLogInfo errorLogInfo);

    /**
     * 删除异常捕获
     *
     * @param errorId 异常捕获主键
     * @return 结果
     */
    public int deleteErrorLogInfoByErrorId(String errorId);

    /**
     * 批量删除异常捕获
     *
     * @param errorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteErrorLogInfoByErrorIds(String[] errorIds);
}
