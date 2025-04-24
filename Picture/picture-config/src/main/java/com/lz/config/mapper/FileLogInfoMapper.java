package com.lz.config.mapper;

import java.util.List;
import com.lz.config.model.domain.FileLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 文件日志Mapper接口
 *
 * @author YY
 * @date 2025-04-24
 */
public interface FileLogInfoMapper extends BaseMapper<FileLogInfo>
{
    /**
     * 查询文件日志
     *
     * @param logId 文件日志主键
     * @return 文件日志
     */
    public FileLogInfo selectFileLogInfoByLogId(String logId);

    /**
     * 查询文件日志列表
     *
     * @param fileLogInfo 文件日志
     * @return 文件日志集合
     */
    public List<FileLogInfo> selectFileLogInfoList(FileLogInfo fileLogInfo);

    /**
     * 新增文件日志
     *
     * @param fileLogInfo 文件日志
     * @return 结果
     */
    public int insertFileLogInfo(FileLogInfo fileLogInfo);

    /**
     * 修改文件日志
     *
     * @param fileLogInfo 文件日志
     * @return 结果
     */
    public int updateFileLogInfo(FileLogInfo fileLogInfo);

    /**
     * 删除文件日志
     *
     * @param logId 文件日志主键
     * @return 结果
     */
    public int deleteFileLogInfoByLogId(String logId);

    /**
     * 批量删除文件日志
     *
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFileLogInfoByLogIds(String[] logIds);
}
