package com.lz.user.service;

import java.util.List;
import com.lz.user.model.domain.UserBindingInfo;
import com.lz.user.model.vo.userBindingInfo.UserBindingInfoVo;
import com.lz.user.model.dto.userBindingInfo.UserBindingInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户第三方账号绑定Service接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface IUserBindingInfoService extends IService<UserBindingInfo>
{
    //region mybatis代码
    /**
     * 查询用户第三方账号绑定
     *
     * @param bindingId 用户第三方账号绑定主键
     * @return 用户第三方账号绑定
     */
    public UserBindingInfo selectUserBindingInfoByBindingId(String bindingId);

    /**
     * 查询用户第三方账号绑定列表
     *
     * @param userBindingInfo 用户第三方账号绑定
     * @return 用户第三方账号绑定集合
     */
    public List<UserBindingInfo> selectUserBindingInfoList(UserBindingInfo userBindingInfo);

    /**
     * 新增用户第三方账号绑定
     *
     * @param userBindingInfo 用户第三方账号绑定
     * @return 结果
     */
    public int insertUserBindingInfo(UserBindingInfo userBindingInfo);

    /**
     * 修改用户第三方账号绑定
     *
     * @param userBindingInfo 用户第三方账号绑定
     * @return 结果
     */
    public int updateUserBindingInfo(UserBindingInfo userBindingInfo);

    /**
     * 批量删除用户第三方账号绑定
     *
     * @param bindingIds 需要删除的用户第三方账号绑定主键集合
     * @return 结果
     */
    public int deleteUserBindingInfoByBindingIds(String[] bindingIds);

    /**
     * 删除用户第三方账号绑定信息
     *
     * @param bindingId 用户第三方账号绑定主键
     * @return 结果
     */
    public int deleteUserBindingInfoByBindingId(String bindingId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param userBindingInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<UserBindingInfo> getQueryWrapper(UserBindingInfoQuery userBindingInfoQuery);

    /**
     * 转换vo
     *
     * @param userBindingInfoList UserBindingInfo集合
     * @return UserBindingInfoVO集合
     */
    List<UserBindingInfoVo> convertVoList(List<UserBindingInfo> userBindingInfoList);
}
