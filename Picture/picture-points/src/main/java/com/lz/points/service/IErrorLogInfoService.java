package com.lz.points.service;

import java.util.List;

import com.alipay.api.AlipayApiException;
import com.lz.points.model.domain.ErrorLogInfo;
import com.lz.points.model.vo.errorLogInfo.ErrorLogInfoVo;
import com.lz.points.model.dto.errorLogInfo.ErrorLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 异常捕获Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IErrorLogInfoService extends IService<ErrorLogInfo> {
    //region mybatis代码

    /**
     * 查询异常捕获
     *
     * @param errorId 异常捕获主键
     * @return 异常捕获
     */
    public ErrorLogInfo selectErrorLogInfoByErrorId(String errorId);

    /**
     * 查询异常捕获列表
     *
     * @param errorLogInfo 异常捕获
     * @return 异常捕获集合
     */
    public List<ErrorLogInfo> selectErrorLogInfoList(ErrorLogInfo errorLogInfo);

    /**
     * 新增异常捕获
     *
     * @param errorLogInfo 异常捕获
     * @return 结果
     */
    public int insertErrorLogInfo(ErrorLogInfo errorLogInfo);

    /**
     * 修改异常捕获
     *
     * @param errorLogInfo 异常捕获
     * @return 结果
     */
    public int updateErrorLogInfo(ErrorLogInfo errorLogInfo);

    /**
     * 批量删除异常捕获
     *
     * @param errorIds 需要删除的异常捕获主键集合
     * @return 结果
     */
    public int deleteErrorLogInfoByErrorIds(String[] errorIds);

    /**
     * 删除异常捕获信息
     *
     * @param errorId 异常捕获主键
     * @return 结果
     */
    public int deleteErrorLogInfoByErrorId(String errorId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param errorLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<ErrorLogInfo> getQueryWrapper(ErrorLogInfoQuery errorLogInfoQuery);

    /**
     * 转换vo
     *
     * @param errorLogInfoList ErrorLogInfo集合
     * @return ErrorLogInfoVO集合
     */
    List<ErrorLogInfoVo> convertVoList(List<ErrorLogInfo> errorLogInfoList);


    /**
     * 保存异常记录
     *
     * @param userId      用户编号
     * @param paymentType 支付方式
     * @param payType     支付类型（渠道）
     * @param errorType   异常类型
     * @param orderType   订单类型
     * @param errorCode   异常Code
     * @param errorMsg    异常消息
     * @param result      完整异常信息
     * @return int
     * @author: YY
     * @method: saveErrorLogInfo
     * @date: 2025/5/20 00:12
     **/
    int saveErrorLogInfo(String userId, String paymentType, String payType, String orderType, String errorType, String errorCode, String errorMsg, Object result);
}
