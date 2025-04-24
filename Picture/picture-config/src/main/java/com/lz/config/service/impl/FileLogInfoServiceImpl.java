package com.lz.config.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lz.common.utils.StringUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.config.mapper.FileLogInfoMapper;
import com.lz.config.model.domain.FileLogInfo;
import com.lz.config.service.IFileLogInfoService;
import com.lz.config.model.dto.fileLogInfo.FileLogInfoQuery;
import com.lz.config.model.vo.fileLogInfo.FileLogInfoVo;

/**
 * 文件日志Service业务层处理
 *
 * @author YY
 * @date 2025-04-24
 */
@Service
public class FileLogInfoServiceImpl extends ServiceImpl<FileLogInfoMapper, FileLogInfo> implements IFileLogInfoService
{
    @Resource
    private FileLogInfoMapper fileLogInfoMapper;

    //region mybatis代码
    /**
     * 查询文件日志
     *
     * @param logId 文件日志主键
     * @return 文件日志
     */
    @Override
    public FileLogInfo selectFileLogInfoByLogId(String logId)
    {
        return fileLogInfoMapper.selectFileLogInfoByLogId(logId);
    }

    /**
     * 查询文件日志列表
     *
     * @param fileLogInfo 文件日志
     * @return 文件日志
     */
    @Override
    public List<FileLogInfo> selectFileLogInfoList(FileLogInfo fileLogInfo)
    {
        return fileLogInfoMapper.selectFileLogInfoList(fileLogInfo);
    }

    /**
     * 新增文件日志
     *
     * @param fileLogInfo 文件日志
     * @return 结果
     */
    @Override
    public int insertFileLogInfo(FileLogInfo fileLogInfo)
    {
        fileLogInfo.setCreateTime(DateUtils.getNowDate());
        return fileLogInfoMapper.insertFileLogInfo(fileLogInfo);
    }

    /**
     * 修改文件日志
     *
     * @param fileLogInfo 文件日志
     * @return 结果
     */
    @Override
    public int updateFileLogInfo(FileLogInfo fileLogInfo)
    {
        return fileLogInfoMapper.updateFileLogInfo(fileLogInfo);
    }

    /**
     * 批量删除文件日志
     *
     * @param logIds 需要删除的文件日志主键
     * @return 结果
     */
    @Override
    public int deleteFileLogInfoByLogIds(String[] logIds)
    {
        return fileLogInfoMapper.deleteFileLogInfoByLogIds(logIds);
    }

    /**
     * 删除文件日志信息
     *
     * @param logId 文件日志主键
     * @return 结果
     */
    @Override
    public int deleteFileLogInfoByLogId(String logId)
    {
        return fileLogInfoMapper.deleteFileLogInfoByLogId(logId);
    }
    //endregion
    @Override
    public QueryWrapper<FileLogInfo> getQueryWrapper(FileLogInfoQuery fileLogInfoQuery){
        QueryWrapper<FileLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = fileLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String logId = fileLogInfoQuery.getLogId();
        queryWrapper.eq(StringUtils.isNotEmpty(logId) ,"log_id",logId);

    String userId = fileLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String fileType = fileLogInfoQuery.getFileType();
        queryWrapper.eq(StringUtils.isNotEmpty(fileType) ,"file_type",fileType);

    String logStatus = fileLogInfoQuery.getLogStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(logStatus) ,"log_status",logStatus);

    Date createTime = fileLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    Date deleteTime = fileLogInfoQuery.getDeleteTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginDeleteTime"))&&StringUtils.isNotNull(params.get("endDeleteTime")),"delete_time",params.get("beginDeleteTime"),params.get("endDeleteTime"));

    String deviceId = fileLogInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId) ,"device_id",deviceId);

    String browser = fileLogInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser) ,"browser",browser);

    String os = fileLogInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os) ,"os",os);

    String platform = fileLogInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform) ,"platform",platform);

    String ipAddress = fileLogInfoQuery.getIpAddress();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddress) ,"ip_address",ipAddress);

        return queryWrapper;
    }

    @Override
    public List<FileLogInfoVo> convertVoList(List<FileLogInfo> fileLogInfoList) {
        if (StringUtils.isEmpty(fileLogInfoList)) {
            return Collections.emptyList();
        }
        return fileLogInfoList.stream().map(FileLogInfoVo::objToVo).collect(Collectors.toList());
    }

}
