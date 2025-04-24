package com.lz.config.service;

import java.util.List;
import com.lz.config.model.domain.FileLogInfo;
import com.lz.config.model.vo.fileLogInfo.FileLogInfoVo;
import com.lz.config.model.dto.fileLogInfo.FileLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 文件日志Service接口
 *
 * @author YY
 * @date 2025-04-24
 */
public interface IFileLogInfoService extends IService<FileLogInfo>
{
    //region mybatis代码
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
     * 批量删除文件日志
     *
     * @param logIds 需要删除的文件日志主键集合
     * @return 结果
     */
    public int deleteFileLogInfoByLogIds(String[] logIds);

    /**
     * 删除文件日志信息
     *
     * @param logId 文件日志主键
     * @return 结果
     */
    public int deleteFileLogInfoByLogId(String logId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param fileLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<FileLogInfo> getQueryWrapper(FileLogInfoQuery fileLogInfoQuery);

    /**
     * 转换vo
     *
     * @param fileLogInfoList FileLogInfo集合
     * @return FileLogInfoVO集合
     */
    List<FileLogInfoVo> convertVoList(List<FileLogInfo> fileLogInfoList);
}
