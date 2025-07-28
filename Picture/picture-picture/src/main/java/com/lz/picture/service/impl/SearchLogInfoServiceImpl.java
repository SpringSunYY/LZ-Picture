package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomSort;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.picture.mapper.SearchLogInfoMapper;
import com.lz.picture.model.domain.SearchLogInfo;
import com.lz.picture.model.dto.searchLogInfo.SearchLogInfoQuery;
import com.lz.picture.model.vo.searchLogInfo.SearchLogInfoVo;
import com.lz.picture.service.ISearchLogInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户搜索记录Service业务层处理
 *
 * @author YY
 * @date 2025-06-03
 */
@Service
public class SearchLogInfoServiceImpl extends ServiceImpl<SearchLogInfoMapper, SearchLogInfo> implements ISearchLogInfoService {
    @Resource
    private SearchLogInfoMapper searchLogInfoMapper;

    //region mybatis代码

    /**
     * 查询用户搜索记录
     *
     * @param searchId 用户搜索记录主键
     * @return 用户搜索记录
     */
    @Override
    public SearchLogInfo selectSearchLogInfoBySearchId(String searchId) {
        return searchLogInfoMapper.selectSearchLogInfoBySearchId(searchId);
    }

    /**
     * 查询用户搜索记录列表
     *
     * @param searchLogInfo 用户搜索记录
     * @return 用户搜索记录
     */
    @CustomSort(sortFields = {"createTime", "resultCount", "searchDuration"},
            sortMappingFields = {"create_time", "result_count", "search_duration"})
    @Override
    public List<SearchLogInfo> selectSearchLogInfoList(SearchLogInfo searchLogInfo) {
        return searchLogInfoMapper.selectSearchLogInfoList(searchLogInfo);
    }

    /**
     * 新增用户搜索记录
     *
     * @param searchLogInfo 用户搜索记录
     * @return 结果
     */
    @Override
    public int insertSearchLogInfo(SearchLogInfo searchLogInfo) {
        searchLogInfo.setSearchId(IdUtils.fastSimpleUUID());
        searchLogInfo.setCreateTime(DateUtils.getNowDate());
        searchLogInfo.setCreateTime(DateUtils.getNowDate());
        return searchLogInfoMapper.insertSearchLogInfo(searchLogInfo);
    }

    /**
     * 修改用户搜索记录
     *
     * @param searchLogInfo 用户搜索记录
     * @return 结果
     */
    @Override
    public int updateSearchLogInfo(SearchLogInfo searchLogInfo) {
        return searchLogInfoMapper.updateSearchLogInfo(searchLogInfo);
    }

    /**
     * 批量删除用户搜索记录
     *
     * @param searchIds 需要删除的用户搜索记录主键
     * @return 结果
     */
    @Override
    public int deleteSearchLogInfoBySearchIds(String[] searchIds) {
        return searchLogInfoMapper.deleteSearchLogInfoBySearchIds(searchIds);
    }

    /**
     * 删除用户搜索记录信息
     *
     * @param searchId 用户搜索记录主键
     * @return 结果
     */
    @Override
    public int deleteSearchLogInfoBySearchId(String searchId) {
        return searchLogInfoMapper.deleteSearchLogInfoBySearchId(searchId);
    }

    //endregion
    @Override
    public QueryWrapper<SearchLogInfo> getQueryWrapper(SearchLogInfoQuery searchLogInfoQuery) {
        QueryWrapper<SearchLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = searchLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String searchId = searchLogInfoQuery.getSearchId();
        queryWrapper.eq(StringUtils.isNotEmpty(searchId), "search_id", searchId);

        String userId = searchLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String keyword = searchLogInfoQuery.getKeyword();
        queryWrapper.like(StringUtils.isNotEmpty(keyword), "keyword", keyword);

        String searchType = searchLogInfoQuery.getSearchType();
        queryWrapper.eq(StringUtils.isNotEmpty(searchType), "search_type", searchType);

        String referSource = searchLogInfoQuery.getReferSource();
        queryWrapper.eq(StringUtils.isNotEmpty(referSource), "refer_source", referSource);

        String searchStatus = searchLogInfoQuery.getSearchStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(searchStatus), "search_status", searchStatus);

        Long resultCount = searchLogInfoQuery.getResultCount();
        queryWrapper.eq(StringUtils.isNotNull(resultCount), "result_count", resultCount);

        Date createTime = searchLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Long searchDuration = searchLogInfoQuery.getSearchDuration();
        queryWrapper.eq(StringUtils.isNotNull(searchDuration), "search_duration", searchDuration);

        String deviceId = searchLogInfoQuery.getDeviceId();
        queryWrapper.like(StringUtils.isNotEmpty(deviceId), "device_id", deviceId);

        String browser = searchLogInfoQuery.getBrowser();
        queryWrapper.like(StringUtils.isNotEmpty(browser), "browser", browser);

        String os = searchLogInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os), "os", os);

        String platform = searchLogInfoQuery.getPlatform();
        queryWrapper.like(StringUtils.isNotEmpty(platform), "platform", platform);

        String ipAddress = searchLogInfoQuery.getIpAddress();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddress), "ip_address", ipAddress);

        String ipAddr = searchLogInfoQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr), "ip_addr", ipAddr);

        return queryWrapper;
    }

    @Override
    public List<SearchLogInfoVo> convertVoList(List<SearchLogInfo> searchLogInfoList) {
        if (StringUtils.isEmpty(searchLogInfoList)) {
            return Collections.emptyList();
        }
        return searchLogInfoList.stream().map(SearchLogInfoVo::objToVo).collect(Collectors.toList());
    }

}
