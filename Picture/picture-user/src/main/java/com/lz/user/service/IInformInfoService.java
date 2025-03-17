package com.lz.user.service;

import java.util.List;
import com.lz.user.model.domain.InformInfo;
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
public interface IInformInfoService extends IService<InformInfo>
{
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
}
