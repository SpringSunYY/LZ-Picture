package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.SearchLogInfo;
import com.lz.picture.model.vo.searchLogInfo.SearchLogInfoVo;
import com.lz.picture.model.dto.searchLogInfo.SearchLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户搜索记录Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface ISearchLogInfoService extends IService<SearchLogInfo>
{
    //region mybatis代码
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
     * 批量删除用户搜索记录
     *
     * @param searchIds 需要删除的用户搜索记录主键集合
     * @return 结果
     */
    public int deleteSearchLogInfoBySearchIds(String[] searchIds);

    /**
     * 删除用户搜索记录信息
     *
     * @param searchId 用户搜索记录主键
     * @return 结果
     */
    public int deleteSearchLogInfoBySearchId(String searchId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param searchLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<SearchLogInfo> getQueryWrapper(SearchLogInfoQuery searchLogInfoQuery);

    /**
     * 转换vo
     *
     * @param searchLogInfoList SearchLogInfo集合
     * @return SearchLogInfoVO集合
     */
    List<SearchLogInfoVo> convertVoList(List<SearchLogInfo> searchLogInfoList);
}
