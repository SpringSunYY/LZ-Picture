package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.SearchLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户搜索记录Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface SearchLogInfoMapper extends BaseMapper<SearchLogInfo>
{
    /**
     * 查询用户搜索记录
     *
     * @param searchId 用户搜索记录主键
     * @return 用户搜索记录
     */
    public SearchLogInfo selectSearchLogInfoBySearchId(String searchId);

    /**
     * 查询用户搜索记录列表
     *
     * @param searchLogInfo 用户搜索记录
     * @return 用户搜索记录集合
     */
    public List<SearchLogInfo> selectSearchLogInfoList(SearchLogInfo searchLogInfo);

    /**
     * 新增用户搜索记录
     *
     * @param searchLogInfo 用户搜索记录
     * @return 结果
     */
    public int insertSearchLogInfo(SearchLogInfo searchLogInfo);

    /**
     * 修改用户搜索记录
     *
     * @param searchLogInfo 用户搜索记录
     * @return 结果
     */
    public int updateSearchLogInfo(SearchLogInfo searchLogInfo);

    /**
     * 删除用户搜索记录
     *
     * @param searchId 用户搜索记录主键
     * @return 结果
     */
    public int deleteSearchLogInfoBySearchId(String searchId);

    /**
     * 批量删除用户搜索记录
     *
     * @param searchIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSearchLogInfoBySearchIds(String[] searchIds);
}
