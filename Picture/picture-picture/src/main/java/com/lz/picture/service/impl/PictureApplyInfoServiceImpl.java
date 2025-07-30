package com.lz.picture.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomCacheEvict;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.annotation.CustomSort;
import com.lz.common.config.OssConfig;
import com.lz.common.constant.redis.PictureRedisConstants;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.manager.file.PictureDownloadManager;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.config.model.enmus.CTemplateTypeEnum;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureFileLogAsyncFactory;
import com.lz.picture.mapper.PictureApplyInfoMapper;
import com.lz.picture.model.domain.PictureApplyInfo;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.pictureApplyInfo.PictureApplyInfoQuery;
import com.lz.picture.model.dto.pictureInfo.PictureMoreInfo;
import com.lz.picture.model.enums.*;
import com.lz.picture.model.vo.pictureApplyInfo.PictureApplyInfoVo;
import com.lz.picture.service.IPictureApplyInfoService;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.picture.utils.SpaceAuthUtils;
import com.lz.system.service.ISysConfigService;
import com.lz.user.manager.UserAsyncManager;
import com.lz.user.manager.factory.InformInfoAsyncFactory;
import com.lz.user.model.enums.UInformTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.ConfigConstants.PICTURE_P;
import static com.lz.common.constant.Constants.COMMON_SEPARATOR;
import static com.lz.common.constant.config.TemplateInfoKeyConstants.PICTURE_APPLY_REVIEW;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_APPLY_DETAIL;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_APPLY_DETAIL_EXPIRE_TIME;
import static com.lz.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;
import static com.lz.config.utils.ConfigInfoUtils.PICTURE_INDEX_P_VALUE;

/**
 * 图片申请信息Service业务层处理
 *
 * @author YY
 * @date 2025-06-17
 */
@Service
public class PictureApplyInfoServiceImpl extends ServiceImpl<PictureApplyInfoMapper, PictureApplyInfo> implements IPictureApplyInfoService {
    @Resource
    private PictureApplyInfoMapper pictureApplyInfoMapper;

    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private SpaceAuthUtils spaceAuthUtils;

    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    private PictureDownloadManager pictureDownloadManager;

    @Resource
    private ISysConfigService sysConfigService;


    //region mybatis代码

    /**
     * 查询图片申请信息
     *
     * @param applyId 图片申请信息主键
     * @return 图片申请信息
     */
    @CustomCacheable(keyPrefix = PICTURE_APPLY_DETAIL, keyField = "applyId", expireTime = PICTURE_APPLY_DETAIL_EXPIRE_TIME)
    @Override
    public PictureApplyInfo selectPictureApplyInfoByApplyId(String applyId) {
        PictureApplyInfo pictureApplyInfo = pictureApplyInfoMapper.selectPictureApplyInfoByApplyId(applyId);
        //压缩图片
        if (StringUtils.isNotNull(pictureApplyInfo)) {
            String inCache = sysConfigService.selectConfigByKey(PICTURE_P);
            Integer p = Integer.valueOf(inCache);
            String url = builderPictureUrl(pictureApplyInfo.getApplyImage(), p);
            pictureApplyInfo.setApplyImage(url);
            String pictureUrl = builderPictureUrl(pictureApplyInfo.getThumbnailUrl(), p);
            pictureApplyInfo.setThumbnailUrl(pictureUrl);
            String fileUrl = builderFileUrl(pictureApplyInfo.getApplyFile());
            pictureApplyInfo.setApplyFile(fileUrl);
        }
        return pictureApplyInfo;
    }

    /**
     * 查询图片申请信息列表
     *
     * @param pictureApplyInfo 图片申请信息
     * @return 图片申请信息
     */
    @CustomSort(sortFields = {"createTime","reviewTime","updateTime"},sortMappingFields = {"create_time","review_time","update_time"})
    @Override
    public List<PictureApplyInfo> selectPictureApplyInfoList(PictureApplyInfo pictureApplyInfo) {
        List<PictureApplyInfo> pictureApplyInfos = pictureApplyInfoMapper.selectPictureApplyInfoList(pictureApplyInfo);
        //压缩图片
        for (PictureApplyInfo info : pictureApplyInfos) {
            builderUrl(info, PICTURE_INDEX_P_VALUE);
        }
        return pictureApplyInfos;
    }

    private void builderUrl(PictureApplyInfo info, Integer p) {
        //构建url
        if (StringUtils.isNotEmpty(info.getApplyImage())) {
            String url = builderPictureUrl(info.getApplyImage(), p);
            info.setApplyImage(url);
        }
        if (StringUtils.isNotEmpty(info.getApplyFile())) {
            String url = builderUrl(info.getApplyFile());
            info.setApplyFile(url);
        }
        if (StringUtils.isNotEmpty(info.getThumbnailUrl())) {
            info.setThumbnailUrl(builderUrl(info.getThumbnailUrl()));
        }
    }

    private String builderPictureUrl(String urls, Integer p) {
        if (StringUtils.isEmpty(urls)) {
            return "";
        }
        String[] split = urls.split(COMMON_SEPARATOR);
        StringBuilder buffer = new StringBuilder();
        for (String str : split) {
            buffer.append(OssConfig.builderUrl(str)).append("?x-oss-process=image/resize,p_").append(p).append(COMMON_SEPARATOR);
        }
        //删除尾部逗号
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    private String builderUrl(String urls) {
        if (StringUtils.isEmpty(urls)) {
            return "";
        }
        String[] split = urls.split(COMMON_SEPARATOR);
        StringBuilder buffer = new StringBuilder();
        for (String str : split) {
            buffer.append(OssConfig.builderUrl(str)).append(COMMON_SEPARATOR);
        }
        //删除尾部逗号
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    private String builderFileUrl(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return "";
        }
        String[] split = fileUrl.split(COMMON_SEPARATOR);
        StringBuilder buffer = new StringBuilder();
        for (String str : split) {
            String downloadUrl = pictureDownloadManager.generateDownloadUrl(str, 60L);
            buffer.append(downloadUrl).append(COMMON_SEPARATOR);
        }
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    /**
     * 新增图片申请信息
     *
     * @param pictureApplyInfo 图片申请信息
     * @return 结果
     */
    @Override
    public int insertPictureApplyInfo(PictureApplyInfo pictureApplyInfo) {
        pictureApplyInfo.setCreateTime(DateUtils.getNowDate());
        return pictureApplyInfoMapper.insertPictureApplyInfo(pictureApplyInfo);
    }

    /**
     * 修改图片申请信息
     *
     * @param pictureApplyInfo 图片申请信息
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = PICTURE_APPLY_DETAIL, keyFields = {"pictureApplyInfo.applyId"})
    @Override
    public int updatePictureApplyInfo(PictureApplyInfo pictureApplyInfo) {
        //获取数据库数据
        PictureApplyInfo db = selectPictureApplyInfoByApplyId(pictureApplyInfo.getApplyId());
        ThrowUtils.throwIf(StringUtils.isNull(db), "图片申请信息不存在");
        ThrowUtils.throwIf(db.getReviewStatus().equals(PPictureApplyStatusEnum.PICTURE_APPLY_STATUS_1.getValue()), "图片申请信息已审核通过，请勿重复操作");

        //查询图片
        PictureInfo pictureInfo = pictureInfoService.selectNormalPictureInfoByPictureId(pictureApplyInfo.getPictureId());
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo), "图片不存在");
        ThrowUtils.throwIf(pictureInfo.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue()), "图片已发布，请勿重复操作");
        //如果两者状态不一样表示要审核
        Date nowDate = DateUtils.getNowDate();
        if (!db.getReviewStatus().equals(pictureApplyInfo.getReviewStatus())) {
            pictureApplyInfo.setReviewTime(nowDate);
            pictureApplyInfo.setReviewUserId(SecurityUtils.getUserId());
            pictureInfo.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_0.getValue());
            //如果是通过
            if (pictureApplyInfo.getReviewStatus().equals(PPictureApplyStatusEnum.PICTURE_APPLY_STATUS_1.getValue())) {
                pictureInfo.setPublishTime(nowDate);
                //如果传过来的积分不为空，判断是否为10的倍数或者0
                ThrowUtils.throwIf(StringUtils.isNotNull(pictureApplyInfo.getPointsNeed())
                        && pictureApplyInfo.getPointsNeed() % 10 != 0
                        || pictureApplyInfo.getPointsNeed() < 0, "积分必须是10的倍数或者0");
                pictureInfo.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_0.getValue());
                String moreInfo = pictureInfo.getMoreInfo();
                PictureMoreInfo pictureMoreInfo = new PictureMoreInfo();
                if (StringUtils.isNotEmpty(moreInfo)) {
                    pictureMoreInfo = JSON.parseObject(moreInfo, PictureMoreInfo.class);
                }
                //是原创作品 原创作品可以设置 价格 其他的可以设置积分
                if (pictureApplyInfo.getApplyType().equals(PPictureApplyTypeEnum.PICTURE_APPLY_TYPE_0.getValue())) {
                    pictureMoreInfo.setPointsNeed(pictureApplyInfo.getPointsNeed());
                    pictureMoreInfo.setPriceNeed(pictureApplyInfo.getPriceNeed());
                } else {
                    pictureMoreInfo.setPriceNeed(BigDecimal.valueOf(0));
                    pictureMoreInfo.setPointsNeed(pictureApplyInfo.getPointsNeed());
                }
                pictureInfo.setName(pictureApplyInfo.getPictureName());
                pictureMoreInfo.setApplyType(pictureApplyInfo.getApplyType());
                pictureInfo.setMoreInfo(JSON.toJSONString(pictureMoreInfo));
            }
            pictureInfoService.updatePictureInfo(pictureInfo);
            //发送消息
            /*
                {
                  "pictureId": "123456",
                  "pictureName": "夕阳下的海岸",
                  "applyTime": "2025-06-25 14:30:00",
                  "applyType": "原创作品",
                  "applyReason": "用于毕业设计展示项目",
                  "reviewStatue": "已通过",
                  "reviewMessage": "图片质量良好，符合平台规范。",
                  "reviewTime": "2025-06-26 10:15:00"
                }
             */
            HashMap<String, String> params = new HashMap<>();
            params.put("pictureId", pictureApplyInfo.getPictureId());
            params.put("pictureName", pictureApplyInfo.getPictureName());
            params.put("applyTime", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, db.getCreateTime()));
            params.put("applyReason", pictureApplyInfo.getApplyReason());
            Optional<PPictureApplyStatusEnum> enumByValue = PPictureApplyStatusEnum.getEnumByValue(pictureApplyInfo.getReviewStatus());
            if (enumByValue.isPresent()) {
                params.put("reviewStatue", enumByValue.get().getLabel());
            } else {
                params.put("reviewStatue", "未知类型"); // 默认值
            }
            Optional<PPictureApplyTypeEnum> applyTypeEnum = PPictureApplyTypeEnum.getEnumByValue(pictureApplyInfo.getApplyType());
            if (applyTypeEnum.isPresent()) {
                params.put("applyType", applyTypeEnum.get().getLabel());
            } else {
                params.put("applyType", "未知类型"); // 默认值
            }
            if (StringUtils.isEmpty(pictureApplyInfo.getReviewMessage())) {
                pictureApplyInfo.setReviewMessage("您的图片已经审核，感谢您的支持");
            }
            params.put("reviewMessage", pictureApplyInfo.getReviewMessage());
            params.put("reviewTime", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, pictureApplyInfo.getReviewTime()));
            UserAsyncManager.me().execute(InformInfoAsyncFactory.sendInform(
                    db.getUserId(),
                    PICTURE_APPLY_REVIEW,
                    null,
                    CTemplateTypeEnum.TEMPLATE_TYPE_3.getValue(),
                    UInformTypeEnum.INFORM_TYPE_0.getValue(),
                    params
            ));
        }
        //删除缓存
        String key = PictureRedisConstants.PICTURE_PICTURE_DETAIL + pictureApplyInfo.getPictureId();
        redisCache.deleteObject(key);
        pictureApplyInfo.setUpdateTime(nowDate);
        return pictureApplyInfoMapper.updatePictureApplyInfo(pictureApplyInfo);
    }

    /**
     * 批量删除图片申请信息
     *
     * @param applyIds 需要删除的图片申请信息主键
     * @return 结果
     */
    @Override
    public int deletePictureApplyInfoByApplyIds(String[] applyIds) {
        return pictureApplyInfoMapper.deletePictureApplyInfoByApplyIds(applyIds);
    }

    /**
     * 删除图片申请信息信息
     *
     * @param applyId 图片申请信息主键
     * @return 结果
     */
    @Override
    public int deletePictureApplyInfoByApplyId(String applyId) {
        return pictureApplyInfoMapper.deletePictureApplyInfoByApplyId(applyId);
    }

    //endregion
    @Override
    public QueryWrapper<PictureApplyInfo> getQueryWrapper(PictureApplyInfoQuery pictureApplyInfoQuery) {
        QueryWrapper<PictureApplyInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureApplyInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String applyId = pictureApplyInfoQuery.getApplyId();
        queryWrapper.eq(StringUtils.isNotEmpty(applyId), "apply_id", applyId);

        String pictureId = pictureApplyInfoQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId), "picture_id", pictureId);

        String pictureName = pictureApplyInfoQuery.getPictureName();
        queryWrapper.like(StringUtils.isNotEmpty(pictureName), "picture_name", pictureName);

        String applyType = pictureApplyInfoQuery.getApplyType();
        queryWrapper.eq(StringUtils.isNotEmpty(applyType), "apply_type", applyType);

        String userId = pictureApplyInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        Date createTime = pictureApplyInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = pictureApplyInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String reviewStatus = pictureApplyInfoQuery.getReviewStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(reviewStatus), "review_status", reviewStatus);

        Long reviewUserId = pictureApplyInfoQuery.getReviewUserId();
        queryWrapper.eq(StringUtils.isNotNull(reviewUserId), "review_user_id", reviewUserId);

        Date reviewTime = pictureApplyInfoQuery.getReviewTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginReviewTime")) && StringUtils.isNotNull(params.get("endReviewTime")), "review_time", params.get("beginReviewTime"), params.get("endReviewTime"));

        return queryWrapper;
    }

    @Override
    public List<PictureApplyInfoVo> convertVoList(List<PictureApplyInfo> pictureApplyInfoList) {
        if (StringUtils.isEmpty(pictureApplyInfoList)) {
            return Collections.emptyList();
        }
        return pictureApplyInfoList.stream().map(PictureApplyInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public int userInsertPictureApplyInfo(PictureApplyInfo pictureApplyInfo) {
        //如果传过来的积分不为空，判断是否为10的倍数或者0
        ThrowUtils.throwIf(StringUtils.isNotNull(pictureApplyInfo.getPointsNeed())
                && pictureApplyInfo.getPointsNeed() % 10 != 0
                || pictureApplyInfo.getPointsNeed() < 0, "积分必须是10的倍数或者0");
        //判断图片是否存在，获取缩略图url
        PictureInfo pictureInfo = pictureInfoService.selectNormalPictureInfoByPictureId(pictureApplyInfo.getPictureId());
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo),
                "图片不存在");
        //如果当前拥有未审核的申请信息，则不允许再次申请
        List<PictureApplyInfo> applyInfos = this.list(new LambdaQueryWrapper<PictureApplyInfo>().eq(PictureApplyInfo::getPictureId, pictureApplyInfo.getPictureId()).eq(PictureApplyInfo::getReviewStatus, PPictureApplyStatusEnum.PICTURE_APPLY_STATUS_0.getValue()));
        ThrowUtils.throwIf(StringUtils.isNotEmpty(applyInfos), "您当前有未审核的申请信息，请勿重复申请");
        //查询图片空间是否是团队空间
        SpaceInfo spaceInfo = spaceInfoService.selectNormalSpaceInfoBySpaceId(pictureInfo.getSpaceId());
        //如果是团队空间
        if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_1.getValue())) {
            ThrowUtils.throwIf(!spaceAuthUtils.checkSpaceEditPerm(spaceInfo.getSpaceId()),
                    "您当前没有权限操作此图片");
        }
        //如果是个人空间或者团队空间
        else {
            ThrowUtils.throwIf(!pictureInfo.getUserId().equals(pictureApplyInfo.getUserId()), "图片不存在！！！");
        }
        ThrowUtils.throwIf(pictureInfo.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue()), "当前图片已经公开！！！");
        pictureApplyInfo.setThumbnailUrl(pictureInfo.getThumbnailUrl());
        pictureApplyInfo.setPictureName(pictureInfo.getName());
        //初始值
        pictureApplyInfo.setCreateTime(DateUtils.getNowDate());
        pictureApplyInfo.setReviewStatus(PPictureApplyStatusEnum.PICTURE_APPLY_STATUS_0.getValue());
        //更新文件日志信息
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.updateNormalPictureApplyFileLog(pictureApplyInfo));
        return this.save(pictureApplyInfo) ? 1 : 0;
    }

}
