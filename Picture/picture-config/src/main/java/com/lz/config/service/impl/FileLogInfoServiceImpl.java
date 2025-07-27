package com.lz.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomSort;
import com.lz.common.config.OssConfig;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.common.manager.file.model.FileResponse;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.mapper.FileLogInfoMapper;
import com.lz.config.model.domain.FileLogInfo;
import com.lz.config.model.dto.fileLogInfo.FileLogInfoQuery;
import com.lz.config.model.dto.fileLogInfo.FileLogUpdate;
import com.lz.config.model.enmus.CFileLogIsCompressEnum;
import com.lz.config.model.enmus.CFileLogOssTypeEnum;
import com.lz.config.model.enmus.CFileLogStatusEnum;
import com.lz.config.model.vo.fileLogInfo.FileLogInfoVo;
import com.lz.config.service.IFileLogInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件日志Service业务层处理
 *
 * @author YY
 * @date 2025-04-24
 */
@Service
public class FileLogInfoServiceImpl extends ServiceImpl<FileLogInfoMapper, FileLogInfo> implements IFileLogInfoService {
    @Resource
    private FileLogInfoMapper fileLogInfoMapper;


    @Resource
    private PictureUploadManager pictureUploadManager;


    //region mybatis代码

    /**
     * 查询文件日志
     *
     * @param logId 文件日志主键
     * @return 文件日志
     */
    @Override
    public FileLogInfo selectFileLogInfoByLogId(String logId) {
        return fileLogInfoMapper.selectFileLogInfoByLogId(logId);
    }

    /**
     * 查询文件日志列表
     *
     * @param fileLogInfo 文件日志
     * @return 文件日志
     */
    @CustomSort(sortFields = {"createTime", "deleteTime"}, sortMappingFields = {"create_time", "delete_time"})
    @Override
    public List<FileLogInfo> selectFileLogInfoList(FileLogInfo fileLogInfo) {
        List<FileLogInfo> fileLogInfos = fileLogInfoMapper.selectFileLogInfoList(fileLogInfo);
        for (FileLogInfo info : fileLogInfos) {
            if (StringUtils.isNotEmpty(info.getDnsUrl())) {
                info.setFileUrl(OssConfig.getDnsUrl() + info.getFileUrl());
            } else {
                info.setFileUrl(OssConfig.getDnsUrl() + info.getFileUrl());
            }
        }
        return fileLogInfos;
    }

    /**
     * 新增文件日志
     *
     * @param fileLogInfo 文件日志
     * @return 结果
     */
    @Override
    public int insertFileLogInfo(FileLogInfo fileLogInfo) {
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
    public int updateFileLogInfo(FileLogInfo fileLogInfo) {
        return fileLogInfoMapper.updateFileLogInfo(fileLogInfo);
    }

    /**
     * 批量删除文件日志
     *
     * @param logIds 需要删除的文件日志主键
     * @return 结果
     */
    @Override
    public int deleteFileLogInfoByLogIds(String[] logIds) {
        return fileLogInfoMapper.deleteFileLogInfoByLogIds(logIds);
    }

    /**
     * 删除文件日志信息
     *
     * @param logId 文件日志主键
     * @return 结果
     */
    @Override
    public int deleteFileLogInfoByLogId(String logId) {
        return fileLogInfoMapper.deleteFileLogInfoByLogId(logId);
    }

    //endregion
    @Override
    public QueryWrapper<FileLogInfo> getQueryWrapper(FileLogInfoQuery fileLogInfoQuery) {
        QueryWrapper<FileLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = fileLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String logId = fileLogInfoQuery.getLogId();
        queryWrapper.eq(StringUtils.isNotEmpty(logId), "log_id", logId);

        String userId = fileLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String fileType = fileLogInfoQuery.getFileType();
        queryWrapper.eq(StringUtils.isNotEmpty(fileType), "file_type", fileType);

        String logStatus = fileLogInfoQuery.getLogStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(logStatus), "log_status", logStatus);

        Date createTime = fileLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date deleteTime = fileLogInfoQuery.getDeleteTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginDeleteTime")) && StringUtils.isNotNull(params.get("endDeleteTime")), "delete_time", params.get("beginDeleteTime"), params.get("endDeleteTime"));

        String deviceId = fileLogInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId), "device_id", deviceId);

        String browser = fileLogInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser), "browser", browser);

        String os = fileLogInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os), "os", os);

        String platform = fileLogInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform), "platform", platform);

        String ipAddress = fileLogInfoQuery.getIpAddress();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddress), "ip_address", ipAddress);

        return queryWrapper;
    }

    @Override
    public List<FileLogInfoVo> convertVoList(List<FileLogInfo> fileLogInfoList) {
        if (StringUtils.isEmpty(fileLogInfoList)) {
            return Collections.emptyList();
        }
        return fileLogInfoList.stream().map(FileLogInfoVo::objToVo).collect(Collectors.toList());
    }


    @Override
    public void recordFileLog(FileResponse fileResponse, String userId, String ossType, String logType, DeviceInfo deviceInfo) {
        FileLogInfo fileLogInfo = new FileLogInfo();
        BeanUtils.copyProperties(deviceInfo, fileLogInfo);
        //设置对应值
        fileLogInfo.setUserId(userId);
        fileLogInfo.setDnsUrl(fileResponse.getDnsUrl());
        //如果是官方
        if (!CFileLogOssTypeEnum.OSS_TYPE_0.getValue().equals(ossType)) {
            fileLogInfo.setOssType(ossType);
        }
        fileLogInfo.setLogType(logType);
        fileLogInfo.setCreateTime(DateUtils.getNowDate());
        fileLogInfo.setFileType(fileResponse.getPicFormat());
        fileLogInfo.setLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
        if (StringUtils.isNotEmpty(fileResponse.getUrl())) {
            fileLogInfo.setFileUrl(fileResponse.getUrl());
            //默认冗余
            //先插入原图
            fileLogInfo.setIsCompress(CFileLogIsCompressEnum.LOG_IS_COMPRESS_1.getValue());
            fileLogInfo.setLogId(IdUtils.fastUUID());
            fileLogInfoMapper.insertFileLogInfo(fileLogInfo);
        }
        if (StringUtils.isNotEmpty(fileResponse.getThumbnailUrl())) {
            //其次压缩
            //压缩图片全部webp
            fileLogInfo.setFileType("webp");
            fileLogInfo.setIsCompress(CFileLogIsCompressEnum.LOG_IS_COMPRESS_0.getValue());
            fileLogInfo.setFileUrl(fileResponse.getThumbnailUrl());
            fileLogInfo.setLogId(IdUtils.fastUUID());
            fileLogInfoMapper.insertFileLogInfo(fileLogInfo);
        }
    }

    @Override
    public int updateFileLog(FileLogUpdate fileLogUpdate) {

        //判断是否传来图片地址
        if (StringUtils.isNotEmpty(fileLogUpdate.getPictureUrl())) {
            //查询到该图片
            LambdaQueryWrapper<FileLogInfo> eq = new LambdaQueryWrapper<FileLogInfo>()
                    .eq(FileLogInfo::getLogStatus, fileLogUpdate.getQueryLogStatus())
                    .eq(FileLogInfo::getLogType, fileLogUpdate.getQueryLogType())
                    .eq(FileLogInfo::getUserId, fileLogUpdate.getUserId())
                    .eq(FileLogInfo::getFileUrl, fileLogUpdate.getPictureUrl());
            FileLogInfo fileLogInfo = this.getOne(eq);
            //如果不为空更新
            if (StringUtils.isNotNull(fileLogInfo)) {
                fileLogInfo.setLogStatus(fileLogUpdate.getUpdateLogStatus());
                fileLogInfo.setTargetContent(fileLogUpdate.getTargetContent());
                //正常表示无需删除
                fileLogInfo.setTargetId(fileLogUpdate.getTargetId());
                this.updateFileLogInfo(fileLogInfo);
            }
        }
        //判断传来的压缩图片
        if (StringUtils.isNotEmpty(fileLogUpdate.getThumbnailUrl())) {
            //查询到该图片
            LambdaQueryWrapper<FileLogInfo> eq = new LambdaQueryWrapper<FileLogInfo>()
                    .eq(FileLogInfo::getLogStatus, fileLogUpdate.getQueryLogStatus())
                    .eq(FileLogInfo::getLogType, fileLogUpdate.getQueryLogType())
                    .eq(FileLogInfo::getUserId, fileLogUpdate.getUserId())
                    .eq(FileLogInfo::getFileUrl, fileLogUpdate.getThumbnailUrl());
            FileLogInfo fileLogInfo = this.getOne(eq);
            //如果不为空更新
            if (StringUtils.isNotNull(fileLogInfo)) {
                fileLogInfo.setTargetContent(fileLogUpdate.getTargetContent());
                fileLogInfo.setTargetId(fileLogUpdate.getTargetId());
                //正常表示无需删除
                fileLogInfo.setLogStatus(fileLogUpdate.getUpdateLogStatus());
                this.updateFileLogInfo(fileLogInfo);
            }
        }
        return 1;
    }

    @Override
    public int autoDeleteFile(Integer size, Integer days) {
        //根据页数查询到第几天的数据
        Page<FileLogInfo> page = new Page<>(1, size);
        LambdaQueryWrapper<FileLogInfo> queryWrapper = new LambdaQueryWrapper<FileLogInfo>()
                .eq(FileLogInfo::getLogStatus, CFileLogStatusEnum.LOG_STATUS_0.getValue())
                //时间精确到日
                .apply("DATE_FORMAT(create_time, '%Y-%m-%d') = DATE_SUB(CURDATE(), INTERVAL {0} DAY)", days);
        Page<FileLogInfo> result = this.page(page, queryWrapper);
        List<FileLogInfo> logInfos = result.getRecords();
        //获取到所有的文件路径
        Date deleteDate = DateUtils.getNowDate();
        String logStatus = CFileLogStatusEnum.LOG_STATUS_2.getValue();
        List<String> urlList = logInfos.stream()
                .map(log -> {
                    log.setDeleteTime(deleteDate);
                    log.setLogStatus(logStatus);
                    //去掉文件第一个/
                    if (log.getFileUrl().startsWith("/")) {
                        return log.getFileUrl().substring(1);
                    }
                    return log.getFileUrl();
                })
                .toList();
        //如果不为空则删除
        if (StringUtils.isNotEmpty(urlList)) {
            pictureUploadManager.deleteFile(urlList);
        }
        //不需要事务
        //获取到总数
        this.updateBatchById(logInfos);
        long total = result.getTotal();
        if (total > 0) {
            //如果总数不为0则表示还有数据没有删除则直接删除
            this.autoDeleteFile(size, days);
        }
        return 1;
    }
}
