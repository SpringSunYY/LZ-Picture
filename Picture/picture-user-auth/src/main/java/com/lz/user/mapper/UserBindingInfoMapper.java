package com.lz.user.mapper;

import java.util.List;
import com.lz.user.model.domain.UserBindingInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户第三方账号绑定Mapper接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface UserBindingInfoMapper extends BaseMapper<UserBindingInfo>
{
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
     * 删除用户第三方账号绑定
     *
     * @param bindingId 用户第三方账号绑定主键
     * @return 结果
     */
    public int deleteUserBindingInfoByBindingId(String bindingId);

    /**
     * 批量删除用户第三方账号绑定
     *
     * @param bindingIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserBindingInfoByBindingIds(String[] bindingIds);
}
