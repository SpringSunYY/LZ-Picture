package com.lz.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.picture.model.domain.UserReportInfo;
import com.lz.picture.model.dto.userReportInfo.UserReportInfoQuery;
import com.lz.picture.model.vo.userReportInfo.UserReportInfoVo;

import java.util.List;

/**
 * 用户举报信息Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IUserReportInfoService extends IService<UserReportInfo> {
    //region mybatis代码

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
     * 批量删除用户举报信息
     *
     * @param reportIds 需要删除的用户举报信息主键集合
     * @return 结果
     */
    public int deleteUserReportInfoByReportIds(String[] reportIds);

    /**
     * 删除用户举报信息信息
     *
     * @param reportId 用户举报信息主键
     * @return 结果
     */
    public int deleteUserReportInfoByReportId(String reportId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param userReportInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<UserReportInfo> getQueryWrapper(UserReportInfoQuery userReportInfoQuery);

    /**
     * 转换vo
     *
     * @param userReportInfoList UserReportInfo集合
     * @return UserReportInfoVO集合
     */
    List<UserReportInfoVo> convertVoList(List<UserReportInfo> userReportInfoList);

    int userInsertUserReportInfo(UserReportInfo userReportInfo);

    /**
     * 审核用户举报信息
     *
     * @param userReportInfo 用户举报信息
     * @return int
     * @author: YY
     * @method: auditUserReportInfo
     * @date: 2025/6/17 21:18
     **/
    int auditUserReportInfo(UserReportInfo userReportInfo);
}
