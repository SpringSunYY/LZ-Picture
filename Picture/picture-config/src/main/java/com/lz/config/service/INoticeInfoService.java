package com.lz.config.service;

import java.util.List;
import com.lz.config.model.domain.NoticeInfo;
import com.lz.config.model.vo.noticeInfo.NoticeInfoVo;
import com.lz.config.model.dto.noticeInfo.NoticeInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户公告Service接口
 *
 * @author YY
 * @date 2025-07-26
 */
public interface INoticeInfoService extends IService<NoticeInfo>
{
    //region mybatis代码
    /**
     * 查询用户公告
     *
     * @param noticeId 用户公告主键
     * @return 用户公告
     */
    public NoticeInfo selectNoticeInfoByNoticeId(String noticeId);

    /**
     * 查询用户公告列表
     *
     * @param noticeInfo 用户公告
     * @return 用户公告集合
     */
    public List<NoticeInfo> selectNoticeInfoList(NoticeInfo noticeInfo);

    /**
     * 新增用户公告
     *
     * @param noticeInfo 用户公告
     * @return 结果
     */
    public int insertNoticeInfo(NoticeInfo noticeInfo);

    /**
     * 修改用户公告
     *
     * @param noticeInfo 用户公告
     * @return 结果
     */
    public int updateNoticeInfo(NoticeInfo noticeInfo);

    /**
     * 批量删除用户公告
     *
     * @param noticeIds 需要删除的用户公告主键集合
     * @return 结果
     */
    public int deleteNoticeInfoByNoticeIds(String[] noticeIds);

    /**
     * 删除用户公告信息
     *
     * @param noticeId 用户公告主键
     * @return 结果
     */
    public int deleteNoticeInfoByNoticeId(String noticeId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param noticeInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<NoticeInfo> getQueryWrapper(NoticeInfoQuery noticeInfoQuery);

    /**
     * 转换vo
     *
     * @param noticeInfoList NoticeInfo集合
     * @return NoticeInfoVO集合
     */
    List<NoticeInfoVo> convertVoList(List<NoticeInfo> noticeInfoList);
}
