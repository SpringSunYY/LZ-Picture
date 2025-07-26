package com.lz.config.mapper;

import java.util.List;
import com.lz.config.model.domain.NoticeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户公告Mapper接口
 *
 * @author YY
 * @date 2025-07-26
 */
public interface NoticeInfoMapper extends BaseMapper<NoticeInfo>
{
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
     * 删除用户公告
     *
     * @param noticeId 用户公告主键
     * @return 结果
     */
    public int deleteNoticeInfoByNoticeId(String noticeId);

    /**
     * 批量删除用户公告
     *
     * @param noticeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNoticeInfoByNoticeIds(String[] noticeIds);
}
