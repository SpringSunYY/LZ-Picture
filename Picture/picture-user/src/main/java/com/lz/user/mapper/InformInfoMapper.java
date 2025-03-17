package com.lz.user.mapper;

import java.util.List;
import com.lz.user.model.domain.InformInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户通知记录Mapper接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface InformInfoMapper extends BaseMapper<InformInfo>
{
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
     * 删除用户通知记录
     *
     * @param recordId 用户通知记录主键
     * @return 结果
     */
    public int deleteInformInfoByRecordId(String recordId);

    /**
     * 批量删除用户通知记录
     *
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInformInfoByRecordIds(String[] recordIds);
}
