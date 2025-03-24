package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.UserReportInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户举报信息Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface UserReportInfoMapper extends BaseMapper<UserReportInfo>
{
    /**
     * 查询用户举报信息
     *
     * @param reportId 用户举报信息主键
     * @return 用户举报信息
     */
    public UserReportInfo selectUserReportInfoByReportId(String reportId);

    /**
     * 查询用户举报信息列表
     *
     * @param userReportInfo 用户举报信息
     * @return 用户举报信息集合
     */
    public List<UserReportInfo> selectUserReportInfoList(UserReportInfo userReportInfo);

    /**
     * 新增用户举报信息
     *
     * @param userReportInfo 用户举报信息
     * @return 结果
     */
    public int insertUserReportInfo(UserReportInfo userReportInfo);

    /**
     * 修改用户举报信息
     *
     * @param userReportInfo 用户举报信息
     * @return 结果
     */
    public int updateUserReportInfo(UserReportInfo userReportInfo);

    /**
     * 删除用户举报信息
     *
     * @param reportId 用户举报信息主键
     * @return 结果
     */
    public int deleteUserReportInfoByReportId(String reportId);

    /**
     * 批量删除用户举报信息
     *
     * @param reportIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserReportInfoByReportIds(String[] reportIds);
}
