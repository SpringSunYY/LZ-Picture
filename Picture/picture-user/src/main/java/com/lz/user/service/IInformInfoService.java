package com.lz.user.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.user.model.domain.InformInfo;
import com.lz.user.model.dto.informInfo.UserInformInfoQuery;
import com.lz.user.model.vo.informInfo.InformInfoVo;
import com.lz.user.model.dto.informInfo.InformInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 用户通知记录Service接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface IInformInfoService extends IService<InformInfo> {
    //region mybatis代码

    /**
     * 查询用户通知记录
     *
     * @param recordId 用户通知记录主键
     * @return 用户通知记录
     */
    public InformInfo selectInformInfoByRecordId(String recordId);

    /**
     * 查询用户通知记录列表
     *
     * @param informInfo 用户通知记录
     * @return 用户通知记录集合
     */
    public List<InformInfo> selectInformInfoList(InformInfo informInfo);

    /**
     * 新增用户通知记录
     *
     * @param informInfo 用户通知记录
     * @return 结果
     */
    public int insertInformInfo(InformInfo informInfo);

    /**
     * 修改用户通知记录
     *
     * @param informInfo 用户通知记录
     * @return 结果
     */
    public int updateInformInfo(InformInfo informInfo);

    /**
     * 批量删除用户通知记录
     *
     * @param recordIds 需要删除的用户通知记录主键集合
     * @return 结果
     */
    public int deleteInformInfoByRecordIds(String[] recordIds);

    /**
     * 删除用户通知记录信息
     *
     * @param recordId 用户通知记录主键
     * @return 结果
     */
    public int deleteInformInfoByRecordId(String recordId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param informInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<InformInfo> getQueryWrapper(InformInfoQuery informInfoQuery);

    /**
     * 转换vo
     *
     * @param informInfoList InformInfo集合
     * @return InformInfoVO集合
     */
    List<InformInfoVo> convertVoList(List<InformInfo> informInfoList);

    /**
     * 发送通知
     *
     * @param userId       用户id
     * @param local        语言
     * @param templateKey  模板key
     * @param templateType 模板类型
     * @param informType   通知类型
     * @param params       参数
     * @return 结果
     */
    int sendInform(String userId, String templateKey, String local, String templateType, String informType, Map<String, String> params);

    /**
     * 查询用户通知列表
     *
     * @param userInformInfoQuery 查询条件
     * @return Page<InformInfo>
     * @author: YY
     * @method: selectUserInformInfoList
     * @date: 2025/5/28 23:33
     **/
    Page<InformInfo> selectUserInformInfoList(UserInformInfoQuery userInformInfoQuery);

    /**
     * 获取用户未读信息数
     *
     * @param userId 用户编号
     * @return int
     * @author: YY
     * @method: getUnReadInformCount
     * @date: 2025/5/29 22:57
     **/
    Integer getUnReadInformCount(String userId);

    /**
     * 用户查询自己的通知记录详情
     *
     * @param recordId 通知记录编号
     * @param userId   用户编号
     * @return InformInfo
     * @author: YY
     * @method: selectInformInfoByRecordIdAndUserId
     * @date: 2025/5/29 23:04
     **/
    InformInfo selectInformInfoByRecordIdAndUserId(String recordId, String userId);

    /**
     * 一键已读
     * @author: YY
     * @method: resetRead
     * @date: 2025/7/25 22:14
     * @param userId
     * @return int
     **/
    int resetRead(String userId);
}
