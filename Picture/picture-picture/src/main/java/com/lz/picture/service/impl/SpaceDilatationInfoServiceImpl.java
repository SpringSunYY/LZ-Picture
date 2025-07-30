package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomSort;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.config.model.enmus.CTemplateTypeEnum;
import com.lz.picture.mapper.SpaceDilatationInfoMapper;
import com.lz.picture.model.domain.SpaceDilatationInfo;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.spaceDilatationInfo.SpaceDilatationInfoQuery;
import com.lz.picture.model.enums.PSpaceDilatationTypeEnum;
import com.lz.picture.model.enums.PSpaceTypeEnum;
import com.lz.picture.model.vo.spaceDilatationInfo.SpaceDilatationInfoCalculationVo;
import com.lz.picture.model.vo.spaceDilatationInfo.SpaceDilatationInfoVo;
import com.lz.picture.service.ISpaceDilatationInfoService;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.model.enums.PoPointsUsageLogTypeEnum;
import com.lz.points.model.enums.PoPointsUsageTypeEnum;
import com.lz.points.service.IAccountInfoService;
import com.lz.points.service.IPointsUsageLogInfoService;
import com.lz.user.manager.UserAsyncManager;
import com.lz.user.manager.factory.InformInfoAsyncFactory;
import com.lz.user.model.enums.UInformTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.config.TemplateInfoKeyConstants.PICTURE_SPACE_DILATATION_SUCCESS;
import static com.lz.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;
import static com.lz.config.utils.ConfigInfoUtils.*;

/**
 * 空间扩容信息Service业务层处理
 *
 * @author YY
 * @date 2025-06-28
 */
@Service
public class SpaceDilatationInfoServiceImpl extends ServiceImpl<SpaceDilatationInfoMapper, SpaceDilatationInfo> implements ISpaceDilatationInfoService {
    @Resource
    private SpaceDilatationInfoMapper spaceDilatationInfoMapper;

    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    private IAccountInfoService accountInfoService;

    @Resource
    private IPointsUsageLogInfoService pointsUsageLogInfoService;

    @Resource
    private TransactionTemplate transactionTemplate;

    //region mybatis代码

    /**
     * 查询空间扩容信息
     *
     * @param dilatationId 空间扩容信息主键
     * @return 空间扩容信息
     */
    @Override
    public SpaceDilatationInfo selectSpaceDilatationInfoByDilatationId(String dilatationId) {
        return spaceDilatationInfoMapper.selectSpaceDilatationInfoByDilatationId(dilatationId);
    }

    /**
     * 查询空间扩容信息列表
     *
     * @param spaceDilatationInfo 空间扩容信息
     * @return 空间扩容信息
     */
    @CustomSort(sortFields = {"createTime", "pointsTotal", "dilatationTotal", "dilatationUnit"},
            sortMappingFields = {"create_time", "points_total", "dilatation_total", "dilatation_unit"})
    @Override
    public List<SpaceDilatationInfo> selectSpaceDilatationInfoList(SpaceDilatationInfo spaceDilatationInfo) {
        return spaceDilatationInfoMapper.selectSpaceDilatationInfoList(spaceDilatationInfo);
    }

    /**
     * 新增空间扩容信息
     *
     * @param spaceDilatationInfo 空间扩容信息
     * @return 结果
     */
    @Override
    public int insertSpaceDilatationInfo(SpaceDilatationInfo spaceDilatationInfo) {
        spaceDilatationInfo.setCreateTime(DateUtils.getNowDate());
        return spaceDilatationInfoMapper.insertSpaceDilatationInfo(spaceDilatationInfo);
    }

    /**
     * 修改空间扩容信息
     *
     * @param spaceDilatationInfo 空间扩容信息
     * @return 结果
     */
    @Override
    public int updateSpaceDilatationInfo(SpaceDilatationInfo spaceDilatationInfo) {
        return spaceDilatationInfoMapper.updateSpaceDilatationInfo(spaceDilatationInfo);
    }

    /**
     * 批量删除空间扩容信息
     *
     * @param dilatationIds 需要删除的空间扩容信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceDilatationInfoByDilatationIds(String[] dilatationIds) {
        return spaceDilatationInfoMapper.deleteSpaceDilatationInfoByDilatationIds(dilatationIds);
    }

    /**
     * 删除空间扩容信息信息
     *
     * @param dilatationId 空间扩容信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceDilatationInfoByDilatationId(String dilatationId) {
        return spaceDilatationInfoMapper.deleteSpaceDilatationInfoByDilatationId(dilatationId);
    }

    //endregion
    @Override
    public QueryWrapper<SpaceDilatationInfo> getQueryWrapper(SpaceDilatationInfoQuery spaceDilatationInfoQuery) {
        QueryWrapper<SpaceDilatationInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = spaceDilatationInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String dilatationId = spaceDilatationInfoQuery.getDilatationId();
        queryWrapper.eq(StringUtils.isNotEmpty(dilatationId), "dilatation_id", dilatationId);

        String dilatationKey = spaceDilatationInfoQuery.getDilatationKey();
        queryWrapper.eq(StringUtils.isNotEmpty(dilatationKey), "dilatation_key", dilatationKey);

        String spaceId = spaceDilatationInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId), "space_id", spaceId);

        String spaceName = spaceDilatationInfoQuery.getSpaceName();
        queryWrapper.like(StringUtils.isNotEmpty(spaceName), "space_name", spaceName);

        String dilatationType = spaceDilatationInfoQuery.getDilatationType();
        queryWrapper.eq(StringUtils.isNotEmpty(dilatationType), "dilatation_type", dilatationType);

        Long pointsTotal = spaceDilatationInfoQuery.getPointsTotal();
        queryWrapper.eq(StringUtils.isNotNull(pointsTotal), "points_total", pointsTotal);

        String userId = spaceDilatationInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        Date createTime = spaceDilatationInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<SpaceDilatationInfoVo> convertVoList(List<SpaceDilatationInfo> spaceDilatationInfoList) {
        if (StringUtils.isEmpty(spaceDilatationInfoList)) {
            return Collections.emptyList();
        }
        return spaceDilatationInfoList.stream().map(SpaceDilatationInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public int userInsertSpaceDilatationInfo(SpaceDilatationInfo spaceDilatationInfo) {
        //首先校验用户是否输入密码
        String userId = spaceDilatationInfo.getUserId();
        ThrowUtils.throwIf(accountInfoService.getVerifyPassword(userId) != 1, "请输入密码");
        //校验空间是否存在是否是自己的
        SpaceInfo spaceInfo = spaceInfoService.selectNormalSpaceInfoBySpaceId(spaceDilatationInfo.getSpaceId());
        ThrowUtils.throwIf(StringUtils.isNull(spaceInfo) || !spaceInfo.getUserId().equals(userId), "空间不存在");
        //如果是个人空间，不可以扩容人数
        ThrowUtils.throwIf(spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_2.getValue())
                && spaceDilatationInfo.getDilatationType().equals(PSpaceDilatationTypeEnum.SPACE_DILATATION_TYPE_2.getValue()), "个人空间不可以扩容");
        //校验积分与用户余额
        String dilatationType = spaceDilatationInfo.getDilatationType();
        Long dilatationTotal = spaceDilatationInfo.getDilatationTotal();
        SpaceDilatationInfoCalculationVo spaceDilatationInfoCalculationVo = calculateDilatationPointTotal(dilatationTotal, dilatationType);
        Long pointTotal = spaceDilatationInfoCalculationVo.getPointsTotal();
        spaceDilatationInfo.setPointsTotal(pointTotal);
        AccountInfo accountInfo = accountInfoService.selectAccountInfoByUserId(userId);
        ThrowUtils.throwIf(accountInfo.getPointsBalance() < pointTotal, "积分余额不足");
        //赋值
        spaceDilatationInfo.setDilatationKey("DEFAULT"); //先写死，后续有套餐再重新计算
        Long dilatationUnit = spaceDilatationInfoCalculationVo.getDilatationUnit();
        spaceDilatationInfo.setDilatationUnit(dilatationUnit);
        spaceDilatationInfo.setCreateTime(DateUtils.getNowDate());
        spaceDilatationInfo.setSpaceName(spaceInfo.getSpaceName());
        spaceDilatationInfo.setThumbnailUrl(spaceInfo.getSpaceAvatar());
        String unit = "";
        String total = "";
        //空间赋值
        if (dilatationType.equals(PSpaceDilatationTypeEnum.SPACE_DILATATION_TYPE_0.getValue())) {
            //单位是GB
            spaceInfo.setMaxSize(spaceInfo.getMaxSize() + dilatationTotal * 1024 * 1024 * 1024);
            unit = dilatationUnit + "积分/1GB";
            total = dilatationTotal + "GB";
        } else if (dilatationType.equals(PSpaceDilatationTypeEnum.SPACE_DILATATION_TYPE_1.getValue())) {
            spaceInfo.setMaxCount(spaceInfo.getMaxCount() + dilatationTotal);
            unit = dilatationUnit + "积分/1个";
            total = dilatationTotal + "个";
        } else if (dilatationType.equals(PSpaceDilatationTypeEnum.SPACE_DILATATION_TYPE_2.getValue())) {
            spaceInfo.setMemberLimit(spaceInfo.getMemberLimit() + dilatationTotal);
            unit = dilatationUnit + "积分/1人";
            total = dilatationTotal + "人";
        }

        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        Integer execute = transactionTemplate.execute(res -> {
            spaceDilatationInfoMapper.insert(spaceDilatationInfo);
            spaceInfoService.updateById(spaceInfo);
            return pointsUsageLogInfoService.updateAccountByPointsRechargeInfo(
                    userId,
                    null,
                    PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_1.getValue(),
                    PoPointsUsageTypeEnum.POINTS_USAGE_TYPE_1.getValue(),
                    spaceDilatationInfo.getDilatationId(),
                    -pointTotal,
                    deviceInfo
            );
        });
        if (StringUtils.isNotNull(execute) && execute == 1) {
            spaceInfoService.deleteSpacePersonalTableCacheByUserId(spaceDilatationInfo.getUserId());
            //发送消息
            /*
                {
                  "spaceName": "我的私人空间",
                  "dilatationType": "容量扩容",
                  "dilatationUnit": "10积分/GB",
                  "dilatationTotal": "5GB",
                  "pointsTotal": "50",
                  "createTime": "2025-06-26 18:45:00"
                }
             */
            HashMap<String, String> params = new HashMap<>();
            params.put("spaceName", spaceInfo.getSpaceName());
            Optional<PSpaceDilatationTypeEnum> enumByValue = PSpaceDilatationTypeEnum.getEnumByValue(spaceDilatationInfo.getDilatationType());
            if (enumByValue.isPresent()) {
                params.put("dilatationType", enumByValue.get().getLabel());
            } else {
                params.put("dilatationType", "未知类型"); // 默认值
            }
            params.put("dilatationTotal", total);
            params.put("dilatationUnit", unit);
            params.put("pointsTotal", String.valueOf(pointTotal));
            params.put("createTime", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, spaceDilatationInfo.getCreateTime()));
            UserAsyncManager.me().execute(InformInfoAsyncFactory.sendInform(
                    userId,
                    PICTURE_SPACE_DILATATION_SUCCESS,
                    null,
                    CTemplateTypeEnum.TEMPLATE_TYPE_3.getValue(),
                    UInformTypeEnum.INFORM_TYPE_0.getValue(),
                    params
            ));
            //删除空间缓存
            spaceInfoService.deleteSpacePersonalTableCacheByUserId(spaceDilatationInfo.getUserId());
            spaceInfoService.deleteSpaceTeamTableCacheByUserId(spaceDilatationInfo.getUserId());
            return execute;
        } else {
            return 0;
        }
    }

    @Override
    public SpaceDilatationInfoCalculationVo calculateDilatationPointTotal(Long dilatationTotal, String dilatationType) {
        Long dilatationUnit = 0L;
        //获取到对应的价格
        if (dilatationType.equals(PSpaceDilatationTypeEnum.SPACE_DILATATION_TYPE_0.getValue())) {
            dilatationUnit = PICTURE_SPACE_DILATATION_SIZE_VALUE;
        } else if (dilatationType.equals(PSpaceDilatationTypeEnum.SPACE_DILATATION_TYPE_1.getValue())) {
            dilatationUnit = PICTURE_SPACE_DILATATION_COUNT_VALUE;
        } else if (dilatationType.equals(PSpaceDilatationTypeEnum.SPACE_DILATATION_TYPE_2.getValue())) {
            dilatationUnit = PICTURE_SPACE_DILATATION_MEMBER_VALUE;
        } else {
            throw new ServiceException("请选择扩容类型");
        }
        long pointTotal = dilatationTotal * dilatationUnit;
        ThrowUtils.throwIf(pointTotal <= 0, "请选择扩容数量");
        return new SpaceDilatationInfoCalculationVo(dilatationType, dilatationUnit, dilatationTotal, pointTotal);
    }
}
