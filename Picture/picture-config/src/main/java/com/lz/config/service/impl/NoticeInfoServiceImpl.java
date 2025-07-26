package com.lz.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.mapper.NoticeInfoMapper;
import com.lz.config.model.domain.NoticeInfo;
import com.lz.config.model.dto.noticeInfo.NoticeInfoQuery;
import com.lz.config.model.enmus.CNoticeIsExhibitEnum;
import com.lz.config.model.vo.noticeInfo.NoticeInfoVo;
import com.lz.config.service.II18nLocaleInfoService;
import com.lz.config.service.INoticeInfoService;
import com.lz.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户公告Service业务层处理
 *
 * @author YY
 * @date 2025-07-26
 */
@Service
public class NoticeInfoServiceImpl extends ServiceImpl<NoticeInfoMapper, NoticeInfo> implements INoticeInfoService {
    @Resource
    private NoticeInfoMapper noticeInfoMapper;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private II18nLocaleInfoService ii18nLocaleInfoService;
    //region mybatis代码

    /**
     * 查询用户公告
     *
     * @param noticeId 用户公告主键
     * @return 用户公告
     */
    @Override
    public NoticeInfo selectNoticeInfoByNoticeId(String noticeId) {
        return noticeInfoMapper.selectNoticeInfoByNoticeId(noticeId);
    }

    /**
     * 查询用户公告列表
     *
     * @param noticeInfo 用户公告
     * @return 用户公告
     */
    @Override
    public List<NoticeInfo> selectNoticeInfoList(NoticeInfo noticeInfo) {
        List<NoticeInfo> noticeInfos = noticeInfoMapper.selectNoticeInfoList(noticeInfo);
        if (StringUtils.isNotEmpty(noticeInfos)) {
            List<Long> userIds = noticeInfos.stream().map(NoticeInfo::getUserId).toList();
            List<SysUser> users = sysUserService.selectUserByIds(userIds);
            //map key-userId value-username
            Map<Long, String> userMap = users.stream()
                    .collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
            noticeInfos.forEach(info -> info.setUserName(userMap.get(info.getUserId())));
        }
        return noticeInfos;
    }

    /**
     * 新增用户公告
     *
     * @param noticeInfo 用户公告
     * @return 结果
     */
    @Override
    public int insertNoticeInfo(NoticeInfo noticeInfo) {
        noticeInfo.setNoticeId(IdUtils.snowflakeId().toString());
        noticeInfo.setCreateTime(DateUtils.getNowDate());
        //查询如果此公告需要展示但是已经有展示了的更新他为不展示
        List<NoticeInfo> noticeInfoList = this.list(new LambdaQueryWrapper<NoticeInfo>().eq(NoticeInfo::getIsExhibit, CNoticeIsExhibitEnum.NOTICE_IS_EXHIBIT_1.getValue()));
        noticeInfoList.forEach(notice -> notice.setIsExhibit(CNoticeIsExhibitEnum.NOTICE_IS_EXHIBIT_0.getValue()));
        this.updateBatchById(noticeInfoList);
        return noticeInfoMapper.insertNoticeInfo(noticeInfo);
    }

    /**
     * 修改用户公告
     *
     * @param noticeInfo 用户公告
     * @return 结果
     */
    @Override
    public int updateNoticeInfo(NoticeInfo noticeInfo) {
        List<NoticeInfo> noticeInfoList = this.list(new LambdaQueryWrapper<NoticeInfo>().eq(NoticeInfo::getIsExhibit, CNoticeIsExhibitEnum.NOTICE_IS_EXHIBIT_1.getValue()));
        noticeInfoList.forEach(notice -> notice.setIsExhibit(CNoticeIsExhibitEnum.NOTICE_IS_EXHIBIT_0.getValue()));
        this.updateBatchById(noticeInfoList);
        noticeInfo.setUpdateTime(DateUtils.getNowDate());
        return noticeInfoMapper.updateNoticeInfo(noticeInfo);
    }

    /**
     * 批量删除用户公告
     *
     * @param noticeIds 需要删除的用户公告主键
     * @return 结果
     */
    @Override
    public int deleteNoticeInfoByNoticeIds(String[] noticeIds) {
        return noticeInfoMapper.deleteNoticeInfoByNoticeIds(noticeIds);
    }

    /**
     * 删除用户公告信息
     *
     * @param noticeId 用户公告主键
     * @return 结果
     */
    @Override
    public int deleteNoticeInfoByNoticeId(String noticeId) {
        return noticeInfoMapper.deleteNoticeInfoByNoticeId(noticeId);
    }

    //endregion
    @Override
    public QueryWrapper<NoticeInfo> getQueryWrapper(NoticeInfoQuery noticeInfoQuery) {
        QueryWrapper<NoticeInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = noticeInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String noticeId = noticeInfoQuery.getNoticeId();
        queryWrapper.eq(StringUtils.isNotEmpty(noticeId), "notice_id", noticeId);

        String locale = noticeInfoQuery.getLocale();
        queryWrapper.eq(StringUtils.isNotEmpty(locale), "locale", locale);

        String platform = noticeInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform), "platform", platform);

        String noticeType = noticeInfoQuery.getNoticeType();
        queryWrapper.eq(StringUtils.isNotEmpty(noticeType), "notice_type", noticeType);

        String isExhibit = noticeInfoQuery.getIsExhibit();
        queryWrapper.eq(StringUtils.isNotEmpty(isExhibit), "is_exhibit", isExhibit);

        String noticeTitle = noticeInfoQuery.getNoticeTitle();
        queryWrapper.like(StringUtils.isNotEmpty(noticeTitle), "notice_title", noticeTitle);

        String noticeStatus = noticeInfoQuery.getNoticeStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(noticeStatus), "notice_status", noticeStatus);

        Long userId = noticeInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotNull(userId), "user_id", userId);

        Date createTime = noticeInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = noticeInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<NoticeInfoVo> convertVoList(List<NoticeInfo> noticeInfoList) {
        if (StringUtils.isEmpty(noticeInfoList)) {
            return Collections.emptyList();
        }
        return noticeInfoList.stream().map(NoticeInfoVo::objToVo).collect(Collectors.toList());
    }

}
